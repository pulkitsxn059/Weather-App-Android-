package com.mypackage;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class DataBaseManager extends SQLiteOpenHelper {
	
	public DataBaseManager(Context ctx) {
		super(ctx,"weather.db", null, 1);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL("create table if not exists weatherTable(last_updated varchar(100),city varchar(100),region varchar(100),country varchar(100),temp varchar(100),txt varchar(100),descimg blob)");
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void insertData(String lu,String cty,String r,String cn,String temp,String text,Bitmap bp)
	{
		SQLiteDatabase db=getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put("last_updated",lu);
		cv.put("city",cty);
		cv.put("region",r);
		cv.put("country",cn);
		cv.put("temp",temp);
		cv.put("txt",text);
		ByteArrayOutputStream bout=new ByteArrayOutputStream();
		bp.compress(CompressFormat.JPEG, 40, bout);
		cv.put("descimg",bout.toByteArray());
		db.insert("weatherTable",null,cv);
		db.close();
	}
	
	public void updateData(String lu,String cty,String r,String cn,String temp,String text,Bitmap bp)
	{
		SQLiteDatabase db=getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put("last_updated",lu);
		cv.put("city",cty);
		cv.put("region",r);
		cv.put("country",cn);
		cv.put("temp",temp);
		cv.put("txt",text);
		ByteArrayOutputStream bout=new ByteArrayOutputStream();
		bp.compress(CompressFormat.JPEG, 40, bout);
		cv.put("descimg",bout.toByteArray());
		db.update("weatherTable",cv,null,null);
		db.close();
	}
	
	public Userdata getData()
	{
		Userdata ud=new Userdata();
		SQLiteDatabase db=getReadableDatabase();
		Cursor c=db.query("weatherTable",null,null,null,null,null,null);
		c.moveToNext();
		String lu=c.getString(0);
		String cty=c.getString(1);
		String r=c.getString(2);
		String cn=c.getString(3);
		String temp=c.getString(4);
		String text=c.getString(5);
		byte b[]=c.getBlob(6);
		Bitmap bp=BitmapFactory.decodeByteArray(b,0,b.length);
		ud.setLu(lu);
		ud.setCty(cty);
		ud.setR(r);
		ud.setCn(cn);
		ud.setTemp(temp);
		ud.setText(text);
		ud.setBp(bp);
		return ud;
	}
}
