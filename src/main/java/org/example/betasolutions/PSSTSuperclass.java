package org.example.betasolutions;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PSSTSuperclass {
    protected final Connection conn;

    public PSSTSuperclass(ConnectionManager connectionManager) {
        this.conn = connectionManager.getConnection();
    }

    //create method
    public int create(ModelInterface object, String tableName,String name,int hours, int days, int totalPrice) {
        String sql = "insert into " + tableName + " (" + name + ") values(?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setInt(2, hours);
            preparedStatement.setInt(3, days);
            preparedStatement.setInt(4, totalPrice);
            preparedStatement.setDate(5, object.getEndDate());
            preparedStatement.setDate(6, object.getStartDate());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
    }
        return 0;
    }
    // Read Method
    public List<ModelInterface object> readAll(String tableName, int EmployeeID,Object model) {
        List<Object> allObjects = new ArrayList<>();
        String sql = "select * from " + tableName + " where employee_id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, EmployeeID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int hours = resultSet.getInt("hours");
                int days = resultSet.getInt("days");
                int totalPrice = resultSet.getInt("total_price");
                Date endDate = resultSet.getDate("end_date");
                Date startDate = resultSet.getDate("start_date");

                allObjects.add(new object(name, hours, days, totalPrice, endDate, startDate));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allObjects;
    }

    public List<String> read(String tableName, String idName, int id, Object model){
        String sql = "SELECT * FROM " + tableName + " WHERE " + idName + " =?";
        try{
            PreparedStatement prepstatement = conn.prepareStatement(sql);
            prepstatement.setInt(1, id);
            ResultSet resultSet = prepstatement.executeQuery();



        }catch(SQLException e){

        }

    }

    /*
    read
    insert
    delete
    update
     */


}
