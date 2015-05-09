package edu.rit.csci759.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class EditActivity extends Activity {

	TextView item1;
	Spinner spinner_temp;
	Spinner spinner_ambi;
	Spinner spinner_relation;
	Spinner spinner_blind;
	Button btn_modify;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_rule_page);
	
	
		spinner_temp = (Spinner)findViewById(R.id.spinner_temp);
		spinner_ambi = (Spinner)findViewById(R.id.spinner_ambi);
		spinner_relation = (Spinner)findViewById(R.id.spinner_relation);
		spinner_blind = (Spinner)findViewById(R.id.spinner_blind);
		
		ArrayAdapter<CharSequence> adapter_temp = ArrayAdapter.createFromResource(EditActivity.this, R.array.temp_values, android.R.layout.simple_spinner_item);
		adapter_temp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_temp.setAdapter(adapter_temp);
		
		ArrayAdapter<CharSequence> adapter_ambi = ArrayAdapter.createFromResource(EditActivity.this, R.array.ambi_values, android.R.layout.simple_spinner_item);
		adapter_ambi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_ambi.setAdapter(adapter_ambi);
		
		ArrayAdapter<CharSequence> adapter_relation = ArrayAdapter.createFromResource(EditActivity.this, R.array.relation_values, android.R.layout.simple_spinner_item);
		adapter_relation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_relation.setAdapter(adapter_relation);
		
		ArrayAdapter<CharSequence> adapter_blind = ArrayAdapter.createFromResource(EditActivity.this, R.array.blind_values, android.R.layout.simple_spinner_item);
		adapter_blind.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_blind.setAdapter(adapter_blind);
		
		
		Intent _intent = getIntent();
		final String pos = _intent.getStringExtra("pos");
		String[] temp_populate = _intent.getStringArrayExtra("populate");
		
		
		if(temp_populate[5].equals("warm")){
			spinner_temp.setSelection(2);
		}else if(temp_populate[5].equals("hot")){
			spinner_temp.setSelection(1);
		}else if(temp_populate[5].equals("cold")){
			spinner_temp.setSelection(3);
		}else if(temp_populate[5].equals("comfort")){
			spinner_temp.setSelection(4);
		}else if(temp_populate[5].equals("freezing")){
			spinner_temp.setSelection(5);
		}else if(temp_populate[5].equals("NA")){
			spinner_temp.setSelection(0);
		}
		
		if(temp_populate[10].equals("NA")){
			spinner_ambi.setSelection(0);
		}else if(temp_populate[10].equals("bright")){
			spinner_ambi.setSelection(1);
		}else if(temp_populate[10].equals("dim")){
			spinner_ambi.setSelection(2);
		}else if(temp_populate[10].equals("dark")){
			spinner_ambi.setSelection(3);
		}
		
		if(temp_populate[6].equals("NA")){
			spinner_relation.setSelection(0);
		}else if(temp_populate[6].equals("AND")){
			spinner_relation.setSelection(1);
		}else if(temp_populate[6].equals("OR")){
			spinner_relation.setSelection(2);
		}
		
		if(temp_populate[14].equals("close")){
			spinner_ambi.setSelection(0);
		}else if(temp_populate[14].equals("open")){
			spinner_ambi.setSelection(1);
		}else if(temp_populate[14].equals("half")){
			spinner_ambi.setSelection(2);
		}
		
		
		OnClickListener buttonListenerRules = new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Intent intent = new Intent(EditActivity.this,RulesActivity.class);
				intent.putExtra("tempVal", spinner_temp.getSelectedItem().toString());
				intent.putExtra("ambiVal", spinner_ambi.getSelectedItem().toString());
				intent.putExtra("relationVal", spinner_relation.getSelectedItem().toString());
				intent.putExtra("blindVal", spinner_blind.getSelectedItem().toString());
				
				
				
				if(!pos.equals("AddButtonClicked")){
					intent.putExtra("pos", pos);
				}
		    	setResult(RESULT_OK, intent);
		    	finish();
		    }
		};
		
		btn_modify = (Button) findViewById(R.id.btn_modify);
		btn_modify.setOnClickListener(buttonListenerRules);
		
	}
	
	protected void onStop() {	
		Intent intent = new Intent(EditActivity.this,notificationService.class);
    	stopService(intent);
		super.onStop();
	}
	
	protected void onResume() {
		super.onResume();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onBackPresses(){
		moveTaskToBack(true);
		EditActivity.this.finish();
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
	
}
