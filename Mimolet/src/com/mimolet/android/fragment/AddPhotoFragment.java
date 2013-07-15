package com.mimolet.android.fragment;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mimolet.android.ChoiseImagesActivity;
import com.mimolet.android.Constants;
import com.mimolet.android.R;
import com.mimolet.android.global.GlobalMethods;
import com.mimolet.android.global.GlobalVariables;
import com.mimolet.android.multiphoto.MultiPhotoSelectActivity;

import entity.Order;

public class AddPhotoFragment extends Fragment {

  LinearLayout myGallery;
  
  private int imageCounter = 0;
  
  private Order order;
  
  public void setOrder(Order order) {
    this.order = order;
  }

  public Order getOrder() {
    return order;
  }

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
      public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        switch (position) {
        case 0:
          final Intent intent = new Intent(getActivity()
              .getApplicationContext(), MultiPhotoSelectActivity.class);
          final Bundle bundle = new Bundle();
          bundle.putSerializable(Constants.BUNDLE_ORDER, order);
          intent.putExtras(bundle);
          startActivityForResult(intent, 1);
          break;
        case 1:
        case 2:
        case 3:
        default:
          System.out.println("Only first tab is implemented yet");
          break;
        }
        
      }
    });

    list.setAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.custom_list_view, listviewItems));
    myGallery = (LinearLayout) view.findViewById(R.id.mygallery);
    reviewFolder();
    return view;
  }
//  @Override
//  public void onActivityCreated(Bundle savedInstanceState) {
//    super.onActivityCreated(savedInstanceState);
//    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//        getActivity(), android.R.layout.simple_list_item_1,
//        listviewItems);
//    setListAdapter(adapter);
//    getActivity().setContentView(R.layout.fragment_add_photo);
//    myGallery = (LinearLayout) getActivity().findViewById(R.id.mygallery);
//    System.out.println(myGallery);
//    reviewFolder();
//  }
//
//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//      // XXX find correct way
//      switch (position) {
//      case 0:
//        final Intent intent = new Intent(getActivity()
//            .getApplicationContext(), ChoiseImagesActivity.class);
//        final Bundle bundle = new Bundle();
//        bundle.putSerializable(Constants.BUNDLE_ORDER, order);
//        intent.putExtras(bundle);
//        startActivity(intent);
//        break;
//      case 1:
//      case 2:
//      case 3:
//      default:
//        System.out.println("Only first tab is implemented yet");
//        break;
//      }
//    }
  
  private void reviewFolder () {
    final String imageFolderPath = GlobalVariables.IMAGE_FOLDER;
    final File imageFolder = new File(imageFolderPath);
    File[] listOfFiles = imageFolder.listFiles();
    if (listOfFiles.length > imageCounter) {
      for (int i = imageCounter; i < listOfFiles.length; i++) {
          if (listOfFiles[imageCounter].isFile()) {
            myGallery.addView(insertPhoto(listOfFiles[imageCounter].getAbsolutePath()));
          }
        imageCounter++;
      }
    }
  }
  
  private View insertPhoto(String path) {
    Bitmap bm = GlobalMethods.decodeSampledBitmapFromFile(path, 220, 165);

    LinearLayout layout = new LinearLayout(getActivity().getApplicationContext());
    layout.setLayoutParams(new LayoutParams(240, 180));
    layout.setGravity(Gravity.CENTER);

    ImageView imageView = new ImageView(getActivity().getApplicationContext());
    imageView.setLayoutParams(new LayoutParams(220, 165));
    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    imageView.setImageBitmap(bm);

    layout.addView(imageView);
    return layout;
  }
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    reviewFolder();
  }
}
