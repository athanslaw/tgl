package com.example.doreopartners.bg_tgl;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import android.app.DatePickerDialog;

import java.util.ArrayList;
import java.util.HashMap;


public class TFM_Schedule extends Activity{

    AutoCompleteTextView village;
    private Cursor c;
    private VillageDB db;

    private DatePicker datePicker;
    private TextView dateView;
    private int year, month, day;

    private Calendar calendar;
    private String format = "";

    TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfm__schedule);

        ArrayList list = new ArrayList();

        db = new VillageDB(this);
        c = db.getVillages(); // you would not typically call this on the main thread
        if (c.moveToFirst()) {
            do {
                list.add(c.getString(1));

            } while (c.moveToNext());

        }

        village=(AutoCompleteTextView)findViewById(R.id.tfm_village);

        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        village.setAdapter(adapter);
        village.setThreshold(1);

        dateView = (TextView) findViewById(R.id.tfm_date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        //  initiate the edit text
        Button timebtn = (Button) findViewById(R.id.timebtn);
        time = (TextView) findViewById(R.id.time);


        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        time.setText((hour < 10?"0"+hour:hour+"") + ":" + (minute < 10?"0"+minute:minute+""));

        // perform click event listener on edit text
        timebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TFM_Schedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText((selectedHour < 10?"0"+selectedHour:selectedHour+"") + ":" + (selectedMinute < 10?"0"+selectedMinute:selectedMinute+""));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public void tfm_save(View view){
        try{
            dateView = (TextView) findViewById(R.id.tfm_date);
            TextView vil = (TextView) findViewById(R.id.tfm_village);
            String vilTxt = vil.getText().toString();
            db = new VillageDB(this);
            String amik="";
            c = db.getVillages(vilTxt); // you would not typically call this on the main thread
            if (c.moveToFirst()) {
                    amik = c.getString(2);
            }

            if(amik.equalsIgnoreCase("")){
                Toast.makeText(getBaseContext(), "The provided village does not exist in our database. Kindky select from the suggestions", Toast.LENGTH_LONG).show();
            }
            else {

                time = (TextView) findViewById(R.id.time);

                SessionManagement session;
                session = new SessionManagement(getApplicationContext());
                HashMap<String, String> sessions = session.getUserDetails();
                int no = 0;
                ContentValues values = new ContentValues();
                values.put(usersProvider.tfm_date, dateView.getText().toString());
                values.put(usersProvider.tfm_time, time.getText().toString());
                values.put(usersProvider.village, vilTxt);
                values.put(usersProvider.amik, amik);

                getContentResolver().update(usersProvider.CONTENT_URI, values, usersProvider.ikNumber + " = '" + sessions.get(SessionManagement.IK_Number) + "'", null);
                Toast.makeText(getBaseContext(), "Saved successfully", Toast.LENGTH_LONG).show();

                Intent i = new Intent(this, com.example.doreopartners.bg_tgl.Success.class);

                closedb();
                this.finish();
                startActivity(i);
            }

        }
        catch(Exception e){
            System.out.println("Error: "+e);
            Toast.makeText(getBaseContext(),
                    "Kindly use a village that exists in the drop down", Toast.LENGTH_LONG).show();}
    }

    public void closedb(){
        c.close();
        db.close();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        c.close();
        db.close();
    }


}
