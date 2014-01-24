package com.mimolet.android;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.mimolet.android.util.ImageHelper;

public class EditPhotoActivity extends SherlockActivity {

    private static final String TAG = "EditPhotoActivity";

	public static String IS_LEFT = "isLeft";

    private static final int FULL_SCREEN_MODE = 0;
    private static final int FULL_SCREEN_WITH_CORNER_MODE = 1;
    private static final int IN_FRAME_MODE = 2;

    private static final int WITHOUT_TEXT = 0;
    private static final int WITH_TEXT = 1;

    private int currentFrameMode = FULL_SCREEN_MODE;
    private int currentTextMode = WITHOUT_TEXT;

    private Bitmap photoBitmap = Bitmap.createBitmap(2048, 1024, Bitmap.Config.ARGB_8888);

	//private boolean isLeft;

	private ImageButton[] bottomTabs;
	private int[] bottomTabsSelectedResources;
	private int[] bottomTabsNormalResources;

	private LinearLayout chooseLayoutPopup;
	private LinearLayout chooseBackgroundPopup;
	private LinearLayout addTextPopup;
	private LinearLayout changePhotoPopup;

	private Integer previouslySelectedButton;

	private RadioGroup.OnCheckedChangeListener fontToggleListener = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
			for (int j = 0; j < radioGroup.getChildCount(); j++) {
				final ToggleButton view = (ToggleButton) radioGroup
						.getChildAt(j);
				view.setChecked(view.getId() == i);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_photo);
		//isLeft = getIntent().getBooleanExtra(IS_LEFT, true);

        photoBitmap.eraseColor(Color.RED);

