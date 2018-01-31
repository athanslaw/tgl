package com.example.doreopartners.bg_tgl;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Failure extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SessionManagement sessions = new SessionManagement(getApplicationContext());
        if(sessions.isLoggedIn()){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_failure);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            ((TextView)findViewById(R.id.failure_date)).setText(dateFormat.format(date));

            HashMap<String, String> session = sessions.getUserDetails();
            String score1 = session.get(SessionManagement.SECTION_1);
            ((TextView)findViewById(R.id.section_1)).setText("Section 1 score = "+score1);
            int score = Integer.parseInt(score1);

            if(session.get(SessionManagement.SECTION_2) != null) {
                String score2 = session.get(SessionManagement.SECTION_2);
                ((TextView)findViewById(R.id.section_2)).setText("Section 2 score = "+score2);
                score += Integer.parseInt(score2);
            }

            if(session.get(SessionManagement.SECTION_3) != null) {
                String score3 = session.get(SessionManagement.SECTION_3);
                ((TextView)findViewById(R.id.section_3)).setText("Section 3 score = "+score3);
                score = Integer.parseInt(score3);
            }

            ((TextView)findViewById(R.id.failure_total)).setText("TOTAL SCORE = "+score);

            String id = session.get(SessionManagement.IK_Number);

            Uri uri = getIntent().getData();

            String URL = "content://com.example.provider.bg_tgl/tgl";
            Uri students = Uri.parse(URL);

            try{
                    Cursor c = managedQuery(students, null,  usersProvider.ikNumber +  " = '" + id+"'", null, "tgl_name");
                    if (c.moveToFirst()) {
                        do{
                            ((TextView)findViewById(R.id.failure_ikName)).setText(c.getString(c.getColumnIndex( usersProvider.ikName)));
                            ((TextView)findViewById(R.id.failure_interviewer)).setText(c.getString(c.getColumnIndex( usersProvider.interviewer)));
                        } while (c.moveToNext());
                    }
            }catch(Exception e){Toast.makeText(getBaseContext(), "View Record  "+e, Toast.LENGTH_LONG).show();}

        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Back button is disabled",
                Toast.LENGTH_SHORT).show();
        return;
    }

    public void newApplicant(View view){
        try{
            SessionManagement s = new SessionManagement(getApplicationContext());
            s.newApplicant();
        }
        catch(Exception e){e.getStackTrace();}
    }

    public void logout(View view){
        try{
            SessionManagement s = new SessionManagement(getApplicationContext());
            s.logoutUser();
        }
        catch(Exception e){e.getStackTrace();}
    }
}
