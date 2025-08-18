package com.bash.Unitrack.Controllers;

import com.bash.Unitrack.Data.Models.Department;
import com.bash.Unitrack.Repositories.DepartmentRepository;
import com.bash.Unitrack.authentication.repository.UserRepository;
import com.bash.Unitrack.Service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public DepartmentController(DepartmentService departmentService, UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.departmentService = departmentService;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<Department>> fetchDepartments(){
        return departmentService.fetchDepartments();
    }

    @PostMapping("/create")
    public ResponseEntity<String> addDepartment(@RequestBody Map<String, String> request){
        return departmentService.addDepartment(request.get("departmentName"));
    }
}
