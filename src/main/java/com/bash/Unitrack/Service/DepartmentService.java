package com.bash.Unitrack.Service;

import com.bash.Unitrack.Data.Models.Department;
import com.bash.Unitrack.Repositories.DepartmentRepository;
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
