/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pttests;

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
import productivitytracker.statistics.Statistics;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.*;

/**
 *
 * @author tuukkapuonti
 */
public class StatisticsTest {
    private PTDataBase testBase;
    private Statistics stats;
    public StatisticsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws SQLException{
        this.testBase = new PTDataBase(true);
        this.stats = new Statistics(this.testBase);
    }
    
    @After
    public void tearDown() throws SQLException{
        this.testBase.deleteDb();
    }

    @Test
    public void testGetMean() throws SQLException{
        assertEquals(0, (int)stats.getMean("tira", 0));
        testBase.setCoursePointsAndGoal("tira", 5, 5);
        testBase.setDailyTime("01.01.2021", "Mon", "tira", 5);
        assertEquals(5, (int)stats.getMean("tira", 0));
        assertEquals(5, (int)stats.getMean("Mon", 1));
        assertEquals(5, (int)stats.getMean("01.01.2021", 2));
    }
    
    @Test
    public void testGetMedian() throws SQLException {
        assertEquals(0, (int)stats.getMedian("tira", 0));
        testBase.setCoursePointsAndGoal("tira", 5, 5);
        testBase.setDailyTime("01.01.2021", "Mon", "tira", 5);
        assertEquals(5, (int)stats.getMedian("Mon", 1));
        assertEquals(5, (int)stats.getMedian("01.01.2021", 2));       
    }
    
    @Test
    public void testGetSTD() throws SQLException {
        assertEquals(0, (int)stats.getSTD("tira", 0));
        testBase.setCoursePointsAndGoal("tira", 5, 5);
        testBase.setDailyTime("01.01.2021", "Mon", "tira", 5);
        assertEquals(0, (int)stats.getSTD("Mon", 1));
        assertEquals(0, (int)stats.getSTD("01.01.2021", 2));
    }
    
    @Test
    public void testGetTotal() throws SQLException {
        assertEquals(0, (int)stats.getTotal("tira", 0));
        testBase.setCoursePointsAndGoal("tira", 5, 5);
        testBase.setDailyTime("01.01.2021", "Mon", "tira", 5);
        assertEquals(5, (int)stats.getTotal("tira",0));
        assertEquals(5, (int)stats.getTotal("Mon", 1));
        assertEquals(5, (int)stats.getTotal("01.01.2021", 2));
    }
    
    @Test
    public void testOverLimit() throws SQLException {
        testBase.setCoursePointsAndGoal("tira", 1, 1);
        testBase.setDailyTime("01.01.2021", "Mon", "tira", 2000);      
        assertTrue(stats.isOverLimit("tira"));
    }
    
    @Test
    public void TotalByGrade() throws SQLException {
        testBase.setCoursePointsAndGoal("tira", 5, 5);
        testBase.setGrade(5, "tira");
        testBase.setDailyTime("01.01.2021", "Mon", "tira", 5);
        HashMap<Integer, Double> test = new HashMap<>();
        test.put(5, 5.0);
        assertEquals(test, stats.getTotalHoursByGrade());
    }
    
    @Test
    public void getAvgStudyAgainstGoal() throws SQLException {
        testBase.setCoursePointsAndGoal("tira", 5, 5);
        testBase.setDailyTime("01.01.2021", "Mon", "tira", 5);
        LinkedHashMap<String, Double> list = new LinkedHashMap<>();
        list.put("Mon", 1.0);
        list.put("Tue", Double.NaN);
        list.put("Wed", Double.NaN);
        list.put("Thu", Double.NaN);
        list.put("Fri", Double.NaN);
        list.put("Sat", Double.NaN);
        list.put("Sun", Double.NaN);
        assertEquals(list, stats.getAvgStudyAgainstGoal("tira"));
    }
    
    @Test
    public void getTotalPerDay() throws SQLException{
        testBase.setCoursePointsAndGoal("tira", 5, 5);
        testBase.setDailyTime("01.01.2021", "Mon", "tira", 5);
        LinkedHashMap<String, Double> list = new LinkedHashMap<>();
        list.put("Mon", 5.0);
        list.put("Tue", 0.0);
        list.put("Wed", 0.0);
        list.put("Thu", 0.0);
        list.put("Fri", 0.0);
        list.put("Sat", 0.0);
        list.put("Sun", 0.0);
        assertEquals(list, stats.getTotalHoursPerDay());
    }
            
}


