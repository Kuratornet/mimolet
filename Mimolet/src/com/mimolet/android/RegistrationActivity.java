package com.mimolet.android;

import java.io.IOException;
import java.util.Properties;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mimolet.android.task.RegistrationTask;

public class RegistrationActivity extends Activity {

	private EditText loginField;
	private EditText firstPasswordField;
	private EditText secondPasswordField;
	private Button registrationButton;
	private Activity thisActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		thisActivity = this;
		loginField = (EditText) findViewById(R.id.registrationLoginField);
		firstPasswordField = (EditText) findViewById(R.id.registrationEnerFirstPassword);
		secondPasswordField = (EditText) findViewById(R.id.registrationEnerSecondPassword);
		registrationButton = (Button) findViewById(R.id.registrationRegistrationButton);
		registrationButton.setOnClickListener(new RegistrationOnClickListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}
	
	private class RegistrationOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String login = loginField.getText().toString();
			String fPasswordString = firstPasswordField.getText().toString();
			String sPasswwordString = secondPasswordField.getText().toString();
			if (!fPasswordString.equals(sPasswwordString)) {
			    Toast.makeText(getApplicationContext(), R.string.registration_passwordNotEqual, 
			    		Toast.LENGTH_LONG).show();
			    return;
			}
			if (fPasswordString.length() < 6) {
				Toast.makeText(getApplicationContext(), R.string.registration_passwordToShort, 
						Toast.LENGTH_LONG).show();
				return;
			}
			try {
				final Properties connectionProperties = new Properties();
				connectionProperties.load(getAssets().open("connection.properties"));
				String serverUrl = connectionProperties.getProperty("server_url")
						+ connectionProperties.getProperty("registration");
				new RegistrationTask(thisActivity).execute(login, firstPasswordField.getText().toString(), serverUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
