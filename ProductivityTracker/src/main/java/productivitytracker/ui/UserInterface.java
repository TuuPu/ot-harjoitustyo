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
import service.Service;
import java.text.DecimalFormat;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.TilePane;
/**
 *
 * @author tuukkapuonti
 */

   /**
    * Graafinen käyttöliittymä opiskelun seurannalle
    */
public class UserInterface extends Application {
    
    HashMap<String, Course> courseMap;
    String currentCourseName;
    String choice;
    boolean studying;
    DecimalFormat numberFormat;
    Stage stage;
    double coursePoints;
    double goalTime;
    ArrayList<String> courseList;
    boolean timerOn;
    Timer timer;
    
    Service s;
    
    /**
    * Konstruktori, joka alustaa tarvittavat muuttujat
    */
    public UserInterface() throws SQLException{
        numberFormat = new DecimalFormat("0.00");
        this.currentCourseName = "";     
        this.choice = "";
        this.timerOn=false;
        this.studying = false;
        this.courseMap = new HashMap<>();
        
        
        
        
    }
    
    /**
    * Metodi, joka kutsuu Course-luokan ja databasen informaatioita aiempien opiskelu-
    * sessioiden latausta varten.
    * @param base, tietokantaluokan ilmentymä
    */
    
    
    @Override
    public void start(Stage primaryStage) throws SQLException {

        stage = primaryStage;
        Scene scene = generalView();       
        primaryStage.setScene(scene);
        primaryStage.show();
        s = new Service();
    
    }
    
   /**
    * Päänäkymä, josta voi navigoida muualle ohjelmaan
    * @return scene, joka luo päänäkymän.
    */
    
    public Scene generalView(){
        VBox layout = new VBox();
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setPrefSize(500, 400);
        layout.setAlignment(Pos.TOP_CENTER);

        Text title = new Text("Your personal productivityTracker");
        title.setStyle("-fx-font-weight: bold");
        Button exitButton = new Button("Exit");
        exitButton.setPrefWidth(200);
        exitButton.setAlignment(Pos.BOTTOM_CENTER);
        Parent buttonsLayout = generalViewHelper();
        

        exitButton.setOnAction(e -> {
            if(this.timerOn){
            this.timer.cancel();
            this.timerOn=false;
            }
            try {
            s.dataClose();
            } catch (SQLException ex){
                Text closefail = new Text("Failed to close database");
                layout.getChildren().add(closefail);
            }
            stage.close();
            javafx.application.Platform.exit();
        });
        
        layout.getChildren().addAll(title, buttonsLayout, exitButton);
        
        return new Scene(layout);
    }
    
   /**
    * Päänäkymän avustajametodi, jolla luodaan navigointityökaluja (nappeja)
    * @return useita nappeja päänäkymän työkaluiksi.
    */
    
    public Parent generalViewHelper(){
        
        VBox addButtons = new VBox(8);
        addButtons.setPadding(new Insets(20, 20, 20 ,20));
        addButtons.setAlignment(Pos.CENTER);
        
        Button studyTrackerButton = new Button("Start studying");
        Button sessionButton = new Button("Start a session");
        
        studyTrackerButton.setPrefWidth(200);
        studyTrackerButton.setOnAction(e -> { stage.setScene(studyTrackerScene()); });
        
        sessionButton.setOnAction(e -> {stage.setScene(sessionScene());});
        
        addButtons.getChildren().addAll(studyTrackerButton, sessionButton);
        
        return addButtons;
    }
    
