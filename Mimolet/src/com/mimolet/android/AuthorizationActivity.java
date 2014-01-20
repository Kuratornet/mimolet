package com.mimolet.android;

import java.util.Arrays;
import java.util.Properties;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.facebook.FacebookException;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;
import com.mimolet.android.global.GlobalMethods;
import com.mimolet.android.task.AuthorizationTask;
import com.sromku.simple.fb.Permissions;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnProfileRequestListener;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Profile;

public class AuthorizationActivity extends SherlockActivity {

  private static final String TAG = "AuthorizationActivity";
  /*private GraphUser user;*/
  private EditText loginField;
  private EditText passwordField;
  private LoginButton facebookButton;
  private SimpleFacebook mSimpleFacebook;
  OnLoginListener onLoginListener = new OnLoginListener() {
      @Override
      public void onFail(String reason) {
          Log.w(TAG, reason);
      }

      @Override
      public void onException(Throwable throwable) {
          Log.e(TAG, "Bad thing happened", throwable);
      }

      @Override
      public void onThinking() {
          // show progress bar or something to the user while login is happening
          Log.i(TAG, "In progress");
      }

      @Override
      public void onLogin() {
          // change the state of the button or do whatever you want
          Log.e(TAG, "Logged in");
          mSimpleFacebook.getProfile(new OnProfileRequestListener()
          {
              @Override
              public void onComplete(Profile profile) {
                  String email = profile.getEmail();
                  if (email != null) {
                	  authorization(email, AuthorizationActivity.this.getResources().getString(R.string.social_password), "facebook");
                  }
              }

			@Override
			public void onThinking() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onException(Throwable throwable) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onFail(String reason) {
				// TODO Auto-generated method stub
				
			}
          });
      }

      @Override
      public void onNotAcceptingPermissions() {
          Log.w(TAG, "User didn't accept read permissions");
      }

  };
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
    mSimpleFacebook = SimpleFacebook.getInstance(this);
    Permissions[] permissions = new Permissions[] {
    		    Permissions.USER_PHOTOS,
    		    Permissions.EMAIL,
    		    Permissions.PUBLISH_ACTION
    		};
    @SuppressWarnings("unused")
	SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
    .setPermissions(permissions).build();
    
//    actionBar = getSupportActionBar();
    
    loginField = (EditText) findViewById(R.id.loginField);
    passwordField = (EditText) findViewById(R.id.passwordField);
    facebookButton = (LoginButton) findViewById(R.id.facebookLoginButton);
    facebookButton.setOnErrorListener(new OnErrorListener() {
    	@Override
    	public void onError(FacebookException error) {
    		Log.i(TAG, "Error " + error.getMessage());
    	}
    });
    facebookButton.setReadPermissions(Arrays.asList("basic_info","email"));
    facebookButton.setOnClickListener(new OnClickListener() {
    	@Override
		public void onClick(View v) {
    		/*Intent faceBookAuthIntent = new Intent(AuthorizationActivity.this, FaceBookAuth.class);
			startActivity(faceBookAuthIntent);*/
    		mSimpleFacebook.login(onLoginListener);
		}
	});
    /* facebookButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
        @Override
        public void onUserInfoFetched(GraphUser user) {
            AuthorizationActivity.this.user = user;
            // It's possible that we were waiting for this.user to be populated in order to post a
            // status update.
        }
    }); */
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
	  authorization(loginField.getText().toString(), passwordField.getText().toString(), "casual");
  }
  
  public void authorization(String userLogin, String userPassword, String type) {
	  if (GlobalMethods.isOnline(this)) {
	      final Properties connectionProperties = new Properties();
	      try {
	        connectionProperties.load(getAssets().open("connection.properties"));
	        final String serverUrl = connectionProperties.getProperty("server_url")
	            + connectionProperties.getProperty("login_path");
	        new AuthorizationTask(this).execute(userLogin, userPassword, serverUrl, type);
	      } catch (Exception ex) {
	        Log.v(TAG, "Could not read connection configuration", ex);
	      }
	    } else {
	      Toast.makeText(getApplicationContext(), R.string.loose_internet_connection, Toast.LENGTH_LONG).show();
	    }
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data); 
      super.onActivityResult(requestCode, resultCode, data);
  } 
}
