package com.fms.flight.service;

import java.util.List;

import com.fms.flight.dto.FlightDTO;
import com.fms.flight.dto.ScheduleFlightDTO;
import com.fms.flight.exceptions.FlightModelAlreadyexistsException;
import com.fms.flight.exceptions.FlightNumberNotFoundException;

public interface FlightService {
	
	String addFlight(FlightDTO flightDto) throws FlightModelAlreadyexistsException;
	
	String modifyFlightByFlightNumber(Long flightNumber,FlightDTO flightDTO) throws FlightNumberNotFoundException;
	
	String removeFlightByFlightNumber(Long flightNumber) throws FlightNumberNotFoundException;
	
	FlightDTO getFlightByFlightNumber(Long flightNumber) throws FlightNumberNotFoundException;
	
	List<FlightDTO> getAllFlights();
	
	List<ScheduleFlightDTO> getAllSchedulesByFlightNumber(Long flightNumber) throws FlightNumberNotFoundException;

}
