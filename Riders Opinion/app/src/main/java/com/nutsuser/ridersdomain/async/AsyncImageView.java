package com.nutsuser.ridersdomain.async;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.http.FileAsyncHttpResponseHandler;
import com.nutsuser.ridersdomain.restclient.LIRestClient;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import java.io.File;
import java.io.InputStream;
import org.apache.http.Header;


public class AsyncImageView extends FrameLayout {

	private static final String TAG = AsyncImageView.class.getName();
	
	ProgressBar progressBar;

	Context c;
	InputStream input;
	public ImageView imageView;
	String fileName;
	String URL;
	
	public String strDefaultImage="app_icon";
	
	public AsyncImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		c = context;

		progressBar = new ProgressBar(c, null, android.R.attr.progressBarStyleSmall);
		LayoutParams params = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, Gravity.TOP | Gravity.LEFT);

		imageView = new ImageView(c);
		imageView.setScaleType(ScaleType.FIT_XY);

		//imageView.setBackgroundColor(color.darker_gray);
		addView(imageView, params);
		//

		params = new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		addView(progressBar, params);
		progressBar.setVisibility(View.INVISIBLE);
		
	}
	
	public void setDefaultImage() {
		try {
			int resID = getResources().getIdentifier(strDefaultImage,  "drawable", c.getPackageName());
			imageView.setImageResource(resID);
		}catch(NullPointerException e){

		}
	}
	
	public AsyncImageView(Context context) {
		this(context, null);		
	}
	///this function is for get rounded bitmap
	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		Log.e("scaleBitmapImage:",""+scaleBitmapImage);

		int targetWidth = 70;
		int targetHeight = 70;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
				targetHeight, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth),
						((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap,
				new Rect(0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight()),
				new Rect(0, 0, targetWidth, targetHeight), null);
		return targetBitmap;
	}

	public void downloadImage(String url) {
		URL = url;
		Bitmap myBitmap = null;
		//System.out.println("helo this is url -> " + URL);
		fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
		File cacheDir = getContext().getExternalCacheDir();
		File f = new File(cacheDir, fileName);
		if (f.exists()) {
			System.out.println("getting image from folder " + f);
			myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
			if(myBitmap==null){
				imageView.setImageResource(R.drawable.app_icon);
			}
			else{
				Bitmap roundbit=getRoundedShape(myBitmap);
				imageView.setImageBitmap(roundbit);
			}


		} else {
			System.out.println("getting image from url" + f);
			
			progressBar.setVisibility(View.VISIBLE);
			
			LIRestClient.downloadFile(url, null, new FileAsyncHttpResponseHandler(f) {
				@Override
				public void onSuccess(int statusCode, File file) {
					Log.i(TAG, "onClick onSuccess :" + statusCode);
					
					Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
					if(myBitmap==null){
						imageView.setImageResource(R.drawable.app_icon);
					}
					else{
						Bitmap roundbit=getRoundedShape(myBitmap);
						imageView.setImageBitmap(roundbit);
					}


					
					progressBar.setVisibility(View.GONE);
				}

				@Override
				public void onProgress(int bytesWritten, int totalSize) {
					int totProgress = (bytesWritten * 100) / totalSize;
					if (totProgress > 0) {
						// Log.i(TAG, "totProgress: " + totProgress);
						
					}
				}
				 
				@Override
				public void onFailure(int statusCode, Header[] headers, Throwable e, File response) {
					response.delete();
					Log.i(TAG, "onClick onFailure :" + statusCode+" "+e.getLocalizedMessage());
					e.printStackTrace();
					progressBar.setVisibility(View.GONE);
					
			try {
				int resID = getResources().getIdentifier(strDefaultImage, "drawable", c.getPackageName());
				imageView.setImageResource(resID);
			}catch(NullPointerException e1){

			}
				}
			});
		}
	}
}