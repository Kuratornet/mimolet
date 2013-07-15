package com.mimolet.android;

import java.io.File;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.mimolet.android.fragment.AddPhotoFragment;
import com.mimolet.android.fragment.ChooseStyleFragment;
import com.mimolet.android.fragment.GalleryPhotoFragment;
import com.mimolet.android.fragment.StylePageFragment;
import com.mimolet.android.global.GlobalVariables;

import entity.Order;

public class AddBookActivity extends SherlockFragmentActivity {
  private static final String TAG = "AddBookActivity";

  /* These numbers should correlate with tab order */
  private static final int CHOOSE_STYLE_TAB = 1;
  private static final int ADD_PHOTO_TAB = 2;
  private static final int STYLE_PAGE_TAB = 3;
  private static final int PREVIEW_TAB = 4;

  private ChooseStyleFragment chooseStyleFragment;
  private AddPhotoFragment addPhotoFragment;
  private StylePageFragment stylePageFragment;

  private GestureDetector gestureDetector;
  View.OnTouchListener gestureListener;

  private Order order;

  private int selectedTab;

  private final BottomMenu bottomMenu = new BottomMenu();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    order = new Order();
    chooseStyleFragment = new ChooseStyleFragment();
    addPhotoFragment = new AddPhotoFragment();
    stylePageFragment = new StylePageFragment();
    chooseStyleFragment.setOrder(order);
    addPhotoFragment.setOrder(order);
    stylePageFragment.setOrder(order);

    setContentView(R.layout.activity_add_book);
    final ActionBar actionbar = getSupportActionBar();
    actionbar.setDisplayShowTitleEnabled(false);
    actionbar.setDisplayShowHomeEnabled(false);
    actionbar.setDisplayShowTitleEnabled(true);
    actionbar.setTitle(R.string.creating_book);

    chooseStyleTabClick(null);

    gestureDetector = new GestureDetector(this, new SwipeGestureDetector());
    gestureListener = new View.OnTouchListener() {
      public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
      }
    };
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

  public void switchFragment(Fragment fragment) {
    final FragmentTransaction fragmentTransaction = getSupportFragmentManager()
        .beginTransaction();
    fragmentTransaction.replace(R.id.fragment_container, fragment);
    fragmentTransaction.commit();
    getSupportFragmentManager().executePendingTransactions();
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

      switchFragment(addPhotoFragment);
    }
  }

  public void stylePageTabClick(View view) {
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

      switchFragment(stylePageFragment);

      loadPreviewImages();
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

  public void onChangeSequenceButtonClick(View view) {
    String selectedValue = "Functional isn`t rdy yet";
    Toast.makeText(this, selectedValue, Toast.LENGTH_LONG).show();
  }

  public void onLeftImageClick(View view) {
    String selectedValue = "Left image clicked";
    Toast.makeText(this, selectedValue, Toast.LENGTH_LONG).show();
  }

  public void onRightImageClick(View view) {
    String selectedValue = "Right image clicked";
    Toast.makeText(this, selectedValue, Toast.LENGTH_LONG).show();
  }

  private void loadPreviewImages() {
    final ImageView bookCover = (ImageView) stylePageFragment.getView()
        .findViewById(R.id.previewImage);
    final ImageView leftImage = (ImageView) stylePageFragment.getView()
        .findViewById(R.id.leftImage);
    final ImageView rightImage = (ImageView) stylePageFragment.getView()
        .findViewById(R.id.rightImage);

    final File imageFolder = new File(GlobalVariables.IMAGE_FOLDER);
    stylePageFragment.setImagePathes(imageFolder.list());
    ImageUtils.loadImage(bookCover, GlobalVariables.IMAGE_FOLDER
        + stylePageFragment.getPreviewImagePath());

    leftImage.setOnTouchListener(gestureListener);
    rightImage.setOnTouchListener(gestureListener);
    
    loadLeftRightImages();
  }
  
  private void loadLeftRightImages() {
    final ImageView leftImage = (ImageView) stylePageFragment.getView()
        .findViewById(R.id.leftImage);
    final ImageView rightImage = (ImageView) stylePageFragment.getView()
        .findViewById(R.id.rightImage);
    ImageUtils.loadImage(leftImage, GlobalVariables.IMAGE_FOLDER
        + stylePageFragment.getLeftImagePath());
    ImageUtils.loadImage(rightImage, GlobalVariables.IMAGE_FOLDER
        + stylePageFragment.getRightImagePath());
  }
  
  private void onLeftSwipe() {
    stylePageFragment.flipImagesRight();
    loadLeftRightImages();
  }
  
  private void onRightSwipe() {
    stylePageFragment.flipImagesLeft();
    loadLeftRightImages();
  }

  class SwipeGestureDetector extends SimpleOnGestureListener {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
        float velocityY) {
      try {
        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
          return false;
        // right to left swipe
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
          onLeftSwipe();
        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
          onRightSwipe();
        }
      } catch (Exception e) {
        // nothing
      }
      return false;
    }
  }
}
