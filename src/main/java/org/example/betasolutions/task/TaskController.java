package org.example.betasolutions.task;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TaskController {
    private final HttpSession session;
    private TaskService taskService;

    public TaskController(TaskService taskService, HttpSession session){
        this.taskService = taskService;
        this.session = session;
    }

    @GetMapping("project/task/{taskID}")
    public String getTask(Model model, @PathVariable int taskID){
        Task task = taskService.getTask(taskID);
        session.setAttribute("task", task);
        System.out.println("task contr. start date: " + task.getStartDate());

        model.addAttribute("task", task);
        model.addAttribute("hours", task.getHours());
        model.addAttribute("projectID", task.getProjectID());
        return "taskpage";
    }

    //this one is for creating a new task on a project
    @GetMapping("/project/{projectID}/New/task")
    public String createNewTaskForProject(Model model, @PathVariable("projectID") int projectID){
        model.addAttribute("projectID", projectID);
        model.addAttribute("task", new Task());
        return "taskpage";
    }

    //this one is for creating a new task on a project
    @PostMapping("project/{projectID}/task/new/post")
    public String createNewTaskForProjectPost(@PathVariable("projectID") int projectID, @ModelAttribute Task task){
        task.setProjectID(projectID);
        task.setHours(task.getHours());

        taskService.createTaskForProject(task); //create task, update total hours for subproject and project.

        return "redirect:/project/" + projectID;
    }

    @GetMapping("/project/{projectID}/subproject/{subProjectID}/New/task")
    public String createNewTaskForSubProject(Model model, @PathVariable("projectID") int projectID, @PathVariable("subProjectID") int subProjectID){
        model.addAttribute("projectID", projectID);
        model.addAttribute("subProjectID", subProjectID);
        model.addAttribute("task", new Task());
        return "taskpageforSubProject";
    }

    //create new task for subproject.
    @PostMapping("/project/{projectID}/subproject/{subProjectID}/New/task/post")
    public String createNewTaskForSubProjectPost(@PathVariable("projectID") int projectID, @PathVariable("subProjectID") int subProjectID,@ModelAttribute Task task){
        task.setProjectID(projectID); //set project id.
        task.setSubProjectID(subProjectID); //set subproject id.

        taskService.createTaskForSubProject(task); //create task for subproject, update total hours for subproject and project.
        return "redirect:/project/" + projectID;
    }

    @PostMapping("project/task/delete")
    public String deleteTask(@RequestParam int taskID, @RequestParam int projectID){
        taskService.deleteTask(taskID);
        return "redirect:/project/"+ projectID ;
    }
}
