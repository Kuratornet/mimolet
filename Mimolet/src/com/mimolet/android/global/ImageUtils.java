package com.mimolet.android.global;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * This class needs huge revision.
 * 
 * @author yshylov
 * 
 */
public class ImageUtils {

	public static void loadImage(ImageView view, String imagePath) {
		loadImage(view, imagePath, 300);
	}

	public static void loadImage(ImageView view, String imagePath, int size,
			boolean cutExcess) {
		view.setImageBitmap(decodeSampledBitmapFromFile(imagePath, size, size,
				cutExcess));
	}

	public static void loadImage(ImageView view, String imagePath, int size) {
		loadImage(view, imagePath, size, true);
	}

	public static Bitmap decodeSampledBitmapFromFile(String filepath,
			int reqWidth, int reqHeight, boolean stretchSmallerSide) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, options);
		if (stretchSmallerSide) {
			options.inSampleSize = calculateInSampleSize2(options, reqWidth,
					reqHeight);
		} else {
			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
		}

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filepath, options);
	}

	private static int calculateInSampleSize2(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
}
