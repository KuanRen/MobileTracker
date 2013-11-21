package com.example.httptest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements  LocationListener {
	private TextView value, tvLongitude, tvLatitude, address1, address2, number;
	private EditText mobile;
	private ToggleButton btn;
	private ProgressBar pb;
	LocationManager locationManager ;
    String provider;
    private Timer mTimer1;
    private TimerTask mTt1;
    private CheckBox checkBox;
    private Button submit;
    private Handler mTimerHandler = new Handler();
    private String gmo;
    

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		number =(TextView) findViewById(R.id.textView11);
		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		value = (TextView) findViewById(R.id.textView1);
		address1 = (TextView) findViewById(R.id.textView6);
		address2 = (TextView) findViewById(R.id.textView7);
		tvLongitude = (TextView)findViewById(R.id.textView2);
        tvLatitude = (TextView)findViewById(R.id.textView3);
		btn = (ToggleButton) findViewById(R.id.toggleButton1);
		mobile = (EditText) findViewById(R.id.mobile);
		submit = (Button) findViewById(R.id.button1);
		pb = (ProgressBar) findViewById(R.id.progressBar1);   
		
		pb.setVisibility(View.INVISIBLE);
		
		final SessionManager sm = new SessionManager(getBaseContext());
		
		if (sm.check()==false) {
			
			mobile.setVisibility(View.VISIBLE);
			submit.setVisibility(View.VISIBLE);
			number.setVisibility(View.VISIBLE);
			btn.setVisibility(View.INVISIBLE);
			
		} else {
			
			mobile.setVisibility(View.INVISIBLE);
			submit.setVisibility(View.INVISIBLE);
			number.setVisibility(View.INVISIBLE);
			btn.setVisibility(View.VISIBLE);
			gmo = Integer.toString(sm.getMobile());
			
		}
		
		submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				gmo = mobile.getText().toString();
				
				if(mobile.getText().length() < 8){
					
				Toast.makeText(getBaseContext(), "Please Enter Valid Number", Toast.LENGTH_SHORT).show();
				
				}
				else{
					
				
				sm.setMobile(Integer.parseInt(gmo));
				recreate(); 
				Toast.makeText(getBaseContext(), "Number Saved", Toast.LENGTH_SHORT).show();
				
				}
			
			}
		});
	}
	
	//back button
	public void onBackPressed() {
		//do nothing
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	//sms send function
	private void sendSMS(String phoneNumber, String message)
	{
	SmsManager sms = SmsManager.getDefault();
	sms.sendTextMessage(phoneNumber, null, message, null, null);
	}
	
	//Get Coordinates and address method
	public void getAddress(){
		
		String status = null;
		// Getting LocationManager object
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
 
        // Creating an empty criteria object
        Criteria criteria = new Criteria();
 
        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);
		
		if(provider!=null && !provider.equals("")){
			 
            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);
 
            locationManager.requestLocationUpdates(provider, 20000, 1, this);
        
		
		 List<String>  providerList = locationManager.getAllProviders();
         if(null!=location && null!=providerList && providerList.size()>0){                 
         double longitude = location.getLongitude();
         double latitude = location.getLatitude();
         Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());                 
         
         try {
             List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
             if(null!=listAddresses&&listAddresses.size()>0){
                 String _Location = listAddresses.get(0).getAddressLine(0);
                 String _City = listAddresses.get(0).getAddressLine(1);
                 
                 address1.setText(_Location);
                 address2.setText(_City);
                 
             }
         } catch (IOException e) {
             e.printStackTrace();
         	}
         
         }

         if(location!=null){
             onLocationChanged(location);
         }
         
         else {
        	 
             Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
	         address1.setText("Not Available");
	         address2.setText("Not Available");
	         tvLongitude.setText("Not Available");
	         tvLatitude.setText("Not Available");
	         status = address1.getText().toString();   
         }
		
		if(status == "Not Available"){
			
			Toast.makeText(getBaseContext(), "GPS Not turned on or signal not available", Toast.LENGTH_LONG).show();
			
		}
		else {
			
			String phoneno = "97379176";
			TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			new RegisterIMEITask().execute(phoneno, telephonyManager.getDeviceId(), gmo);		
		}

     }else{
         Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
     }
	}
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
	
	/*private void stopTimer(){
        if(mTimer1 != null){
            mTimer1.cancel();
            mTimer1.purge();         
        }
    }*/
	
	
	//Method to send Location periodically	
    private void startTimer(){
    	
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                	
                    public void run(){
                    	onToggleClicked(btn);
                    	
                    }
                });
            }
        };

        //600000 = 10 mins
        mTimer1.schedule(mTt1, 1, 60000);
    }
	
	
	public void onToggleClicked(View v) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) v).isChecked();
	    
	    
	
	    if ((on) && (mTimer1 == null)) {
	    	startTimer();
	    }
	    
	    else if ((on) && (mTimer1 != null)) {
	    	
	    	pb.setVisibility(View.VISIBLE);
	   
	    	//Get Coordinates and address method
	    	getAddress();
  	
	    }       

		else {
			
			mTimer1.cancel();
	        mTimer1.purge(); 
	        mTimer1 = null;
	        checkBox.setChecked(false);
	        
	    	value.setText("Feed Inactive");
	    	pb.setVisibility(View.INVISIBLE);
	    	Toast.makeText(getBaseContext(), "Your Location Has Stopped Being Sent", Toast.LENGTH_LONG).show();
	    }
	}

	private class MyAsyncTask extends AsyncTask<String, Integer, String> {
		String mobile;
		
		@Override
		protected String doInBackground(String... params) 
		{
			mobile = params[2];
			TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			return postData(telephonyManager.getDeviceId(), params[0], params[1]);
		}

		protected void onPostExecute(String result) {
			pb.setVisibility(View.GONE);
			
			if(result != "Failed"){
			value.setText("Success!");
			checkBox.setChecked(true);
			Toast.makeText(getBaseContext(), "Your Location Is Actively Being Sent", Toast.LENGTH_LONG).show();
			
			//send sms of elderly location to caregiver
			sendSMS(mobile, "Hello, current location of elderly is at " +address1.getText().toString() +" " + address2.getText().toString());
			}
			else{
				value.setText("Failed!");
				checkBox.setChecked(false);
				Toast.makeText(getBaseContext(), "Your Location Is Not Being Sent", Toast.LENGTH_LONG).show();
			}
			
		
		}

		protected void onProgressUpdate(Integer... progress) {
			pb.setProgress(progress[0]);
		}
		

		public String postData(String imei, String lat, String lng) {
			HttpClient Client = new DefaultHttpClient();

			// Create URL string
			String URL = "http://2.mytrack101.appspot.com/mytrack?imei="+ imei +"&lat="+lat+"&lng=" +lng;

			String response = "";
			try {
			
				// Create Request to server and get response

				HttpGet httpget = new HttpGet(URL);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				response = Client.execute(httpget, responseHandler);

				// Show response on activity
				
			} catch (Exception ex) {
				response = "Failed";
			}
			return response;
		}
	}
	
	
	private class RegisterIMEITask extends AsyncTask<String, Integer, String> 
	{
		String mobile;
		@Override
		protected String doInBackground(String... params) 
		{
			mobile = params[2];
			return registerIMEI(params[0], params[1]);
		}

		protected void onPostExecute(String result) {
			pb.setVisibility(View.GONE);
			new MyAsyncTask().execute(tvLatitude.getText().toString(), tvLongitude.getText().toString(), mobile);
		}

		protected void onProgressUpdate(Integer... progress) {
			pb.setProgress(progress[0]);
		}

		public String registerIMEI(String phoneno, String imei) {
			HttpClient Client = new DefaultHttpClient();

			// Create URL string
			String URL = "http://2.mytrack101.appspot.com/updateIMEI?phoneno="+phoneno+"&imei="+ imei;
				
			String response = "";
			try {
				

				// Create Request to server and get response

				HttpGet httpget = new HttpGet(URL);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				response = Client.execute(httpget, responseHandler);

				// Show response on activity
				
			} catch (Exception ex) {
				response = "Failed";
			}
			return response;
		}
	}
	
	

	@Override
	public void onLocationChanged(Location location) {
        // Setting Current Longitude
        tvLongitude.setText("" + location.getLongitude());
 
        // Setting Current Latitude
        tvLatitude.setText("" + location.getLatitude() );
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
	
	
}



