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


            char[] profileCols = {'i','s','s','s','t',};


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
            System.out.println(pittSocial.login("1@1.org", "111"));
            pittSocial.initiateFriendship(4, "i'm NO. 1, add me NO. 4");
            pittSocial.initiateFriendship(2, "i'm NO. 1, add me NO. 2");
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("2@2.org", "222"));
            pittSocial.initiateFriendship(3, "i'm NO. 2, add me NO. 3");
            pittSocial.initiateFriendship(1, "i'm NO. 2, add me NO. 1");
            pittSocial.initiateFriendship(5, "i'm NO. 2, add me NO. 5");
            pittSocial.initiateFriendship(7, "i'm NO. 2, add me NO. 7");
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("3@3.org", "333"));
            pittSocial.initiateFriendship(1, "i'm NO. 3, add me NO. 1");
            pittSocial.initiateFriendship(2, "i'm NO. 3, add me NO. 2");

            //test create group
            pittSocial.createGroup("lame", 5, "group 1 description");
            pittSocial.createGroup("lame2", 5, "group 2 description");
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("1@1.org", "111"));
            pittSocial.createGroup("not lame", 5, "group 3 description");

            //test initiateGroup
            pittSocial.initiateAddingGroup(1, "my id is 1, put me into group 1");
            pittSocial.initiateAddingGroup(2, "my id is 1, put me into group 2");
            System.out.println(pittSocial.logout());

            System.out.println(pittSocial.login("2@2.org", "222"));
            pittSocial.initiateAddingGroup(2, "my id is 2, put me into group 2");
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("5@5.org", "555"));
            pittSocial.initiateAddingGroup(3, "my id is 5, put me into group 3");
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("4@4.org", "444"));
            pittSocial.initiateAddingGroup(1, "my id is 4, put me into group 1");
            pittSocial.initiateAddingGroup(2, "my id is 4, put me into group 2");
            pittSocial.initiateFriendship(7, "AAA");
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("6@6.org", "666"));
            pittSocial.initiateAddingGroup(2, "my id is 6, put me into group 2");
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("7@7.org", "777"));
            pittSocial.initiateAddingGroup(2, "my id is 7, put me into group 2");

            // test showFriendsRequests
            System.err.println(pittSocial.showFriendRequests());
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("1@1.org", "111"));
            System.err.println(pittSocial.showFriendRequests());

            //test showGroupRequests
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("3@3.org", "333"));
            System.err.println(pittSocial.showGroupRequests());
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("1@1.org", "111"));
            System.err.println(pittSocial.showGroupRequests());

