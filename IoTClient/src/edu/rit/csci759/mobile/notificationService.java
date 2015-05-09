package edu.rit.csci759.mobile;

import java.util.ArrayList;
import java.util.HashMap;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class notificationService extends android.app.Service {

	private boolean isRunning = false;
	protected final static String broadcastMsg = "BroadcastAction";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		isRunning = true;

	}

	@Override
	public void onDestroy() {
		isRunning = false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startID) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (isRunning) {
					try {
						Thread.sleep(3000);
						new SendJSONRequest().execute();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

		return Service.START_STICKY;
	}

	class SendJSONRequest extends AsyncTask<Void, String, Object> {
		Object response_txt;

		public SendJSONRequest() {
			System.out.println("***I am constructore***");
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Object doInBackground(Void... params) {
			// String serverURL_text = et_server_url.getText().toString();
			// String request_method = et_requst_method.getText().toString();
			String request_method = "getSensor";

			System.out.println("**********JSON HANDLER**********8");
			response_txt = JSONHandler.testJSONRequest("10.10.10.125:8080",
					request_method, null);
			System.out.println(response_txt);

			return response_txt;
		}

		protected void onProgressUpdate(Integer... progress) {
			// setProgressPercent(progress[0]);
		}

		protected void onPostExecute(Object result) {
			HashMap<String, String> resultMap = (HashMap<String, String>) result;
			ArrayList<String> value = new ArrayList<String>();
			
			value.add(0, resultMap.get("date"));
			value.add(1, resultMap.get("temp"));
			
			Intent myInt = new Intent();
			myInt.setAction(broadcastMsg);
			myInt.putStringArrayListExtra("values", value);
			sendBroadcast(myInt);
		}

	}

}
