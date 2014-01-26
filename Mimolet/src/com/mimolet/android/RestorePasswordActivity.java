package com.mimolet.android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RestorePasswordActivity extends Activity {
	
	EditText loginField;
	Button restoreButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restore_password);
		loginField = (EditText) findViewById(R.id.restoreLoginField);
		restoreButton = (Button) findViewById(R.id.restoreRestoreButton);
		restoreButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String login = loginField.getText().toString();
				Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
				Matcher m = p.matcher(login);
				if (m.matches()) {
					
				} else {
					Toast.makeText(getApplicationContext(), R.string.restore_thisIsNotEmail, 
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.restore_password, menu);
		return true;
	}

}