		bottomTabs = new ImageButton[4];
		bottomTabs[0] = (ImageButton) findViewById(R.id.chooseLayoutTab);
		bottomTabs[1] = (ImageButton) findViewById(R.id.chooseBackgroundTab);
		bottomTabs[2] = (ImageButton) findViewById(R.id.addTextTab);
		bottomTabs[3] = (ImageButton) findViewById(R.id.changePhotoTab);
		bottomTabsNormalResources = new int[] { R.drawable.edit_photo_tab1,
				R.drawable.edit_photo_tab2, R.drawable.edit_photo_tab3,
				R.drawable.edit_photo_tab4 };
		bottomTabsSelectedResources = new int[] {
				R.drawable.edit_photo_tab1_selected,
				R.drawable.edit_photo_tab2_selected,
				R.drawable.edit_photo_tab3_selected,
				R.drawable.edit_photo_tab4_selected };

		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.edit_photo_menu);
		getSupportActionBar().getCustomView().findViewById(R.id.action_bar_no)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						EditPhotoActivity.this.onBackPressed();
					}
				});
		getSupportActionBar().getCustomView().findViewById(R.id.action_bar_yes)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(EditPhotoActivity.this, "Yes", Toast.LENGTH_SHORT)
								.show();
					}
				});

		chooseLayoutPopup = (LinearLayout) findViewById(R.id.popup_layout);
		chooseBackgroundPopup = (LinearLayout) findViewById(R.id.popup_background);
		addTextPopup = (LinearLayout) findViewById(R.id.popup_text);
		changePhotoPopup = (LinearLayout) findViewById(R.id.popup_change_photo);

		((RadioGroup) findViewById(R.id.font_toggle_group))
				.setOnCheckedChangeListener(fontToggleListener);

		final ListView imageSourceListView = (ListView) changePhotoPopup
				.findViewById(R.id.change_photo_list);
		imageSourceListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.image_source_list_item,
				R.id.image_source_list_item_text, getResources()
						.getStringArray(R.array.image_source_labels)));

        // init buttons
        ImageView editPhotoLayoutAllButton = (ImageView) findViewById(R.id.edit_photo_layout_all_button);
        editPhotoLayoutAllButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFrameMode = FULL_SCREEN_MODE;
                setLayoutMode(currentFrameMode, currentTextMode);
            }
        });
        ImageView editPhotoLayoutRoundeButton = (ImageView) findViewById(R.id.edit_photo_layout_rounded_button);
        editPhotoLayoutRoundeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFrameMode = FULL_SCREEN_WITH_CORNER_MODE;
                setLayoutMode(currentFrameMode, currentTextMode);
            }
        });
        ImageView editPhotoLayoutCenterButton = (ImageView) findViewById(R.id.edit_photo_layout_center_button);
        editPhotoLayoutCenterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFrameMode = IN_FRAME_MODE;
                setLayoutMode(currentFrameMode, currentTextMode);
            }
        });
	}

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        setLayoutMode(currentFrameMode, currentTextMode);
    }

    private void renderSelectedButton(Integer selectedButtonId) {
		for (int i = 0; i < bottomTabs.length; i++) {
			final ImageButton bottomTab = bottomTabs[i];
			if (selectedButtonId != null
					&& bottomTab.getId() == selectedButtonId
					&& (previouslySelectedButton == null || previouslySelectedButton != bottomTab
							.getId())) {
				bottomTab.setBackgroundResource(bottomTabsSelectedResources[i]);
			} else {
				bottomTab.setBackgroundResource(bottomTabsNormalResources[i]);
			}
		}
	}

	public void bottomMenuButtonClick(View view) {
		renderSelectedButton(view.getId());
		if (previouslySelectedButton != null
				&& view.getId() == previouslySelectedButton) {
			chooseLayoutPopup.setVisibility(View.GONE);
			chooseBackgroundPopup.setVisibility(View.GONE);
			addTextPopup.setVisibility(View.GONE);
			changePhotoPopup.setVisibility(View.GONE);
			previouslySelectedButton = null;
		} else {
            @SuppressWarnings("deprecation")
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
			switch (view.getId()) {
			case R.id.chooseLayoutTab:
                params.addRule(RelativeLayout.ABOVE, R.id.popup_layout);
				chooseLayoutPopup.setVisibility(View.VISIBLE);
				chooseBackgroundPopup.setVisibility(View.GONE);
				addTextPopup.setVisibility(View.GONE);
				changePhotoPopup.setVisibility(View.GONE);
				break;
			case R.id.chooseBackgroundTab:
                params.addRule(RelativeLayout.ABOVE, R.id.popup_background);
				chooseLayoutPopup.setVisibility(View.GONE);
				chooseBackgroundPopup.setVisibility(View.VISIBLE);
				addTextPopup.setVisibility(View.GONE);
				changePhotoPopup.setVisibility(View.GONE);
				break;
			case R.id.addTextTab:
                params.addRule(RelativeLayout.ABOVE, R.id.popup_text);
				chooseLayoutPopup.setVisibility(View.GONE);
				chooseBackgroundPopup.setVisibility(View.GONE);
				addTextPopup.setVisibility(View.VISIBLE);
				changePhotoPopup.setVisibility(View.GONE);
				break;
			case R.id.changePhotoTab:
                params.addRule(RelativeLayout.ABOVE, R.id.popup_change_photo);
				chooseLayoutPopup.setVisibility(View.GONE);
				chooseBackgroundPopup.setVisibility(View.GONE);
				addTextPopup.setVisibility(View.GONE);
				changePhotoPopup.setVisibility(View.VISIBLE);
				break;
			}
            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
            scrollView.setLayoutParams(params);
			previouslySelectedButton = view.getId();
		}
	}

	public void popupOkClick(View view) {
		switch (view.getId()) {
		case R.id.choose_layout_ok:
			chooseLayoutPopup.setVisibility(View.GONE);
			break;
		case R.id.choose_background_ok:
			chooseBackgroundPopup.setVisibility(View.GONE);
			break;
		case R.id.add_text_ok:
			addTextPopup.setVisibility(View.GONE);
			break;
		}
		renderSelectedButton(null);
	}

	public void fontToggle(View view) {
		((RadioGroup) view.getParent()).check(view.getId());
		// app specific stuff ..
	}

    private void setLayoutMode(int frameMode, int textMode) {
        ImageView mainFrame = (ImageView) findViewById(R.id.mainFrame);
        ImageView backgroundRect = (ImageView) findViewById(R.id.backgroundRect);
        ImageView photo = (ImageView) findViewById(R.id.photo);

        // mainFrame
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(mainFrame.getMeasuredWidth(), mainFrame.getMeasuredWidth());
        mainFrame.setLayoutParams(relativeLayoutParams);

        // backgroundRect
        ShapeDrawable backgroundRectDImage = new ShapeDrawable();
        backgroundRectDImage.setShape(new RoundRectShape(new float[] {50,50,50,50,50,50,50,50},
                null,
                null));
        backgroundRectDImage.getPaint().setColor(Color.GREEN);
        backgroundRect.setBackgroundDrawable(backgroundRectDImage);

        switch (frameMode) {
            case FULL_SCREEN_MODE:
                fullScreenMode(backgroundRect,
                        photo);
                break;
            case FULL_SCREEN_WITH_CORNER_MODE:
                fullScreenWithCornerMode(backgroundRect,
                        photo);
                break;
            case IN_FRAME_MODE:
                inFrameMode(backgroundRect,
                        photo);
                break;
            default:
                throw new IllegalStateException("setLayoutMode: undefined frameMode");
        }

        EditText editText = (EditText) findViewById(R.id.editText1);
        switch (textMode) {
            case WITHOUT_TEXT:
                // do nothing
                editText.setVisibility(View.GONE);
                break;
            case WITH_TEXT:
                // update backgroundFrame and photo size
                editText.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalStateException("setLayoutMode: undefined textMode");
        }
    }

    private void fullScreenMode(ImageView backgroundRect,
                                ImageView photo) {
        ImageView mainFrame = (ImageView) findViewById(R.id.mainFrame);
        // backgroundRect
        int backgroundRectrelativeLayoutParamsMargin = 5;
        RelativeLayout.LayoutParams backgroundRectrelativeLayoutParams = new RelativeLayout.LayoutParams(mainFrame.getMeasuredWidth(),
                mainFrame.getMeasuredWidth() - 2*(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics()));
        backgroundRectrelativeLayoutParams.setMargins(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics())
        );
        backgroundRect.setLayoutParams(backgroundRectrelativeLayoutParams);

        // photo
        LinearLayout photoImageLinearLayout = (LinearLayout) findViewById(R.id.photoImageLinearLayout);
        int photoImageLinearLayoutMargin = 30;
        RelativeLayout.LayoutParams photoImageLinearLayoutParams = new RelativeLayout.LayoutParams(mainFrame.getMeasuredWidth(),
                mainFrame.getMeasuredWidth() - 2*(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()));
        photoImageLinearLayoutParams.setMargins(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics())
        );
        photoImageLinearLayout.setLayoutParams(photoImageLinearLayoutParams);
        photo.setPadding(0, 0, 0, 0);
        photo.setBackgroundColor(Color.TRANSPARENT);
        photo.setImageBitmap(photoBitmap);
    }

    private void fullScreenWithCornerMode(ImageView backgroundRect,
                                          ImageView photo) {
        ImageView mainFrame = (ImageView) findViewById(R.id.mainFrame);
        // backgroundRect
        int backgroundRectrelativeLayoutParamsMargin = 5;
        RelativeLayout.LayoutParams backgroundRectrelativeLayoutParams = new RelativeLayout.LayoutParams(mainFrame.getMeasuredWidth(),
                mainFrame.getMeasuredWidth() - 2*(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics()));
        backgroundRectrelativeLayoutParams.setMargins(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics())
        );
        backgroundRect.setLayoutParams(backgroundRectrelativeLayoutParams);

        // photo
        LinearLayout photoImageLinearLayout = (LinearLayout) findViewById(R.id.photoImageLinearLayout);
        int photoImageLinearLayoutMargin = 30;
        RelativeLayout.LayoutParams photoImageLinearLayoutParams = new RelativeLayout.LayoutParams(mainFrame.getMeasuredWidth(),
                mainFrame.getMeasuredWidth() - 2*(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()));
        photoImageLinearLayoutParams.setMargins(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics())
        );
        photoImageLinearLayout.setLayoutParams(photoImageLinearLayoutParams);
        photo.setPadding(0, 0, 0, 0);
        photo.setBackgroundColor(Color.TRANSPARENT);
        photo.setImageBitmap(ImageHelper.getRoundedCornerBitmap(photoBitmap, 50));
    }

    private void inFrameMode(ImageView backgroundRect,
                             ImageView photo) {
        ImageView mainFrame = (ImageView) findViewById(R.id.mainFrame);
        // backgroundRect
        int backgroundRectrelativeLayoutParamsMargin = 5;
        RelativeLayout.LayoutParams backgroundRectrelativeLayoutParams = new RelativeLayout.LayoutParams(mainFrame.getMeasuredWidth(),
                mainFrame.getMeasuredWidth() - 2*(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics()));
        backgroundRectrelativeLayoutParams.setMargins(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, backgroundRectrelativeLayoutParamsMargin, getResources().getDisplayMetrics())
        );
        backgroundRect.setLayoutParams(backgroundRectrelativeLayoutParams);

        // photo
        LinearLayout photoImageLinearLayout = (LinearLayout) findViewById(R.id.photoImageLinearLayout);
        int photoImageLinearLayoutMargin = 55;
        RelativeLayout.LayoutParams photoImageLinearLayoutParams = new RelativeLayout.LayoutParams(mainFrame.getMeasuredWidth(),
                mainFrame.getMeasuredWidth() - 2*(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()));
        photoImageLinearLayoutParams.setMargins(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics())
        );
        photoImageLinearLayout.setLayoutParams(photoImageLinearLayoutParams);
        photo.setPadding(20, 20, 20, 20);
        photo.setBackgroundColor(Color.BLACK);
        photo.setImageBitmap(photoBitmap);
    }
}
