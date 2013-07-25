package com.mimolet.android;

import java.util.Properties;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.mimolet.android.global.GlobalMethods;
import com.mimolet.android.task.AuthorizationTask;

public class AuthorizationActivity extends SherlockActivity {

  private static final String TAG = "AuthorizationActivity";
  private EditText loginField;
  private EditText passwordField;
//  private ActionBar actionBar;
//  private PopupWindow pwindo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authorization);
    
    if( getIntent().getBooleanExtra("Exit me", false)){
      finish();
      return; // add this to prevent from doing unnecessary stuffs
    }
    
//    actionBar = getSupportActionBar();
    
    loginField = (EditText) findViewById(R.id.loginField);
    passwordField = (EditText) findViewById(R.id.passwordField);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(R.string.action_settings).setIcon(R.drawable.ic_settings).setOnMenuItemClickListener(new OnMenuItemClickListener() {
      
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        Intent intent = new Intent(AuthorizationActivity.this, SettingsActivity.class);
        startActivityForResult(intent, 1);
        return true;
      }
    }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    return true;
  }
  
  @Override
  public final boolean onKeyDown(int keyCode, KeyEvent event) {
    if ((keyCode == KeyEvent.KEYCODE_MENU)) {
      Intent intent = new Intent(AuthorizationActivity.this, SettingsActivity.class);
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
    } else {
      Toast.makeText(getApplicationContext(), R.string.loose_internet_connection, Toast.LENGTH_LONG).show();
    }
  }

}
