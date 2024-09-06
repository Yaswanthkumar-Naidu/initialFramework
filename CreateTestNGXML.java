package reusable;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateTestNGXML {
	private static final Logger logger =LoggerFactory.getLogger(CreateTestNGXML.class.getName());
	   

    public static void main(String[] args) {
    	if (args.length == 0) {
            logger.info("Usage: java CreateTestNGXML <class1> <class2> ... <classN>");
            System.exit(1);
        }

        generateTestNGXML(args);
    }

    private static void generateTestNGXML(String[] classNames) {
    	classNames = classNames[0].split(",");
        try (FileWriter fileWriter = new FileWriter((Paths.get("").toAbsolutePath().toString()).replace("/src/main/java/Reusable", "")+"/RegressionXML_Custom/DynamicSuite_TestNG.xml")) {
            fileWriter.write("<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\">\n");
            fileWriter.write("<suite name=\"Suite\">\n");
            fileWriter.write(String.format("  <parameter name=\"thread-count\" value=\"${ParallelNodes}\"></parameter>%n"));
            
            for (String className : classNames) {
            	fileWriter.write(String.format("  <test name=\"%s\">%n", className));
                fileWriter.write(String.format("    <classes>%n"));
                fileWriter.write(String.format("      <class name=\"%s\" />%n", className));
                fileWriter.write(String.format("    </classes>%n"));
                fileWriter.write(String.format("  </test>%n"));

            }

            fileWriter.write("</suite>\n");

           logger.info("TestNG XML file created successfully: testng.xml");
        } catch (IOException e) {
            logger.info("Error creating TestNG XML file: {}", e.getMessage());
        }
    }
}

