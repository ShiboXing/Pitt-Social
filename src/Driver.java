import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Driver {

    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException {

        DataManager dataManager= new DataManager();
        dataManager.initDatabase();
        //schema.sql contains 'set search_path to pitt_social;' !!!
        PittSocial PS= new PittSocial(dataManager.getConnection());
        PS.createUser("Kirk","kk@ha.org","iii","1997-09-10");

    }
}
