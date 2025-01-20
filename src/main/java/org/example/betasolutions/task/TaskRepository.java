package org.example.betasolutions.task;
import org.example.betasolutions.ConnectionManager;

import org.example.betasolutions.ModelInterface;
import org.example.betasolutions.AssignmentRepository;
import org.example.betasolutions.subTask.SubTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository extends AssignmentRepository {

    @Autowired
    public TaskRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    public void updateTaskTotalHours(int taskID, int taskHours){
        super.updateInt("task", "task_Total_Hours", taskID, taskHours);
    }

    public void updateTaskTotalDays(int taskID, int taskDays){
        super.updateInt("task", "task_total_days", taskID, taskDays);

    }

    public void updateTaskDeadline(int taskID, Date taskDate){
        super.updateDate("task", "task_deadline", taskID, taskDate);
    }

    public boolean addTaskToProject(Task task){
/*
        String sql = "insert into task (task_name, task_total_hours,task_total_days,task_total_price,task_deadline,task_start_date,project_id, task_hours, task_days) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = super.insertAssignmentIntoTable(task,sql); //get prepared statement from superclass.
        try{
            preparedStatement.setInt(7,task.getProjectID()); //set project id for task.

            /*preparedStatement.setInt(2, getTotalHoursForTask(task));//set total hours for task.
            preparedStatement.setInt(3, task.getTotalDays()); //update total days.
            preparedStatement.setInt(5, task.getDeadline());//update*/
        /*
            preparedStatement.setInt(8, task.getHours());//set task specific hours
            preparedStatement.setInt(9, task.getDays());//set task specific days.

            preparedStatement.executeUpdate(); //add task to database.
*/

    String sql = "insert into task (task_name, task_hours,task_total_hours,task_days, task_total_days,task_total_price,task_deadline,task_start_date,project_id) values(?,?,?,?,?,?,?,?,?)";
    try (PreparedStatement preparedStatement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

        preparedStatement.setString(1, task.getName()); //name
        preparedStatement.setInt(2, task.getHours());   //hours
        preparedStatement.setInt(3, task.getTotalHours());//totalhours
        preparedStatement.setInt(4, task.getDays());//      //days
        preparedStatement.setInt(5, task.getTotalDays());   //totaldays
        preparedStatement.setDouble(6, task.getTotalPrice());  //price
        preparedStatement.setDate(7, task.getDeadline());   //deadline
        preparedStatement.setDate(8, task.getStartDate());  //startDate
        preparedStatement.setInt(9, task.getProjectID());   //project ID
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean addTaskToSubProject(Task task){
      /*  String sql = "insert into task (task_name, task_total_hours,task_total_days,task_total_price,task_deadline,task_start_date,project_id, sub_project_id, task_hours, task_days) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = super.insertAssignmentIntoTable(task,sql); //get prepared statement from superclass.
        try{
            preparedStatement.setInt(7,task.getProjectID()); //set project id for task.
            preparedStatement.setInt(8,task.getSubProjectID()); //set subproject id for task.

            preparedStatement.setInt(2, task.getTotalHours());//update task Total hours.
            preparedStatement.setInt(9, task.getHours()); //set task specific hours.
            preparedStatement.setInt(10, task.getDays());//set task specific days
            preparedStatement.executeUpdate(); //add task to database.
            return true;
        }catch (Exception e){
*/
        String sql = "insert into task (task_name, task_hours,task_total_hours,task_days, task_total_days,task_total_price,task_deadline,task_start_date,project_id,sub_project_id) values(?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, task.getName());
            preparedStatement.setInt(2, task.getHours());
            preparedStatement.setInt(3, task.getTotalHours());
            preparedStatement.setInt(4, task.getDays()); //get days
            preparedStatement.setInt(5, task.getTotalDays());
            preparedStatement.setDouble(6, task.getTotalPrice());
            preparedStatement.setDate(7, task.getDeadline());
            preparedStatement.setDate(8, task.getStartDate());
            preparedStatement.setInt(9, task.getProjectID());
            preparedStatement.setInt(10, task.getSubProjectID());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<Task> readAllTasksBelongingToProject(int projectID){
        ArrayList<Task> taskList = new ArrayList<>();

        for(ModelInterface assignmentObject : super.readAllAssignmentsBelongingToProject("task","task",Task::new,projectID)) {
            if (assignmentObject instanceof Task) {
                Task task = (Task) assignmentObject; //typecasting.

                int subProjectID = super.getTableIntByInt("task", "sub_project_id", "task_id", task.getID()); //get subprojectID
                //fix hours/totalhours + days/totaldays
                int totalHours = task.getHours();
                int totalDays = task.getDays();
                int hours = super.getTableIntByInt("task", "task_hours", "task_id", task.getID()); //get hours
                int days = super.getTableIntByInt("task", "task_days", "task_id", task.getID()); //get hours

                task.setHours(hours); //set hours
                task.setTotalHours(totalHours); //set total hours
                task.setDays(days);
                task.setTotalDays(totalDays);

                task.setSubProjectID(subProjectID); //set subprojectID

                taskList.add(task); //add task to tasklist.
            }
        }
        return taskList;
    }

    public List<Task> readAllTasksForSubProject(int projectID, int subProjectID){

        List <ModelInterface> allTasksOnProject = readAllAssignmentsBelongingToProject("task", //holds tasks temporarily.
                "task",Task::new,projectID);
        List <Task> allTasksForSubProject = new ArrayList<>(); // will be returned.

        for (ModelInterface projectTask : allTasksOnProject){
            if (projectTask instanceof Task){
                Task task = (Task) projectTask; //typecast projecttask as task.

                //set subprojectID and projectID
                task.setProjectID(super.getTableIntByInt("task", "project_id", "task_id", task.getID()));
                task.setSubProjectID(super.getTableIntByInt("task", "sub_project_id", "task_id", task.getID()));


                if (task.getSubProjectID() == subProjectID) {
                    allTasksForSubProject.add(task);//add task to return list.
                }
            }// end of "if (projectTask instanceof Task)"
        }//end of for loop.

        return allTasksForSubProject;
    }

    public Task readTask(int taskID){

         Task task = (Task) super.readAssignmentByID("task","task",Task::new,taskID); //read task using super class.
        task.setProjectID(super.getTableIntByInt("task", "project_id", "task_id", task.getID()));
        task.setSubProjectID(super.getTableIntByInt("task", "sub_project_id", "task_id", task.getID()));
        return task;
    }


    public int deleteTask(int taskID){
        try {
            conn.setAutoCommit(false);
            super.deleteObjectFromTable("task", "task", taskID);
            super.deleteAllWhere("sub_task", "task_id = " + taskID);
            super.deleteAllWhere("project_employee_task_subTask", "task_id = " + taskID);
            super.deleteAllWhere("project_employee_task", "task_id = " + taskID);
            conn.commit();
            conn.setAutoCommit(true);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalHoursForTask(Task task){

        int totalHoursForTask  = super.getTableIntByInt("task", "task_hours", "task_id", task.getID()); //get task hours.

        //if total hours havn't been set.
        if (totalHoursForTask == -1){
            totalHoursForTask = task.getHours(); //set task hours.
        }else {

            List<ModelInterface> allSubTasks = super.readAllAssignments("sub_task", "sub_task", SubTask::new);//get All subtasks.

            for (ModelInterface modelInterface : allSubTasks) {
                SubTask subTask = (SubTask) modelInterface; //typecasting.
                subTask.setTaskID(getTableIntByInt("sub_task", "task_id", "sub_task_id", subTask.getID()));//set task id for subtask.

                if (subTask.getTaskID() == task.getID()) {
                    totalHoursForTask += subTask.getHours(); //add subtask-specific hours to total.
                }
            }//end of all subtasks.
        }

        task.setTotalHours(totalHoursForTask);
        // updateTotalHoursForTask(task.getID(), totalHoursForTask);
        return totalHoursForTask;
    }

    public boolean updateTotalHoursForTask(int taskID, int newTotalHoursForTask){
        return super.updateInt("task", "task_total_hours", taskID, newTotalHoursForTask);
    }

    public boolean updateHoursForTask(Task task, int newHoursForTask){
        task.setHours(newHoursForTask); //set Task hours.
        int totalHours = getTotalHoursForTask(task); //get new total hours.
        
        return updateTotalHoursForTask(task.getID(), totalHours); //update total hours for task.
    }

    public boolean updateTaskPrice(int taskID, double newPrice){
        return super.updateDouble("task", "task_total_price", taskID, newPrice);
    }

    public boolean updateTask(Task task){
        String sql = "UPDATE task SET task_Name = ?, task_Total_Hours = ?, task_Total_Days = ?, task_Hours = ?, task_Days = ?, task_Total_Price = ?, task_Deadline = ?,task_Start_Date =? WHERE task_ID = ?";

        try{
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, task.getName());
            preparedStatement.setInt(2, task.getTotalHours());
            preparedStatement.setInt(3, task.getTotalDays());
            preparedStatement.setInt(4, task.getHours());
            preparedStatement.setInt(5, task.getDays());
            preparedStatement.setDouble(6, task.getTotalDays());
            preparedStatement.setDate(7, task.getDeadline());
            preparedStatement.setDate(8, task.getStartDate());
            preparedStatement.setInt(9, task.getID());

            preparedStatement.executeUpdate();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;

    }


}



