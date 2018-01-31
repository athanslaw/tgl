package com.example.doreopartners.bg_tgl;

/**
 * Created by Doreo Partners on 05/01/2018.
 */
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // User name (make variable public to access from outside)
    public static final String IK_Number = "ik_number";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Question Number (make variable public to access from outside)
    public static final String QUESTION_NO = "questionNo";

    // Section 1 (make variable public to access from outside)
    public static final String SECTION_1 = "section_1";

    // Section 2 (make variable public to access from outside)
    public static final String SECTION_2 = "section_2";

    // Section 3 (make variable public to access from outside)
    public static final String SECTION_3 = "section_3";


    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }

    public void createLogin(String name){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // commit changes
        editor.commit();
    }

    public void setSection1(String section_1){
        // Storing name in pref
        editor.putString(SECTION_1, section_1);

        // commit changes
        editor.commit();
    }

    public void setSection2(String section_2){
        // Storing name in pref
        editor.putString(SECTION_2, section_2);

        // commit changes
        editor.commit();
    }

    public void setSection3(String section_3){
        // Storing name in pref
        editor.putString(SECTION_3, section_3);

        // commit changes
        editor.commit();
    }

    public void setIK_Number(String ikNumber){
        // Storing name in pref
        editor.putString(IK_Number, ikNumber);

        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
     //   user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // QuestionNo
        user.put(QUESTION_NO, pref.getString(QUESTION_NO, null));

        // user email id
    //    user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

         //TGL IK Number
            user.put(IK_Number, pref.getString(IK_Number, null));

        //TGL Interviewer
            user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        //TGL Section 1
        user.put(SECTION_1, pref.getString(SECTION_1, null));

        //TGL Section 2
        user.put(SECTION_2, pref.getString(SECTION_2, null));

        //TGL Section 3
        user.put(SECTION_3, pref.getString(SECTION_3, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Clear session details
     * */
    public void newApplicant(){
        // Clearing some data from Shared Preferences
        editor.remove(IK_Number);
        editor.remove(SECTION_1);
        editor.remove(SECTION_2);
        editor.remove(SECTION_3);
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Home_page.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}
