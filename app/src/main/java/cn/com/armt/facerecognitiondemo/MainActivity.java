package cn.com.armt.facerecognitiondemo;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.Toast;

import cn.com.armt.sdk.DeviceManager;
import cn.com.armt.sdk.hardware.GPIOPort;
import cn.com.armt.sdk.hardware.AdcPort;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private DeviceManager mDeviceManager;
    private Camera[] mVisibleCamera = new Camera[2];
    private SurfaceHolder[] mHolder = new SurfaceHolder[2];
    private SurfaceView[] mCamView = new SurfaceView[2];
    private Button mBtnCtrlWhiteFlashlight;
    private Button mBtnCtrlGreenFlashlight;
    private Button mBtnCtrlRedFlashlight;
    private Button mBtnCtrlRelay;
    private Button mBtnCtrlSta;
    private Button mBtnCtrlNav;
    private Button mBtnReboot;
    private Button mBtnReadAdcIn2;
    private Button mBtnInstall;
    private boolean ctrlWrite =false;
    private boolean ctrlRed =false;
    private boolean ctrlGreen =false;
    private boolean ctrlRelay =false;
    private boolean ctrlStatus = true;
    private boolean ctrlNav = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCamView[0] = (SurfaceView)findViewById(R.id.camera0);
        mCamView[1] = (SurfaceView)findViewById(R.id.camera1);


        mDeviceManager = new DeviceManager(this);

        mBtnCtrlWhiteFlashlight = (Button)findViewById(R.id.CtrlWhiteFlashlight);
        mBtnCtrlWhiteFlashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ctrlWrite) {
                    OpenWhiteFlashlight();
                    ctrlWrite = true;
                    mBtnCtrlWhiteFlashlight.setText("关闭白光灯");
                }else{
                    CloseWhiteFlashlight();
                    ctrlWrite = false;
                    mBtnCtrlWhiteFlashlight.setText("打开白光灯");
                }
            }
        });
        mBtnCtrlGreenFlashlight = (Button)findViewById(R.id.CtrlGreenFlashlight);
        mBtnCtrlGreenFlashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ctrlGreen) {
                    OpenGreenFlashlight();
                    ctrlGreen = true;
                    mBtnCtrlGreenFlashlight.setText("关闭绿光灯");
                }else{
                    CloseGreenFlashlight();
                    ctrlGreen= false;
                    mBtnCtrlGreenFlashlight.setText("打开绿光灯");
                }
            }
        });
        mBtnCtrlRedFlashlight = (Button)findViewById(R.id.CtrlRedFlashlight);
        mBtnCtrlRedFlashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ctrlRed) {
                    OpenRedFlashlight();
                    ctrlRed = true;
                    mBtnCtrlRedFlashlight.setText("关闭红光灯");
                }else{
                    CloseRedFlashlight();
                    ctrlRed= false;
                    mBtnCtrlRedFlashlight.setText("打开红光灯");
                }
            }
        });

        mBtnCtrlRelay = (Button)findViewById(R.id.CtrlRelay);
        mBtnCtrlRelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ctrlRelay) {
                    openRelay();
                    ctrlRelay = true;
                    mBtnCtrlRelay.setText("关闭继电器");
                }else{
                    closeRelay();
                    ctrlRelay= false;
                    mBtnCtrlRelay.setText("打开继电器");
                }
            }
        });

        mBtnCtrlSta = (Button)findViewById(R.id.ctlstatus);
        mBtnCtrlSta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ctrlStatus) {
                    enableStatusbar();
                    ctrlStatus = true;
                    mBtnCtrlSta.setText("关闭状态栏");
                }else{
                    ctrlStatus = false;
                    disableStatusbar();
                    mBtnCtrlSta.setText("显示状态栏");
                }
            }
        });

        mBtnCtrlNav = (Button)findViewById(R.id.ctlnav);
        mBtnCtrlNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ctrlNav) {
                    enableNavigation();
                    ctrlNav = true;
                    mBtnCtrlNav.setText("关闭导航栏");
                }else{
                    ctrlNav = false;
                    disableNavigation();
                    mBtnCtrlNav.setText("显示导航栏");
                }
            }
        });

        mBtnReboot = (Button)findViewById(R.id.btnreboot);
        mBtnReboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeviceManager.reboot(false,null);
            }
        });

        mBtnReadAdcIn2 = (Button)findViewById(R.id.readadcin2);
        mBtnReadAdcIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"read adcin2 "+ readAdcIn(2),Toast.LENGTH_LONG).show();
                String str = runCommand("mkdir /data/testxxx");
                Log.d("test","execRootShell ret " + str);

            }
        });

        mBtnInstall = (Button)findViewById(R.id.btninstall);
        mBtnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean installret = mDeviceManager.installPackage("/sdcard/test.apk","cn.com.armt.sdk.demo");
                Toast.makeText(MainActivity.this,"install ret "+ installret,Toast.LENGTH_LONG).show();
            }
        });

        Button wiegand = (Button)findViewById(R.id.btn_wiegand);
        wiegand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , WiegandActivity.class);
                startActivity(i);
            }
        });

        CloseWhiteFlashlight();
        CloseGreenFlashlight();
        CloseRedFlashlight();
        closeRelay();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void openOpticsCamera(int cameraid){
        mVisibleCamera[cameraid] = Camera.open(cameraid);
        if (mVisibleCamera[cameraid] != null) {
            mHolder[cameraid] = mCamView[cameraid].getHolder();
            mHolder[cameraid].addCallback(new OpticsSurfaceCallback(cameraid));
            mVisibleCamera[cameraid].startPreview();
        }
    }
    private void closeOpticsCamera(int cameraid){
        if (mVisibleCamera[cameraid] != null) {
            mVisibleCamera[cameraid].setPreviewCallback(null);
            mVisibleCamera[cameraid].stopPreview();
            mVisibleCamera[cameraid].release();
            mVisibleCamera[cameraid] = null;
        }
    }

    private class OpticsSurfaceCallback implements SurfaceHolder.Callback {
        private int mcameraid;
        public OpticsSurfaceCallback(int cameraid){
            mcameraid = cameraid;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mVisibleCamera[mcameraid].setPreviewDisplay(holder);
                //mVisibleCamera[mcameraid].setDisplayOrientation(90);
                mVisibleCamera[mcameraid].startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public void surfaceChanged(final SurfaceHolder holder, int format, int
                width, int height) {
            try {
                mVisibleCamera[mcameraid].setPreviewCallback(new Camera.PreviewCallback()
                {
                    @Override
                    public void onPreviewFrame(byte[] bytes, Camera camera) {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            openOpticsCamera(0);
            openOpticsCamera(1);
        }catch (Exception e){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            closeOpticsCamera(0);
            closeOpticsCamera(1);
        }catch (Exception e){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CloseWhiteFlashlight();
        CloseGreenFlashlight();
        CloseRedFlashlight();
        closeRelay();

    }

    public void OpenWhiteFlashlight() {
        Intent it = new Intent("com.android.intent.OpenWhiteFlashlight");
        sendBroadcast(it);
        //GPIOPort port = new GPIOPort(169);
        //port.enable_out();
        //port.write(1);
    }

    public void CloseWhiteFlashlight() {
        Intent it = new Intent("com.android.intent.CloseWhiteFlashlight");
        sendBroadcast(it);
        //GPIOPort port = new GPIOPort(169);
        //port.enable_out();
        //port.write(0);
    }
    public void OpenGreenFlashlight() {
        Intent it = new Intent("com.android.intent.OpenGreenFlashlight");
        sendBroadcast(it);
        //GPIOPort port = new GPIOPort(171);
        //port.enable_out();
        //port.write(1);
    }

    public void CloseGreenFlashlight() {
        Intent it = new Intent("com.android.intent.CloseGreenFlashlight");
        sendBroadcast(it);
        //GPIOPort port = new GPIOPort(171);
        //port.enable_out();
        //port.write(0);
    }

    public void OpenRedFlashlight() {
        Intent it = new Intent("com.android.intent.OpenRedFlashlight");
        sendBroadcast(it);
        //GPIOPort port = new GPIOPort(162);
        //port.enable_out();
        //port.write(1);
    }

    public void CloseRedFlashlight() {
        Intent it = new Intent("com.android.intent.CloseRedFlashlight");
        sendBroadcast(it);
        //GPIOPort port = new GPIOPort(162);
        //port.enable_out();
        //port.write(0);
    }

    public void openRelay() {
        Intent it = new Intent("com.android.intent.OpenRelay");
        sendBroadcast(it);
        //GPIOPort port = new GPIOPort(65);
        //port.enable_out();
        //port.write(1);
    }

    public void closeRelay() {
        Intent it = new Intent("com.android.intent.CloseRelay");
        sendBroadcast(it);
        //GPIOPort port = new GPIOPort(65);
        //port.enable_out();
        //port.write(0);
    }

    public void enableStatusbar() {
        Intent it = new Intent("com.android.intent.EnableStatusbar");
        sendBroadcast(it);
    }

    public void disableStatusbar() {
        Intent it = new Intent("com.android.intent.DisableStatusbar");
        sendBroadcast(it);
    }

    public void enableNavigation() {
        Intent it = new Intent("com.android.intent.EnableNavigation");
        sendBroadcast(it);
    }

    public void disableNavigation() {
        Intent it = new Intent("com.android.intent.DisableNavigation");
        sendBroadcast(it);
    }


    public int readAdcIn(int adcid){
        AdcPort port = new AdcPort(adcid);
        return port.read();
    }

    public static String runCommand(String command) {
        Process process = null;
        String result = "";
        DataOutputStream os = null;
        DataInputStream is = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            is = new DataInputStream(process.getInputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            String line = null;
            while ((line = is.readLine()) != null) {
                result += line;
            }
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }


}
