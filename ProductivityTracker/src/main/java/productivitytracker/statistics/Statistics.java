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

   /**
    * Luokka, jonka avulla voidaan suorittaa statistista laskentaa.
    */
public class Statistics {
    PTDataBase data;
    private double mean;
    private double std;
    private double median;
    private double total;
    private DecimalFormat formatter;
    private ArrayList<String> dayList;
   

   /**
    * Konstruktori, joka pohjustaa arvot käytettäväksi luokalle.
    * @param courseData, hyödyntää tietokanta-luokkaa toiminnassaan.
    * 
    */
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
   /**
    * Metodi, joka laskee keskiarvon opiskeluille tietyin parametrein
    * 
    * @param type, kertoo mitä halutaan inputiksi (kurssi, päivä tai päivämäärä)
    * @param identifier, kertoo metodille mihin ehtolauseeseen mennä: 0=kurssi, 1=päivä, 2=päivämäärä
    * @return stats.getMean(), eli keskiarvo annetuilla tiedoilla.
    */
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
    
   /**
    * Metodi, joka laskee keskihajonnan opiskeluille tietyin parametrein
    * 
    * @param type, kertoo mitä halutaan inputiksi (kurssi, päivä tai päivämäärä)
    * @param identifier, kertoo metodille mihin ehtolauseeseen mennä: 0=kurssi, 1=päivä, 2=päivämäärä
    * @return stats.getSandardDeviation(), eli keskihajonna annetuilla tiedoilla.
    */
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
    
   /**
    * Metodi, joka laskee mediaanin opiskeluille tietyin parametrein
    * 
    * @param type, kertoo mitä halutaan inputiksi (kurssi, päivä tai päivämäärä)
    * @param identifier, kertoo metodille mihin ehtolauseeseen mennä: 0=kurssi, 1=päivä, 2=päivämäärä
    * @return stats.getMedian(), eli mediaanin annetuilla tiedoilla.
    */
    
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
    /**
    * Metodi, joka laskee kokonaisajan opiskeluille tietyin parametrein
    * 
    * @param type, kertoo mitä halutaan inputiksi (kurssi, päivä tai päivämäärä)
    * @param identifier, kertoo metodille mihin ehtolauseeseen mennä: 0=kurssi, 1=päivä, 2=päivämäärä
    * @return palauttaa käytettyjen parametrien perusteella täyden opiskeluajan.
    */
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
    /*
    public HashMap totalVsGrade(){
        HashMap<String, Double> gradeList = new HashMap<>();
        
    }
    */
    
    
    /**
    * Metodi, joka palauttaa listana totaaliajan opiskeluista per. päivä
    * 
    * @return ArrayList
    */
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
   /**
    * Metodi, joka tarkistaa onko opiskelluista sessioista kertyvä aika yli
    * suosituksen per. opintopiste (27 tuntia)
    * 
    * @param course käyttäjän antama kurssi
    * @return boolean, riippuen onko opiskelusessioiden summa jaettuna opinto-
    * pisteiden määrällä yli 27.
    */
    public boolean isOverLimit(String course) throws SQLException {
        double session = 0;
        double coursePoints = 0; 
        double total = 0;
        try {
            session = data.getStudyTimesByCourse(course);
            coursePoints = data.getCoursePoints(course);
            total = session / coursePoints;
        } catch (SQLException e) {
            
        }
        if (total >= 27) {
            return true;
        } else {
            return false;
        }
    }
    
   /**
    * Metodi, joka laskee suhteen keskimääräisen opiskelusession keston ja tavoiteajan välillä
    * 
    * @param course, Käyttäjän antama tieto kurssista, jolle lasku halutaan tehdä
    * @return HashMap, joka kertoo per. viikonpäivä suhteen keskimääräisen opiskelun ja tavoiteajan välillä.
    */
    
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
