package org.example.betasolutions;
import org.example.betasolutions.project.Project;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// Uses active profile to make sure we are hooked to database
@ActiveProfiles("test")
// @SQL ensures that h2 is reset for usage
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
class AssignmentRepositoryTest {

    //private Task task;


    @Autowired
    @Qualifier("projectRepository") // Specify the exact bean name
    private AssignmentRepository superRepository;

    @Autowired
    private ConnectionManager connectionManager;
    private Connection conn;


    @BeforeEach
    void setUp() {
        conn = connectionManager.getConnection(); //instantiate this.conn using ConnectionManager object.
        try {
            conn.setAutoCommit(false);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @AfterEach
    void tearDown(){
        try {
            conn.rollback(); //rollback changes
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    //not done
    @Test
    void insertAssignmentIntoTable() {
        //instantiate new project.
        Project project = new Project("projectName", "projectOwner", 43, 500000.5, Date.valueOf("2024-12-02"));

        //testing method using project object and new project sql statement
        PreparedStatement preparedStatement = superRepository.insertAssignmentIntoTable(project,
                "INSERT INTO project (project_Name, project_Total_Hours, project_Total_Days, " +
                        "project_Total_Price, project_Deadline, project_Start_Date, project_Owner) " +
                        "VALUES (?,?,?,?,?,?,?)");

        assertNotNull(preparedStatement);
    }

    @Test
    void testRealAllAssignments(){
        List<ModelInterface> actualProjectList = superRepository.readAllAssignments("project", "project", Project::new);
        assertNotNull(actualProjectList);

        String actualProjectName = actualProjectList.get(0).getName();
        String expectedProjectName = "Car dealership";

        assertEquals(expectedProjectName, actualProjectName);
        assertEquals(1, actualProjectList.get(0).getID());
        /*
        for (ModelInterface project : actualProjectList) {
            System.out.println(project.getName());
        }*/
        assertEquals(2, actualProjectList.size());
    }

    @Test
    void testReadAllAssignmentsBelongingToProject() {/*
       List<ModelInterface> actualTaskList = superRepository.readAllAssignmentsBelongingToProject("task",  "task", Task::new, 1);

        //retrieving name for first element in task list.
        String actualTaskName = actualTaskList.get(0).getName();
        String expectedTaskName = "Task 1";

        assertEquals(expectedTaskName, actualTaskName); //verify name is same as in database.

        //retrieving task ID's for 1st and 2nd task object in tasklist..
        int taskID1 = actualTaskList.get(0).getID();
        int taskID2 = actualTaskList.get(1).getID();

        assertNotEquals(taskID1, taskID2); //verify ID's are unique.*/
    }

    //brug for employeeID fra andet table.
    @Test
    void testReadAllObjectsForEmployee() {
       /* List<ModelInterface> actualTaskList = superRepository.readAllAssignmentsForEmployee("task",1, 1, "task", Task::new);
        String actualTaskName = actualTaskList.get(0).getName();
        String expectedTaskName = "Task 1";

        assertEquals(expectedTaskName, actualTaskName); //verify name is same as in database.

        int taskID1 = actualTaskList.get(0).getID();
        int taskID2 = actualTaskList.get(1).getID();

        assertNotEquals(taskID1, taskID2); //verify ID's are unique.*/
        //assertTrue(false);

    }

    @Test
    void deleteObjectFromTable() {
        boolean objectDeleted = superRepository.deleteObjectFromTable("sub_Task", "sub_Task", 1);
        assertTrue(objectDeleted);
    }

    @Test
    void testUpdateObjectString(){
        boolean objectUpdate = superRepository.updateObjectString("task", "task_Name", 1, "supersej task");
        assertTrue(objectUpdate);
    }

    @Test
    void getTableIntByString(){
        //get id by name ("Task 1")
        int actualID = superRepository.getTableIntByString("task", "task_ID", "task_Name", "Select a car");
        int expectedID = 1;

        assertEquals(expectedID, actualID);
    }

    @Test
    void getTableIntByInt(){
        //get id by total hours (100)
        int actualID = superRepository.getTableIntByInt("task", "task_ID", "task_Total_Hours", 100);
        int expectedID = 1;

        assertEquals(expectedID, actualID);
    }

    @Test
    void getTableStringByString(){
        String expectedProjectOwner = "Suzan";
        String actualProjectOwner = superRepository.getTableStringByString("project", "project_Owner", "project_Name", "Car dealership");

        assertEquals(expectedProjectOwner, actualProjectOwner); //verify projectOwner = owner 1

        String expectedName = "Car dealership";
        String actualName = superRepository.getTableStringByString("project", "project_Name", "project_Owner", "Suzan");
        assertEquals(expectedName, actualName );//verify actualName = "Car dealership"
    }

    @Test
    void getTableStringByInt(){
        String expectedName = "Car dealership";
        String actualName = superRepository.getTableStringByInt("project", "project_Name", "project_ID", 1);
        assertEquals(expectedName, actualName); //verify projectName = Project A

        String expectedProjectOwner = "Suzan";
        String actualProjectOwner = superRepository.getTableStringByInt("project", "project_Owner", "project_ID", 1);
        assertEquals(expectedProjectOwner, actualProjectOwner); //verify projectOwner = "Owner 1"
    }

    @Test
    void deleteAllWhere(){
        //delete all subtasks.
        boolean deletedAllSubTasks = superRepository.deleteAllWhere("sub_Task", "task_ID = 1");
        assertTrue(deletedAllSubTasks);
    }

    @Test

    void updateInt(){
        boolean updatedHours = superRepository.updateInt("task", "task_Total_hours", 1, 4);
        assertTrue(updatedHours);

    }

    @Test
    void updateDate(){
        boolean updateDeadline = superRepository.updateDate("task", "task_deadline", 1, Date.valueOf(LocalDate.now()));
        assertTrue(updateDeadline);
    }

    @Test
    void readAssignmentByID(){
        int expectedID = 1;
        Project project = (Project) superRepository.readAssignmentByID("project", "project", Project::new, expectedID);
        int actualID = project.getID();

        assertEquals(expectedID, actualID);
    }

}