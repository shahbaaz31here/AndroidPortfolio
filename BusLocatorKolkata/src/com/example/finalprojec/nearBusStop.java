package com.example.finalprojec;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class nearBusStop extends FragmentActivity implements LocationListener{
	
	GoogleMap mGoogleMap;	
	Spinner mSprPlaceType;	
	
	boolean gps_enabled = false;
    boolean network_enabled = false;
    Marker marker=null;
	double mLatitude=0;
	double mLongitude=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearbustop);
		//mGoogleMap.clear();
		
		
		
		Button btnFind;
		
		
		
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

        	int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available
        	
	    	// Getting reference to the SupportMapFragment
	    	SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	    			
	    	// Getting Google Map
	    	mGoogleMap = fragment.getMap();
	    	Log.e("got map","yes")	;
	    	// Enabling MyLocation in Google Map
	    	mGoogleMap.setMyLocationEnabled(true);
	    	
	    	
	    	
	    	// Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            
            // Creating a criteria object to retrieve provider
           // Criteria criteria = new Criteria();

            // Getting the name of the best provider
            //String provider = locationManager.getBestProvider(criteria, true);
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            String provider;
            Location location=null;;
            if(gps_enabled)
            	location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            if(network_enabled)
             location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            Log.e("got gps provider","yes")	;
            // Getting Current Location From GPS
           // Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
                   onLocationChanged(location);
            	Log.e("my location","got")	;
            	//my_showCurrentMapPos(location);
            }
            if(gps_enabled)
            {
            	locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 20000, 0, this);
            	
            }
            if(network_enabled)
                {
            	locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 20000, 0, this);
            	
            	}
                
            
	    	
	    	
										
					
					StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
					sb.append("location="+mLatitude+","+mLongitude);
					sb.append("&radius=2000");
					sb.append("&types="+"bus_station");
					sb.append("&sensor=true");
					sb.append("&key=AIzaSyDefjDi7utsCbpkeCHExNN3L72-tmS3a3E");
					
					
					// Creating a new non-ui thread task to download Google place json data 
			        PlacesTask placesTask = new PlacesTask();		        			        
			        
					// Invokes the "doInBackground()" method of the class PlaceTask
			        placesTask.execute(sb.toString());
					
					
				}//end else
		//	});
	    	
        //}		
 		
}
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);                
                

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();                

                // Connecting to url 
                urlConnection.connect();                

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }

                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }

        return data;
    }         

	
	/** A class, to download Google Places */
	private class PlacesTask extends AsyncTask<String, Integer, String>{

		String data = null;
		
		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try{
				data = downloadUrl(url[0]);
			}catch(Exception e){
				 Log.d("Background Task",e.toString());
			}
			return data;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result){			
			ParserTask parserTask = new ParserTask();
			
			// Start parsing the Google places in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			parserTask.execute(result);
		}
		
	}
	
	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

		JSONObject jObject;
		
		// Invoked by execute() method of this object
		@Override
		protected List<HashMap<String,String>> doInBackground(String... jsonData) {
		
			List<HashMap<String, String>> places = null;			
			placeJSONParser placeJsonParser = new placeJSONParser();
        
	        try{
	        	jObject = new JSONObject(jsonData[0]);
	        	
	            /** Getting the parsed data as a List construct */
	            places = placeJsonParser.parse(jObject);
	            
	        }catch(Exception e){
	                Log.d("Exception",e.toString());
	        }
	        return places;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String,String>> list){			
			
			// Clears all the existing markers 
			//mGoogleMap.clear();
			for(int i=0;i<list.size();i++){
				
				// Creating a marker
	            MarkerOptions markerOptions = new MarkerOptions();
	            
	            // Getting a place from the places list
	            HashMap<String, String> hmPlace = list.get(i);
	
	            // Getting latitude of the place
	            double lat = Double.parseDouble(hmPlace.get("lat"));	            
	            
	            // Getting longitude of the place
	            double lng = Double.parseDouble(hmPlace.get("lng"));
	           // Location loc;
	            float[] results=new float[1];
	            try {
					Location.distanceBetween(mLatitude, mLongitude, lat, lng,
							results);
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("Distanceto","except");
				}
	            
	            //to remove decimal
	            
				// Getting name
	            String name = hmPlace.get("place_name");
	            
	            // Getting vicinity
	            String vicinity = hmPlace.get("vicinity");
	            
	            LatLng latLng = new LatLng(lat, lng);
	            
	            // Setting the position for the marker
	            markerOptions.position(latLng);
	            
	            // Setting the title for the marker. 
	            //This will be displayed on taping the marker
	            markerOptions.title(name + " : " + vicinity);
	            if(results[0]>=1000.00)
	            	{
	            	DecimalFormat df = new DecimalFormat("#.#");
	            	results[0]/=1000;
	            	markerOptions.snippet("Distance: "+String.valueOf(df.format(results[0]))+" km");
	            	}
	            else
	            	 {
	            	DecimalFormat df = new DecimalFormat("#");
	            	markerOptions.snippet("Distance: "+String.valueOf(df.format(results[0]))+" metre");
	            	 }
	            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop));
	         
	            // Placing a marker on the touched position
	            mGoogleMap.addMarker(markerOptions);            
            
			}		
			
		}
		
	}
	
	

	

	@Override
	public void onLocationChanged(Location location) {
		Log.e("change","bingo")	;
		if(marker!=null)
			marker.remove();
		mLatitude=location.getLatitude();

		mLongitude=location.getLongitude();
		
		// Creating a LatLng object for the current location
	    LatLng latLng = new LatLng(mLatitude, mLongitude);
	    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	    
	 // Zoom in the Google Map
	    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//adding marker
	  
	   marker=mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("hello").snippet("You are here"));
	   marker.showInfoWindow();
		
	}
	public void my_showCurrentMapPos(Location location)
    {
    	
		//TextView tv=(TextView) findViewById(R.id.tv_location);
		//getting the latitude and longitude
		mLatitude=location.getLatitude();

		mLongitude=location.getLongitude();
		
		// Creating a LatLng object for the current location
	    LatLng latLng = new LatLng(mLatitude, mLongitude);
	    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	    
	 // Zoom in the Google Map
	    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//adding marker
	    Marker marker;
	   marker=mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("hello").snippet("You are here"));
	   marker.showInfoWindow();

	    
	 
	    
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

