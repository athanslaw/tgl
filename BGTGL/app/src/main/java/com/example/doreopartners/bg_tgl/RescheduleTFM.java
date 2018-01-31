package com.example.doreopartners.bg_tgl;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class RescheduleTFM  extends Activity implements OnItemSelectedListener {

    String iknumbername;
    ArrayList list;

    private TextView dateView;
    private int year, month, day;

    private Calendar calendar;
    private String format = "";

    TextView time;

    AutoCompleteTextView village;
    private Cursor c;
    private VillageDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_tfm);

        list = new ArrayList();
        Uri uri = getIntent().getData();

        String URL = "content://com.example.provider.bg_tgl/tgl";
        Uri students = Uri.parse(URL);

        int score = 0;
        Cursor c = managedQuery(students, null, null, null, "ik_number");
        if (c.moveToFirst()) {
            do{
                score = new Integer(c.getString(c.getColumnIndex( usersProvider.score))).intValue();
                if(score > 20) {
                    list.add(c.getString(c.getColumnIndex(usersProvider.ikNumber)) + "::" + c.getString(c.getColumnIndex(usersProvider.ikName)));
                }
            } while (c.moveToNext());
        }

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.successfulliknumbers);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        dateView = (TextView) findViewById(R.id.successtfm_date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        //  initiate the edit text
        Button timebtn = (Button) findViewById(R.id.successtimebtn);
        time = (TextView) findViewById(R.id.successtime);


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
                mTimePicker = new TimePickerDialog(RescheduleTFM.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText((selectedHour < 10?"0"+selectedHour:selectedHour+"") + ":" + (selectedMinute < 10?"0"+selectedMinute:selectedMinute+""));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        // Village autofill

        ArrayList list = new ArrayList();

        db = new VillageDB(this);
        c = db.getVillages(); // you would not typically call this on the main thread
        if (c.moveToFirst()) {
            do {
                list.add(c.getString(1));
            } while (c.moveToNext());
        }

        village=(AutoCompleteTextView)findViewById(R.id.successtfm_village);

        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        village.setAdapter(adapter);
        village.setThreshold(1);

    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        iknumbername = list.get(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub

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

    public void reschedule_save(View view){
        try{
            dateView = (TextView) findViewById(R.id.successtfm_date);

            time = (TextView) findViewById(R.id.successtime);
            String[] iknumbersplit = iknumbername.split("::");
            String iknumber = iknumbersplit[0];

            TextView vil = (TextView) findViewById(R.id.successtfm_village);
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
                if (!iknumber.equalsIgnoreCase("")) {
                    ContentValues values = new ContentValues();
                    values.put(usersProvider.tfm_date, dateView.getText().toString());
                    values.put(usersProvider.tfm_time, time.getText().toString());
                    values.put(usersProvider.status, "0");
                    values.put(usersProvider.village, vilTxt);
                    values.put(usersProvider.amik, amik);

                    getContentResolver().update(usersProvider.CONTENT_URI, values, usersProvider.ikNumber + " = '" + iknumber + "'", null);
                    Toast.makeText(getBaseContext(), "Rescheduling operation successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Please select an IK to reschedule", Toast.LENGTH_LONG).show();
                }
            }
        }
        catch(Exception e){
            System.out.println("Error: "+e);
            Toast.makeText(getBaseContext(),
                    "Operation failed, kindly contact the esystems team", Toast.LENGTH_LONG).show();
        }
    }


    public void gohome(View view){
        try{
            Intent i = new Intent(this, com.example.doreopartners.bg_tgl.Home_page.class);
            startActivity(i);
        }
        catch(Exception e){e.getStackTrace();}
    }


}
