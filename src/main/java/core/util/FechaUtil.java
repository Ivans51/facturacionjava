package core.util;

import com.jfoenix.controls.JFXDatePicker;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Years;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

public class FechaUtil {

    public static long diffHours;
    private static Date date = new Date();
    private static Calendar cal = Calendar.getInstance();

    public static java.sql.Date getDatePickentCurrent(JFXDatePicker datePicker) {
        return java.sql.Date.valueOf(datePicker.getValue());
    }

    public static Date getCurrentDate() throws ParseException {
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }

    public static int getHourMinutes() throws ParseException {
        DateTime dt = new DateTime();
        int hours = dt.getHourOfDay();
        int minutes = dt.getMinuteOfHour();
        return hours;
    }

    public static String getDateFormat(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return date == null ? dateFormat.format(FechaUtil.date) : dateFormat.format(date);
    }

    public static String getDateFormat(Date date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return date == null ? dateFormat.format(FechaUtil.date) : dateFormat.format(date);
    }

    public static Date getDateFormatValue(Date date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String format = getDateFormat(date);
        return dateFormat.parse(format);
    }

    public static String getDateFormatTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String getDateFormatTimeS() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S");
        return dateFormat.format(date);
    }

    public static Date getCalendarFormat() throws ParseException {
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    public static Date getCalendarFormatTime() {
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    private static String getCalendarFormatString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(cal.getTime());
    }

    private static String getCalendarFormatTimeString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(cal.getTime());
    }

    public static int getYearsDiff(Date time) {
        DateTime now = DateTime.now();
        DateTime then = new DateTime(time.getTime());
        return Years.yearsBetween(then, now).getYears();
    }

    public static int getMesesLaborando(Date time) {
        int months = getMonthsDiff(time);
        return (months <= 12) ? months : (getYearsDiff(time) * 12);
    }

    private static int getMonthsDiff(Date time) {
        DateTime now = DateTime.now();
        DateTime then = new DateTime(time.getTime());
        return Months.monthsBetween(then, now).getMonths();
    }

    public static void getDifference(String dateStart) {

        String dateStop = "11/03/14 09:33:43";
        String dateStartTest = "11/03/14 09:29:58";
        // use parse(date, LocalDateTime::from) to get LocalDateTime

        // dateStart = getDateFormatTimeS();

        TemporalAccessor temporal = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S").parse(dateStart);
        String output = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss").format(temporal);
        String dateFormatTime = getDateFormatTime();

        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(output);
            d2 = format.parse(dateFormatTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Get msec from each, and subtract.
        long diff = d2.getTime() - d1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        diffHours = diff / (60 * 60 * 1000);
        System.out.println("Time in seconds: " + diffSeconds + " seconds.");
        System.out.println("Time in minutes: " + diffMinutes + " minutes.");
        System.out.println("Time in hours: " + diffHours + " hours.");
    }

    public static long getDiffHours() {
        return diffHours;
    }
}