   /**
    * Sessionäkymä, jossa käyttäjä voi seurata jo luotuja kursseja.
    * Sisältää myös statistiikkaa.
    * @return sessionäkymä ja sen tiedot.
    */
    public Scene sessionScene(){
        
        VBox layout = new VBox();
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setPrefSize(1000, 1000);
        layout.setSpacing(10);
        layout.setAlignment(Pos.TOP_LEFT);       
        Button start = new Button("Start!");
        Button stop = new Button ("Stop!");
        
        HBox statsLO = new HBox();
        
        statsLO.setAlignment(Pos.CENTER_RIGHT);
        Text statsInfo = new Text("Here you can see total hours studied and your progress for the selected goaltime");
        
        CategoryAxis xAxisTotal = new CategoryAxis();
        xAxisTotal.setLabel("Day");
        
        NumberAxis yAxisTotal = new NumberAxis();
        yAxisTotal.setAutoRanging(false);
        yAxisTotal.setLowerBound(0);
        yAxisTotal.setUpperBound(20);
        yAxisTotal.setTickUnit(0.5);
        
        BarChart TotalDayChart = new BarChart(xAxisTotal, yAxisTotal);
        XYChart.Series dayTotalData = new XYChart.Series();
        dayTotalData.setName("Study session totals per. weekday");
        
        try{
        for(int i=0;i<s.days().size();i++){
            String day=s.days().get(i).toString();
            double total=(double)s.statsTotalHoursPerDay().get(day);
            dayTotalData.getData().add(new XYChart.Data(day, total));
                }
        } catch(SQLException er){          
        }
        TotalDayChart.getData().add(dayTotalData);
            
        
        VBox textLO = new VBox();
        textLO.setAlignment(Pos.TOP_CENTER);
        Text info = new Text("You can start a session here and view information about your studies. Current course selected: "+this.currentCourseName);
        info.setTextAlignment(TextAlignment.CENTER);       
        textLO.getChildren().addAll(info);
        
        
        this.courseList=new ArrayList<>();
        try{
            this.courseList=s.dataGetCoursesNoGrades();
        } catch(SQLException er){
           
        }
        
        ComboBox courseBox = new ComboBox(FXCollections.observableArrayList(this.courseList));
            EventHandler<ActionEvent> courseEvent = (ActionEvent e) -> {          
            this.currentCourseName=courseBox.getValue().toString();
            try{
            this.goalTime=s.dataGetDailyGoal(this.currentCourseName);
            } catch (SQLException err){
                
            }
            info.setText("You can start a session here and view information about your studies. Current course selected: "+this.currentCourseName);      
        };
        courseBox.setOnAction(courseEvent);
                        
        
        VBox infoLO = new VBox();
        infoLO.setAlignment(Pos.CENTER);
        infoLO.setPadding(new Insets(20, 20, 20, 20));
        infoLO.getChildren().addAll(TotalDayChart);
        
        start.setOnAction(e ->{
            this.timer = new Timer();
            this.timerOn=true;
            courseBox.setDisable(true);          
            this.timer.schedule(new TimerTask() {
            public void run() {
             info.setText("You have studied for 30 minutes, take a break and stretch!");
            }
            }, 1800000, 14400000);
            
            Text course = new Text(this.currentCourseName);
            String goal="";
            
            try{
                goal=String.valueOf(s.dataGetDailyGoal(this.currentCourseName));
            } catch (SQLException err){
                
            }
            
            Text studytime = new Text(goal);
            infoLO.getChildren().addAll(course, studytime);
            
            if(this.currentCourseName.equals("")){
                Text noCourseWarning = new Text("WARNING: You have to select a course first");
                infoLO.getChildren().addAll(noCourseWarning);
            }
            else{
                s.mapStartSession(this.currentCourseName);             
                this.studying=true;
            }
        });
        
        stop.setOnAction(e ->{
            
            this.timer.cancel();
            this.timerOn=false;
            courseBox.setDisable(false);
            
            if(!this.studying){
                Text noStart = new Text("WARNING: You have to start a session before you stop!");
            }
            else{
                s.mapStopSession(this.currentCourseName);
                this.studying=false;
                Text percentage = new Text("The amount spent studying in this session vs goal: "+ s.mapGetSessionVsGoal(this.currentCourseName)+"%");
                infoLO.getChildren().addAll(percentage);
                try{
                    s.dataSetDailyTime(s.mapGetDate(this.currentCourseName), s.mapGetDay(this.currentCourseName), this.currentCourseName, s.mapGetSessionTime(this.currentCourseName));
                    if(s.statsIsOverLimit(this.currentCourseName)){
                        Text limitWarning = new Text("Your sessions in total for this course are exceeding the limit of 27 hours per. 1 credit. Take it easy");
                        infoLO.getChildren().addAll(limitWarning);
                    }
                    
            Text success = new Text("Study session tracked succesfully!");
            infoLO.getChildren().addAll(success);
                      
            CategoryAxis xAxisAgainstGoal = new CategoryAxis();
            xAxisAgainstGoal.setLabel("Days");
        
            NumberAxis yAxisAgainstGoal = new NumberAxis();
        
            yAxisAgainstGoal.setAutoRanging(false);
            yAxisAgainstGoal.setLowerBound(0);
            yAxisAgainstGoal.setUpperBound(100);
            yAxisAgainstGoal.setTickUnit(0.1);
            yAxisAgainstGoal.setLabel("percentage");
        
            BarChart againstGoalChart = new BarChart(xAxisAgainstGoal, yAxisAgainstGoal);
        
            XYChart.Series againstGoal = new XYChart.Series();
        
            againstGoal.setName("Average study times against goal time for course "+this.currentCourseName);
        
                try{
                    for(int i=0;i<s.statsAvgStudyVsGoal(this.currentCourseName).size();i++){
                        String day=s.days().get(i).toString();
                        double per=(double)s.statsAvgStudyVsGoal(this.currentCourseName).get(day);
                        againstGoal.getData().add(new XYChart.Data(day, per));
                     }
                } catch(SQLException er){          
                }
                againstGoalChart.getData().add(againstGoal);
                infoLO.getChildren().addAll(againstGoalChart);
                    
 
                    
                } catch (SQLException error){
                    Text failure = new Text("Failed to add info to database");
                    infoLO.getChildren().addAll(failure);
                }                
            }
        });
         
        Button statsButton = new Button ("Statistics");
        statsButton.setPrefWidth(200);
        statsButton.setAlignment(Pos.BOTTOM_CENTER);
        statsButton.setOnMouseClicked(e -> { 
            if(this.timerOn){
            this.timer.cancel();
            this.timerOn=false;
            }
            stage.setScene(statisticScene()); });
        
        Button returnButton = new Button("Return");
        returnButton.setPrefWidth(200);
        returnButton.setAlignment(Pos.BOTTOM_CENTER);
        returnButton.setOnMouseClicked(e -> {
            if(this.timerOn){
            this.timer.cancel();
            this.timerOn=false;
            }
            stage.setScene(generalView()); }); 
        layout.getChildren().addAll(textLO, courseBox, start, stop, infoLO, statsButton, returnButton);
        return new Scene(layout);
    }
    
