package me.hysong.dev.modules;

import me.hysong.Applications;

public class PathFactory {
    public static String getPath() {
        return Applications.getApplicationDataPath("transfer");
    }

}
