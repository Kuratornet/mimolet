package com.mimolet.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class EditPhotoActivity extends Activity {

	public static String IS_LEFT = "isLeft";

	private boolean isLeft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isLeft = getIntent().getBooleanExtra(IS_LEFT, true);
		setContentView(R.layout.activity_edit_photo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_photo, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
