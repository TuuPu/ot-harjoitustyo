/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productivitytracker.course;
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.text.DecimalFormat;


/**
 *
 * @author tuukkapuonti
 */

   /**
    * Luokka, jonka avulla pidetään kirjaa ja tietoja eri kursseista
    */
public class Course {
    
      
    String [] startValues;
    String [] stopValues;
    
    double points;
    double goalTime;
    double percentage;
    double sessionMinute;
    double sessionHour;
    double sessionSecond;
    double startHour;
    double startMinute;
    double startSecond;
    double startTotal;
    double stopHour;
    double stopMinute;
    double stopSecond;
    double studyTimeSession;
    
    int grade;
     
    
    DateTimeFormatter startDay;
    DateTimeFormatter startDate;
    DateTimeFormatter startTime;
    DateTimeFormatter stopDay;
    DateTimeFormatter stopDate;
    DateTimeFormatter stopTime;
    
    LocalDateTime startNow;
    LocalDateTime stopNow;
    
    DecimalFormat numberFormat;
    
    String formatStartTime;
    String formatStartDay;
    String formatStartDate;
    String formatStopTime;
    String formatStopDay;
    String formatStopDate;
    String course;
    
    /**
    * Konstruktori, joka pohjustaa kaikki tarvittavat globaalit muuttujat
    * 
    *
    * 
    * 
    */
    
    public Course() {
        
        numberFormat = new DecimalFormat("0.00");
        
        this.course = "";
        this.points = 0;
        this.grade = 0;
        this.goalTime = 0; 
        
        this.startDay = DateTimeFormatter.ofPattern("E");
        this.startDate = DateTimeFormatter.ofPattern("MMM dd");
        this.startTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        this.stopDay = DateTimeFormatter.ofPattern("E");
        this.stopDate = DateTimeFormatter.ofPattern("MMM dd");
        this.stopTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        this.startNow = LocalDateTime.now();
        this.stopNow = LocalDateTime.now();
        
        this.formatStartTime = "";
        this.formatStartDay = "";
        this.formatStartDate = "";
        
        this.formatStopTime = "";
        this.formatStopDay = "";
        this.formatStopDate = "";
        
        this.percentage = 0;
        
        this.startValues = formatStartTime.split(":");
        this.stopValues = formatStopTime.split(":");
        
        this.startHour = 0;
        this.startMinute = 0;
        this.startSecond = 0;
        
        this.stopHour = 0;
        this.stopMinute = 0;
        this.stopSecond = 0;
        
        this.sessionHour = 0;
        this.sessionMinute = 0;
        this.sessionSecond = 0;
        this.studyTimeSession = 0;
    }
    
    public void setCourse(String course) {
        this.course = course;
    }
    
    public void setPoints(double points) {
        this.points = points;      
    }
    
    public void setGrade(int grade) {
        this.grade = grade;
    }
    
    public void setDailyGoal(double goaltime) {
        this.goalTime = goaltime;
    }
    
   /**
    * Metodi, joka asettaa ajastaminen käyntiin ja antaa muuttujille niiden arvot,
    * muokkaa myös aikamuuttujat helpommin käytettävään double-muotoon
    * 
    */
    public void startSession() {
        this.startNow = LocalDateTime.now();
        this.startDay = DateTimeFormatter.ofPattern("E");
        this.startDate = DateTimeFormatter.ofPattern("MM dd");
        this.startTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.formatStartTime = startNow.format(startTime);
        this.formatStartDay = startNow.format(startDay);
        this.formatStartDate = startNow.format(startDate);
        this.startValues = formatStartTime.split(":");
        this.startHour = Double.parseDouble(startValues[0]);
        this.startMinute = Double.parseDouble(startValues[1]);
        this.startSecond = Double.parseDouble(startValues[2]);
    }   
    
   /**
    * Metodi, joka pysäyttää ajastaminen  ja antaa muuttujille niiden arvot,
    * muokkaa myös aikamuuttujat helpommin käytettävään double-muotoon ja
    * lopuksi laskee ajastimen käyttämän ajan.
    * 
    */
    
    public void stopSession() {
        //keskiyön yli laskeminen myöhemmin
        this.stopNow = LocalDateTime.now();
        this.stopDay = DateTimeFormatter.ofPattern("E");
        this.stopDate = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        this.stopTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.formatStopTime = stopNow.format(stopTime);
        this.formatStopDay = stopNow.format(stopDay);
        this.formatStopDate = stopNow.format(stopDate);
        this.stopValues = formatStopTime.split(":");
        this.stopHour = Double.parseDouble(stopValues[0]);
        this.stopMinute = Double.parseDouble(stopValues[1]);
        this.stopSecond = Double.parseDouble(stopValues[2]);
        this.sessionHour = this.stopHour - this.startHour;
        this.sessionMinute = (this.stopMinute - this.startMinute) / 60;
        this.sessionSecond = this.stopSecond - this.startSecond;
        this.studyTimeSession = this.sessionHour + this.sessionMinute;
    }
    
    
    public double getSessionTime() {
        return this.studyTimeSession;
    }
    
    public String getSessionDay() {
        return this.formatStopDay;
    }
    
   /**
    * Metodi, joka laskee suhteen yhdelle opiskelusessiolle vs. tavoiteaika
    * 
    * 
    */
    private void calculateSessionVsGoal() {
        if (this.goalTime > 0.0) {
            this.percentage = (this.studyTimeSession / this.goalTime) * 100;
        } else {
            this.percentage = 0;
        }
    }
    
    public String getDate() {
        return this.formatStopDate;
    }
    
   /**
    * Palauttaa prosenttimuodossa session aika/tavoiteaika. (Kutsuu sisällään
    * calculateSessionVsGoal() metodia.
    * 
    */
    public String getSessionVsGoalPercentage() {
        this.calculateSessionVsGoal();
        return numberFormat.format(this.percentage);      
    }
    
    public double getDailyGoal() {
        return this.goalTime;
    }
    
    public String getCourse() {
        return this.course;
    }
    
    public double getPoints() {
        return this.points;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    
}
