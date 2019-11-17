import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Driver {

    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException {

        DataManager dataManager= new DataManager();
        dataManager.initDatabase();
        /*---- schema.sql contains 'set search_path to pitt_social;' !!! --- */
        PittSocial PS= new PittSocial(dataManager.getConnection());
        PS.createUser("didi","dd@dd.org","dd","1900-09-10");
        PS.createUser("didi","3dd@dd.org","dd","1900-09-10");
        PS.createUser("dodo","dodo@tt.org","dd","2017-09-10");
        PS.login("3dd@dd.org","dd");

        PS.initiateFriendship(3,"waddup");
        PS.initiateFriendship(2,"waddup");
    }
}
