package com.example.doreopartners.bg_tgl;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Question3_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question3_1);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Back button is disabled",
                Toast.LENGTH_SHORT).show();
        return;
    }

    public void saveAns(View view){
        try{
            int score = 0, count = 0;
            SessionManagement session;
            session = new SessionManagement(getApplicationContext());
            CheckBox chkAnsA = (CheckBox) findViewById(R.id.qus3_1_optionA);
            String qus_ans = "1";
            if(chkAnsA.isChecked()){
                qus_ans += "__A";
                count += 1;
            }
            switch (count){
                case 0: score = 0; break;
                case 1: score = 5; break;
                default: score = 5;
            }
            session.setSection3(score+"");

            HashMap<String, String> sessions = session.getUserDetails();

            ContentValues values = new ContentValues();
            values.put(usersProvider.qus6, qus_ans);
            int total = score+Integer.parseInt(sessions.get(SessionManagement.SECTION_2)) + Integer.parseInt(sessions.get(SessionManagement.SECTION_1));
            values.put(usersProvider.score, total);

            getContentResolver().update(usersProvider.CONTENT_URI, values, usersProvider.ikNumber +  " = '" + sessions.get(SessionManagement.IK_Number)+"'", null);

            if(score >= 5) {
                getContentResolver().update(usersProvider.CONTENT_URI, values, usersProvider.ikNumber + " = '" + sessions.get(SessionManagement.IK_Number) + "'", null);
                Toast.makeText(getBaseContext(), "Saved successfully", Toast.LENGTH_LONG).show();

                Intent i = new Intent(this, com.example.doreopartners.bg_tgl.Question3_2.class);
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