   /**
    * Statistiikan päänäkymä, josta käyttäjä näkee jo seurattujen opintojen statistista informaatiota
    * @return scene, joka luo statistisen esityksen opiskeluille
    */
    
    public Scene statisticScene(){
        HBox layout = new HBox();
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setPrefSize(1000, 500);
        layout.setSpacing(10);
        
        CategoryAxis xAxisMeanDay = new CategoryAxis();
        CategoryAxis xAxisMedianDay = new CategoryAxis();
        CategoryAxis xAxisSTDDay = new CategoryAxis();
        CategoryAxis xAxisGrade = new CategoryAxis();
        
        xAxisMeanDay.setLabel("Days");
        xAxisGrade.setLabel("Grade");
        
        NumberAxis yAxisMeanDay = new NumberAxis();
        NumberAxis yAxisSTDDay = new NumberAxis();
        NumberAxis yAxisMedianDay = new NumberAxis();
        NumberAxis yAxisGrade = new NumberAxis();
        
        yAxisSTDDay.setAutoRanging(false);
        yAxisMeanDay.setAutoRanging(false);
        yAxisMedianDay.setAutoRanging(false);
        yAxisGrade.setAutoRanging(false);
        
        yAxisSTDDay.setLowerBound(0);
        yAxisMedianDay.setLowerBound(0);
        yAxisMedianDay.setUpperBound(5);
        yAxisGrade.setLowerBound(0);
        
        yAxisMeanDay.setLowerBound(0);
        yAxisSTDDay.setUpperBound(5);
        yAxisMeanDay.setUpperBound(5);
        yAxisGrade.setUpperBound(100);
        
        yAxisMeanDay.setTickUnit(0.10);
        yAxisSTDDay.setTickUnit(0.10);
        yAxisMedianDay.setTickUnit(0.10);
        yAxisGrade.setTickUnit(0.10);
        yAxisGrade.setLabel("Hours");
        yAxisMeanDay.setLabel("Hours");
        
        BarChart gradeHourChart = new BarChart(xAxisGrade, yAxisGrade);
        BarChart MeanDayChart = new BarChart(xAxisMeanDay, yAxisMeanDay);
        XYChart.Series gradeData = new XYChart.Series();
        XYChart.Series daySTD = new XYChart.Series();
        XYChart.Series dayData = new XYChart.Series();
        XYChart.Series dayMedian = new XYChart.Series();
        gradeData.setName("Grade vs. total hours");
        dayData.setName("Study session mean per. weekday");
        daySTD.setName("Standard deviation of study time per. weekday");
        dayMedian.setName("Median of study time per. weekday");
        
        try{
            HashMap<Integer, Double> gradeList=s.statsGetTotalHoursByGrade();
            for(int i : gradeList.keySet()){
                String grade=String.valueOf(i);
                double total=(double)gradeList.get(i);
                gradeData.getData().add(new XYChart.Data(grade, total));
            }
        } catch (SQLException error){
            
        }
        gradeHourChart.getData().add(gradeData);
        
        try{
        for(int i=0;i<s.days().size();i++){
            String day=s.days().get(i).toString();
            double mean=s.statsMean(day, 1);
            double std=s.statsSTD(day, 1);
            double median=s.statsMedian(day, 1);
            dayData.getData().add(new XYChart.Data(day, mean));
            daySTD.getData().add( new XYChart.Data(day, std));
            dayMedian.getData().add( new XYChart.Data(day, median));
                }
        } catch(SQLException er){          
        }
        
        MeanDayChart.getData().add(dayData);
        MeanDayChart.getData().add(daySTD);
        MeanDayChart.getData().add(dayMedian);
        
        CategoryAxis xAxisMeanDate = new CategoryAxis();
        CategoryAxis xAxisMedianDate = new CategoryAxis();
        CategoryAxis xAxisSTDDate = new CategoryAxis();
        xAxisMeanDate.setLabel("Dates");
        
        NumberAxis yAxisMeanDate = new NumberAxis();
        NumberAxis yAxisSTDDate = new NumberAxis();
        NumberAxis yAxisMedianDate = new NumberAxis();
        
        yAxisMedianDate.setAutoRanging(false);
        yAxisMedianDate.setLowerBound(0);
        yAxisMedianDate.setUpperBound(5);
        yAxisMedianDate.setTickUnit(0.10);
        
        yAxisSTDDate.setAutoRanging(false);
        yAxisSTDDate.setLowerBound(0);
        yAxisSTDDate.setUpperBound(5);
        yAxisSTDDate.setTickUnit(0.10);
        
        yAxisMeanDay.setAutoRanging(false);       
        yAxisMeanDay.setLowerBound(0);
        yAxisMeanDay.setUpperBound(5);
        yAxisMeanDay.setTickUnit(0.10);
        
        yAxisMeanDate.setLabel("Hours");
        BarChart MeanDateChart = new BarChart(xAxisMeanDate, yAxisMeanDate);
        
        XYChart.Series dateData = new XYChart.Series();
        XYChart.Series STDData = new XYChart.Series();
        XYChart.Series MedianData = new XYChart.Series();
        STDData.setName("Standard deviation per. date");
        dateData.setName("Study session mean per. date");
        MedianData.setName("Median for study time per. date");
        
        try{
        for(int i=0;i<s.dataGetDates().size();i++){
            String date=s.dataGetDates().get(i).toString();
            double mean=s.statsMean(date, 2);
            double std=s.statsSTD(date, 2);
            double median = s.statsMedian(date, 2);
            dateData.getData().add(new XYChart.Data(date, mean));
            STDData.getData().add(new XYChart.Data(date, std));
            MedianData.getData().add(new XYChart.Data(date, median));
                }
        } catch(SQLException er){          
        }
        
        MeanDateChart.getData().add(dateData);
        MeanDateChart.getData().add(STDData);
        MeanDateChart.getData().add(MedianData);
        
        CategoryAxis xAxisMeanCourse = new CategoryAxis();
        CategoryAxis xAxisSTDCourse = new CategoryAxis();
        CategoryAxis xAxisMedianCourse = new CategoryAxis();
        
        xAxisMeanCourse.setLabel("Courses");
        
        NumberAxis yAxisMeanCourse = new NumberAxis();
        NumberAxis yAxisSTDCourse = new NumberAxis();
        NumberAxis yAxisMedianCourse = new NumberAxis();
        
        yAxisMedianCourse.setAutoRanging(false);
        yAxisMedianCourse.setLowerBound(0);
        yAxisMedianCourse.setUpperBound(5);
        yAxisMedianCourse.setTickUnit(0.1);
        
        yAxisSTDCourse.setAutoRanging(false);
        yAxisSTDCourse.setLowerBound(0);
        yAxisSTDCourse.setUpperBound(5);
        yAxisSTDCourse.setTickUnit(0.10);
        
        yAxisMeanCourse.setAutoRanging(false);
        yAxisMeanCourse.setLowerBound(0);
        yAxisMeanCourse.setUpperBound(5);
        yAxisMeanCourse.setTickUnit(0.10);
        
        yAxisMeanCourse.setLabel("Hours");
        
        BarChart MeanCourseChart = new BarChart(xAxisMeanCourse, yAxisMeanCourse);
        
        XYChart.Series courseData = new XYChart.Series();
        XYChart.Series courseSTD = new XYChart.Series();
        XYChart.Series courseMedian = new XYChart.Series();
        
        courseSTD.setName("Standard deviation of studies per. course");
        courseData.setName("Study session mean per. course");
        courseMedian.setName("Study time median per. course");
        
        try{
        for(int i=0;i<s.dataGetCourses().size();i++){
            String course=s.dataGetCourses().get(i).toString();
            double mean=s.statsMean(course, 0);
            double std=s.statsSTD(course, 0);
            double median=s.statsMedian(course, 0);
            courseData.getData().add(new XYChart.Data(course, mean));
            courseSTD.getData().add(new XYChart.Data(course, std));
            courseMedian.getData().add(new XYChart.Data(course, median));
                }
        } catch(SQLException er){          
        }
        
        MeanCourseChart.getData().add(courseSTD);
        MeanCourseChart.getData().add(courseData);
        MeanCourseChart.getData().add(courseMedian);
        
  
        Button returnButton = new Button("Return");
        returnButton.setAlignment(Pos.BOTTOM_CENTER);
        returnButton.setOnMouseClicked(e -> { stage.setScene(generalView()); });
        layout.getChildren().addAll(MeanCourseChart, MeanDayChart, MeanDateChart, gradeHourChart, returnButton);
        return new Scene(layout);
    }
    
    
   /**
    * Kurssien seurannan näkymä, josta käyttäjä voi asettaa kursseja seurattavaksi.
    * @return scene, joka luo studyTrackerScenen.
    */
    
