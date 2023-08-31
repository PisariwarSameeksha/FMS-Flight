package com.fms.flight.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fms.flight.dto.FlightDTO;
import com.fms.flight.dto.ScheduleFlightDTO;
import com.fms.flight.repository.FlightRepository;

import reactor.core.publisher.Mono;

import com.fms.flight.entity.Flight;
import com.fms.flight.exceptions.FlightModelAlreadyexistsException;
import com.fms.flight.exceptions.FlightNumberNotFoundException;

@Service
public class FlightServiceIplementation implements FlightService {

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private WebClient webClient;

	@Override
	public String addFlight(FlightDTO flightDTO) throws FlightModelAlreadyexistsException {

		Flight newModel = this.flightRepository.findByFlightModel(flightDTO.getFlightModel());
		if (newModel == null) {
			Flight flight = modelMapper.map(flightDTO, Flight.class);
			flightRepository.save(flight);
			return "Flight added successfully!";
		} else {
			throw new FlightModelAlreadyexistsException("This model already exists!");
		}

	}

	@Override
	public String modifyFlightByFlightNumber(Long flightNumber, FlightDTO flightDTO)
			throws FlightNumberNotFoundException {

		Optional<Flight> existingFlight = this.flightRepository.findById(flightNumber);
		if (existingFlight.isEmpty()) {
			throw new FlightNumberNotFoundException("Flight Number doesn't exist");
		}
		Flight flight = existingFlight.get();
		flight.setCarrierName(flightDTO.getCarrierName());
		flight.setFlightModel(flightDTO.getFlightModel());
		flight.setSeatCapacity(flightDTO.getSeatCapacity());
		flightRepository.save(flight);
		return "Flight Details updated successfully!";

	}

	@Override
	public String removeFlightByFlightNumber(Long flightNumber) throws FlightNumberNotFoundException {
		Optional<Flight> optFlight = this.flightRepository.findById(flightNumber);
		if (optFlight.isEmpty()) {
			throw new FlightNumberNotFoundException("Flight Number does not exist");
		}
		Mono<String> response = webClient.delete()
				.uri("http://localhost:8093/api/scheduleFlight/allSchedules/{fid}", flightNumber).retrieve()
				.bodyToMono(String.class);
		String responseMessage = response.block();
		if (responseMessage == null) {
			throw new FlightNumberNotFoundException("Failed to receive response from scheduleFlight service");
		}
		Flight flight = optFlight.get();
		this.flightRepository.delete(flight);
		return "Flight removed successfully!";
	}

	@Override
	public FlightDTO getFlightByFlightNumber(Long flightNumber) throws FlightNumberNotFoundException {

		Optional<Flight> optFlight = this.flightRepository.findById(flightNumber);
		if (optFlight.isEmpty()) {
			throw new FlightNumberNotFoundException("Flight Number does not exist");
		}
		Flight flight = optFlight.get();
		return modelMapper.map(flight, FlightDTO.class);

	}

	@Override
	public List<FlightDTO> getAllFlights() {

		List<Flight> flights = flightRepository.findAll();
		return flights.stream().map(flight -> modelMapper.map(flight, FlightDTO.class)).toList();

	}

	@Override
	public List<ScheduleFlightDTO> getAllSchedulesByFlightNumber(Long flightNumber)
			throws FlightNumberNotFoundException {

		Mono<ScheduleFlightDTO[]> response = webClient.get().uri("http://localhost:8093/api/scheduleFlight/schedules")
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(ScheduleFlightDTO[].class).log();

		ScheduleFlightDTO[] scheduleFlights = response.block();
		return Arrays.stream(scheduleFlights).filter(s -> s.getFlightId().equals(flightNumber))
				.toList();
	}

}
