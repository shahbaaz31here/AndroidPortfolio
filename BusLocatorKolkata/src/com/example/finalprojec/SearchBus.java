package com.example.finalprojec;

import java.util.ArrayList;
import java.util.List;

import com.example.finalprojec.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class SearchBus extends Activity {
	
	String from_str,to_str,Busno_str,outstream_str,Bus_type;
	TextView routeview,from,to,bus_no;
	AutoCompleteTextView userinput;
	ArrayAdapter<String> adapter;
	final Context context = this;
    SQLiteDatabase db1;
    private static String DBNAME1 = "bus.db";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prompts);
		
		 
		
		userinput=(AutoCompleteTextView) findViewById(R.id.autoBusNo);
			
		db1=openOrCreateDatabase(DBNAME1, Context.MODE_PRIVATE,null);
		//initialize autocomplete text view
		autoComplete();	
		
		
		
		userinput.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
			 Busno_str=userinput.getText().toString().trim();
		    	Log.e("Busno",Busno_str);
				String nul=null;
			 outstream_str="";
			 from_str=null;
			 to_str=null;
			 
				
				int i=1;
				try {
					String sql="select bus_type from bus_table where bus_no='"+Busno_str+"'";
					Cursor c=db1.rawQuery(sql,null);
					c.moveToFirst();
					Bus_type=c.getString(c.getColumnIndex("bus_type"));
					 sql="select stop1 from bus_route where route_no=(select route_no from bus_table where bus_no='"+Busno_str+"') and stop1<>'"+nul+"'";
				
					 c=db1.rawQuery(sql,null);
						//Toast.makeText(getApplicationContext(),"search for :"+c.getCount()+" "+parent.getItemAtPosition(pos),Toast.LENGTH_SHORT).show();
						if(c!=null)
						{
							if(c.moveToFirst())
								do{
								
								String route=c.getString(c.getColumnIndex("stop1"));//bus stopps
								
								outstream_str+=(i++)+".  "+route+"\n";
								
								
								}while(c.moveToNext());
						c.moveToFirst();
						from_str=c.getString(c.getColumnIndex("stop1"));
						c.moveToLast();
						to_str=c.getString(c.getColumnIndex("stop1"));
						//
						//calling to show busRoute
						busRouteDialog();
						//Busno=parent.getItemAtPosition(pos).toString();
						/*Intent gotoRouteLayout=new Intent(getApplicationContext(),RouteLayout.class);
						
						gotoRouteLayout.putExtra("outstr",outstream);
						
						gotoRouteLayout.putExtra("fro",from);
						gotoRouteLayout.putExtra("too",to);
						//gotoRouteLayout.putExtra("too",to);
						gotoRouteLayout.putExtra("busno",busno);
						
						startActivity(gotoRouteLayout);*/
						}
						else
							Toast.makeText(getApplicationContext(), "Sorry ..Bus Number Not there",Toast.LENGTH_SHORT).show();
						
						
				} catch (Exception e) {
					
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("shaz",Busno_str);
				}
			}

			
		});
	}//end create
	
	public void onBack(View v)
	{
		onBackPressed();
		
	}
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
					//adapter.setDropDownViewResource(R.layout.my_custom_dropdown);
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
	
	public void busRouteDialog() {
		// TODO Auto-generated method stub
		LayoutInflater li=LayoutInflater.from(context);
		View promptView=li.inflate(R.layout.routelayout,null);
		final AlertDialog.Builder prompt=new Builder(context);
		
		prompt.setView(promptView);
		
		//From route layout for routeprompt
				routeview=(TextView)promptView.findViewById(R.id.routview);
				routeview.setMovementMethod(new ScrollingMovementMethod());
				from=(TextView)promptView.findViewById(R.id.fromval);
				to=(TextView)promptView.findViewById(R.id.toval);
				bus_no=(TextView)promptView.findViewById(R.id.busnoval);
		
		from.setText(from_str);
		to.setText(to_str);
		bus_no.setText(Bus_type);
		prompt.setTitle("Bus no: "+Busno_str);
		routeview.setText(outstream_str);
		prompt.setNegativeButton("OK",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
			
			
		
		AlertDialog dialog=prompt.create();
		
		dialog.show();
	}
}
