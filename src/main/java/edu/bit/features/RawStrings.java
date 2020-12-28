package edu.bit.features;

import java.io.IOException;

// TODO : intelliJ Update for Raw Strings
public class RawStrings {

    public static void filePathRawStrings() throws IOException {
        // Before JEP-326
        Runtime.getRuntime().exec("\"C:\\Program Files\\foo\" bar");
        // After JEP-326
//        Runtime.getRuntime().exec(```"C:\\Program Files\\foo\\bar"```);
    }

    public static void polyglotRawStrings() {
        // Before JEP-326
        String script = "function hello() {\n" +
                "   print(\'\"Hello World\"\');\n" +
                "}\n" +
                "\n" +
                "hello();\n";
//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
//        Object obj = engine.eval(script);


        // After JEP-326
//        String scriptNow = `function hello() {
//                    print('"Hello World"');
//                 }
//
//                 hello();
//                `;
//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
//        Object obj = engine.eval(script);
    }

    public static void regexRawStrings() {
        // Before JEP-326
        System.out.println("this".matches("\\w\\w\\w\\w"));
        // After JEP-326
//        System.out.println("this".matches(`\w\w\w\w`));
    }

    public static void traditionalRawStrings() {
        // Before JEP-326
        String html = "<html>\n" +
                "    <body>\n" +
                "		    <p>Hello World.</p>\n" +
                "    </body>\n" +
                "</html>\n";

        // After JEP-326
//        String htmlnow = ```<html>
//                   <body>
//                       <p>Hello World.</p>
//                   </body>
//               </html>
//              `;
    }
}
