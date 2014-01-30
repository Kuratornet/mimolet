package com.mimolet.android.fragment;

import java.io.File;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimolet.android.R;

import entity.Order;

public class StylePageFragment extends Fragment {
	private static final String TAG = "StylePageFragment";

	private Order order;

	private String[] imagePathes;
	private File imageFolder;

	private int currentImageIndex = 0;

	public void setOrder(Order order) {
		this.order = order;
	}
	
	public Order getOrder() {
		return order;
	} 
	
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
