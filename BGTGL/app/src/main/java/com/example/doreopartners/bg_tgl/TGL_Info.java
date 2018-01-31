package com.example.doreopartners.bg_tgl;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentValues;
import android.net.Uri;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TGL_Info extends AppCompatActivity {

    AutoCompleteTextView tgl;
    private Cursor c;
    private TGLDB db;
    TextView ikName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tgl__info);

        ArrayList list = new ArrayList();
        db = new TGLDB(this);
        c = db.getTGLs(); // you would not typically call this on the main thread
        if (c.moveToFirst()) {
            do {
                    list.add(c.getString(1));
            } while (c.moveToNext());
        }

        tgl=(AutoCompleteTextView)findViewById(R.id.ik_number);

        //set the onFocusChange listener
        //tgl.setOnFocusChangeListener(ikNumberFocus);

        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        tgl.setAdapter(adapter);
        tgl.setThreshold(1);
    }


    public void checkIkNumber() {
        tgl = (AutoCompleteTextView) findViewById(R.id.ik_number);
        String tglTxt = tgl.getText().toString();
        db = new TGLDB(this);
        String ikNameStr = "";
        c = db.getTGLs(tglTxt); // you would not typically call this on the main thread
        if (c.moveToFirst()) {
            ((TextView) findViewById(R.id.label_ik_name)).setText("TGL Name: ");
            ((TextView) findViewById(R.id.ik_name)).setText(c.getString(2));

            ((TextView) findViewById(R.id.label_phone)).setText("Phone: ");
            ((TextView) findViewById(R.id.ik_phone)).setText(c.getString(7));

            ((TextView) findViewById(R.id.ik_others)).setText("Age: "+c.getString(5)+"; Sex: "+c.getString(6));
        }

    }

    public void verifyIkNum(View view) {
                //set the text
                checkIkNumber();
    }

    public void saveTglInfo(View view){
        try{
            tgl = (AutoCompleteTextView) findViewById(R.id.ik_number);
            ikName = (TextView) findViewById(R.id.ik_name);

            String tglTxt = tgl.getText().toString();
            db = new TGLDB(this);
            String ikNameStr="";
            c = db.getTGLs(tglTxt); // you would not typically call this on the main thread
            if (c.moveToFirst()) {
                ikNameStr = c.getString(2);
            }

            if(ikNameStr.equalsIgnoreCase("")){
                Toast.makeText(getBaseContext(), "The provided IK Number does not exist in our database. Kindky select from the suggestions", Toast.LENGTH_LONG).show();
            }
            else {
                // Add a new student record
                ContentValues values = new ContentValues();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

                SessionManagement session;
                session = new SessionManagement(getApplicationContext());
                HashMap<String, String> sessions = session.getUserDetails();

                String interviewer = sessions.get(SessionManagement.KEY_NAME);
                RadioGroup ansGroup;
                RadioButton radioAnsButton;
                ansGroup = (RadioGroup) findViewById(R.id.age);
                int selectedId = ansGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioAnsButton = (RadioButton) findViewById(selectedId);

                String id = radioAnsButton.getText().toString();
                if (id.equalsIgnoreCase("No")) {
                    Toast.makeText(getBaseContext(),
                            "Kindly re-apply when you turn 18", Toast.LENGTH_LONG).show();
                    return;
                }

                session = new SessionManagement(getApplicationContext());
                CheckBox chkAns = (CheckBox) findViewById(R.id.qus1_optionA);
                String qus_ans = "";
                int score = 0;
                if (chkAns.isChecked()) {
                    qus_ans = "Yes";
                    score = 1;
                    session.setSection1(score + "");

                    if (checkUniqueIKNumber(tgl.getText().toString()) > 0) {
                        Toast.makeText(getBaseContext(),
                                "IK Number already exists on the system", Toast.LENGTH_LONG).show();
                        return;
                    }


                    values.put(usersProvider.qus1, qus_ans);
                    values.put(usersProvider.score, score);

                    values.put(usersProvider.ikName, ikNameStr);
                    values.put(usersProvider.ikNumber, tgl.getText().toString());
                    values.put(usersProvider.interviewer, interviewer);

                    values.put(usersProvider.date, dateFormat.format(date));
                    values.put(usersProvider.status, "0");
                    values.put(usersProvider.qus2, "-");
                    values.put(usersProvider.qus3, "-");
                    values.put(usersProvider.qus4, "-");
                    values.put(usersProvider.qus5, "-");
                    values.put(usersProvider.qus6, "-");
                    values.put(usersProvider.qus7, "-");
                    values.put(usersProvider.qus8, "-");
                    values.put(usersProvider.qus9, "-");
                    values.put(usersProvider.qus10, "-");
                    values.put(usersProvider.qus11, "-");
                    values.put(usersProvider.qus12, "-");

                    values.put(usersProvider.village, "-");
                    values.put(usersProvider.amik, "-");
                    values.put(usersProvider.tfm_date, "-");
                    values.put(usersProvider.tfm_time, "-");

                    if (tgl.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getBaseContext(),
                                "IK Number is compulsory.", Toast.LENGTH_LONG).show();
                    } else {
                        Uri uri = getContentResolver().insert(
                                usersProvider.CONTENT_URI, values);

                        Toast.makeText(getBaseContext(),
                                "Redirecting to questions ...", Toast.LENGTH_LONG).show();

                        session = new SessionManagement(getApplicationContext());
                        session.setIK_Number(tgl.getText().toString());

                        closedb();
                        Intent i = new Intent(this, com.example.doreopartners.bg_tgl.question1_2.class);
                        startActivity(i);
                    }

                } else {
                    Toast.makeText(getBaseContext(),
                            "Kindly verify candidates Id card to proceed", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        catch(Exception e){System.out.println("On save caught: "+e);e.getStackTrace();}
    }

    protected int checkUniqueIKNumber(String iknumber) {

        Uri uri = getIntent().getData();

        String URL = "content://com.example.provider.bg_tgl/tgl";
        Uri students = Uri.parse(URL);

        int count = 0;
        try{
            try{  Cursor c = managedQuery(students, null, usersProvider.ikNumber +  " = '"+iknumber+"'", null, null);
                return c.getCount();
            }
            catch(Exception ex){
                System.out.println("checkUniqueIKNumber: "+ex);
            }
        }catch(Exception e){Toast.makeText(getBaseContext(), "Check Unique IK Number  "+e, Toast.LENGTH_LONG).show();}

        return 0;
    }

    public void onClickRetrieveStudents(View view) {
        Intent i = new Intent(this, com.example.doreopartners.bg_tgl.ViewTgl.class);
        startActivity(i);
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
