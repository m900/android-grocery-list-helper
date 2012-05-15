package com.luisromero.listmenu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MapSelectedActivity extends Activity implements OnClickListener, OnKeyListener , OnItemClickListener {
	ArrayList<String> toDoItems;
	ArrayAdapter<String> aa;
	ArrayList<Item> items;
	ListView listItems;
	DbHelper dbHelper;
	Button btnMapItems;
	
	ArrayList<String> mapItems;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_selected_items);
		Bundle bundle=getIntent().getExtras();
		toDoItems=bundle.getStringArrayList("toDoItems");
		Log.d("listItems", toDoItems.toString());
		
		listItems= (ListView) findViewById(R.id.mapListView); 
		
		btnMapItems = (Button) findViewById(R.id.btnMapItems);
		btnMapItems.setOnClickListener(this);
        
		dbHelper = new DbHelper(this);
		toDoItems = dbHelper.getAllProducts();
		items=dbHelper.getAllItems();
		
        aa= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,toDoItems);
        listItems.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
       // View footer=getLayoutInflater().inflate(R.layout.map_selected_items,null);
        //View header=null;
        //listItems.addFooterView(footer);
        listItems.setAdapter(aa);
        listItems.setOnItemClickListener(this);
      
	}
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		// TODO Auto-generated method stub
		CheckedTextView ListTextView = (CheckedTextView)view;
		
		if(ListTextView.isChecked()){
			ListTextView.toggle();
		}
		
	}
	public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==this.btnMapItems){
			//intent for listmapactivity with the items selected
			mapItems=new ArrayList<String>();
			int length=listItems.getCount();
			SparseBooleanArray checked = listItems.getCheckedItemPositions();
			for (int i=0;i<length;i++){
				if(checked.get(i)){
					mapItems.add(toDoItems.get(i));
				}
			}
			
			Log.d("items",Integer.toString(length) + " | " + mapItems.toString());
			Intent intent=new Intent(MapSelectedActivity.this,ListItemMapActivity.class);
			intent.putStringArrayListExtra("mapItems", mapItems);
			startActivity(intent);
		}
		
	}	
}
