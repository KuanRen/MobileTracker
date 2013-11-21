package com.example.httptest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		Button button1 = (Button)findViewById(R.id.button1);  
		final EditText number = (EditText) findViewById(R.id.editText1);
		
		
		 button1.setOnClickListener(new OnClickListener() {
		      public void onClick(View arg) { 
		    	  
		    	  int hpnumber = Integer.parseInt(number.getText().toString());
		    	  
		    	  
		    	  
		    	  if(number.getText().length() != 8 ){
		    		  
						Toast.makeText(getBaseContext(), "Invalid Number!", Toast.LENGTH_SHORT).show();
						
		    	  }
		    	  
		    	  else if(hpnumber != 97379176){
		    		  
						Toast.makeText(getBaseContext(),"Number Doesn't Exist!", Toast.LENGTH_SHORT).show();
		    	  }
		    	  else{
		    		  
		    	  Intent goToNextActivity = new Intent(getApplicationContext(), LocationMap.class);
		    	  startActivity(goToNextActivity);
		    	  
		    	  }
					
		      }
   
		 });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_screen, menu);
		return true;
	}

}