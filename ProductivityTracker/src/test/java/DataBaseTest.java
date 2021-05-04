/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.time.*;
import java.time.format.*;
import java.util.concurrent.TimeUnit;
import junit.framework.Assert;
import productivitytracker.course.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import productivitytracker.ptdatabase.PTDataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author tuukkapuonti
 */
public class DataBaseTest {
    private PTDataBase baseTest;
    
    public DataBaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws SQLException {
        this.baseTest = new PTDataBase(true);
    }
    
    @After
    public void tearDown() throws SQLException {
        baseTest.deleteDb();
    }

    @Test
    public void setGradeTest() throws SQLException{
        assertEquals(0, baseTest.getGrade("tira"));
        baseTest.setCoursePointsAndGoal("tira", 1, 5);
        baseTest.setGrade(5, "tira");
        assertEquals(5, baseTest.getGrade("tira"));
    }
    
    @Test
    public void setCoursePointsAndGoalTest() throws SQLException {
        assertEquals("[]", baseTest.getCourses().toString());
        assertEquals(0,0, baseTest.getDailyGoal("tira"));
        assertEquals(0,0, baseTest.getCoursePoints("tira"));
        baseTest.setCoursePointsAndGoal("tira", 1, 5);
        assertEquals("[tira]", baseTest.getCourses().toString());
        assertEquals(1,0, baseTest.getDailyGoal("tira"));
        assertEquals(5,0, baseTest.getCoursePoints("tira"));
    }
    
    @Test
    public void setDailyTimeTest() throws SQLException {
        assertEquals(0,0, baseTest.getStudyTimeByDate("01.04.2021"));
        assertEquals(0,0, baseTest.getStudyTimeByDay("Mon"));
        assertEquals(0,0, baseTest.getStudyTimesByCourse("tira"));
        baseTest.setCoursePointsAndGoal("tira", 1, 5);
        baseTest.setDailyTime("01.04.2021", "Mon", "tira", 1.0);
        assertEquals(1,0, baseTest.getStudyTimeByDate("01.04.2021"));
        assertEquals(1,0, baseTest.getStudyTimeByDay("Mon"));
        assertEquals(1,0, baseTest.getStudyTimesByCourse("tira"));
    }
    
    @Test
    public void getWeekdaysTest() throws SQLException {
        baseTest.setCoursePointsAndGoal("tira", 0, 0);
        baseTest.setDailyTime("01.05.2021", "Mon", "tira", 0);
        ArrayList<String> weekday = new ArrayList<>();
        weekday.add("Mon");
        assertEquals(weekday, baseTest.getWeekDays());
    }
    
    @Test
    public void getDateTest() throws SQLException {
        baseTest.setCoursePointsAndGoal("tira", 0, 0);
        baseTest.setDailyTime("01.05.2021", "Mon", "tira", 0);
        ArrayList<String> date = new ArrayList<>();
        date.add("01.05.2021");
        assertEquals(date, baseTest.getDates());
    }
    
    @Test
    public void getTimeListByDayTest() throws SQLException{
        baseTest.setCoursePointsAndGoal("tira",0,0);
        baseTest.setDailyTime("01.05.2021", "Mon", "tira", 1.0);
        ArrayList<Double> time = new ArrayList<>();
        time.add(1.0);
        assertEquals(time, baseTest.getTimeListByDay("Mon"));
    }
    
    @Test
    public void getTimeListByDateTest() throws SQLException{
        baseTest.setCoursePointsAndGoal("tira",0,0);
        baseTest.setDailyTime("01.05.2021", "Mon", "tira", 1.0);
        ArrayList<Double> time = new ArrayList<>();
        time.add(1.0);
        assertEquals(time, baseTest.getTimeListByDate("01.05.2021"));
    }
    
    @Test
    public void getTimeListByCourseTest() throws SQLException{
        baseTest.setCoursePointsAndGoal("tira",0,0);
        baseTest.setDailyTime("01.05.2021", "Mon", "tira", 1.0);
        ArrayList<Double> time = new ArrayList<>();
        time.add(1.0);
        assertEquals(time, baseTest.getTimeListByCourse("tira"));
    }
            
    
    
}
    
