package com.mypackage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;

public class FlashActivity extends Activity {
	ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flash_main);
		new MyTask().execute();
	}
	
	public class MyTask extends AsyncTask<String,String, Boolean>
	{
		@Override
		protected void onPreExecute() {
			pd=new ProgressDialog(FlashActivity.this);
			pd.setMessage("Checking Connectivity...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
			// TODO Auto-generated method stub
		}
		@Override
		protected Boolean doInBackground(String... arg0) {
			ConnectivityManager manager=
					(ConnectivityManager)getSystemService(
							CONNECTIVITY_SERVICE);
			if(manager.getNetworkInfo(0).getState()==android.net.NetworkInfo.State.CONNECTED
					||manager.getNetworkInfo(0).getState()==android.net.NetworkInfo.State.CONNECTING
					||manager.getNetworkInfo(1).getState()==android.net.NetworkInfo.State.CONNECTED
					||manager.getNetworkInfo(1).getState()==android.net.NetworkInfo.State.CONNECTING){
				return true;
			}
			else{
				return false;
			}
		
			// TODO Auto-generated method stub
		}
		@Override
		protected void onPostExecute(Boolean result) {
			pd.dismiss();
			// TODO Auto-generated method stub
			Intent intn=new Intent(FlashActivity.this,WeatherappActivity.class);
			intn.putExtra("isInternetAvailable",result);
			startActivity(intn);
		}
	}
}