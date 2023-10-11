package edu.bit;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.String.format;

public class FileHandling {

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

    public static void createFile(String directoryPath, String fileName) {
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

    public void fileHandlingSampleWithStream() {
        IntStream.range(0, 5).forEach(i -> createFile("", "test.txt"));
        Stream.iterate("text.txt", x -> "test.txt")
                .limit(5)
                .forEach(x -> createFile("", x));
    }

    //
    public void readingZipFile(final Path zipFile) {
        System.out.println("Reading zip-file: " + zipFile);
        final URI uri = URI.create("zip:file:" + zipFile.toUri().getPath().replace(" ", "%20"));

        try (final FileSystem fs = zipFile.getFileSystem().provider().newFileSystem(uri, Collections.singletonMap("create", "true"))) {
            final long entriesRead = StreamSupport.stream(fs.getRootDirectories().spliterator(), false)
                    .flatMap(root -> {
                        try {
                            return Files.walk(root);
                        } catch (final IOException ex) {
                            throw new RuntimeException(format(
                                    "Error traversing zip file system '%s', root: '%s'.",
                                    zipFile, root), ex);
                        }
                    }).mapToLong(file -> {
                        try {
                            Files.lines(file).forEachOrdered(System.out::println);
                            return 1;
                        } catch (final IOException ex) {
                            throw new RuntimeException(format(
                                    "Error modifying DAE-file '%s' in zip file system '%s'.",
                                    file, zipFile), ex);
                        }
                    }).sum();

            System.out.format("A total of %,d entries read.%n", entriesRead);

        } catch (final IOException ex) {
            throw new RuntimeException(format(
                    "Error reading zip-file '%s'.", zipFile
            ), ex);
        }
    }

    private void redirectToFile() throws IOException, InterruptedException {
        File outFile = new File("out.tmp");
        Process p = new ProcessBuilder("ls", "-la")
                .redirectOutput(outFile)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();
        int status = p.waitFor();
        if (status == 0) {
            p = new ProcessBuilder("cat", outFile.toString())
                    .inheritIO()
                    .start();
            p.waitFor();
        }
    }
}