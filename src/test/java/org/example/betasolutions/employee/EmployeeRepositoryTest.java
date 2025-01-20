package org.example.betasolutions.employee;

import jakarta.transaction.Transactional;
import org.example.betasolutions.ConnectionManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
// Uses active profile to make sure we are hooked to database
@ActiveProfiles("test")
// @SQL ensures that h2 is reset for usage
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")

class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ConnectionManager connectionManager;

    private Connection conn;

    @BeforeEach
    void setUp(){
        conn = connectionManager.getConnection();

    }

    @AfterEach
    void tearDown(){
        try{
            conn.rollback();
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Test
    void createNewEmployee() {
        int actualID = employeeRepository.createNewEmployee(new Employee(1, "John Doe", "Office", "Proficient", "1000"));
        int expectedID = 8;
        assertEquals(expectedID, actualID);
    }

    @Test
    void getAllEmployees() {
        int actual = employeeRepository.getAllEmployees().size();
        int expected = 7;
        assertEquals(expected, actual);
        String actualName = employeeRepository.getAllEmployees().get(0).getEmployeeName();
        String expectedName = "Mads";
        assertEquals(expectedName, actualName);
    }

    @Test
    void getAllEmployeesForProject() {
        int actual = employeeRepository.getAllEmployeesForProject(1).size();
        int expected = 1;
        assertEquals(expected, actual);
        String actualName = employeeRepository.getAllEmployeesForProject(1).get(0).getEmployeeName();
        String expectedName = "Mads";
        assertEquals(expectedName, actualName);
    }

    @Test
    void getAllEmployeesNotOnProject() {
        int actual = employeeRepository.getAllEmployeesNotOnProject(1).size();
        int expected = 6; //4 employees not assigned project 1.
        assertEquals(expected, actual);
        String actualName = employeeRepository.getAllEmployeesNotOnProject(1).get(0).getEmployeeName();
        String expectedName = "Oscar";
        assertEquals(expectedName, actualName);

    }
    @Test
    void getAllEmployeesForTask() {
        List<Employee> employeeList = employeeRepository.getAllEmployeesForTask(1, 1);
        //Test list size:
        int expectedSize = 1;
        int actualSize = employeeList.size();

        assertEquals(expectedSize, actualSize);

        //Test name for employee 1.
        String expectedName = "Mads";
        String actualName = employeeList.get(0).getEmployeeName();

        assertEquals(expectedName, actualName);

    }

    @Test
    void getAllEmployeesNotAssingedToTaskForProject() {
        List<Employee> employeeList = employeeRepository.getAllemployeesNotAssingedToTaskForProject(1, 1);
        //Test list size:
        int expectedSize = 0; //no employees assigned project, without a task.
        int actualSize = employeeList.size();

        assertEquals(expectedSize, actualSize);
/*
        //Test name for employee 1.
        String expectedName = "Employee 3";
        String actualName = employeeList.get(0).getEmployeeName();

        assertEquals(expectedName, actualName);
*/
    }

    @Test
    void getAllEmployeesForSubTask() {
        List<Employee> employeeList = employeeRepository.getAllEmployeesForSubTask(1,1,1);
        //Test list size:
        int expectedSize = 1;
        int actualSize = employeeList.size();

        assertEquals(expectedSize, actualSize);

        //Test name for employee 1.
        String expectedName = "Mads";
        String actualName = employeeList.get(0).getEmployeeName();

        assertEquals(expectedName, actualName);

    }

    @Test
    void getAllEmployeesNotOnSubtaskForProject() {
        List<Employee> employeeList = employeeRepository.getAllEmployeesNotOnSubtaskForProject(1);
        //Test list size:
        int expectedSize = 0;
        int actualSize = employeeList.size();

        assertEquals(expectedSize, actualSize);
/*
        //Test name for employee 1.
        String expectedName = "Employee 1";
        String actualName = employeeList.get(0).getEmployeeName();

        assertEquals(expectedName, actualName);
*/
    }


    @Test
    void addExistingEmployeeToProject() {
        assertTrue(employeeRepository.addExistingEmployeeToProject(1, 2));
        /*
        int  actualSize = employeeRepository.getAllEmployeesForProject(2).size();
        int expectedSize = 3; //3 employees in project.
        assertEquals(expectedSize, actualSize);*/
    }

    @Test
    void addExistingEmployeeToTask() {
       assertTrue(employeeRepository.addExistingEmployeeToTask(3, 2, 2));
      /*  int actual = employeeRepository.getAllEmployees().size();
        int expected = 3;
        assertEquals(expected, actual);*/
    }

    @Test
    void addExistingEmployeeToSubTask() {
        assertTrue (employeeRepository.addExistingEmployeeToSubTask(4, 2, 2, 2));
        /*
        int actual = employeeRepository.getAllEmployees().size();
        int expected = 3;
        assertEquals(expected, actual);*/
    }
    @Test
    void getAllEmployeeOffices() {
    }

}