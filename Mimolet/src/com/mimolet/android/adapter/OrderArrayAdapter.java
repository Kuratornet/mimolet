package com.mimolet.android.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.mimolet.android.R;


public class OrderArrayAdapter extends ArrayAdapter<String> {
  private static final String TAG = "OrderArrayAdapter";
  
  private final Context context;
  private final String[] values;
  private final String[] imageSources;
  private final String[] createData;
 
  public OrderArrayAdapter(Context context, int textViewResourceIdint, String[] values, String[] images, String[] createData) {
    super(context, textViewResourceIdint, values);
    this.context = context;
    this.values = values;
    this.imageSources = images;
    this.createData = createData;
  }
 
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
      .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
    View rowView = inflater.inflate(R.layout.order_in_list, parent, false);
    final TextView textView = (TextView) rowView.findViewById(R.id.label);
    final TextView dataLabel = (TextView) rowView.findViewById(R.id.descriptionlabel);
    final ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
    
    textView.setText(values[position]);
    dataLabel.setText(createData[position]);
    try {
      UrlImageViewHelper.setUrlDrawable(imageView, imageSources[position]);
    } catch (Exception e) {
      Log.e(TAG, e.toString());
      e.printStackTrace();
    }


    return rowView;
  }
  
}
