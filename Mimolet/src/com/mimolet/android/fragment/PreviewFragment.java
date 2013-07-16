package com.mimolet.android.fragment;

import com.mimolet.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import entity.Order;

public class PreviewFragment extends Fragment {

	private Order order;

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_preview,
				container, false);
		return view;
	}
}
