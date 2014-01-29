package com.mimolet.android;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.mimolet.android.util.ImageHelper;

public class EditPhotoActivity extends SherlockActivity {

    private static final String TAG = "EditPhotoActivity";
    
	public static String IS_LEFT = "isLeft";

	private EditText addingTextField;
	
    private static final int FULL_SCREEN_MODE = 0;
    private static final int FULL_SCREEN_WITH_CORNER_MODE = 1;
    private static final int IN_FRAME_MODE = 2;

    private static final int WITHOUT_TEXT = 0;
    private static final int WITH_TEXT = 1;

    
    private String[] fontBindingArray = { "Corinthia" };
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
		addingTextField = (EditText) findViewById(R.id.addingTextField);
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
                updateLayoutMode();
            }
        });
        ImageView editPhotoLayoutRoundeButton = (ImageView) findViewById(R.id.edit_photo_layout_rounded_button);
        editPhotoLayoutRoundeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFrameMode = FULL_SCREEN_WITH_CORNER_MODE;
                updateLayoutMode();
            }
        });
        ImageView editPhotoLayoutCenterButton = (ImageView) findViewById(R.id.edit_photo_layout_center_button);
        editPhotoLayoutCenterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFrameMode = IN_FRAME_MODE;
                updateLayoutMode();
            }
        });
        
        //Text color changing
        Log.i(TAG, "Set listeners to text color radio buttons");
        RadioButton whiteRadioButton = (RadioButton) findViewById(R.id.whiteColorCheckBox);
        whiteRadioButton.setChecked(true);
        RadioButton blackRadioButton = (RadioButton) findViewById(R.id.blackColorCheckBox);
        RadioButton biegeRadioButton = (RadioButton) findViewById(R.id.biegeColorCheckBox);
        RadioButton orangeRadioButton = (RadioButton) findViewById(R.id.orangeColorCheckBox);
        RadioButton brownRadioButton = (RadioButton) findViewById(R.id.brownColorCheckBox);
        whiteRadioButton.setOnCheckedChangeListener(new WhiteTextCheckBoxOnChangeListener());
        blackRadioButton.setOnCheckedChangeListener(new BlackTextCheckBoxOnChangeListener());
        biegeRadioButton.setOnCheckedChangeListener(new BiegeTextCheckBoxOnChangeListener());
        orangeRadioButton.setOnCheckedChangeListener(new OrangeTextCheckBoxOnChangeListener());
        brownRadioButton.setOnCheckedChangeListener(new BrownTextCheckBoxOnChangeListener());
        
        //Text font changing
        Log.i(TAG, "Set listeners to text font spinner");
        Typeface font = Typeface.createFromAsset(getAssets(), "Coronet.ttf");
		addingTextField.setTypeface(font);
        ArrayAdapter<String> fontBindingAdapter =
                new ArrayAdapter<String>(this, R.layout.spinner_item, fontBindingArray);
		final Spinner fontBindingSpinner = (Spinner) findViewById(R.id.textFontBindingSpinner);
		fontBindingSpinner.setAdapter(fontBindingAdapter);
		fontBindingSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (fontBindingSpinner.getSelectedItemPosition() == 0) {
							Typeface font = Typeface.createFromAsset(getAssets(), "Coronet.ttf");
							addingTextField.setTypeface(font);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
	}

    @SuppressWarnings("deprecation")
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ImageView mainFrame = (ImageView) findViewById(R.id.mainFrame);
        ImageView backgroundRect = (ImageView) findViewById(R.id.backgroundRect);
        // backgroundRect once
        ShapeDrawable backgroundRectDImage = new ShapeDrawable();
        backgroundRectDImage.setShape(new RoundRectShape(new float[] {50,50,50,50,50,50,50,50},
                null,
                null));
        backgroundRectDImage.getPaint().setColor(Color.GREEN);
        backgroundRect.setBackgroundDrawable(backgroundRectDImage);

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

        updateLayoutMode();
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
            if (previouslySelectedButton.equals(R.id.addTextTab) &&
                    "".equals(((EditText) findViewById(R.id.addingTextField)).getText().toString())) {
                currentTextMode = WITHOUT_TEXT;
                updateLayoutMode();
            }
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
                if ("".equals(((EditText) findViewById(R.id.addingTextField)).getText().toString())) {
                    currentTextMode = WITHOUT_TEXT;
                    updateLayoutMode();
                }
				break;
			case R.id.chooseBackgroundTab:
                params.addRule(RelativeLayout.ABOVE, R.id.popup_background);
				chooseLayoutPopup.setVisibility(View.GONE);
				chooseBackgroundPopup.setVisibility(View.VISIBLE);
				addTextPopup.setVisibility(View.GONE);
				changePhotoPopup.setVisibility(View.GONE);
                if ("".equals(((EditText) findViewById(R.id.addingTextField)).getText().toString())) {
                    currentTextMode = WITHOUT_TEXT;
                    updateLayoutMode();
                }
				break;
			case R.id.addTextTab:
                params.addRule(RelativeLayout.ABOVE, R.id.popup_text);
				chooseLayoutPopup.setVisibility(View.GONE);
				chooseBackgroundPopup.setVisibility(View.GONE);
				addTextPopup.setVisibility(View.VISIBLE);
				changePhotoPopup.setVisibility(View.GONE);
                currentTextMode = WITH_TEXT;
                updateLayoutMode();
				break;
			case R.id.changePhotoTab:
                params.addRule(RelativeLayout.ABOVE, R.id.popup_change_photo);
				chooseLayoutPopup.setVisibility(View.GONE);
				chooseBackgroundPopup.setVisibility(View.GONE);
				addTextPopup.setVisibility(View.GONE);
				changePhotoPopup.setVisibility(View.VISIBLE);
                if ("".equals(((EditText) findViewById(R.id.addingTextField)).getText().toString())) {
                    currentTextMode = WITHOUT_TEXT;
                    updateLayoutMode();
                }
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

    private void updateLayoutMode() {
        ImageView mainFrame = (ImageView) findViewById(R.id.mainFrame);
        ImageView photo = (ImageView) findViewById(R.id.photo);

        // mainFrame
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(mainFrame.getMeasuredWidth(), mainFrame.getMeasuredWidth());
        mainFrame.setLayoutParams(relativeLayoutParams);

        switch (currentFrameMode) {
            case FULL_SCREEN_MODE:
                fullScreenMode(photo);
                break;
            case FULL_SCREEN_WITH_CORNER_MODE:
                fullScreenWithCornerMode(photo);
                break;
            case IN_FRAME_MODE:
                inFrameMode(photo);
                break;
            default:
                throw new IllegalStateException("setLayoutMode: undefined frameMode");
        }

        EditText editText = (EditText) findViewById(R.id.addingTextField);
        switch (currentTextMode) {
            case WITHOUT_TEXT:
                editText.setVisibility(View.GONE);
                break;
            case WITH_TEXT:
                // update backgroundFrame and photo size
                editText.setVisibility(View.VISIBLE);
                editText.requestFocus();
                break;
            default:
                throw new IllegalStateException("setLayoutMode: undefined textMode");
        }
    }

    private void fullScreenMode(ImageView photo) {
        updatePhotoLayout(photo);
        photo.setPadding(0, 0, 0, 0);
        photo.setBackgroundColor(Color.TRANSPARENT);
        photo.setImageBitmap(photoBitmap);
    }

    private void fullScreenWithCornerMode(ImageView photo) {
        updatePhotoLayout(photo);
        photo.setPadding(0, 0, 0, 0);
        photo.setBackgroundColor(Color.TRANSPARENT);
        photo.setImageBitmap(ImageHelper.getRoundedCornerBitmap(photoBitmap, 50));
    }

    private void inFrameMode(ImageView photo) {
        updatePhotoLayout(photo);
        photo.setPadding(20, 20, 20, 20);
        photo.setBackgroundColor(Color.BLACK);
        photo.setImageBitmap(photoBitmap);
    }

    private void updatePhotoLayout(ImageView photo) {
        LinearLayout photoImageLinearLayout = (LinearLayout) findViewById(R.id.photoImageLinearLayout);
        int photoImageLinearLayoutMargin = 0;
        switch (currentFrameMode) {
            case FULL_SCREEN_MODE:
                photoImageLinearLayoutMargin = 30;
                break;
            case FULL_SCREEN_WITH_CORNER_MODE:
                photoImageLinearLayoutMargin = 30;
                break;
            case IN_FRAME_MODE:
                photoImageLinearLayoutMargin = 55;
                break;
            default:
                throw new IllegalStateException("updatePhotoLayout: undefined frameMode");
        }
        //EditText editText = (EditText) findViewById(R.id.editText1);

        ImageView mainFrame = (ImageView) findViewById(R.id.mainFrame);
        RelativeLayout.LayoutParams photoImageLinearLayoutParams = null;
        if (currentTextMode == WITHOUT_TEXT) {
            photoImageLinearLayoutParams = new RelativeLayout.LayoutParams(mainFrame.getMeasuredWidth(),
                    mainFrame.getMeasuredWidth() - 2*(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()));
        } else {
            photoImageLinearLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            photoImageLinearLayoutParams.addRule(RelativeLayout.ABOVE, R.id.addingTextField);
        }
        photoImageLinearLayoutParams.setMargins(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, photoImageLinearLayoutMargin, getResources().getDisplayMetrics())
        );
        photoImageLinearLayout.setLayoutParams(photoImageLinearLayoutParams);
    }
    
    private class WhiteTextCheckBoxOnChangeListener implements CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				addingTextField.setTextColor(getResources().getColor(R.color.white));
			}
		}
    }
    private class BlackTextCheckBoxOnChangeListener implements CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				addingTextField.setTextColor(getResources().getColor(R.color.black));
			}
		}
    }
    private class BiegeTextCheckBoxOnChangeListener implements CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				addingTextField.setTextColor(getResources().getColor(R.color.biege));
			}
		}
    }
    private class OrangeTextCheckBoxOnChangeListener implements CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				addingTextField.setTextColor(getResources().getColor(R.color.orange));
			}
		}
    }
    private class BrownTextCheckBoxOnChangeListener implements CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				addingTextField.setTextColor(getResources().getColor(R.color.brown));
			}
		}
    }
}
