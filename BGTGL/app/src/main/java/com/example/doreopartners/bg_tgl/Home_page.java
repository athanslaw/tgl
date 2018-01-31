package com.example.doreopartners.bg_tgl;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Home_page extends AppCompatActivity {


    //DB Class to perform DB related operations
    //DBController controller = new DBController(this);
    //Progress Dialog Object
    ProgressDialog prgDialog, prgDialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Initialize Progress Dialog properties
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Sending remote data to online database. Please wait...");
        prgDialog.setCancelable(false);

        //Initialize Progress2 Dialog properties
        prgDialog2 = new ProgressDialog(this);
        prgDialog2.setMessage("Loading data from online database. Please wait...");
        prgDialog2.setCancelable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //When Sync action button is clicked
        if (id == R.id.refresh) {
            //Sync SQLite DB data to remote MySQL DB
            syncSQLiteMySQLDB();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void recyclerActivity(View view){
        try{
            Intent i = new Intent(this, com.example.doreopartners.bg_tgl.RecyclerViewThings.class);
            startActivity(i);
        }
        catch(Exception e){e.getStackTrace();}
    }

    public void startInterview(View view){
        try{
            Intent i = new Intent(this, com.example.doreopartners.bg_tgl.TGL_Info.class);
            startActivity(i);
        }
        catch(Exception e){e.getStackTrace();}
    }

    public void syncSQLiteMySQLDB(){

        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<Questions> userList =  this.view_record();
        if(userList.size()!=0){
                prgDialog.show();
                params.put("usersJSON", composeJSONfromSQLite());
                client.post("http://tgl.babbangona.com/tgl_insertuser.php",params ,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        prgDialog.hide();
                        try {
                            JSONArray arr = new JSONArray(response);
                            for(int i=0; i<arr.length();i++){
                                JSONObject obj = (JSONObject)arr.get(i);
                                updatedb(obj.get("id").toString(),obj.get("status").toString());
                            }
                            Toast.makeText(getApplicationContext(), "DB Sync completed!", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // TODO Auto-generated method stub
                        prgDialog.hide();
                        if(statusCode == 404){
                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }else if(statusCode == 500){
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        }else{
            Toast.makeText(getApplicationContext(), "No data in Phone's DB to move online", Toast.LENGTH_LONG).show();
        }
    }

    public void syncSQLiteMySQLDB(View view){

        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<Questions> userList =  view_record();
        if(userList.size()!=0){
            if(dbSyncCount() != 0){
                prgDialog.show();
                params.put("usersJSON", composeJSONfromSQLite());

                client.post("http://tgl.babbangona.com/tglinterview/tgl_insertuser.php",params ,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("Response Starts: "+response+" Response ends ");
                        prgDialog.hide();
                        try {
                            JSONArray arr = new JSONArray(response);
                            System.out.println(arr.length());
                            for(int i=0; i<arr.length();i++){
                                JSONObject obj = (JSONObject)arr.get(i);
                                System.out.println(obj.get("id"));
                                System.out.println(obj.get("status"));
                                updatedb(obj.get("id").toString(),obj.get("status").toString());
                            }
                            Toast.makeText(getApplicationContext(), "DB Sync completed!", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            System.out.println("Yes o: "+e);
                            Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // TODO Auto-generated method stub
                        prgDialog.hide();
                        if(statusCode == 404){
                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }else if(statusCode == 500){
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(), "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "No new data to send", Toast.LENGTH_LONG).show();
        }
    }

    public void refreshSQLiteMySQLDB(View view){

        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        Cursor userList =  viwTGLsDateDesc();
        System.out.println("Before userList");

        if(userList.getCount()>0){
                prgDialog2.show();
                params.put("usersJSON", composeJSONfromSQLiteDate());
                System.out.println("Parameters to post online: "+params);
                client.post("http://tgl.babbangona.com/tglinterview/tgl_fetchuser.php",params ,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();

                        String dates = dateFormat.format(date);
                        prgDialog2.hide();
                        try {
                            JSONArray arr = new JSONArray(response);
                            System.out.println(arr.length()+ " Athans");
                            for(int i=0; i<arr.length();i++){
                                JSONObject obj = (JSONObject)arr.get(i);
                                System.out.println(obj.get("iknumber")+", "+obj.get("ikname"));
                                insertTGL(obj.get("iknumber").toString(), obj.get("ikname").toString(), "1", dates);
                            }
                            Toast.makeText(getApplicationContext(), "IK Numbers successfully refreshed!", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            System.out.println("refreshSQLiteMySQLDB o: "+e);
                            Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // TODO Auto-generated method stub
                        prgDialog2.hide();
                        if(statusCode == 404){
                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }else if(statusCode == 500){
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        }else{
            Toast.makeText(getApplicationContext(), "No new data online. Your database is up to date", Toast.LENGTH_LONG).show();
        }
    }

    protected ArrayList<Questions> view_record() {
        ArrayList<Questions> input = new ArrayList<>();

        Uri uri = getIntent().getData();

        String URL = "content://com.example.provider.bg_tgl/tgl";
        Uri students = Uri.parse(URL);

        int count = 0;
        try{
            try{  Cursor c = managedQuery(students, null, usersProvider.status +  " = '0'", null, null);
                if (c.moveToFirst()) {
                    do{
                        count +=1;
                        input.add(new Questions(c.getString(c.getColumnIndex( usersProvider._ID)), c.getString(c.getColumnIndex( usersProvider.ikName)),
                                c.getString(c.getColumnIndex( usersProvider.ikNumber)), c.getString(c.getColumnIndex( usersProvider.qus1)), c.getString(c.getColumnIndex( usersProvider.qus2)), c.getString(c.getColumnIndex( usersProvider.qus3)),
                                c.getString(c.getColumnIndex( usersProvider.qus4)), c.getString(c.getColumnIndex( usersProvider.qus5)), c.getString(c.getColumnIndex( usersProvider.qus6)),
                                c.getString(c.getColumnIndex( usersProvider.qus7)), c.getString(c.getColumnIndex( usersProvider.qus8)), c.getString(c.getColumnIndex( usersProvider.qus9)), c.getString(c.getColumnIndex( usersProvider.qus10)), c.getString(c.getColumnIndex( usersProvider.qus11))
                                , c.getString(c.getColumnIndex( usersProvider.qus12)), c.getString(c.getColumnIndex( usersProvider.score)), c.getString(c.getColumnIndex( usersProvider.status)), c.getString(c.getColumnIndex( usersProvider.interviewer)), c.getString(c.getColumnIndex( usersProvider.date))
                                , c.getString(c.getColumnIndex( usersProvider.village)), c.getString(c.getColumnIndex( usersProvider.amik)), c.getString(c.getColumnIndex( usersProvider.tfm_date)), c.getString(c.getColumnIndex( usersProvider.tfm_time))));
                    } while (c.moveToNext());

                }
            }
            catch(Exception ex){
                System.out.println("The ID must be a number");
            }
        }catch(Exception e){System.out.println("View Record  "+e);}

        return input;
    }

    protected Cursor viwTGLsDateDesc() {
        TGLDB db = new TGLDB(this);
        System.out.println("Here viewTGLsDateDesc");
        return db.getTGLsDateDesc(); // you would not typically call this on the main thread
    }

    protected int dbSyncCount() {
        Uri uri = getIntent().getData();

        String URL = "content://com.example.provider.bg_tgl/tgl";
        Uri students = Uri.parse(URL);

        int count = 0;
        try{
            try{  Cursor c = managedQuery(students, null, usersProvider.status +  " = '0'", null, null);
                return c.getCount();
            }
            catch(Exception ex){
                System.out.println("dbSyncCount: "+ex);
            }
        }catch(Exception e){Toast.makeText(getBaseContext(), "View Record  "+e, Toast.LENGTH_LONG).show();}

        return 0;
    }

    public String composeJSONfromSQLite(){
        ArrayList<Questions> input = new ArrayList<>();

        Uri uri = getIntent().getData();

        String URL = "content://com.example.provider.bg_tgl/tgl";
        Uri students = Uri.parse(URL);

        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        try{
            try{  Cursor cursor = managedQuery(students, null, usersProvider.status +  " = '0'", null, null);
                if (cursor.moveToFirst()) {
                    HashMap<String, String> map;
                    do{
                        map = new HashMap<String, String>();
                        map.put("id", cursor.getString(0));
                        map.put("ik_number", cursor.getString(1));
                        map.put("tgl_name", cursor.getString(2));
                        map.put("qus1", cursor.getString(3));
                        map.put("qus2", cursor.getString(4));
                        map.put("qus3", cursor.getString(5));
                        map.put("qus4", cursor.getString(6));
                        map.put("qus5", cursor.getString(7));
                        map.put("qus6", cursor.getString(8));
                        map.put("qus7", cursor.getString(9));
                        map.put("qus8", cursor.getString(10));
                        map.put("qus9", cursor.getString(11));
                        map.put("qus10", cursor.getString(12));
                        map.put("qus11", cursor.getString(13));
                        map.put("qus12", cursor.getString(14));
                        map.put("score", cursor.getString(15));
                        map.put("interviewer", cursor.getString(16));
                        map.put("date", cursor.getString(17));
                        map.put("status", cursor.getString(18));
                        map.put("village", cursor.getString(19));
                        map.put("amik", cursor.getString(20));
                        map.put("tfm_date", cursor.getString(21));
                        map.put("tfm_time", cursor.getString(22));
                        wordList.add(map);
                    } while (cursor.moveToNext());

                }
            }
            catch(Exception ex){
                System.out.println("The ID must be a number"+ ex);
            }
        }catch(Exception e){
            System.out.println("View Record  "+e);}

        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public String composeJSONfromSQLiteDate(){

        System.out.println("Here composeJSONfromSQLiteDate");
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        try{
            TGLDB db = new TGLDB(this);
            Cursor cursor = db.getTGLsDateDesc(); // you would not typically call this on the main thread

            HashMap<String, String> map;
            do{
                map = new HashMap<String, String>();
                map.put("date", cursor.getString(4));
                System.out.println("Date: "+cursor.getString(4));
                wordList.add(map);
                break;
            } while (cursor.moveToNext());

        }catch(Exception e){
            System.out.println("View Record  "+e);}

        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public void updatedb(String id, String status){
        try{
            int id1 = Integer.parseInt(id);
            ContentValues values = new ContentValues();
            values.put(usersProvider.status, status);
            getContentResolver().update(usersProvider.CONTENT_URI, values, usersProvider._ID +  " = " + id1, null);
        }
        catch(Exception e){
            Toast.makeText(getBaseContext(),
                    "Kindly choose an option or click on skip to proceed", Toast.LENGTH_LONG).show();}
    }

    public void insertTGL(String iknumber, String ikname, String status, String dates){
        try{
            HashMap<String, String> values = new HashMap<String, String>();

            values.put("IKNumber", iknumber);
            values.put("IKName", ikname);
            values.put("status", status);
            values.put("date", dates);
            TGLDB db = new TGLDB(this);
            db.insertRecord(values);
        }
        catch(Exception e){
            Toast.makeText(getBaseContext(),
                    "Kindly choose an option or click on skip to proceed", Toast.LENGTH_LONG).show();}
    }

    public void rescheduleTfm(View view){
        try{
            Intent i = new Intent(this, com.example.doreopartners.bg_tgl.RescheduleTFM.class);
            startActivity(i);
        }
        catch(Exception e){e.getStackTrace();}
    }

}
