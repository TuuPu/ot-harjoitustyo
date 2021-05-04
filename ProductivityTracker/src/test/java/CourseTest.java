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

/**
 *
 * @author tuukkapuonti
 */
public class CourseTest {
    
    Course newCourseTest;
    Course newCourseTest1;
    Course newCourseTest2;
    
    public CourseTest() {
        newCourseTest=new Course();
        newCourseTest1=new Course();
        newCourseTest2=new Course();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void setCourse() {
        newCourseTest.setCourse("tira");
        assertEquals("tira", newCourseTest.getCourse());
    }
    
    @Test
    public void setPoints() {
        newCourseTest.setPoints(5);
        String asDouble=Double.toString(5.0);
        assertEquals(asDouble, Double.toString(newCourseTest.getPoints()));
    }
    @Test
    public void getSessionDay(){
        LocalDateTime testNow = LocalDateTime.now();
        DateTimeFormatter testDay = DateTimeFormatter.ofPattern("E");
        String formatMidDay = testNow.format(testDay);
        newCourseTest.setCourse("tira");
        newCourseTest.startSession();
        newCourseTest.stopSession();
        assertEquals(formatMidDay, newCourseTest.getSessionDay());
    }
    @Test
    public void getSessionDate(){
        LocalDateTime testNow = LocalDateTime.now();
        DateTimeFormatter testDate = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        String formatTestDate = testNow.format(testDate);
        newCourseTest.setCourse("tira");
        newCourseTest.startSession();
        newCourseTest.stopSession();
        assertEquals(formatTestDate, newCourseTest.getDate());
    }
    
    @Test
    public void setGrade() {
        newCourseTest.setGrade(4);
        assertEquals(4, newCourseTest.getGrade());
    }
    
    @Test
    public void getSessionTime() {
        double zero=0.0;
        String asDouble=Double.toString(zero);     
        assertEquals(asDouble, Double.toString(newCourseTest.getSessionTime()));
    }
    
    
    
    @Test
    public void getDailyGoal() {
        newCourseTest.setDailyGoal(2);
        String asDouble=Double.toString(2.0);
        assertEquals(asDouble, Double.toString(newCourseTest.getDailyGoal()));
    }
    
    
    /*
   
    @Test
    public void stopTimeIsGreaterThanStartTime() throws InterruptedException{
        newCourseTest.startSession();
        TimeUnit.SECONDS.sleep(2);
        newCourseTest.stopSession();
        boolean check=false;
        if(newCourseTest.getStartTimeAsValue()<newCourseTest.getStopTimeAsValue()){
            check=true;
        }
        assertTrue(check);
    }
    */
    @Test
    public void percentageIsZeroIfGoalTimeIsNotCorrect() throws InterruptedException{
        //Tähän saattaa tulla vielä tarkempi testi, toistaiseksi opiskelua mitataan
        //minimissään minuuteissa, mutta testin mielekkyyden takia voisin lisätä
        //sekuntimittauksen myös. Ikävää odotella testin valmistumista minuutteja.
        newCourseTest.setDailyGoal(0);
        newCourseTest.startSession();
        TimeUnit.SECONDS.sleep(1);
        newCourseTest.stopSession();
        assertEquals("0,00", newCourseTest.getSessionVsGoalPercentage());
        newCourseTest1.setDailyGoal(-5);
        newCourseTest1.startSession();
        TimeUnit.SECONDS.sleep(1);
        newCourseTest1.stopSession();
        assertEquals("0,00",newCourseTest1.getSessionVsGoalPercentage());
        newCourseTest2.setDailyGoal(3);
        newCourseTest2.startSession();
        TimeUnit.SECONDS.sleep(1);
        newCourseTest2.stopSession();
        assertEquals("0,00", newCourseTest2.getSessionVsGoalPercentage());
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
