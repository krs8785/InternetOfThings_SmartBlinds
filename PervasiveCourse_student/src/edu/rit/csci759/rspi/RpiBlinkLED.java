package edu.rit.csci759.rspi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  ControlGpioExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import edu.rit.csci759.fuzzylogic.MyBlindClass;

public class RpiBlinkLED {
	
	public String smartSense () {
		// create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
		RpiSensorViaMCP3008 rpisense = new RpiSensorViaMCP3008();
		float output = rpisense.getTemperature(gpio);
		try {
			Thread.sleep(500L);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		int ambi = rpisense.getAmbient(gpio);
		System.out.println("output"+output);
		System.out.println("ambi"+ambi);
		
		MyBlindClass fuzzyLogic = new MyBlindClass();
		
		String outputNew = fuzzyLogic.calculateFuzzy(80, 82);
		System.out.println("fuzzyyy"+output);
		//double output = 0.0;
		gpio.shutdown();
		return outputNew;
	}

	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("<--Pi4J--> GPIO Control Example ... started.");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // provision gpio pin #01 as an output pin and turn on by default
        final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "MyLED", PinState.HIGH);
        System.out.println("--> GPIO state should be: ON");
        
        Thread.sleep(1000);
        
        // turn off gpio pin #01
        pin.low();
        System.out.println("--> GPIO state should be: OFF");

        Thread.sleep(1000);

        // toggle the current state of gpio pin #01 (should turn on)
        pin.toggle();
        System.out.println("--> GPIO state should be: ON");

        Thread.sleep(3000);

        // toggle the current state of gpio pin #01  (should turn off)
        pin.toggle();
        System.out.println("--> GPIO state should be: OFF");
        
        Thread.sleep(1000);

        // turn on gpio pin #01 for 1 second and then off
        System.out.println("--> GPIO state should be: ON for only 1 second");
        pin.pulse(1000, true); // set second argument to 'true' use a blocking call
        
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
        gpio.unprovisionPin(pin);
	}

}
