import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.LinkedList;

public class InfoPrinter {
    public static void printWithColor(String color, String msg) {
        System.out.println(color + msg + ConsoleColors.RESET);
    }

    public static void printWithoutColor(String msg) {
        System.out.println(msg);
    }

    public static String createTitle(String title, int length) {
        return "|" +
                paddingCharacter('-', length) +
                "|\n|" +
                paddingCharacter(' ', length) +
                "|\n|" +
                paddingCenter(title, length) +
                "|\n|" +
                paddingCharacter(' ', length) +
                "|\n|" +
                paddingCharacter('-', length) +
                "|\n";
    }

    public static String createField(String fieldInfo, int length) {
        fieldInfo += " ";
        return paddingLeft(fieldInfo, length);
    }

    public static String paddingLeft(String str, int length) {
        return String.format("%" + length + "s", str);
    }

    public static String paddingRight(String str, int length) {
        return String.format("%-" + length + "s", str);
    }

    public static String paddingCenter(String str, int length) {
        String res = "";
        res += paddingCharacter(' ', (int) Math.ceil((double) (length - str.length()) / 2));
        res += str;
        res += paddingCharacter(' ', (int) Math.floor((double) (length - str.length()) / 2));
        return res;
    }

    public static String paddingCharacter(char character, int length) {
        return String.format("%" + length + "s", "").replace(' ', character);
    }
}