//        test confirm friend and group requests
            pittSocial.resolveFriendRequest(2, false);
            pittSocial.resolveFriendRequest(3, true);
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("5@5.org", "555"));
            pittSocial.resolveFriendRequest(2, true);
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("2@2.org", "222"));
            pittSocial.resolveFriendRequest(3, true);
            pittSocial.resolveFriendRequest(1, true);
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("7@7.org", "777"));
            pittSocial.resolveFriendRequest(4, true);
            pittSocial.resolveFriendRequest(2, true);

            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("3@3.org", "333"));
            System.out.println(pittSocial.resolveGroupRequest(1, 4, false));
            System.out.println(pittSocial.resolveGroupRequest(2, 4, false));
            System.out.println(pittSocial.resolveGroupRequest(2, 2, true));
            System.out.println(pittSocial.resolveGroupRequest(2, 7, true));
            System.out.println(pittSocial.resolveGroupRequest(2, 6, true));
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("1@1.org", "111"));
            pittSocial.resolveGroupRequest(3, 5, true);

            //test sendMessageToUser
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("5@5.org", "555"));
            System.err.println(pittSocial.sendMessageToUser(2, "5 saying hi to 2"));
            System.err.println(pittSocial.sendMessageToUser(2, "5 saying hi to 2"));
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("2@2.org", "222"));
            System.err.println(pittSocial.sendMessageToUser(5, "2 saying hi to 5"));
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("1@1.org", "111"));
            System.err.println(pittSocial.sendMessageToUser(3, "1 saying hi to 3"));
            System.err.println(pittSocial.sendMessageToUser(2, "1 saying hi to 2"));
            System.err.println(pittSocial.sendMessageToUser(2, "1 saying hi to 2"));

            //test sendMessageToGroup
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("5@5.org", "555"));
            System.err.println(pittSocial.sendMessageToGroup(3, "5 saying hi to group 3"));
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("1@1.org", "111"));
            System.err.println(pittSocial.sendMessageToGroup(3, "1 saying hi to group 3"));
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("6@6.org", "666"));
            System.err.println(pittSocial.sendMessageToGroup(2, "6 saying hi to group 2"));
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("1@1.org", "111"));

            //test display messages and display new messages
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("2@2.org", "222"));
            System.err.println(pittSocial.displayMessages());
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("3@3.org", "333"));
            System.err.println(pittSocial.displayMessages());
            System.err.println(pittSocial.sendMessageToGroup(2, "3 saying hi to group 2"));
            System.err.println(pittSocial.sendMessageToGroup(2, "3 saying hi to group 2"));
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("7@7.org", "777"));
            System.err.println(pittSocial.sendMessageToUser(2, "7 saying hi to 2"));
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("2@2.org", "222"));
            System.err.println(pittSocial.displayNewMessages());
            System.err.println(pittSocial.displayMessages());

            //test displayFriends
            System.err.println(pittSocial.displayFriends());
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("3@3.org", "333"));
            System.err.println(pittSocial.displayFriends());

            //test display profile
            System.err.println(pittSocial.displayProfile(1));
            System.out.println(pittSocial.logout());
            System.out.println(pittSocial.login("2@2.org", "222"));
            System.err.println(pittSocial.displayProfile(7));



            //insert the test data from DataSource
            System.out.println("\n\n---------------------- REINITIALIZING DATABASE WITH TEST DATA FROM SQL ----------------------\n\n");
            dataManager.initDatabaseWithTest(true);

        } else {
            dataManager.initDatabase(false);
        }
        /*---- schema.sql contains 'set search_path to pitt_social;' !!! --- */




        Scanner confirm = new Scanner(System.in);
        System.out.println("proceed to the main menu? (Y/N)");
        if (confirm.nextLine().equalsIgnoreCase("Y")) {
            enterMainMenu(pittSocial);
        }

    }

    public static void printMainMenu(boolean loginStatus) {
        StringBuilder menu = new StringBuilder(InfoPrinter.createTitle("Welcome to Pitt Social", 60));
        menu.append("\nOperations: \n");
        ArrayList<String> operationList = new ArrayList<>();
        if (!loginStatus) {
            operationList.add("Login");
        } else {
            operationList.add("Logout");
        }
        operationList.addAll(Arrays.asList("Register", "Friends Management", "Group Management", "Requests Management", "Messages Management", "Exit"));

        for (int i = 0; i < operationList.size(); i++) {
            menu.append("(").append(i).append(")\t").append(operationList.get(i)).append("\n");
        }
        System.out.println(menu);
    }

    public static void enterMainMenu(PittSocial pittSocial) throws IOException, InterruptedException {
        Console console = System.console();
        if (console == null) {
            System.err.println("Please run in Console!");
            System.exit(-1);
        }
        while (true) {
            flushConsole();
            printMainMenu(pittSocial.isLoggedIn());
            String raw_input = System.console().readLine();
            try {
                int inputSelection = Integer.parseInt(raw_input);
                if (inputSelection == 0) {
                    if (!pittSocial.isLoggedIn()) {
                        try {
                            enterLoginMenu(pittSocial, console);
                        } catch (SQLException e) {
                            System.out.println("Internal Error");
                            e.printStackTrace();
                            console.reader().read();
                        }
                    } else {
                        try {
                            enterLogoutMenu(pittSocial, console);
                        } catch (SQLException e) {
                            System.out.println("Internal Error");
                            e.printStackTrace();
                            console.reader().read();
                        }
                    }
                } else if (inputSelection == 1) {
                    enterRegisterMenu(pittSocial, console);
                } else if (inputSelection == 2) {
                    enterUserManagementMenu(pittSocial, console);
                } else if (inputSelection == 3) {
                    enterGroupManagementMenu(pittSocial, console);
                } else if (inputSelection == 4) {
                    enterRequestsManagementMenu(pittSocial, console);
                } else if (inputSelection == 5) {
                    enterMessagesManagementMenu(pittSocial, console);
                } else if (inputSelection == 6) {
                    InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Are you sure to exit? (Y / other key)");
                    boolean exitFlag = console.readLine().equalsIgnoreCase("Y");
                    if (exitFlag) {
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Please enter an Integer Number");
            }
        }

    }


    public static void printRegisterMenu() {
        StringBuilder menu = new StringBuilder(InfoPrinter.createTitle("Register", 60));
        menu.append("\nOperations: \n");
        ArrayList<String> operationList = new ArrayList<>(Arrays.asList("Create User", "Drop User"));
        operationList.add("Upper Menu");
        for (int i = 0; i < operationList.size(); i++) {
            menu.append("(").append(i).append(")\t").append(operationList.get(i)).append("\n");
        }
        System.out.println(menu);
    }

    public static void enterRegisterMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException {
        while (true) {
            flushConsole();
            printRegisterMenu();
            String raw_input = System.console().readLine();
            try {
                int inputSelection = Integer.parseInt(raw_input);
                if (inputSelection == 0) {
                    try {
                        enterCreateUserMenu(pittSocial, console);
                    } catch (SQLException e) {
                        System.out.println("Internal Error");
                        e.printStackTrace();
                        console.reader().read();
                    }
                } else if (inputSelection == 1) {
                    try {
                        enterDropUserMenu(pittSocial, console);
                    } catch (SQLException e) {
                        System.out.println("Internal Error");
                        e.printStackTrace();
                        console.reader().read();
                    }
                } else if (inputSelection == 2) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Please enter an Integer Number");
            }
        }
        enterMainMenu(pittSocial);
    }

    public static void printCreateUserMenu() {
        System.out.println(InfoPrinter.createTitle("Create User", 60) + "\n");
    }

    public static void enterCreateUserMenu(PittSocial pittSocial, Console console) throws SQLException, IOException, InterruptedException {
        flushConsole();
        printCreateUserMenu();
        System.out.print("Input User Name: ");
        String userName = console.readLine();
        String email = "";

        while (true) {
            System.out.print("Input Email: ");
            email = console.readLine();
            if (!email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
                System.out.println("Please enter a correct email !!");
                continue;
            }
            break;
        }
        String birthDate = "";
        while (true) {
            try {
                System.out.print("Input Birth Date (YYYY-MM-DD): ");
                birthDate = console.readLine();
                Date.valueOf(birthDate);
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a correct date of birth !!");
                continue;
            }
            break;
        }
        System.out.print("Input Password: ");
        String password = new String(console.readPassword());

        System.out.println("Are you sure to creat the user: (Y / other key)");
        System.out.println("UserName: " + userName);
        System.out.println("Email: " + email);
        System.out.println("Birth Date: " + birthDate);

        String confirm = console.readLine();
        if (confirm.equalsIgnoreCase("Y")) {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Creating User...");
            pittSocial.createUser(userName, email, password, birthDate);
            InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Creation Success, Press Enter...");
            console.reader().read();
        } else {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Cancelled Creation, Press Enter...");
            console.reader().read();
        }
    }

    public static void printDropUserMenu() {
        System.out.println(InfoPrinter.createTitle("Drop User", 60) + "\n");
    }

    public static void enterDropUserMenu(PittSocial pittSocial, Console console) throws SQLException, IOException, InterruptedException {
        if (pittSocial.isLoggedIn()) {
            flushConsole();
            printDropUserMenu();
            System.out.println("Are you sure to drop the current user? (Y / other key) (This will delete ALL the information of the current user)");
            String confirm = console.readLine();
            if (confirm.equalsIgnoreCase("Y")) {
                InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Dropping User...");
                pittSocial.dropUser();
                InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Drop User Success, Press Enter...");
                console.reader().read();
            } else {
                InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Cancelled Drop User, Press Enter...");
                console.reader().read();
            }
        } else {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Please Login First...");
            console.reader().read();
        }
    }

    public static void printUserManagementMenu() {
        StringBuilder menu = new StringBuilder(InfoPrinter.createTitle("User Management", 60));
        menu.append("\nOperations: \n");
        ArrayList<String> operationList = new ArrayList<>(Arrays.asList("Friends Management", "Search User", "Show Three Degrees Friends"));
        operationList.add("Upper Menu");
        for (int i = 0; i < operationList.size(); i++) {
            menu.append("(").append(i).append(")\t").append(operationList.get(i)).append("\n");
        }
        System.out.println(menu);
    }

    public static void enterUserManagementMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException {
        if (pittSocial.isLoggedIn()) {
            while (true) {
                flushConsole();
                printUserManagementMenu();
                String raw_input = System.console().readLine();
                try {
                    int inputSelection = Integer.parseInt(raw_input);
                    if (inputSelection == 0) {
                        enterFriendsManagementMenu(pittSocial, console);
                    } else if (inputSelection == 1) {
                        try {
                            enterSearchUserMenu(pittSocial, console);
                        } catch (SQLException e) {
                            System.out.println("Internal Error");
                            e.printStackTrace();
                            console.reader().read();
                        }
                    } else if (inputSelection == 2) {
                        try {
                            enterShowThreeDegreesFriendsMenu(pittSocial, console);
                        } catch (SQLException e) {
                            System.out.println("Internal Error");
                            e.printStackTrace();
                            console.reader().read();
                        }
                    } else if (inputSelection == 3) {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Please enter an Integer Number");
                }
            }
        } else {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Please Login First...");
            console.reader().read();
        }
    }

    public static void printFriendsManagementMenu() {
        StringBuilder menu = new StringBuilder(InfoPrinter.createTitle("Friends Management", 60));
        menu.append("\nOperations: \n");
        ArrayList<String> operationList = new ArrayList<>(Arrays.asList("Initiate Friendship", "Display Friends"));
        operationList.add("Upper Menu");
        for (int i = 0; i < operationList.size(); i++) {
            menu.append("(").append(i).append(")\t").append(operationList.get(i)).append("\n");
        }
        System.out.println(menu);
    }

    public static void enterFriendsManagementMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException {
        while (true) {
            flushConsole();
            printFriendsManagementMenu();
            String raw_input = System.console().readLine();
            try {
                int inputSelection = Integer.parseInt(raw_input);
                if (inputSelection == 0) {
                    try {
                        enterInitiateFriendShipMenu(pittSocial, console);
                    } catch (SQLException e) {
                        System.out.println("Internal Error");
                        e.printStackTrace();
                        console.reader().read();
                    }
                } else if (inputSelection == 1) {
                    try {
                        enterDisplayFriendsMenu(pittSocial, console);
                    } catch (SQLException e) {
                        System.out.println("Internal Error");
                        e.printStackTrace();
                        console.reader().read();
                    }
                } else if (inputSelection == 2) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Please enter an Integer Number");
            }
        }
    }


    public static void printInitiateFriendshipMenu() {
        System.out.println(InfoPrinter.createTitle("Initiate Friendship", 60) + "\n");
    }

    public static void enterInitiateFriendShipMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        flushConsole();
        printInitiateFriendshipMenu();
        int userId;
        while (true) {
            try {
                System.out.print("Input User Id: ");
                userId = Integer.parseInt(console.readLine());
                System.out.println("You are initiating friendship with User: " + pittSocial.getUserNameFromId(userId));
                break;
            } catch (NumberFormatException e) {
                System.err.println("Please enter an Integer Number");
            }
        }
        System.out.print("Input Message: ");
        String message = console.readLine();

        System.out.println("Are you sure to initiate friendship: (Y / other key)");
        System.out.println("User Id: " + userId);
        System.out.println("Message: " + message);

        String confirm = console.readLine();
        if (confirm.equalsIgnoreCase("Y")) {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Initiating Friendship...");
            pittSocial.initiateFriendship(userId, message);
            InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Initiate Friendship Success, Press Enter...");
            console.reader().read();
        } else {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Cancelled Initiating Friendship, Press Enter...");
            console.reader().read();
        }
    }

    public static void printDisplayFriendsMenu() {
        System.out.println(InfoPrinter.createTitle("Display Friends", 60) + "\n");

    }

    public static void enterDisplayFriendsMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        flushConsole();
        printDisplayFriendsMenu();
        System.out.println(pittSocial.displayFriends());
        while (true) {
            try {
                System.out.println("Input User Id to show Profile, (E) to exit...");
                String confirm = console.readLine();
                if (confirm.equalsIgnoreCase("E")) {
                    break;
                } else {
                    int userId = Integer.parseInt(confirm);
                    System.out.println(pittSocial.displayProfile(userId));
                }
            } catch (NumberFormatException e) {
                System.err.println("Please enter an Integer Number");
            }
        }
        InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Display Finished, Press Enter...");
        console.reader().read();
    }

    public static void printSearchUserMenu() {
        System.out.println(InfoPrinter.createTitle("Search User", 60) + "\n");

    }

    public static void enterSearchUserMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        flushConsole();
        printSearchUserMenu();
        System.out.print("Input User Name: ");
        String keyword = console.readLine();
        System.out.println(pittSocial.searchForUser(keyword));
        InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Search User Success, Press Enter...");
        console.reader().read();
    }

    public static void printShowThreeDegreesFriendsMenu() {
        System.out.println(InfoPrinter.createTitle("Three Degrees Friends", 60) + "\n");

    }

    public static void enterShowThreeDegreesFriendsMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        flushConsole();
        printShowThreeDegreesFriendsMenu();
        System.out.print("Input User Name: ");
        int targetUserId;
        while (true) {
            try {
                targetUserId = Integer.parseInt(console.readLine());
                break;
            } catch (NumberFormatException e) {
                System.err.println("Please enter an Integer Number");
            }
        }
        System.out.println(pittSocial.threeDegrees(targetUserId));
        InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Show Three Degrees Friends Success, Press Enter...");
        console.reader().read();
    }


    public static void printGroupManagementMenu() {
        StringBuilder menu = new StringBuilder(InfoPrinter.createTitle("Group Management", 60));
        menu.append("\nOperations: \n");
        ArrayList<String> operationList = new ArrayList<>(Arrays.asList("Create Group", "Initiate Adding Group"));
        operationList.add("Upper Menu");
        for (int i = 0; i < operationList.size(); i++) {
            menu.append("(").append(i).append(")\t").append(operationList.get(i)).append("\n");
        }
        System.out.println(menu);
    }

    public static void enterGroupManagementMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException {
        if (pittSocial.isLoggedIn()) {
            while (true) {
                flushConsole();
                printGroupManagementMenu();
                String raw_input = System.console().readLine();
                try {
                    int inputSelection = Integer.parseInt(raw_input);
                    if (inputSelection == 0) {
                        try {
                            enterCreateGroupMenu(pittSocial, console);
                        } catch (SQLException e) {
                            System.out.println("Internal Error");
                            e.printStackTrace();
                            console.reader().read();
                        }
                    } else if (inputSelection == 1) {
                        try {
                            enterInitiateAddingGroupMenu(pittSocial, console);
                        } catch (SQLException e) {
                            System.out.println("Internal Error");
                            e.printStackTrace();
                            console.reader().read();
                        }
                    } else if (inputSelection == 2) {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Please enter an Integer Number");
                }
            }
        } else {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Please Login First...");
            console.reader().read();
        }
    }

    public static void printCreateGroupMenu() {
        System.out.println(InfoPrinter.createTitle("Create Group", 60) + "\n");
    }

    public static void enterCreateGroupMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        flushConsole();
        printCreateGroupMenu();
        System.out.print("Input Group Name: ");
        String groupName = console.readLine();
        System.out.print("Input Group Limit: ");
        int limit = Integer.parseInt(console.readLine());
        System.out.print("Input Group Description: ");
        String description = console.readLine();

        System.out.println("Are you sure to create the group: (Y / other key)");
        System.out.println("Group Name: " + groupName);
        System.out.println("Limit: " + limit);
        System.out.println("Description: " + description);

        String confirm = console.readLine();
        if (confirm.equalsIgnoreCase("Y")) {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Creating Group...");
            pittSocial.createGroup(groupName, limit, description);
            InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Creation Success, Press Enter...");
            console.reader().read();
        } else {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Cancelled Creation, Press Enter...");
            console.reader().read();
        }
    }

    public static void printInitiateAddingGroupMenu() {
        System.out.println(InfoPrinter.createTitle("Initiate Adding Group", 60) + "\n");
    }

    public static void enterInitiateAddingGroupMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        flushConsole();
        printInitiateAddingGroupMenu();
        int groupId;
        while (true) {
            try {
                System.out.print("Input Group Id: ");
                groupId = Integer.parseInt(console.readLine());
                break;
            } catch (NumberFormatException e) {
                System.err.println("Please enter an Integer Number");
            }
        }
        System.out.print("Input Message: ");
        String message = console.readLine();

        System.out.println("Are you sure to initiate adding group: (Y / other key)");
        System.out.println("Group Id: " + groupId);
        System.out.println("Message: " + message);

        String confirm = console.readLine();
        if (confirm.equalsIgnoreCase("Y")) {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Initiating Adding Group...");
            pittSocial.initiateAddingGroup(groupId, message);
            InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Initiate Adding Group Success, Press Enter...");
            console.reader().read();
        } else {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Cancelled Initiate Adding Group, Press Enter...");
            console.reader().read();
        }
    }

    public static void printRequestsManagementMenu() {
        StringBuilder menu = new StringBuilder(InfoPrinter.createTitle("Requests Management", 60));
        menu.append("\nOperations: \n");
        ArrayList<String> operationList = new ArrayList<>(Arrays.asList("Friendship Requests", "Group Requests"));
        operationList.add("Upper Menu");
        for (int i = 0; i < operationList.size(); i++) {
            menu.append("(").append(i).append(")\t").append(operationList.get(i)).append("\n");
        }
        System.out.println(menu);
    }

    public static void enterRequestsManagementMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException {
        if (pittSocial.isLoggedIn()) {
            while (true) {
                flushConsole();
                printRequestsManagementMenu();
                String raw_input = System.console().readLine();
                try {
                    int inputSelection = Integer.parseInt(raw_input);
                    if (inputSelection == 0) {
                        try {
                            enterFriendShipRequestsMenu(pittSocial, console);
                        } catch (SQLException e) {
                            System.out.println("Internal Error");
                            e.printStackTrace();
                            console.reader().read();
                        }
                    } else if (inputSelection == 1) {
                        try {
                            enterGroupRequestsMenu(pittSocial, console);
                        } catch (SQLException e) {
                            System.out.println("Internal Error");
                            e.printStackTrace();
                            console.reader().read();
                        }
                    } else if (inputSelection == 2) {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Please enter an Integer Number");
                }
            }
        } else {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Please Login First...");
            console.reader().read();
        }
    }

    public static void printFriendShipRequestsMenu() {
        System.out.println(InfoPrinter.createTitle("Friendship Requests", 60) + "\n");
    }

    public static void enterFriendShipRequestsMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        while (true) {
            flushConsole();
            printFriendShipRequestsMenu();
            System.out.println(pittSocial.showFriendRequests());
            StringBuilder menu = new StringBuilder();
            menu.append("\nOperations: \n");
            ArrayList<String> operationList = new ArrayList<>(Arrays.asList("Resolve Request"));
            operationList.add("Upper Menu");
            for (int i = 0; i < operationList.size(); i++) {
                menu.append("(").append(i).append(")\t").append(operationList.get(i)).append("\n");
            }
            System.out.println(menu);

            String raw_input = System.console().readLine();
            try {
                int inputSelection = Integer.parseInt(raw_input);
                if (inputSelection == 0) {
                    try {
                        enterResolveFriendShipRequestsMenu(pittSocial, console);
                    } catch (SQLException e) {
                        System.out.println("Internal Error");
                        e.printStackTrace();
                        console.reader().read();
                    }
                } else if (inputSelection == 1) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Please enter an Integer Number");
            }
        }
    }


    public static void enterResolveFriendShipRequestsMenu(PittSocial pittSocial, Console console) throws IOException, SQLException {
        int userId;
        while (true) {
            System.out.println("Input user id to resolve, (E) to exit resolving request and DECLINE ALL requests, (R) to resolve ALL requests");
            String confirm = console.readLine();
            if (confirm.equalsIgnoreCase("E")) {
                ArrayList<Integer> remainingRequestList = pittSocial.getFriendRequests();
                for(int _userId : remainingRequestList) {
                    pittSocial.resolveFriendRequest(_userId, false);
                }
                InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Deny ALL friend request Success, Press Enter...");
                console.reader().read();
                break;
            } else if (confirm.equalsIgnoreCase("R")) {
                ArrayList<Integer> remainingRequestList = pittSocial.getFriendRequests();
                for(int _userId : remainingRequestList) {
                    pittSocial.resolveFriendRequest(_userId, true);
                }
                InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Accept ALL friend request Success, Press Enter...");
                console.reader().read();
                break;
            } else {
                try {
                    userId = Integer.parseInt(confirm);
                } catch (NumberFormatException e) {
                    System.err.println("Please enter an Integer Number");
                    continue;
                }
                System.out.println("Do you want to add user [" + userId + "] as friend? (Y / other key)");
                String confirmAdd = console.readLine();
                if (confirmAdd.equalsIgnoreCase("Y")) {
                    InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Resolving Friend Request...");
                    pittSocial.resolveFriendRequest(userId, true);
                    InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Accept friend request Success, Press Enter...");
                    console.reader().read();
                } else {
                    InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Resolving Friend Request...");
                    pittSocial.resolveFriendRequest(userId, false);
                    InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Deny friend request Success, Press Enter...");
                    console.reader().read();
                }
            }
        }
    }


    public static void printGroupRequestsMenu() {
        System.out.println(InfoPrinter.createTitle("Group Requests", 60) + "\n");
    }

    public static void enterGroupRequestsMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        while (true) {
            flushConsole();
            printGroupRequestsMenu();
            System.out.println(pittSocial.showGroupRequests());
            StringBuilder menu = new StringBuilder();
            menu.append("\nOperations: \n");
            ArrayList<String> operationList = new ArrayList<>(Arrays.asList("Resolve Request"));
            operationList.add("Upper Menu");
            for (int i = 0; i < operationList.size(); i++) {
                menu.append("(").append(i).append(")\t").append(operationList.get(i)).append("\n");
            }
            System.out.println(menu);

            String raw_input = System.console().readLine();
            try {
                int inputSelection = Integer.parseInt(raw_input);
                if (inputSelection == 0) {
                    try {
                        enterResolveGroupRequestsMenu(pittSocial, console);
                    } catch (SQLException e) {
                        System.out.println("Internal Error");
                        e.printStackTrace();
                        console.reader().read();
                    }
                } else if (inputSelection == 1) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Please enter an Integer Number");
            }
        }
    }

    public static void enterResolveGroupRequestsMenu(PittSocial pittSocial, Console console) throws IOException, SQLException {
        int groupId;
        int userId;
        while (true) {
            System.out.println("Input group id to resolve, (E) to exit resolving request and DECLINE ALL requests, (R) to resolve ALL requests");
            String confirm = console.readLine();
            if  (confirm.equalsIgnoreCase("E")) {
                ArrayList<Pair<Integer, Integer>> remainingRequestList = pittSocial.getGroupRequests();
                for(Pair<Integer, Integer> _requestInfo : remainingRequestList) {
                    pittSocial.resolveGroupRequest(_requestInfo.getKey(), _requestInfo.getValue(), false);
                }
                InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Deny ALL group request Success, Press Enter...");
                console.reader().read();
                break;
            } else if (confirm.equalsIgnoreCase("R")) {
                ArrayList<Pair<Integer, Integer>> remainingRequestList = pittSocial.getGroupRequests();
                for(Pair<Integer, Integer> _requestInfo : remainingRequestList) {
                    pittSocial.resolveGroupRequest(_requestInfo.getKey(), _requestInfo.getValue(), true);
                }
                InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Accept ALL group request Success, Press Enter...");
                console.reader().read();
                break;
            } else {
                try {
                    groupId = Integer.parseInt(confirm);
                    System.out.print("Input user id to resolve: ");
                    userId = Integer.parseInt(console.readLine());
                } catch (NumberFormatException e) {
                    System.err.println("Please enter an Integer Number");
                    continue;
                }

                System.out.println("Do you want to add this user [" + userId + "] to group [" + groupId + "] (Y / other key)");
                String confirmAdd = console.readLine();
                if (confirmAdd.equalsIgnoreCase("Y")) {
                    InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Resolving Group Request...");
                    boolean result = pittSocial.resolveGroupRequest(groupId, userId, true);
                    if (result) {
                        InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Accept group request Success, Press Enter...");
                    } else {
                        InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Accept group request Fail, group Press Enter...");
                    }
                    console.reader().read();
                } else {
                    InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Resolving Group Request...");
                    boolean result = pittSocial.resolveGroupRequest(groupId, userId, false);
                    InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Deny friend request Success, Press Enter...");
                    console.reader().read();
                }
            }
        }
    }

    public static void printMessagesManagementMenu() {
        StringBuilder menu = new StringBuilder(InfoPrinter.createTitle("Message Management", 60));
        menu.append("\nOperations: \n");
        ArrayList<String> operationList = new ArrayList<>(Arrays.asList("Display All Messages", "Display New Messages", "Display Top K Messages", "Send Message to User", "Send Message to Group"));
        operationList.add("Upper Menu");
        for (int i = 0; i < operationList.size(); i++) {
            menu.append("(").append(i).append(")\t").append(operationList.get(i)).append("\n");
        }
        System.out.println(menu);
    }

    public static void enterMessagesManagementMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException {
        if (pittSocial.isLoggedIn()) {
            while (true) {
                flushConsole();
                printMessagesManagementMenu();
                String raw_input = System.console().readLine();
                try {
                    int inputSelection = Integer.parseInt(raw_input);
                    if (inputSelection == 0) {
                        try {
                            enterDisplayAllMessagesMenu(pittSocial, console);
                        } catch (SQLException e) {
                            System.out.println("Internal Error");
                            e.printStackTrace();
                            console.reader().read();
                        }
                    } else if (inputSelection == 1) {
                        try {
                            enterDisplayNewMessagesMenu(pittSocial, console);
                        } catch (SQLException e) {
                            System.out.println("Internal Error");
                            e.printStackTrace();
                            console.reader().read();
                        }
                    } else if (inputSelection == 2) {
                        try {
                            enterDisplayTopKMessagesMenu(pittSocial, console);
                        } catch (SQLException e) {
                            System.out.println("Internal Error");
                            e.printStackTrace();
                            console.reader().read();
                        }
                    } else if (inputSelection == 3) {
                        try {
                            enterSendMessageToUserMenu(pittSocial, console);
                        } catch (SQLException e) {
                            System.out.println("Internal Error");
                            e.printStackTrace();
                            console.reader().read();
                        }
                    } else if (inputSelection == 4) {
                        try {
                            enterSendMessageToGroupMenu(pittSocial, console);
                        } catch (SQLException e) {
                            System.out.println("Internal Error");
                            e.printStackTrace();
                            console.reader().read();
                        }
                    } else if (inputSelection == 5) {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Please enter an Integer Number");
                }
            }
        } else {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Please Login First...");
            console.reader().read();
        }
    }

    public static void printDisplayAllMessagesMenu() {
        System.out.println(InfoPrinter.createTitle("Display All Messages", 60) + "\n");
    }

    public static void enterDisplayAllMessagesMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        flushConsole();
        printDisplayAllMessagesMenu();
        System.out.println(pittSocial.displayMessages());

        InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Display All Messages Success, Press Enter...");
        console.reader().read();
    }

    public static void printDisplayNewMessagesMenu() {
        System.out.println(InfoPrinter.createTitle("Display New Messages", 60) + "\n");
    }

    public static void enterDisplayNewMessagesMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        flushConsole();
        printDisplayNewMessagesMenu();
        System.out.println(pittSocial.displayNewMessages());

        InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Display New Messages Success, Press Enter...");
        console.reader().read();
    }

    public static void printDisplayTopKMessagesMenu() {
        System.out.println(InfoPrinter.createTitle("Display Top K Users", 60) + "\n");
    }

    public static void enterDisplayTopKMessagesMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        flushConsole();
        printDisplayTopKMessagesMenu();
        int k;
        int x;
        while (true) {
            System.out.println("Please input the number of users you want to show, (E) to exit display top k messages...");
            String confirm = console.readLine();
            if (confirm.equalsIgnoreCase("E")) {
                return;
            } else {
                try {
                    k = Integer.parseInt(confirm);
                    System.out.println("Input the time range(month):");
                    x = Integer.parseInt(console.readLine());
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("Please enter an Integer Number");
                }
            }
        }
        System.out.println(pittSocial.displayTopKMessages(k, x));

        InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Display Top k Messages Success, Press Enter...");
        console.reader().read();
    }

    public static void printSendMessageToUserMenu() {
        System.out.println(InfoPrinter.createTitle("Send Message to User", 60) + "\n");
    }

    public static void enterSendMessageToUserMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        flushConsole();
        printSendMessageToUserMenu();
        int userId;
        while (true) {
            try {
                System.out.print("Input User Id: ");
                userId = Integer.parseInt(console.readLine());
                break;
            } catch (NumberFormatException e) {
                System.err.println("Please enter an Integer Number");
            }
        }
        System.out.print("Input Message: ");
        String message = console.readLine();

        System.out.println("Are you sure to Send this Message: (Y / other key)");
        System.out.println("User Id: " + userId);
        System.out.println("Message: " + message);

        String confirm = console.readLine();
        if (confirm.equalsIgnoreCase("Y")) {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Sending Message...");
            pittSocial.sendMessageToUser(userId, message);
            InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Send Message Success, Press Enter...");
            console.reader().read();
        } else {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Cancelled Sending Message, Press Enter...");
            console.reader().read();
        }
    }

    public static void printSendMessageToGroupMenu() {
        System.out.println(InfoPrinter.createTitle("Send Message to Group", 60) + "\n");
    }

    public static void enterSendMessageToGroupMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        flushConsole();
        printSendMessageToGroupMenu();
        int groupId;
        while (true) {
            try {
                System.out.print("Input Group Id: ");
                groupId = Integer.parseInt(console.readLine());
                break;
            } catch (NumberFormatException e) {
                System.err.println("Please enter an Integer Number");
            }
        }
        System.out.print("Input Message: ");
        String message = console.readLine();

        System.out.println("Are you sure to Send this Message: (Y / other key)");
        System.out.println("Group Id: " + groupId);
        System.out.println("Message: " + message);

        String confirm = console.readLine();
        if (confirm.equalsIgnoreCase("Y")) {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Sending Message...");
            pittSocial.sendMessageToGroup(groupId, message);
            InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Send Message Success, Press Enter...");
            console.reader().read();
        } else {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Cancelled Sending Message, Press Enter...");
            console.reader().read();
        }
    }


    public static void printLoginMenu() {
        System.out.println(InfoPrinter.createTitle("Login", 60) + "\n");

    }

    public static void enterLoginMenu(PittSocial pittSocial, Console console) throws IOException, InterruptedException, SQLException {
        flushConsole();
        printLoginMenu();
        System.out.print("Input Email: ");
        String email = console.readLine();
        System.out.print("Input Password: ");
        String password = new String(console.readPassword());
        int result = pittSocial.login(email, password);
        if (result == 0) {
            InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Login Success, Press Enter...");
            console.reader().read();
        } else {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Login Fail, Record does not match...");
            console.reader().read();
        }
    }

    public static void printLogoutMenu() {
        System.out.println(InfoPrinter.createTitle("Logout", 60) + "\n");
    }

    public static void enterLogoutMenu(PittSocial pittSocial, Console console) throws IOException, SQLException, InterruptedException {
        flushConsole();
        printLogoutMenu();
        System.out.println("Are you sure to logout: (Y / other key)");
        String confirm = console.readLine();
        if (confirm.equalsIgnoreCase("Y")) {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Logout User...");
            pittSocial.logout();
            InfoPrinter.printWithColor(ConsoleColors.GREEN_BRIGHT, "Logout Success, Press Enter...");
            console.reader().read();
        } else {
            InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Cancelled logout, Press Enter...");
            console.reader().read();
        }

    }

    public static void flushConsole() throws IOException, InterruptedException {
        final String os = System.getProperty("os.name");

        if (os.contains("Windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}
