package com.fms.flight.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fms.flight.dto.ScheduleFlightDTO;
import com.fms.flight.dto.FlightDTO;
import com.fms.flight.exceptions.FlightModelAlreadyexistsException;
import com.fms.flight.exceptions.FlightNumberNotFoundException;
import com.fms.flight.service.FlightService;

@RestController
@RequestMapping("/api/flight")
@CrossOrigin(origins = "http://localhost:4200")
public class FlightController {

	private static final Logger logger = LoggerFactory.getLogger(FlightController.class);

	@Autowired
	private FlightService flightService;

	@PostMapping("/flights")
	ResponseEntity<String> addFlight(@Valid @RequestBody FlightDTO flightDTO) throws FlightModelAlreadyexistsException {

		logger.info("Received request to add a flight: {}", flightDTO);
		flightService.addFlight(flightDTO);
		logger.info("Flight added: {}", flightDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body("Added!");

	}

	@PutMapping("/flights/{fnum}")
	ResponseEntity<String> modifyFlight(@PathVariable("fnum") Long fnum, @Valid @RequestBody FlightDTO flightDTO) {

		try {
			logger.info("Received request to modify flight with number: {}", fnum);
			flightService.modifyFlightByFlightNumber(fnum, flightDTO);
			logger.info("Flight with number {} modified successfully", fnum);
			return ResponseEntity.status(HttpStatus.OK).body("Updated!");
		} catch (FlightNumberNotFoundException e) {
			logger.warn("Flight with number {} not found for modification", fnum);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight Not Found");
		}

	}

	@DeleteMapping("/flights/{fnum}")
	ResponseEntity<String> removeFlight(@PathVariable("fnum") Long fnum) {

		try {
			logger.info("Received request to remove flight with number: {}", fnum);
			flightService.removeFlightByFlightNumber(fnum);
			logger.info("Flight with number {} removed successfully", fnum);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted!");
		} catch (FlightNumberNotFoundException e) {
			logger.warn("Flight with number {} not found for removal", fnum);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight Not Found");
		}

	}

	@GetMapping("/flights/{fnum}")
	ResponseEntity<FlightDTO> getFlightDetails(@PathVariable("fnum") Long fnum) {

		try {
			logger.info("Received request to fetch details for flight with number: {}", fnum);
			FlightDTO flightDTO = flightService.getFlightByFlightNumber(fnum);
			logger.info("Flight details fetched successfully for flight with number: {}", fnum);
			return ResponseEntity.status(HttpStatus.OK).body(flightDTO);
		} catch (FlightNumberNotFoundException e) {
			logger.warn("Flight with number {} not found", fnum);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

	}

	@GetMapping("/flights")
	ResponseEntity<List<FlightDTO>> getAllFlights() {

		logger.info("Received request to fetch all flights");
		List<FlightDTO> flights = flightService.getAllFlights();
		logger.info("Fetched {} flights", flights.size());
		return ResponseEntity.status(HttpStatus.OK).body(flights);
	}

	@GetMapping("/flights/schedules/{fnum}")
	public ResponseEntity<List<ScheduleFlightDTO>> getAllSchedules(@PathVariable("fnum") Long fnum)
			throws FlightNumberNotFoundException {

		logger.info("Received a request to get scheduled flights");
		List<ScheduleFlightDTO> flights = flightService.getAllSchedulesByFlightNumber(fnum);
		logger.info("Fetched {} schedules", flights.size());
		return ResponseEntity.status(HttpStatus.OK).body(flights);

	}

}
