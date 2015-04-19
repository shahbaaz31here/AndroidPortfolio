package com.example.finalprojec;

import java.sql.Array;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class source_destination extends Activity {

	SQLiteDatabase db1;
	String	source,dest;
	Button search;
	int colorflag;
	String busType="All";
	 MyAdapter 	adapt;
	 ArrayList<String>  busList=null;
	int breakpoint;
	ArrayList<String> breakstop=new ArrayList<String>();
	private static String DBNAME1 = "bus.db";
	private Spinner from_spin,to_spin;
	ArrayAdapter<String> adapter,spinAdapt,autoadapt,destadapt;
	TextView tvw,buslistNo,routeTitle;
	AutoCompleteTextView inp_from,inp_to;
	Editable d1,d2;
	String Outstr = null,sql;
	
	ArrayList<ArrayList<String>> busListBreak=null;
	List<String> spList=new ArrayList<String>();
	ListView lv,routePlanList;
	List<String> routplnlst= new ArrayList<String>();
	//String[] routplnlst=new String[20];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.source2dest);
		
		
		inp_from=(AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewSource);
		
		//inp_from=(EditText) findViewById(R.id.input_from);
		inp_to=(AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewDestination);
		from_spin=(Spinner) findViewById(R.id.from_spin);
		to_spin=(Spinner) findViewById(R.id.to_spin);
		//adding bus no in text view  routeplanlist.xml
		//buslistNo=(TextView) findViewById(R.id.busview);
		//routeTitle=(TextView) findViewById(R.id.route_title_view);
		//lv=(ListView) findViewById(R.id.listView1);
		lv=(ListView)findViewById(R.id.routeList);
		search=(Button)findViewById(R.id.search);
		
		//Database will be created through below method
		db1=openOrCreateDatabase(DBNAME1, Context.MODE_PRIVATE,null);
		//populateSpinner();
	
		
		
		

		//Selecting data from Spinner to Source place
		from_spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				if(pos==0)
					;
				else
				inp_from.setText(parent.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//Selecting data from Spinner to Destination place
		to_spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				if(pos==0)
					;
				else
				inp_to.setText(parent.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
				
		/*
		 try{
		 
			 sql="create table if not exists bus_table(route_no varchar2(10) NOT NULL ,bus_no varchar2(10) PRIMARY KEY NOT NULL);";
			db1.execSQL(sql);
			sql="create table if not exists bus_route(route_no varchar2(10) NOT NULL  ,stop1 varchar2(40) ,stop2 varchar2(40) );";
			db1.execSQL(sql);
			
			db1.execSQL(sql);
			sql="insert into bus_route(route_no,stop1,stop2) values('route2',null,'Bbdbag');";
			db1.execSQL(sql);
			sql="insert into bus_route(route_no,stop1,stop2) values('route2','Bbdbag','Esplanade');";
			db1.execSQL(sql);
			sql="insert into bus_route(route_no,stop1,stop2) values('route2','Esplanade','Keshtopur');";
			db1.execSQL(sql);
			sql="insert into bus_route(route_no,stop1,stop2) values('route2','Keshtopur',null);";
			db1.execSQL(sql);
			
			
		}
		catch(Exception e){
			System.out.println(e);
		}
		*/
		new PopulateSpnr().execute();//for doing inintializing & showing progress bar
		
		
		
	}//end create()
	
	public void selectCompany(View view)
	{
		boolean checked=((RadioButton) view).isChecked();
		switch(view.getId())
		{
		case R.id.All:
			if(checked)
			{
				RadioButton rb=(RadioButton)findViewById(R.id.All);
				 busType=rb.getText().toString();
			}
			break;
		case R.id.Volvo:
			if(checked)
			{
				RadioButton rb=(RadioButton)findViewById(R.id.Volvo);
				busType=rb.getText().toString();
			}
			break;
		}
	}
	
	
	
	public class PopulateSpnr extends AsyncTask<Void,Void,Void>{//Showing Loading while doing in background
		ProgressDialog dialog=new ProgressDialog(source_destination.this);
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.setMessage("Initializing..");
			dialog.setCancelable(false);
			dialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			try{
				populateSpinner();
				//Thread.sleep(1000);
				
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println(e);
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			spinAdapt=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_expandable_list_item_1,spList);
			spinAdapt.setDropDownViewResource(R.layout.my_custom_dropdown);
			from_spin.setAdapter(spinAdapt);
			to_spin.setAdapter(spinAdapt);
			//Adding the Stop from database to Autocomplete 
			autoadapt=new ArrayAdapter<String>(getApplicationContext(),R.layout.my_custom_dropdown, spList);
			//autoadapt.setDropDownViewResource(R.layout.my_custom_dropdown);
			inp_from.setAdapter(autoadapt);
			Log.e("S111", "1");
			destadapt=new ArrayAdapter<String>(getApplicationContext(),R.layout.my_custom_dropdown, spList);
			//destadapt.setDropDownViewResource(R.layout.my_custom_dropdown);
			inp_to.setAdapter(destadapt);
			Log.e("S111", "2");
			if(dialog.isShowing())
				dialog.dismiss();
		}

		
		
		
	}
	
	//Populating spinner
	public void populateSpinner() {
		// TODO Auto-generated method stub
		try {
			String nul=null;
			String sql="select distinct stop1 from bus_route where stop1<> '"+nul+"' order by stop1";
			Cursor c=db1.rawQuery(sql, null);
			Log.e("Spinner", "noerror");
			//if(c!=null)
			spList.add("Select");
			if(c.moveToFirst())
			{
				do{
					spList.add(c.getString(c.getColumnIndex("stop1")));
				}while(c.moveToNext());
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e("Spinner", "Exeption");
		}
		
	}

	public void onSearch(View v) //clicking on search button
	{
		//busList.clear();
		busListBreak=new ArrayList<ArrayList<String>>();
		routplnlst.clear();
		
		switch(v.getId())
		{
		case R.id.search:
		//TextView busview=null;
			source=inp_from.getText().toString().trim();//source point
			dest=inp_to.getText().toString().trim();//destination point
		/*Toast.makeText(this,"search for :"+source+" "+dest,Toast.LENGTH_SHORT).show();*/
		//int countbus=0;
		//String.matches("[A-Za-z0-9]+")
		
		
		if(source.equals(dest))
		{
			Toast.makeText(getApplicationContext(), "Please Change Source or Destination", Toast.LENGTH_LONG).show();
			break;
		}
		if(source.equals("") || dest.equals(""))
		{
			Toast.makeText(getApplicationContext(), "Please Enter Source/Destination", Toast.LENGTH_LONG).show();
			break;
		}
		Log.e("async","before exexcute");
		LoadData task=new LoadData();
		task.execute();
		Log.e("async","afterexecute");
		
		
		
		
		//lv.setAdapter(null);
		
		
		break;
			
		default:
			break;
			
			
		}
		
	}
	
	public void onBack1(View b)
	{
		switch(b.getId())
		{
		case R.id.Back:
			
			//Intent goback=new Intent(getApplicationContext(),source_destination.class);
			onBackPressed();//default back button on mobile
			//startActivity(goback);
		}
	}
	
	public class MyAdapter extends BaseAdapter{
		private LayoutInflater inflater=null;
	    private List<String> routplnlisst;
	    private Context context;
	   
		public MyAdapter(Context context,
				List<String> routplnlst) {
			// TODO Auto-generated constructor stub
			//super();
	       // inflater = LayoutInflater.from(context);
			
	        this.context = context;
	        this.routplnlisst = routplnlst;
	        inflater = (LayoutInflater) context  
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 

		}
		

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return routplnlisst.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return routplnlisst.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View v = arg1;  
	           CompleteListViewHolder viewHolder;  
	           if (v == null) {  
	                LayoutInflater li = (LayoutInflater) context  
	                          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	                v = li.inflate(R.layout.routeplanlist, null);  
	                viewHolder = new CompleteListViewHolder(v);  
	                v.setTag(viewHolder);  
	           } else {  
	                viewHolder = (CompleteListViewHolder) v.getTag();  
	           }  
	           viewHolder.mTVItem.setText(routplnlisst.get(arg0));
	           
	        	   viewHolder.mTVItem.setBackgroundColor(Color.parseColor("#ffffff"));
	        	  
	           return v;  
	      }  
		
		 class CompleteListViewHolder {  
		      public TextView mTVItem;  
		      public CompleteListViewHolder(View base) {  
		           mTVItem = (TextView) base.findViewById(R.id.busview);  
		      }  
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

	public void onBackSd(View v)
	{
		switch(v.getId())
		{
		case R.id.Back:
			
			onBackPressed();//default back button on mobile
			//startActivity(goback);
		}
	}
	
	public class LoadData extends AsyncTask<Void,Void,Void>{
		ProgressDialog dialog=new ProgressDialog(source_destination.this);
		Boolean direct=false;
		String buslistv="";
		
		 int count;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Log.e("async","onpreexecute");
		dialog.setTitle("Searching Route");
		dialog.setMessage(source+" to "+dest);
		dialog.setCancelable(false);
		dialog.show();
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				breakpoint=0;
				colorflag=1;
				Cursor	 c=null;
				if(busType.equals("All"))
				{
					String	sql="select bus_no from bus_table where route_no in(select  route_no from bus_route br where '"+source+"' in " + 
							"(select stop1 from bus_route where route_no=br.route_no) and '"+dest+"' in (select stop1 from bus_route where route_no=br.route_no) )";
						 c=db1.rawQuery(sql,null);//fetching values based on source destination 
				}
				else
				{
					String	sql="select bus_no from bus_table where bus_type='AC Bus' and route_no in(select  route_no from bus_route br where '"+source+"' in " + 
							"(select stop1 from bus_route where route_no=br.route_no) and '"+dest+"' in (select stop1 from bus_route where route_no=br.route_no) )";
						 c=db1.rawQuery(sql,null);
				}
					//if(c.isNull(c.getColumnIndex("bus_no"))
					
				
					
					//if(c!=null)
					//{
						//if direct bus is available then
						if(c.moveToFirst())
						{
							  busList=new ArrayList<String>();
							direct=true;
							//routeTitle.setText("Route Plan 1 ");//
							buslistv="   DIRECT BUS     >> \n\n";
							 buslistv+=" 1. "+ source+" to "+dest+"\n";
							 buslistv+="Bus No. ";
							int i=0;
							do{
								i++;
							busList.add(c.getString(c.getColumnIndex("bus_no")));
								String busno=c.getString(c.getColumnIndex("bus_no"));
							buslistv+=busno+" , ";
							
							if(i>=5)
								{
								buslistv+="\n";
								buslistv+="  ";
								i=0;
								}
						
							}while(c.moveToNext());
							//routplnlst.add(buslistv);//adding route details with  bus number to list
							//countbus=c.getCount();
							//buslistNo.setText(buslistv);
							//adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,routplnlst); 
				
			
			
						}
					else//when no direct bus available then looking for break journey
					{
					 	adapt = null;
					 	Cursor c2=null;
						String qry="Drop Table if exists routeno_src ";
						db1.execSQL(qry);
						 qry="Drop Table if exists routeno_dest ";
						db1.execSQL(qry);
						
						String nul=null;
						if(busType.equals("All"))
						{
							String routeno_src_table="create  table routeno_src as select distinct route_no from bus_route br1" +
						
								" where '"+source+"' in (select stop1 from bus_route where route_no=br1.route_no)";
						db1.execSQL(routeno_src_table);
						
						String routeno_dest_table="create  table routeno_dest as select distinct route_no from bus_route br2" +
								" where '"+dest+"' in (select stop1 from bus_route where route_no=br2.route_no)";
						db1.execSQL(routeno_dest_table);
						
						//fetching the intersection bus stop for break journey
						
						
						}
						else
						{
							String routeno_src_table="create  table routeno_src as select distinct route_no from bus_route br1" +
									
								" where '"+source+"' in (select stop1 from bus_route br,bus_table bt where bus_type='AC Bus'  and br.route_no=bt.route_no and br.route_no=br1.route_no )";
						db1.execSQL(routeno_src_table);
						String routeno_dest_table="create  table routeno_dest as select distinct route_no from bus_route br2" +
								" where '"+dest+"' in (select stop1 from bus_route br,bus_table bt where bus_type='AC Bus' and br.route_no=bt.route_no and br.route_no=br2.route_no)";
						db1.execSQL(routeno_dest_table);
						}
						
						String intersect_stop="select stop1 from bus_route br1,routeno_src rsrc where br1.route_no=rsrc.route_no and stop1 <> '"+nul+"' "+ 
								"intersect " + " select stop1 from bus_route br2,routeno_dest rdest where br2.route_no=rdest.route_no and stop1 <> '"+nul+"'";
								
								
							 	
								String busnos_root1=" ";
								
								 c2=db1.rawQuery(intersect_stop,null);
						//int i=c2.getCount();
						//breakstop[]=new String[i];//to store the breakStop
						
						int i=0;
						 count=1;
						//Log.d("lenght",String.valueOf(i));
					 
						if(c2.moveToFirst())
							{
								do{
									 busList=new ArrayList<String>();
									//busList.clear();
									buslistv="";
									buslistv+="\tROUTE PLAN "+count+"    "+">>"+"\n\n";
									
									String destin=c2.getString(c2.getColumnIndex("stop1"));//intersection stop
									Log.e(""+count,destin);
									
									
									
									buslistv+="1 . "+ source+" to "+destin+"\n";
									//bus number from to intrsection point
									//String busql="select bus_no from bus_table where route_no = (select distinct rsrc.route_no from routeno_src rsrc,routeno_dest rdest "
									//		+ " where exists ( select stop1 from bus_route where route_no=rsrc.route_no " + 
									//	" intersect select stop1 from bus_route where route_no=rdest.route_no))";
									String	busql;
									if(busType.equals("All"))
										{
											busql="select bus_no from bus_table where route_no in(select  route_no from bus_route br where '"+source+"' in " + 
										
									"(select stop1 from bus_route where route_no=br.route_no) and '"+destin+"' in (select stop1 from bus_route where route_no=br.route_no) )";
										}
									else
									{
											busql="select bus_no from bus_table where bus_type='AC Bus' and route_no in(select  route_no from bus_route br where '"+source+"' in " + 
												
									"(select stop1 from bus_route where route_no=br.route_no) and '"+destin+"' in (select stop1 from bus_route where route_no=br.route_no) )";
									}
									
									Cursor c3=db1.rawQuery(busql,null);
										if(c3.moveToFirst())//getting bus no from source to intersection point
										{
											i=0;
											buslistv+="Bus No. : ";
											do{
												i++;
												//busList.add(c3.getString(c3.getColumnIndex("bus_no")));
												String busno=c3.getString(c3.getColumnIndex("bus_no"));//adding busno to textbox of list
												busList.add(busno);//to store the busNumber for buslist
												buslistv+=busno+",";
												if(i>=5)
												{
													buslistv+="\n";
													buslistv+="  ";
													i=0;
												}
											}while(c3.moveToNext());
										}
										buslistv+="\n";
										//quey ffrom intersection
										//bus number from  inetrsection point to ultimate destination
										buslistv+="2 ."+"  "+destin+" to "+dest+"\n";
										// busql="select bus_no from bus_table where route_no = (select distinct rdest.route_no from routeno_src rsrc,routeno_dest rdest "
										//	+ "where exists ( select stop1 from bus_route where route_no=rsrc.route_no " + 
										//" intersect select stop1 from bus_route where route_no=rdest.route_no))";
										if(busType.equals("All"))
												{
											busql="select bus_no from bus_table where route_no in(select  route_no from bus_route br where '"+destin+"' in " + 
										"(select stop1 from bus_route where route_no=br.route_no) and '"+dest+"' in (select stop1 from bus_route where route_no=br.route_no) )"; 
												}
										else
										{
											busql="select bus_no from bus_table where bus_type='AC Bus' and route_no in(select  route_no from bus_route br where '"+destin+"' in " + 
													"(select stop1 from bus_route where route_no=br.route_no) and '"+dest+"' in (select stop1 from bus_route where route_no=br.route_no) )"; 
										}
												
										Cursor c4=db1.rawQuery(busql,null);
										
										if(c4.moveToFirst())//getting bus no from intersection point destination point
										{
											i=0;
											buslistv+="Bus No. : ";
											do{
												i++;
												//busList.add(c4.getString(c4.getColumnIndex("bus_no")));
												String busno=c4.getString(c4.getColumnIndex("bus_no"));
												busList.add(busno);//to store the busNumber for buslist
												buslistv+=busno+",";
												if(i>=5)
												{
													buslistv+="\n";
													buslistv+="  ";
													i=0;
												}
											}while(c4.moveToNext());
										}
										buslistv+="\n";
										Log.e("buslistv",buslistv);
										
										busListBreak.add(busList);//listoflist for buslist
										count++;//Route Plan next
										//Log.e("BreakLit",busListBreak.get(0));
										 routplnlst.add(buslistv);//adding route details with  bus number to list
										
										
										runOnUiThread(new Runnable() {//for updating ui in background

									         @Override
									             public void run() {
									        	
									        	
									        	
									        	dialog.setTitle("Found route "+(count-1));
		   	 								
									         }
									        });

								}while(c2.moveToNext());//while there exists intersection point
								//Log.e(""+count,destin);
															}
						else
						{
							breakpoint=1;//no break journey
							//routplnlst.add(null);	
								
							
						}
						
						
					
					}
						
					
						

				
				
			}catch(Exception e){
				e.printStackTrace();
			System.out.println(e);	
			}
			Log.e("Return ","beforenull");
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//Log.e("Result",result);
			if(breakpoint==1)
				{
				adapt=new MyAdapter(getApplicationContext(),routplnlst);
	        	 lv.setAdapter(adapt);
	        	 
	        	
	        	adapt.notifyDataSetChanged();
				 
				dialog.dismiss();
				Toast.makeText(getApplicationContext(),"No bus available",Toast.LENGTH_LONG).show();
				sql="Drop Table routeno_src";
				db1.execSQL(sql);
				Log.e("table","no-error");
				sql="Drop Table routeno_dest";
				db1.execSQL(sql);
				}
			else if(direct){
				routplnlst.add(buslistv);
				 adapt=new MyAdapter(getApplicationContext(),routplnlst);
					lv.setAdapter(adapt);
					adapt.notifyDataSetChanged();
				//if user clicks the RouteNo of direect bus
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						Intent buslistpage=new Intent(getApplicationContext(),Buslist.class);
						buslistpage.putExtra("data",(busList));
						startActivity(buslistpage);
					}
				});
				
				}
			else
			{
				//routplnlst.add(buslistv);
				adapt=new MyAdapter(getApplicationContext(),routplnlst);
       		 Log.e("notify","nulladapt");
       	 lv.setAdapter(adapt);
       	 adapt.notifyDataSetChanged();
				//else if the user clicks the list of indirect bus ie. break journey
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						//busList.clear();
						//adapt.notifyDataSetChanged();
						
						
						Intent buslistpage=new Intent(getApplicationContext(),Buslist.class);
						buslistpage.putExtra("data",busListBreak.get(arg2));
						startActivity(buslistpage);
					}
				});
							
							
							String qry="Drop Table if exists routeno_src ";
							db1.execSQL(qry);
							 qry="Drop Table if exists routeno_dest ";
							db1.execSQL(qry);
							
							
			}
			if(dialog.isShowing())
				dialog.dismiss();
		}

		

		
	}

}
