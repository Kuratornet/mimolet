package com.mimolet.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.mimolet.android.fragment.AddPhotoFragment;
import com.mimolet.android.fragment.ChooseStyleFragment;

import entity.Order;

public class AddBookActivity extends SherlockFragmentActivity {
  private static final String TAG = "AddBookActivity";

  /* These numbers should correlate with tab order */
  private static final int CHOOSE_STYLE_TAB = 1;
  private static final int ADD_PHOTO_TAB = 2;
  private static final int STYLE_PAGE_TAB = 3;
  private static final int PREVIEW_TAB = 4;

  private Order order;

  private int selectedTab;

  private final BottomMenu bottomMenu = new BottomMenu();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    order = new Order();

    setContentView(R.layout.activity_add_book);
    final ActionBar actionbar = getSupportActionBar();
    actionbar.setDisplayShowTitleEnabled(false);
    actionbar.setDisplayShowHomeEnabled(false);
    actionbar.setDisplayShowTitleEnabled(true);
    actionbar.setTitle(R.string.creating_book);

    chooseStyleTabClick(null);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    final MenuInflater inflater = getSupportMenuInflater();
    inflater.inflate(R.menu.add_book, menu);
    return super.onCreateOptionsMenu(menu);
  }

  private boolean isTabAccessible(int tabIndex) {
    return selectedTab == tabIndex - 1 || selectedTab > tabIndex;
  }

  private void switchFragment(Fragment fragment) {
    final FragmentTransaction fragmentTransaction = getSupportFragmentManager()
        .beginTransaction();
    fragmentTransaction.replace(R.id.fragment_container, fragment);
    fragmentTransaction.commit();
  }

  public void chooseStyleTabClick(View view) {
    if (isTabAccessible(CHOOSE_STYLE_TAB)) {
      selectedTab = CHOOSE_STYLE_TAB;
      final ImageButton chooseStyleTab = (ImageButton) findViewById(R.id.chooseStyleTab);
      chooseStyleTab
          .setBackgroundResource(R.drawable.choose_style_tab_selected);
      final ImageButton addPhotoTab = (ImageButton) findViewById(R.id.addPhotoTab);
      addPhotoTab.setBackgroundResource(R.drawable.add_photo_tab);
      final ImageButton stylePageTab = (ImageButton) findViewById(R.id.stylePageTab);
      stylePageTab.setBackgroundResource(R.drawable.style_page_tab);
      final ImageButton previewTab = (ImageButton) findViewById(R.id.previewTab);
      previewTab.setBackgroundResource(R.drawable.preview_tab);

      final ChooseStyleFragment chooseStyleFragment = new ChooseStyleFragment();
      chooseStyleFragment.setOrder(order);
      switchFragment(chooseStyleFragment);
    }
  }

  public void addPhotoTabClick(View view) {
    if (isTabAccessible(ADD_PHOTO_TAB)) {
      selectedTab = ADD_PHOTO_TAB;
      final ImageButton chooseStyleTab = (ImageButton) findViewById(R.id.chooseStyleTab);
      chooseStyleTab
          .setBackgroundResource(R.drawable.choose_style_tab_done);
      final ImageButton addPhotoTab = (ImageButton) findViewById(R.id.addPhotoTab);
      addPhotoTab
          .setBackgroundResource(R.drawable.add_photo_tab_selected);
      final ImageButton stylePageTab = (ImageButton) findViewById(R.id.stylePageTab);
      stylePageTab.setBackgroundResource(R.drawable.style_page_tab);
      final ImageButton previewTab = (ImageButton) findViewById(R.id.previewTab);
      previewTab.setBackgroundResource(R.drawable.preview_tab);

      final AddPhotoFragment addPhotoFragment = new AddPhotoFragment();
      addPhotoFragment.setOrder(order);
      switchFragment(addPhotoFragment);
    }
  }

  public void stylePageTabClick(View view) {
    String selectedValue = "Functional isn`t rdy yet";
    Toast.makeText(this, selectedValue, Toast.LENGTH_LONG).show();
    if (isTabAccessible(STYLE_PAGE_TAB)) {
      selectedTab = STYLE_PAGE_TAB;
      final ImageButton chooseStyleTab = (ImageButton) findViewById(R.id.chooseStyleTab);
      chooseStyleTab
          .setBackgroundResource(R.drawable.choose_style_tab_done);
      final ImageButton addPhotoTab = (ImageButton) findViewById(R.id.addPhotoTab);
      addPhotoTab.setBackgroundResource(R.drawable.add_photo_tab_done);
      final ImageButton stylePageTab = (ImageButton) findViewById(R.id.stylePageTab);
      stylePageTab
          .setBackgroundResource(R.drawable.style_page_tab_selected);
      final ImageButton previewTab = (ImageButton) findViewById(R.id.previewTab);
      previewTab.setBackgroundResource(R.drawable.preview_tab);
    }
  }

  public void previewTabClick(View view) {
    String selectedValue = "Functional isn`t rdy yet";
    Toast.makeText(this, selectedValue, Toast.LENGTH_LONG).show();
    if (isTabAccessible(PREVIEW_TAB)) {
      selectedTab = PREVIEW_TAB;
      final ImageButton chooseStyleTab = (ImageButton) findViewById(R.id.chooseStyleTab);
      chooseStyleTab
          .setBackgroundResource(R.drawable.choose_style_tab_done);
      final ImageButton addPhotoTab = (ImageButton) findViewById(R.id.addPhotoTab);
      addPhotoTab.setBackgroundResource(R.drawable.add_photo_tab_done);
      final ImageButton stylePageTab = (ImageButton) findViewById(R.id.stylePageTab);
      stylePageTab.setBackgroundResource(R.drawable.style_page_tab_done);
      final ImageButton previewTab = (ImageButton) findViewById(R.id.previewTab);
      previewTab.setBackgroundResource(R.drawable.preview_tab_selected);
    }
  }

  public void myOrders(View view) {
    bottomMenu.openMyOrders(this);
  }

  public void createBook(View view) {
    // already here, do nothing
  }

  public void paidOrders(View view) {
    bottomMenu.openMyPaidOrders(this);
  }
}
