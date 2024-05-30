package edu.bit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;

public class TextBlocks {

    public void filePathTextBlock() throws IOException {
        // Before JEP-326
        Runtime.getRuntime().exec("\"C:\\Program Files\\foo\" bar");
        // After JEP-326
        Runtime.getRuntime().exec("""
                "C:\\Program Files\\foo\\bar"
                """);
    }

    public void polyglotTextBlock() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        // Before JEP-326
        String script = "function hello() {\n" +
                "   print('\"Hello World\"');\n" +
                "}\n" +
                "\n" +
                "hello();\n";

        // After JEP-326
        String scripts = """
                function hello() {
                    print('"Hello, world"');
                }
                                         
                hello();
                """;
        Object obj = engine.eval(script);
    }

    public void traditionalTextBlock() {
        // Before JEP-326
        String html = "<html>\n" +
                "    <body>\n" +
                "		    <p>Hello World.</p>\n" +
                "    </body>\n" +
                "</html>\n";

        // After JEP-326
        String htmlNow = """
                <html>
                    <body>
                        <p>Hello, world</p>
                    </body>
                </html>
                """;
    }

    public void indentTextBlocks() {
        String embeddedString = """
                          <html>
                          <body>
                              <p>Hello World.</p>
                          </body>
                      </html>
                """.stripIndent();
        System.out.print(embeddedString);

        String indentedBody = """
                       <html>
                  <body>
                       <p>Hello World - Indented.</p>
                   </body>
                </html>""".indent(4);
        System.out.print(indentedBody);

        String htmlBody = """
                     <html>
                         <body>
                             <p>Hello World.</p>
                         </body>
                     </html>
                """.indent(4);
        System.out.print(htmlBody);

        String multiLine = """
                First line
                Second line with indentation
                Third line
                and so on...""";
        System.out.println(multiLine);
    }

    void matchingBlock(){
        String gqlRequest = "{\"query\":\"query ChargingStationAndConnector{" +
                "\\n\\tconnector(id: \\\"05f113a5-d662-4f00-bf31-fc18e10a6306\\\"){" +
                "\\n\\t\\tid\\n\\t\\tevseId\\n\\t\\t" +
                "connectorType{\\n\\t\\t\\tid\\n\\t\\t\\tchargePointType\\n\\t\\t}\\n\\t}\\n\\t" +
                "station:cp(id: \\\"33842d46-3b53-4f7f-b2a9-cc062658e8c7\\\"){" +
                "\\n\\t\\tid\\n\\t\\taddress\\n\\t\\tcity\\n\\t\\tzip\\n\\t\\tcountry\\n\\t}\\n}\"," +
                "\"operationName\":\"ChargingStationAndConnector\"}";

        String gqlRequestBlock = """
                {"query":"query ChargingStationAndConnector{
                	connector(id: "05f113a5-d662-4f00-bf31-fc18e10a6306"){
                		id
                		evseId
                		connectorType{
                			id
                			chargePointType
                		}
                	}
                	station:cp(id: "33842d46-3b53-4f7f-b2a9-cc062658e8c7"){
                		id
                		address
                		city
                		zip
                		country
                	}
                }","operationName":"ChargingStationAndConnector"}
                """;
    }
}
