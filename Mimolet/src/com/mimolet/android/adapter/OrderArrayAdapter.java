package com.mimolet.android.adapter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mimolet.android.R;


public class OrderArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
	private final String[] imageSources;
 
	public OrderArrayAdapter(Context context, String[] values, String[] images) {
		super(context, R.layout.activity_orders_list, values);
		this.context = context;
		this.values = values;
		this.imageSources = images;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.activity_orders_list, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		
		textView.setText(values[position]);

		imageView.setImageDrawable(drawableFromUrl(imageSources[position]));

		return rowView;
	}
	
	@SuppressWarnings("deprecation")
	public static Drawable drawableFromUrl(String url) {
		Bitmap x = null;
		try {
	    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
	    connection.connect();
	    InputStream input = connection.getInputStream();

	    x = BitmapFactory.decodeStream(input);
	    
		} catch (Exception e) {
			Log.e("ERROR", e.toString());
		}
		return new BitmapDrawable(x);
	}
}
