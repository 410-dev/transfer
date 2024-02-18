package me.hysong.dev.modules;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.hysong.libhycore.CoreDate;
import me.hysong.libhyextended.utils.StackTraceStringifier;

public class Logger {
    public static void simpleLog(String message) {
        CoreDate.useSystemZoneOffset();
        String time = CoreDate.timestamp("yyyy-MM-dd HH:mm:ss (Z)");
        String format = "[ MISC.  ] [ %s ] Logged from servlet: \"%s\"";
        message = String.format(format, time, message);
        System.out.println(message);
    }

    public static void logger(HttpServletRequest request, HttpServletResponse response, String message) {
        CoreDate.useSystemZoneOffset();
        String time = CoreDate.timestamp("yyyy-MM-dd HH:mm:ss (Z)");
        long unixTime = CoreDate.mSecSince1970();
        String format = "[ MISC.  ] [ %s ] Logged from %s: \"%s\"";
        message = String.format(format, time, request.getRemoteAddr(), message);
        recordToDB(request, "MISCELLANEOUS", time, unixTime, message);
    }

    public static void errorLog(HttpServletRequest request, HttpServletResponse response, String message, Exception e) {
        CoreDate.useSystemZoneOffset();
        String time = CoreDate.timestamp("yyyy-MM-dd HH:mm:ss (Z)");
        long unixTime = CoreDate.mSecSince1970();
        String format = "[ ERROR  ] [ %s ] Exception occurred by %s: \"%s\". ||||| Stack trace: %s";
        message = String.format(format, time, request.getRemoteAddr(), message, StackTraceStringifier.stringify(e));
        recordToDB(request, "MISCELLANEOUS", time, unixTime, message);
    }

    public static void accessLogger(HttpServletRequest request, HttpServletResponse response) {
        CoreDate.useSystemZoneOffset();
        String time = CoreDate.timestamp("yyyy-MM-dd HH:mm:ss (Z)");
        long unixTime = CoreDate.mSecSince1970();
        String format = "[ ACCESS ] [ %s ] HTTP request from %s: %s";
        String message = String.format(format, time, request.getRemoteAddr(), request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
        recordToDB(request, "ACCESS", time, unixTime, message);
    }

    private static void recordToDB(HttpServletRequest request, String logType, String time, long unixTime, String message) {
        System.out.println(message);
//        try {
////            SQLiteFactory.getLogDatabase().executeUpdate("INSERT INTO " + LogTableStruct.TABLE_NAME +" (" + LogTableStruct.COLUMN_UNIX_TIME + ", " + LogTableStruct.COLUMN_TIMESTAMP + ", " + LogTableStruct.COLUMN_TYPE + ", " + LogTableStruct.COLUMN_ADDRESS + ", " + LogTableStruct.COLUMN_REQUEST + ", " + LogTableStruct.COLUMN_MESSAGE + ") VALUES (?, ?, ?, ?, ?, ?)", unixTime, time, logType, request.getRemoteAddr(), request.getRequestURL().toString(), message);
//        } catch (SQLException e) {
//            error(message, e);
//        }
    }

    private static void error(String message, Exception e) {
        CoreDate.useSystemZoneOffset();
        String time = CoreDate.timestamp("yyyy-MM-dd HH:mm:ss (Z)");
        String format = "[ ERRLOG ] [ %s ] !!! Minimal Log !!! This is not recorded in the database. Message to log: \"%s\"";
        message = String.format(format, time, message);
        System.out.println(message + " /////// Exception: " + e.getMessage());
        e.printStackTrace();
    }

}
