package me.hysong.dev.modules;

import me.hysong.Applications;
import me.hysong.libhyextended.Shell;

import java.io.File;

public class PathFactory {

    public static final String contextName = "transfer";

    public static String getPath() {
        System.out.println(System.getProperty("catalina.base") + File.separator + "webapps" + File.separator + contextName);
        return System.getProperty("catalina.base") + File.separator + "webapps" + File.separator + contextName;
    }

    public static String getCentralPath() {
        return Applications.getApplicationDataPath(contextName);
    }
}
