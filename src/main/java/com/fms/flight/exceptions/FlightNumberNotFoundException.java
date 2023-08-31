package com.fms.flight.exceptions;

@SuppressWarnings("serial")
public class FlightNumberNotFoundException extends Exception{
	
	public FlightNumberNotFoundException(String msg) {
		super(msg);
	}

}
