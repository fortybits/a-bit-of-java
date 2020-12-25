module a.bit.of.java {
    requires java.net.http;
    requires java.logging;
    requires java.desktop;
    requires java.sql;

    requires jdk.unsupported;
    requires jdk.jshell;
    requires jdk.management;
    requires jdk.jlink;

    requires lombok;
    requires com.google.common;

    exports edu.bit.functional;
    exports edu.bit.process to jdk.internal.ed;
}