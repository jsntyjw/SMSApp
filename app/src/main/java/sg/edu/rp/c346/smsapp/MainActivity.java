package sg.edu.rp.c346.smsapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    
    TextView textView1;
    TextView textView2;
    EditText etTo;
    EditText etContent;
    Button btnSend;
    Button btnViaMsg;
    BroadcastReceiver br = new MessageReceiver();
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        
        textView1 = findViewById(R.id.tv1);
        textView2 = findViewById(R.id.tv2);
        etTo = findViewById(R.id.etTo);
        etContent = findViewById(R.id.etContent);
        btnSend = findViewById(R.id.btnSend);
        btnViaMsg = findViewById(R.id.buttonViaMsg);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br,filter);
        
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etTo.getText().toString().contains(",")){
                    String[] strArray = etTo.getText().toString().split(",");
                    for(int i=0;i<strArray.length;i++){
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(strArray[i].toString(),null,etContent.getText().toString(),null,null);
                    }
                }
                else{
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(etTo.getText().toString(),null,etContent.getText().toString(),null,null);
                }
                etContent.setText(null);
                Toast my_toast = Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_LONG);
                my_toast.show();

            }
        });

        btnViaMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Uri uri = Uri.parse("smsto:"+etTo.getText().toString());
//                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
//                it.putExtra("sms_body", etContent.getText().toString());
//                startActivity(it);



                if(etTo.getText().toString().contains(",")){
                    String[] strArray = etTo.getText().toString().split(",");
                    String numberList = "";
                    for(int i=0;i<strArray.length;i++){
                         numberList += strArray[i]+";";
                    }
                    Uri uri = Uri.parse("smsto:"+numberList);
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", etContent.getText().toString());
                    startActivity(it);
                }
                else{
                    Uri uri = Uri.parse("smsto:"+etTo.getText().toString());
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", etContent.getText().toString());
                    startActivity(it);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }

    }


}
