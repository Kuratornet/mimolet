package com.mimolet.android.multiphoto;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.mimolet.android.R;
import com.mimolet.android.global.GlobalVariables;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class MultiPhotoSelectActivity extends BaseActivity {
  private ArrayList<String> imageUrls;
  private DisplayImageOptions options;
  private ImageAdapter imageAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ac_image_grid);

    ImageLoaderConfiguration config =
        new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPoolSize(3)
            .threadPriority(Thread.NORM_PRIORITY - 2).memoryCacheSize(1500000)
            // 1.5 Mb
            .denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
            .enableLogging() // Not necessary in common
            .build();
    // Initialize ImageLoader with configuration.
    ImageLoader.getInstance().init(config);

    final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
    final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
    Cursor imagecursor =
        managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");

    this.imageUrls = new ArrayList<String>();

    for (int i = 0; i < imagecursor.getCount(); i++) {
      imagecursor.moveToPosition(i);
      int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
      imageUrls.add(imagecursor.getString(dataColumnIndex));
    }

    options =
        new DisplayImageOptions.Builder().showStubImage(R.drawable.stub_image)
            .showImageForEmptyUri(R.drawable.image_for_empty_url).cacheInMemory().cacheOnDisc().build();

    imageAdapter = new ImageAdapter(this, imageUrls);

    GridView gridView = (GridView) findViewById(R.id.gridview);
    gridView.setAdapter(imageAdapter);
    /*
     * gridView.setOnItemClickListener(new OnItemClickListener() {
     *
     * @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
     * startImageGalleryActivity(position); } });
     */
  }

  @Override
  protected void onStop() {
    imageLoader.stop();
    super.onStop();
  }

  public void btnChoosePhotosClick(View v) {
    ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
    for (int i = 0; i < selectedItems.size(); i++) {
      saveImage(decodeSampledBitmapFromFile(selectedItems.get(i), 1024, 1024), i);
    }
    Intent intent = new Intent();
    setResult(RESULT_OK, intent);
    finish();
    /*ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
    Toast.makeText(MultiPhotoSelectActivity.this, "Total photos selected: " + selectedItems.size(), Toast.LENGTH_SHORT)
        .show();
    Log.d(MultiPhotoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());*/
  }

  /*
   * private void startImageGalleryActivity(int position) { Intent intent = new Intent(this, ImagePagerActivity.class);
   * intent.putExtra(Extra.IMAGES, imageUrls); intent.putExtra(Extra.IMAGE_POSITION, position); startActivity(intent); }
   */

  public class ImageAdapter extends BaseAdapter {

    ArrayList<String> mList;
    LayoutInflater mInflater;
    Context mContext;
    SparseBooleanArray mSparseBooleanArray;

    public ImageAdapter(Context context, ArrayList<String> imageList) {
      // TODO Auto-generated constructor stub
      mContext = context;
      mInflater = LayoutInflater.from(mContext);
      mSparseBooleanArray = new SparseBooleanArray();
      mList = new ArrayList<String>();
      this.mList = imageList;

    }

    public ArrayList<String> getCheckedItems() {
      ArrayList<String> mTempArry = new ArrayList<String>();

      for (int i = 0; i < mList.size(); i++) {
        if (mSparseBooleanArray.get(i)) {
          mTempArry.add(mList.get(i));
        }
      }

      return mTempArry;
    }

    @Override
    public int getCount() {
      return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
      return null;
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

      if (convertView == null) {
        convertView = mInflater.inflate(R.layout.row_multiphoto_item, null);
      }

      CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
      final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);

      imageLoader.displayImage("file://" + imageUrls.get(position), imageView, options,
          new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(Bitmap loadedImage) {
              Animation anim = AnimationUtils.loadAnimation(MultiPhotoSelectActivity.this, R.anim.fade_in);
              imageView.setAnimation(anim);
              anim.start();
            }
          });

      mCheckBox.setTag(position);
      mCheckBox.setChecked(mSparseBooleanArray.get(position));
      mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);

      return convertView;
    }

    OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
      }
    };
  }
  
  private void saveImage(Bitmap finalBitmap, int position) {
    File myDir = new File(GlobalVariables.IMAGE_FOLDER);
    myDir.mkdirs();
    String fname = "Image-" + (position) + ".png";
    File file = new File(myDir, fname);
    if (file.exists()) {
      file.delete();
    }
    try {
      FileOutputStream out = new FileOutputStream(file);
      finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
      out.flush();
      out.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private Bitmap decodeSampledBitmapFromFile(String filepath, int reqWidth, int reqHeight) {

    // First decode with inJustDecodeBounds=true to check dimensions
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(filepath, options);

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(filepath, options);
  }
  
  private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

      // Calculate ratios of height and width to requested height and
      // width
      final int heightRatio = Math.round((float) height / (float) reqHeight);
      final int widthRatio = Math.round((float) width / (float) reqWidth);

      // Choose the smallest ratio as inSampleSize value, this will
      // guarantee
      // a final image with both dimensions larger than or equal to the
      // requested height and width.
      inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }

    return inSampleSize;
  }
}