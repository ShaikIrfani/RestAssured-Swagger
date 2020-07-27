package com.jgsudhakar.restassured.utils;

import com.google.gson.Gson;
import com.jgsudhakar.restassured.pojo.*;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @Author : Sudhakar Tangellapalli
 * @File : com.jgsudhakar.restassured.utils.AutoCodeGenUtil
 * @Date : 21/07/2020
 */
public class AutoCodeGenUtil {

    private static Map<String,Integer> testCasesPreparedList = new HashMap<String,Integer>();

    /**
     * This method will be used to delete the all the files exists in the root path of generating folder.
     * */
    public static void safeDeleteFiles(File directory) {
        if(null != directory && directory.isDirectory()){
                if(null != directory.listFiles()){
                    for(File fil : directory.listFiles()){
                        if(fil.isDirectory()){
                            safeDeleteFiles(fil);
                        }else{
                            fil.delete();
                        }
                }
            }
            directory.delete();
        }else{
            File[] filesList = directory.listFiles();
            if(null != filesList) {
                for (File fileData : filesList) {
                    if(fileData.exists() &&  fileData.isFile())
                        fileData.delete();
                }
            }
        }
    }

    /**
     * This method will create directory if not exist in the system path
     * */
    public static void createDirectoryIfNotExists(String path){
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void printData(String message){
        System.out.println(message);
    }

    /**
     * This method will delete the old scripts which was generated from the location specified.
     * And this will create a new path in the same location to store the newly generated files.
     * */
    public static void reInitilize(){
        printData("Code Generating for Automation");
        printData("##############################");
        String codePath = "D:\\sudhakar\\softwares\\SpringSTS\\sts-4.1.0.RELEASE\\MxAdminNew\\MxAdminAM\\src\\com\\jgsudhakar\\restassured\\junit\\generated\\";

        // Deleting Existing classed
        printData("Deleting old files");
        safeDeleteFiles(new File(codePath));
        printData("Deleted Successfully");

        printData("Creating Directory To Store Generated Files");
        createDirectoryIfNotExists(codePath);
        printData("Directory Created");

    }

    /**
     * This method will read the swagger json and construct the final object which hold the required information to generate the
     * Rest Assured Automation code.
     * */
    public static AutoMationDto getAutomationObjectFromSwagger(String path) {
        if(null == path || path.equalsIgnoreCase(""))
            path = "D:\\sudhakar\\softwares\\SpringSTS\\sts-4.1.0.RELEASE\\MxAdminNew\\MxAdminAM\\src\\api-docs.json";
        StringBuilder swaggerJsonDataBuilder = new StringBuilder();

        System.out.println(" Reading the Swagger Json Data from : "+path);

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(data-> {
                swaggerJsonDataBuilder.append(data);
            });
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertNotNull(null,"Failed while reading the Swagger JSon. Please do validate the Swagger JSon.");
        }
        AutoMationDto data = new Gson().fromJson(swaggerJsonDataBuilder.toString(), AutoMationDto.class);
        printData("Reading the Swagger Json Data End");

        return data;
    }

