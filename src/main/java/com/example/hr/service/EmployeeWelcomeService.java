package com.example.hr.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.example.hr.application.HrApplication;
import com.example.hr.application.event.EmployeeHiredvent;

@Service
public class EmployeeWelcomeService {

	private HrApplication application;
	
	public EmployeeWelcomeService(HrApplication application) {
		this.application = application;
	}

	@EventListener
	public void handleCustomerAcquiredEvent(EmployeeHiredvent event) {
		var identity = event.getIdentity();
		var employee = application.findEmployeeByIdentity(identity);
		var fullName = employee.getFullname();
		System.err.println("Sending welcome message to the customer..."+ fullName);
	}
}
