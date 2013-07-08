package com.mimolet.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import com.mimolet.android.task.SendPDFTask;

import entity.Order;

public class PhoneGalleryActivity extends SherlockActivity {

	private List<String> urls = new ArrayList<String>();

	private static int RESULT_LOAD_IMAGE = 666;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_gallery);

		Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
		buttonLoadImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});

		final Button createPDF = (Button) findViewById(R.id.buttonCreatePdf);
		createPDF.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final int SIDE_SIZE = 576;
				final int BORDER_SIZE = 43;
				Document document = new Document(new Rectangle(SIDE_SIZE,
						SIDE_SIZE));
				document.setMargins(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE,
						BORDER_SIZE);
				final String targetFile = android.os.Environment
						.getExternalStorageDirectory()
						+ java.io.File.separator
						+ "Images.pdf";
				final String previewFile = android.os.Environment
						.getExternalStorageDirectory()
						+ java.io.File.separator
						+ "preview.png";
				try {

					File f = new File(targetFile);
					if (!f.exists()) {
						f.createNewFile();
					}
					File preview = new File(previewFile);
					if (!preview.exists()) {
						preview.createNewFile();
					}
					final String previewUrl = urls.get(0);
					final Bitmap previewBitmap = decodeSampledBitmapFromFile(
							previewUrl, 200, 200);
					FileOutputStream fOut = new FileOutputStream(preview);

					previewBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
					fOut.flush();
					fOut.close();
					previewBitmap.recycle();
					PdfWriter.getInstance(document, new FileOutputStream(
							targetFile));
					document.open();
					for (String url : urls) {
						final Bitmap bitmap = decodeSampledBitmapFromFile(url,
								SIDE_SIZE, SIDE_SIZE);
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.PNG /* FileType */,
								100 /* Ratio */, stream);
						Image png = Image.getInstance(stream.toByteArray());
						png.setAlignment(Image.ALIGN_MIDDLE);
						png.scaleToFit(SIDE_SIZE - 2 * BORDER_SIZE, SIDE_SIZE
								- 2 * BORDER_SIZE);
						document.add(png);
						bitmap.recycle();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				document.close();
				new SendPDFTask(PhoneGalleryActivity.this, targetFile,
						previewFile, (Order) getIntent().getSerializableExtra(
								Constants.BUNDLE_ORDER)).execute(new Void[0]);
			}
		});
	}

	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	private Bitmap decodeSampledBitmapFromFile(String filepath, int reqWidth,
			int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filepath, options);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			ImageView imageView = (ImageView) findViewById(R.id.imgView);

			final Bitmap bitmap = decodeSampledBitmapFromFile(picturePath,
					1024, 1024);
			imageView.setImageBitmap(bitmap);
			urls.add(picturePath);
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		startManagingCursor(cursor);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
}
