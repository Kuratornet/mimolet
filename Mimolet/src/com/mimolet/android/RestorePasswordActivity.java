package com.mimolet.android;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mimolet.android.task.RestorePasswordTask;

public class RestorePasswordActivity extends Activity {
	
	private static final String TAG = "RestorePasswordActivity";
	EditText loginField;
	Button restoreButton;
	Activity thisActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restore_password);
		thisActivity = this;
		loginField = (EditText) findViewById(R.id.restoreLoginField);
		restoreButton = (Button) findViewById(R.id.restoreRestoreButton);
		restoreButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String login = loginField.getText().toString();
				Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
				Matcher m = p.matcher(login);
				if (m.matches()) {
					try {
						Log.i(TAG, "Try to restore password to " + login);
						final Properties connectionProperties = new Properties();
						connectionProperties.load(getAssets().open("connection.properties"));
						String serverUrl = connectionProperties.getProperty("server_url")
								+ connectionProperties.getProperty("restorepass");
						new RestorePasswordTask(thisActivity).execute(login, serverUrl);
					} catch (IOException e) {
						e.printStackTrace();
					}
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
