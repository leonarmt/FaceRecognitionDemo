package cn.com.armt.facerecognitiondemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class FaceReceiver extends BroadcastReceiver {
    public FaceReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if("com.android.intent.BodyIn".equals(action)) {
            Toast.makeText(context,"BodyIn",Toast.LENGTH_SHORT).show();
        }else if("com.android.intent.BodyOut".equals(action)) {
            Toast.makeText(context,"BodyOut",Toast.LENGTH_SHORT).show();
        }

    }
}
