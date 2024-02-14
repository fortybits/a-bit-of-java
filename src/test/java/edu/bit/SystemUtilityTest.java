package edu.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Map;

class SystemUtilityTest {

    @Test
    void testAccessingPreviewClasses() throws IOException {
        FileSystem fs = FileSystems.getFileSystem(URI.create("jrt:/"));
        // Object works, I get the bytes.
        byte[] object = Files.readAllBytes(fs.getPath("modules", "java.base",
                "java/lang/Object.class"));

        // should work with java-8 as well - https://stackoverflow.com/a/66044561/1746118
        String jdk15Home = "/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home";
        FileSystem jrtFs = FileSystems.newFileSystem(
                URI.create("jrt:/"), Map.of("java.home", jdk15Home));
        byte[] record = Files.readAllBytes(
                jrtFs.getPath("modules", "java.base", "java/lang/Record.class"));
        Assertions.assertTrue(record.length != 0);
    }
}