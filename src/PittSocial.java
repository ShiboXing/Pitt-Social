//import javafx.util.Pair;

import javafx.util.Pair;

import java.io.Console;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;
import java.util.concurrent.Callable;

public class PittSocial {
    private Connection _conn;
    private int currentUserId;
    private Timestamp loginTime;

    public PittSocial(Connection new_conn) throws ClassNotFoundException, SQLException {
        _conn = new_conn;
        this.currentUserId = -1;
    }

    public void createUser(String username, String email, String password, String birthDate) throws SQLException {
        _conn.setAutoCommit(false);
        _conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            CallableStatement st = _conn.prepareCall("call createuser(?,?,?,?,null);");
            st.setString(1, username);
            st.setString(2, email);
            st.setString(3, password);
            st.setDate(4, Date.valueOf(birthDate));
            st.execute();
            _conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            _conn.rollback();
        } finally {
            _conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            _conn.setAutoCommit(true);
        }
    }

    public int login(String email, String password) throws IOException, SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from login(?,?);");
        st.setString(1, email);
        st.setString(2, password);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            int resID = rs.getInt(1);
            if (resID == 0) //match not found
                return -1;
            else {
                this.currentUserId = resID;
                this.loginTime = rs.getTimestamp(2);
                return 0;
            }
        }
        return -1;
    }

    public int initiateFriendship(int toid, String msg) throws SQLException {
        _conn.setAutoCommit(false);
        _conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            CallableStatement st = _conn.prepareCall("call initiatefriendship(?,?,?);");
            st.setInt(1, currentUserId);
            st.setInt(2, toid);
            st.setString(3, msg);
            st.execute();
            _conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            _conn.rollback();
        } finally {
            _conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            _conn.setAutoCommit(true);
        }
        return 0;
    }

    public int createGroup(String name, int limit, String descr) throws SQLException {
        _conn.setAutoCommit(false);
        _conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            CallableStatement st = _conn.prepareCall("call creategroup(?,?,?,?);");
            st.setInt(1, currentUserId);
            st.setString(2, name);
            st.setInt(3, limit);
            st.setString(4, descr);
            st.execute();
            _conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            _conn.rollback();
        } finally {
            _conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            _conn.setAutoCommit(true);
        }
        return 0;
    }

    public int initiateAddingGroup(int gid, String msg) throws SQLException {
        _conn.setAutoCommit(false);
        _conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            CallableStatement st = _conn.prepareCall("call initiateaddinggroup(?,?,?);");
            st.setInt(1, currentUserId);
            st.setInt(2, gid);
            st.setString(3, msg);
            st.execute();
            _conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            _conn.rollback();
        } finally {
            _conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            _conn.setAutoCommit(true);
        }
        return 0;
    }


    public String showFriendRequests() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from showfriendrequests(?);");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();

        return createDisplayFriendRequestBody(rs, 10, 50);
    }

    public ArrayList<Integer> getFriendRequests() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from showfriendrequests(?);");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        ArrayList<Integer> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rs.getInt(1));
        }
        return result;
    }

    public String showGroupRequests() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from showGrouprequests(?);");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();

        return createDisplayGroupRequestBody(rs, 10, 10, 50);
    }

    public ArrayList<Pair<Integer, Integer>> getGroupRequests() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from showGrouprequests(?);");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
        while (rs.next()) {
            result.add(new Pair<>(rs.getInt(1), rs.getInt(2)));
        }
        return result;
    }

    public int resolveFriendRequest(int fromID, boolean confirm) throws SQLException {
        _conn.setAutoCommit(false);
        _conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            CallableStatement st = _conn.prepareCall("call resolveFriendRequest(?,?,?);");
            st.setInt(1, currentUserId);
            st.setInt(2, fromID);
            st.setBoolean(3, confirm);
            st.execute();
            _conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            _conn.rollback();
        } finally {
            _conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            _conn.setAutoCommit(true);
        }
        return 0;
    }

    public boolean resolveGroupRequest(int gid, int fromID, boolean confirm) throws SQLException {
        _conn.setAutoCommit(false);
        _conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        boolean result = false;
        try {
            CallableStatement st = _conn.prepareCall("select * from resolveGroupMemberRequest(?,?,?,?);");
            st.setInt(1, currentUserId);
            st.setInt(2, gid);
            st.setInt(3, fromID);
            st.setBoolean(4, confirm);
            ResultSet rs = st.executeQuery();
            _conn.commit();
            rs.next();
            result = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            _conn.rollback();
        } finally {
            _conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            _conn.setAutoCommit(true);
        }
        return result;
    }

    public boolean sendMessageToUser(int toID, String msg) throws SQLException {
        _conn.setAutoCommit(false);
        _conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        boolean result = false;
        try {
            PreparedStatement st = _conn.prepareStatement("select * from createMessage(?,?,?,?);");
            st.setInt(1, currentUserId);
            st.setInt(2, toID);
            st.setString(3, msg);
            st.setBoolean(4, false);
            ResultSet rs = st.executeQuery();
            _conn.commit();
            rs.next();
            result = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            _conn.rollback();
        } finally {
            _conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            _conn.setAutoCommit(true);
        }
        return result;
    }

    public boolean sendMessageToGroup(int toGID, String msg) throws SQLException {
        _conn.setAutoCommit(false);
        _conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        boolean result = false;
        try {
            PreparedStatement st = _conn.prepareStatement("select * from createMessage(?,?,?,?);");
            st.setInt(1, currentUserId);
            st.setInt(2, toGID);
            st.setString(3, msg);
            st.setBoolean(4, true);
            ResultSet rs = st.executeQuery();
            _conn.commit();
            rs.next();
            result = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            _conn.rollback();
        } finally {
            _conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            _conn.setAutoCommit(true);
        }
        return result;
    }

    public String displayMessages() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from displayMessages(?)");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        return createDisplayMessageBody(rs, 10, 50, 30);
    }

    public String displayNewMessages() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from displayNewMessages(?)");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        return createDisplayMessageBody(rs, 10, 50, 30);
    }

    public String displayFriends() throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from returnFriendsList(?);");
        st.setInt(1, currentUserId);
        ResultSet rs = st.executeQuery();
        return createDisplayFriendsBody(rs, 20, 10);

    }

    public String displayProfile(int userID) throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from showProfile(?);");
        st.setInt(1, userID);
        ResultSet rs = st.executeQuery();

        return createDisplayProfileBody(rs, 44, 44 / 2);
    }

    public int logout() throws SQLException {
        _conn.setAutoCommit(false);
        _conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        int result = -1;
        try {
            CallableStatement st = _conn.prepareCall("call logout(?,?)");
            st.setInt(1, currentUserId);
            st.setTimestamp(2, loginTime);
            st.execute();
            _conn.commit();
            int resID = currentUserId;
            currentUserId = -1;
            result = resID;
        } catch (SQLException e) {
            e.printStackTrace();
            _conn.rollback();
        } finally {
            _conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            _conn.setAutoCommit(true);
        }
        return result;
    }

    public String searchForUser(String keywordListString) throws SQLException {
        ArrayList<ResultSet> resultSetList = new ArrayList<>();
        for (String keyword : keywordListString.split(" ")) {
            PreparedStatement st = _conn.prepareStatement("select * from searchForUser(?)");
            st.setString(1, "%" + keyword + "%");
            ResultSet rs = st.executeQuery();
            resultSetList.add(rs);
        }
        return createDisplaySearchUserBody(resultSetList, 20, 20, 20);
    }

    public String threeDegrees(int targetUserId) throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from ThreeDegree(?, ?)");
        st.setInt(1, this.currentUserId);
        st.setInt(2, targetUserId);
        ResultSet rs = st.executeQuery();
        return createDisplayThreeDegreesBody(rs, 50);
    }

    public String displayTopKMessages(int k, int x) throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from topMessages(?, ?, ?)");
        st.setInt(1, currentUserId);
        st.setInt(2, k);
        Date currentDate = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.MONTH, 0 - x);
        st.setTimestamp(3, new Timestamp(c.getTimeInMillis()));
        ResultSet rs = st.executeQuery();
        return createDisplayTopMessageBody(rs, 20, 20);
    }

    public int dropUser() throws SQLException {
        _conn.setAutoCommit(false);
        _conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try {
            PreparedStatement st = _conn.prepareStatement("call dropuser(?)");
            st.setInt(1, this.currentUserId);
            this.logout();
            _conn.setAutoCommit(false); //log out will set autoCommit back to true
            st.execute();
            _conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            _conn.rollback();
        } finally {
            _conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            _conn.setAutoCommit(true);
        }
        return 0;
    }

    public boolean isLoggedIn() {
        return this.currentUserId != -1;
    }

    public String getUserNameFromId(int userId) throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from returnUserName(?)");
        st.setInt(1, userId);
        ResultSet rs = st.executeQuery();
        rs.next();
        return rs.getString(1);
    }

    private String createDisplayMessageBody(ResultSet rs, int firstWidth, int secondWidth, int thirdWidth) throws SQLException {
        StringBuilder res = new StringBuilder();

        res.append(InfoPrinter.createTitle("Display Message", firstWidth + secondWidth + thirdWidth + 7));

        String format = "|%1$" + firstWidth + "s | %2$" + secondWidth + "s | %3$" + thirdWidth + "s" + " |";
        String head = String.format(format, "Sender", "Content", "Time Sent") + '\n';

        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 7) +
                "|\n";
        res.append(head2);
        while (rs.next()) {
            res.append(String.format(format, rs.getString(1), rs.getString(2), rs.getTimestamp(3))).append('\n');
        }
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 7)).append("|\n");

        return res.toString();
    }

    private String createDisplayFriendsBody(ResultSet rs, int firstWidth, int secondWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        res.append(InfoPrinter.createTitle("Display Friends", firstWidth + secondWidth + 4));
        String format = "|%1$" + firstWidth + "s | %2$" + secondWidth + "s" + " |";
        String head = String.format(format, "Name", "User ID") + '\n';
        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + 4) +
                "|\n";
        res.append(head2);
        while (rs.next())
            res.append(String.format(format, rs.getString(1), rs.getInt(2))).append('\n');
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + 4)).append("|\n");
        return res.toString();
    }

    private String createDisplayProfileBody(ResultSet rs, int firstWidth, int secondWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        String format = "| %1$" + firstWidth + "s | %2$" + secondWidth + "s |";
        String TitleFormat = "| %1$" + (firstWidth + secondWidth) + "s |";

        StringBuilder headTop = new StringBuilder("|");
        StringBuilder headMid = new StringBuilder("|");
        StringBuilder TitlePadding = new StringBuilder();
        String Title = "FRIEND PROFILE";
        for (int i = 0; i < firstWidth + secondWidth + 5; i++) {
            if (i == firstWidth + 2) {
                headMid.append('|');
                headTop.append('-');
            } else {
                headMid.append('-');
                headTop.append('-');
            }
            if (i < (firstWidth + secondWidth + 5 - Title.length()) / 2 - 1)
                TitlePadding.append(' ');
        }
        headMid.append("|\n");
        headTop.append("|\n");

        rs.next();
        res.append(headTop);
        res.append(String.format(TitleFormat.replace((firstWidth + secondWidth) + "", (firstWidth + secondWidth + 3) + ""), "")).append('\n');
        res.append(String.format(TitleFormat, TitlePadding + Title + TitlePadding + ' ')).append('\n');
        res.append(String.format(TitleFormat.replace((firstWidth + secondWidth) + "", (firstWidth + secondWidth + 3) + ""), "")).append('\n');
        res.append(headMid);
        res.append(String.format(format, "Name", rs.getString(2))).append('\n');
        res.append(headMid);
        res.append(String.format(format, "User ID", rs.getInt(1))).append('\n');
        res.append(headMid);
        res.append(String.format(format, "Email", rs.getString(3))).append('\n');
        res.append(headMid);
        res.append(String.format(format, "Date of Birth", rs.getString(4))).append('\n');
        res.append(headMid);
        return res.toString();

    }

    private String createDisplayFriendRequestBody(ResultSet rs, int firstWidth, int secondWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        res.append(InfoPrinter.createTitle("Friends Requests", firstWidth + secondWidth + 4));
        String format = "|%1$" + firstWidth + "s | %2$" + secondWidth + "s" + " |";
        String head = String.format(format, "User Id", "Message") + '\n';

        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + 4) +
                "|\n";
        res.append(head2);
        while (rs.next())
            res.append(String.format(format, rs.getInt(1), rs.getString(2))).append('\n');
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + 4)).append("|\n");

        return res.toString();
    }

    private String createDisplayGroupRequestBody(ResultSet rs, int firstWidth, int secondWidth, int thirdWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        res.append(InfoPrinter.createTitle("Group Requests", firstWidth + secondWidth + thirdWidth + 7));
        String format = "|%1$" + firstWidth + "s | %2$" + secondWidth + "s | %3$" + thirdWidth + "s" + " |";
        String head = String.format(format, "Group Id", "User Id", "Message") + '\n';

        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 7) +
                "|\n";
        res.append(head2);
        while (rs.next()) {
            res.append(String.format(format, rs.getInt(1), rs.getInt(2), rs.getString(3))).append('\n');
        }
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 7)).append("|\n");

        return res.toString();
    }

    private String createDisplaySearchUserBody(ArrayList<ResultSet> rsList, int firstWidth, int secondWidth, int thirdWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        res.append(InfoPrinter.createTitle("Search for User", firstWidth + secondWidth + thirdWidth + 7));
        String format = "|%1$" + firstWidth + "s | %2$" + secondWidth + "s | %3$" + thirdWidth + "s" + " |";
        String head = String.format(format, "User Id", "Username", "Email") + '\n';

        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 7) +
                "|\n";
        res.append(head2);
        HashSet<Integer> userIds = new HashSet<>();
        for (ResultSet rs : rsList) {
            while (rs.next()) {
                if (!userIds.contains(rs.getInt(1))) {
                    res.append(String.format(format, rs.getInt(1), rs.getString(2), rs.getString(3))).append('\n');
                    userIds.add(rs.getInt(1));
                }
            }
        }
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + thirdWidth + 7)).append("|\n");

        return res.toString();
    }

    private String createDisplayThreeDegreesBody(ResultSet rs, int firstWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        res.append(InfoPrinter.createTitle("Three Degrees", firstWidth + 1));
        String format = "|%1$" + firstWidth + "s |";
        String head = String.format(format, "Path") + '\n';

        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + 1) +
                "|\n";
        res.append(head2);
        while (rs.next()) {
            String result = rs.getString(1);
            if (result.equals("-1")) {
                result = "No Such Path";
            } else {
                StringBuilder _res = new StringBuilder();
                String[] userIdArray = result.split("->");
                _res.append(this.getUserNameFromId(Integer.parseInt(userIdArray[0])));
                for (int i = 1; i < userIdArray.length; i++) {
                    _res.append("->").append(this.getUserNameFromId(Integer.parseInt(userIdArray[i])));
                }
                result = _res.toString();
            }
            res.append(String.format(format, result)).append('\n');
        }
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + 1)).append("|\n");

        return res.toString();
    }

    private String createDisplayTopMessageBody(ResultSet rs, int firstWidth, int secondWidth) throws SQLException {
        StringBuilder res = new StringBuilder();
        res.append(InfoPrinter.createTitle("Top Message Users", firstWidth + secondWidth + 3));
        String format = "|%1$" + firstWidth + "s |%2$" + secondWidth + "s |";
        String head = String.format(format, "User Id", "User Name") + '\n';

        res.append(head);
        String head2 = "|" + InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + 3) +
                "|\n";
        res.append(head2);
        while (rs.next()) {
            int _userId = rs.getInt(1);
            res.append(String.format(format, _userId, this.getUserNameFromId(_userId))).append('\n');
        }
        res.append("|").append(InfoPrinter.paddingCharacter('-', firstWidth + secondWidth + 3)).append("|\n");

        return res.toString();
    }

    public void printTable(String tableName, char[] cols) throws SQLException {
        PreparedStatement st = _conn.prepareStatement("select * from ?");
        st.setString(1, tableName);
        st = _conn.prepareStatement(st.toString().replace("'",""));
        ResultSet rs = st.executeQuery();
        System.out.println("printout of Table "+tableName+": \n");
         while (rs.next()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0;i<cols.length;i++) {
                if (cols[i] == 's') {
                    sb.append("   "+rs.getString(i + 1));
                } else if (cols[i] == 'i') {
                    sb.append("   "+rs.getInt(i + 1));
                } else if (cols[i] == 't') {
                    sb.append("   "+rs.getTimestamp(i + 1));
                } else if (cols[i] == 'd') {
                    sb.append("   "+rs.getDate(i + 1));
                }
            }
            System.out.println(sb);
        }
        System.out.println("---------------------------------------------------");
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
        System.out.print("Input User ID: ");
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
            try {
                pittSocial.initiateAddingGroup(groupId, message);
            }catch (PSQLException e){
                System.err.println("You are already in this group!");
                InfoPrinter.printWithColor(ConsoleColors.RED_BRIGHT, "Cancelled Initiate Adding Group, Press Enter...");
                console.reader().read();
            }
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

    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException, InterruptedException {
        DataManager dataManager = new DataManager();
        PittSocial pittSocial = new PittSocial(dataManager.getConnection());
        if (args.length == 1 && args[0].equalsIgnoreCase("I")) {
            dataManager.initDatabaseWithTest(true);
        }
        enterMainMenu(pittSocial);
    }
}
