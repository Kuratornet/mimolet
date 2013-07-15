package com.mimolet.android.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import com.mimolet.android.Constants;
import com.mimolet.android.R;
import com.mimolet.android.global.GlobalVariables;
import com.mimolet.android.task.SendPDFTask;

import entity.Order;

public class GalleryPhotoFragment extends Fragment {
  
  Activity activity;
  
  private List<String> urls = new ArrayList<String>();
  //private int imageCounter = 0;
  
  private static int RESULT_LOAD_IMAGE = 666;
  
  private int imageCounter = 0;
  LinearLayout myGallery;
  
  public GalleryPhotoFragment (final Activity activity) {
    this.activity = activity;
    
    myGallery = (LinearLayout) activity.findViewById(R.id.mygallery);

    final Button buttonLoadImage = (Button) activity.findViewById(R.id.buttonLoadPicture);
    buttonLoadImage.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        if (imageCounter < 20) {

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
        } else {
          Toast.makeText(activity.getApplicationContext(), "Вы уже загрузили 20 снимков. \n Перейдите в раздел оформления.", Toast.LENGTH_LONG).show();
        }
      }
    });

    final Button createPDF = (Button) activity.findViewById(R.id.buttonCreatePdf);
    createPDF.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        final int SIDE_SIZE = 576;
        final int BORDER_SIZE = 43;
        Document document = new Document(new Rectangle(SIDE_SIZE, SIDE_SIZE));
        document.setMargins(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE);
        final String targetFile =
            android.os.Environment.getExternalStorageDirectory() + java.io.File.separator + "Images.pdf";
        final String previewFile =
            android.os.Environment.getExternalStorageDirectory() + java.io.File.separator + "preview.png";
        try {

          File f = new File(targetFile);
          if (!f.exists()) {
            f.createNewFile();
          }
          File preview = new File(previewFile);
          if (!preview.exists()) {
            preview.createNewFile();
          }
          final String previewUrl = urls.get(0);
          final Bitmap previewBitmap = decodeSampledBitmapFromFile(previewUrl, 200, 200);
          FileOutputStream fOut = new FileOutputStream(preview);

          previewBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
          fOut.flush();
          fOut.close();
          previewBitmap.recycle();
          PdfWriter.getInstance(document, new FileOutputStream(targetFile));
          document.open();
          for (String url : urls) {
            final Bitmap bitmap = decodeSampledBitmapFromFile(url, SIDE_SIZE, SIDE_SIZE);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG /* FileType */, 100 /* Ratio */, stream);
            Image png = Image.getInstance(stream.toByteArray());
            png.setAlignment(Image.ALIGN_MIDDLE);
            png.scaleToFit(SIDE_SIZE - 2 * BORDER_SIZE, SIDE_SIZE - 2 * BORDER_SIZE);
            document.add(png);
            bitmap.recycle();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        document.close();
        
        final File toBeCleared = new File(GlobalVariables.IMAGE_FOLDER);
        File[] listOfFiles = toBeCleared.listFiles();
        for (File file : listOfFiles) {
          if (file.isFile()) {
            file.delete();
          }
        }
        
        new SendPDFTask(activity, targetFile, previewFile, (Order) activity.getIntent().getSerializableExtra(
            Constants.BUNDLE_ORDER)).execute(new Void[0]);
      }
    });
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.activity_phone_gallery,
        container, false);
    
    return view;
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

  public String getPath(Uri uri) {
    String[] projection = { MediaStore.Images.Media.DATA };
    Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
    activity.startManagingCursor(cursor);
    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();
    return cursor.getString(column_index);
  }

  private void saveImage(Bitmap finalBitmap) {
    File myDir = new File(GlobalVariables.IMAGE_FOLDER);
    myDir.mkdirs();
    String fname = "Image-" + (imageCounter) + ".png";
    File file = new File(myDir, fname);
    if (file.exists())
      file.delete();
    try {
      FileOutputStream out = new FileOutputStream(file);
      finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
      out.flush();
      out.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Method convert bitmap from some path to View.
   *
   * @param String path to source file.
   * @return View, who can be use in ImageView.
   */
  private View insertPhoto(String path) {
    Bitmap bm = decodeSampledBitmapFromFile(path, 220, 165);

    LinearLayout layout = new LinearLayout(activity.getApplicationContext());
    layout.setLayoutParams(new LayoutParams(240, 180));
    layout.setGravity(Gravity.CENTER);

    ImageView imageView = new ImageView(activity.getApplicationContext());
    imageView.setLayoutParams(new LayoutParams(220, 165));
    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    imageView.setImageBitmap(bm);

    layout.addView(imageView);
    return layout;
  }

  private void reviewFolder () {
    final String imageFolderPath = GlobalVariables.IMAGE_FOLDER;
    final File imageFolder = new File(imageFolderPath);
    File[] listOfFiles = imageFolder.listFiles();
    if (listOfFiles[imageCounter].isFile()) {
      myGallery.addView(insertPhoto(listOfFiles[imageCounter].getAbsolutePath()));
    }
    imageCounter++;
//    for (File file : listOfFiles) {
//      if (file.isFile()) {
//        myGallery.addView(insertPhoto(file.getAbsolutePath()));
//      }
//    }
  }
}
