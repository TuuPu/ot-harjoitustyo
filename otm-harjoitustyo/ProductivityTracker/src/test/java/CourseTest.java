/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.time.*;
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
    
    public CourseTest() {
        newCourseTest=new Course();
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
    
    @Test
    public void percentageIsZeroIfGoalTimeIsNotCorrect() throws InterruptedException{
        //Tähän saattaa tulla vielä tarkempi testi, toistaiseksi opiskelua mitataan
        //minimissään minuuteissa, mutta testin mielekkyyden takia voisin lisätä
        //sekuntimittauksen myös. Ikävää odotella testin valmistumista minuutteja.
        newCourseTest.setDailyGoal(0);
        newCourseTest.startSession();
        TimeUnit.SECONDS.sleep(1);
        newCourseTest.stopSession();
        assertEquals("0.00", newCourseTest.getSessionVsGoalPercentage());
        newCourseTest.setDailyGoal(-5);
        newCourseTest.startSession();
        TimeUnit.SECONDS.sleep(1);
        newCourseTest.stopSession();
        assertEquals("0.00",newCourseTest.getSessionVsGoalPercentage());
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