    public Scene studyTrackerScene(){
        VBox layout = new VBox();
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setPrefSize(1000, 500);
        layout.setAlignment(Pos.TOP_CENTER);
        Text title = new Text("Your personal productivityTracker");
        title.setStyle("-fx-font-weight: bold");
        
        Text guide = new Text("Start by typing in a new course, fill in its studypoints and the daily goal for studying");
        HBox addCourse = new HBox();
        addCourse.setSpacing(10);
        addCourse.setAlignment(Pos.CENTER);
        addCourse.setPadding(new Insets(20, 20, 20, 20));
        TextField courseTextField = new TextField();
        courseTextField.setPrefWidth(200);
        Button addCourseButton = new Button("Add course");
        
        HBox addPoints = new HBox();
        addPoints.setSpacing(10);
        addPoints.setAlignment(Pos.CENTER);
        addPoints.setPadding(new Insets(20, 20, 20, 20));
        TextField pointsTextField = new TextField();
        pointsTextField.setPrefWidth(200);
        Button addPointsButton = new Button("Add points");
        
        HBox addGoal = new HBox();
        addGoal.setSpacing(10);
        addGoal.setAlignment(Pos.CENTER);
        addGoal.setPadding(new Insets(20, 20, 20, 20));
        TextField goalTextField = new TextField();
        goalTextField.setPrefWidth(200);
        Button addGoalButton = new Button("Add goal time");
        
        HBox addGrade = new HBox();
        addGrade.setSpacing(10);
        addGrade.setAlignment(Pos.CENTER);
        addGrade.setPadding(new Insets(20, 20, 20, 20));
        Text gradeGuide = new Text("Has a course ended? You can add a grade here.");
        TextField courseField = new TextField();
        courseField.setPrefWidth(200);
        TextField gradeField = new TextField();
        gradeField.setPrefWidth(200);
        Button courseButton = new Button("Which course?");
        Button addGradeButton = new Button("Add grade");
        
        VBox addLO = new VBox(5);
        addLO.setAlignment(Pos.CENTER);
        addLO.setPadding(new Insets(20, 20, 20, 20));
        
        courseButton.setOnAction(e -> {
            try {
                if(courseField.getText().equals("") || s.dataGetCourse(courseField.getText()).equals("")){
                    Text noTextWarning = new Text("WARNING: The input for course name was empty or it wasn't found in study database");
                    addLO.getChildren().add(noTextWarning);
                }
                else{
                    this.currentCourseName=courseField.getText();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        addGradeButton.setOnAction(e -> {
            try {
                if(gradeField.getText().equals("") || s.dataGetGrade(this.currentCourseName)!=0){
                    Text noTextWarning = new Text("WARNING: The input for grade was empty or the course has already been graded");
                    addLO.getChildren().add(noTextWarning);
                }
                else{
                    int grade=Integer.parseInt(gradeField.getText());
                    try {
                        s.mapSetGrade(this.currentCourseName, grade);
                    } catch (SQLException ex) {
                    }
                }
            } catch (SQLException ex) {
            }
        });
        
        addCourseButton.setOnAction(e -> {
            s.newCourse();
            if(courseTextField.getText().equals("") || s.mapContainsKey(courseTextField.getText())){
                Text noTextWarning = new Text("WARNING: The input for course name was empty or the course is already added");
                addLO.getChildren().add(noTextWarning);
            }
            else{
                this.currentCourseName=courseTextField.getText();
                s.courseSetCourse(this.currentCourseName);
                s.mapPutCourse(this.currentCourseName, s.course());
            }         
        });
        
        addPointsButton.setOnAction(e -> {
            if(this.currentCourseName.equals("")){
                Text courseFirst = new Text("Please give a course to track first");
                addLO.getChildren().add(courseFirst);
            } else{
            try {
            this.coursePoints=Double.parseDouble(pointsTextField.getText());
            s.mapSetCoursePoints(this.currentCourseName, this.coursePoints);
            } catch (NumberFormatException ignore){
                Text notNumberWarning = new Text("WARNING: Use numbers please");
                addLO.getChildren().add(notNumberWarning);
            } 
            }
        });
        
        addGoalButton.setOnAction(e ->{
            if(this.currentCourseName.equals("")){
                Text courseFirst = new Text("Please give a course to track first");
                addLO.getChildren().add(courseFirst);
            } else {         
            try {
                this.goalTime=Double.parseDouble(goalTextField.getText());
                s.mapSetDailyGoal(this.currentCourseName, this.goalTime);
            try {
                s.dataSetCoursePointsAndGoal(this.currentCourseName, this.goalTime, this.coursePoints);
                Text success = new Text("Course succesfully added");
                addLO.getChildren().add(success);
            } catch (SQLException ex) {
                Text failedToInsertBase = new Text("Adding a course failed");
                addLO.getChildren().add(failedToInsertBase);
                }
            } catch (NumberFormatException ignore){
                Text notNumberWarning = new Text("WARNING: use numbers please");
                addLO.getChildren().add(notNumberWarning);
            }
            }
        });  
        
        Button returnButton = new Button("Return");
        returnButton.setPrefWidth(200);
        returnButton.setAlignment(Pos.BOTTOM_CENTER);
        
        Button toSessionButton = new Button("Start a session?");
        toSessionButton.setPrefWidth(200);
        toSessionButton.setAlignment(Pos.BOTTOM_CENTER);
        toSessionButton.setOnAction(e -> { stage.setScene(sessionScene()); });
        
        layout.getChildren().addAll(title, guide, addCourse, addPoints, addGoal, addGrade, returnButton, toSessionButton, addLO);
        
        addGrade.getChildren().addAll(gradeGuide, courseField, courseButton, gradeField, addGradeButton);
        
        addCourse.getChildren().addAll(courseTextField, addCourseButton);
        
        addPoints.getChildren().addAll(pointsTextField, addPointsButton);
        
        addGoal.getChildren().addAll(goalTextField, addGoalButton);    
        
        returnButton.setOnMouseClicked(e -> { stage.setScene(generalView()); });         
        return new Scene(layout);    
    }
    
    
   /**
    * Käyttöliittymän main, joka käynnistää käyttöliittymän.
    */
    public static void main(String[] args){
        launch(args);
    }
}
