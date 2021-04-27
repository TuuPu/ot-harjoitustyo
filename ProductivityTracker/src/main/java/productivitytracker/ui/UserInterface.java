/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productivitytracker.ui;
import java.sql.SQLException;
import java.util.*;
import productivitytracker.course.Course;
import productivitytracker.ptdatabase.PTDataBase;
import productivitytracker.statistics.Statistics;
import java.text.DecimalFormat;
/**
 *
 * @author tuukkapuonti
 */
public class UserInterface {
    
    Course currentCourse;
    HashMap<String, Course> courseMap;
    String currentCourseName;
    String choice;
    boolean studying;
    DecimalFormat numberFormat;
    
    public UserInterface() {
        numberFormat = new DecimalFormat("0.00");
        this.currentCourseName = "";     
        this.choice = "";
        this.studying = false;
        this.courseMap = new HashMap<>();
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
            }
        } catch (SQLException e) {
            
        }
    }
    
    public void launch() throws SQLException {
        PTDataBase base = new PTDataBase(false);
        Statistics stats = new Statistics(base);
        Scanner sc = new Scanner(System.in);
        loadClasses(base);
        boolean stop = false;
        do {
            System.out.println("Create a new course to track (choose 1)");
            System.out.println("Choose a course from created courses (choose 2)");
            System.out.println("Start study session (choose 3) (Course chosen: " + this.currentCourseName + ")");
            System.out.println("End study session (choose 4) (Course chosen: " + this.currentCourseName + ")");
            System.out.println("Get information about your studies (choose 5)");
            System.out.println("End program (choose 6)");
            this.choice = sc.nextLine();
            switch (this.choice) {
                case "1":
                    Course newcourse = new Course();
                    System.out.println("Course name?");
                    String name = sc.nextLine();
                if (this.courseMap.containsKey(name)) {
                        System.out.println("The course is already in the database, create a new course?");
                        break;
                    }                   
                    newcourse.setCourse(name);                     
                    System.out.println("Course credits amount?");
                    try {
                        double coursepoints = Double.parseDouble(sc.nextLine());
                        newcourse.setPoints(coursepoints);
                        
                    } catch (NumberFormatException ignore) {
                        System.out.println("Use numbers, please");
                        System.out.println("------------------------------------");
                        break;
                    }
                    System.out.println("Daily goal time of studying for course " + name + "? (In decimal hours eg. 2 hours 30 minutes is 2.5 hours)");
                    try {
                        double goal = Double.parseDouble(sc.nextLine());
                        newcourse.setDailyGoal(goal);
                        double credits = newcourse.getPoints();
                        base.setCoursePointsAndGoal(name, goal, credits);
                    } catch (NumberFormatException ignore) {
                        System.out.println("Use numbers, please");
                        System.out.println("------------------------------------");
                        break;
                    }
                    this.courseMap.put(name, newcourse);
                    System.out.println("----------------------------------------");
                    break;
                case "2":
                    System.out.println("Name of the course you'd like to select? (Case sensitive)");
                    String courseName = sc.nextLine();
                    if (!this.courseMap.containsKey(courseName)) {
                        System.out.println("Couldn't find the course you were looking for, try again.");
                        System.out.println("------------------------------------");
                    } else {
                        this.currentCourse = this.courseMap.get(courseName);
                        System.out.println(this.currentCourse);
                        this.currentCourseName = this.currentCourse.getCourse();
                        System.out.println(this.currentCourse.getCourse());
                    }
                    System.out.println("----------------------------------------");
                    break;
                case "3":
                    if (this.currentCourseName.equalsIgnoreCase("")) {
                        System.out.println("You have to choose a course before starting your study session");
                        System.out.println("----------------------------------------");
                    } else {
                        System.out.println("Starting study session...");
                        System.out.println("----------------------------------------");
                        this.currentCourse.startSession();                       
                        this.studying = true;                      
                    }                    
                    break;
                case "4":
                    if (this.studying == false) {
                        System.out.println("You have to start a study session, before you end it, silly!");
                        System.out.println("----------------------------------------");
                    } else {
                        this.currentCourse.stopSession();                   
                        this.studying = false;
                        System.out.println("The amount spent studying in this session vs. goal: " + this.currentCourse.getSessionVsGoalPercentage() + "%");
                        base.getSessionTimeForDB(this.currentCourse.getDate(), this.currentCourse.getSessionDay(), this.currentCourseName, this.currentCourse.getSessionTime());
                        base.setDailyTime(this.currentCourse.getDate(), this.currentCourse.getSessionDay(), this.currentCourseName, this.currentCourse.getSessionTime());                      
                        System.out.println("----------------------------------------");
                    }
                    break;
                case "5":
                    System.out.println("Choose one of the following to view information about your studies:");
                    System.out.println("Choose 'A' to see information about your studies per. day");
                    System.out.println("Choose 'B' to see information about your studies per. course");
                    System.out.println("Choose 'C' to see information about your studies per. date");
                    String option = sc.nextLine();
                    switch (option) {
                        case "A":
                            System.out.println("Type in day, for example 'Mon' (case sensitive)");
                            String day = sc.nextLine();
                            double meanDouble=stats.getMean(day, 1);
                            double medianDouble=stats.getMedian(day, 1);
                            double stdDouble=stats.getSTD(day, 1);
                            double totalDouble=stats.getTotal(day, 1);
                            String totalString=numberFormat.format(totalDouble);
                            String stdString=numberFormat.format(stdDouble);
                            String medianString=numberFormat.format(medianDouble);
                            String meanString=numberFormat.format(meanDouble);                                                   
                            System.out.println("Mean of studytime for day " + day + ": " + meanString + " hours");
                            System.out.println("Median of studytime for day " + day + ": " + medianString + " hours");
                            System.out.println("Standard deviation of studytime for day " + ": " + stdString + " hours");
                            System.out.println("Total studytime for " + day + ": " + totalString + " hours");
                            LinkedHashMap <String, Double> dayTotals = new LinkedHashMap<String, Double>();
                            dayTotals=stats.getTotalHoursPerDay();
                            System.out.println("And the total time spent studying for each day of the week:");
                            for(Map.Entry<String, Double> entry : dayTotals.entrySet()){
                                String key=entry.getKey();
                                double value=entry.getValue();
                                String valueString=numberFormat.format(value);
                                System.out.println( key + " " + valueString + " hours" );
                            }
                            break;
                        case "B":
                            LinkedHashMap <String, Double> avgMap = new LinkedHashMap<>();
                            System.out.println("Type in course, for example 'ohte' (case sensitive)");
                            String course = sc.nextLine();
                            meanDouble=stats.getMean(course, 0);
                            medianDouble=stats.getMedian(course, 0);
                            stdDouble=stats.getSTD(course, 0);
                            totalDouble=stats.getTotal(course, 0);
                            meanString=numberFormat.format(meanDouble);
                            medianString=numberFormat.format(medianDouble);
                            stdString=numberFormat.format(stdDouble);
                            totalString=numberFormat.format(totalDouble);
                            avgMap=stats.getAvgStudyAgainstGoal(course);
                            System.out.println("Mean of studytime for course " + course + ": " + meanString + " hours");
                            System.out.println("Median of studytime for course " + course + ": " + medianString + " hours");
                            System.out.println("Standard deviation of studytime for course " + ": " + stdString + " hours");
                            System.out.println("Total studytime for " + course + ": " + totalString + " hours");
                            System.out.println("And an average for study time per. day compared to goal time:");
                            for(Map.Entry<String, Double> entry2 : avgMap.entrySet()){
                                String key=entry2.getKey();
                                double number=entry2.getValue();
                                String value=numberFormat.format(number);
                                if(value.equals("NaN")){
                                    value="0";
                                }
                                System.out.println(key + " " + value + "%");
                            }                          
                            break;
                        case "C":
                            System.out.println("Type in date, for example '19.04.2021' (form sensitive)");                          
                            String date = sc.nextLine();
                            meanDouble=stats.getMean(date, 2);
                            medianDouble=stats.getMedian(date, 2);
                            stdDouble=stats.getSTD(date, 2);
                            totalDouble=stats.getTotal(date, 2);
                            meanString=numberFormat.format(meanDouble);
                            medianString=numberFormat.format(medianDouble);
                            stdString=numberFormat.format(stdDouble);
                            totalString=numberFormat.format(totalDouble);
                            System.out.println("Mean of studytime for date " + date + ": " + meanString + " hours");
                            System.out.println("Median of studytime for date " + date + ": " + medianString + " hours");
                            System.out.println("Standard deviation of studytime for date " + date + ": " + stdString + " hours");
                            System.out.println("Total studytime for " + date + ": " + totalString + " hours");                           
                            break;
                        default:
                    }
                    break;
                case "6":
                    System.out.println("Quitting program, thanks and see you again! :)");                  
                    base.close();
                    stop = true;
                    break;
                default:
                        
            }
            
        } while (stop == false);
    }
}
