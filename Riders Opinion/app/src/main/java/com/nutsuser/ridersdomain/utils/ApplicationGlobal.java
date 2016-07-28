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

import com.appeaser.sublimepickerlibrary.utilities.Config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Amit Agnihotri on 8/13/2015.
 */
public class ApplicationGlobal {
    public static boolean TAKE_TOUR=false;
    public static boolean FIRST_TAKE_TOUR=false;
    public static String DestID="";
    public static final String YOUTUBE_API_KEY = "AIzaSyDpKUbvuTh7erR-0GRxPvQ5mwGQ2E9XkDQ";
    public static final String TAG_GRAPH_OBJECT = "graphObject";
    public static final String TAG_ACCOUNT_ID = "id";
    public static final String TAG_FIRST_NAME = "first_name";
    public static final String TAG_LAST_NAME = "last_name";
    public static final String TAG_USER_NAME = "name";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_CITY = "locale";
    public static final String TAG_CONTACT = "contact";
    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int CAMERA_REQUEST = 2;
    public static final String JPEG_FILE_PREFIX = "IMG_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";
    public static final String LOCAL_STORAGE_BASE_PATH_FOR_POSTED_PHOTOS = Environment.getExternalStorageDirectory() +
            File.separator + "RiderOpinion";
    public static String baseurl_favroite = "ridingDestination/favroite?";
    public static String baseurl_like = "ridingDestination/likeDestination?";

    //https://ridersapi.herokuapp.com/riders/signup?utypeid=sarita@ucreate.co.in&latitude=30.7103887&longitude=76.7074065&baseLocation=Sahibzada%20Ajit%20Singh%20Nagar&userName=test2&deviceToken=eZCYu7WsId8:APA91bEfWhGYVNJyt1rJnatE3Ehi_FECqGV0DQ5TNeAt9vo_EzF2qET88AB5d2zOLNUDOe861bV0rjqfITiuQ6as0kvQ4Q9N4mr7Xj6yzz9DtgRzJCqFfimWF4Q_frXThCTB8YRDa2Rj&OS=Android&loginType=default
    public static String baseurl_sigup = "riders/signup?";
    public static String baseurl_search = "ridingDestination/search?";
    public static String baseurl_updateUserInfo = "riders/updateUserInfo?";
    public static String baseurl_ridingdestination = "ridingDestination?";
    public static String baseurl_ridingevent = "rideEvent?";
    public static String baseurl_trackuserlist = "track/trackUser?";

    public static String baseurl_matchevent = "rideEvent/matchEvent?";
    public static String baseurl_eventdetails = "rideEvent/eventDetail?";
    public static String baseurl_joinEvent = "rideEvent/joinEvent?";

    public static String baseurl_postride = "rideEvent/postRide";

    public static String baseurl_mapinfo = "map/saveCounter";
    public static String baseurl_ridingdetailDestination = "ridingDestination/detailDestination?";
    public static String baseurl_ridingFilter = "ridingDestination?";

    //
    public static String baseurl_ridingSubcribedetails = "ridingDestination/ridingDestinationAfterSubscribe?";
    public static String baseurl_vehicle = "riders/vehicle";
    public static String baseurl_model = "riders/vehicleId?vehicleId=";
   // public static String ROOT = "http://ridersopinion.herokuapp.com/index.php/";
    public static String ROOT = "https://ridersapi.herokuapp.com/index.php/";

    public static String TAG_LOGIN_FACEBOOK = "facebook";// facebook
    public static String TAG_LOGIN_TWITTER = "twitter";// twitter
    public static String TAG_LOGIN_DEFAULT = "default";// default
    public static boolean taskscreen = false;
    public static boolean notificationscreen = false;
    public static boolean documenttask = false;
    public static boolean contactscreen = false;
    //camera params
    public static Bitmap bMapRotate;
    public static boolean menuhandle = true;
    public static String sToken = "";
    public static int sClientId;
    public static String sPropertyAddress = "";

