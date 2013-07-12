package com.mimolet.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mimolet.android.Constants;
import com.mimolet.android.PhoneGalleryActivity;

import entity.Order;

public class AddPhotoFragment extends ListFragment {

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
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        getActivity(), android.R.layout.simple_list_item_1,
        listviewItems);
    setListAdapter(adapter);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    // XXX find correct way
    switch (position) {
    case 0:
      final Intent intent = new Intent(getActivity()
          .getApplicationContext(), PhoneGalleryActivity.class);
      final Bundle bundle = new Bundle();
      bundle.putSerializable(Constants.BUNDLE_ORDER, order);
      intent.putExtras(bundle);
      startActivity(intent);
//		  final AddPhotoFragment addPhotoFragment = new AddPhotoFragment();
//      switchFragment(addPhotoFragment);
      break;
    case 1:
    case 2:
    case 3:
    default:
      System.out.println("Only first tab is implemented yet");
      break;
    }
  }
}
