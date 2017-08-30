package com.mypackage;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherappActivity extends Activity {
    /** Called when the activity is first created. */
	ProgressDialog pd;
	EditText et;
	TextView tv2,tv3,tv4,tv6,tv7,tv8;
	ImageView iv;
	DataBaseManager dbm;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	setContentView(R.layout.main);
	super.onCreate(savedInstanceState);
	tv2=(TextView)findViewById(R.id.textView2);
    tv3=(TextView)findViewById(R.id.textView3);
    tv4=(TextView)findViewById(R.id.textView4);
    tv6=(TextView)findViewById(R.id.textView6);
    tv7=(TextView)findViewById(R.id.textView7);
    tv8=(TextView)findViewById(R.id.textView8);
    et=(EditText)findViewById(R.id.editText1);
    iv=(ImageView)findViewById(R.id.imageView1);
    dbm=new DataBaseManager(WeatherappActivity.this);
    Intent intn=getIntent();
    Bundle b=intn.getExtras();
    boolean b1=b.getBoolean("isInternetAvailable");
    if(b1)
    {
    new MyTask().execute("Kanpur");
    }
    else
    {
    	/*SQLiteDatabase db=openOrCreateDatabase("weather.db",SQLiteDatabase.CREATE_IF_NECESSARY, null);
    	Cursor c=db.query("weatherTable", null,null,null,null,null,null);
    	c.moveToNext();
    	String lu=c.getString(0);
    	String cty=c.getString(1);
    	String r=c.getString(2);
    	String cn=c.getString(3);
    	String tc=c.getString(4);
    	String text=c.getString(5);
    	tv2.setText(cty);
    	
		tv3.setText(r);
		tv4.setText(cn);
		tv6.setText(tc);
		tv7.setText(text);
		tv8.setText(lu);
    	iv.setImageResource(R.drawable.ic_launcher); */ 
    	Userdata u =dbm.getData();
    	tv2.setText(u.getCty());
    	tv3.setText(u.getR());
    	tv4.setText(u.getCn());
    	tv6.setText(u.getTemp());
    	tv7.setText(u.getText());
    	tv8.setText(u.getLu());
    	iv.setImageBitmap(u.getBp()); 
    	
    }
}

	public void setvalue(View arg)
	{
		String a=et.getText().toString();
		et.setText("");
		new MyTask().execute(a);
	}

    public class MyTask extends AsyncTask<String,Void,Void>
    {
    	@Override
    	protected void onPreExecute() {
    		pd=new ProgressDialog(WeatherappActivity.this);
    		pd.setMessage("Please Wait...");
    		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		pd.setCancelable(false);
    		pd.show();
    		//super.onPreExecute();
    	}
    	String cty,r,cn,lu,tc,text;
    	Drawable d;
    	@Override
    	protected Void doInBackground(String... arg0) {
    		try
    		{
    		URL url=new URL("http://api.apixu.com/v1/current.json?key=f1afde3c53264b3cb3271733170707&q="+arg0[0]);
    		InputStream in=url.openStream();
    		DataInputStream din=new DataInputStream(in);
    		String res="",s="";
    		while((s=din.readLine())!=null)
    			res+=s;
    		JSONObject jsonObject=new JSONObject(res);
    		JSONObject jsonObject2=jsonObject.getJSONObject("location");
    		 cty=jsonObject2.getString("name");
    		 r=jsonObject2.getString("region");
    		 cn=jsonObject2.getString("country");
    		JSONObject jsonObject3=jsonObject.getJSONObject("current");
    		 lu=jsonObject3.getString("last_updated");
    		 tc=jsonObject3.getString("temp_c");
    		JSONObject jsonObject4=jsonObject3.getJSONObject("condition");
    		text=jsonObject4.getString("text");
    		String path=jsonObject4.getString("icon");
    		URL u=new URL("http:"+path);
    		d=Drawable.createFromStream((InputStream)u.getContent(),"Image Not Available");
    		BitmapDrawable bd=(BitmapDrawable)d;
    		Bitmap bp=bd.getBitmap();
    		//SQLiteDatabase db=openOrCreateDatabase("weather.db",SQLiteDatabase.CREATE_IF_NECESSARY, null);
    		SharedPreferences prefs=getSharedPreferences("FirstTime",MODE_APPEND);
    		boolean b=prefs.getBoolean("isFirstTime",true);
    		if(b)
    		{
    	/*	db.execSQL("create table if not exists weatherTable(last_updated varchar(100),city varchar(100),region varchar(100),country varchar(100),temp varchar(100),txt varchar(100));");
    		db.execSQL("insert into weatherTable values('"+lu+"','"+cty+"','"+r+"','"+cn+"','"+tc+"','"+text+"')");*/
    		
    		dbm.insertData(lu,cty,r,cn,tc,text,bp);
    		SharedPreferences.Editor et=prefs.edit();
    		et.putBoolean("isFirstTime",false);
    		et.commit();
    		}
    		else
    		{
    			dbm.updateData(lu,cty,r,cn,tc,text,bp);
    		//db.execSQL("update weatherTable set last_updated='"+lu+"',city='"+cty+"',region='"+r+"',country='"+cn+"',temp='"+tc+"',txt='"+text+"'");
    		}
    		}
    		catch(Exception e)
    		{
    			Log.d("Error==>",e+"");
    		}

    		// TODO Auto-generated method stub
    		return null;
    	}
    	@Override
    	protected void onPostExecute(Void result) {
    		pd.dismiss();
    		tv2.setText(cty);
    		tv3.setText(r);
    		tv4.setText(cn);
    		tv6.setText(tc);
    		tv7.setText(text);
    		tv8.setText(lu);
    		iv.setImageDrawable(d);
    		// TODO Auto-generated method stub
    		//super.onPostExecute(result);
    	}
    }
}