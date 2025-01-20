package org.example.betasolutions.task;

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
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// Uses active profile to make sure we are hooked to database
@ActiveProfiles("test")
// @SQL ensures that h2 is reset for usage
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    private ConnectionManager connectionManager;

    Connection conn;

    @BeforeEach
    void setup(){
        conn = connectionManager.getConnection();
    }


    @AfterEach
    void tearDown(){
        try {
            conn.rollback();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
/*
    @Test
    void updateTaskTotalHours() {
        //taskRepository.updateTaskHours(1, 4);

    }

    @Test
    void updateTaskTotalDays() {
    }
*/
    @Test
    void addTaskForProject() {
    }

    //foreing key error. 'project.project_id'
    @Test
    void addTaskToProject() {
        Task task = new Task (3, "new task", 4, 1, 5000.2, Date.valueOf("2024-12-09"), Date.valueOf("2024-12-10"));
        task.setProjectID(1);
        boolean taskAddedToProject = taskRepository.addTaskToProject(task);

        assertTrue(taskAddedToProject);
    }

    //foreing key error. 'project.project_id'
    @Test
    void addTaskToSubProject() {
        Task task = new Task ("new task", 4, 5000.2, Date.valueOf("2024-12-09"), 1);
        //task.setProjectID(1);

        boolean taskAddedToSubProject = taskRepository.addTaskToProject(task);

        assertTrue(taskAddedToSubProject);

    }

    @Test
    void readAllTasksBelongingToProject() {
        List<Task> allTasksForProject = taskRepository.readAllTasksBelongingToProject(1);
        String expectedName = "Select a car";
        String actualName = allTasksForProject.get(0).getName(); //get name from first task in tasklist.

        assertEquals(expectedName, actualName);

    }

    //hmm.
    @Test
    void readAllTasksForSubProject() {
        List <Task> allTasksForSubProject = taskRepository.readAllTasksForSubProject(1, 1); //??

        int expectedSize = 1;
        int actualSize = allTasksForSubProject.size();
        assertEquals(expectedSize, actualSize);


        String expectedName = "Select a car";
        String actualName = allTasksForSubProject.get(0).getName();
        assertEquals(expectedName, actualName);

    }

    @Test
    void readTask() {

        int expectedID = 1;
        Task task1 = taskRepository.readTask(expectedID);
        int actualID = task1.getID();

        assertEquals(expectedID, actualID);

        Task task2 = taskRepository.readTask(2);

        assertNotEquals(task1, task2);

    }
    @Test
    void deleteTask() {
        int taskDeleted = taskRepository.deleteTask(1);
        assertTrue(true);
    }

    @Test
    void getTotalHoursForTask(){
        Task task = taskRepository.readTask(1);
        int totalHours = taskRepository.getTotalHoursForTask(task);
        int expectedHours = 70;
        assertEquals(expectedHours, totalHours);
    }

    @Test
    void updateTotalHoursForTask(){
        assertTrue(taskRepository.updateTotalHoursForTask(1, 100));
    }
    

}