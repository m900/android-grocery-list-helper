package com.luisromero.listmenu;


import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;


import android.widget.ListView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	protected String[] myList = new String[] {"Onion","Garlic","Cilantro","Milk"}; 
    private int posItem=0;
    private View viewItem;
    private boolean IsSomeItemChecked=false;
    
    
    public void setIsSomeItemChecked(boolean checked){
    	this.IsSomeItemChecked=checked;
    }
    
    public boolean getIsSomeItemChecked(){
    	return IsSomeItemChecked;
    }
    
    /*Sets the position of the item checked*/
    public void setPosItem(int position){
    	this.posItem=position;
    }
    
    /*Sets the view in the item checked*/
    public void setViewItem(View view){
    	this.viewItem=view;
    }
    
    /*Gets the position in the item checked*/
    public int getPosItem(){
    	return posItem;
    }
    
    /*Gets the view in the item checked*/
    public View getViewItem(){
    	return viewItem;
    }
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView lv = new ListView(this);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,myList));
        setContentView(lv);
        lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				setPosItem(position);
				setViewItem(view);
				
				CheckedTextView ListTextView = (CheckedTextView)view;
				/*If some item is checked others cannot be checked*/
				if(getIsSomeItemChecked()){
					ListTextView.setChecked(!ListTextView.isChecked());
				}
				
			}
		});
    }
    
    @Override //to create option menu from xml file
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater= getMenuInflater();
    	inflater.inflate(R.menu.listmenu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	if(item.getItemId()==R.id.item1){
    		//Log.d("Option","Add item was clicked");
    
    	
    		startActivity(new Intent(MainActivity.this,AddListItemActivity.class));
    	}else if(item.getItemId()==R.id.item2){
    		Log.d("Option","Edit item was clicked");
    	}else if(item.getItemId()==R.id.item3){
    		Log.d("Option","Delete item was clicked");
    	}else if(item.getItemId()==R.id.item4){
    		//Log.d("Option","Close App was clicked");
    		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    		builder.setMessage("Are you sure you want to exit?");
    		builder.setCancelable(false);
    		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// This closes the Main activity- and goes to android-dashboard
					
					MainActivity.this.finish();
		            System.exit(0);
				}
			});
    		
    		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
    		
    		AlertDialog alert=builder.create();//create the alert with the information
    		alert.show();//to show the dialog before closing
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    
}