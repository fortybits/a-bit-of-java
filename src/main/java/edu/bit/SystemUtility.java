package edu.bit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.*;
import java.lang.management.ManagementFactory;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SystemUtility {

    public void verifyJavaRuntimeVersion() {
        System.out.println("java.runtime.version = " + System.getProperty("java.runtime.version"));
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

    //
    public void executeProcesses() throws IOException {
        Process process = new ProcessBuilder(List.of("ping", "-i", "1", "-c", "5", "google.com")).inheritIO().start();
        ProcessHandle processHandle = process.toHandle();
        System.out.println(processHandle + " - " + processHandle.info());

        CompletableFuture<ProcessHandle> onExitFuture = processHandle.onExit();
        CompletableFuture<ProcessHandle.Info> infoCompletableFuture = onExitFuture.thenApply(ProcessHandle::info);
        CompletableFuture<Optional<Duration>> optionalCompletableFuture =
                infoCompletableFuture.thenApply(ProcessHandle.Info::totalCpuDuration);
        CompletableFuture<Void> accept = optionalCompletableFuture.thenAccept(System.out::println);
        System.in.read();
    }

    //
    private void getProcessInfo() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("echo", "Hello World!");
        Process p = pb.start();
        ProcessHandle.Info info = p.info();
        System.out.printf("Process ID: %s%n", p.pid());
        String na = "<not available>";
        System.out.printf("Command name: %s%n", info.command().orElse(na));
        System.out.printf("Command line: %s%n", info.commandLine().orElse(na));

        System.out.printf("Start time: %s%n",
                info.startInstant().map(i -> i.atZone(ZoneId.systemDefault())
                        .toLocalDateTime().toString())
                        .orElse(na));

        System.out.printf("Arguments: %s%n",
                info.arguments().map(a -> String.join(" ", a))
                        .orElse(na));

        System.out.printf("User: %s%n", info.user().orElse(na));
    }

    //
    public static void killProcess() {
        Stream<ProcessHandle> currentProcess = ProcessHandle.allProcesses();
        currentProcess.parallel().forEach(processHandle -> {
            System.out.println(processHandle.pid());
            processHandle.destroy();
        });
        System.out.println("current process id:" + currentProcess.toString());
        System.out.println("current process id:" + ManagementFactory.getRuntimeMXBean().getName());

        long pid = Long.MAX_VALUE;
        Optional<ProcessHandle> processHandle = ProcessHandle.of(pid);
        if (processHandle.isPresent()) {
            ProcessHandle.Info processInfo = processHandle.get().info();
            System.out.println("Process arguments: " + Arrays.toString(processInfo.arguments().orElse(new String[0])));
            System.out.println("Process executable: " + processInfo.command().orElse(""));
            System.out.println("Process command line: " + processInfo.commandLine().orElse(""));
            System.out.println("Process start time: " + processInfo.startInstant().orElse(null));
            System.out.println("Process total cputime accumulated: " + processInfo.totalCpuDuration().orElse(null));
            System.out.println("Process user: " + processInfo.user().orElse(""));
        }
    }

    //
    public int publicTestVariable = 10;

    public void varHandleSample() throws NoSuchFieldException, IllegalAccessException {
        SystemUtility e = new SystemUtility();
        System.out.println(e.publicTestVariable);
        e.update();
        System.out.println(e.publicTestVariable);
    }

    private void update() throws NoSuchFieldException, IllegalAccessException {
        VarHandle publicIntHandle = MethodHandles.lookup().in(String.class)
                .findVarHandle(SystemUtility.class, "publicTestVariable", int.class);
        publicIntHandle.compareAndSet(this, 10, 100); // CAS
    }

    //
    public void varHandleDetailedExperiment() throws Throwable {
        //        such lookup to obtain a VarHandle for a field named i of type int on a receiver class Foo might be performed as follows:
        VarHandle fieldHandle = MethodHandles.lookup().
                in(Basics.Question.class).
                findVarHandle(Basics.Question.class, "votes", int.class);


        //        a VarHandle to an array of int may be created as follows:
        VarHandle intArrayHandle = MethodHandles.arrayElementVarHandle(int[].class);


        //        a VarHandle to view an array of byte as an unaligned array of long may be created as follows:
        VarHandle longArrayViewHandle = MethodHandles.byteArrayViewVarHandle(
                long[].class, java.nio.ByteOrder.BIG_ENDIAN);

        //        to produce a MethodHandle to the "compareAndSet" access mode for a particular variable kind and type:
        Basics.Question q = new Basics.Question();
        MethodHandle mhToVhCompareAndSet = MethodHandles.publicLookup().findVirtual(
                VarHandle.class,
                "compareAndSet",
                MethodType.methodType(boolean.class, Basics.Question.class, int.class, int.class));

        //        The MethodHandle can then be invoked with a variable kind and type compatible VarHandle instance as the first parameter:
        boolean r = (boolean) mhToVhCompareAndSet.invokeExact(fieldHandle, q, 0, 1);


        //        Such a MethodHandle lookup using findVirtual will perform an asType transformation to adjust arguments and return values.
        MethodHandle mhToVhCompareAndSet2 = MethodHandles.varHandleExactInvoker(
                VarHandle.AccessMode.COMPARE_AND_SET,
                MethodType.methodType(boolean.class, Basics.Question.class, int.class, int.class));

        boolean r1 = (boolean) mhToVhCompareAndSet2.invokeExact(fieldHandle, q, 0, 1);
    }

    // var handling the global variable within a class
    static class Point {

        private static final VarHandle X;

        static {
            try {
                X = MethodHandles.lookup().findVarHandle(Point.class, "x", int.class);
                System.out.println(X.getVolatile(new Point()));
            } catch (ReflectiveOperationException e) {
                throw new Error(e);
            }
        }

        volatile int x, y;
    }

    // compact strings was specifically a feature/implementation with Java-9 as
    // and when the string representation had changed
    public static class CompactStrings {

        String field;

        public static void main(String[] args) {
            overviewCompactString();
            concatFactory();
            testStringCompaction();
        }

        private static void concatFactory() {
            try {
                StringConcatFactory.makeConcat(MethodHandles.lookup(), "abc", MethodType.methodType(String.class));
                //            StringConcatFactory.makeConcat(MethodHandles.publicLookup(), "abc", MethodType.methodType(String.class)); // throws a StringConcatException
            } catch (StringConcatException e) {
                e.printStackTrace();
            }
        }

        private static void testStringCompaction() {
            String java9 = "MAJOR.MINOR";
            StackWalker stackWalker = StackWalker.getInstance(Set.of(StackWalker.Option.RETAIN_CLASS_REFERENCE));
            stackWalker.getCallerClass();
            System.out.println(Arrays.toString(stackWalker.getCallerClass().getDeclaredFields()));
        }

        public static void overviewCompactString() {
            long startTime = System.currentTimeMillis();
            List strings = IntStream.rangeClosed(1, 10000000).mapToObj(Integer::toString).collect(Collectors.toList());
            long totaltime = System.currentTimeMillis() - startTime;

            System.out.println("Generated String " + strings.size() + " strings in " + totaltime + " ms.");


            startTime = System.currentTimeMillis();
            String appended = (String) strings.stream().limit(100000).reduce("", (l, r) -> l.toString() + r.toString());

            totaltime = System.currentTimeMillis() - startTime;


            System.out.println("Created String of length " + appended.length() + " in " + totaltime + " ms.");
        }

        public void stringConcatenationInJava9() throws NoSuchMethodException, IllegalAccessException {
            MethodHandle concatHandle = MethodHandles.publicLookup()
                    .findVirtual(String.class, "concat", MethodType.methodType(String.class, String.class));
            concatHandle = concatHandle.bindTo("Hello, ");
        }
    }

    public void currencyFormatter() {
        Currency currency = Currency.getInstance("EUR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        currencyFormatter.setMaximumFractionDigits(0);
        currencyFormatter.setMinimumFractionDigits(0);
        currencyFormatter.setCurrency(currency);

        String expected = "123 457 €";
        String obtained = currencyFormatter.format(123456.789);
        System.out.println(expected.equals(obtained));
        System.out.println(expected);
        System.out.println(obtained);
        System.out.println(expected.equals(obtained));

        System.out.format("Bytes from expected: %s\n", Arrays.toString(expected.getBytes()));
        System.out.format("Bytes from expected: %s\n", Arrays.toString(obtained.getBytes()));
    }

    public void formatNumberShort() {
        NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        System.out.println(fmt.format(1000));
        System.out.println(fmt.format(100000));
        System.out.println(fmt.format(10000000));
        System.out.println(fmt.format(5501));
        System.out.println(fmt.format(12034));
    }
}