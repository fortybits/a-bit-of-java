package edu.bit;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class TimeUtility {

    // detailed in https://stackoverflow.com/questions/49109551/literal-dash-added-to-datetimeformatterbuilder-causes-parsing-to-fail
    // registered as a bug at https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8199412
    public void parsingWithDashes() {
        var x = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.BASIC_ISO_DATE)
                .appendLiteral('-')
                .append(DateTimeFormatter.ISO_LOCAL_TIME)
                .toFormatter();
//        x.parse("20180302-17:45:21");
        var y = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.BASIC_ISO_DATE)
                .appendLiteral('-')
                .append(DateTimeFormatter.ISO_LOCAL_TIME)
                .toFormatter();

        y.parse("20180302-17:45:21");
    }
}