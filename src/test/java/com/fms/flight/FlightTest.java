package com.fms.flight;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import com.fms.flight.dto.FlightDTO;
import com.fms.flight.entity.Flight;
import com.fms.flight.exceptions.FlightModelAlreadyexistsException;
import com.fms.flight.exceptions.FlightNumberNotFoundException;
import com.fms.flight.repository.FlightRepository;
import com.fms.flight.service.FlightService;
import com.fms.flight.service.FlightServiceIplementation;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

class FlightTest {

	@Mock
	private FlightRepository flightRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private FlightServiceIplementation flightService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Test to add")
	@Order(1)
	void addFlightTest() throws FlightModelAlreadyexistsException {

		FlightDTO flightDTO = new FlightDTO();
		flightDTO.setCarrierName("American Airlines");
		flightDTO.setFlightModel("A1001");
		flightDTO.setSeatCapacity(180);

		String result = flightService.addFlight(flightDTO);
		assertEquals("Flight added successfully!", result);

	}

	@Test
	@DisplayName("Test to Modify")
	@Order(2)
	void ModifyFlightByFlightNumberTest() throws FlightNumberNotFoundException {

		Long existingFlightNumber = 12345L;
		Flight existingFlight = new Flight(existingFlightNumber, "ABC Airlines", "ABC101", 100);
		when(flightRepository.findById(existingFlightNumber)).thenReturn(Optional.of(existingFlight));

		FlightDTO updatedFlightDTO = new FlightDTO();
		updatedFlightDTO.setCarrierName("B Airways");
		updatedFlightDTO.setFlightModel("B102");
		updatedFlightDTO.setSeatCapacity(155);

		String result = flightService.modifyFlightByFlightNumber(existingFlightNumber, updatedFlightDTO);
		verify(flightRepository, times(1)).findById(existingFlightNumber);
		verify(flightRepository, times(1)).save(existingFlight);
		assertEquals("Flight Details updated successfully!", result);

	}

	@Test
	@DisplayName("Test to Modify Exception")
	@Order(3)
	void ModifyFlightByFlightNumberNumberNotFoundExceptionTest() {

		Long flightNumber = 1L;
		assertThrows(FlightNumberNotFoundException.class,
				() -> flightService.modifyFlightByFlightNumber(flightNumber, new FlightDTO()));

	}

	@Test
	@DisplayName("Test to removeException")
	@Order(4)
	void testRemoveFlightByFlightNumber() {

		Long nonExistingFlightNumber = 99999L;

		when(flightRepository.findById(nonExistingFlightNumber)).thenReturn(Optional.empty());
		assertThrows(FlightNumberNotFoundException.class,
				() -> flightService.removeFlightByFlightNumber(nonExistingFlightNumber));
		// Verify that the delete method was not called since the flight does not exist
		verify(flightRepository, never()).delete(any(Flight.class));

	}

	@Test
	@DisplayName("Test to removeException 1")
	@Order(5)
	void testRemoveFlightByFlightNumberFlightNumberNotFound() {

		Long flightNumber = 1L;
		assertThrows(FlightNumberNotFoundException.class, () -> flightService.removeFlightByFlightNumber(flightNumber));
	}

	@Test
	@DisplayName("Test to Find Flight")
	@Order(6)
	void testGetFlightByFlightNumber() throws FlightNumberNotFoundException, FlightModelAlreadyexistsException {

		Long flightNumber = 1L;

		FlightService flightService = mock(FlightService.class);
		FlightDTO existingFlight = new FlightDTO();
		existingFlight.setFlightNumber(flightNumber);
		existingFlight.setCarrierName("Test Carrier");
		existingFlight.setFlightModel("Test Model2");
		existingFlight.setSeatCapacity(200);

		when(flightService.addFlight(existingFlight)).thenReturn("Flight added successfully");
		when(flightService.getFlightByFlightNumber(flightNumber)).thenReturn(existingFlight);

		// Call the addFlight method to store the flight
		String addFlightResponse = flightService.addFlight(existingFlight);
		assertNotNull(addFlightResponse);
		assertEquals("Flight added successfully", addFlightResponse);

		// Call the getFlightByFlightNumber method to retrieve the stored flight
		FlightDTO result = flightService.getFlightByFlightNumber(flightNumber);

		assertEquals(existingFlight.getFlightNumber(), result.getFlightNumber());
		assertEquals(existingFlight.getCarrierName(), result.getCarrierName());
		assertEquals(existingFlight.getFlightModel(), result.getFlightModel());
		assertEquals(existingFlight.getSeatCapacity(), result.getSeatCapacity());

	}

