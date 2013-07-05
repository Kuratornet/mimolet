package com.mimolet.android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
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
    Bitmap bmp;
    try {
//      URL url = new URL (imageSources[position]);
//      bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//      imageView.setImageBitmap(bmp);
      UrlImageViewHelper.setUrlDrawable(imageView, imageSources[position]);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


    return rowView;
  }
  
}
