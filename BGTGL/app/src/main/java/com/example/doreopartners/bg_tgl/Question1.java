package com.example.doreopartners.bg_tgl;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.HashMap;

public class Question1 extends AppCompatActivity {

    int score = 0;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_question1);
        }catch(Exception e){System.out.println("Exception caught: "+e);}
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, com.example.doreopartners.bg_tgl.Home_page.class);
        startActivity(i);
    }

    public void saveAns(View view){
        try{
            session = new SessionManagement(getApplicationContext());
            CheckBox chkAns = (CheckBox) findViewById(R.id.qus1_optionA);
            String qus_ans = "";
            if(chkAns.isChecked()){
                qus_ans = "Yes";
                score = 1;
                session.setSection1(score+"");


                HashMap<String, String> sessions = session.getUserDetails();

                ContentValues values = new ContentValues();
                values.put(usersProvider.qus1, qus_ans);
                int total = score;
                values.put(usersProvider.score, total);
                getContentResolver().update(usersProvider.CONTENT_URI, values, usersProvider.ikNumber +  " = '" + sessions.get(SessionManagement.IK_Number)+"'", null);
                Toast.makeText(getBaseContext(), "Saved successfully", Toast.LENGTH_LONG).show();

                Intent i = new Intent(this, com.example.doreopartners.bg_tgl.question1_2.class);
                this.finish();
                startActivity(i);

            }
            else{
                qus_ans = "-";

                session.setSection1(score+"");
                HashMap<String, String> sessions = session.getUserDetails();

                ContentValues values = new ContentValues();
                values.put(usersProvider.qus1, qus_ans);
                int total = score;
                values.put(usersProvider.score, total);
                getContentResolver().update(usersProvider.CONTENT_URI, values, usersProvider.ikNumber +  " = '" + sessions.get(SessionManagement.IK_Number)+"'", null);
                Toast.makeText(getBaseContext(), "Saved successfully", Toast.LENGTH_LONG).show();

                Intent i = new Intent(this, com.example.doreopartners.bg_tgl.Failure.class);
                this.finish();
                startActivity(i);
            }

        }
        catch(Exception e){
            Toast.makeText(getBaseContext(),
                    "Kindly choose an option or click on skip to proceed", Toast.LENGTH_LONG).show();}
    }

}
