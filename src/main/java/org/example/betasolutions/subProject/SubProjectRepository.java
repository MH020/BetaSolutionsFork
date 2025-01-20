package org.example.betasolutions.subProject;
import org.example.betasolutions.ConnectionManager;
import org.example.betasolutions.ModelInterface;
import org.example.betasolutions.AssignmentRepository;
import org.example.betasolutions.project.Project;
import org.example.betasolutions.task.Task;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SubProjectRepository extends AssignmentRepository {

    public SubProjectRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }
    //create a subproject
    public boolean insertSubProject(SubProject subProject){
        String sql =( "insert into sub_project (sub_project_name,sub_project_total_hours,sub_project_total_days,sub_project_total_price,sub_project_deadline,sub_project_start_date,project_id) values(?,?,?,?,?,?,?)");
        PreparedStatement preparedStatement = super.insertAssignmentIntoTable(subProject,sql);
        try{
            preparedStatement.setInt(7,subProject.getProjectID()); //set foreign key 'project_ID' for subproject.
            preparedStatement.executeUpdate();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    //read all subprojects
    public List<SubProject> readAllSubProjects(int projectID){
        List<SubProject> subProjects = new ArrayList<>();
        for(ModelInterface assignmentObject : super.readAllAssignmentsBelongingToProject("sub_project","sub_project",SubProject::new,projectID)){
            if(assignmentObject instanceof SubProject){
                SubProject subProject = (SubProject) assignmentObject;
                subProjects.add(subProject);

                int projectIDfromTable = super.getTableIntByInt("sub_project","project_id","sub_project_id",subProject.getID());
                subProject.setProjectID(projectIDfromTable);

                subProjects.add(subProject);
            }
        }
        return subProjects;
    }
    //read a subproject
    public SubProject readSubProject(int subProjectID){
        //int projectID = super.getTableIntByInt("sub_project", "project_id", "sub_project_id", subProjectID); //get project id.
        SubProject subProject = (SubProject) super.readAssignmentByID("sub_project","sub_project",SubProject::new,subProjectID);
        subProject.setProjectID(super.getTableIntByInt("sub_project", "project_id", "sub_project_id", subProject.getID()));
        return subProject;
    }
    //delete a subproject
    public boolean deleteSubProject(int subProjectID){
        try {
        conn.setAutoCommit(false);
         super.deleteObjectFromTable("sub_project","sub_project",subProjectID);
         super.deleteAllWhere("task","sub_project_id =" + subProjectID);
            conn.commit();

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSubProjectTotalHours(SubProject subProject, int newTotalHours){
        return super.updateObjectInt("sub_project", "sub_project_total_hours", subProject.getID(), newTotalHours);
    }

    public int getTotalHoursForSubProject(SubProject subProject){
        int totalHours = 0;//subProject.getHours(); //get subProject-specific hours.

        List<ModelInterface> allTasks = super.readAllAssignments("task", "task", Task::new);//get All tasks.

        for (ModelInterface modelInterface : allTasks){
            Task task = (Task) modelInterface; //typecasting.
            task.setSubProjectID(super.getTableIntByInt("task", "sub_project_id", "task_id", task.getID()));
            task.setHours(super.getTableIntByInt("task", "task_hours", "task_id", task.getID()));//set task total hours.
            //task.setProjectID(super.getTableIntByInt("task", "project_id", "task_id", task.getID()));

            if (task.getSubProjectID() == subProject.getID()){
                totalHours += task.getHours(); //add task-specific hours to total.
            }
        }//end of all subtasks.

        return totalHours;
    }

    public boolean updateSubProjectPrice(SubProject subProject, double price){
        return super.updateDouble("sub_project", "sub_project_total_price", subProject.getID(), price);
    }

    public boolean updateSubProject(SubProject subProject) {
        String sql = "UPDATE subProject SET sub_project_Name = ?, sub_project_Total_Hours = ?, sub_project_Total_Days = ?, sub_project_Total_Price = ?, sub_project_Deadline = ?, sub_project_Start_Date =? WHERE sub_project_ID = ?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, subProject.getName());
            preparedStatement.setInt(2, subProject.getHours());
            preparedStatement.setInt(3, subProject.getDays());
            preparedStatement.setDouble(4, subProject.getTotalPrice());
            preparedStatement.setDate(5, subProject.getDeadline());
            preparedStatement.setDate(6, subProject.getStartDate());
            preparedStatement.setInt(7, subProject.getID());
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
