package edu.bit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SystemUtility {

    public void verifyJavaRuntimeVersion() {
        System.out.println(Runtime.version());
        Runtime.Version version = Runtime.Version.parse("9");
        version = Runtime.Version.parse("9.0.1");
        version = Runtime.Version.parse("9.0.0.15");
        version = Runtime.Version.parse("9.0.0.15+181");

        System.out.println(System.getProperty("java.specification.version"));
        System.out.println(getMajorVersion());
    }

    private static int getMajorVersion() {
        String systemVersionProperty = System.getProperty("java.specification.version");
        return systemVersionProperty.contains(".") ? Integer.parseInt(systemVersionProperty.substring(2)) :
                Integer.parseInt(systemVersionProperty);
    }

    public void testCodePointBehaviour() {
        String yourString = "U+00E4";
        int codepoint = Integer.parseInt(yourString.substring(2), 16);
        System.out.println(Character.toString(codepoint));

        String myString = "\u0048\u0065\u006C\u006C\u006F World";
        myString.chars().forEach(a -> System.out.print((char) a));


        "Dodd\u2013Frank".chars().forEach(a -> System.out.print((char) a));
    }

    // filtering processes of a specific user
    private void filterProcessesTest() {
        Optional<String> currUser = ProcessHandle.current().info().user();
        ProcessHandle.allProcesses()
                .filter(p1 -> p1.info().user().equals(currUser))
                .sorted(SystemUtility::parentComparator)
                .forEach(SystemUtility::showProcess);
    }

    private static int parentComparator(ProcessHandle p1, ProcessHandle p2) {
        long pid1 = p1.parent().map(ProcessHandle::pid).orElse(-1L);
        long pid2 = p2.parent().map(ProcessHandle::pid).orElse(-1L);
        return Long.compare(pid1, pid2);
    }

    private static void showProcess(ProcessHandle ph) {
        ProcessHandle.Info info = ph.info();
        System.out.printf("pid: %d, user: %s, cmd: %s%n",
                ph.pid(), info.user().orElse("none"), info.command().orElse("none"));
    }

    public static class DifferentProcess {

        public static void main(String[] args) {
            System.out.println("### Current process info ###");
            ProcessHandle currentProcess = ProcessHandle.current();
            printInfo(currentProcess);

            System.out.println();

            // Fork a child process that lasts for a few seconds
            spawnProcess("jshell --startup ./sleep.txt");

            printAllVisibleProcesses();
        }

        private static void printAllVisibleProcesses() {
            System.out.println("### Visible processes info ###");
            ProcessHandle.allProcesses().forEach(DifferentProcess::printInfo);
            System.out.println();
        }

        private static void spawnProcess(String command) {
            System.out.println("Spawning: " + command);
            try {
                Runtime.getRuntime().exec(command);
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static void printInfo(ProcessHandle processHandle) {
            ProcessHandle.Info processInfo = processHandle.info();
            System.out.println("Process ID: " + processHandle.pid());

            System.out.println("Process arguments: " + Arrays.toString(processInfo.arguments().orElse(new String[0])));
            System.out.println("Process executable: " + processInfo.command().orElse(""));
            System.out.println("Process command line: " + processInfo.commandLine().orElse(""));
            System.out.println("Process start time: " + processInfo.startInstant().orElse(null));
            System.out.println("Process total cputime accumulated: " + processInfo.totalCpuDuration().orElse(null));
            System.out.println("Process user: " + processInfo.user().orElse(""));
        }
    }

    private static void handlingProcessesOnExit() throws IOException {
        List<ProcessBuilder> greps = new ArrayList<>();
        greps.add(new ProcessBuilder("/bin/sh", "-c", "grep -c \"java\" *"));
        greps.add(new ProcessBuilder("/bin/sh", "-c", "grep -c \"Process\" *"));
        greps.add(new ProcessBuilder("/bin/sh", "-c", "grep -c \"onExit\" *"));
        startSeveralProcesses(greps, SystemUtility::printGrepResults);
        startSeveralProcessesAndTerminateBeforePreceeding(greps, SystemUtility::printGrepResults);
        System.out.println("\nPress enter to continue ...\n");
        System.in.read();
    }

    private static void startSeveralProcesses(List<ProcessBuilder> pBList, Consumer<Process> onExitMethod) {
        System.out.println("Number of processes: " + pBList.size());
        pBList.forEach(pb -> {
                    try {
                        Process p = pb.start();
                        System.out.printf("Start %d, %s%n",
                                p.pid(), p.info().commandLine().orElse("<na>"));
                        p.onExit().thenAccept(onExitMethod);
                    } catch (IOException e) {
                        System.err.println("Exception caught");
                        e.printStackTrace();
                    }
                }
        );
    }

    private static void printGrepResults(Process p) {
        System.out.printf("Exit %d, status %d%n%s%n%n",
                p.pid(), p.exitValue(), output(p.getInputStream()));
    }

    private static String output(InputStream inputStream) {
        String s = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            s = br.lines().collect(Collectors.joining(System.getProperty("line.separator")));
        } catch (IOException e) {
            System.err.println("Caught IOException");
            e.printStackTrace();
        }
        return s;
    }

    private static void startSeveralProcessesAndTerminateBeforePreceeding(List<ProcessBuilder> pBList, Consumer<Process> onExitMethod) {
        System.out.println("Number of processes: " + pBList.size());
        pBList.forEach(pb -> {
                    try {
                        Process p = pb.start();
                        System.out.printf("Start %d, %s%n",
                                p.pid(), p.info().commandLine().orElse("<na>"));
                        p.onExit().get();
                        printGrepResults(p);
                    } catch (IOException | InterruptedException | ExecutionException e) {
                        System.err.println("Exception caught");
                        e.printStackTrace();
                    }
                }
        );
    }
}