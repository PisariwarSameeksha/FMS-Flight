package com.fms.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fms.flight.dto.FlightDTO;
import com.fms.flight.entity.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long>{
	
	void save(FlightDTO flightDTO);
	
	Flight findByFlightModel(String flightModel);

}
