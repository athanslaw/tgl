package com.example.doreopartners.bg_tgl;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Question2_3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2_3);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Back button is disabled",
                Toast.LENGTH_SHORT).show();
        return;
    }

    public void saveAns(View view){
        try{
            int count = 0;

            SessionManagement session = new SessionManagement(getApplicationContext());

            HashMap<String, String> sessions = session.getUserDetails();
            int score = Integer.parseInt(sessions.get(SessionManagement.SECTION_2));

            CheckBox chkAnsA = (CheckBox) findViewById(R.id.qus2_3_optionA);
            CheckBox chkAnsB = (CheckBox) findViewById(R.id.qus2_3_optionB);
            CheckBox chkAnsC = (CheckBox) findViewById(R.id.qus2_3_optionC);
            CheckBox chkAnsD = (CheckBox) findViewById(R.id.qus2_3_optionD);
            CheckBox chkAnsE = (CheckBox) findViewById(R.id.qus2_3_optionE);
            CheckBox chkAnsF = (CheckBox) findViewById(R.id.qus2_3_optionF);
            CheckBox chkAnsG = (CheckBox) findViewById(R.id.qus2_3_optionG);
            CheckBox chkAnsH = (CheckBox) findViewById(R.id.qus2_3_optionH);
            CheckBox chkAnsI = (CheckBox) findViewById(R.id.qus2_3_optionI);
            CheckBox chkAnsJ = (CheckBox) findViewById(R.id.qus2_3_optionJ);
            CheckBox chkAnsK = (CheckBox) findViewById(R.id.qus2_3_optionK);
            EditText chkAnsO = (EditText) findViewById(R.id.qus2_3other);
            String qus_ans = "1";
            if(chkAnsA.isChecked()){
                qus_ans += "__A";
                count += 1;
            }
            if(chkAnsB.isChecked()){
                qus_ans += "__B";
                count += 1;
            }
            if(chkAnsC.isChecked()){
                qus_ans += "__C";
                count += 1;
            }
            if(chkAnsD.isChecked()){
                qus_ans += "__D";
                count += 1;
            }
            if(chkAnsE.isChecked()){
                qus_ans += "__E";
                count += 1;
            }
            if(chkAnsF.isChecked()){
                qus_ans += "__F";
                count += 1;
            }
            if(chkAnsG.isChecked()){
                qus_ans += "__G";
                count += 1;
            }
            if(chkAnsH.isChecked()){
                qus_ans += "__H";
                count += 1;
            }
            if(chkAnsI.isChecked()){
                qus_ans += "__I";
                count += 1;
            }
            if(chkAnsJ.isChecked()){
                qus_ans += "__J";
                count += 1;
            }
            if(chkAnsK.isChecked()){
                qus_ans += "__K";
                count += 1;
            }
            if(chkAnsO.getText().toString() != null && !chkAnsO.getText().toString().trim().equalsIgnoreCase("")){
                qus_ans += "___"+chkAnsO.getText().toString();
                count += 1;
            }
            switch (count){
                case 0: score += 0; break;
                case 1: score += 0; break;
                case 2: score += 1; break;
                case 3: score += 2; break;
                case 4: score += 3; break;
                case 5: score += 4; break;
                default: score += 5;
            }
            session.setSection2(score+"");

            int no = 0;
            ContentValues values = new ContentValues();
            values.put(usersProvider.qus5, qus_ans);

            int total = score + Integer.parseInt(sessions.get(SessionManagement.SECTION_1));
            values.put(usersProvider.score, total);

            getContentResolver().update(usersProvider.CONTENT_URI, values, usersProvider.ikNumber +  " = '" + sessions.get(SessionManagement.IK_Number)+"'", null);
            Toast.makeText(getBaseContext(), "Saved successfully", Toast.LENGTH_LONG).show();

            if(score >= 9) {
                getContentResolver().update(usersProvider.CONTENT_URI, values, usersProvider.ikNumber + " = '" + sessions.get(SessionManagement.IK_Number) + "'", null);
                Toast.makeText(getBaseContext(), "Good. Qualified for Section 3 (Final Stage)", Toast.LENGTH_LONG).show();

                Intent i = new Intent(this, com.example.doreopartners.bg_tgl.Question3_1.class);
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

    public void showother(View view) {
        EditText et = (EditText) findViewById(R.id.qus2_3other);
        et.setVisibility(View.VISIBLE);

        TextView etv = (TextView) findViewById(R.id.qus2_3_other);
        etv.setVisibility(View.GONE);
    }
}
