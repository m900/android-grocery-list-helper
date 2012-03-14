package com.luisromero.listmenu;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AddListItemActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_list_item);
		
		Button btnCancel=(Button) findViewById(R.id.btnCancel);
		Button btnAdd=(Button) findViewById(R.id.btnAdd);
		
        btnCancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// Close the activity and goes back to the caller activity
				AddListItemActivity.this.finish();
				System.exit(0);
			}
		});
        
        btnAdd.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
			}
		});
	}
}
