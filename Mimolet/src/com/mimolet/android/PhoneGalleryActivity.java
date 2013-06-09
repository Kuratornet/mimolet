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
import com.mimolet.android.task.AuthorizationTask;
import com.mimolet.android.task.SendPDFTask;

public class PhoneGalleryActivity extends SherlockActivity {

	private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
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
				Document document = new Document(new Rectangle(566, 594));
				final String targetFile = android.os.Environment
						.getExternalStorageDirectory()
						+ java.io.File.separator + "Images.pdf";
				try {
					
					File f = new File(targetFile);
					if (!f.exists()) {
						f.createNewFile();
					}
					PdfWriter.getInstance(document, new FileOutputStream(
							targetFile));
					document.open();
					for (Bitmap bitmap : bitmaps) {
						// for (String path : urls) {
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.PNG /* FileType */,
								100 /* Ratio */, stream);
						Image png = Image.getInstance(stream.toByteArray());
						png.scaleAbsolute(566, 594);
						document.add(png);

						// PdfContentByte canvas = writer.getDirectContent();
						//
						// canvas.saveState();
						// canvas.setColorStroke(GrayColor.BLACK);
						// canvas.setColorFill(GrayColor.YELLOW);
						// canvas.rectangle(0, 0, 566, 100);
						// canvas.fillStroke();
						// String text = "ABCDE‚‡Ô‚‡F";
						// BaseFont bf = BaseFont.createFont();
						// canvas.beginText();
						// canvas.setColorStroke(GrayColor.BLACK);
						// canvas.setFontAndSize(bf, 22);
						// canvas.setTextMatrix(50, 50);
						// canvas.showText(text);
						// canvas.showTextAligned(PdfContentByte.ALIGN_CENTER,
						// text, 100, 100, 0);
						// canvas.endText();
						// canvas.restoreState();
						// Image image = Image.getInstance(path);
						// image.scaleAbsolute(566, 594);
						// document.add(image);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				document.close();
				new SendPDFTask(PhoneGalleryActivity.this, targetFile).execute(new Void[0]);
			}
//			new send().execute(loginField.getText().toString(),
//					passwordField.getText().toString(), serverUrl);
		});
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
			final Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
			imageView.setImageBitmap(bitmap);
			bitmaps.add(bitmap);
			urls.add(selectedImage.getPath());
		}
		// System.out.println(data.getDataString());
		// System.out.println(requestCode);
		// System.out.println(resultCode);
		// // if (Intent.ACTION_SEND_MULTIPLE.equals(getIntent().getAction())
		// // && getIntent().hasExtra(Intent.EXTRA_STREAM)) {
		// if (requestCode == RESULT_LOAD_IMAGE) {
		// ArrayList<Parcelable> list = getIntent()
		// .getParcelableArrayListExtra(Intent.EXTRA_STREAM);
		// for (Parcelable parcel : list) {
		// Uri selectedImage = (Uri) parcel;
		// String sourcepath = getPath(selectedImage);
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		// System.out.println(sourcepath);
		// Cursor cursor = getContentResolver().query(selectedImage,
		// filePathColumn, null, null, null);
		// cursor.moveToFirst();
		//
		// int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		// String picturePath = cursor.getString(columnIndex);
		// cursor.close();
		//
		// ImageView imageView = (ImageView) findViewById(R.id.imgView);
		// imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		// }
		// finish();
		// }
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