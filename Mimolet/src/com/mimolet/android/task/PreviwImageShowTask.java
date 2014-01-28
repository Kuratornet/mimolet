package com.mimolet.android.task;

import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.mimolet.android.PurchaseActivity;

public class PreviwImageShowTask extends AsyncTask<String, Void, Bitmap> {
	private static final String TAG = "PreviwImageShowTask";
	private PurchaseActivity parent;
	private final ProgressDialog dialog;

	public PreviwImageShowTask(Activity activity) {
		this.parent = (PurchaseActivity) activity;
		dialog = new ProgressDialog(this.parent);
	}

	@Override
	protected void onPreExecute() {
		this.dialog.setMessage("Loaddin info...");
		this.dialog.setCancelable(false);
		this.dialog.show();
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		try {
			InputStream in = new java.net.URL(params[0]).openStream();
			Bitmap coverFromURL = BitmapFactory.decodeStream(in);
			return coverFromURL;
		} catch (Exception e) {
			Log.e(TAG, "Something goes wrong!");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
		parent.setCoverImage(result);
	}

}
