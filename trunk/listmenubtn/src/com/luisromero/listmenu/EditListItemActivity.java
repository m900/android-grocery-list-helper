package com.luisromero.listmenu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditListItemActivity extends Activity implements OnClickListener, OnKeyListener{
	Button btnSaveEdit;
	Button btnCancel;
	EditText txtProductName;
	EditText txtProductQuantity;
	
	Spinner spinnerStores;
	ArrayAdapter<CharSequence> spinnerAdapter;
	private String productName;
	private String productQuantity;
	private Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_list_item);
		
		btnSaveEdit = (Button) findViewById(R.id.btnSaveProductEdit);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		txtProductName=(EditText) findViewById(R.id.txtProductName);
		txtProductQuantity=(EditText) findViewById(R.id.txtProductQuantity);
		spinnerStores=(Spinner) findViewById(R.id.ddLocation);
		
		spinnerAdapter = ArrayAdapter.createFromResource(
	            this, R.array.storeNames, android.R.layout.simple_spinner_item);
	    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinnerStores.setAdapter(spinnerAdapter);
		
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
		this.productQuantity=txtProductQuantity.getText().toString().trim();
		Intent data = new Intent();
		data.putExtra("producName", this.productName);
		data.putExtra("productQuantity", this.productQuantity);
		setResult(RESULT_OK,data);
		super.finish();
	}
	
}
