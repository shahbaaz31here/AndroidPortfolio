package com.example.finalprojec;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Buslist extends Activity {

	String from_str,to_str,Busno_str,outstream_str,Bus_type;
	TextView routeview,from,to,bus_no;
	SQLiteDatabase db1;
	final Context context = this;
	private static String DBNAME1 = "bus.db";
	ArrayAdapter<String> adapter;
	ArrayList<String> buslist=new ArrayList<String>();
	ListView lv;
	Button back;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buslist);
		
		//Database will be created through below method
				db1=openOrCreateDatabase(DBNAME1, Context.MODE_PRIVATE,null);
		lv=(ListView)findViewById(R.id.Buslist);
		back=(Button)findViewById(R.id.Back);
		lv.setBackgroundColor(Color.parseColor("#ffffff"));
		Intent getBuslist=getIntent();
		buslist=(ArrayList<String>)getBuslist.getSerializableExtra("data");//warning
		
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,buslist);
		
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				String nul=null;
				 outstream_str="";
				from_str = null;
				to_str=null;
				Busno_str=null;
				Busno_str=parent.getItemAtPosition(pos).toString();
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
						
						
						busRouteDialog();
						/*Intent gotoRouteLayout=new Intent(getApplicationContext(),RouteLayout.class);
						
						gotoRouteLayout.putExtra("outstr",outstream);
						
						gotoRouteLayout.putExtra("fro",from);
						gotoRouteLayout.putExtra("too",to);
						//gotoRouteLayout.putExtra("too",to);
						gotoRouteLayout.putExtra("busno",Busno);
						startActivity(gotoRouteLayout);*/
						
						}
						
					//Spanned s=Html.fromHtml(outstream);	
					//routeview.setText(s);
				} catch (Exception e) {
					
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
	}//end create
	
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
		bus_no.setText(Bus_type);//setting the bus_type
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
	
	
		public void onBack(View v)
		{
			switch(v.getId())
			{
			case R.id.Back:
				buslist.clear();
				//Intent goback=new Intent(getApplicationContext(),source_destination.class);
				onBackPressed();//default back button on mobile
				//startActivity(goback);
			}
		}
	
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// TODO Auto-generated method stub
			MenuInflater mif= getMenuInflater();
			mif.inflate(R.menu.activity_main,menu);
			return super.onCreateOptionsMenu(menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			 super.onOptionsItemSelected(item);
			switch(item.getItemId()){
			case R.id.home:
				 Intent i=new Intent(this,MainActivity.class);
				 startActivity(i);
			
				break;
			
			}
			return true;
		}
}

	