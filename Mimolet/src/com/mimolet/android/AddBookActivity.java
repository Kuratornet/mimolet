package com.mimolet.android;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mimolet.android.fragment.AddPhotoFragment;
import com.mimolet.android.fragment.ChooseStyleFragment;
import com.mimolet.android.fragment.FragmentWithPreviews;
import com.mimolet.android.fragment.PreviewFragment;
import com.mimolet.android.fragment.StylePageFragment;
import com.mimolet.android.global.GlobalVariables;
import com.mimolet.android.global.ImageUtils;
import com.mimolet.android.task.GetOrdersListTask;

import entity.Order;
import entity.PhotoData;

public class AddBookActivity extends FragmentActivity {

	/* These numbers should correlate with tab order */
	private static final int CHOOSE_STYLE_TAB = 1;
	private static final int ADD_PHOTO_TAB = 2;
	private static final int STYLE_PAGE_TAB = 3;
	private static final int PREVIEW_TAB = 4;

	private ChooseStyleFragment chooseStyleFragment;
	private AddPhotoFragment addPhotoFragment;
	private StylePageFragment stylePageFragment;
	private PreviewFragment previewFragment;
	private Fragment currentFragment;

	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	private Order order;

	private int selectedTab;

	private final BottomMenu bottomMenu = new BottomMenu();

