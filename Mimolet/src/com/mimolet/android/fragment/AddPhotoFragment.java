package com.mimolet.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mimolet.android.R;
import com.mimolet.android.global.GlobalVariables;
import com.mimolet.android.multiphoto.MultiPhotoSelectActivity;

import entity.Order;

public class AddPhotoFragment extends Fragment {

	LinearLayout myGallery;

	private Order order;

	private final String[] listviewItems = new String[] { "Phone memory",
			"Facebook", "Instagram", "Google+" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_add_photo,
				container, false);
		ListView list = (ListView) view.findViewById(R.id.listview);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (position) {
				case 0:
					final Intent intent = new Intent(getActivity()
							.getApplicationContext(),
							MultiPhotoSelectActivity.class);
					final Bundle bundle = new Bundle();
					bundle.putSerializable(GlobalVariables.BUNDLE_ORDER, order);
					intent.putExtras(bundle);
					startActivityForResult(intent, 1);
					break;
				case 1:
				case 2:
				case 3:
				default:
					Toast.makeText(view.getContext(),
							R.string.loose_internet_connection,
							Toast.LENGTH_LONG).show();
					break;
				}

			}
		});

		list.setAdapter(new ArrayAdapter<String>(view.getContext(),
				R.layout.custom_list_view, listviewItems));
		myGallery = (LinearLayout) view.findViewById(R.id.mygallery);
		return view;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}
}
