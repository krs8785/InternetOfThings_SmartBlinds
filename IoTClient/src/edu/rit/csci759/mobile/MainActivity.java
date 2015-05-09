package edu.rit.csci759.mobile;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button btn_send_request;
	static EditText et_server_url;
	static EditText et_requst_method;
	TextView tv_response;
	TextView temp;
	TextView ambi;
	Button btn_getRules;
	Button btn_changeTempDiff;
	int oldTemp = 0;
	notificationReceiver tempReceiver;
	ListView notificationList;
	ArrayAdapter nArrayAdapter;
	ArrayList<String> tempList = new ArrayList<String>();
	EditText temperatureDiff;
	int difference = 2;
	
	private class notificationReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			ArrayList<String> value =  intent.getStringArrayListExtra("values");
			//difference = Integer.parseInt(temperatureDiff.getText().toString());
			checkTemp(value);
		}
	}
	
	protected void checkTemp (ArrayList<String> resultValue) {
		String tempValue = resultValue.get(1);
		Log.d("BR Temp :: ","test : "+tempValue);
		int newTemp = Integer.parseInt(tempValue);
		
		if (Math.abs((newTemp - oldTemp)) >= difference) {
			this.oldTemp = newTemp;
			String notificationString = resultValue.get(0)+", "+resultValue.get(1);
			Log.d("BR1 Temp :: ","test : "+tempValue);
			tempList.add(notificationString);
			notificationList.setAdapter(nArrayAdapter);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		et_server_url = (EditText) findViewById(R.id.et_serverURL);
		//tv_response = (TextView) findViewById(R.id.tv_response);
		
		temp = (TextView) findViewById(R.id.temp);
		ambi = (TextView) findViewById(R.id.ambi);
		
		temperatureDiff = (EditText) findViewById(R.id.temperature_diff);
		
		notificationList = (ListView) findViewById(R.id.main_listView);
		nArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, tempList);
		notificationList.setAdapter(nArrayAdapter);
		
		OnClickListener buttonListener = new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	
		    	new SendJSONRequest().execute();
		    	
		    	tempReceiver = new notificationReceiver();
		    	IntentFilter intentFilter = new IntentFilter();
		    	intentFilter.addAction(notificationService.broadcastMsg);
		    	registerReceiver(tempReceiver, intentFilter);
		    	
		    	Intent intent = new Intent(MainActivity.this,notificationService.class);
		    	startService(intent);
		    	
		    }
		};
		btn_send_request = (Button) findViewById(R.id.btn_sendRequest);
		btn_send_request.setOnClickListener(buttonListener);
		
		
		//final Context con = this;
		OnClickListener buttonListenerRules = new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	//setContentView(R.layout.rule_list_page);		  	
		    	Intent intent = new Intent(MainActivity.this,RulesActivity.class);
		    	startActivity(intent);
		    }
		};
		
		btn_getRules = (Button) findViewById(R.id.btn_getRules);
		btn_getRules.setOnClickListener(buttonListenerRules);
		
		OnClickListener buttonListenerChangeTempDiff = new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	difference = Integer.parseInt(temperatureDiff.getText().toString());
		    }
		};
		
		btn_changeTempDiff = (Button) findViewById(R.id.btn_changeTempDiff);
		btn_changeTempDiff.setOnClickListener(buttonListenerChangeTempDiff);
		
	}
	
	protected void onStop() {
		if(null !=tempReceiver){
			unregisterReceiver(tempReceiver);
		}	
		Intent intent = new Intent(MainActivity.this,notificationService.class);
    	stopService(intent);
		super.onStop();
	}
	
	protected void onResume() {
		super.onResume();
		
	}

	public void onBackPresses(){
		moveTaskToBack(true);
		MainActivity.this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	class SendJSONRequest extends AsyncTask<Void, String, Object> {
		Object response_txt;
		
		public SendJSONRequest(){
			System.out.println("***I am constructore***");
		}
		
		
		
		@Override
		protected void onPreExecute() {
		}
		
		@Override
		protected Object doInBackground(Void... params) {
			String serverURL_text = et_server_url.getText().toString();
			//String request_method = et_requst_method.getText().toString();
			String request_method = "getSensor";
			
			System.out.println("**********JSON HANDLER**********8");
			response_txt = JSONHandler.testJSONRequest(serverURL_text, request_method, null);
			System.out.println(response_txt);
			
			return response_txt;
		}
		
		protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }

	     protected void onPostExecute(Object result) {
	    	 Log.v("hi i am here",result.toString());
	    	 Log.d("debug", result.toString());
	    	 Log.v("debug", response_txt.toString());
			HashMap<String, String> resultMap = (HashMap<String, String>) result;
	    	 
	    	//tv_response.setText(resultMap.get("date"));
	    	temp.setText(resultMap.get("temp"));
	    	ambi.setText(resultMap.get("ambi"));
	     }
		
	}
	
}



