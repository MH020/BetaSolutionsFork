package org.example.betasolutions.subTask;

import org.example.betasolutions.BudgetManager;
import org.example.betasolutions.task.Task;
import org.example.betasolutions.task.TaskService;
import org.example.betasolutions.TimeManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubTaskService {

    private final TaskService taskService;
    private final TimeManager timeManager;
    private SubTaskRepository subTaskRepository;

    public SubTaskService(SubTaskRepository subTaskRepository, TimeManager timeManager, TaskService taskService){
        this.subTaskRepository = subTaskRepository;
        this.taskService = taskService;
        this.timeManager = timeManager;
    }

    public void createSubTask(SubTask subTask){
        calculateDeadline(subTask);
        subTaskRepository.addSubTaskToTask(subTask);
        //taskService.updateTaskTotalHours(subTask.getTaskID()); //update on task.
        updateSubTaskTotalHours(subTask, subTask.getHours());
    }

    public List<SubTask> readAllSubTasks(int ProjectID, int TaskID){
        return subTaskRepository.readAllSubTasks(ProjectID, TaskID);
    }

    public void deleteSubTask(int subTaskID){
        subTaskRepository.deleteSubTask(subTaskID);
    }

    public void calculateDeadline(SubTask subTask){
        subTask.setTotalDays(timeManager.calculateDays(subTask.getHours()));
        subTask.setDeadline(timeManager.calculateEndDate(subTask.getStartDate(), subTask.getDays()));
    }

    public void updateSubTaskTotalHours(SubTask subTask, int totalHours){
        subTask.setHours(totalHours); //update on object.
        subTaskRepository.updateSubTaskTotalHours(subTask); //update on database
        updateSubTaskPrice(totalHours, subTask); //update price on database

        taskService.updateTaskTotalHours(subTask.getTaskID()); //update on task.
    }

    public void updateSubTaskPrice(int hours, SubTask subTask){
        BudgetManager budgetManager = new BudgetManager();
        double price = budgetManager.calculateCost(hours);

        subTaskRepository.updateSubTaskPrice(subTask, price); //update on database.
    }
}
