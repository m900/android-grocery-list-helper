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
	
	DbHelper dbHelper;
	SQLiteDatabase db;
	
	ArrayList<String> toDoItems;
	ArrayAdapter<String> aa;
	ArrayList<Item> items; //list of Item objects
	Item product; // Item object - product in the list
	
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
        dbHelper = new DbHelper(this); // not sure if this is a good idea/ it runs multiple times or something..
        db=dbHelper.getWritableDatabase();
        toDoItems = this.getAllProducts();
        locations=new ArrayList<String>();
        //items=new ArrayList<Item>(); // add item objects to an array list for easier location mapping
        
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
    		intent.putExtra("storeLocation", this.item_location);
    		startActivity(intent);
    		
    	}else if(item.getItemId()==R.id.item2){
    		//Log.d("Option","Edit item was clicked");
    		//startActivity(new Intent(MainActivity.this,AddListItemActivity.class));
    		
    		//Toast.makeText(getApplicationContext(),"Size of items list is: "+ items.size(), Toast.LENGTH_SHORT).show();
    		Item selectedItem =items.get(getPosItem());
    		String productName=selectedItem.getProduct();
    		int productQuantity=selectedItem.getQuantity();
    		Intent intent=new Intent(MainActivity.this,EditListItemActivity.class);
			intent.putExtra("productName",productName);
			intent.putExtra("productQuantity", Integer.toString(productQuantity));	
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
			Item current=items.get(getPosItem());
			
			Toast.makeText(getApplicationContext(),"position is: "+ getPosItem() +" _id:"+current.getId()+" name:"+current.getProduct(), Toast.LENGTH_SHORT).show();
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
    
    private void updateProductNameOnList(String listItem){
    	if(listItem.length()>0){
    		toDoItems.set(getPosItem(), listItem);
    		this.product=this.items.get(getPosItem());
    		this.product.setProduct(listItem);
    		this.items.set(getPosItem(), this.product);
    		this.aa.notifyDataSetChanged();
    	}
    }
    
    private void deleteItem(int itemId){
    	if(itemId >=0){
    		String itemName = (String)listItems.getItemAtPosition(itemId);
    		this.toDoItems.remove(itemId);
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
    		 if(data.hasExtra("productName")){
    			 String productName=data.getExtras().getString("productName");
    			 int productQuantity=Integer.parseInt(data.getExtras().getString("productQuantity"));
    			 item_location=data.getExtras().getString("productLocation");
    			 locations.add(item_location);//LOCATION
    			 this.addItem(productName);
    			 this.insertProductToDB(productName, productQuantity, item_location);
    		 }
         }
    	 /* To edit list entries */
    	 if(resultCode == RESULT_OK && requestCode==2){
        	 if(data.hasExtra("productName")){
        		 this.product=items.get(getPosItem());
        		 int id=product.getId();
        		 String productName=data.getExtras().getString("productName");
    			 int productQuantity=Integer.parseInt(data.getExtras().getString("productQuantity"));
    			 String productStore=data.getExtras().getString("productStore");
        		 this.updateProductToDB(id,productName,productQuantity,productStore);
        		 this.updateProductNameOnList(productName);
        	     Toast.makeText(getApplicationContext(), "values changed: "+ productName +" "+ productQuantity+" "+ productStore, Toast.LENGTH_SHORT).show();
        	 }
         }
    }
    
    private void updateProductToDB(int id, String product, int quantity, String location) {
		db=dbHelper.getWritableDatabase();
		String[] _id={Integer.toString(id)};
		ContentValues content = new ContentValues();
		content.put(DbHelper.PRODUCT,product);
		content.put(DbHelper.QUANTITY,quantity);
		content.put(DbHelper.LOCATION,location);
		db.update("items", content,DbHelper.ID+"=?",_id);
		db.close();
	}

	public void insertProductToDB(String product, int quantity, String location){
		db = dbHelper.getWritableDatabase();
		ContentValues content = new ContentValues();
		content.put(DbHelper.PRODUCT,product);
		content.put(DbHelper.QUANTITY,quantity);
		content.put(DbHelper.LOCATION,location);
		//content.put(DbHelper.LOCATION_NAME,store);
		db.insert("items",DbHelper.PRODUCT,content);
		db.close();
		//First add it to DB - get the new info on the DB and added to the Items object list
		this.product=getNewDbEntry();
		this.items.add(this.product);
	}
    
    public void deleteProductFromDB(String product){
		db = dbHelper.getWritableDatabase();
		String[] products={product};
		db.delete("items",DbHelper.PRODUCT+"=?",products);
		db.close();
    }
    
    public ArrayList<String> getAllProducts(){
    	this.items=new ArrayList<Item>();
    	this.toDoItems=new ArrayList<String>();
    	this.db=dbHelper.getReadableDatabase();
    	String query="SELECT * FROM items";
    	Cursor cursor=db.rawQuery(query,null);
    	int id=0;
    	int quantity=0;
    	if(cursor.moveToFirst()){
    		do{
    			this.product=new Item();
    			id=Integer.parseInt(cursor.getString(0));
    		    quantity=Integer.parseInt(cursor.getString(2));
    			this.product.setId(id);
    			this.product.setProduct(cursor.getString(1));
    			this.product.setQuantity(quantity);
    			this.product.setLocation(cursor.getString(3));
    			this.items.add(this.product); //adding items also to the ArrayList of items
    			toDoItems.add(cursor.getString(1));
    		}while(cursor.moveToNext());	
    	}
    	cursor.close();
    	db.close();
    	return toDoItems;
    }
    
    /* This gets the lates DB Items table entry - to get the id and other 
     * 
     * information and update it on the Item object List
     * 
     */
    public Item getNewDbEntry(){
    	this.product=new Item();
    	this.db=dbHelper.getReadableDatabase();
    	String query="SELECT * FROM items";
    	Cursor cursor=db.rawQuery(query,null);
    	if(cursor.moveToLast()){
    		this.product.setId(Integer.parseInt(cursor.getString(0)));
    		this.product.setProduct(cursor.getString(1));
    		this.product.setQuantity(Integer.parseInt(cursor.getString(2)));
    		this.product.setLocation(cursor.getString(3));
    	}
    	cursor.close();
    	db.close();
    	return this.product;
    }
    
}