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
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.*;
import java.io.File;
/**
 *
 * @author tuukkapuonti
 */

   /**
    * Luokka, joka pitää tallessa tiedot opiskelluista kursseista
    */
public class PTDataBase {
    
    private Connection db = null;
    private Statement statement = null;
    
    private DecimalFormat numberFormat;   
    
    private double totalTime;   
    private double previousStudies;
    
    private LinkedHashMap<String, Double> byDay;
     
    /**
    * Kontstruktori luo tietokannan ja tarkistaa yhteyden.
    * Konstruktorin boolean muuttujalla varmistetaan onko ohjelma käyttö-, vai testitilassa.
    *
    * @param   testDB   Annetaan ohjelman luojan toimesta
    *
    * 
    */   
    public PTDataBase(boolean testDB) throws SQLException {
        this.byDay = new LinkedHashMap<>();
        numberFormat = new DecimalFormat("0.00");
        try {
            if (testDB) {
                this.db = DriverManager.getConnection("jdbc:sqlite:testDB.db");
            } else {
                this.db = DriverManager.getConnection("jdbc:sqlite:productivitytracker.db");
            }
            statement = db.createStatement();    
            setTables();
            System.out.println("Connection to database succesful");
        } catch (SQLException e) {
            System.out.println("Database already exists... Moving on.");
        }
    }       
    /**
    * Metodi luo tietokannan taulut valmiiksi käytettäväksi.
    * 
    */
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
    /**
    * Metodi hakee tietokannasta annetun kurssin perusteella opintopisteiden määrän.
    * 
    *
    * @param   course   Käyttäjän antama kurssin nimi
    *
    * @return kurssien opintopisteet.
    */
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
        /**
        * Asettaa tietokantaan kurssin arvosanan. HUOM! Edellyttää, että kurssi
        * on luotuna tietokantaan!
        * 
        *
        * @param grade   Käyttäjän antama kurssin arvosana
        * @param course  Käyttäjän antama kurssin nimi
        *
        * 
        */   
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
        /**
        * Metodi hakee kurssin arvosanan tietokannasta kurssin nimen perusteella
        * 
        *
        * @param   course   Käyttäjän antama kurssin nimi
        *
        * @return kurssin arvosanan.
        */   
    public int getGrade(String course) throws SQLException {
        PreparedStatement prepared;
        try {
            prepared = db.prepareStatement("SELECT Courses.grade FROM Courses WHERE Courses.course=?");
            prepared.setString(1, course);
            ResultSet res = prepared.executeQuery();
            return res.getInt("grade");
        } catch (SQLException e) {
            
        }
        return 0;
    }    
            /**
        * Metodi hakee tietokannassa sijaitsevat viikonpäivät
        * 
        *
        * @return list palauttaa ArrayList-muodossa viikonpäivät
        */    
    public ArrayList getWeekDays()throws SQLException {
        PreparedStatement prepared;
        ArrayList<String> list = new ArrayList<>();
        try {
            prepared = db.prepareStatement("SELECT Daily.dayOfWeek FROM Daily");
            ResultSet res = prepared.executeQuery();
            while (res.next()) {
                list.add(res.getString("dayOfWeek"));
            }
        } catch (SQLException e) {            
        }
        return list;
    }    
            /**
        * Metodi hakee tietokannassa sijaitsevat kurssit
        * 
        *
        * @return list, joka sisältää kurssit.
        */   
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
            /**
        * Metodi hakee tietokannassa sijaitsevat päivämäärät
        * 
        *
        * @return list, joka palauttaa päivämäärät
        */
    public ArrayList getDates()throws SQLException {
        PreparedStatement prepared;
        ArrayList<String> list = new ArrayList<>();
        try {
            prepared = db.prepareStatement("SELECT Daily.date FROM Daily");
            ResultSet res = prepared.executeQuery();
            while (res.next()) {
                list.add(res.getString("date"));
            }
        } catch (SQLException e) {           
        }
        return list;
    }
            /**
        * Metodi hakee tavoiteajan päivittäiselle opiskelulle
        * 
        *
        * @param   course   Käyttäjän antama kurssin nimi
        *
        * @return tavoiteajan (double)
        */   
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
        /**
        * Asettaa tietokantaan kurssin, opintopistemäärän ja tavoiteajan
        * 
        *
        * @param   course   Käyttäjän antama kurssin nimi
        * @param goal Käyttäjän antama tavoiteaika
        * @param points Käyttäjän antama opintopistemäärä
        *
        */
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
       /**
        * Metodi hakee tietokannasta kurssin aiemmat opinnot (aika) 
        * ja tallentaa tiedon globaaliin muuttujaan this.previousStudies.
        * 
        *
        *@param   course   Käyttäjän antama kurssin nimi
        *@param date Käyttäjän antama päivämäärä
        * @param day Käyttäjän antama päivä
        * @param sessionTime ohjelmalta saatava nykyisen session kesto
        */ 
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
       /**
        * Metodi asettaa tietokantaan tiedon opiskeluajasta, joka yhdistetään kurssiin liittyviin muihin tietoihin.
        * 
        *
        * @param   course   Käyttäjän antama kurssin nimi
        *@param day Käyttäjän antama päivän nimi
        * @param date Käyttäjän antama päivämäärä
        * @param sessionTime ohjelmalta saatava opiskelusession pituus      
        */   
    public void setDailyTime(String date, String day, String course, double sessionTime)throws SQLException {
        PreparedStatement prepared;
        PreparedStatement select;
        try {
            select = db.prepareStatement("SELECT Courses.id FROM Courses WHERE Courses.course=?");
            select.setString(1, course);
            ResultSet resCourse = select.executeQuery();
            int id = resCourse.getInt("id");
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
       /**
        * Palauttaa listan muodossa tiedon opiskeluajoista per. viikonpäivä
        * 
        *
        * @param   day   Käyttäjän antama päivän nimi
        *
        * @return list, joka sisältää opiskeluajat.
        */   
    public ArrayList getTimeListByDay(String day)throws SQLException {
        ArrayList<Double> dayList = new ArrayList<Double>();
        PreparedStatement prepared;
        try {
            prepared = db.prepareStatement("SELECT Daily.timeForDay FROM Daily WHERE Daily.dayOfWeek=?");
            prepared.setString(1, day);
            ResultSet res = prepared.executeQuery();
            while (res.next()) {
                dayList.add(res.getDouble("timeForDay"));               
            }
            return dayList;
        } catch (SQLException e) {           
        }
        return dayList;
    }
         /**
        * Palauttaa listan muodossa tiedon opiskeluajoista per. päivämäärä
        * 
        *
        * @param   date   Käyttäjän antama päivämäärä
        *
        * @return list, joka sisältää opiskeluajat.
        */
    public ArrayList getTimeListByDate(String date)throws SQLException {
        ArrayList<Double> dateList = new ArrayList<Double>();
        PreparedStatement prepared;
        try {
            prepared = db.prepareStatement("SELECT Daily.timeForDay FROM Daily WHERE Daily.date=?");
            prepared.setString(1, date);
            ResultSet res = prepared.executeQuery();
            while (res.next()) {
                dateList.add(res.getDouble("timeForDay"));               
            }
            return dateList;
        } catch (SQLException e) {           
        }
        return dateList;
    }
       /**
        * Palauttaa listan muodossa tiedon opiskeluajoista päivän ja kurssin perusteella
        * 
        *
        * @param   day   Käyttäjän antama päivän nimi.
        * @param course Käyttäjän antama kurssin nimi.
        *
        * @return list, joka sisältää opiskeluajat.
        */
    public DescriptiveStatistics getTimeByDayAndCourse(String day, String course)throws SQLException {
        DescriptiveStatistics dayStatistics = new DescriptiveStatistics();
        PreparedStatement prepared;
        try {
            prepared = db.prepareStatement("SELECT Daily.timeForDay FROM Daily WHERE Daily.courseForDay=? AND Daily.dayOfWeek=?");
            prepared.setString(1, course);
            prepared.setString(2, day);
            ResultSet res = prepared.executeQuery();
            while (res.next()) {
                dayStatistics.addValue(res.getDouble("timeForDay"));
            }
            return dayStatistics;
        } catch (SQLException e) {
            
        }
        return dayStatistics;
    }
       /**
        * Palauttaa listan muodossa tiedon opiskeluajoista per. kurssi
        * 
        *
        * @param   course   Käyttäjän antama kurssin nimi
        *
        * @return list, joka sisältää opiskeluajat.
        */    
    public ArrayList getTimeListByCourse(String course)throws SQLException {
        ArrayList<Double> courseList = new ArrayList<Double>();
        PreparedStatement prepared;
        try {
            prepared = db.prepareStatement("SELECT Daily.timeForDay FROM Daily WHERE Daily.courseForDay=?");
            prepared.setString(1, course);
            ResultSet res = prepared.executeQuery();
            while (res.next()) {
                courseList.add(res.getDouble("timeForDay"));               
            }
            return courseList;
        } catch (SQLException e) {           
        }
        return courseList;
    }
       /**
        * Palauttaa opiskeluaikojen summan kurssin nimen perusteella
        * 
        *
        * @param   course   Käyttäjän antama kurssin nimi
        *
        * @return total, double joka on kaikkien opintosessioiden summa tietylle kurssille.
        */
    
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
    /**
        * Palauttaa opiskeluaikojen summan päivän nimen perusteella
        * 
        *
        * @param   day   Käyttäjän antama päivän nimi
        *
        * @return total, double joka on kaikkien opintosessioiden summa tietylle päivälle.
        */
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
    /**
        * Palauttaa opiskeluaikojen summan päivämäärän perusteella
        * 
        *
        * @param   date   Käyttäjän antama päivämäärä
        *
        * @return total, double joka on kaikkien opintosessioiden summa tietylle päivämäärälle.
        */   
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
       /**
        * Sulkee tietokannan kutsuttaessa
        * 
        *
        */
    public void close()throws SQLException {
        try {
            db.close();
        } catch (SQLException e) {           
        }
    }
    /**
        * Poistaa tietokannan (testitietokannan)
        * 

        *
        * @return true/false, riippuen onnistuuko tietokannan poisto.
        */
    public boolean deleteDb() throws SQLException {
        try {
            Statement stmt = db.createStatement();
            stmt.close();
            this.db.close();
            return new File("testDB.db").delete();
        } catch (SQLException e) {
            return false;
        }
    }
}
