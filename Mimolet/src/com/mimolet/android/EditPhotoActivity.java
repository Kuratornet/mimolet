package com.mimolet.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class EditPhotoActivity extends SherlockActivity {

	public static String IS_LEFT = "isLeft";

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
	}

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ImageView previewImage = (ImageView) findViewById(R.id.previewImage);
        ImageView previewCover = (ImageView) findViewById(R.id.previewCover);
        Log.e("SSAI", String.valueOf(previewImage.getMeasuredWidth()));
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(previewImage.getMeasuredWidth(), previewImage.getMeasuredWidth());
        previewImage.setLayoutParams(relativeLayoutParams);
        previewCover.setLayoutParams(relativeLayoutParams);
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
}
