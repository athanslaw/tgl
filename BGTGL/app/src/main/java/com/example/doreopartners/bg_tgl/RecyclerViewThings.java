package com.example.doreopartners.bg_tgl;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewThings extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_things);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<Questions> input = new ArrayList<>();

        Uri uri = getIntent().getData();

        String URL = "content://com.example.provider.bg_tgl/tgl";
        Uri students = Uri.parse(URL);
        String grades = "NAME and IK NUMBER\n";

        String grade = "";
        int count = 0;
        try{
                try{  Cursor c = managedQuery(students, null, null, null, "tgl_name");
                    if (c.moveToFirst()) {
                        do{
                            count +=1;

                            grade = Integer.parseInt(c.getString(c.getColumnIndex( usersProvider.score)))>=21?"Passed":"Failed";

                            input.add(new Questions(count+"", "IK NUMBER: "+c.getString(c.getColumnIndex( usersProvider.ikNumber)), "IK NAME: "+c.getString(c.getColumnIndex( usersProvider.ikName)), "Grade: "+grade, "SCORE: "+c.getString(c.getColumnIndex( usersProvider.score)), "VILLAGE: "+c.getString(c.getColumnIndex( usersProvider.village)),
                                    "TFM DATE: "+c.getString(c.getColumnIndex( usersProvider.tfm_date)), "TFM TIME: "+c.getString(c.getColumnIndex( usersProvider.tfm_time)), "AMIK: "+c.getString(c.getColumnIndex( usersProvider.amik))));
                        } while (c.moveToNext());

                    }if(count <1)Toast.makeText(getBaseContext(), "No records found", Toast.LENGTH_LONG).show();
                    mAdapter = new MyAdapter(input);
                    recyclerView.setAdapter(mAdapter);
                }
                catch(Exception ex){
                    Toast.makeText(getBaseContext(), "The ID must be a number", Toast.LENGTH_LONG).show();
                }
        }catch(Exception e){Toast.makeText(getBaseContext(), "View Record  "+e, Toast.LENGTH_LONG).show();}

    }

    public void deletes(){
        try{
            // Delete a single record
            String id = ((TextView)findViewById(R.id.r_ikNumber)).getText().toString();
            int no = 0;
            try{
                System.out.println("id = "+id);
                no = getContentResolver().delete(usersProvider.CONTENT_URI, usersProvider.ikNumber +  " = '" + id+"'", null);
                Toast.makeText(getBaseContext(),
                        no+" record deleted", Toast.LENGTH_LONG).show();}
            catch(Exception e){System.out.println("The ID entered does not exist");
            }
        }
        catch(Exception e){
            System.out.println(e+" Kindly choose an option or click on skip to proceed");}
    }

}
