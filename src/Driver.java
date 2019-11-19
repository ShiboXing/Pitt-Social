import java.io.IOException;
import java.sql.SQLException;

public class Driver {

    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException {


        DataManager dataManager= new DataManager();
        dataManager.initDatabase();
        /*---- schema.sql contains 'set search_path to pitt_social;' !!! --- */
        PittSocial PS= new PittSocial(dataManager.getConnection());

        //test createuser
        PS.createUser("didi","dd@dd.org","qq","1900-09-10");
        PS.createUser("didi","3dd@dd.org","dd","1900-09-10");
        PS.createUser("dodo","dodo@tt.org","dd","2017-09-10");
        PS.createUser("lolo","lolo@tt.org","tt","2017-09-10");
        System.out.println(PS.login("3dd@dd.org","dd"));

        PS.initiateFriendship(3,"waddup");
        PS.initiateFriendship(1,"waddup");
        System.out.println(PS.login("dodo@tt.org","dd"));
        PS.initiateFriendship(4,"hi");

        PS.createGroup("lame",3,"lame group description");
        PS.createGroup("lame2",2,"lame2 group description");
        System.out.println(PS.login("dd@dd.org","qq"));
        PS.createGroup("not lame",3,"not lame group description");

        PS.initiateAddingGroup(1,"my id is 1, put me into the lame group");
        PS.initiateAddingGroup(2,"my id is 1, put me into the lame2 group");
        System.out.println(PS.login("lolo@tt.org","tt"));
        PS.initiateAddingGroup(3,"my id is 4, put me into the not lame group");
    }
}
