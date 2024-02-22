package me.hysong.dev.apps.transfer.backend;

import me.hysong.dev.modules.PathFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DeletionTrackers {

    public static void scanAndDelete() {
        File metaDir = new File(PathFactory.getPath() + "/metas/");
        File[] files = metaDir.listFiles();
        if (files == null) return;

        for (File metaFile : files) {

            // If not a meta file, skip
            if (!metaFile.getName().endsWith(".meta")) continue;

            // If not a file, skip
            if (!metaFile.isFile()) continue;

            // Read file
            try {
                BufferedReader reader = new BufferedReader(new FileReader(metaFile));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                // Remove \n and meaningless spaces
                String content = builder.toString().replaceAll("\n", "").replaceAll(" ", "");

                // Cast to long
                long expireTime = Long.parseLong(content);

                // If expired, delete
                String randomID = metaFile.getName().substring(0, 5);
                String fileName = metaFile.getName().substring(6, metaFile.getName().length() - 5);
                if (expireTime < System.currentTimeMillis()) {
                    boolean success = metaFile.delete();
                    success = success && new File(PathFactory.getPath() + "/files/" + randomID + "/" + fileName).delete();

                    if (success) {
                        System.out.println("[Transfer] Deleted " + randomID + "/" + fileName);

                        File[] filesInDir = new File(PathFactory.getPath() + "/files/" + randomID).listFiles();
                        if (filesInDir == null || filesInDir.length == 0) {
                            new File(PathFactory.getPath() + "/files/" + randomID).delete();
                            System.out.println("[Transfer] Deleted empty directory " + randomID);
                        }

                    } else {
                        System.out.println("[Transfer] Failed to delete " + randomID + "/" + fileName);
                    }
                }else{
                    System.out.println("[Transfer] Not expired: " + randomID + "/" + fileName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void scanAndDeleteAsynchronously() {
        new Thread(DeletionTrackers::scanAndDelete).start();
    }
}
