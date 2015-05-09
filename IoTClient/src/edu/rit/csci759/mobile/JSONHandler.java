package edu.rit.csci759.mobile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

public class JSONHandler {

	public static Object testJSONRequest(String server_URL_text, String method, HashMap<String,Object>  finalMap){
		// Creating a new session to a JSON-RPC 2.0 web service at a specified URL
		
		Log.d("i am here", method);
		Log.d("Debug serverURL", server_URL_text);
		
		// The JSON-RPC 2.0 server URL
		URL serverURL = null;

		try {
			serverURL = new URL("http://"+server_URL_text);
			System.out.println("----------Yes we have reached here--------");

		} catch (MalformedURLException e) {
		// handle exception...
		}

		// Create new JSON-RPC 2.0 client session
		JSONRPC2Session mySession = new JSONRPC2Session(serverURL);


		// Once the client session object is created, you can use to send a series
		// of JSON-RPC 2.0 requests and notifications to it.

		// Sending an example "getTime" request:
		// Construct new request
		System.out.println("**********Getting neew request");
		int requestID = 0;
		HashMap<String,Object> params1 = finalMap;
		JSONRPC2Request request = new JSONRPC2Request(method, params1 ,requestID);
		
		Log.d("i am here 2",method);
		// Send request
		JSONRPC2Response response = null;

		try {
			System.out.println("****sending....");
			Log.d("i am here 3",request.toString());
			response = mySession.send(request);
			Log.d("I am here got the result",response.toString());

		} catch (JSONRPC2SessionException e) {

		Log.e("error", e.getMessage().toString());
		// handle exception...
		}

		// Print response result / error
		if (response.indicatesSuccess())
			Log.d("debug", response.getResult().toString());
		else
			Log.e("error", response.getError().getMessage().toString());
		
	
		return response.getResult();
	}
	
}
