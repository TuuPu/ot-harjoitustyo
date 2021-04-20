/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productivitytracker.ptdatabase;

import productivitytracker.course.Course;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;
/**
 *
 * @author tuukkapuonti
 */
public class PTDataBase {
    
    private Connection db = null;
    private Statement statement = null;
    
    private DecimalFormat numberFormat;   
    
    private double totalTime;   
    private double previousStudies;
    
    
    public PTDataBase() throws SQLException {
        numberFormat = new DecimalFormat("0.00");
        try {
            this.db = DriverManager.getConnection("jdbc:sqlite:productivitytracker.db");                
            statement = db.createStatement();    
            setTables();
            System.out.println("Connection to database succesful");
        } catch (SQLException e) {
            System.out.println("Database already exists... Moving on.");
        }
    }
    
    private void setTables() throws SQLException {       
        try {
            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute("CREATE TABLE Courses (id INTEGER PRIMARY KEY, course TEXT NOT NULL UNIQUE, grade INTEGER, goalTime REAL, coursePoints REAL)");
            statement.execute("CREATE TABLE Daily (id INTEGER PRIMARY KEY, course_id TEXT, date TEXT, dayOfWeek TEXT, timeForDay REAL, courseForDay TEXT)");
            System.out.println("Database created");
        } catch (SQLException e) {
            System.out.println("Tables have been created");
        }
    }
    
    public double getCoursePoints(String course)throws SQLException {
        PreparedStatement prepared;
        try {
            prepared = db.prepareStatement("SELECT Courses.coursePoints FROM Courses WHERE Courses.course=?");
            prepared.setString(1, course);
            ResultSet res = prepared.executeQuery();
            return res.getDouble("coursePoints");
        } catch (SQLException e) {          
        }
        return 0;
    }
    
    public void setGrade(int grade, String course)throws SQLException {
        PreparedStatement prepared;
        try {
            prepared = db.prepareStatement("UPDATE Courses SET grade=? WHERE course=?");
            prepared.setInt(1, grade);
            prepared.setString(2, course);
            prepared.executeUpdate();
        } catch (SQLException e) {

        }
    }
    
    public ArrayList getCourses()throws SQLException {
        PreparedStatement prepared;
        ArrayList<String> list = new ArrayList<>();
        try {
            prepared = db.prepareStatement("SELECT Courses.course FROM Courses");
            ResultSet res = prepared.executeQuery();
            while (res.next()) {
                list.add(res.getString("course"));              
            }
        } catch (SQLException e) {
           
        }
        return list;
    }
    
    public double getDailyGoal(String course)throws SQLException {
        PreparedStatement prepared;
        try {
            prepared = db.prepareStatement("SELECT Courses.goalTime FROM Courses WHERE Courses.course=?");
            prepared.setString(1, course);
            ResultSet res = prepared.executeQuery();
            return res.getDouble("goalTime");
        } catch (SQLException e) {
            
        }
        return 0;
    }
    
    public void setCoursePointsAndGoal(String course, double goal, double points)throws SQLException {
        PreparedStatement prepared;
        try {
            prepared = db.prepareStatement("INSERT INTO Courses (course, goalTime, coursePoints) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            prepared.setString(1, course);
            prepared.setDouble(2, goal);
            prepared.setDouble(3, points);
            prepared.executeUpdate();
        } catch (SQLException e) {
            
        }
    }
    
    public void getSessionTimeForDB(String date, String day, String course, double sessionTime)throws SQLException {
        PreparedStatement prepared;
        this.previousStudies = 0;
        try {
            prepared = db.prepareStatement("SELECT timeForDay FROM Daily WHERE Daily.courseForDay=? AND Daily.dayOfWeek=? AND daily.date=?");
            prepared.setString(1, course);
            prepared.setString(2, day);
            prepared.setString(3, date);
            ResultSet res = prepared.executeQuery();
            if (res.getDouble("timeForDay") == 0) {
                this.previousStudies = 0;
            } else {
                this.previousStudies = res.getDouble("timeForDay");
            }
        } catch (SQLException e) {
        }
    }
    
    public void setDailyTime(String date, String day, String course, double sessionTime)throws SQLException {
        PreparedStatement prepared;
        PreparedStatement select;
        try {
            select = db.prepareStatement("SELECT Courses.id FROM Courses WHERE Courses.course=?");
            select.setString(1, course);
            ResultSet rescourse = select.executeQuery();
            int id = rescourse.getInt("id");
            ResultSet push = select.getGeneratedKeys();
            if (push.next()) {
                PreparedStatement insert = db.prepareStatement("INSERT INTO Daily (course_id, date, dayOfWeek, timeForDay, courseForDay) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                insert.setInt(1, id);
                insert.setString(2, date);
                insert.setString(3, day);
                this.previousStudies = this.previousStudies + sessionTime;
                insert.setDouble(4, this.previousStudies);
                insert.setString(5, course);
                insert.executeUpdate();
            }
        } catch (SQLException e) {         
        }
    }
    
    public double getStudyTimesByCourse(String course)throws SQLException {
        double total = 0;
        PreparedStatement prepared;
        try {
            prepared = db.prepareStatement("SELECT Daily.timeForDay FROM Daily WHERE Daily.courseForDay=?");
            prepared.setString(1, course);
            ResultSet res = prepared.executeQuery();
            while (res.next()) {
                total = total + res.getDouble("timeForDay");
            }
            return total;
        } catch (SQLException e) {           
        }
        return total;
    }
    
    public double getStudyTimeByDay(String day)throws SQLException {
        PreparedStatement prepared;
        double total = 0;
        try {
            prepared = db.prepareStatement("SELECT Daily.timeForDay FROM Daily WHERE Daily.dayOfWeek=?");
            prepared.setString(1, day);
            ResultSet res = prepared.executeQuery();
            while (res.next()) {
                total = total + res.getDouble("timeForDay");
            }
            return total;
        } catch (SQLException e) {           
        }
        return total;
    }
    
    public double getStudyTimeByDate(String date)throws SQLException {
        PreparedStatement prepared;
        double total = 0;
        try {
            prepared = db.prepareStatement("SELECT Daily.timeForDay FROM Daily WHERE Daily.date=?");
            prepared.setString(1, date);
            ResultSet res = prepared.executeQuery();
            while (res.next()) {
                total = total + res.getDouble("timeForDay");
            }
            return total;
        } catch (SQLException e) {
            
        }
        return total;
    }
       
    public void close()throws SQLException {
        try {
            db.close();
        } catch (SQLException e) {           
        }
    }  
}