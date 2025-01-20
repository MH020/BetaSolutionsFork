package org.example.betasolutions.task;

import org.example.betasolutions.BudgetManager;
import org.example.betasolutions.TimeManager;

import org.example.betasolutions.project.ProjectService;
import org.example.betasolutions.subProject.SubProjectRepository;
import org.example.betasolutions.subProject.SubProjectService;

import org.example.betasolutions.subTask.SubTask;

import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class TaskService {
    private TaskRepository taskRepository;

    private SubProjectService subProjectService;
    private final ProjectService projectService;
    private TimeManager timeManager;

    public TaskService(TaskRepository taskRepository, SubProjectService subProjectService, ProjectService projectService){
        this.taskRepository = taskRepository;
        this.subProjectService = subProjectService;

        timeManager = new TimeManager();
        this.projectService = projectService;
    }

    public void createTaskForProject(Task task){
        //task.setTotalHours(getTotalHoursForTask(task));//set totalHours, totalDays, and deadline.

        calculateDeadline(task); //set variables temporarily.
        taskRepository.addTaskToProject(task);
        updateTaskTotalHours(task); //update hours.
    }

    public void createTaskForSubProject(Task task){
        //task.setTotalHours(getTotalHoursForTask(task));//set totalHours, totalDays, and deadline.

        calculateDeadline(task); //set variables temporarily.
        taskRepository.addTaskToSubProject(task);
        updateTaskTotalHours(task);
    }

    public int getTotalHoursForTask(Task task){
        return taskRepository.getTotalHoursForTask(task);
    }

    public List<Task> getAllTasksBelongingToProject(int projectID){
        return taskRepository.readAllTasksBelongingToProject(projectID);
    }

    public List <Task> getAllTasksForSubProject(int projectID, int subProjectID){
        return taskRepository.readAllTasksForSubProject(projectID, subProjectID);
    }
    public int deleteTask(int taskID){
        return taskRepository.deleteTask(taskID);

    }

    public Task getTask(int taskID){
        Task task = (Task) taskRepository.readTask(taskID);//readAllAssignmentsBelongingToProject("task", "task", Task::new, projectID).get(taskID - 1);
        return task;
    }

    public void updateHours(Task task, int hours) {
        //task.setTotalHours(hours); //set task hours = new hours;

        int taskID = task.getID();
        int days = timeManager.calculateDays(hours); //calculate days;
        //task.setTotalDays(days);  //set task days.

        //System.out.println("taskservice. start date: " + task.getStartDate());

        Date deadline = timeManager.calculateEndDate(task.getStartDate(), days); //calculate new expected end date for task, using startdate and days.


       // taskRepository.updateTaskHours(taskID, hours);
        //taskRepository.updateTaskDays(taskID, days);
        taskRepository.updateTaskDeadline(taskID, deadline);

    }

    public void calculateDeadline(Task task){
       // task.setTotalHours(taskRepository.getTotalHoursForTask(task));//set total hours
        task.setTotalDays(timeManager.calculateDays(task.getTotalHours())); //set total days

        task.setDays(timeManager.calculateDays(task.getHours())); //set days
        task.setDeadline(timeManager.calculateEndDate(task.getStartDate(), task.getDays())); //set deadline
    }

    public void updateTaskTotalHours(Task task){
        int totalHours = taskRepository.getTotalHoursForTask(task);//set total hours
        task.setTotalHours(totalHours);//set total hours on task.

        calculateDeadline(task); //calculate days and deadline.

        taskRepository.updateTaskTotalHours (task.getID(), totalHours); //update task total hours on database.
        taskRepository.updateTask(task);
        updateTaskPrice(totalHours, task); //update price on database.

        //update hours on project or subproject:
        if (task.getSubProjectID() > 0) {
           subProjectService.updateSubProjectTotalHours(task.getSubProjectID());//task.getTotalHours());
        }else{
           projectService.updateProjectTotalHours(task.getProjectID());
        }
    }


    public void updateTaskTotalHours(int taskID){
        Task task = taskRepository.readTask(taskID); //read task.
        updateTaskTotalHours(task);
    }

    public void updateTaskPrice(int hours, Task task){
        BudgetManager budgetManager = new BudgetManager();
        double price = budgetManager.calculateCost(hours);
        //task.setTotalPrice(price);

        taskRepository.updateTaskPrice(task.getID(), price);

    }

}
