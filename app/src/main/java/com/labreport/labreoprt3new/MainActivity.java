package com.labreport.labreoprt3new;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Currency;

public class MainActivity extends AppCompatActivity {

    public BroadcastReceiver brc = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            if(level<20){
                Toast.makeText(MainActivity.this, "Battery low", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "battery died", Toast.LENGTH_SHORT).show();
            }

        }

    };

//    public BroadcastReceiver timeChange = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //int i = intent.getIntExtra("time", 0);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),800000,intent,0);
//            AlarmManager alarmManager =(AlarmManager) getSystemService(ALARM_SERVICE);
//            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(60000),pendingIntent);
//            Toast.makeText(MainActivity.this,"ভাই উঠ !!!", Toast.LENGTH_LONG).show();
//
//
//
//        }
//    };

    EditText eTname, eTemail, eTmobile, eTpassword;
    Button bInsert, bDiaplay, bUpdate, bDelete;
    DBHelper db;
    LinearLayout mainlayout;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(brc, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED
        ));
//        registerReceiver(timeChange, new IntentFilter(
//                Intent.ACTION_TIME_CHANGED
//        ));


        eTemail = findViewById(R.id.eemail);
        eTname = findViewById(R.id.ename);
        eTpassword = findViewById(R.id.epass);
        eTmobile = findViewById(R.id.emobile);
        mainlayout = findViewById(R.id.mainlayout);


        bInsert = findViewById(R.id.binsert);
        bDiaplay = findViewById(R.id.bdisplay);
        bUpdate = findViewById(R.id.bupdate);
        bDelete = findViewById(R.id.bdelete);
        db = new DBHelper(this);


        bInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = eTname.getText().toString();
                String email = eTemail.getText().toString();
                String password = eTpassword.getText().toString();
                String mobile = eTmobile.getText().toString();
                if (name.equals("") || mobile.equals("") || email.equals("") || password.equals("")) {
                    Toast.makeText(MainActivity.this, "No field can be left empty", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkinsertdata = db.insertUserdata(name, email, password, mobile);
                    if (checkinsertdata == true)
                        Toast.makeText(MainActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "Data Already Exists, The info of the 'Name' field cannot be the same", Toast.LENGTH_SHORT).show();
                }


            }
        });

        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = eTname.getText().toString();
                String email = eTemail.getText().toString();
                String password = eTpassword.getText().toString();
                String mobile = eTmobile.getText().toString();


                Boolean checkupdatetdata = db.updateUserdata(name, email, password, mobile);
                if (checkupdatetdata == true)
                    Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Nothing to update", Toast.LENGTH_SHORT).show();
            }

        });
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = eTname.getText().toString();
                Boolean checkdeletedata = db.deleteUserdata(name);
                if (checkdeletedata == true)
                    Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Cant delete data / Invalid selection", Toast.LENGTH_SHORT).show();

            }
        });

        bDiaplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = db.getData();
                if (res.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "No Data Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("name: " + res.getString(0) + "\n");
                    buffer.append("email: " + res.getString(1) + "\n");
                    buffer.append("password: " + res.getString(2) + "\n");
                    buffer.append("mobile: " + res.getString(3) + "\n\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("User Entries");
                builder.setMessage(buffer.toString());
                builder.show();

            }
        });

        Intent i = new Intent(getApplicationContext(),broadcastReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),800000, i,PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000,pendingIntent);

//        mainlayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(),broadcastReciver.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),800000,intent,0);
//
//                AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(10000),pendingIntent);
//            }
//        });




//        bDiaplay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), viewData.class);
//                startActivity(i);
//            }
//        });


    }

}
