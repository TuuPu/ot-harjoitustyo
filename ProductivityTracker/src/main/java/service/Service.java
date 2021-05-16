/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;
import productivitytracker.course.Course;
import productivitytracker.statistics.Statistics;
import productivitytracker.ptdatabase.PTDataBase;
import java.util.*;
import java.sql.*;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.*;
/**
 *
 * @author tuukkapuonti
 */
public class Service {
    ArrayList<String> dayList;
    PTDataBase base;
    PTDataBase baseTest;
    Statistics stats;
    Course course;
    HashMap<String, Course> courseMap;
    
    public Service()throws SQLException {
        base = new PTDataBase(false);
        stats = new Statistics(base);
        this.dayList = new ArrayList<String>();
        this.dayList.add("Mon");
        this.dayList.add("Tue");
        this.dayList.add("Wed");
        this.dayList.add("Thu");
        this.dayList.add("Fri");
        this.dayList.add("Sat");
        this.dayList.add("Sun");
        this.courseMap = new HashMap<>();
        loadClasses(base);        
    }
    
    public void newCourse() {
        this.course = new Course();
    }
    public Course course() {
        return this.course;
    }
    
    public void mapStartSession(String name) {
        this.course = this.courseMap.get(name);
        this.course.startSession();
    }
    
    

    
    public void mapSetGrade(String name, int grade)throws SQLException {
        this.course = this.courseMap.get(name);
        this.course.setGrade(grade);
        this.base.setGrade(grade, name);
    }   
    
    public boolean mapCheckGrade(String name)throws SQLException {
        if (base.getGrade(name) == 0) {
            return false;
        } else {
            return true;
        }      
    }
       
    
    public String mapGetDate(String name) {
        this.course = this.courseMap.get(name);
        return this.course.getDate();
    }
    
    public String mapGetDay(String name) {
        this.course = this.courseMap.get(name);
        return this.course.getSessionDay();
    }
    
    public Double mapGetSessionTime(String name) {
        this.course = this.courseMap.get(name);
        return this.course.getSessionTime();
    }
    
    public String mapGetSessionVsGoal(String name) {
        this.course = this.courseMap.get(name);
        return this.course.getSessionVsGoalPercentage();
    }
    
    
    public void mapStopSession(String name) {
        this.course = this.courseMap.get(name);
        this.course.stopSession();
    }
    
    public void mapSetDailyGoal(String name, double goaltime) {
        this.courseMap.get(name).setDailyGoal(goaltime);
    }
    
    public void mapSetCoursePoints(String name, double points) {
        this.courseMap.get(name).setPoints(points);
    }
    
    public Course getCourse(String name) {
        this.course = this.courseMap.get(name);
        return this.course;
    }
    
    public void mapPutCourse(String name, Course course) {
        this.courseMap.put(name, this.course);
    }
    
    public boolean mapContainsKey(String name) {
        return this.courseMap.containsKey(name);
    }
    
    public ArrayList days() {
        return this.dayList;
    }
    
    public void loadClasses(PTDataBase base)throws SQLException {
        String courseName = "";
        try {
            for (int i = 0; i < base.getCourses().size(); i++) {
                courseName = base.getCourses().get(i).toString();
                Course tempCourse = new Course();
                this.courseMap.put(courseName, tempCourse);
                tempCourse.setCourse(courseName);
                tempCourse.setDailyGoal(base.getDailyGoal(courseName));
                tempCourse.setPoints(base.getCoursePoints(courseName));
                tempCourse.setGrade(base.getGrade(courseName));
            }
        } catch (SQLException e) {
            
        }
    }
    

    
    public double statsMean(String type, int identifier)throws SQLException {
        return stats.getMean(type, identifier);
    }
    
    public double statsSTD(String type, int identifier)throws SQLException {
        return stats.getSTD(type, identifier);
    }
    
    public double statsMedian(String type, int identifier)throws SQLException {
        return stats.getMedian(type, identifier);
    }
    
    public double statsTotal(String type, int identifier)throws SQLException {
        return stats.getTotal(type, identifier);
    }
    
    public LinkedHashMap statsTotalHoursPerDay()throws SQLException {
        return stats.getTotalHoursPerDay();
    }
    
