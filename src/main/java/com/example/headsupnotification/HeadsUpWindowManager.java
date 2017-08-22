package com.example.headsupnotification;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Description:
 * Author: qiubing
 * Date: 2017-08-21 14:58
 */
public class HeadsUpWindowManager {
    private static final String TAG = "HeadsUpWindowManager";
    private static HeadsUpWindow mHeadsUpWindow;
    private static WindowManager.LayoutParams mParams;
    private static OrientationEventListener mOrientationEventListener;

    public static void createHeadsUpWindow(Context context){
        Log.e(TAG, "createHeadsUpWindow()...");
        if (mHeadsUpWindow == null){
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            mHeadsUpWindow = (HeadsUpWindow) LayoutInflater.from(context).inflate(R.layout.heads_up_window,null);
            mParams = mHeadsUpWindow.getWindowParams();
            initViews(context, mHeadsUpWindow);
            windowManager.addView(mHeadsUpWindow,mParams);
            mOrientationEventListener = new OrientationEventListener(context) {
                @Override
                public void onOrientationChanged(int orientation) {
                    if (mHeadsUpWindow != null && orientation != ORIENTATION_UNKNOWN){
                        // 屏幕旋转，更新屏幕的长和宽
                        mHeadsUpWindow.updateScreenSize();
                    }
                }
            };
            mOrientationEventListener.enable();
        }
    }

    public static void removeSmallWindow(Context context){
        Log.e(TAG,"removeSmallWindow()...");
        if (mHeadsUpWindow != null){
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            mOrientationEventListener.disable();
            windowManager.removeView(mHeadsUpWindow);
            mHeadsUpWindow = null;
        }
    }

    private static void initViews(final Context context,View view){
        ImageButton mCallBtn = (ImageButton) view.findViewById(R.id.call_Button);
        ImageButton mEndBtn = (ImageButton)view.findViewById(R.id.end_Button);
        RelativeLayout mCallInfoLayout = (RelativeLayout) view.findViewById(R.id.callInfoLayout);
        mCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"mCallBtn...onClick");
                Toast.makeText(context,"Call Button Clicked",Toast.LENGTH_LONG).show();
            }
        });

        mEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"mEndBtn...onClick");
                Toast.makeText(context,"End Button Clicked",Toast.LENGTH_LONG).show();
            }
        });

        mCallInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "mCallInfoLayout...onClick");
                Toast.makeText(context,"CallInfoLayout Clicked",Toast.LENGTH_LONG).show();
            }
        });
    }
}
