package com.example.httptest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "MOBILE_NO";

	public static final int HANDPHONE = 0;

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	public void setMobile(int gmo){
        editor.putInt("HANDPHONE", gmo);
        editor.commit();
	}
	
	public int getMobile(){
		return pref.getInt("HANDPHONE", -1);
	}
	
	public boolean check(){
		return pref.contains("HANDPHONE");
	}
}