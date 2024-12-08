package org.example.betasolutions.subTask;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SubTaskController {
    private SubTaskService subTaskService;

    public SubTaskController(SubTaskService subTaskService){
        this.subTaskService = subTaskService;
    }

    @GetMapping("/project/{projectID}/task/{taskID}/subtasks")
    public String SubTasksforTask(Model model,@PathVariable ("projectID") int projectID, @PathVariable ("taskID") int taskID){
        model.addAttribute("subtasks", subTaskService.readAllSubTasks(projectID, taskID));
        return "subtaskpage";
    }

    @PostMapping("project/subtask/edit")
    public String editSubTask(){
        return "redirect:/project";
    }

    @PostMapping("project/subtask/delete")
    public String deleteSubTask(){
        return "redirect:/project";
    }
    @PostMapping("/project/subtask/new")
    public String createNewSubTask(){
        return "redirect:/project";
    }
}
