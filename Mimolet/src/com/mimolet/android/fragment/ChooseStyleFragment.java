package com.mimolet.android.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.mimolet.android.R;
import com.mimolet.android.adapter.TextWatcherAdapter;

import entity.Binding;
import entity.Order;
import entity.Size;

public class ChooseStyleFragment extends Fragment {

  private Order order;

  public Order getOrder() {
    return order;
  }

  private String[] bindingArray = { "Мягкий на скобе" };

  private String[] sizeArray = { "20x20" };

  public void setOrder(Order order) {
    this.order = order;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_choose_style, container, false);

    // Set book name in order.
    final EditText bookName = (EditText) view.findViewById(R.id.bookNameField);
    bookName.addTextChangedListener(new TextWatcherAdapter() {
      @Override
      public void onTextChanged(final CharSequence s, int start, int before, int count) {
        if (s.length() > 40) {
          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
          builder.setMessage(R.string.albom_name_warning)
              .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  bookName.setText(s.subSequence(0, 40));
                }
              });
          AlertDialog alert = builder.create();
          alert.show();
        } else {
          order.setDescription(s.toString());
        }
      }
    });
    ArrayAdapter<String> bindingAdapter =
        new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, bindingArray);

    // Create book binding spinner and set binding in order.
    final Spinner bindingSpinner = (Spinner) view.findViewById(R.id.bindingSpinner);
    bindingSpinner.setAdapter(bindingAdapter);

    /*
     * TODO Yurii, do this sh1t better then now!
     */

    bindingSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (bindingSpinner.getSelectedItemPosition() == 0) {
          order.setBinding(Binding.SOFT_ON_SKOBA);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> arg0) {
      }
    });

    // Create book size spinner and set size in order.
    ArrayAdapter<String> sizeAdapter =
        new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, sizeArray);

    final Spinner sizeSpinner = (Spinner) view.findViewById(R.id.sizeSpinner);
    sizeSpinner.setAdapter(sizeAdapter);

    sizeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (sizeSpinner.getSelectedItem().equals("20x20")) {
          order.setPaper(Size.TWENTY_X_TWENTY);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> arg0) {
      }
    });
    return view;
  }
}
