package com.example.doreopartners.bg_tgl;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.HashMap;

public class Question3_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question3_2);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void saveAns(View view){
        try{
            int count = 0;
            SessionManagement session;
            session = new SessionManagement(getApplicationContext());


            HashMap<String, String> sessions = session.getUserDetails();
            int score = Integer.parseInt(sessions.get(SessionManagement.SECTION_3));

            CheckBox chkAnsA = (CheckBox) findViewById(R.id.qus3_2_optionA);
            CheckBox chkAnsB = (CheckBox) findViewById(R.id.qus3_2_optionB);
            String qus_ans = "1";
            if(chkAnsA.isChecked()){
                qus_ans += "__A";
                count += 1;
            }
            if(chkAnsB.isChecked()){
                qus_ans += "__B";
                count += 1;
            }
            switch (count){
                case 0: score += 0; break;
                case 1: score += 5; break;
                default: score += 5;
            }
            session.setSection3(score+"");

            ContentValues values = new ContentValues();
            values.put(usersProvider.qus7, qus_ans);

            int total = score+Integer.parseInt(sessions.get(SessionManagement.SECTION_2)) + Integer.parseInt(sessions.get(SessionManagement.SECTION_1));
            values.put(usersProvider.score, total);

            getContentResolver().update(usersProvider.CONTENT_URI, values, usersProvider.ikNumber +  " = '" + sessions.get(SessionManagement.IK_Number)+"'", null);
            Toast.makeText(getBaseContext(), "Saved successfully", Toast.LENGTH_LONG).show();

            if(score >= 10) {
                getContentResolver().update(usersProvider.CONTENT_URI, values, usersProvider.ikNumber + " = '" + sessions.get(SessionManagement.IK_Number) + "'", null);
                Toast.makeText(getBaseContext(), "Congratulations. You passed the interview", Toast.LENGTH_LONG).show();

                Intent i = new Intent(this, com.example.doreopartners.bg_tgl.TFM_Schedule.class);
                this.finish();
                startActivity(i);
            }
            else {
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
