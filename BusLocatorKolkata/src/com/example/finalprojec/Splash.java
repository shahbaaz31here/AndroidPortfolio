package com.example.finalprojec;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Splash extends Activity {
	Button exit,blink;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		//getActionBar().setTitle("Blink");
		exit=(Button)findViewById(R.id.exit);
		blink=(Button)findViewById(R.id.blink_page);
		
	}
		public void onClick(View view){
			
			switch(view.getId()){
			case R.id.blink_page:
				Intent b=new Intent(getApplicationContext(),MainActivity.class);
				startActivity(b);
				
				break;
			case R.id.exit:
				finish();
				
				break;
		 
			}

		}
}