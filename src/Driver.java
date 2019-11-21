import java.io.Console;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Driver {

    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException {


        DataManager dataManager = new DataManager();
        dataManager.initDatabase();
        /*---- schema.sql contains 'set search_path to pitt_social;' !!! --- */
        PittSocial PS = new PittSocial(dataManager.getConnection());
        enterMainMenu();

        //test createuser
//        PS.createUser("didi", "1@1.org", "111", "1900-09-10");
//        PS.createUser("hoho", "2@2.org", "222", "1900-09-10");
//        PS.createUser("dodo", "3@3.org", "333", "2017-09-10");
//        PS.createUser("lolo", "4@4.org", "444", "2017-09-10");
//        PS.createUser("aoao", "5@5.org", "555", "2017-09-10");
//        PS.createUser("yoyo", "6@6.org", "666", "2017-09-10");
//        PS.createUser("jojo", "7@7.org", "777", "2017-09-10");
//
//        //test initiateFriend
//        System.out.println(PS.login("1@1.org", "111"));
//        PS.initiateFriendship(4, "i'm NO. 1, add me NO. 4");
//        PS.initiateFriendship(2, "i'm NO. 1, add me NO. 2");
//        System.out.println(PS.logout());
//        System.out.println(PS.login("2@2.org", "222"));
//        PS.initiateFriendship(3, "i'm NO. 2, add me NO. 3");
//        PS.initiateFriendship(1, "i'm NO. 2, add me NO. 1");
//        PS.initiateFriendship(5, "i'm NO. 2, add me NO. 5");
//        PS.initiateFriendship(7, "i'm NO. 2, add me NO. 7");
//        System.out.println(PS.logout());
//        System.out.println(PS.login("3@3.org", "333"));
//        PS.initiateFriendship(1, "i'm NO. 3, add me NO. 1");
//        PS.initiateFriendship(2, "i'm NO. 3, add me NO. 2");
//
//        //test create group
//        PS.createGroup("lame", 3, "group 1 description");
//        PS.createGroup("lame2", 2, "group 2 description");
//        System.out.println(PS.logout());
//        System.out.println(PS.login("1@1.org", "111"));
//        PS.createGroup("not lame", 3, "group 3 description");
//
//        //test initiateGroup
//        PS.initiateAddingGroup(1, "my id is 1, put me into group 1");
//        PS.initiateAddingGroup(2, "my id is 1, put me into group 2");
//        System.out.println(PS.logout());
//
//        System.out.println(PS.login("2@2.org", "222"));
//        PS.initiateAddingGroup(2, "my id is 2, put me into group 2");
//        System.out.println(PS.logout());
//        System.out.println(PS.login("5@5.org", "555"));
//        PS.initiateAddingGroup(3, "my id is 5, put me into group 3");
//        System.out.println(PS.logout());
//        System.out.println(PS.login("4@4.org", "444"));
//        PS.initiateAddingGroup(1, "my id is 4, put me into group 1");
//        PS.initiateAddingGroup(2, "my id is 4, put me into group 2");
//        System.out.println(PS.logout());
//        System.out.println(PS.login("6@6.org", "666"));
//        PS.initiateAddingGroup(2, "my id is 6, put me into group 2");
//        System.out.println(PS.logout());
//        System.out.println(PS.login("7@7.org", "777"));
//        PS.initiateAddingGroup(2, "my id is 7, put me into group 2");
//
//        // test showFriendsRequests
//        System.err.println(PS.showFriendRequests());
//        System.out.println(PS.logout());
//        System.out.println(PS.login("1@1.org", "111"));
//        System.err.println(PS.showFriendRequests());
//
//        //test showGroupRequests
//        System.out.println(PS.logout());
//        System.out.println(PS.login("3@3.org", "333"));
//        System.err.println(PS.showGroupRequests());
//        System.out.println(PS.logout());
//        System.out.println(PS.login("1@1.org", "111"));
//        System.err.println(PS.showGroupRequests());
//
//        //test confirm friend and group requests
//        PS.resolveFriendRequest(2, false);
//        PS.resolveFriendRequest(3, true);
//        System.out.println(PS.logout());
//        System.out.println(PS.login("5@5.org", "555"));
//        PS.resolveFriendRequest(2, true);
//        System.out.println(PS.logout());
//        System.out.println(PS.login("2@2.org", "222"));
//        PS.resolveFriendRequest(3, true);
//        PS.resolveFriendRequest(1, true);
//        System.out.println(PS.logout());
//        System.out.println(PS.login("7@7.org", "777"));
//        PS.resolveFriendRequest(2, true);
//
//        System.out.println(PS.logout());
//        System.out.println(PS.login("3@3.org", "333"));
//        PS.resolveGroupRequest(1, 4, false);
//        PS.resolveGroupRequest(2, 4, false);
//        PS.resolveGroupRequest(2, 2, true);
//        PS.resolveGroupRequest(2, 7, true);
//        PS.resolveGroupRequest(2, 6, true);
//        System.out.println(PS.logout());
//        System.out.println(PS.login("1@1.org", "111"));
//        PS.resolveGroupRequest(3, 5, true);
//
//        //test sendMessageToUser
//        System.out.println(PS.logout());
//        System.out.println(PS.login("5@5.org", "555"));
//        System.err.println(PS.sendMessageToUser(2, "5 saying hi to 2"));
//        System.err.println(PS.sendMessageToUser(2, "5 saying hi to 2"));
//        System.out.println(PS.logout());
//        System.out.println(PS.login("2@2.org", "222"));
//        System.err.println(PS.sendMessageToUser(5, "2 saying hi to 5"));
//        System.out.println(PS.logout());
//        System.out.println(PS.login("1@1.org", "111"));
//        System.err.println(PS.sendMessageToUser(3, "1 saying hi to 3"));
//        System.err.println(PS.sendMessageToUser(2, "1 saying hi to 2"));
//        System.err.println(PS.sendMessageToUser(2, "1 saying hi to 2"));
//
//        //test sendMessageToGroup
//        System.out.println(PS.logout());
//        System.out.println(PS.login("5@5.org", "555"));
//        System.err.println(PS.sendMessageToGroup(3, "5 saying hi to group 3"));
//        System.out.println(PS.logout());
//        System.out.println(PS.login("1@1.org", "111"));
//        System.err.println(PS.sendMessageToGroup(3, "1 saying hi to group 3"));
//        System.out.println(PS.logout());
//        System.out.println(PS.login("6@6.org", "666"));
//        System.err.println(PS.sendMessageToGroup(2, "6 saying hi to group 2"));
//        System.out.println(PS.logout());
//        System.out.println(PS.login("1@1.org", "111"));
//
//        //test display messages and display new messages
//        System.out.println(PS.logout());
//        System.out.println(PS.login("2@2.org", "222"));
//        System.err.println(PS.displayMessages());
//        System.out.println(PS.logout());
//        System.out.println(PS.login("3@3.org", "333"));
//        System.err.println(PS.displayMessages());
//        System.err.println(PS.sendMessageToGroup(2, "3 saying hi to group 2"));
//        System.err.println(PS.sendMessageToGroup(2, "3 saying hi to group 2"));
//        System.out.println(PS.logout());
//        System.out.println(PS.login("7@7.org", "777"));
//        System.err.println(PS.sendMessageToUser(2, "7 saying hi to 2"));
//        System.out.println(PS.logout());
//        System.out.println(PS.login("2@2.org", "222"));
//        System.err.println(PS.displayNewMessages());
//        System.err.println(PS.displayMessages());
//
//        //test displayFriends
//        System.err.println(PS.displayFriends());
//        System.out.println(PS.logout());
//        System.out.println(PS.login("3@3.org", "333"));
//        System.err.println(PS.displayFriends());
//
//        //test display profile
//        System.err.println(PS.displayProfile(1));
//        System.out.println(PS.logout());
//        System.out.println(PS.login("2@2.org", "222"));
//        System.err.println(PS.displayProfile(7));


    }

    public static void printMainMenu() {
        StringBuilder menu = new StringBuilder(InfoPrinter.createTitle("Welcome to Pitt Social", 60));
        menu.append("\nOperations: \n");
        ArrayList<String> operationList = new ArrayList<>(Arrays.asList("User Menu", "Group Menu"));
        for (int i=0; i<operationList.size(); i++) {
            menu.append("(").append(i).append(")\t").append(operationList.get(i)).append("\n");
        }
        System.out.println(menu);
    }

    public static void enterMainMenu() {
        Console console = System.console();
        if(console==null){
            System.err.println("Please run in Console!");
            System.exit(-1);
        }
        console.flush();
        printMainMenu();
        String raw_input = System.console().readLine();
        try {
            int inputSelection = Integer.parseInt(raw_input);
        } catch (NumberFormatException e) {
            System.err.println("Please enter an Integer Number");
        }
    }
}
