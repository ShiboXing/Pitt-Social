import java.io.IOException;
import java.sql.SQLException;

public class Driver {

    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException {


        DataManager dataManager= new DataManager();
        dataManager.initDatabase();
        /*---- schema.sql contains 'set search_path to pitt_social;' !!! --- */
        PittSocial PS= new PittSocial(dataManager.getConnection());

        //test createuser
        PS.createUser("didi","1@1.org","111","1900-09-10");
        PS.createUser("didi","2@2.org","222","1900-09-10");
        PS.createUser("dodo","3@3.org","333","2017-09-10");
        PS.createUser("lolo","4@4.org","444","2017-09-10");
        PS.createUser("aoao","5@5.org","555","2017-09-10");
        PS.createUser("yoyo","6@6.org","666","2017-09-10");
        PS.createUser("jojo","7@7.org","777","2017-09-10");

        //test initiateFriend
        System.out.println(PS.login("1@1.org","111"));
        PS.initiateFriendship(4,"add me NO. 4");
        System.out.println(PS.login("2@2.org","222"));
        PS.initiateFriendship(3,"waddup");
        PS.initiateFriendship(1,"waddup");
        System.out.println(PS.login("3@3.org","333"));
        PS.initiateFriendship(1,"hi");

        //test create group
        PS.createGroup("lame",3,"group 1 description");
        PS.createGroup("lame2",2,"group 2 description");
        System.out.println(PS.login("1@1.org","111"));
        PS.createGroup("not lame",3,"group 3 description");

        //test initiateGroup
        PS.initiateAddingGroup(1,"my id is 1, put me into group 2");
        PS.initiateAddingGroup(2,"my id is 1, put me into group 2");
        System.out.println(PS.login("5@5.org","555"));
        PS.initiateAddingGroup(3,"my id is 5, put me into group 3");
        System.out.println(PS.login("4@4.org","444"));
        PS.initiateAddingGroup(1,"my id is 4, put me into group 1");
        PS.initiateAddingGroup(2,"my id is 4, put me into group 2");

        // test showFriendsRequests
        System.err.println(PS.showFriendRequests());
        System.out.println(PS.login("1@1.org","111"));
        System.err.println(PS.showFriendRequests());

        //test showGroupRequests
        System.out.println(PS.login("3@3.org","333"));
        System.err.println(PS.showGroupRequests());
        System.out.println(PS.login("1@1.org","111"));
        System.err.println(PS.showGroupRequests());




    }
}