    /**
     * This method will be used to Create Java Class for Each API tag
     */
    public static void generateAutomationScript(String tagName, List<Operation> operations, Map<String, Definitions> definitions, String baseUrl, String path) throws IOException {

        // creating the file and writing the content
        StringBuilder stringBuilder = new StringBuilder();
        String className = "";

        //package Import
        // class Name Creation
        className = operations.get(0).getTags().get(0).replaceAll(" ", "");
        String groupName = className;
        if(!testCasesPreparedList.containsKey(className)){
            testCasesPreparedList.put(className,1);
            printData(">> Started Test Case Generating for >>" + className);
        }else{
            Integer alreadyMappedCount = testCasesPreparedList.get(className);
            testCasesPreparedList.put(className,(alreadyMappedCount+1));
        }
        stringBuilder.append(" " +
                "package com.jgsudhakar.restassured.junit.generated;\n" +
                "import org.json.simple.JSONObject;\n" +
                "import org.json.simple.JSONArray;\n" +
                "import org.testng.Assert;\n" +
                "import org.testng.annotations.Test;\n" +
                "import com.jgsudhakar.restassured.junit.BaseTestClass;\n" +
                "import io.restassured.RestAssured;\n" +
                "import io.restassured.http.Method;\n" +
                "import io.restassured.path.json.JsonPath;\n" +
                "import io.restassured.response.Response;\n" +
                "import io.restassured.specification.RequestSpecification;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "import io.restassured.response.Response;\n" +
                "import io.restassured.http.Headers;\n" +
                "import io.restassured.response.ResponseBody;\r\n" +
                "import org.testng.annotations.Listeners;\r\n" +
                "import com.jgsudhakar.restassured.reports.ExtentReporterNG;" +
                "\n\n");
        stringBuilder.append("@Listeners(value = ExtentReporterNG.class)\r\n" +
                "public class " + className).append(" extends BaseTestClass { \r\n");
        stringBuilder.append("\r\n");
        operations.forEach(operationData -> {
//            if(operationData.getTags().contains("Language Collection")) {
                Map<String, Object> reqBody = new HashMap<>();
                StringBuffer headerMapData = new StringBuffer();
                StringBuffer reqMapData = new StringBuffer();
                headerMapData.append("   Map<String, String> headerMap = new HashMap<>();\r\n");
                reqMapData.append("   Map<String, Object> reqBody = new HashMap<>();\r\n");
                operationData.getParameters().forEach(parameter -> {
                    if (parameter.getIn().equals("header")) {
                        Object headerValue = RestAssuredConstants.getHeaderValue(parameter.getName());
                        if (parameter.isRequired()) {
                            headerMapData.append("   headerMap.put(" + '"' + parameter.getName() + '"' + ", " + '"' + headerValue + '"' + ");\r\n");
                        } else
                            headerMapData.append("   headerMap.put(" + '"' + parameter.getName() + '"' + ", " + '"' + headerValue + '"' + ");\r\n");
                    } else if (parameter.getIn().equals("body")) {
                        if (parameter.isRequired()) {
                            String refName = (null != parameter.getSchema().getOriginalRef() ? parameter.getSchema().getOriginalRef() : parameter.getSchema().getItems().getOriginalRef());
                            Definitions reqData = definitions.get(refName);
                            List<String> requiData = reqData.getRequired();
                            if (null == requiData)
                                requiData = new ArrayList<>();
                            List<String> requiredData = requiData;
                            Map<String, Properties> properties = reqData.getProperties();
                            properties.entrySet().forEach(reqBodyData -> {
                                String key = reqBodyData.getKey();
                                Properties value = reqBodyData.getValue();
                                Object bodyValue = RestAssuredConstants.getBodyValue(key);
                                if (requiredData.contains(key)) {
                                    if (value.getType().equals("integer")) {
                                        reqBody.put(key, 1);
                                        reqMapData.append("   reqBody.put(" + '"' + key + '"' + ", " + '"' + bodyValue + '"' + ");\r\n");
                                    } else if (value.getType().equals("string")) {
                                        reqBody.put(key, "DATA");
                                        reqMapData.append("   reqBody.put(" + '"' + key + '"' + ", " + '"' + bodyValue + '"' + ");\r\n");
                                    } else {
                                        reqMapData.append("   reqBody.put(" + '"' + key + '"' + ", " + '"' + bodyValue + '"' + ");\r\n");
                                    }
                                } else {
                                    // In Some Cases this Type will be null , Because there may be scenario where Array of Objects need to send
                                    String type = value.getType();
                                    if (null != type) {
                                        if (type.equals("integer")) {
                                            //											reqBody.put(key, );
                                            reqMapData.append("   reqBody.put(" + '"' + key + '"' + ", " + '"' + bodyValue + '"' + ");\r\n");
                                        } else if (type.equals("string")) {
                                            reqBody.put(key, "");
                                            reqMapData.append("   reqBody.put(" + '"' + key + '"' + ", " + '"' + bodyValue + '"' + ");\r\n");
                                        } else {
                                            reqMapData.append("   reqBody.put(" + '"' + key + '"' + ", " + '"' + bodyValue + '"' + ");\r\n");
                                        }
                                    } else {
                                        String objName = value.getOriginalRef();
                                        Definitions refDef = definitions.get(objName);
                                        List<String> refDefData = refDef.getRequired();
                                        Map<String, Properties> propData = refDef.getProperties();
                                        propData.forEach((k, v) -> {
//										System.out.println("Key::>"+k);
//										String typeData = v.getType();
//										if(null != typeData && typeData.equalsIgnoreCase("object")){
//											reqMapData.append("   JSONObject "+k+ " = new JSONObject();");
//											StringBuffer buffer = new StringBuffer();
//											StringBuffer sBuff = setReqData(k, v, buffer,definitions);
//											reqMapData.append("   "+sBuff.toString());
//											reqMapData.append("   reqBody.put(" + '"' + k + '"' + ", " + '"' + k + '"' + ");\r\n");
//										}else if(null != typeData && typeData.equalsIgnoreCase("array")){
//											reqMapData.append("   JSONArray  "+k+ " = new JSONArray();");
//											StringBuffer buffer = new StringBuffer();
//											StringBuffer sBuff = setReqData(k, v, buffer,definitions);
//											reqMapData.append("   "+sBuff.toString());
//											reqMapData.append("   reqBody.put(" + '"' + k + '"' + ", " + k + ");\r\n");
//										}
                                        });
                                    }
                                }
                            });
                        }
                    }

                });
                // Genrating The Method
                stringBuilder.append("\r\n");
                stringBuilder.append(
                        "@Test(groups=" + '"' + groupName + '"' + ")\r\n" +
                                "  public void " + operationData.getOperationId() + "(){\r\n" +
                                "    String url = " + '"' + baseUrl + '"' + ";\r\n" +
                                headerMapData.toString() +
                                "    headerMap.put(" + '"' + "Content-Type" + '"' + "," + '"' + (null != operationData.getConsumes() ? operationData.getConsumes().get(0) : "application/json") + '"' + ");\r\n" +
                                reqMapData.toString() +
                                "    Response response = " + operationData.getOperationType() + "(reqBody,headerMap,url);\r\n" +
                                "    int resStatusCode = response.getStatusCode();\r\n" +
                                "    ResponseBody  responseBody = response.getBody();\r\n" +
                                "    Headers headers = response.getHeaders();\r\n" +
                                "    Assert.assertEquals(resStatusCode, 200);\r\n" +
                                "}\r\n"
                );


//            }// end tag if

            // Response Data
            Map<String, Response> responses = operationData.getResponses();
            responses.entrySet().forEach(resData -> {
//				System.out.println(resData.getKey());
            });
        });

        // Before writing to Location , Check whether the file Exist or not, Because in some cases this path may be different and Collection May be same
        String fileNameWithPath = path + className + ".java";
        File file = new File(fileNameWithPath);
        if (file.exists()) {
            // reading the file and updating the data in the same content
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            StringBuilder stringBuffer = new StringBuilder();
            while ((st = br.readLine()) != null) {
                stringBuffer.append(st).append("\r\n");
            }
            int firstTestIndex = stringBuffer.toString().indexOf("@Test");
            int lastBracket = stringBuffer.toString().lastIndexOf("}");
            String trimData = stringBuffer.toString().substring(firstTestIndex,lastBracket);
            stringBuilder.append(trimData);
        }
        stringBuilder.append("\r\n } \r\n");
        // writing to the file
        Files.write(Paths.get(fileNameWithPath), stringBuilder.toString().getBytes());
        Integer sysCount = testCasesPreparedList.get(className);
        if(sysCount <2)
            printData(">> End Generating Test Case for >>" + className);
    }

