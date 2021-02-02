package edu.bit;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;

class SystemUtilityTest {

    @Test
    void testAccessingPreviewClasses() throws IOException {
        FileSystem fs = FileSystems.getFileSystem(URI.create("jrt:/"));
        // Object works, I get the bytes.
        byte[] object = Files.readAllBytes(fs.getPath("modules", "java.base",
                "java/lang/Object.class"));
        // Record fails, NoSuchFile.
        byte[] record = Files.readAllBytes(fs.getPath("modules", "java.base",
                "java/lang/Record.class"));
    }

}