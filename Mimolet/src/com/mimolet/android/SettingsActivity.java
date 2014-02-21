package com.mimolet.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.mimolet.android.global.GlobalVariables;

import de.ankri.views.Switch;
import entity.AuthorizationSettings;
import entity.AuthorizationType;

public class SettingsActivity extends Activity {
	
	private AuthorizationSettings authorizationSettings;
	private Switch enteredChoice;
	private Switch facebookChoiser;
	private Switch googlePlusChoiser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppThemeLight);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		enteredChoice = (Switch) findViewById(R.id.entered_name_choiser);
		facebookChoiser = (Switch) findViewById(R.id.facebook_enable_choiser);
		googlePlusChoiser = (Switch) findViewById(R.id.google_plus_enable_choiser);
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
		}
		switch (authorizationSettings.getAuthorizationType()) {
			case UNCHECKED:
				enteredChoice.setChecked(false);
				facebookChoiser.setChecked(false);
				googlePlusChoiser.setChecked(false);
				break;
			case STANDART:
				enteredChoice.setChecked(true);
				facebookChoiser.setChecked(false);
				googlePlusChoiser.setChecked(false);
				break;
			case FACEBOOK:
				enteredChoice.setChecked(false);
				facebookChoiser.setChecked(true);
				googlePlusChoiser.setChecked(false);
				break;
			case GOOGLEPLUS:
				enteredChoice.setChecked(false);
				facebookChoiser.setChecked(false);
				googlePlusChoiser.setChecked(true);
				break;
		}
		setOnClickListeners();
		Button clearSettings = (Button) findViewById(R.id.clearDataButton);
		clearSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				File file = new File(GlobalVariables.MIMOLET_FOLDER + GlobalVariables.AUTH_DATA_FILE);
				if (file.exists()) {
					file.delete();
				}
				enteredChoice.setChecked(false);
				facebookChoiser.setChecked(false);
				googlePlusChoiser.setChecked(false);
			}
		});
	}

	private void setOnClickListeners() {
		enteredChoice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					facebookChoiser.setChecked(false);
					googlePlusChoiser.setChecked(false);
					authorizationSettings.setAuthorizationType(AuthorizationType.STANDART);
				} else {
					authorizationSettings.setAuthorizationType(AuthorizationType.UNCHECKED);
				}
				saveSettings();
			}
		});
		facebookChoiser.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					enteredChoice.setChecked(false);
					googlePlusChoiser.setChecked(false);
					authorizationSettings.setAuthorizationType(AuthorizationType.FACEBOOK);
				} else {
					authorizationSettings.setAuthorizationType(AuthorizationType.UNCHECKED);
				}
				saveSettings();
			}
		});
		googlePlusChoiser.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					enteredChoice.setChecked(false);
					facebookChoiser.setChecked(false);
					authorizationSettings.setAuthorizationType(AuthorizationType.GOOGLEPLUS);
				} else {
					authorizationSettings.setAuthorizationType(AuthorizationType.UNCHECKED);
				}
				saveSettings();
			}
		});
	}

	protected void saveSettings() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(GlobalVariables.MIMOLET_FOLDER + GlobalVariables.AUTH_DATA_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(authorizationSettings);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
