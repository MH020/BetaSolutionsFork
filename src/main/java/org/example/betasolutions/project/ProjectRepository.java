package org.example.betasolutions.project;

import org.example.betasolutions.ConnectionManager;
import org.example.betasolutions.ModelInterface;
import org.example.betasolutions.AssignmentRepository;
import org.example.betasolutions.subProject.SubProject;
import org.example.betasolutions.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository

public class ProjectRepository extends AssignmentRepository {

    @Autowired
    public ProjectRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    //Create method
    // Insert project into project table with the projectOwner.which is still completely nuts to me.
    public int insertAssignmentIntoTable(Project project) {
        String sql = "INSERT INTO project (project_Name, project_Total_Hours, project_Total_Days, project_Total_Price, project_Deadline, project_Start_Date, project_Owner) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = super.insertAssignmentIntoTable(project, sql);
        try {
            preparedStatement.setString(7, project.getProjectOwner());//set projectowner.
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt("project_ID");//return objectID.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;//if failed
    }
    //update


    //Read method
    //this is just a basic read method for all projects in the project table.i have a idea on how to make it work with superclass read method instead but i need to test it first so this is a placeholder for now.
    public List<Project> readAllProjects() {
        ArrayList<Project> projectList = new ArrayList<>();

        for (ModelInterface assignmentObject : super.readAllAssignments("project", "project", Project::new)) {
            //typecasting assignmentObject as Project:
            if (assignmentObject instanceof Project) {
                Project project = (Project) assignmentObject;

                //Getting projectowner from projectTAble using projectID.
                String projectOwner = super.getTableStringByInt("project", "project_Owner", "project_ID", project.getID());

                //adding projectowner to projectObject.
                project.setProjectOwner(projectOwner);
                //adding projectObject to projectList.
                projectList.add(project);
            }
        }//end of for loop.
        return projectList;
    }

    //Read method by ID for project. not sure if this actually works but i will test it later.
    public Project readProjectByID(int projectID) {
        Project Project = (Project) super.readAssignmentByID("project", "project", Project::new, projectID);
        if (Project != null) {
            String projectOwner = super.getTableStringByInt("project", "project_Owner", "project_ID", Project.getID());
            Project.setProjectOwner(projectOwner);
        }
        return Project;
    }

    public boolean updateProject(Project project, int projectID) {
        String sql = "UPDATE project SET project_Name = ?, project_Owner = ?, project_Total_Hours = ?, project_Total_Days = ?, project_Total_Price = ?, project_Deadline = ?,project_Start_Date =? WHERE project_ID = ?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, project.getName());
            preparedStatement.setString(2, project.getProjectOwner());
            preparedStatement.setInt(3, project.getTotalHours());
            preparedStatement.setInt(4, project.getTotalDays());
            preparedStatement.setDouble(5, project.getTotalPrice());
            preparedStatement.setDate(6, project.getDeadline());
            preparedStatement.setDate(7, project.getStartDate());
            preparedStatement.setInt(8, projectID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getTotalHoursForProject(Project project){
        int totalHours = 0;//project.getHours(); //get project hours.

        //get all tasks for project from database.
        List <ModelInterface> allTasks = super.readAllAssignmentsBelongingToProject("task", "task", Task::new, project.getID());

        //for each task in project, add task total hours.
        for (ModelInterface modelInterface : allTasks){
            totalHours += super.getTableIntByInt("task", "task_hours", "task_id", modelInterface.getID());
        }

        return totalHours;
    }

    public boolean updateTotalHoursForProject(int projectID, int newTotalHours) {
        return super.updateObjectInt("project", "project_total_hours", projectID, newTotalHours);
    }

    public boolean updateProjectPrice(Project project, double price){
        return super.updateDouble("project", "project_total_price", project.getID(), price);
    }

    public boolean deleteProject(int projectID) {
        return super.deleteObjectFromTable("project", "project", projectID);
    }
}
