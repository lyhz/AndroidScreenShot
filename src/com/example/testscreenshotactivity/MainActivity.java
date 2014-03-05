package com.example.testscreenshotactivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener{
    
	private ImageView img_display;
	private Button bt_screenshot;
	
    private Display mDisplay;
    private DisplayMetrics mDisplayMetrics;
    private Matrix mDisplayMatrix;
    private Bitmap mScreenBitmap;
	private WindowManager mWindowManager;
	
	private int SampleNumber = 500;
	private double screenpower = 0.0;
	
	private double rpower = 3.0647e-006;
	private double gpower = 4.4799e-006;
	private double bpower = 6.4045e-006;
	
	
	
	private int[] pixels = new int[SampleNumber];
	
	 private boolean state = false;
	 private int period=1000;
	private final Handler powerHandler = new Handler();
    private final Runnable powerPeriodicTask = new Runnable() {
        public void run() {
            if(!state)
            	return;
            mDisplay.getRealMetrics(mDisplayMetrics);
	        float[] dims = {mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels};
	        float degrees = getDegreesForRotation(mDisplay.getRotation());
	        boolean requiresRotation = (degrees > 0);
	        Log.i("screenshot", "Start the native function");
	        long tstart = System.currentTimeMillis();
	        mScreenBitmap = SurfaceControl.screenshot((int) dims[0], (int) dims[1]);
	        
	        CalculatePower(mScreenBitmap);
	        long tend = System.currentTimeMillis();
	        Log.i("screenshot", "end the native function");
	        Log.i("screen", "the time is "+ String.valueOf(tend-tstart));
	        Log.i("screen", "the power is "+ screenpower);
	        
	        if (mScreenBitmap == null) {
	        	          Log.i("screenshot","we can not get the screenshot");
	        	            return;
	        	        }
          
            powerHandler.postDelayed(powerPeriodicTask, period);
        }
    };     
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        bt_screenshot = (Button)findViewById(R.id.bt_screenshot);
        img_display = (ImageView)findViewById(R.id.img_display);
        
        bt_screenshot.setOnClickListener(this);
        
        mDisplayMatrix = new Matrix();
        
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
        mDisplayMetrics = new DisplayMetrics();
        mDisplay.getRealMetrics(mDisplayMetrics); 
    }

	@Override
	public void onClick(View v) {
		if(v.equals(bt_screenshot)){
	//		mDisplay.getRealMetrics(mDisplayMetrics);
	//        float[] dims = {mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels};
	//        float degrees = getDegreesForRotation(mDisplay.getRotation());
	//        boolean requiresRotation = (degrees > 0);
	/*        if (requiresRotation) {
	            // Get the dimensions of the device in its native orientation
	            mDisplayMatrix.reset();
	            mDisplayMatrix.preRotate(-degrees);
	            mDisplayMatrix.mapPoints(dims);
	            dims[0] = Math.abs(dims[0]);
	            dims[1] = Math.abs(dims[1]);
	        }*/
	//        Log.i("screenshot", "Start the native function");
	//        long tstart = System.currentTimeMillis();
	//        mScreenBitmap = SurfaceControl.screenshot((int) dims[0], (int) dims[1]);
	        
	//        CalculatePower(mScreenBitmap);
	//        long tend = System.currentTimeMillis();
	//        Log.i("screenshot", "end the native function");
	//        Log.i("screen", "the time is "+ String.valueOf(tend-tstart));
	 //       Log.i("screen", "the power is "+ screenpower);
	  /*      if (requiresRotation) {
	            // Rotate the screenshot to the current orientation
	            Bitmap ss = Bitmap.createBitmap(mDisplayMetrics.widthPixels,
	                    mDisplayMetrics.heightPixels, Bitmap.Config.ARGB_8888);
	            Canvas c = new Canvas(ss);
	            c.translate(ss.getWidth() / 2, ss.getHeight() / 2);
	            c.rotate(degrees);
	            c.translate(-dims[0] / 2, -dims[1] / 2);
	            c.drawBitmap(mScreenBitmap, 0, 0, null);
	            c.setBitmap(null);
	            mScreenBitmap = ss;
	        }
*/
			powerHandler.postDelayed(powerPeriodicTask, period);   
	        state = true;
	        // If we couldn't take the screenshot, notify the user
	//        if (mScreenBitmap == null) {
	//           Log.i("screenshot","we can not get the screenshot");
	//            return;
	//        }

	        // Optimizations
/*	        mScreenBitmap.setHasAlpha(false);
	        mScreenBitmap.prepareToDraw();
	        
	     //   CalculatePower(mScreenBitmap);
			
	        img_display.setImageBitmap(mScreenBitmap);*/
		}
		
	}
	
	private void CalculatePower(Bitmap bitmap){

	       int pi = 0;
	       int h = bitmap.getHeight();
	       int hi = h/20;
	       int w = bitmap.getWidth();
	       int wi = w/25;
	    //   Log.i("screen", "The height is "+bitmap.getHeight());
	    //   Log.i("screen", "The width is "+bitmap.getWidth());
	       
	        	for(int x = w/2; x<bitmap.getWidth(); x=x+wi)
	        	 for(int y = h/2; y<bitmap.getHeight(); y=y+hi)
	        		 {
	        		 	pixels[pi] = mScreenBitmap.getPixel(x, y);
	        		 	pi++;
	        		 //	Log.i("screen", "The position is "+x+", "+y);
	        		 }
	       
	       int r=0,g=0,b=0;
	    //   Log.i("screen", "The sample length is "+pixels.length);
	       for(int i = 0; i<pixels.length; i++)
	       {
	    	 //  Log.i("screen", Color.red(pixels[i])+"  "+ Color.green(pixels[i]) + " "+Color.blue(pixels[i]));
	    	   r+=Color.red(pixels[i]);
	    	   g+=Color.green(pixels[i]);
	    	   b+=Color.blue(pixels[i]);
	       }
	       
	       screenpower = (r/SampleNumber*rpower+g/SampleNumber*gpower+b/SampleNumber*bpower)*h*w;
	       
	        	
	}
	
	@Override
	public void onDestroy() {        
		state = false;
        powerHandler.removeCallbacks(powerPeriodicTask);
	    super.onDestroy();
	}
	
	/**
     * @return the current display rotation in degrees
     */
    private float getDegreesForRotation(int value) {
        switch (value) {
        case Surface.ROTATION_90:
            return 360f - 90f;
        case Surface.ROTATION_180:
            return 360f - 180f;
        case Surface.ROTATION_270:
            return 360f - 270f;
        }
        return 0f;
    }
}