import java.util.Arrays;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        String re = "(, )|,| ";
        String[] result = new String[0];
        Email[] emails = new Email[0];
        MainClass mainClass = new MainClass();
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("END_OF_INFORMATION")) {
                break;
            }
            result = input.split(re);
            emails = mainClass.create(result,emails);
        }
        String[] search = new String[0];
        String[] print = new String[0];
        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            search = input.split(" ");
            print = mainClass.search(search,print,emails);
        }
        for (int i = 0;i < print.length;i++) {
            System.out.println(print[i]);
        }
    }

    public Email[] create(String[] result,Email[] emails) {
        Email[] newemails = emails;
        for (int i = 0;i < result.length;i++) {
            String name = result[i].split("@")[0];
            int index = result[i].split("@")[1].indexOf("-");
            StringBuilder stringBuilder = new StringBuilder(result[i].split("@")[1]);
            stringBuilder.replace(index,index + 1," ");
            String dominan = stringBuilder.toString().split(" ")[0];
            String time = stringBuilder.toString().split(" ")[1];
            Email email = new Email(name,dominan,time);
            newemails = Arrays.copyOf(newemails,newemails.length + 1);
            newemails[newemails.length - 1] = email;
        }
        return newemails;
    }

    public String[] SearchbyName(String[] search,String[] print,Email[] emails) {
        String[] newprint = print;
        String username = search[1];
        String out = "";
        int i;
        for (i = 0;i < emails.length; i++) {
            if (emails[i].getUsername().equals(username)) {
                StringBuilder stringBuilder = new StringBuilder(search[0]);
                String type = stringBuilder.delete(0,1).toString();
                if (type.equals("dtype")) {
                    out = emails[i].getDtype();
                }
                else if (type.equals("year")) {
                    out = emails[i].getYear();
                }
                else if (type.equals("month")) {
                    out = emails[i].getMonth();
                }
                else if (type.equals("day")) {
                    out = emails[i].getDay();
                }
                else if (type.equals("hour")) {
                    out = emails[i].getHour();
                }
                else if (type.equals("minute")) {
                    out = emails[i].getMinute();
                }
                else if (type.equals("utype")) {
                    out = emails[i].getUtype();
                }
                else {
                    out = emails[i].getSec();
                }
                break;
            }
        }
        if (i == emails.length) {
            out = "no username exists";
        }
        newprint = Arrays.copyOf(newprint,newprint.length + 1);
        newprint[newprint.length - 1] = out;
        return newprint;
    }

}
