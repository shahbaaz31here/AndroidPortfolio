package com.example.finalprojec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class About extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus);
		
	}
	
	public void onBack(View v)
	{
		switch(v.getId())
		{
		case R.id.BackAbout:
			finish();
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
