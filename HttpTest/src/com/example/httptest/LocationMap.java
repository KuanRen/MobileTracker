package com.example.httptest;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.httptest.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;


public class LocationMap extends FragmentActivity implements LocationListener {
	
	private SupportMapFragment mMapFragment;
	protected GoogleMap mMap;
	protected static LocationManager locationManager;
    protected static String provider;
    protected LatLng lastLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_map);
		makeMap();
	}
	
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		new GetLastLocationTask().execute();
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_map, menu);
		return true;
	}
	
	private void makeMap(){
		mMapFragment = SupportMapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.map, mMapFragment);
		fragmentTransaction.commit();
	}
		private void setUpMapIfNeeded() {
	        if (mMap == null) {
	        	mMap = mMapFragment.getMap();
	        }
	        if (mMap != null) {
	        	
	        	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 12));
	        	mMap.addMarker(new MarkerOptions().position(lastLocation).title("Elderly's Last Location"));
	

	        	/*locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    		Criteria criteria = new Criteria();
	    		provider = locationManager.getBestProvider(criteria, false);
	    		if(provider!=null && !provider.equals("")){
	                Location location = locationManager.getLastKnownLocation(provider);
	                locationManager.requestLocationUpdates(provider, 1000, 1, this);
	                if(location!=null){
	                    onLocationChanged(location);
	                }
	                else {
	                    Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
	                }
	            } else {
	                Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
	            }*/
	        }
	    }

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
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
		
		
		private class GetLastLocationTask extends AsyncTask<String, Integer, String> 
		{
			@Override
			protected String doInBackground(String... params) 
			{
				TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				return getLastLocation(telephonyManager.getDeviceId());
			}

			protected void onPostExecute(String result) {
				//pb.setVisibility(View.GONE);
				try {
					JSONObject object = new JSONObject(result);
					JSONArray jArray = object.getJSONArray("aaData");
					    try {
					        JSONObject oneObject = jArray.getJSONObject(0);
					        // Pulling items from the array
					        String[] latlng = oneObject.getString("latlng").split(",");
					        for(String entity : latlng)
					        {
					        	entity.trim();
					        }
					        
					       LocationMap.this.lastLocation = new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]));
					       
					       setUpMapIfNeeded(); 
					       
					    } catch (JSONException e) {
					        //Error
					    }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			protected void onProgressUpdate(Integer... progress) {
				//pb.setProgress(progress[0]);
			}

			public String getLastLocation(String imei) {
				HttpClient Client = new DefaultHttpClient();

				// Create URL string
				String URL = "http://2.mytrack101.appspot.com/data?imei="+imei;
					
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
	}
	 
