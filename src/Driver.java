import javafx.util.Pair;

import java.io.Console;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.LinkedList;
import java.util.Scanner;

public class Driver {

    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException, InterruptedException {


        DataManager dataManager = new DataManager();
        PittSocial pittSocial = new PittSocial(dataManager.getConnection());

        if (args.length == 1 && args[0].equalsIgnoreCase("I")) {
            dataManager.initDatabase(true);


            char[] profileCols = {'i','s','s','s','d','t'};
            char[] groupInfoCols = {'i','s','i','s'};
            char[] groupMemberCols = {'i','i','s'};
            char[] pendingGroupMemberCols = {'i','i','s'};
            char[] pendingFriendCols = {'i','i','s'};
            char[] friendCols = {'i','i','d','s'};
            char[] messageInfoCols = {'i','i','s','i','i','t'};
            char[] messageRecipientCols = {'i','i'};


            //test createuser
            pittSocial.createUser("didi", "1@1.org", "111", "1900-09-10");
            pittSocial.printTable("Profile", profileCols);
            pittSocial.createUser("hoho", "2@2.org", "222", "1900-09-10");
            pittSocial.printTable("Profile", profileCols);
            pittSocial.createUser("dodo", "3@3.org", "333", "2017-09-10");
            pittSocial.printTable("Profile", profileCols);
            pittSocial.createUser("lolo", "4@4.org", "444", "2017-09-10");
            pittSocial.printTable("Profile", profileCols);
            pittSocial.createUser("aoao", "5@5.org", "555", "2017-09-10");
            pittSocial.printTable("Profile", profileCols);
            pittSocial.createUser("yoyo", "6@6.org", "666", "2017-09-10");
            pittSocial.printTable("Profile", profileCols);
            pittSocial.createUser("jojo", "7@7.org", "777", "2017-09-10");
            pittSocial.printTable("Profile", profileCols);

            //test initiateFriend
            pittSocial.login("1@1.org", "111");
            pittSocial.initiateFriendship(4, "i'm NO. 1, add me NO. 4");
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.initiateFriendship(2, "i'm NO. 1, add me NO. 2");
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("2@2.org", "222");
            pittSocial.initiateFriendship(3, "i'm NO. 2, add me NO. 3");
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.initiateFriendship(1, "i'm NO. 2, add me NO. 1");
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.initiateFriendship(5, "i'm NO. 2, add me NO. 5");
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.initiateFriendship(7, "i'm NO. 2, add me NO. 7");
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("3@3.org", "333");
            pittSocial.initiateFriendship(1, "i'm NO. 3, add me NO. 1");
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.initiateFriendship(2, "i'm NO. 3, add me NO. 2");
            pittSocial.printTable("PendingFriend", pendingFriendCols);

            //test create group
            pittSocial.createGroup("lame1", 5, "group 1 description");
            pittSocial.printTable("GroupInfo", groupInfoCols);
            pittSocial.printTable("GroupMember", groupMemberCols);
            pittSocial.createGroup("lame2", 5, "group 2 description");
            pittSocial.printTable("GroupInfo", groupInfoCols);
            pittSocial.printTable("GroupMember", groupMemberCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);
            pittSocial.login("1@1.org", "111");
            pittSocial.createGroup("lame3", 5, "group 3 description");
            pittSocial.printTable("GroupInfo", groupInfoCols);
            pittSocial.printTable("GroupMember", groupMemberCols);

            //test initiateGroupRequest
            pittSocial.initiateAddingGroup(1, "my id is 1, put me into group 1");
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);
            pittSocial.initiateAddingGroup(2, "my id is 1, put me into group 2");
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("2@2.org", "222");
            pittSocial.initiateAddingGroup(2, "my id is 2, put me into group 2");
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("5@5.org", "555");
            pittSocial.initiateAddingGroup(3, "my id is 5, put me into group 3");
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("4@4.org", "444");
            pittSocial.initiateAddingGroup(1, "my id is 4, put me into group 1");
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);
            pittSocial.initiateAddingGroup(2, "my id is 4, put me into group 2");
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);
            pittSocial.initiateFriendship(7, "AAA");
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("6@6.org", "666");
            pittSocial.initiateAddingGroup(2, "my id is 6, put me into group 2");
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("7@7.org", "777");
            pittSocial.initiateAddingGroup(2, "my id is 7, put me into group 2");
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);

            // test showFriendsRequests
            System.err.println(pittSocial.showFriendRequests());
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);
            pittSocial.login("1@1.org", "111");
            System.err.println(pittSocial.showFriendRequests());

            //test showGroupRequests
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);
            pittSocial.login("3@3.org", "333");
            System.err.println(pittSocial.showGroupRequests());
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);
            pittSocial.login("1@1.org", "111");
            System.err.println(pittSocial.showGroupRequests());

            //test confirm friend and group requests
            pittSocial.resolveFriendRequest(2, false);
            pittSocial.printTable("Friend", friendCols);
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.resolveFriendRequest(3, true);
            pittSocial.printTable("Friend", friendCols);
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("5@5.org", "555");
            pittSocial.resolveFriendRequest(2, true);
            pittSocial.printTable("Friend", friendCols);
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("2@2.org", "222");
            pittSocial.resolveFriendRequest(3, true);
            pittSocial.printTable("Friend", friendCols);
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.resolveFriendRequest(1, true);
            pittSocial.printTable("Friend", friendCols);
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("7@7.org", "777");
            pittSocial.resolveFriendRequest(4, true);
            pittSocial.printTable("Friend", friendCols);
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.resolveFriendRequest(2, true);
            pittSocial.printTable("Friend", friendCols);
            pittSocial.printTable("PendingFriend", pendingFriendCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("3@3.org", "333");
            pittSocial.resolveGroupRequest(1, 4, false);
            pittSocial.printTable("GroupMember", groupMemberCols);
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);
            pittSocial.resolveGroupRequest(2, 4, false);
            pittSocial.printTable("GroupMember", groupMemberCols);
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);
            pittSocial.resolveGroupRequest(2, 2, true);
            pittSocial.printTable("GroupMember", groupMemberCols);
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);
            pittSocial.resolveGroupRequest(2, 7, true);
            pittSocial.printTable("GroupMember", groupMemberCols);
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);
            pittSocial.resolveGroupRequest(2, 6, true);
            pittSocial.printTable("GroupMember", groupMemberCols);
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("1@1.org", "111");
            pittSocial.resolveGroupRequest(3, 5, true);
            pittSocial.printTable("GroupMember", groupMemberCols);
            pittSocial.printTable("PendingGroupMember", pendingGroupMemberCols);

            //test sendMessageToUser
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);
            pittSocial.login("5@5.org", "555");
            pittSocial.sendMessageToUser(2, "5 saying hi to 2");
            pittSocial.printTable("MessageInfo", messageInfoCols);
            pittSocial.printTable("MessageRecipient", messageRecipientCols);
            pittSocial.sendMessageToUser(2, "5 saying hi to 2");
            pittSocial.printTable("MessageInfo", messageInfoCols);
            pittSocial.printTable("MessageRecipient", messageRecipientCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("2@2.org", "222");
            pittSocial.sendMessageToUser(5, "2 saying hi to 5");
            pittSocial.printTable("MessageInfo", messageInfoCols);
            pittSocial.printTable("MessageRecipient", messageRecipientCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("1@1.org", "111");
            pittSocial.sendMessageToUser(3, "1 saying hi to 3");
            pittSocial.printTable("MessageInfo", messageInfoCols);
            pittSocial.printTable("MessageRecipient", messageRecipientCols);
            pittSocial.sendMessageToUser(2, "1 saying hi to 2");
            pittSocial.printTable("MessageInfo", messageInfoCols);
            pittSocial.printTable("MessageRecipient", messageRecipientCols);
            pittSocial.sendMessageToUser(2, "1 saying hi to 2");
            pittSocial.printTable("MessageInfo", messageInfoCols);
            pittSocial.printTable("MessageRecipient", messageRecipientCols);

            //test sendMessageToGroup
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("5@5.org", "555");
            pittSocial.sendMessageToGroup(3, "5 saying hi to group 3");
            pittSocial.printTable("MessageInfo", messageInfoCols);
            pittSocial.printTable("MessageRecipient", messageRecipientCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("1@1.org", "111");
            pittSocial.sendMessageToGroup(3, "1 saying hi to group 3");
            pittSocial.printTable("MessageInfo", messageInfoCols);
            pittSocial.printTable("MessageRecipient", messageRecipientCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("6@6.org", "666");
            pittSocial.sendMessageToGroup(2, "6 saying hi to group 2");
            pittSocial.printTable("MessageInfo", messageInfoCols);
            pittSocial.printTable("MessageRecipient", messageRecipientCols);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);
            pittSocial.login("1@1.org", "111");

            //test display messages and display new messages
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("2@2.org", "222");
            pittSocial.displayMessages();
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("3@3.org", "333");
            pittSocial.displayMessages();
            pittSocial.sendMessageToGroup(2, "3 saying hi to group 2");
            pittSocial.sendMessageToGroup(2, "3 saying hi to group 2");
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("7@7.org", "777");
            pittSocial.sendMessageToUser(2, "7 saying hi to 2");
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("2@2.org", "222");
            pittSocial.displayNewMessages();
            pittSocial.displayMessages();

            //test displayFriends
            pittSocial.displayFriends();
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);
            pittSocial.login("3@3.org", "333");
            pittSocial.displayFriends();

            //test display profile
            pittSocial.displayProfile(1);
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);
            pittSocial.login("2@2.org", "222");
            pittSocial.displayProfile(7);

            //test searchForUser
            System.out.println(pittSocial.searchForUser("hoho 2 2 1 2"));
            System.out.println(pittSocial.searchForUser("org"));
            System.out.println(pittSocial.searchForUser("jojo 3 4"));
            System.out.println(pittSocial.searchForUser("nonsense query"));

            //test threeDegrees
            System.out.println(pittSocial.threeDegrees(1));
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("7@7.org", "777");
            System.out.println(pittSocial.threeDegrees(1));
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("1@1.org", "111");
            System.out.println(pittSocial.threeDegrees(5));

            //test topMessages
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("2@2.org", "222");
            System.out.println(pittSocial.displayTopKMessages(3,1));
            System.out.println(pittSocial.displayTopKMessages(1,1));
            System.out.println(pittSocial.displayTopKMessages(2,6));
            pittSocial.logout();
            pittSocial.printTable("Profile", profileCols);

            pittSocial.login("3@3.org", "333");
            System.out.println(pittSocial.displayTopKMessages(10000,10));


            //test dropUser
            pittSocial.dropUser();
            pittSocial.printTable("Profile", profileCols);
            pittSocial.login("7@7.org", "777");
            pittSocial.dropUser();
            pittSocial.printTable("Profile", profileCols);




            //insert the test data from DataSource

            //System.out.println("\n\n---------------------- REINITIALIZING DATABASE WITH TEST DATA FROM SQL ----------------------\n\n");
            //dataManager.initDatabaseWithTest(true);

        } else {
            dataManager.initDatabase(false);
        }
    }
}
