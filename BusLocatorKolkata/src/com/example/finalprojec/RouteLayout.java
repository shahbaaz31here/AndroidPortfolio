package com.example.finalprojec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class RouteLayout extends Activity{

	TextView routeview,from,to,bus_no;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routelayout);
		
		
		routeview=(TextView)findViewById(R.id.routview);
		routeview.setMovementMethod(new ScrollingMovementMethod());
		from=(TextView)findViewById(R.id.fromval);
		to=(TextView)findViewById(R.id.toval);
		bus_no=(TextView)findViewById(R.id.busnoval);
		Intent i=getIntent();
		String outstr=i.getStringExtra("outstr");
		String fro=i.getStringExtra("fro");
		String too=i.getStringExtra("too");
		String busno=i.getStringExtra("busno");
		from.setText(fro);
		to.setText(too);
		bus_no.setText(busno);
		routeview.setText(outstr);
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
	/*public void onBackR(View v)
	{
		switch(v.getId())
		{
		//case R.id.BackRoute:
			finish();
			//Intent goback=new Intent(getApplicationContext(),source_destination.class);
			onBackPressed();//default back button on mobile
			//startActivity(goback);
		}
	}*/
	
}
