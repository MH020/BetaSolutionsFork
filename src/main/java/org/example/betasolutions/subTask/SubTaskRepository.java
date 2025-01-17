package org.example.betasolutions.subTask;

import org.example.betasolutions.ConnectionManager;
import org.example.betasolutions.AssignmentRepository;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SubTaskRepository extends AssignmentRepository {

    public SubTaskRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    public void addSubTaskToTask(SubTask subTask){
        String sql = "INSERT INTO sub_task " +
                "(sub_task_name, sub_task_total_hours,sub_task_total_days,sub_task_total_price,sub_task_deadline,sub_task_start_date,task_id) " +
                "VALUES(?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement = super.insertAssignmentIntoTable(subTask,sql);

        try{
            preparedStatement.setInt(7,subTask.getTaskID()); //set task id for subtask.
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public List<SubTask> readAllSubTasks(int ProjectID, int TaskID){
        ArrayList<SubTask> subTaskList = new ArrayList<>();
        String SQL ="SELECT *  FROM sub_task " +
                "JOIN task ON sub_task.task_id = task.task_id " +
                "WHERE task.project_id = ? AND task.task_id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(SQL);
            preparedStatement.setInt(1,ProjectID);
            preparedStatement.setInt(2,TaskID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                SubTask subTask = new SubTask();
                subTask.setSubTaskID(resultSet.getInt("sub_task_id"));
                subTask.setName(resultSet.getString("sub_task_name"));
                subTask.setStartDate(resultSet.getDate("sub_task_start_date"));
                subTask.setHours(resultSet.getInt("sub_task_total_hours"));
                subTask.setTotalDays(resultSet.getInt("sub_task_total_days"));
                subTask.setTotalPrice(resultSet.getDouble("sub_task_total_price"));
                subTask.setDeadline(resultSet.getDate("sub_task_deadline"));
                subTask.setTaskID(resultSet.getInt("task_id"));
                subTaskList.add(subTask);
            }//end of while.
        }catch (SQLException e){
            e.printStackTrace();
        }

        return subTaskList;
    }

    public boolean updateSubTaskTotalHours(SubTask subTask) {
        return updateObjectInt("sub_task", "sub_task_total_hours", subTask.getID(), subTask.getHours());
    }

    public boolean updateSubTaskPrice(SubTask subTask, double price){
        return super.updateDouble("sub_task", "sub_task_total_price", subTask.getID(), price);
    }

    public void deleteSubTask(int subTaskID) {
        try {
            conn.setAutoCommit(false);
            super.deleteAllWhere("sub_task", "sub_task_id = " + subTaskID);
            super.deleteAllWhere("project_employee_task_subTask", "sub_task_id = " + subTaskID);
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
