package com.mimolet.android.global;

import android.app.Application;

public class GlobalVariables extends Application {
	private Integer ownerID;

	public Integer getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(Integer ownerID) {
		this.ownerID = ownerID;
	}
	
}
