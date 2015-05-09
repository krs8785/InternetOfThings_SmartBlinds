package edu.rit.csci759.mobile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class RulesActivity extends Activity{
	
	static EditText et_server_url;
	Button btn_viewRules;
	Button btn_addButton;
	Button btn_saveAll;
	TextView allRules;
	ListView allRulesList;
	ArrayAdapter rulesArrayAdapter;
	ArrayList<String> ruleList = new ArrayList<String>();
	static String whoClickedMe = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("hello mike","in there "); 
		
		setContentView(R.layout.rule_list_page);
		allRules = (TextView) findViewById(R.id.allRules);
		
		
		//et_server_url = (EditText) findViewById(R.id.et_serverURL);
		
		OnClickListener buttonListenerView = new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	whoClickedMe = "getRules";		    	
		    	new SendJSONRequest().execute();
		    	btn_saveAll.setEnabled(true);
		    	btn_addButton.setEnabled(true);
		    }
		};
		btn_viewRules = (Button) findViewById(R.id.btn_viewRules);
		btn_viewRules.setOnClickListener(buttonListenerView);
		
		OnClickListener buttonListenerSave = new View.OnClickListener() {
		    @Override
		   
		    public void onClick(View v) {
		    	 whoClickedMe = "setRules";
		    	 Log.d("karan","karan");
		    	 new SendJSONRequest().execute();
		    	 Log.d("karan1","karan1");
		    	 Log.d("karan3", whoClickedMe);
		    }
		};

		btn_saveAll = (Button) findViewById(R.id.btn_saveAll);
		btn_saveAll.setEnabled(false);
		btn_saveAll.setOnClickListener(buttonListenerSave);
		
		OnClickListener buttonListenerAdd = new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent intent = new Intent(RulesActivity.this,EditActivity.class);
		    	String addRule = "Rule 1: IF temperature IS NA NA IF ambient IS NA THEN blind IS NA";
		    	String[] t = addRule.split(" ");
		    	intent.putExtra("pos", "AddButtonClicked");
		    	intent.putExtra("populate", t);
				startActivityForResult(intent,1);
		    }
		};
		btn_addButton = (Button) findViewById(R.id.btn_addButton);
		btn_addButton.setEnabled(false);
		btn_addButton.setOnClickListener(buttonListenerAdd);
		
		
		allRulesList = (ListView) findViewById(R.id.listView_allRulesList);
		rulesArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, ruleList);
		allRulesList.setAdapter(rulesArrayAdapter);
		
		allRulesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			
				int itemPosition = position;
				String listItem = (String)allRulesList.getItemAtPosition(itemPosition);
				Log.d("itemmmm",listItem);
				String[] item = listItem.split(" ");
				Log.d("list of rules",listItem);
				
				
				
				Intent intent = new Intent(RulesActivity.this,EditActivity.class);
				intent.putExtra("pos", ""+itemPosition);
				intent.putExtra("populate", item);
				startActivityForResult(intent,1);
		    	
			}
		});		
		
	}
	
	protected void onActivityResult(int reqCode, int resultCode, Intent intent){
		
		if(reqCode ==1 ){
			if(resultCode == RESULT_OK){
				
				int pos=0;
				if(null != intent.getStringExtra("pos") ){
					pos = Integer.parseInt(intent.getStringExtra("pos"));
				}
					List<String> getSpinnerValues = new ArrayList<String>();
					getSpinnerValues.add(0,intent.getStringExtra("tempVal"));
					getSpinnerValues.add(1,intent.getStringExtra("ambiVal"));
					getSpinnerValues.add(2,intent.getStringExtra("relationVal"));
					getSpinnerValues.add(3,intent.getStringExtra("blindVal"));
					allRules.setText(getSpinnerValues.toString());	

					if(pos != 0){
						ruleList.remove(pos);
						ruleList.add("Rule "+(pos+1)+": "+"IF Temperature IS "+ getSpinnerValues.get(0)+" "+getSpinnerValues.get(2)+" IF Ambient IS "+getSpinnerValues.get(1)
								+" Then Blind Is "+getSpinnerValues.get(3));
					}else{
						ruleList.add("Rule "+(ruleList.size()+1)+": "+"IF Temperature IS "+ getSpinnerValues.get(0)+" "+getSpinnerValues.get(2)+" IF Ambient IS "+getSpinnerValues.get(1)
								+" Then Blind Is "+getSpinnerValues.get(3));
						allRulesList.setAdapter(rulesArrayAdapter);
					}
				
				Collections.sort(ruleList);	    
				
			}
		}
		
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
	
	public void onBackPresses(){
		moveTaskToBack(true);
		RulesActivity.this.finish();
	}

	class SendJSONRequest extends AsyncTask<Void, String, Object> {
		Object response_txt;

		HashMap<String,Object> myFinalList = new HashMap<String, Object>();
		
		public SendJSONRequest(){
			System.out.println("***I am constructore***");
		}
		
		@Override
		protected void onPreExecute() {
			if(whoClickedMe.equals("setRules")){
				for(String item : ruleList){
					String[] tempArray = item.split(" ");
					ArrayList<String> temp_finalValue = new ArrayList<String>();
					temp_finalValue.add(0, tempArray[5]);
					temp_finalValue.add(1, tempArray[10]);
					temp_finalValue.add(2, tempArray[6]);
					temp_finalValue.add(3, tempArray[14]);			
					
					myFinalList.put(""+tempArray[1].charAt(0), temp_finalValue);
				}
			}
		}
		
		@Override
		protected Object doInBackground(Void... params) {
			//String serverURL_text = et_server_url.getText().toString();
			//String request_method = btn_viewRules.getText().toString();
			String request_method = whoClickedMe;
			
			
			System.out.println("**********JSON HANDLER**********8");
			response_txt = JSONHandler.testJSONRequest("10.10.10.125:8080", request_method, myFinalList);
			
			//response_txt = response_txt + params;
			System.out.println(response_txt);
			
			return response_txt;
		}
		
		protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }

	     protected void onPostExecute(Object result) {
	    	 
	    	 if(whoClickedMe.equals("getRules")){
		    	 Log.d("hi i am here",result.toString());
		    	 Log.d("debug", result.toString());
		    	 Log.d("debug", response_txt.toString());
		    	 HashMap<String, List<String>> resultMap = (HashMap<String, List<String>>) result;
		    	 Log.d("MAP","####MAP IS####"+resultMap.toString());
		    	 allRules.setText(resultMap.toString());
		    	 
		    	 Iterator itr = resultMap.entrySet().iterator();
		    	 
		    	 while(itr.hasNext()){
		    		 HashMap.Entry  pair = (HashMap.Entry)itr.next();
		    		 List<String> temp = (List<String>)pair.getValue();
		    		 ruleList.add("Rule "+pair.getKey()+": "+"IF Temperature IS "+ temp.get(0)+" "+temp.get(2)+" IF Ambient IS "+temp.get(1)
		    				 +" Then Blind Is "+temp.get(3));
		    		 Collections.sort(ruleList);	    		 
		    	 }
	    	 }
	    	 
	     }		
	}	
}
