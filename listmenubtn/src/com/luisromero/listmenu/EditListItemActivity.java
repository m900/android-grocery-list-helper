package com.luisromero.listmenu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditListItemActivity extends Activity implements OnClickListener, OnKeyListener{
	Button btnSaveEdit;
	Button btnCancel;
	EditText txtProductName;
	private String productName;
	private Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_list_item);
		
		btnSaveEdit = (Button) findViewById(R.id.btnSaveProductEdit);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		txtProductName=(EditText) findViewById(R.id.txtProductName);
		
		btnCancel.setOnClickListener(this);
		btnSaveEdit.setOnClickListener(this);
		
		bundle=getIntent().getExtras();
		productName=(String)bundle.get("productName");
		txtProductName.setText(productName);
	}

	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}

	public void onClick(View v) {
		if(v==this.btnSaveEdit){
			finish();//return to previous activity with information.
		}else if(v==this.btnCancel){
			EditListItemActivity.this.finish();
			System.exit(0);
		}
	}
	
	@Override
	public void finish() {
		// Prepare data intent 
		this.productName=txtProductName.getText().toString();
		Intent data = new Intent();
		data.putExtra("producName", this.productName);
		setResult(RESULT_OK,data);
		super.finish();
	}
	
}
