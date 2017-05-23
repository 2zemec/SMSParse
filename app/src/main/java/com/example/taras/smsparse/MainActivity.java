package com.example.taras.smsparse;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView smsList = (ListView)findViewById(R.id.sms_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.sms_list_layout, readSMS(getApplicationContext()));
        smsList.setAdapter(adapter);
    }

    private List<String>  readSMS(Context context)
    {
        ContentResolver cr = context.getContentResolver();

        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED) {
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }
        else
        {
            List<String> lstSms = new ArrayList<String>();
            String[] reqCols = new String[] { "_id", "address", "body" };
            Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);
            int totalSMS = 0;
            if (c != null) {
                totalSMS = c.getCount();
                if (c.moveToFirst()) {
                    for (int j = 0; j < totalSMS; j++) {
                        //String smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                        String number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));

                        if (number.equals("KREDOBANK")){
                            String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                            //Date dateFormat= new Date(Long.valueOf(smsDate));
                            lstSms.add(number + "\\" + body);
                        }
                        c.moveToNext();
                    }
                }
            } else {
                Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show();
            }
            c.close();
            return lstSms;
        }
        return null;
    }
}