    /*=========Rollbar Stagging key================*/
    public static String rollBarId = "fa1e2ba931d447f79985cf3dad4c3c16";
    /*=========Rollbar Production key================*/
 //  public static String rollBarId = "6da1886940294e8197d53581a6ca5cda";

    public static boolean sessionexp = false;
    //CLient side: fa1e2ba931d447f79985cf3dad4c3c16
    //Server Side: 96887c40c55c4818bb157a20a2111c9c

    public static String caseIdUsed = "";


    public static File setUpPhotoFile() throws IOException {


        File imageF = null;
        File secondfolder= null;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {

               /* File storageDir = new File(
                        LOCAL_STORAGE_BASE_PATH_FOR_POSTED_PHOTOS)
                        .getParentFile();*/
                File folder = new File(LOCAL_STORAGE_BASE_PATH_FOR_POSTED_PHOTOS);
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdir();
                    Log.e("createFolder1", "" + success);
                }

                Log.e("createFolder2", "" + success);
                if (success) {
                     secondfolder = new File(Environment.getExternalStorageDirectory() +
                            File.separator + "RiderOpinion" + File.separator + "Image");
                    Log.e("createAnotherFolder", "" + secondfolder);
                    boolean successsecond = true;
                    if (!secondfolder.exists()) {
                        successsecond = secondfolder.mkdir();
                        Log.e("createAnotherFolder1", "" + successsecond);
                    }
                    Log.e("createAnotherFolder2", "" + successsecond);
                    if (successsecond) {
                        //persistImage(bitmap, "" + System.currentTimeMillis());
                    } else {
                        // persistImage(bitmap, "" + System.currentTimeMillis());
                    }
                } else {
                    secondfolder = new File(Environment.getExternalStorageDirectory() +
                            File.separator + "RiderOpinion" + File.separator + "Image");
                    Log.e("createAnotherFolder", "" + secondfolder);
                    boolean successsecond = true;
                    if (!secondfolder.exists()) {
                        successsecond = secondfolder.mkdir();
                    }
                    Log.e("createAnotherFolder", "" + successsecond);
                    if (successsecond) {

                    } else {

                    }

                }

                imageF = File.createTempFile(JPEG_FILE_PREFIX
                                + System.currentTimeMillis() + "_",
                        JPEG_FILE_SUFFIX, secondfolder);

                Log.e("imageF",""+imageF.getAbsolutePath());
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
    public static File bitmapToFile(Bitmap bmp) {
        File imageF = null;
        try {
            File folder = new File(LOCAL_STORAGE_BASE_PATH_FOR_POSTED_PHOTOS);
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            Log.e("createFolder", "" + success);
            if (success) {
                imageF = File.createTempFile(JPEG_FILE_PREFIX
                                + System.currentTimeMillis() + "_",
                        JPEG_FILE_SUFFIX, folder);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, bos);
                byte[] bArr = bos.toByteArray();
                bos.flush();
                bos.close();
                FileOutputStream fos=new FileOutputStream(imageF);
                fos.write(bArr);
                fos.flush();
                fos.close();
                return imageF;
            } else {
                imageF = File.createTempFile(JPEG_FILE_PREFIX
                                + System.currentTimeMillis() + "_",
                        JPEG_FILE_SUFFIX, folder);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, bos);
                byte[] bArr = bos.toByteArray();
                bos.flush();
                bos.close();
                FileOutputStream fos=new FileOutputStream(imageF);
                fos.write(bArr);
                fos.flush();
                fos.close();
                return imageF;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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


                options.inSampleSize = calculateInSampleSize(options, 350, 350);
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inDither = true;

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
            int inSampleSize = 3;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

//                while ((halfHeight / inSampleSize) > reqHeight
//                        && (halfWidth / inSampleSize) > reqWidth) {
//                    inSampleSize *= 2;
//                }
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
