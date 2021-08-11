import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email {
    private String username;
    private String domain;
    private String time;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String sec;
    private String dtype;

    public Email(String username, String domain, String time) {
        this.username = username;
        this.domain = domain;
        this.time = time;
    }

    public void explain() {
        String str = time;
        String[] result =  str.split("-");
        year = result[0];
        month = result[1];
        day = result[2];
        if (result.length >= 4) {
            result = result[3].split(":");
            hour = result[0];
            if (result.length == 2) {
                minute = result[1];
            }
            else if (result.length == 3) {
                minute = result[1];
                sec = result[2];
            }
        }
    }

    public String getUsername() {
        return username.toLowerCase(Locale.ROOT);
    }

    public String getDomain() {
        return domain;
    }

    public String getDtype() {
        dtype = domain.split("\\.")[0];
        return dtype;
    }

    public String getTime() {
        return time;
    }

    public String getYear() {
        explain();
        return year;
    }

    public String getMonth() {
        explain();
        return month;
    }

    public String getDay() {
        explain();
        return day;
    }

    public String getHour() {
        explain();
        return hour;
    }

    public String getMinute() {
        explain();
        return minute;
    }

    public String getSec() {
        explain();
        return sec;
    }

    public String getUtype() {
        int count = 0;
        String type = "";
        String reA = "a{2,3}b{2,4}a{2,4}c{2,3}";
        Pattern patterna = Pattern.compile(reA);
        Matcher matcher = patterna.matcher(username);
        if (matcher.find()) {
            count++;
            type = type + "A";
        }
        String reB = "a{2,3}(ba)*(bc){2,4}";
        Pattern patternb = Pattern.compile(reB);
        matcher = patternb.matcher(username);
        if (matcher.find()) {
            count++;
            type = type + "B";
        }
        matcher = patternb.matcher(username.toLowerCase(Locale.ROOT));
        if (matcher.find()) {
            count++;
            type = type + "C";
        }
        String reD1 = "^a{0,3}b+c{2,3}";
        Pattern patternd1 = Pattern.compile(reD1);
        String reD2 = "b{1,2}a{1,2}c{0,3}$";
        Pattern patternd2 = Pattern.compile(reD2);
        matcher = patternd1.matcher(username);
        Matcher matcher1 = patternd2.matcher(username.toLowerCase(Locale.ROOT));
        if (matcher.find() && matcher1.find()) {
            count++;
            type = type + "D";
        }
        String reE = "a.*b.*b.*c.*b.*c.*c.*";
        Pattern patterne = Pattern.compile(reE);
        matcher = patterne.matcher(username);
        if (matcher.find()) {
            count++;
            type = type + "E";
        }
        String out = "";
        if (count == 0) {
            out = String.valueOf(0);
        }
        else {
            out = String.valueOf(count) + " " + type;
        }
        return out;
    }
}
