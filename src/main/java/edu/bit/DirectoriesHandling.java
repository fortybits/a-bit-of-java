package edu.bit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

public class DirectoriesHandling {

    private static void deleteFilesBasedOnPaths(List<Path> paths) {
        for (Path folderPath : paths) {
            Path to = folderPath.getRoot().resolve(
                    folderPath.getParent().subpath(0, folderPath.getNameCount() - 1));
            try {
                Files.list(folderPath).forEach(filePath -> {
                    try {
                        Files.move(filePath, to.resolve(filePath.getFileName()), StandardCopyOption.ATOMIC_MOVE);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                });
                if (Files.list(folderPath).count() == 0) {
                    Files.deleteIfExists(folderPath); // this call
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void createAFreshDirectoryForScreenShotsDuringAutomation() throws IOException {
        String currentDirectory = System.getProperty("user.dir");
        File screenshotDirectory = new File(currentDirectory + "/logs/screenshots");
        if (!screenshotDirectory.exists()) {
            if (screenshotDirectory.mkdirs()) {
                System.out.println("Created the screenshots directory under logs.");
            } else {
                System.out.println("Couldn't create a directory.");
            }
        } else {
            if (deleteDir(screenshotDirectory)) {
                System.out.println("Deleted the screenshots directory under logs.");
            } else {
                System.out.println("Couldn't delete the directory.");
            }
        }
    }

    public boolean deleteDir(File dir) throws IOException {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : Objects.requireNonNull(children)) {
                return deleteDir(new File(dir, aChildren));
            }
        }
        Files.delete(dir.toPath());
        return true;
    }
}