package com.mimolet.android.global;

import java.io.File;

import android.app.Application;
import android.os.Environment;

public class GlobalVariables extends Application {
  private Integer ownerID;

  public Integer getOwnerID() {
    return ownerID;
  }

  public void setOwnerID(Integer ownerID) {
    this.ownerID = ownerID;
  }
  
  public static final String IMAGE_FOLDER = Environment.getExternalStorageDirectory().toString() + File.separator
      + "mimolet_images" + File.separator;
}
