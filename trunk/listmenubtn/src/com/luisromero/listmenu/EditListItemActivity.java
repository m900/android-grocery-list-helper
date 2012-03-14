package com.luisromero.listmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EditListItemActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_list_item);
		
		Button btnEdit = (Button) findViewById(R.id.btnEdit);
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditListItemActivity.this.finish();
				System.exit(0);
			}
		});
		
		btnEdit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Nothing yet	
			}
		});
	}
	
	
	
	
}