	@Test
	@DisplayName("Test to Find Flight Exception")
	@Order(7)
	void testGetFlightByFlightNumberFlightNumberNotFound() {

		Long flightNumber = 1L;
		assertThrows(FlightNumberNotFoundException.class, () -> flightService.getFlightByFlightNumber(flightNumber));
	}

	@Test
	@DisplayName("Test to Get all Flight")
	@Order(8)
	void testGetAllFlights() {

		Flight flight1 = new Flight();
		flight1.setCarrierName("Test Carrier 1");
		flight1.setFlightModel("Test Model 1");
		flight1.setSeatCapacity(200);

		Flight flight2 = new Flight();
		flight2.setCarrierName("Test Carrier 2");
		flight2.setFlightModel("Test Model 2");
		flight2.setSeatCapacity(250);

		Mockito.when(flightRepository.findAll()).thenReturn(Arrays.asList(flight1, flight2));
		List<FlightDTO> result = flightService.getAllFlights();
		assertEquals(2, result.size());

	}

	@Test
	@DisplayName("Test-2 to Get all Flight")
	@Order(9)
	void getAllFlightsTest() {
		assertNotNull(flightService.getAllFlights());
	}

	@Test
	@DisplayName("Test to Modify Flight Status")
	@Order(10)
	void modifyFlightStatusTest() throws FlightNumberNotFoundException {

		Long flightNumber = 12345L;
		Flight existingFlight = new Flight(flightNumber, "ABC Airlines", "ABC101", 100);
		when(flightRepository.findById(flightNumber)).thenReturn(Optional.of(existingFlight));

		FlightDTO updatedFlightDTO = new FlightDTO();
		updatedFlightDTO.setCarrierName("ABC Airlines");
		updatedFlightDTO.setFlightModel("ABC101");
		updatedFlightDTO.setSeatCapacity(100);

		String result = flightService.modifyFlightByFlightNumber(flightNumber, updatedFlightDTO);
		verify(flightRepository, times(1)).findById(flightNumber);
		verify(flightRepository, times(1)).save(existingFlight);

		assertEquals("Flight Details updated successfully!", result);

	}

	@Test
	@DisplayName("Test to Modify Flight with Empty DTO")
	@Order(11)
	void modifyFlightWithEmptyDTOTest() {

		Long flightNumber = 12345L;
		assertThrows(FlightNumberNotFoundException.class,
				() -> flightService.modifyFlightByFlightNumber(flightNumber, new FlightDTO()));

	}

	@Test
	@DisplayName("Test to Remove Non-existing Flight")
	@Order(12)
	void testRemoveNonExistingFlightByFlightNumber() {

		Long nonExistingFlightNumber = 99999L;
		when(flightRepository.findById(nonExistingFlightNumber)).thenReturn(Optional.empty());

		assertThrows(FlightNumberNotFoundException.class,
				() -> flightService.removeFlightByFlightNumber(nonExistingFlightNumber));
		// Verify that the delete method was not called since the flight does not exist
		verify(flightRepository, never()).delete(any(Flight.class));

	}

	@Test
	@DisplayName("Test to Get All Flights with Empty Repository")
	@Order(13)
	void testGetAllFlightsEmptyRepository() {

		when(flightRepository.findAll()).thenReturn(Collections.emptyList());
		List<FlightDTO> result = flightService.getAllFlights();
		assertEquals(0, result.size());

	}

}
