package com.mimolet.android;

import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.mimolet.android.task.AuthorizationTask;

public class AuthorizationActivity extends Activity {

	private static final String TAG = "AuthorizationActivity";
	private EditText loginField;
	private EditText passwordField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authorization);

		loginField = (EditText) findViewById(R.id.loginField);
		passwordField = (EditText) findViewById(R.id.passwordField);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authorization, menu);
		return true;
	}

	public void authorize(View view) {
		final Properties connectionProperties = new Properties();
		try {
			connectionProperties
					.load(getAssets().open("connection.properties"));
			final String serverUrl = connectionProperties
					.getProperty("server_url")
					+ connectionProperties.getProperty("login_path");
			new AuthorizationTask(this).execute(loginField.getText().toString(),
					passwordField.getText().toString(), serverUrl);
		} catch (Exception ex) {
			Log.v(TAG, "Could not read connection configuration", ex);
		}
	}

	/**
	 * temp method for testing
	 */
	public void goToAddBook(View view) {
		final Intent intent = new Intent(getApplicationContext(),
				AddBookActivity.class);
		startActivity(intent);
	}
}
