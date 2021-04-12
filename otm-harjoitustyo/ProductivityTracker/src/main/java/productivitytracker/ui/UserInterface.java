/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productivitytracker.ui;
import java.util.*;
import productivitytracker.course.Course;
/**
 *
 * @author tuukkapuonti
 */
public class UserInterface {
    
    Course currentCourse;
    HashMap<String, Course> courseMap;
    String currentCourseName;
    String yesno;
    String choice;
    boolean studying;
    
    public UserInterface(){
        this.currentCourseName="";
        this.yesno="";
        this.choice="";
        this.studying=false;
        this.courseMap=new HashMap<>();
    }
    public void Launch(){
        Scanner sc=new Scanner(System.in);
        boolean stop=false;
        do{
            System.out.println("Create a new course to track (choose 1)");
            System.out.println("Choose a course from created courses (choose 2)");
            System.out.println("Start study session (choose 3) (Course chosen: "+this.currentCourseName+")");
            System.out.println("End study session (choose 4) (Course chosen: "+this.currentCourseName+")");
            System.out.println("End program (choose 5)");
            this.choice=sc.nextLine();
            switch (this.choice){
                case "1":
                    Course newcourse=new Course();
                    System.out.println("Course name?");
                    String name=sc.nextLine();
                    newcourse.setCourse(name);                  
                    System.out.println("Course credits amount?");
                    try{
                        double coursepoints=Double.parseDouble(sc.nextLine());
                        newcourse.setPoints(coursepoints);
                    }
                    catch(NumberFormatException ignore){
                        System.out.println("Use numbers, please");
                        System.out.println("------------------------------------");
                        break;
                    }
                    System.out.println("Daily goal time of studying for course "+name+"? (In decimal hours eg. 2 hours 30 minutes is 2.5 hours)");
                    try{
                        double goal=Double.parseDouble(sc.nextLine());
                        newcourse.setDailyGoal(goal);
                    }
                    catch(NumberFormatException ignore){
                        System.out.println("Use numbers, please");
                        System.out.println("------------------------------------");
                        break;
                    }
                    this.courseMap.put(name, newcourse);
                    System.out.println("----------------------------------------");
                    break;
                case "2":
                    System.out.println("Name of the course you'd like to select? (Case sensitive)");
                    String courseName=sc.nextLine();
                    if(!this.courseMap.containsKey(courseName)){
                        System.out.println("Couldn't find the course you were looking for, try again.");
                        System.out.println("------------------------------------");
                    }
                    else{
                        this.currentCourse=this.courseMap.get(courseName);
                        this.currentCourseName=this.currentCourse.getCourse();
                    }
                    System.out.println("----------------------------------------");
                    break;
                case "3":
                    if(this.currentCourseName.equalsIgnoreCase("")){
                        System.out.println("You have to choose a course before starting your study session");
                        System.out.println("----------------------------------------");
                    }
                    else{
                        System.out.println("Starting study session...");
                        System.out.println("----------------------------------------");
                        this.currentCourse.startSession();
                        this.studying=true;                      
                    }                    
                    break;
                case "4":
                    if(this.studying==false){
                        System.out.println("You have to start a study session, before you end it, silly!");
                        System.out.println("----------------------------------------");
                    }
                    else{
                    this.currentCourse.stopSession();                   
                    this.studying=false;
                    System.out.println("The amount spent studying in this session vs. goal: "+this.currentCourse.getSessionVsGoalPercentage()+"%");
                    System.out.println("Overall study time daily: "+this.currentCourse.getDailyHours());
                    System.out.println("----------------------------------------");
                    }
                    break;
                case "5":
                    System.out.println("Quitting program, thanks and see you again! :)");
                    stop=true;
                    break;
                default:
                        
            }
            
        }while(stop==false);
    }
}
