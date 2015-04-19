package com.example.finalprojec;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class More extends Activity {
Button visit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		visit=(Button) findViewById(R.id.visit);
	}
		public void onClickMore(View view){
			switch(view.getId()){
			case R.id.visit:
				Intent m=new Intent(getApplicationContext(),Visit.class);
				startActivity(m);
				
				break;
			case R.id.feedBack:
				Intent f=new Intent(this,Feedback.class);
				 startActivity(f);

				break;
			case R.id.aboutus:
				Intent au=new Intent(this,About.class);
				 startActivity(au);
				break;

			}
}@Override
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