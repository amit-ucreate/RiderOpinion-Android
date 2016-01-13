package com.nutsuser.ridersdomain.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by user on 8/13/2015.
 */
public class ApplicationGlobal {

    public static String baseurl_sigup="riders/signup?";
    public static String ROOT = "http://ridersopininon.herokuapp.com/index.php/";

    public static boolean taskscreen=false;
    public static boolean notificationscreen=false;
    public static boolean documenttask=false;
    public static boolean contactscreen=false;
    //camera params
    public static Bitmap bMapRotate;
    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int CAMERA_REQUEST = 2;
    public static final String JPEG_FILE_PREFIX = "IMG_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";
    public static final String LOCAL_STORAGE_BASE_PATH_FOR_POSTED_PHOTOS = Environment.getExternalStorageDirectory().toString()
            + File.separator + "WYM";
    public static boolean menuhandle = true;
    public static String sToken = "";
    public static int sClientId;
    public static String sPropertyAddress = "";
    public static String rollBarId = "78941b4fa82d4f73965f1957c1f615c6";
    public static boolean sessionexp = false;
    //live 78941b4fa82d4f73965f1957c1f615c6
    //testing 5eb9d7cfba834f0e9aee04c154e53685
    //production 3f3a790e813045e2a6321a1c88e1d17e

    public static String caseIdUsed = "";


    public static File setUpPhotoFile() throws IOException {


        File imageF = null;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {

                File storageDir = new File(
                        LOCAL_STORAGE_BASE_PATH_FOR_POSTED_PHOTOS)
                        .getParentFile();

                if (storageDir != null) {
                    if (!storageDir.mkdirs()) {
                        if (!storageDir.exists()) {
                            Log.d("CameraSample", "failed to create directory");
                            return null;
                        }
                    }
                }
                imageF = File.createTempFile(JPEG_FILE_PREFIX
                                + System.currentTimeMillis() + "_",
                        JPEG_FILE_SUFFIX, storageDir);
            } else {
                Log.v("image loading status",
                        "External storage is not mounted READ/WRITE.");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageF;

    }

    public static Bitmap getFile(String imgPath, Context context) {
        try {

            if (imgPath != null) {
                ExifInterface exif = new ExifInterface(imgPath);

                int mOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 1);

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imgPath, options);


                options.inSampleSize = calculateInSampleSize(options, 400, 400);
                options.inJustDecodeBounds = false;

                bMapRotate = BitmapFactory.decodeFile(imgPath, options);
                if (mOrientation == 6) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bMapRotate = Bitmap.createBitmap(bMapRotate, 0, 0,
                            bMapRotate.getWidth(), bMapRotate.getHeight(),
                            matrix, true);
                } else if (mOrientation == 8) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(270);
                    bMapRotate = Bitmap.createBitmap(bMapRotate, 0, 0,
                            bMapRotate.getWidth(), bMapRotate.getHeight(),
                            matrix, true);
                } else if (mOrientation == 3) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(180);
                    bMapRotate = Bitmap.createBitmap(bMapRotate, 0, 0,
                            bMapRotate.getWidth(), bMapRotate.getHeight(),
                            matrix, true);
                }


            } else {
                Toast.makeText(
                        context,
                        "There might be some problem in fetching photo.. please try again.",
                        Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bMapRotate;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        try {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 4;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //check internet connection
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
// There are no active networks.
            return false;
        } else
            return true;
    }


}
