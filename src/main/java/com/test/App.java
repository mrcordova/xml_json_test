package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;

import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;
import org.xml.sax.SAXException;


public final class App {
    private App() {
    }

    /**
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws Exception {
       

        if (args.length <= 1 || args.length > 2){
            System.out.println("Invaild Format\nFormat: [file] --[xml | json | schema]\n");
            System.exit(0);
        } 

        InputStream in = new FileInputStream(args[0]);
        String cmd = args[1];

        String fileName = new File(args[0]).getName();
        
        int dotIndex = fileName.lastIndexOf('.');
        String fileType = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
        
        Scanner sc = new Scanner(in);
        StringBuffer sb = new StringBuffer();
        while (sc.hasNext()) {
            sb.append(sc.nextLine());
        }

        if (cmd.equals("--json")  && fileType.equals("xml")){
            
            JSONObject jsonObj = XML.toJSONObject(sb.toString());
            String jsonPrettyPrintString = jsonObj.toString(2);

            FileWriter fileWriter = new FileWriter("./output.json");

            fileWriter.write(jsonPrettyPrintString);
            fileWriter.close();
          
        } else if (cmd.equals("--xml")  && fileType.equals("json")){
           
            String jsonString = sb.toString();
            JSONObject jsonObj = new JSONObject(jsonString);

            FileWriter fileWriter = new FileWriter("./output.xml");
            fileWriter.write(XML.toString(jsonObj));
            fileWriter.close();

        } else if (cmd.equals("--schema")){

            if (fileType.equals("json")){
                File schemaFile = new File("./src/main/java/com/test/schema.json");
                JSONTokener schemaData = new JSONTokener(new FileInputStream(schemaFile));
                JSONObject jsonSchema = new JSONObject(schemaData);

            //json Data
                File jsonData = new File("./output.json");
                JSONTokener jsonDataFile = new JSONTokener(new FileInputStream(jsonData));
                JSONObject jsonObject = new JSONObject(jsonDataFile);

                try {
                    Schema schemaValidator = SchemaLoader.load(jsonSchema); 
                    schemaValidator.validate(jsonObject);
               
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                
                    e.getCausingExceptions().stream()
                    .map(ValidationException::getMessage)
                    .forEach(System.out::println);
              }
            } else if (fileType.equals("xml")) {
                String xmlPath = args[0];
                String xsdPath = "./src/main/java/com/test/schema.xsd";
               
                System.out.println(fileName + " validates against schema.xsd? "+ validateXMLSchema(xsdPath, xmlPath ));     
            }
           
              
            

        } else {
            System.out.println(fileType + " must be different then command line arguement: "+ args[1]);
            System.exit(0);
        }
        
        sc.close();

        
    }
    public static boolean validateXMLSchema(String xsdPath, String xmlPath){
        
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            javax.xml.validation.Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: "+ e.getMessage());
            return false;
        }
        return true;
    }
}
