package com.example.headsupnotification;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;

/**
 * Description:
 * Author: qiubing
 * Date: 2017-08-21 14:32
 */
public class HeadsUpWindow extends RelativeLayout {
    private static final String TAG = "HeadsUpWindow";

    private static final int HEADS_UP_WINDOW_HEIGHT = 210;
    private static final int HEADS_UP_WINDOW_WIDTH = 780;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private static int statusBarHeight;
    private float xInScreen;
    private float yInScreen;
    private float xDownInScreen;
    private float yDownInScreen;
    private float xInView;
    private float yInView;

    private int mScreenWidth;
    private int mScreenHeight;

    private Context mContext;
    private volatile boolean mShouldIntercepted = false;

    public HeadsUpWindow(Context context){
        this(context,null);
    }

    public HeadsUpWindow(Context context,AttributeSet attrs){
        this(context, attrs, 0);
    }

    public HeadsUpWindow(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        statusBarHeight = getStatusBarHeight(context);
        updateScreenSize();
        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mParams.gravity = Gravity.LEFT | Gravity.BOTTOM;//以左下角为参考点，避免全屏时坐标计算不准确
        mParams.height = HEADS_UP_WINDOW_HEIGHT;
        mParams.width = HEADS_UP_WINDOW_WIDTH;
        mParams.x = (mScreenWidth - HEADS_UP_WINDOW_WIDTH)/2;
        mParams.y = (mScreenHeight - statusBarHeight - HEADS_UP_WINDOW_HEIGHT) / 2;
    }

    public WindowManager.LayoutParams getWindowParams(){
        return mParams;
    }

    public void updateScreenSize(){
        mScreenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
    }


    public boolean handleTouchEvent(MotionEvent event) {
        xInScreen = event.getRawX();
        yInScreen = event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG,"onTouchEvent()...ACTION_DOWN");
                xInView = event.getX();
                yInView = event.getY();

                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY();
                mShouldIntercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e(TAG,"onTouchEvent()...ACTION_MOVE");
                updateViewPosition();
                mShouldIntercepted = false;
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"onTouchEvent()...ACTION_UP");
                if (getPointDistance(xDownInScreen,yDownInScreen,xInScreen,yInScreen) < 15.0f){
                    Log.e(TAG,"this is the onClick event ");
                    mShouldIntercepted = false;
                }else {
                    mShouldIntercepted = true;
                }
                break;
        }
        return mShouldIntercepted;
    }

   @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
       boolean intercepted = handleTouchEvent(motionEvent);
       Log.e(TAG,"dispatchTouchEvent()...intercepted = " + intercepted);
       return !intercepted && super.dispatchTouchEvent(motionEvent);
    }

    private void updateViewPosition(){
        mParams.x = (int)(xInScreen - xInView);
        mParams.y = (int)(mScreenHeight - yInScreen - (getMeasuredHeight() - yInView));
        mWindowManager.updateViewLayout(this,mParams);
    }


    private static int  getStatusBarHeight(Context context){
        if (statusBarHeight == 0){
            try {
                Class<?> cls = Class.forName("com.android.internal.R$dimen");
                Object object = cls.newInstance();
                Field field = cls.getField("status_bar_height");
                int x = (Integer) field.get(object);
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    private float getPointDistance(float x1,float y1,float x2,float y2){
        return (float) Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
    }
}
