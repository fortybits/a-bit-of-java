package edu.bit.__dump;

import java.io.File;
import java.io.IOException;

public class RedirectOutput {

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