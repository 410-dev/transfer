//package me.hysong.dev.apps.transfer.servlets;
//
//import me.hysong.ApplicationMeta;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;
//import java.util.ArrayList;
//
//@WebListener
//public class RegisterOnStart implements ServletContextListener {
//
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        ApplicationMeta meta = new ApplicationMeta(
//            "Transfer",
//            "{host}/transfer",
//            "Upload file to server temporarily to transfer files between devices.",
//            "transfer.png",
//            new ArrayList<>()
//        );
//        meta.save();
//    }
//
//}
