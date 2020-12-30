package edu.bit;

import java.util.Optional;

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
}