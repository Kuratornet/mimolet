package com.mimolet.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.mimolet.android.fragment.AddPhotoFragment;
import com.mimolet.android.fragment.ChooseStyleFragment;

public class AddBookActivity extends SherlockFragmentActivity {
	private static final String TAG = "AddBookActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_book);
		final ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setTitle(R.string.creating_book);
		final Fragment chooseStyleFragment = new ChooseStyleFragment();
		final Fragment addPhotoFragment = new AddPhotoFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		final MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.add_book, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void tab1Click(View view) {
		final ImageButton tab1 = (ImageButton) findViewById(R.id.tab1);
		tab1.setBackgroundResource(R.drawable.tab1_selected);
		final ImageButton tab2 = (ImageButton) findViewById(R.id.tab2);
		tab2.setBackgroundResource(R.drawable.tab2);
		final ImageButton tab3 = (ImageButton) findViewById(R.id.tab3);
		tab3.setBackgroundResource(R.drawable.tab3);
		final ImageButton tab4 = (ImageButton) findViewById(R.id.tab4);
		tab4.setBackgroundResource(R.drawable.tab4);
	}

	public void tab2Click(View view) {
		final ImageButton tab1 = (ImageButton) findViewById(R.id.tab1);
		tab1.setBackgroundResource(R.drawable.tab1_done);
		final ImageButton tab2 = (ImageButton) findViewById(R.id.tab2);
		tab2.setBackgroundResource(R.drawable.tab2_selected);
		final ImageButton tab3 = (ImageButton) findViewById(R.id.tab3);
		tab3.setBackgroundResource(R.drawable.tab3);
		final ImageButton tab4 = (ImageButton) findViewById(R.id.tab4);
		tab4.setBackgroundResource(R.drawable.tab4);
	}

	public void tab3Click(View view) {
		final ImageButton tab1 = (ImageButton) findViewById(R.id.tab1);
		tab1.setBackgroundResource(R.drawable.tab1_done);
		final ImageButton tab2 = (ImageButton) findViewById(R.id.tab2);
		tab2.setBackgroundResource(R.drawable.tab2_done);
		final ImageButton tab3 = (ImageButton) findViewById(R.id.tab3);
		tab3.setBackgroundResource(R.drawable.tab3_selected);
		final ImageButton tab4 = (ImageButton) findViewById(R.id.tab4);
		tab4.setBackgroundResource(R.drawable.tab4);
	}

	public void tab4Click(View view) {
		final ImageButton tab1 = (ImageButton) findViewById(R.id.tab1);
		tab1.setBackgroundResource(R.drawable.tab1_done);
		final ImageButton tab2 = (ImageButton) findViewById(R.id.tab2);
		tab2.setBackgroundResource(R.drawable.tab2_done);
		final ImageButton tab3 = (ImageButton) findViewById(R.id.tab3);
		tab3.setBackgroundResource(R.drawable.tab3_done);
		final ImageButton tab4 = (ImageButton) findViewById(R.id.tab4);
		tab4.setBackgroundResource(R.drawable.tab4_selected);
	}

	public void bottom1Click(View view) {
	}

	public void bottom2Click(View view) {
	}

	public void bottom3Click(View view) {
	}
}
