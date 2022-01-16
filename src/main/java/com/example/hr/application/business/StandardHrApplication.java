package com.example.hr.application.business;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.hr.application.HrApplication;
import com.example.hr.application.business.exception.EmployeeAlreadyExistException;
import com.example.hr.application.business.exception.EmployeeNotFoundException;
import com.example.hr.application.event.EmployeeDeleteEvent;
import com.example.hr.application.event.EmployeeHiredvent;
import com.example.hr.document.EmployeeDocument;
import com.example.hr.dto.request.HireEmployeeRequest;
import com.example.hr.dto.request.UpdateEmployeeRequest;
import com.example.hr.dto.response.EmployeeResponse;
import com.example.hr.dto.response.FireEmployeeResponse;
import com.example.hr.dto.response.HireEmployeeResponse;
import com.example.hr.repository.EmployeeMongoRepository;

@Service
public class StandardHrApplication implements HrApplication {
	
	private EmployeeMongoRepository mongoRepository;
	private ModelMapper modelMapper;
	private ApplicationEventPublisher publisher;

	public StandardHrApplication(EmployeeMongoRepository mongoRepository, ModelMapper modelMapper,
			ApplicationEventPublisher publisher) {
		this.mongoRepository = mongoRepository;
		this.modelMapper = modelMapper;
		this.publisher = publisher;
	}

	@Override
	public HireEmployeeResponse hireEmployee(HireEmployeeRequest request) {
		var identity = request.getIdentity();
		var employeeHiredEvent = new EmployeeHiredvent(UUID.randomUUID().toString(), identity);
		if (mongoRepository.existsById(identity))
			throw new EmployeeAlreadyExistException();
		var employee = modelMapper.map(request, EmployeeDocument.class);
		employee = mongoRepository.save(employee);
		publisher.publishEvent(employeeHiredEvent);
		return modelMapper.map(employee, HireEmployeeResponse.class);
	}

	@Override
	public EmployeeResponse updateEmployee(String identity, UpdateEmployeeRequest request) {
//		var managedEmployee = mongoRepository.findById(identity)
//				.orElseThrow(() -> new EmployeeNotFoundException());
//		String photo = request.getPhoto();
//		if (Objects.nonNull(photo))
//			managedEmployee.setPhoto(photo);
//		managedEmployee.setBirthYear(request.getBirthYear());
//		managedEmployee.setDepartment(request.getDepartment());
//		managedEmployee.setFullname(request.getFullname());
//		managedEmployee.setFulltime(request.isFulltime());
//		managedEmployee.setIban(request.getIban());
//		managedEmployee.setIdentity(request.getIdentity());
//		managedEmployee.setSalary(request.getSalary());
//		mongoRepository.save(managedEmployee);
//		var updateCustomerResponse = modelMapper.map(managedEmployee, EmployeeResponse.class);
//		return updateCustomerResponse;
		if(mongoRepository.existsById(identity)) {
			var employee = modelMapper.map(request, EmployeeDocument.class);
			employee.setIdentity(identity);
			return modelMapper.map(mongoRepository.save(employee), EmployeeResponse.class);
		}
		throw new EmployeeNotFoundException();
	}

	@Override
	public FireEmployeeResponse fireEmployee(String identity) {
		var employee = mongoRepository.findById(identity)
				.orElseThrow(() -> new EmployeeNotFoundException());
		var employeeDeleteEvent = new EmployeeDeleteEvent(employee);	
		
		mongoRepository.delete(employee);
		publisher.publishEvent(employeeDeleteEvent);
		return modelMapper.map(employee, FireEmployeeResponse.class);
	}

	@Override
	public List<EmployeeResponse> findEmployees(@Min(0) int page, @Max(25) int size) {
		return mongoRepository.findAll(PageRequest.of(page, size)).getContent().stream()
				.map(emp -> modelMapper.map(emp, EmployeeResponse.class)).toList();
	}

	@Override
	public EmployeeResponse findEmployeeByIdentity(String identity) {
		var employee = mongoRepository.findById(identity).orElseThrow(() -> new EmployeeNotFoundException());
		return modelMapper.map(employee, EmployeeResponse.class);
	}

}
