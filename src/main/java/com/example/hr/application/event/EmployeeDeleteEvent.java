package com.example.hr.application.event;

import com.example.hr.document.EmployeeDocument;

public class EmployeeDeleteEvent {

	private EmployeeDocument document;
	
	public EmployeeDeleteEvent() {
	}

	public EmployeeDeleteEvent(EmployeeDocument document) {
		this.document = document;
	}

	public EmployeeDocument getDocument() {
		return document;
	}

	public void setDocument(EmployeeDocument document) {
		this.document = document;
	}
	
	
	
}
