/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productivitytracker;
import java.util.*;
import productivitytracker.ui.UserInterface;
import java.sql.SQLException;
/**
 *
 * @author tuukkapuonti
 */

   /**
    * Main-luokka, joka kutsuu käyttöliittymäluokan ilmentymää ja käynnistää ohjelman.
    */
public class Main {
    public static void main(String[] args)throws SQLException {
        UserInterface ui = new UserInterface();
        ui.main(args);
    }
}
