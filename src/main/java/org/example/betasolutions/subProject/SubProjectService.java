package org.example.betasolutions.subProject;

import org.example.betasolutions.BudgetManager;
import org.example.betasolutions.project.Project;
import org.example.betasolutions.project.ProjectService;
import org.example.betasolutions.TimeManager;

import org.example.betasolutions.subTask.SubTask;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class SubProjectService {
    private final ProjectService projectService;
    private SubProjectRepository subProjectRepository;
    private TimeManager timeManager;

    public SubProjectService(SubProjectRepository subProjectRepository, ProjectService projectService){
        this.subProjectRepository = subProjectRepository;
        this.projectService = projectService;
        timeManager = new TimeManager();
    }

    public void insertIntoSubProject(SubProject subProject){
        updateProjectVariables(subProject, Date.valueOf(LocalDate.now()),  0);
        //updateSubProjectTotalHours(subProject.getID(), 0);
        subProjectRepository.insertSubProject(subProject);
    }

    public List<SubProject> readAllSubProjects(int projectID){
       return subProjectRepository.readAllSubProjects(projectID);
    }

    public void readSubProjectByID(int subProjectID){
        subProjectRepository.readSubProject(subProjectID);
    }

    public boolean deleteSubProject(int subProjectID){
        return subProjectRepository.deleteSubProject(subProjectID);
    }

    public void updateSubProjectTotalHours(int subProjectID){
        SubProject subProject = subProjectRepository.readSubProject(subProjectID); //read subproject.
        int totalHours = subProjectRepository.getTotalHoursForSubProject(subProject); //get totalHours.

        subProject.setHours(totalHours);//total hours.
        calculateDeadline(subProject);//update on object.

        subProjectRepository.updateSubProjectTotalHours(subProject, totalHours); //update on database.
        updateSubProjectPrice(totalHours, subProject); //update total price on database.

        projectService.updateProjectTotalHours(subProject.getProjectID()); //update project.
    }

    public void calculateDeadline(SubProject subProject){
        subProject.setDays(timeManager.calculateDays(subProject.getHours()));
        subProject.setDeadline(timeManager.calculateEndDate(subProject.getStartDate(), subProject.getDays()));
    }

    public void updateSubProjectPrice(int hours, SubProject subProject){
        BudgetManager budgetManager = new BudgetManager();
        double price = budgetManager.calculateCost(hours);

        subProjectRepository.updateSubProjectPrice(subProject, price); //update on database.
    }
    public void updateProjectVariables(SubProject SubProject, Date startDate, int hours){
        //calculate variables.
        TimeManager timeManager = new TimeManager();
        int days = timeManager.calculateDays(hours);
        Date deadline = timeManager.calculateEndDate(startDate, days);

        //set variables on project:
        SubProject.setStartDate(startDate);
        SubProject.setHours(hours);
        SubProject.setDays(days);
        SubProject.setDeadline(deadline);
    }
}
