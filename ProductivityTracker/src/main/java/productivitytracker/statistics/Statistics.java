/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productivitytracker.statistics;

import productivitytracker.course.Course;
import productivitytracker.ptdatabase.PTDataBase;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
/**
 *
 * @author tuukkapuonti
 */
public class Statistics {
    PTDataBase data;
    private double mean;
    private double std;
    private double median;
    private double total;
    private DecimalFormat formatter;
    private ArrayList<String> dayList;
   

    
    public Statistics(PTDataBase courseData)throws SQLException {
        this.dayList = new ArrayList<String>();
        this.dayList.add("Mon");
        this.dayList.add("Tue");
        this.dayList.add("Wed");
        this.dayList.add("Thu");
        this.dayList.add("Fri");
        this.dayList.add("Sat");
        this.dayList.add("Sun");  
        this.data = courseData;
        this.mean = 0.0;
        this.std = 0.0;
        this.median = 0.0;
        this.total = 0.0;
    }
    
    //Jaa kahteen metodiin? Listan kasaus ja palautus metodille calulcateMean?
    public double getMean(String type, int identifier)throws SQLException {
        ArrayList<Double> list = new ArrayList<Double>();       
        try {
            if (identifier == 0) {       
                list = data.getTimeListByCourse(type);
            }
            if (identifier == 1) {
                list = data.getTimeListByDay(type);
            }
            if (identifier == 2) {
                list = data.getTimeListByDate(type);
            }
            DescriptiveStatistics stats = new DescriptiveStatistics();
            for (int i = 0; i < list.size(); i++) {
                stats.addValue(list.get(i));
            }
            return stats.getMean();
        } catch (SQLException e) {
            
        }
        return 0.0;
    }
    
    public double getSTD(String type, int identifier)throws SQLException {
        ArrayList<Double> list = new ArrayList<Double>();
        try {
            if (identifier == 0) {
                list = data.getTimeListByCourse(type);
            }
            if (identifier == 1) {
                list = data.getTimeListByDay(type);
            }
            if (identifier == 2) {
                list = data.getTimeListByDate(type);
            }
            DescriptiveStatistics stats = new DescriptiveStatistics();
            for (int i = 0; i < list.size(); i++) {
                stats.addValue(list.get(i));
            }
            return stats.getStandardDeviation();
        } catch (SQLException e) {
            
        }
        return 0.0;
    }
    
    public double getMedian(String type, int identifier)throws SQLException {
        ArrayList<Double> list = new ArrayList<Double>();
        try {
            if (identifier == 0) {
                list = data.getTimeListByCourse(type);
            }
            if (identifier == 1) {
                list = data.getTimeListByDay(type);
            }
            if (identifier == 2) {
                list = data.getTimeListByDate(type);
            }
            DescriptiveStatistics stats = new DescriptiveStatistics();
            for (int i = 0; i < list.size(); i++) {
                stats.addValue(list.get(i));
                
            }
            return stats.getPercentile(50);
        } catch (SQLException e) {
            
        }
        return 0.0;
    }
    
    public double getTotal(String type, int identifier)throws SQLException {
        try {
            if (identifier == 0) {
                return data.getStudyTimesByCourse(type);
            }
            if (identifier == 1) {
                return data.getStudyTimeByDay(type);
            }
            if (identifier == 2) {
                return data.getStudyTimeByDate(type);
            }           
        } catch (SQLException e) {           
        }
        return 0.0;
    }
    
    public LinkedHashMap getTotalHoursPerDay()throws SQLException {
        LinkedHashMap<String, Double> perDay = new LinkedHashMap<String, Double>();
        try {
            for (int i = 0; i < this.dayList.size(); i++) {
                perDay.put(this.dayList.get(i), this.data.getStudyTimeByDay(this.dayList.get(i)));
            }
            return perDay;
        } catch (SQLException e) {
            
        }
        return perDay;
    }
    
    public LinkedHashMap getAvgStudyAgainstGoal(String course)throws SQLException {
        double goalTime = 0;
        goalTime = data.getDailyGoal(course);
        LinkedHashMap<String, Double> avgMap = new LinkedHashMap<>();
        for (int i = 0; i < this.dayList.size(); i++) {
            avgMap.put(this.dayList.get(i), (data.getTimeByDayAndCourse(this.dayList.get(i), course).getMean() / goalTime));
        }
        return avgMap;       
    }
    

}
