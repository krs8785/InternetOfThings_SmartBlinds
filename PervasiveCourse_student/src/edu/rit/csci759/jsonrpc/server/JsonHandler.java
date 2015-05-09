package edu.rit.csci759.jsonrpc.server;

/**
 * Demonstration of the JSON-RPC 2.0 Server framework usage. The request
 * handlers are implemented as static nested classes for convenience, but in 
 * real life applications may be defined as regular classes within their old 
 * source files.
 *
 * @author Vladimir Dzhuvinov
 * @version 2011-03-05
 */

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.server.MessageContext;
import com.thetransactioncompany.jsonrpc2.server.RequestHandler;

import edu.rit.csci759.fuzzylogic.MyBlindClass;
import edu.rit.csci759.rspi.RpiFuzzyIndicatorImpl;

public class JsonHandler {

	// Implements a handler for an "echo" JSON-RPC method
	public static class EchoHandler implements RequestHandler {

		// Reports the method names of the handled requests
		public String[] handledRequests() {

			return new String[] { "echo" };
		}

		// Processes the requests
		public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctx) {

			if (req.getMethod().equals("echo")) {

				// Echo first parameter

				List params = (List) req.getParams();

				Object input = params.get(0);

				return new JSONRPC2Response(input, req.getID());
			} else {

				// Method name not supported

				return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND,
						req.getID());
			}
		}
	}

	// Implements a handler for "getDate" and "getTime" JSON-RPC methods
	// that return the current date and time
	public static class DateTimeHandler implements RequestHandler {

		private RpiFuzzyIndicatorImpl fuzzyImpl;

		public DateTimeHandler(RpiFuzzyIndicatorImpl _fuzzyImpl) {
			this.fuzzyImpl = _fuzzyImpl;
		}

		// Reports the method names of the handled requests
		public String[] handledRequests() {
			// System.out.println("test");
			// return new String[]{"getDate", "getTime"};
			// Temperature Sensing - Project
			return new String[] { "getDate", "getTime", "getTemp", "getSensor",
					"getRules" , "setRules"};
		}

		// Processes the requests
		@SuppressWarnings("deprecation")
		public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctx) {

			String hostname = "unknown";
			try {
				hostname = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			if (req.getMethod().equals("getDate")) {

				DateFormat df = DateFormat.getDateInstance();

				String date = df.format(new Date());

				return new JSONRPC2Response(hostname + " " + date, req.getID());

			} else if (req.getMethod().equals("getTime")) {
				System.out.println("####geting time rules####");
				DateFormat df = DateFormat.getTimeInstance();

				String time = df.format(new Date());

				return new JSONRPC2Response(hostname + " " + time, req.getID());
			} else if (req.getMethod().equals("getRules")) {

				System.out.println("####geting fuzzy rules####");
				HashMap<String, List<String>> rulesList = null;
				MyBlindClass myBlindClass = new MyBlindClass();
				rulesList = myBlindClass.getRules();
				// System.out.println(rulesList.toString());
				return new JSONRPC2Response(rulesList, req.getID());
			} else if (req.getMethod().equals("getSensor")) {
				HashMap<String, String> blindOutput = null;
				try {
					// System.out.println("smart sense");
					// RpiFuzzyIndicatorImpl fuzzyImpl = new
					// RpiFuzzyIndicatorImpl();
					blindOutput = this.fuzzyImpl.smartSense();
					// blindValue = String.valueOf(blindOutput);
					// System.out.println("req ID::"+req.getID());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return new JSONRPC2Response(blindOutput, req.getID());

			} else if (req.getMethod().equals("setRules")) {

				System.out.println("SETTING THE NEW RULES");
				//System.out.println(req.getParams());
				HashMap<String, List<String>> rulesList = (HashMap<String, List<String>>)req.getParams();
				MyBlindClass myBlindClass = new MyBlindClass();
				myBlindClass.setRules(rulesList);
				// System.out.println(rulesList.toString());
				return new JSONRPC2Response("KARAN SHAH", req.getID());
			} else {

				// Method name not supported

				return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND,
						req.getID());
			}
		}
	}
}