    public HashMap statsGetTotalHoursByGrade() throws SQLException {
        return stats.getTotalHoursByGrade();
    }
    
    public boolean statsIsOverLimit(String course)throws SQLException {
        return stats.isOverLimit(course);
    }
    
    public LinkedHashMap statsAvgStudyVsGoal(String course)throws SQLException {
        return stats.getAvgStudyAgainstGoal(course);
    }
    
    public void courseSetCourse(String wanted) {
        this.course.setCourse(wanted);
    }
    
    public void courseSetPoints(double points) {
        this.course.setPoints(points);
    }
    
    public void courseSetGrade(int grade) {
        this.course.setGrade(grade);
    }
    
    public void courseSetGoal(double goal) {
        this.course.setDailyGoal(goal);
    }
    
    public void courseStartSesssion() {
        this.course.startSession();
    }
    
    public void courseStopSession() {
        this.course.stopSession();
    }
    
    public double courseGetSessionTime() {
        return this.course.getSessionTime();
    }
    
    public String courseGetSessionDay() {
        return this.course.getSessionDay();
    }
    
    public String courseGetDate() {
        return this.course.getDate();
    }
    
    public String courseGetSessionVsGoal() {
        return this.course.getSessionVsGoalPercentage();
    }
    
    public double courseGetGoal() {
        return this.course.getDailyGoal();
    }
    
    public String courseGetCourse() {
        return this.course.getCourse();
    }
    
    public double courseGetPoints() {
        return this.course.getPoints();
    }
    
    public int courseGetGrade() {
        return this.course.getGrade();
    }
    
    public double dataGetCoursePoints(String course) throws SQLException {
        return base.getCoursePoints(course);
    }
    
    public String dataGetCourse(String name)throws SQLException {
        return this.base.getCourse(name);
    }
    
    public void dataSetGrade(int grade, String course) throws SQLException {
        base.setGrade(grade, course);       
    }
    
    public int dataGetGrade(String course) throws SQLException {
        return base.getGrade(course);
    }
    
    public ArrayList dataGetWeekdays() throws SQLException {
        return base.getWeekDays();
    }
    
    public ArrayList dataGetCourses() throws SQLException {
        return base.getCourses();
    }
    
    public ArrayList dataGetCoursesNoGrades() throws SQLException {
        return base.getCoursesNoGrades();
    }
    
    public HashMap dataGetCoursesWithGrades() throws SQLException {
        return base.getCoursesWithGrades();
    }
    

    
    public ArrayList dataGetDates() throws SQLException {
        return base.getDates();
    }
    
    public ArrayList dataGetGrades() throws SQLException {
        return base.getGrades();
    }
    
    public double dataGetDailyGoal(String course) throws SQLException {
        return base.getDailyGoal(course);
    }
    
    public void dataSetCoursePointsAndGoal(String course, double goal, double points) throws SQLException {
        base.setCoursePointsAndGoal(course, goal, points);
    }
    
    public void dataSetDailyTime(String date, String day, String course, double sessionTime) throws SQLException {
        base.getSessionTimeForDB(date, day, course, sessionTime);
        base.setDailyTime(date, day, course, sessionTime);
    }
    
    public ArrayList dataGetTimeListByDay(String day) throws SQLException {
        return base.getTimeListByDay(day);
    }
    
    public ArrayList dataGetTimeListByDate(String date) throws SQLException {
        return base.getTimeListByDate(date);
    }
    
    public DescriptiveStatistics dataGetTimeByDayAndCourse(String day, String course) throws SQLException {
        return base.getTimeByDayAndCourse(day, course);
    }
    
    public ArrayList dataGetTimeListByCourse(String course) throws SQLException {
        return base.getTimeListByCourse(course);
    }
    
    public double dataGetStudyTimeByCourse(String course) throws SQLException {
        return base.getStudyTimesByCourse(course);
    }
    
    public double dataGetStudyTimeByDay(String day) throws SQLException {
        return base.getStudyTimeByDay(day);
    }
    
    public double dataGetStudyTimeByDate(String date) throws SQLException {
        return base.getStudyTimeByDate(date);
    }
    
    public void dataClose() throws SQLException {
        base.close();
    }
    
}
