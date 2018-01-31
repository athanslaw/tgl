package com.example.doreopartners.bg_tgl;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.HashMap;

public class question1_2 extends AppCompatActivity {

    int score = 0;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question1_2);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Back button is disabled",
                Toast.LENGTH_SHORT).show();
        return;
    }

    public void saveAns1_2(View view){
        try{
            session = new SessionManagement(getApplicationContext());
            CheckBox chkAns = (CheckBox) findViewById(R.id.qus1_2_optionA);
            String qus_ans = "";

            HashMap<String, String> sessions = session.getUserDetails();
            score = Integer.parseInt(sessions.get(SessionManagement.SECTION_1));

            if(chkAns.isChecked()){
                qus_ans = "Yes";
                score += 1;
                session.setSection1(score+"");

                ContentValues values = new ContentValues();
                values.put(usersProvider.qus2, qus_ans);
                values.put(usersProvider.score, score);
                getContentResolver().update(usersProvider.CONTENT_URI, values, usersProvider.ikNumber +  " = '" + sessions.get(SessionManagement.IK_Number)+"'", null);
                Toast.makeText(getBaseContext(), "Good. Qualified for Section 2", Toast.LENGTH_LONG).show();

                Intent i = new Intent(this, com.example.doreopartners.bg_tgl.Question2_1.class);
                this.finish();
                startActivity(i);
            }
            else{
                qus_ans = "-";
                ContentValues values = new ContentValues();
                values.put(usersProvider.qus2, qus_ans);

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
