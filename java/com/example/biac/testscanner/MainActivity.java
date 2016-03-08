package com.example.biac.testscanner;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends Activity implements SurfaceHolder.Callback{

    private Camera myCamera;
    private SurfaceView scanPreview;
    private SurfaceHolder mySurfaceHolder;
//    private RelativeLayout scanContainer;
//    private RelativeLayout scanCropView;
//    private ImageView scanLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanPreview = (SurfaceView)findViewById(R.id.preview_view);
//        scanContainer = (RelativeLayout)findViewById(R.id.capture_container);
//        scanCropView = (RelativeLayout)findViewById(R.id.capture_crop_view);
//        scanLine = (ImageView)findViewById(R.id.capture_scan_line);
//
//        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,0.0f,Animation.RELATIVE_TO_PARENT, 0.9f);
//        animation.setDuration(4500);
//        animation.setRepeatCount(-1);
//        animation.setRepeatMode(Animation.RESTART);
//        scanLine.startAnimation(animation);

    }

    protected void onResume(){
        super.onResume();
        mySurfaceHolder = scanPreview.getHolder();
        mySurfaceHolder.addCallback(this);
        mySurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void initCamera(){
        if(checkCameraHardware(this)){
            myCamera = Camera.open();
        }
        if(myCamera != null){
            try{
                myCamera.setDisplayOrientation(90);
                myCamera.setPreviewDisplay(mySurfaceHolder);
                myCamera.startPreview();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
            initCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private boolean checkCameraHardware(Context context){
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        }else{
            return false;
        }
    }
}
