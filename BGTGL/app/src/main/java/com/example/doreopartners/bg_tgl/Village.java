package com.example.doreopartners.bg_tgl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Village extends AppCompatActivity  {

    private Cursor c;
    private VillageDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village);

        String villages = "Village";
        db = new VillageDB(this);
        c = db.getVillages(); // you would not typically call this on the main thread
        if (c.moveToFirst()) {
            do {
                villages += "__" + c.getString(1);
            } while (c.moveToNext());
            ((TextView) findViewById(R.id.employee_name)).setText(villages);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        c.close();
        db.close();
    }

}
