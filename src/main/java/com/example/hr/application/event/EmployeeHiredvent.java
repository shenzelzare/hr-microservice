package com.example.hr.application.event;

public class EmployeeHiredvent {
	
	private String eventId;
	
	private String identity;

	public EmployeeHiredvent() {
	}

	public EmployeeHiredvent(String eventId, String identity) {
		this.eventId = eventId;
		this.identity = identity;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
	
}
