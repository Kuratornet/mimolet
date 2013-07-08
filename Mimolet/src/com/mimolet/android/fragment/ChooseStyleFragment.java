package com.mimolet.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mimolet.android.R;
import com.mimolet.android.TextWatcherAdapter;

import entity.Order;

public class ChooseStyleFragment extends Fragment {

	private Order order;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_choose_style,
				container, false);
		final EditText bookName = (EditText) view
				.findViewById(R.id.bookNameField);
		bookName.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				order.setDescription(s.toString());
			}
		});
		return view;
	}
}
