package ga.justreddy.wiki.whalepunishments.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author JustReddy
 */
public class TimeUtil {

    public static long getDurationMS(String time) {
        long ms = 0;
        if (time.toLowerCase().contains("s"))
            ms = (Long.parseLong(time.replace("s", "")) * 1000) + System.currentTimeMillis();
        if (time.toLowerCase().contains("m") && !time.toLowerCase().contains("o"))
            ms = ((Long.parseLong(time.replace("m", "")) * 1000) * 60) + System.currentTimeMillis();
        if (time.toLowerCase().contains("h"))
            ms = (((Long.parseLong(time.replace("h", "")) * 1000) * 60) * 60) + System.currentTimeMillis();
        if (time.toLowerCase().contains("d"))
            ms = ((((Long.parseLong(time.replace("d", "")) * 1000) * 60) * 60) * 24) + System.currentTimeMillis();
        if (time.toLowerCase().contains("w"))
            ms = (((((Long.parseLong(time.replace("w", "")) * 1000) * 60) * 60) * 24) * 7) + System.currentTimeMillis();
        if (time.toLowerCase().contains("m") && time.toLowerCase().contains("o"))
            ms = (((((Long.parseLong(time.replace("mo", "")) * 1000) * 60) * 60) * 24) * 30) + System.currentTimeMillis();
        if (time.toLowerCase().contains("y"))
            ms = ((((((Long.parseLong(time.replace("y", "")) * 1000) * 60) * 60) * 24) * 7) * 52) + System.currentTimeMillis();

        return ms;
    }

    public static String getDurationString(long time) {
        long current = System.currentTimeMillis();
        long millies = time - current;
        long seconds = 0L;
        long minutes = 0L;
        long hours = 0L;
        long days = 0L;
        while (millies > 1000L) {
            millies -= 1000L;
            ++seconds;
        }
        while (seconds > 60L) {
            seconds -= 60L;
            ++minutes;
        }
        while (minutes > 60L) {
            minutes -= 60L;
            ++hours;
        }
        while (hours > 24L) {
            hours -= 24L;
            ++days;
        }

        if (days == 0 && hours == 0 && minutes == 0) {
            return seconds + " seconds";
        }
        if (days == 0 && hours == 0) {
            return minutes + " minutes " + seconds + " seconds";
        }
        if (days == 0) {
            return hours + "h" + minutes + "m" + seconds + "s";
        }
        return days + "d" + hours + "h" + minutes + "m" + seconds + "s";
    }

    public static String formattedDate(long time) {
        return formattedDate(new Date(time));
    }

    public static String formattedDate(Date date) {
        ZoneId id = ZoneId.of("Europe/Amsterdam");
        LocalDateTime localDateTime = date.toInstant().atZone(id).toLocalDateTime();
        int day = localDateTime.getDayOfMonth();
        String month = getMonth(localDateTime.getMonth());
        int year = localDateTime.getYear();
        return day + " " + month + " " + year;
    }

    private static String getMonth(Month month) {
        switch (month) {
            case JANUARY -> {
                return "January";
            }
            case FEBRUARY -> {
                return "February";
            }
            case MARCH -> {
                return "March";
            }
            case APRIL -> {
                return "April";
            }
            case MAY -> {
                return "May";
            }
            case JUNE -> {
                return "June";
            }
            case JULY -> {
                return "July";
            }
            case AUGUST -> {
                return "August";
            }
            case SEPTEMBER -> {
                return "September";
            }
            case OCTOBER -> {
                return "October";
            }
            case NOVEMBER -> {
                return "November";
            }
            case DECEMBER -> {
                return "December";
            }
            default -> {
                return null;
            }
        }
    }

}
