package com.example.doreopartners.bg_tgl;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;

public class ViewTgl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tgl);

        Uri uri = getIntent().getData();

        String URL = "content://com.example.provider.bg_tgl/tgl";
        Uri students = Uri.parse(URL);
        String grades = "NAME and IK NUMBER\n";
        
        String id = "0";
        int count = 0;
        try{
            if(!id.equalsIgnoreCase("") && Integer.parseInt(id)>0){
                Cursor c = managedQuery(students, null,  usersProvider._ID +  " = " + id, null, "tgl_name");
                if (c.moveToFirst()) {
                    do{
                        count +=1;
                        Toast.makeText(this,
                                c.getString(c.getColumnIndex(usersProvider._ID)) +
                                        ", " +  c.getString(c.getColumnIndex( usersProvider.ikNumber)) +
                                        ", " + c.getString(c.getColumnIndex( usersProvider.ikName)),
                                Toast.LENGTH_SHORT).show();
                        grades+=c.getString(c.getColumnIndex( usersProvider.ikNumber))+", "+c.getString(c.getColumnIndex( usersProvider.ikName))+"\n ";
                    } while (c.moveToNext());
                    ((TextView)findViewById(R.id.show_data)).setText(grades);
                }if(count <1)Toast.makeText(getBaseContext(), "No records found for this id", Toast.LENGTH_LONG).show();
            }else{
                try{  Cursor c = managedQuery(students, null, null, null, "tgl_name");
                    if (c.moveToFirst()) {
                        do{
                            count +=1;
                            grades+=c.getString(c.getColumnIndex( usersProvider.ikName))+", "+c.getString(c.getColumnIndex( usersProvider.ikNumber))+"\n ";
                        } while (c.moveToNext());
                        ((TextView)findViewById(R.id.show_data)).setText(grades);
                    }if(count <1)Toast.makeText(getBaseContext(), "No records found", Toast.LENGTH_LONG).show();

                }
                catch(Exception ex){
                    Toast.makeText(getBaseContext(), "The ID must be a number", Toast.LENGTH_LONG).show();
                    //}
                }
            }
        }catch(Exception e){Toast.makeText(getBaseContext(), "View Record  "+e, Toast.LENGTH_LONG).show();}

    }
}
