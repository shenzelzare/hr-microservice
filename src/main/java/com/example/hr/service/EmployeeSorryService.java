package com.example.hr.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.example.hr.application.event.EmployeeDeleteEvent;

@Service
public class EmployeeSorryService {

	@EventListener
	public void handleEmployeeDeleteEvent(EmployeeDeleteEvent event) {
		var fullName = event.getDocument().getFullname();
		System.err.println("Sending sorry message to the employee..." + fullName);
	}
}
