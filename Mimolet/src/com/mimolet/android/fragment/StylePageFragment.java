package com.mimolet.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mimolet.android.R;

import entity.Order;

public class StylePageFragment extends Fragment {

	private Order order;

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_style_page,
				container, false);
		return view;
	}
}
