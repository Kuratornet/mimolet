package com.mimolet.android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.mimolet.android.global.GlobalVariables;
import com.mimolet.android.util.ImageHelper;

import entity.PhotoData;

public class EditPhotoActivity extends SherlockActivity {

    private static final String TAG = "EditPhotoActivity";
    private Activity thisActivity;
    private Bitmap photoBitmap;
    private int imageIndex;
    private PhotoData photoData;
    private int currentBorderColor;
    
    //Color change radiogroups
    private RadioGroup backgroundColorChangerGroup;
    private RadioGroup borderColorChangerGroup;
    private RadioGroup textColorChangeGroup;
    
	public static String IS_LEFT = "isLeft";

	private EditText addingTextField;
	
	// Screen mode
    private static final int FULL_SCREEN_MODE = 0;
    private static final int FULL_SCREEN_WITH_CORNER_MODE = 1;
    private static final int IN_FRAME_MODE = 2;

    private static final int WITHOUT_TEXT = 0;
    private static final int WITH_TEXT = 1;

    //TextSize
    private static final float TEXT_SIZE_SMALL = 22;
    private static final float TEXT_SIZE_MEDIUM = 30;
    private static final float TEXT_SIZE_LARGE = 38;
    
    private String[] fontBindingArray = { "Ludvig" };
    private int currentFrameMode = FULL_SCREEN_MODE;
    private int currentTextMode = WITHOUT_TEXT;

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

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_photo);
		thisActivity = this;
		// Load image and get image path and index
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		String imagePath = getIntent().getStringExtra("imageIndex");
		String imageIndexString = imagePath.replaceAll("\\D+","");
		imageIndex = Integer.valueOf(imageIndexString);
		photoBitmap = BitmapFactory.decodeFile(GlobalVariables.IMAGE_FOLDER + imagePath, options);
		addingTextField = (EditText) findViewById(R.id.addingTextField);
		
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
						photoData.setText(addingTextField.getText().toString());
						EditPhotoActivity.this.onBackPressed();
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
                photoData.setPhotoStyle(FULL_SCREEN_MODE);
                updateLayoutMode();
            }
        });
        ImageView editPhotoLayoutRoundeButton = (ImageView) findViewById(R.id.edit_photo_layout_rounded_button);
        editPhotoLayoutRoundeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            	photoData.setPhotoStyle(FULL_SCREEN_WITH_CORNER_MODE);
                currentFrameMode = FULL_SCREEN_WITH_CORNER_MODE;
                updateLayoutMode();
            }
        });
        ImageView editPhotoLayoutCenterButton = (ImageView) findViewById(R.id.edit_photo_layout_center_button);
        editPhotoLayoutCenterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            	photoData.setPhotoStyle(IN_FRAME_MODE);
                currentFrameMode = IN_FRAME_MODE;
                updateLayoutMode();
            }
        });
        
        //Background color changer
        backgroundColorChangerGroup = (RadioGroup) findViewById(R.id.backgroundColorChangerGroup);
        backgroundColorChangerGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				ImageView backgroundRect = (ImageView) findViewById(R.id.backgroundRect);
				ShapeDrawable backgroundRectDImageWhite = new ShapeDrawable();
				float corner = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
				RoundRectShape shapeWithCorners = new RoundRectShape(new float[] {corner, corner, corner, corner, corner, corner, corner, corner},
		                null, null);
				backgroundRectDImageWhite.setShape(shapeWithCorners);
				backgroundRectDImageWhite.getPaint().setColor(getResources().getColor(R.color.white));
				ShapeDrawable backgroundRectDImageBlack = new ShapeDrawable();
				backgroundRectDImageBlack.setShape(shapeWithCorners);
				backgroundRectDImageBlack.getPaint().setColor(getResources().getColor(R.color.black));
				ShapeDrawable backgroundRectDImageBiege = new ShapeDrawable();
				backgroundRectDImageBiege.setShape(shapeWithCorners);
				backgroundRectDImageBiege.getPaint().setColor(getResources().getColor(R.color.biege));
				ShapeDrawable backgroundColorChangerOrange = new ShapeDrawable();
				backgroundColorChangerOrange.setShape(shapeWithCorners);
				backgroundColorChangerOrange.getPaint().setColor(getResources().getColor(R.color.orange));
				ShapeDrawable backgroundRectDImageBrown = new ShapeDrawable();
				backgroundRectDImageBrown.setShape(shapeWithCorners);
				backgroundRectDImageBrown.getPaint().setColor(getResources().getColor(R.color.brown));
				switch (checkedId) {
					case R.id.backgroundColorChangerWhite:
						photoData.setBackgroundColor(getResources().getColor(R.color.white));
				        backgroundRect.setBackgroundDrawable(backgroundRectDImageWhite);
						break;
					case R.id.backgroundColorChangerBlack:
						photoData.setBackgroundColor(getResources().getColor(R.color.black));
				        backgroundRect.setBackgroundDrawable(backgroundRectDImageBlack);
						break;
					case R.id.backgroundColorChangerBiege:
						photoData.setBackgroundColor(getResources().getColor(R.color.biege));
				        backgroundRect.setBackgroundDrawable(backgroundRectDImageBiege);
						break;
					case R.id.backgroundColorChangerOrange:
						photoData.setBackgroundColor(getResources().getColor(R.color.orange));
				        backgroundRect.setBackgroundDrawable(backgroundColorChangerOrange);
						break;
					case R.id.backgroundColorChangerBrown:
						photoData.setBackgroundColor(getResources().getColor(R.color.brown));
				        backgroundRect.setBackgroundDrawable(backgroundRectDImageBrown);
						break;
				}
			}
		});
        
        //Border color changer
        borderColorChangerGroup = (RadioGroup) findViewById(R.id.borderColorChangerGroup);
        borderColorChangerGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (borderColorChangerGroup.equals(group)) {
					if (currentFrameMode == 2) {
						ImageView photo = (ImageView) findViewById(R.id.photo);
						switch (checkedId) {
							case R.id.borderColorChangerWhite:
								photoData.setBorderColor(getResources().getColor(R.color.white));
								photo.setBackgroundColor(getResources().getColor(R.color.white));
								break;
							case R.id.borderColorChangerGrey:
								photoData.setBorderColor(getResources().getColor(R.color.grey));
								photo.setBackgroundColor(getResources().getColor(R.color.grey));
								break;
							case R.id.borderColorChangerBiege:
								photoData.setBorderColor(getResources().getColor(R.color.biege));
								photo.setBackgroundColor(getResources().getColor(R.color.biege));
								break;
							case R.id.borderColorChangerPeach:
								photoData.setBorderColor(getResources().getColor(R.color.peach));
								photo.setBackgroundColor(getResources().getColor(R.color.peach));
								break;
							case R.id.borderColorChangerDarkGrey:
								photoData.setBorderColor(getResources().getColor(R.color.darkgrey));
								photo.setBackgroundColor(getResources().getColor(R.color.darkgrey));
								break;
						}
					} else {
						Toast.makeText(thisActivity,
								R.string.editphoto_withoutBroderMode, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
        
        
        //Text color changing
        Log.i(TAG, "Set listeners to text color radio buttons");
        textColorChangeGroup = (RadioGroup) findViewById(R.id.textColorChangeRadioGroup);
        textColorChangeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.whiteColorTextCheckBox:
					photoData.setTextTextColor(getResources().getColor(R.color.white));
					addingTextField.setTextColor(getResources().getColor(R.color.white));
					break;
				case R.id.blackColorTextCheckBox:
					photoData.setTextTextColor(getResources().getColor(R.color.black));
					addingTextField.setTextColor(getResources().getColor(R.color.black));
					break;
				case R.id.biegeColorTextCheckBox:
					photoData.setTextTextColor(getResources().getColor(R.color.biege));
					addingTextField.setTextColor(getResources().getColor(R.color.biege));
					break;
				case R.id.orangeColorTextCheckBox:
					photoData.setTextTextColor(getResources().getColor(R.color.orange));
					addingTextField.setTextColor(getResources().getColor(R.color.orange));
					break;
				case R.id.brownColorTextCheckBox:
					photoData.setTextTextColor(getResources().getColor(R.color.brown));
					addingTextField.setTextColor(getResources().getColor(R.color.brown));
					break;
				}
				
			}
        });
        
        //Text font changing
        Log.i(TAG, "Set listeners to text font spinner");
        Typeface font = Typeface.createFromAsset(getAssets(), "Ludvig.ttf");
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
							Typeface font = Typeface.createFromAsset(getAssets(), "Ludvig.ttf");
							addingTextField.setTypeface(font);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		
		//Text size changing
		Log.i(TAG, "Set listeners to text size toggle buttons");
		addingTextField.setTextSize(TEXT_SIZE_MEDIUM);
		ToggleButton fontSizeSmall = (ToggleButton) findViewById(R.id.fontSmallButton);
		ToggleButton fontSizeMedium = (ToggleButton) findViewById(R.id.fontMediumButton);
		ToggleButton fontSizeLarge = (ToggleButton) findViewById(R.id.fontLargeButton);
		fontSizeSmall.setOnCheckedChangeListener(new SmallTextCheckBoxOnChangeListener());
		fontSizeMedium.setOnCheckedChangeListener(new MediumTextCheckBoxOnChangeListener());
		fontSizeLarge.setOnCheckedChangeListener(new LargeTextCheckBoxOnChangeListener());
		
		// Initialize and set up previous data. Main frame! 
		if (GlobalVariables.imagesListData.containsKey(imageIndex)) {
			photoData = GlobalVariables.imagesListData.get(imageIndex);
			currentFrameMode = photoData.getPhotoStyle();
			if (photoData.getPhotoStyle() == IN_FRAME_MODE) {
				currentBorderColor = photoData.getBorderColor();
			}
			if (photoData.getText() != null) {
				currentTextMode = WITH_TEXT;
				addingTextField.setTextSize(photoData.getTextFontSyze());
				addingTextField.setTextColor(photoData.getTextTextColor());
				addingTextField.setText(photoData.getText());
			}
			ImageView backgroundRect = (ImageView) findViewById(R.id.backgroundRect);
			ShapeDrawable backgroundRectDImageWhite = new ShapeDrawable();
			float corner = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
			RoundRectShape shapeWithCorners = new RoundRectShape(new float[] {corner, corner, corner, corner, corner, corner, corner, corner},
	                null, null);
			backgroundRectDImageWhite.setShape(shapeWithCorners);
			backgroundRectDImageWhite.getPaint().setColor(photoData.getBackgroundColor());
			backgroundRect.setBackgroundDrawable(backgroundRectDImageWhite);
		} else {
			photoData = new PhotoData();
			GlobalVariables.imagesListData.put(imageIndex, photoData);
			ImageView backgroundRect = (ImageView) findViewById(R.id.backgroundRect);
	        ShapeDrawable backgroundRectDImage = new ShapeDrawable();
	        float corner = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
	        backgroundRectDImage.setShape(new RoundRectShape(new float[] {corner, corner, corner, corner, corner, corner, corner, corner},
	                null,
	                null));
	        backgroundRectDImage.getPaint().setColor(getResources().getColor(R.color.orange));
	        backgroundRect.setBackgroundDrawable(backgroundRectDImage);
	        currentBorderColor = getResources().getColor(R.color.darkgrey);
			photoData.setBackgroundColor(getResources().getColor(R.color.orange));
			photoData.setBorderColor(getResources().getColor(R.color.darkgrey));
			photoData.setTextTextColor(getResources().getColor(R.color.white));
			photoData.setTextFontType(fontBindingArray[0]);
			photoData.setTextFontSyze(TEXT_SIZE_MEDIUM);
			photoData.setPhotoStyle(FULL_SCREEN_MODE);
			backgroundColorChangerGroup.check(R.id.backgroundColorChangerOrange);
			textColorChangeGroup.check(R.id.whiteColorTextCheckBox);
		}
		// End of initializing
	}

	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ImageView mainFrame = (ImageView) findViewById(R.id.mainFrame);
		ImageView backgroundRect = (ImageView) findViewById(R.id.backgroundRect);
        int backgroundRectrelativeLayoutParamsMargin = 4;
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
                inFrameMode(photo, currentBorderColor);
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
        RadioGroup borderColorChangerGroup = (RadioGroup) findViewById(R.id.borderColorChangerGroup);
        borderColorChangerGroup.clearCheck();
        photo.setBackgroundColor(Color.TRANSPARENT);
        photo.setImageBitmap(photoBitmap);
    }

    private void fullScreenWithCornerMode(ImageView photo) {
        updatePhotoLayout(photo);
        photo.setPadding(0, 0, 0, 0);
        RadioGroup borderColorChangerGroup = (RadioGroup) findViewById(R.id.borderColorChangerGroup);
        borderColorChangerGroup.clearCheck();
        photo.setBackgroundColor(Color.TRANSPARENT);
        photo.setImageBitmap(ImageHelper.getRoundedCornerBitmap(photoBitmap, 50));
    }

    private void inFrameMode(ImageView photo, int color) {
        updatePhotoLayout(photo);
        photo.setPadding(20, 20, 20, 20);
        photo.setBackgroundColor(color);
        RadioGroup borderColorChangerGroup = (RadioGroup) findViewById(R.id.borderColorChangerGroup);
        borderColorChangerGroup.check(R.id.borderColorChangerDarkGrey);
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
    
    private class SmallTextCheckBoxOnChangeListener implements CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				photoData.setTextFontSyze(TEXT_SIZE_SMALL);
				addingTextField.setTextSize(TEXT_SIZE_SMALL);
			}
		}
    }
    private class MediumTextCheckBoxOnChangeListener implements CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				photoData.setTextFontSyze(TEXT_SIZE_MEDIUM);
				addingTextField.setTextSize(TEXT_SIZE_MEDIUM);
			}
		}
    }
    private class LargeTextCheckBoxOnChangeListener implements CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				photoData.setTextFontSyze(TEXT_SIZE_LARGE);
				addingTextField.setTextSize(TEXT_SIZE_LARGE);
			}
		}
    }
}
