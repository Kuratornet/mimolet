package com.mimolet.android.fragment;

import java.io.File;

import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mimolet.android.R;
import com.mimolet.android.global.GlobalVariables;

public class StylePageFragment extends FragmentWithPreviews {
	private static final String TAG = "StylePageFragment";

	private View thisView;

	private String[] imagePathes;
	private File imageFolder;

	private int currentImageIndex = 0;

	public void setImagePathes(File file) {
		imageFolder = file;
		this.imagePathes = imageFolder.list();
		Log.i(TAG, imagePathes.toString());

	}

	public String[] getImagePathes() {
		return imagePathes;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_style_page,
				container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.thisView = view;
	}

	public void previwImageCreate() {
		FrameLayout previewImageLayout = (FrameLayout) this.getView()
				.findViewById(R.id.previewImageFrame);
		previewImageLayout.setDrawingCacheEnabled(true);
		thisView.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		thisView.layout(0, 0, thisView.getMeasuredWidth(),
				thisView.getMeasuredHeight());
		previewImageLayout.buildDrawingCache();
		GlobalVariables.previewImageBitmap = previewImageLayout
				.getDrawingCache(true).copy(Config.RGB_565, false);
		previewImageLayout.destroyDrawingCache();
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

	@Override
	public String getLeftImagePath() {
		return imagePathes[currentImageIndex];
	}
	
	@Override
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
