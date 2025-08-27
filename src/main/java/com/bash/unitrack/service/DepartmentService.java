package com.bash.unitrack.service;

import com.bash.unitrack.data.models.Department;
import com.bash.unitrack.repositories.DepartmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public ResponseEntity<List<Department>> fetchDepartments() {
        return ResponseEntity.status(HttpStatus.OK).body(departmentRepository.findAll());
    }

    public ResponseEntity<String> addDepartment(String departmentName){
        Department newDepartment = new Department(departmentName);
        departmentRepository.save(newDepartment);
        return ResponseEntity.status(HttpStatus.CREATED).body("Added Department");
    }
}
