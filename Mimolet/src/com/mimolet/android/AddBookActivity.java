package com.mimolet.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.mimolet.android.fragment.AddPhotoFragment;
import com.mimolet.android.fragment.ChooseStyleFragment;

public class AddBookActivity extends SherlockFragmentActivity {
	private static final String TAG = "AddBookActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_book);
		final ActionBar actionbar = getSupportActionBar();
		// Tell the ActionBar we want to use Tabs.
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// initiating both tabs and set text to it.
		final ActionBar.Tab chooseStyleTab = actionbar.newTab().setText(
				"Choose style");
		final ActionBar.Tab addPhotoTab = actionbar.newTab().setText(
				"Add photo");
		// create the two fragments we want to use for display content
		final Fragment chooseStyleFragment = new ChooseStyleFragment();
		final Fragment addPhotoFragment = new AddPhotoFragment();

		// set the Tab listener. Now we can listen for clicks.
		chooseStyleTab.setTabListener(new MyTabsListener(chooseStyleFragment));
		addPhotoTab.setTabListener(new MyTabsListener(addPhotoFragment));

		// add the two tabs to the actionbar
		actionbar.addTab(chooseStyleTab);
		actionbar.addTab(addPhotoTab);
	}

	private class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;

		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_container, fragment);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	}
}
