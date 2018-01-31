package com.example.doreopartners.bg_tgl;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    private EditText pwd;
    int counter = 3;
    private Button login;
    SessionManagement session;

    AutoCompleteTextView username;
    private Cursor c;
    private UsersDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        login = (Button)findViewById(R.id.btnlogin);

        ArrayList list = new ArrayList();

        db = new UsersDB(this);
        c = db.getUsers(); // you would not typically call this on the main thread
        if (c.moveToFirst()) {
            do {
                list.add(c.getString(1));
            } while (c.moveToNext());
        }

        username=(AutoCompleteTextView)findViewById(R.id.username);
        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        username.setAdapter(adapter);
        username.setThreshold(1);
    }

    public void btnlogin(View view){
        try{
            pwd = (EditText) findViewById(R.id.pwd);
            username = (AutoCompleteTextView) findViewById(R.id.username);

            String user = username.getText().toString();
            String password=""; int status = 0;
            db = new UsersDB(this);

            c = db.getUsers(user); // you would not typically call this on the main thread
            if (c.moveToFirst()) {
                password = c.getString(2);
                status = c.getInt(3);
            }

            if(pwd.getText().toString().equals(password) && !password.equalsIgnoreCase("")) {
                if(status == 1) {
                    session = new SessionManagement(getApplicationContext());
                    session.createLogin(user);

                    Intent i = new Intent(this, com.example.doreopartners.bg_tgl.Home_page.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry, your account has been deactivated. Kindly report to the admin",
                            Toast.LENGTH_SHORT).show();
                }
            }else
            {

                Toast.makeText(getApplicationContext(), "Wrong Credentials",
                        Toast.LENGTH_SHORT).show();
                //attempts.setBackgroundColor(Color.RED);
                counter--;
                //attempts.setText(Integer.toString(counter));
                if(counter==0){
                    login.setEnabled(false);
                }
            }
        }
        catch(Exception e){e.getStackTrace();}
    }
}