    private static StringBuffer setReqData(String key, Properties properties, StringBuffer buffer, Map<String, Definitions> definitions){
//		buffer.append("\r\n   JSONObject "+key+" = new JSONObject();");
        String type = properties.getType();
        if(null != type && type.equals("array")){
            buffer.append("   JSONArray  "+key + " = new JSONArray();");
            String originalRef = properties.getOriginalRef();
            Definitions def = definitions.get(originalRef);
            List<String> reqParams = def.getRequired();
            Map<String, Properties> paramMap = def.getProperties();
            paramMap.entrySet().forEach(props -> {
                System.out.println(props.getKey() + ":::");
                Properties value = props.getValue();
                String ty = value.getType();
                if(null != ty && ty.equalsIgnoreCase("object")){
                    setReqData(props.getKey(),value,buffer,definitions);
                }else if(null != ty && ty.equalsIgnoreCase("array")){

                }else{

                }
            });

        }else if(null != type && type.equalsIgnoreCase("object")){
            String originalRef = properties.getOriginalRef();
            Definitions def = definitions.get(originalRef);
            List<String> reqParams = def.getRequired();
            Map<String, Properties> paramMap = def.getProperties();
            paramMap.entrySet().forEach(defProp -> {
                System.out.println(defProp.getKey() + ":::"+defProp.getValue());
                buffer.append("   "+'"'+defProp.getKey() +'"'+ "," + '"'+defProp.getValue()+'"'+";");
            });
        }else {

        }
        return buffer;
    }
}
