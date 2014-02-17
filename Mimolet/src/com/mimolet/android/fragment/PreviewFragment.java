package com.mimolet.android.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import com.mimolet.android.AddBookActivity;
import com.mimolet.android.R;
import com.mimolet.android.global.GlobalVariables;
import com.mimolet.android.global.ImageUtils;
import com.mimolet.android.task.SendPDFTask;

public class PreviewFragment extends Fragment {

	private static final String TAG = "PreviewFragment";
	AddBookActivity parent;

	private String[] imagePathes;
	private File imageFolder;
	
	private int currentImageIndex = 0;
	
	public void setImagePathes(File file) {
		imageFolder = file;
		this.imagePathes = imageFolder.list();
		parent.getOrder().setPages(this.imagePathes.length);
		Log.i(TAG, imagePathes.toString());
		
	}

	public String[] getImagePathes() {
		return imagePathes;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.parent = (AddBookActivity) getActivity();
		final View view = inflater.inflate(R.layout.fragment_preview,
				container, false);
		final ImageButton createPDF = (ImageButton) view
				.findViewById(R.id.checkoutButton);
		createPDF.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (parent.getOrder().getDescription() == null || parent.getOrder().getDescription().length() == 0) {
					Toast.makeText(getActivity(), R.string.preview_noname_album, Toast.LENGTH_LONG).show();
					return;
				}
				if (parent.getOrder().getPages() < 20) {
					Toast.makeText(getActivity(), R.string.preview_less_then_twenty_photos, Toast.LENGTH_LONG).show();
					return;
				}
				final int SIDE_SIZE = 576;
				final int BORDER_SIZE = 43;
				Document document = new Document(new Rectangle(SIDE_SIZE,
						SIDE_SIZE));
				document.setMargins(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE,
						BORDER_SIZE);
				final String targetFile = GlobalVariables.MIMOLET_FOLDER + "Images.pdf";
				final String previewFile = GlobalVariables.MIMOLET_FOLDER + "preview.png";
				try {
					File f = new File(targetFile);
					if (!f.exists()) {
						f.createNewFile();
					}
					File preview = new File(previewFile);
					if (!preview.exists()) {
						preview.createNewFile();
					}
					FileOutputStream fOut = new FileOutputStream(preview);
					GlobalVariables.previewImageBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
					fOut.flush();
					fOut.close();
					PdfWriter.getInstance(document, new FileOutputStream(targetFile));
					document.open();
					final File workingFolder = new File(GlobalVariables.IMAGE_FOLDER);
					final File[] listOfFiles = workingFolder.listFiles();
					for (File file : listOfFiles) {
						String imageName = GlobalVariables.IMAGE_FOLDER
								+ file.getName();
						final Bitmap bitmap = ImageUtils
								.decodeSampledBitmapFromFile(imageName,
										SIDE_SIZE, SIDE_SIZE, false);
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

				final File toBeCleared = new File(GlobalVariables.IMAGE_FOLDER);
				File[] listOfFiles = toBeCleared.listFiles();
				for (File file : listOfFiles) {
					if (file.isFile()) {
						file.delete();
					}
				}

				Log.i(TAG, "If all ok start this");
				new SendPDFTask(getActivity(), targetFile, previewFile, parent.getOrder())
						.execute(new Void[0]);
			}
		});
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (imageFolder != null) {
			this.imagePathes = imageFolder.list();
		}
	}
	
	public String getPreviewImagePath() {
	  return imagePathes[0];
	}
	
	public String getLeftImagePath() {
	  return imagePathes[currentImageIndex];
	}
	
	public String getRightImagePath() {
	  return imagePathes[currentImageIndex + 1];
	}
	
	public void flipImagesRight() {
	  if (currentImageIndex + 2 < imagePathes.length) {
	    currentImageIndex += 2;
	  }
	}
	
	public void flipImagesLeft() {
	  if (currentImageIndex - 2 >= 0) {
		  currentImageIndex -= 2;
	  }
	}
}
