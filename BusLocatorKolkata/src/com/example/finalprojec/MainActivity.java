package com.example.finalprojec;



/*import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;*/

import java.io.IOException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Button sourc_dest,dbentry,busno,map_view,help,more;
	AutoCompleteTextView userinput;
	ArrayAdapter<String> adapter;
	final Context context = this;
    SQLiteDatabase db1;
    private static String DBNAME1 = "bus.db";
	String Outstr = null;
	//source to destination
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DataBaseHelper myDbHelper = new DataBaseHelper(context);
        myDbHelper = new DataBaseHelper(this);
        try {
        	myDbHelper.createDataBase();
        	} catch (IOException ioe) {
        		 		throw new Error("Unable to create database");
	 		}
        try {
        	myDbHelper.openDataBase();
        	}catch(SQLException sqle){
        			throw sqle;
   			}
		
		
		sourc_dest=(Button) findViewById(R.id.sourcdest);
		map_view=(Button) findViewById(R.id.map);
		busno=(Button) findViewById(R.id.busno);
		//help=(Button) findViewById(R.id.help);
		more=(Button) findViewById(R.id.more);
		
		db1=openOrCreateDatabase(DBNAME1, Context.MODE_PRIVATE,null);
		
		
		
		busno.setOnClickListener(new OnClickListener(){
		
			@Override
			public void onClick(View arg0) {
				
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Intent searchBus=new Intent(getApplicationContext(),SearchBus.class);
				startActivity(searchBus);
				
			}
				
			
			
		});//button
		
	}//end oncreate
	
	public void autoComplete()
	{
		List<String> busnos=new ArrayList<String>();
		//busnos=null;
		try {
		String q="select bus_no from bus_table ";
		
			//Log.e("no error","hry");
				Cursor c1 = db1.rawQuery(q,null);
				Log.e("no error",""+c1.getCount());
				
			//if(c1!=null)
			//{
				if (c1.moveToFirst()) 
				{
					Log.e("no error","MOVEFIRST");
					do {

						//String num = c1.getString(c1.getColumnIndex("bus_no"));//bus stopps
						busnos.add(c1.getString(c1.getColumnIndex("bus_no")));
						

					} while (c1.moveToNext());
						adapter = new ArrayAdapter<String>(getApplicationContext(),
								R.layout.my_custom_dropdown, busnos);
					adapter.setDropDownViewResource(R.layout.my_custom_dropdown);
					Log.e("no error","setAdapt2");
					userinput.setAdapter(adapter);
					Log.e("no error","setAdapt");
				}
			//}
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e("shaz","exception");
			}
		
	}
	
	
	public void onClick(View view)
	{
		switch(view.getId())
		{
		case R.id.sourcdest:
			Intent source_dest=new Intent(getApplicationContext(),source_destination.class);
			startActivity(source_dest);
			
		}
	}
	/*public void onClickhelp(View view){
		switch(view.getId())
		{
		case R.id.help:
			Intent h=new Intent(getApplicationContext(),Help.class);
			startActivity(h);
			
		}
	}*/
	public void onClickmore(View view){
		switch(view.getId())
		{
		case R.id.more:
			Intent v=new Intent(getApplicationContext(),More.class);
			startActivity(v);
			
		}
	}

	
	@SuppressLint("NewApi")
	public void onViewClick(View view)
	{
	boolean gpsEnabled = false,networkEnabled=false,networkinfo=false;
	LocationManager	locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	ConnectivityManager cm=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		if(cm.getActiveNetworkInfo()==null)
			{
			Toast.makeText(this,"Internet Connection is not established ",Toast.LENGTH_SHORT).show();
			return;
			}
        // exceptions will be thrown if provider is not permitted.
        
		try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        

        // don't start listeners if no provider is enabled
       if (!gpsEnabled)
        	{
    	   AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
    	   alertDialogBuilder.setTitle("GPS is disabled!");
    	   alertDialogBuilder.setMessage("Show location settings to enable?");
    	   
    	   alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("Yes",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
				// get user input and set it to result
				// edit text
			    	startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));//tick the gps on
		        	
			    }
			  })
			.setNegativeButton("No",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			    }
			  });

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
    	   return;
    	   
        	//Toast.makeText(this,"Please Enable your GPS ",Toast.LENGTH_SHORT).show();
        	//startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));//tick the gps on
        	//return;
        	
        	}
       

		switch(view.getId())
		{
		
		case R.id.nearBusstop:
			
			Intent nearbusstop=new Intent(getApplicationContext(),nearBusStop.class);
			startActivity(nearbusstop);
			break;
		default:
				break;
			
		}
	}
	

	
	
	
	
 }


