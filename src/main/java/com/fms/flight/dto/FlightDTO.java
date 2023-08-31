package com.fms.flight.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class FlightDTO {

	private Long flightNumber;

	@NotBlank(message = "Carrier name is required")
	@Pattern(regexp = "^[a-zA-Z '-]*$", message = "Input should not contain special characters or numbers")
	@Size(min = 5, max = 14, message = "Input length should be between 5 and 14 characters")
	private String carrierName;

	@NotBlank(message = "Flight model is required")
	@Pattern(regexp = "^[a-zA-Z]{2}\\d{4,6}$", message = "Invalid format. First two characters must be alphabets, and the remaining characters must be numbers.")
	@Size(min = 6, max = 8, message = "Length must be between 6 and 8 characters.")
	private String flightModel;

	@NotNull(message = "Seat capacity is required")
	@Min(value = 50, message = "Value must be at least 50")
	@Max(value = 600, message = "Value must not exceed 600")
	private Integer seatCapacity;

	public FlightDTO() {
		super();
	}

	public Long getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(Long flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getFlightModel() {
		return flightModel;
	}

	public void setFlightModel(String flightModel) {
		this.flightModel = flightModel;
	}

	public Integer getSeatCapacity() {
		return seatCapacity;
	}

	public void setSeatCapacity(Integer seatCapacity) {
		this.seatCapacity = seatCapacity;
	}



}
