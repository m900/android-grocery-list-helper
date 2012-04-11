package com.luisromero.listmenu;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener, OnKeyListener , OnItemClickListener{
    /** Called after the splash-screen */    
    EditText txtItem;
	Button btnAdd;
	ListView listItems;
	ArrayList<Item> products;
	DbHelper dbHelper;
	SQLiteDatabase db;
	
	ArrayList<String> toDoItems;
	ArrayAdapter<String> aa;
	Item product;
	Item item;
	
	String item_location;
	String item_name;
	ArrayList<String> locations;
	
	int posItem=0;
    View viewItem;
    boolean IsSomeItemChecked=false;
    
    
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
        
        txtItem = (EditText) findViewById(R.id.txtItem);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        listItems= (ListView) findViewById(R.id.listItems);
        
        btnAdd.setOnClickListener(this);
        txtItem.setOnClickListener(this);
        dbHelper = new DbHelper(this);
        db=dbHelper.getWritableDatabase();
        
        toDoItems = this.getAllProducts();
        locations=new ArrayList<String>();
        products=new ArrayList<Item>(); // add item objects to an array list for easier location mapping
        
        aa= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,toDoItems);
        listItems.setAdapter(aa);
        listItems.setOnItemClickListener(this);
    }
    
    @Override //to create option menu from xml file
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater= getMenuInflater();
    	inflater.inflate(R.menu.listmenu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId()==R.id.item1){
    		//TO-DO// Sends user to a map where a store location is showed- item can be bought there.
    		Intent intent=new Intent(MainActivity.this,ListItemMapActivity.class);
    		//intent.putExtra("productName", (String)listItems.getItemAtPosition(getPosItem()));
    		intent.putExtra("storeLocation", this.item_location);
    		startActivity(intent);
    		
    	}else if(item.getItemId()==R.id.item2){
    		//Log.d("Option","Edit item was clicked");
    		//startActivity(new Intent(MainActivity.this,AddListItemActivity.class));
    		String productName = (String)listItems.getItemAtPosition(getPosItem());
    		String quantity="";
    		Intent intent=new Intent(MainActivity.this,EditListItemActivity.class);
			intent.putExtra("productName",productName);
			intent.putExtra("productQuantity", quantity);	
			startActivityForResult(intent, 2);
    	
    	}else if(item.getItemId()==R.id.item3){
    		//Log.d("Option","Delete item was clicked");
    		//startActivity(new Intent(MainActivity.this,EditListItemActivity.class));
    		if(getIsSomeItemChecked()==true){
    			setIsSomeItemChecked(false);//restart
    			CheckedTextView restartView = (CheckedTextView)viewItem;
    			restartView.setChecked(false);
    			deleteItem(getPosItem());
    			setPosItem(0);//restart item selected for deletion
    		}else{
    			Toast.makeText(getApplicationContext(),"Select an item to delete",Toast.LENGTH_SHORT).show();
    		}
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

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	viewItem=view;
		CheckedTextView ListTextView = (CheckedTextView)view;
		/*If some item is checked others cannot be checked
		 * And only the one checked can be unchecked.
		 * */
		if(getIsSomeItemChecked()==false){
			ListTextView.setChecked(true);
			setPosItem(position);
			setViewItem(view);
			setIsSomeItemChecked(true);
		}else if(getIsSomeItemChecked()==true && position==getPosItem()){
			ListTextView.setChecked(false);
			setIsSomeItemChecked(false);
		}
	}
    
    private void addItem(String item){
    	if(item.length()>0){
    		this.toDoItems.add(item);
    		this.aa.notifyDataSetChanged(); //Array adapter notify
    		this.txtItem.setText("");
    	}
    }
    
    private void deleteItem(int itemId){
    	if(itemId >=0){
    		//For toast message delete
    		String itemName = (String)listItems.getItemAtPosition(itemId);
    		
    		this.toDoItems.remove(itemId);
    		//this.locations.remove(getPosItem()-1);
    		this.deleteProductFromDB(itemName);
    		aa.notifyDataSetChanged();
    		this.txtItem.setText("");
    	}
    }

    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if(event.getAction()==KeyEvent.ACTION_DOWN & keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
			this.addItem(this.txtItem.getText().toString());
		}
		return false;
	}

    public void onClick(View v) {
		if(v==this.btnAdd){
			String productName;
			productName=this.txtItem.getText().toString();
			Intent intent=new Intent(MainActivity.this,EditListItemActivity.class);
			intent.putExtra("productName",productName);
			intent.putExtra("productQuantity","");
			startActivityForResult(intent, 1);
		}
	}
    
    @Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {     
    	 if (resultCode == RESULT_OK && requestCode==1) { 
    		 if(data.hasExtra("producName")){
    			 String productName=data.getExtras().getString("producName");
    			 int productQuantity=Integer.parseInt(data.getExtras().getString("productQuantity"));
    			 item_location=data.getExtras().getString("productLocation");
    			 locations.add(item_location);
    			 
    			 this.addItem(productName);
    			 this.insertProductToDB(productName, productQuantity, item_location);
    		 }
         }else if(resultCode == RESULT_OK && requestCode==2){
        	 if(data.hasExtra("productName") && data.hasExtra("productQuantity")){
        		 String productName=data.getExtras().getString("producName");
    			 int productQuantity=Integer.parseInt(data.getExtras().getString("productQuantity"));
        		 this.updateProductToDB(productName,productQuantity);
        	 }
        	 
         }
    }
    
    private int updateProductToDB(String product, int quantity) {
		db=dbHelper.getWritableDatabase();
		String[] products={product};
		ContentValues content = new ContentValues();
		content.put(DbHelper.PRODUCT,product);
		content.put(DbHelper.QUANTITY,quantity);
		return db.update("items", content,DbHelper.PRODUCT+"=?",products);
	}

	public void insertProductToDB(String product, int quantity, String location){
		
		db = dbHelper.getWritableDatabase();
		
		ContentValues content = new ContentValues();
		content.put(DbHelper.PRODUCT,product);
		content.put(DbHelper.QUANTITY,quantity);
		content.put(DbHelper.LOCATION,location);
		//content.put(DbHelper.LOCATION_NAME,store);
		db.insert("items", DbHelper.PRODUCT, content);
		db.close();
	}
    
    public void deleteProductFromDB(String product){
		db = dbHelper.getWritableDatabase();
		String[] products={product};
		db.delete("items",DbHelper.PRODUCT+"=?",products);
		db.close();
    }
    
    public ArrayList<String> getAllProducts(){
    	toDoItems=new ArrayList<String>();
    	String query="SELECT * FROM items";
    	db=dbHelper.getWritableDatabase();
    	Cursor cursor=db.rawQuery(query,null);
    	if(cursor.moveToFirst()){
    		do{
    			item=new Item();
    			item.setId(Integer.parseInt(cursor.getString(0)));
    			item.setProduct(cursor.getString(1));
    			item.setQuantity(Integer.parseInt(cursor.getString(2)));
    			item.setLocation(cursor.getString(3));
    			products.add(item); //adding items also to the ArrayList of items
    			toDoItems.add(cursor.getString(1));
    		}while(cursor.moveToNext());	
    	}
    	return toDoItems;
    }
    
}