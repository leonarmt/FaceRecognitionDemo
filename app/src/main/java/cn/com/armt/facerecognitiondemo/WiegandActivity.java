package cn.com.armt.facerecognitiondemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import cn.com.armt.sdk.hardware.Wiegand;

public class WiegandActivity extends AppCompatActivity {
    Button wg26_send;
    Button wg34_send;
    EditText wg_send_et;
    TextView wg_show_tv;
    long wg_read;

    public char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes).flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiegand);

        wg_send_et = (EditText)findViewById(R.id.sendet);
        wg_show_tv = (TextView)findViewById(R.id.show_tv);

        wg26_send = (Button)findViewById(R.id.btn_send_wg26);
        wg26_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send = wg_send_et.getText().toString();
                if(send.length()!=24){
                    wg_show_tv.append("error:must 24!\n\r");
                    return;
                }
                int ret = Wiegand.wiegandWrite(Wiegand.WG_26_MODE,getChars(send.getBytes()));
                if(ret==0){
                    wg_show_tv.append("send:"+send+"\n\r");
                }else{
                    wg_show_tv.append("error:wg26 send!\n\r");
                }
            }
        });

        wg34_send = (Button)findViewById(R.id.btn_send_wg34);
        wg34_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send = wg_send_et.getText().toString();
                if(send.length()!=32){
                    wg_show_tv.append("error:must 32!\n\r");
                    return;
                }
                int ret = Wiegand.wiegandWrite(Wiegand.WG_34_MODE,getChars(send.getBytes()));
                if(ret==0){
                    wg_show_tv.append("send:"+send+"\n\r");
                }else{
                    wg_show_tv.append("error:wg34 send!\n\r");
                }
            }
        });

        new Thread(){
            public void run() {
                while (true){
                    wg_read = Wiegand.wiegandRead();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wg_show_tv.append("read:"+Long.toBinaryString(wg_read)+"\n\r");
                        }
                    });
                }
            }
        }.start();


    }
}
