package org.example.betasolutions.project;

import org.example.betasolutions.BudgetManager;
import org.example.betasolutions.FactoryInterface;
import org.example.betasolutions.ModelInterface;
import org.example.betasolutions.TimeManager;
import org.example.betasolutions.subProject.SubProject;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    public void insertAssignmentIntoTable(Project project){
        updateProjectVariables(project, Date.valueOf(LocalDate.now()),  0);

        //add project on database:
        projectRepository.insertAssignmentIntoTable(project);
    }

    public void updateProjectVariables(Project project, Date startDate, int hours){
        //calculate variables.
        TimeManager timeManager = new TimeManager();
        int days = timeManager.calculateDays(hours);
        Date deadline = timeManager.calculateEndDate(startDate, days);

        //set variables on project:
        project.setStartDate(startDate);
        project.setTotalHours(hours);
        project.setTotalDays(days);
        project.setDeadline(deadline);
    }
    public List<Project> readAllProjects(){
       return projectRepository.readAllProjects();
    }
    public Project readProjectByID(int project_id){
        return projectRepository.readProjectByID(project_id);
    }
    public void updateProject(Project project,int project_id){
        projectRepository.updateProject(project,project_id);
    }

    public void updateProjectTotalHours(int projectID){
        Project project = projectRepository.readProjectByID(projectID); //read project.
        int totalHours = projectRepository.getTotalHoursForProject(project);//get total hours
        updateProjectVariables(project, project.getStartDate(), totalHours); //update time variables on object.
        projectRepository.updateProject(project, projectID); //update on database.

        updateProjectPrice(totalHours, project);
    }

    public void deleteProject(int project_id){
        projectRepository.deleteProject(project_id);
    }

    public void updateProjectPrice(int hours, Project project){
        BudgetManager budgetManager = new BudgetManager();
        double price = budgetManager.calculateCost(hours);

        projectRepository.updateProjectPrice(project, price); //update on database.
    }
}
