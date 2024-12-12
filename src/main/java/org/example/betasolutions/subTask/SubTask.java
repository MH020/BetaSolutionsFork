package org.example.betasolutions.subTask;

import org.example.betasolutions.ModelInterface;
import org.example.betasolutions.TimeManager;

import java.sql.Date;

public class SubTask implements ModelInterface {
    private int subTaskID;
    private String name;
    private int hours;
    private int totalDays;
    private double totalPrice;
    private Date subTaskDeadline;
    private Date subTaskStartDate;
    private int taskID;

    //empty
    public SubTask() {
    }

    //set all values.
    public SubTask (String name, int hours, double price, Date startDate, int taskID){
        this.name = name;
        totalPrice = price;
        subTaskStartDate = startDate;

        setHours(hours);

        this.taskID = taskID;
    }

    //factory
    public SubTask (int subTaskID, String name, int hours, int days, double price, Date deadLine , Date startDate){
        this.subTaskID = subTaskID;
        this.name = name;
        this.hours = hours;
        totalDays = days;
        totalPrice = price;
        subTaskStartDate = startDate;
        subTaskDeadline = deadLine;

    }
    
    /*
    public SubTask(int subTaskID, String subTaskName, int subTaskTotalHours, int subTaskTotalDays, double subTaskTotalPrice, Date subTaskDeadline, Date subTaskStartDate, int taskID) {
        this.subTaskID = subTaskID;
        this.subTaskName = subTaskName;
        this.subTaskTotalHours = subTaskTotalHours;
        this.subTaskTotalDays = subTaskTotalDays;
        this.subTaskTotalPrice = subTaskTotalPrice;
        this.subTaskDeadline = subTaskDeadline;
        this.subTaskStartDate = subTaskStartDate;
        this.taskID = taskID;
    }
    public SubTask(int subTaskID, String subTaskName, int subTaskTotalHours, int subTaskTotalDays, double subTaskTotalPrice, Date subTaskDeadline, Date subTaskStartDate) {
        this.subTaskID = subTaskID;
        this.subTaskName = subTaskName;
        this.subTaskTotalHours = subTaskTotalHours;
        this.subTaskTotalDays = subTaskTotalDays;
        this.subTaskTotalPrice = subTaskTotalPrice;
        this.subTaskDeadline = subTaskDeadline;
        this.subTaskStartDate = subTaskStartDate;
    }
*/
    public int getID() {
        return subTaskID;
    }

    public void setSubTaskID(int subTaskID) {
        this.subTaskID = subTaskID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHours() {
        return hours;
    }

    /*
    public void setSubTaskTotalHours(int subTaskTotalHours) {
        this.subTaskTotalHours = subTaskTotalHours;
    }
*/

    public void setHours(int hours){
        try {
        this.hours = hours;

        TimeManager timeManager = new TimeManager();

        totalDays = timeManager.calculateDays(hours);
        subTaskDeadline = timeManager.calculateEndDate(subTaskStartDate, totalDays);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDeadline() {
        return subTaskDeadline;
    }

    public void setSubTaskDeadline(Date subTaskDeadline) {
        this.subTaskDeadline = subTaskDeadline;
    }

    public Date getStartDate() {
        return subTaskStartDate;
    }
    public void setSubTaskStartDate(Date subTaskStartDate) {
        this.subTaskStartDate = subTaskStartDate;
    }
    public int getTaskID() {
        return taskID;
    }
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
