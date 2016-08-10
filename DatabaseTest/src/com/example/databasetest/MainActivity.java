package com.example.databasetest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private MyDatabaseHelper dbHelper;

	Button createDatabase;
	Button addData;
	Button updateData;
	Button deleteButton;
	Button queryButton;
	Button replaceData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);

		createDatabase 	= (Button) findViewById(R.id.create_database);
		addData 		= (Button) findViewById(R.id.add_data);
		updateData 		= (Button) findViewById(R.id.update_data);
		deleteButton 	= (Button) findViewById(R.id.delete_data);
		queryButton 	= (Button) findViewById(R.id.query_data);
		replaceData 	= (Button) findViewById(R.id.replace_data);

		createDatabase.setOnClickListener(this);
		addData.setOnClickListener(this);
		updateData.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		queryButton.setOnClickListener(this);
		replaceData.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// 创建数据库
		if (v == createDatabase) {
			dbHelper.getWritableDatabase();
		} 
		// 插入数据
		else if (v == addData) {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put("name"	, "The Da Vinci Code");
			values.put("author"	, "Dan Brown");
			values.put("pages"	, 454);
			values.put("price"	, 16.96);
			
			db.insert("Book"	, null, values);
			
			values.clear();
			values.put("name"	, "The Lost Symbol");
			values.put("author"	, "Dan Brown");
			values.put("pages"	, 510);
			values.put("price"	, 19.95);
			
			db.insert("Book", null, values);
			
		} 
		// 更新
		else if (v == updateData){
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("price", 10.99);
			db.update("Book", values, "name = ?",new String[] { "The Da Vinci Code" });
		} 
		// 删除
		else if (v == deleteButton){
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			db.delete("Book", "pages > ?", new String[] { "500" });
		} 
		// 查询
		else if (v == queryButton){
			SQLiteDatabase db 	= dbHelper.getWritableDatabase();
			Cursor cursor 		= db.query("Book", null, null, null, null, null,null);
			if (cursor.moveToFirst()) {
				do {
					String name 	= cursor.getString(cursor.getColumnIndex("name"));
					String author 	= cursor.getString(cursor.getColumnIndex("author"));
					int pages 		= cursor.getInt(cursor.getColumnIndex("pages"));
					double price 	= cursor.getDouble(cursor.getColumnIndex("price"));
					Log.d("MainActivity", "book name is " + name);
					Log.d("MainActivity", "book author is " + author);
					Log.d("MainActivity", "book pages is " + pages);
					Log.d("MainActivity", "book price is " + price);
				} while (cursor.moveToNext());
			}
			cursor.close();
		} 
		// 
		else if (v == replaceData){

			SQLiteDatabase db = dbHelper.getWritableDatabase();
			db.beginTransaction();
			try {
				db.delete("Book", null, null);
				// if (true) {
				// throw new NullPointerException();
				// }
				ContentValues values = new ContentValues();
				values.put("name", "Game of Thrones");
				values.put("author", "George Martin");
				values.put("pages", 720);
				values.put("price", 20.85);
				db.insert("Book", null, values);
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
		
		}

	}

}
