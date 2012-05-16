package com.luisromero.listmenu;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DbHelper - creates the database(s) needed 
 * @author Luis Romero
 * @param SQLite db - creates tables to insert data on database.
 *
 **/

public class DbHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME= "list";
	private static final int DATABASE_SCHEMA=1;
	public static final String ID="_id";
	public static final String PRODUCT="product";
	public static final String LOCATION="location";
	public static final String QUANTITY="quantity";
	public static final String C_LAT="c_lat";
	public static final String C_LON="c_lon";
	public static final String IN_STOCK="in_stock"; //0 for FALSE , 1 for TRUE
	private ArrayList<Item> items;
	private ArrayList<String> toDoItems;
	private SQLiteDatabase db;
	
	Item product; // Item object - product in the list
	
	String item_location;
	String item_name;
	String item_quantity;
	
	public DbHelper(Context context){
		super(context,DATABASE_NAME,null,DATABASE_SCHEMA);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS items (_id INTEGER PRIMARY KEY AUTOINCREMENT, product VARCHAR(35), quantity INTEGER, location VARCHAR(35));");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS items");
		this.onCreate(db);
	}
	
	public ArrayList<Item> getAllItems(){
		return this.items;
	}
	
	public ArrayList<Item> getSelectedItems(ArrayList<String> selectedItems){
		ArrayList<Item> selected=new ArrayList<Item>();
		
		for(int i=0;i<selectedItems.size();i++){
			if(getItem(selectedItems.get(i))!=null){
				selected.add(getItem(selectedItems.get(i)));
			}
		}
		return selected;
	}
	
	public Item getItem(String name){
		Item item=new Item();
		for(int i=0;i<items.size();i++){
			if (items.get(i).getProduct().equals(name)){
				item=items.get(i);
				break;
			}
		}
		return item;
	}
	
	public ArrayList<String> getAllProducts(){
    	this.items=new ArrayList<Item>();
    	this.toDoItems=new ArrayList<String>();
    	this.db=this.getReadableDatabase();
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
	
}
