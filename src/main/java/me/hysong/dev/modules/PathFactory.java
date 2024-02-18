package me.hysong.dev.modules;

import me.hysong.CentralSubsystem;

public class PathFactory {
    public static String getPath() {
        CentralSubsystem.getCentralSubsystem().mkdirs("/data/transfer");
        return CentralSubsystem.getCentralSubsystem().realpath("/data/transfer");
    }

}
