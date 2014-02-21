package com.mimolet.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.mimolet.android.global.GlobalMethods;
import com.mimolet.android.global.GlobalVariables;
import com.mimolet.android.task.AuthorizationTask;
import com.mimolet.android.task.SocialAuthTask;

import entity.AuthorizationSettings;
import entity.AuthorizationType;

public class AuthorizationActivity extends SherlockActivity {

	private static final String TAG = "AuthorizationActivity";
	private AuthorizationSettings authorizationSettings;
	private EditText loginField;
	private EditText passwordField;
	private TextView registrationText;
	private ImageView registrationImage;
	private TextView forgetPasswordText;
	private ImageView forgetPasswordImage;
	private Activity thisActivity;
	Button facebook_button;
	Button googleplus_button;
	SocialAuthAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authorization);
		GlobalMethods.checkConnectionToServer(this);
		thisActivity = this;
		if (getIntent().getBooleanExtra("Exit me", false)) {
			finish();
			return;
		}
		adapter = new SocialAuthAdapter(new DialogListener() {
			@Override
			public void onError(SocialAuthError arg0) {
				Log.e(TAG, "Error");
			}

			@Override
			public void onComplete(Bundle arg0) {
				Profile profileMap = adapter.getUserProfile();
				authorizationSettings.setEmail(profileMap.getEmail());
				if (profileMap.getProviderId().equals(new String("facebook"))) {
					authorizationSettings.setAuthorizationType(AuthorizationType.FACEBOOK);
					authorizationSettings.setFacebookValidationID(profileMap.getValidatedId());
					saveSettings(authorizationSettings);
				} else if (profileMap.getProviderId().equals(new String("googleplus"))) {
					authorizationSettings.setAuthorizationType(AuthorizationType.GOOGLEPLUS);
					authorizationSettings.setGooglePlusValdationID(profileMap.getValidatedId());
					saveSettings(authorizationSettings);
				}
				new SocialAuthTask(thisActivity).execute(profileMap.getEmail(),
						profileMap.getValidatedId(),
						adapter.getCurrentProvider().getAccessGrant().getKey(),
						profileMap.getProviderId());
			}

			@Override
			public void onCancel() {
				Log.e(TAG, "Canccel");
			}

			@Override
			public void onBack() {
				Log.e(TAG, "We are back!");
			}
		});

		ObjectInputStream ois;
		try {
			File authorizationSettingsFile = new File(GlobalVariables.MIMOLET_FOLDER + GlobalVariables.AUTH_DATA_FILE);
			if (authorizationSettingsFile.exists()) {
				FileInputStream fis = new FileInputStream(authorizationSettingsFile);
				ois = new ObjectInputStream(fis);
				authorizationSettings = (AuthorizationSettings)ois.readObject();
				ois.close();
			} else {
				authorizationSettings = new AuthorizationSettings();
			}
		} catch (Exception e) {
			e.printStackTrace();
			authorizationSettings = new AuthorizationSettings();
		}
		if (authorizationSettings.getEmail() != null && 
				authorizationSettings.getEmail().length() != 0) {
			switch (authorizationSettings.getAuthorizationType()) {
					case UNCHECKED:
						break;
					case STANDART:
						if (authorizationSettings.getPassword() != null && 
							authorizationSettings.getPassword().length() != 0) {
							final Properties connectionProperties = new Properties();
							try {
								connectionProperties.load(getAssets().open(
										"connection.properties"));
								final String serverUrl = connectionProperties.getProperty("server_url")
										+ connectionProperties.getProperty("login_path");
								new AuthorizationTask(this, authorizationSettings).execute(authorizationSettings.getEmail(), 
										authorizationSettings.getPassword(), serverUrl);
							} catch (Exception e) {
								e.printStackTrace();
							} 
						}
						break;
					case FACEBOOK:
						if (authorizationSettings.getFacebookValidationID() != null && 
						authorizationSettings.getFacebookValidationID().length() != 0) {
						new SocialAuthTask(thisActivity).execute(authorizationSettings.getEmail(),
								authorizationSettings.getFacebookValidationID(),
								"temp", "facebook");
						} else {
							adapter.authorize(AuthorizationActivity.this, Provider.FACEBOOK);
						}
						break;
					case GOOGLEPLUS:
						if (authorizationSettings.getGooglePlusValdationID() != null && 
						authorizationSettings.getGooglePlusValdationID().length() != 0) {
						new SocialAuthTask(thisActivity).execute(authorizationSettings.getEmail(),
								authorizationSettings.getGooglePlusValdationID(),
								"temp", "googleplus");
						} else {
							adapter.authorize(AuthorizationActivity.this, Provider.GOOGLEPLUS);
						}
						break;
				}
		}
		facebook_button = (Button) findViewById(R.id.facebookLoginButton);
		facebook_button.setBackgroundResource(R.drawable.facebookenter);
		facebook_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				adapter.authorize(AuthorizationActivity.this, Provider.FACEBOOK);
			}
		});
		googleplus_button = (Button) findViewById(R.id.googleplusLoginButton);
		googleplus_button.setBackgroundResource(R.drawable.googleplusenter);
		googleplus_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.authorize(AuthorizationActivity.this,
						Provider.GOOGLEPLUS);
			}
		});
		loginField = (EditText) findViewById(R.id.loginField);
		passwordField = (EditText) findViewById(R.id.passwordField);
		registrationImage = (ImageView) findViewById(R.id.registrationImage);
		registrationText = (TextView) findViewById(R.id.registrationTextButton);
		registrationImage
				.setOnClickListener(new RegistrationFieldsOnClickListener());
		registrationText
				.setOnClickListener(new RegistrationFieldsOnClickListener());
		forgetPasswordImage = (ImageView) findViewById(R.id.forgetPasswordImage);
		forgetPasswordText = (TextView) findViewById(R.id.forgetPasswordTextButton);
		forgetPasswordImage.setOnClickListener(new RestorePasswordFieldsOnClickListener());
		forgetPasswordText.setOnClickListener(new RestorePasswordFieldsOnClickListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(R.string.action_settings).setIcon(R.drawable.ic_settings)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Intent intent = new Intent(AuthorizationActivity.this,
								SettingsActivity.class);
						startActivityForResult(intent, 1);
						return true;
					}
				}).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public final boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_MENU)) {
			Intent intent = new Intent(AuthorizationActivity.this,
					SettingsActivity.class);
			startActivityForResult(intent, 1);
			return true;
		} else if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
			return true;
		}
		return true;
	}

	public void authorize(View view) {
		if (GlobalMethods.isOnline(this)) {
			final Properties connectionProperties = new Properties();
			try {
				connectionProperties.load(getAssets().open(
						"connection.properties"));
				final String serverUrl = connectionProperties
						.getProperty("server_url")
						+ connectionProperties.getProperty("login_path");
				new AuthorizationTask(this, authorizationSettings).execute(loginField.getText()
						.toString(), passwordField.getText().toString(),
						serverUrl);
			} catch (Exception ex) {
				Log.v(TAG, "Could not read connection configuration", ex);
			}
		} else {
			Toast.makeText(getApplicationContext(),
					R.string.loose_internet_connection, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class RegistrationFieldsOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(AuthorizationActivity.this,
					RegistrationActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	private class RestorePasswordFieldsOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(AuthorizationActivity.this,
					RestorePasswordActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	public void saveSettings(AuthorizationSettings authorizationSettingsToSave) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(GlobalVariables.MIMOLET_FOLDER + GlobalVariables.AUTH_DATA_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(authorizationSettingsToSave);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
