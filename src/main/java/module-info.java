module a.bit.of.java {
    requires java.scripting;

    requires java.net.http;
    requires java.logging;
    requires java.desktop;
    requires java.sql;

    requires jdk.unsupported;
    requires jdk.jshell;
    requires jdk.management;
    requires jdk.jlink;

    requires com.google.common;
    requires jdk.httpserver;
}