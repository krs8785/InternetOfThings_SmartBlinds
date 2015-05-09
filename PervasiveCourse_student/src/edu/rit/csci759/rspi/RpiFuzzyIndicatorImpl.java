package edu.rit.csci759.rspi;

import java.security.AllPermission;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import edu.rit.csci759.fuzzylogic.MyBlindClass;
import edu.rit.csci759.rspi.utils.MCP3008ADCReader;

public class RpiFuzzyIndicatorImpl implements RpiIndicatorInterface {
	
	private final static GpioController gpio = GpioFactory.getInstance();
	private final static boolean DEBUG = false;
	
	// Blind open - high
	private final static GpioPinDigitalOutput greenPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "green", PinState.LOW);
	
	// Blind half - mid
	private final static GpioPinDigitalOutput yellowPin  = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "yellow",  PinState.LOW);
	
	// Blind close - low
	private final static GpioPinDigitalOutput redPin   = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "red",   PinState.LOW);
		
	static boolean checkThread = true;
	static boolean keepRunning = true;
	
	public RpiFuzzyIndicatorImpl(){
		MCP3008ADCReader.initSPI(gpio);
	}
	
	public void gpioShutdown(){
		gpio.shutdown();
	}
	
	@Override
	public void led_all_off() {
		//System.out.println("HI I am in LED OFF");
		redPin.low();
		greenPin.low();
		yellowPin.low();
	}

	@Override
	public void led_all_on() {
		redPin.high();
		greenPin.high();
		yellowPin.high();
	}

	@Override
	public void led_error(int blink_count) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void led_when_low() {
		// TODO Auto-generated method stub
		redPin.high();
	}

	@Override
	public void led_when_mid() {
		// TODO Auto-generated method stub
		yellowPin.high();
	}

	@Override
	public void led_when_high() {
		// TODO Auto-generated method stub
		greenPin.high();
	}

	
	
	private class BlindThread extends Thread {
		
		public void run() {	
			
			MyBlindClass fuzzyLogic = new MyBlindClass();
			try {		
				int oldTemp=0,oldAmbi=0;
				while(keepRunning){		
					Thread.sleep(5000);
					int newTemp = read_temperature();
					int newAmbi = read_ambient_light_intensity();					
					
					//System.out.println("temp"+newTemp);
				    //System.out.println("ambi"+newAmbi);
					
					if (newTemp != oldTemp || newAmbi != oldAmbi) {
						//MyBlindClass fuzzyLogic = new MyBlindClass();
						String outputNew = fuzzyLogic.calculateFuzzy(newTemp, newAmbi);
						//System.out.println("fuzzyyy"+outputNew);
						
						led_all_off();
						if (outputNew.equals("close")) {	
							//System.out.println("I am in RED LIGHT");
							led_when_low();
						} else if (outputNew.equals("half") ) {
							//System.out.println("I am in Yello LIGHT");
							led_when_mid();
						} else {
							//System.out.println("I am in GREEN LIGHT");
							led_when_high();
						}
						
						oldTemp = newTemp;
						oldAmbi = newAmbi;
					}
				}				
			
			}
			
			catch(Exception e){
				System.out.println("try catch excp");
			}
			finally {
				System.out.println("swirtching off all leds");				
				led_all_off();
				gpioShutdown();
			}
		}
	}
	
	
	public void startSensing () {
		try {
			this.led_all_on();
			Thread.sleep(2000);
			this.led_all_off();
			Thread.sleep(2000);
			new BlindThread().start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<String,String> smartSense () throws InterruptedException {
		
		int temp = this.read_temperature();
		int ambi = this.read_ambient_light_intensity();
		
		
		//System.out.println("temp"+temp);
		//System.out.println("ambi"+ambi);
		

		
		DateFormat df = DateFormat.getTimeInstance();

		String time = df.format(new Date());
		
		HashMap<String, String> myFinalList = new HashMap<String,String>();
		myFinalList.put("date", time);
		myFinalList.put("temp", String.valueOf(temp));
		myFinalList.put("ambi", String.valueOf(ambi));
		//myFinalList.put("blind", String.valueOf(outputNew));
		
		//this.led_all_on();
		//Thread.sleep(2000);
		//this.led_all_off();
		//Thread.sleep(2000);
		
	
		
		//Thread.sleep(3000);
		//this.led_all_off();		
		//this.gpioShutdown();
		
		//System.out.println(myFinalList);
		return myFinalList;
	}	
	
	
	@Override
	
	public synchronized int read_ambient_light_intensity() {

		//System.out.println("Entering in getAmbient method");

		/*
		 * Reading ambient light from the photocell sensor using the MCP3008 ADC
		 */
		int adc_ambient = MCP3008ADCReader
				.readAdc(MCP3008ADCReader.MCP3008_input_channels.CH1.ch());
		// [0, 1023] ~ [0x0000, 0x03FF] ~ [0&0, 0&1111111111]
		// convert in the range of 1-100
		int ambient = (int) (adc_ambient / 10.24);

		if (DEBUG) {
/*			System.out.println("readAdc:"
					+ Integer.toString(adc_ambient)
					+ " (0x"
					+ MCP3008ADCReader.lpad(Integer.toString(adc_ambient, 16)
							.toUpperCase(), "0", 2)
					+ ", 0&"
					+ MCP3008ADCReader.lpad(Integer.toString(adc_ambient, 2),
							"0", 8) + ")");
			System.out.println("Ambient:" + ambient + "/100 (" + adc_ambient
					+ "/1024)");*/
		}
		//System.out.println("ambient :::"+ambient);
		return ambient;
		}

	@Override
	public synchronized int read_temperature() {

		//System.out.println("Entering in getTemp method");
		//MCP3008ADCReader.initSPI(gpio);		
		
		/*
		 * Reading temperature from the TMP36 sensor using the MCP3008 ADC
		 */
		int adc_temperature = MCP3008ADCReader
				.readAdc(MCP3008ADCReader.MCP3008_input_channels.CH0.ch());
		// [0, 1023] ~ [0x0000, 0x03FF] ~ [0&0, 0&1111111111]
		// convert in the range of 1-100
		int temperature = (int) (adc_temperature / 10.24);

		if (DEBUG) {
			/*System.out
					.println("readAdc:"
							+ Integer.toString(adc_temperature)
							+ " (0x"
							+ MCP3008ADCReader.lpad(
									Integer.toString(adc_temperature, 16)
											.toUpperCase(), "0", 2)
							+ ", 0&"
							+ MCP3008ADCReader.lpad(
									Integer.toString(adc_temperature, 2), "0",
									8) + ")");
			System.out.println("Temperature:" + temperature + "/100 ("
					+ adc_temperature + "/1024)");
*/		}

		float tmp36_mVolts = (float) (adc_temperature * (3300.0 / 1024.0));
		// 10 mv per degree
		float temp_C = (float) (((tmp36_mVolts - 100.0) / 10.0) - 40.0);
		// convert celsius to fahrenheit
		float temp_F = (float) ((temp_C * 9.0 / 5.0) + 32);
		
		/*System.out.println("Ambient:" + ambient + "/100; Temperature:"
				+ temperature + "/100 => " + String.valueOf(temp_C) + "C => "
				+ String.valueOf(temp_F) + "F");
			String result = temp_C+":"+ambient;	
		*/
		/*System.out.println("/100; Temperature:"
				+ temperature + "/100 => " + String.valueOf(temp_C) + "C => "
				+ String.valueOf(temp_F) + "F");
		*/
		return (int)temp_F;
	}
}