	@SuppressLint("UseSparseArrays")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		order = new Order();
		GlobalVariables.imagesList = new ArrayList<Integer>();
		GlobalVariables.imagesListData = new HashMap<Integer, PhotoData>();
		File imagesDir = new File(GlobalVariables.IMAGE_FOLDER);
		if (imagesDir.exists()) {
			File[] files = imagesDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
		File previewsDir = new File(GlobalVariables.PREVIEW_FOLDER);
		if (previewsDir.exists()) {
			File[] files = previewsDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
		chooseStyleFragment = new ChooseStyleFragment();
		addPhotoFragment = new AddPhotoFragment();
		stylePageFragment = new StylePageFragment();
		previewFragment = new PreviewFragment();

		setContentView(R.layout.activity_add_book);

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
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_book, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private boolean isTabAccessible(int tabIndex) {
		return selectedTab == tabIndex - 1 || selectedTab > tabIndex;
	}

	public void switchFragment(Fragment fragment) {
		currentFragment = fragment;
		final FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, fragment);
		fragmentTransaction.commit();
		getSupportFragmentManager().executePendingTransactions();
		resizeScreenParts();
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
		File imageFolder = new File(GlobalVariables.IMAGE_FOLDER);
		imageFolder.mkdirs();
		if (imageFolder.exists()) {
			File[] files = imageFolder.listFiles();
			if (files.length != 0) {
				if (isTabAccessible(STYLE_PAGE_TAB)) {
					selectedTab = STYLE_PAGE_TAB;
					final ImageButton chooseStyleTab = (ImageButton) findViewById(R.id.chooseStyleTab);
					chooseStyleTab
							.setBackgroundResource(R.drawable.choose_style_tab_done);
					final ImageButton addPhotoTab = (ImageButton) findViewById(R.id.addPhotoTab);
					addPhotoTab
							.setBackgroundResource(R.drawable.add_photo_tab_done);
					final ImageButton stylePageTab = (ImageButton) findViewById(R.id.stylePageTab);
					stylePageTab
							.setBackgroundResource(R.drawable.style_page_tab_selected);
					final ImageButton previewTab = (ImageButton) findViewById(R.id.previewTab);
					previewTab.setBackgroundResource(R.drawable.preview_tab);

					switchFragment(stylePageFragment);
					loadPreviewImages();
					stylePageFragment.previwImageCreate();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						R.string.no_image_found, Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), R.string.no_image_found,
					Toast.LENGTH_LONG).show();
		}
	}

	public void previewTabClick(View view) {
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

			switchFragment(previewFragment);

			final ImageView leftImage = (ImageView) previewFragment.getView()
					.findViewById(R.id.leftImage);
			final ImageView rightImage = (ImageView) previewFragment.getView()
					.findViewById(R.id.rightImage);
			
			final File imageFolder = new File(GlobalVariables.PREVIEW_FOLDER);
			previewFragment.setImagePathes(imageFolder);

			leftImage.setOnTouchListener(gestureListener);
			rightImage.setOnTouchListener(gestureListener);
			loadLeftRightImages(previewFragment, GlobalVariables.PREVIEW_FOLDER);
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
		if (currentFragment instanceof StylePageFragment) {
			try {
				final Intent intent = new Intent(getApplicationContext(),
						EditPhotoActivity.class);
				intent.putExtra(EditPhotoActivity.IS_LEFT, true);
				intent.putExtra("imageIndex", stylePageFragment.getLeftImagePath());
				startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(this, R.string.edit_photo_open_error, Toast.LENGTH_LONG).show();
			}
		}
	}

	public void onRightImageClick(View view) {
		try {
			if (currentFragment instanceof StylePageFragment) {
				final Intent intent = new Intent(getApplicationContext(),
						EditPhotoActivity.class);
				intent.putExtra(EditPhotoActivity.IS_LEFT, false);
				intent.putExtra("imageIndex", stylePageFragment.getRightImagePath());
				startActivity(intent);
			}
		} catch (Exception e) {
			Toast.makeText(this, R.string.edit_photo_open_error, Toast.LENGTH_LONG).show();
		}
	}

	private void loadPreviewImages() {
		final ImageView bookCover = (ImageView) stylePageFragment.getView()
				.findViewById(R.id.previewImage);
		final ImageView leftImage = (ImageView) stylePageFragment.getView()
				.findViewById(R.id.leftImage);
		final ImageView rightImage = (ImageView) stylePageFragment.getView()
				.findViewById(R.id.rightImage);

		final File imageFolder = new File(GlobalVariables.IMAGE_FOLDER);
		stylePageFragment.setImagePathes(imageFolder);
		ImageUtils.loadImage(bookCover, GlobalVariables.IMAGE_FOLDER
				+ stylePageFragment.getPreviewImagePath());

		leftImage.setOnTouchListener(gestureListener);
		rightImage.setOnTouchListener(gestureListener);

		loadLeftRightImages(stylePageFragment, GlobalVariables.IMAGE_FOLDER);
	}

	private void loadLeftRightImages(FragmentWithPreviews fragment, String imagesFolder) {
		final ImageView leftImage = (ImageView) fragment.getView()
				.findViewById(R.id.leftImage);
		final ImageView rightImage = (ImageView) fragment.getView()
				.findViewById(R.id.rightImage);
		try {
			ImageUtils.loadImage(leftImage, imagesFolder
					+ fragment.getLeftImagePath(), 300, true);
		} catch (Exception e) {
			leftImage.setImageResource(R.drawable.no_imageb);
		}
		try {
			ImageUtils.loadImage(rightImage, imagesFolder
					+ fragment.getRightImagePath(), 300, true);
		} catch (Exception e) {
			rightImage.setImageResource(R.drawable.no_imageb);
		}
	}

	private void onLeftSwipe() {
		if (currentFragment instanceof StylePageFragment) {
			stylePageFragment.flipImagesRight();
			loadLeftRightImages(stylePageFragment, GlobalVariables.IMAGE_FOLDER);
		} else if (currentFragment instanceof PreviewFragment) {
			previewFragment.flipImagesRight();
			loadLeftRightImages(previewFragment, GlobalVariables.PREVIEW_FOLDER);
		}
	}

	private void onRightSwipe() {
		if (currentFragment instanceof StylePageFragment) {
			stylePageFragment.flipImagesLeft();
			loadLeftRightImages(stylePageFragment, GlobalVariables.IMAGE_FOLDER);
		} else if (currentFragment instanceof PreviewFragment) {
			previewFragment.flipImagesLeft();
			loadLeftRightImages(previewFragment, GlobalVariables.PREVIEW_FOLDER);
		}
	}

	public void checkout(View view) {
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
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	@SuppressWarnings("deprecation")
	public void resizeScreenParts() {
		if (currentFragment instanceof PreviewFragment) {
			Display display = getWindowManager().getDefaultDisplay(); 
			int width = display.getWidth();
			LinearLayout linear = (LinearLayout) findViewById(R.id.linearLayout1);
			LinearLayout.LayoutParams relativeLayoutParams = new LinearLayout.LayoutParams(width, width/2);
			linear.setLayoutParams(relativeLayoutParams);
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (currentFragment instanceof StylePageFragment) {
			loadLeftRightImages(stylePageFragment, GlobalVariables.IMAGE_FOLDER);
		}
	}
	
	@Override
	public final boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			new GetOrdersListTask(this).execute();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
