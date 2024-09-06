package common_utilities.utilities;


//    private static final Logger logger = LoggerFactory.getLogger(Util.class.getName());
//
//    public enum Mode {
//        ALPHA, ALPHANUMERIC, NUMERIC
//    }
//
//    public static String getRootPath() {
//        return Paths.get("").toAbsolutePath().toString();
//    }
//
//    public Util() {
//
//    }
//
//    Formatter formatter = new Formatter();
//
//    public String getData(String colName, String scenario, String testCase, String homePath, Integer currentIteration) {
//        String dataValue = "";
//
//
//        String dbPath = homePath + "/test/resources/TestScripts-TestData/" + scenario + ".csv";
//
//        try {
//            ArrayList<String> whereClause = new ArrayList<>();
//            whereClause.add("TestScript::" + testCase);
//            whereClause.add("Iteration::" + Integer.parseInt(currentIteration.toString()));
//            Map<String, ArrayList<String>> result = PoiReadExcel.fetchWithCondition(dbPath, "TestData", whereClause);
//
//            for (int i = 0; i < result.get("TestScript").size(); i++) {
//                if (testCase.equals(result.get("TestScript").get(i)) && Integer.parseInt(currentIteration.toString()) == Integer.parseInt(result.get("Iteration").get(i))) {
//                    dataValue = result.get(colName).get(i);
//                    break;
//                }
//            }
//
//            return dataValue;
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            logger.error(e.getMessage());
//            logger.error(String.format("%s", "StackTrace:") + e.getStackTrace());
//
//            throw e;
//        }
//
//    }
//
//    public Map<String, ArrayList<String>> getDictionaryFromPOI(String path, String SheetName, String Clause) {
//
//        try {
//
//            ArrayList<String> whereClause_Web_TotalCount = new ArrayList<>();
//            whereClause_Web_TotalCount.add(Clause);
//            return PoiReadExcel.fetchWithCondition(path, SheetName, whereClause_Web_TotalCount);
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            logger.error(e.getMessage());
//            logger.error("StackTrace:" + e.getStackTrace());
//
//            throw e;
//        }
//    }
//
//    public Map<String, String> getTestData(String TDPath, String TestDataSheet, String testCase, Integer currentIteration) {
//        try {
//
//
//            ArrayList<String> TestDataSheets = (ArrayList<String>) List.of(TestDataSheet.split(";"));
//            HashMap<String, String> TestData = new HashMap<>();
//
//            for (String TD : TestDataSheets) {
//                String TestDataPath = TDPath + TD + ".csv";
//
//
//                ArrayList<String> whereClause_TestData = new ArrayList<>();
//                whereClause_TestData.add("TestScript::" + testCase);
//                whereClause_TestData.add("Iteration::" + currentIteration.toString());
//                Map<String, ArrayList<String>> result = PoiReadExcel.fetchWithCondition(TestDataPath, "TestData", whereClause_TestData);
//
//
//                for (String key : result.keySet()) {
//
//                    if (!TestData.containsKey(key)) {
//                        TestData.put(key, result.get(key).get(0));
//                    } else {
//                        TestData.replace(key, result.get(key).get(0));
//                    }
//                }
//
//            }
//            return TestData;
//        } catch (Exception e) {
//
//
//            logger.info(e.getMessage());
//            logger.error(e.getMessage());
//            logger.error("StackTrace:" + e.getStackTrace());
//
//            throw e;
//        }
//
//    }
//
//
//    public static String getCurrentDate() {
//
//        try {
//            LocalDateTime today = LocalDateTime.now();
//            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd-yyyy");
//            String date = today.format(format);
//            date = date.replace(":", "_");
//            date = date.replace(" ", "_");
//            date = date.replace(".", "_");
//            date = date.replace("-", "_");
//            return date;
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            logger.error(e.getMessage());
//            logger.error("StackTrace:" + e.getStackTrace());
//
//            throw e;
//        }
//    }
//
//    /// <summary>
//    /// This method is get the Current Time in the format 'hh-mm-ss'
//    /// </summary>
//    /// Author: Jigesh Shah
//    /// <returns>Current Time in String Format</returns>
//    public static String getCurrentTime() {
//        try {
//            Date date = Calendar.getInstance().getTime();
//            DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss.sss");
//            String result = dateFormat.format(date);
//            result = result.replace(":", "_");
//            result = result.replace(" ", "_");
//            result = result.replace(".", "_");
//            return result;
//
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            logger.error(e.getMessage());
//            logger.error("StackTrace:" + e.getStackTrace());
//
//            throw e;
//        }
//
//    }
//
//
//    /**
//     * Author: Santosh Kumar Anupati
//     * Method Name: getData
//     * Description: This method is to fetch Data from the column in the Input sheet against the test case that is currently executing based on the current iteration
//     * Return Type: String
//     *
//     * @throws Exception
//     */
//    public String getReusableData(String colName, String scenario, String testCase, String homePath, Integer currentIteration) {
//        String dataValue = "";
//
//
//        String dbPath = homePath + "/test/resources/TestScripts-TestData/" + scenario + ".csv";
//
//        try {
//            ArrayList<String> whereClause = new ArrayList<>();
//            whereClause.add("TestScript::" + testCase);
//            whereClause.add("Iteration::" + Integer.parseInt(currentIteration.toString()));
//            Map<String, ArrayList<String>> result = PoiReadExcel.fetchWithCondition(dbPath, "TestData", whereClause);
//
//
//            for (int i = 0; i < result.get("TestScript").size(); i++) {
//                if (testCase.equalsIgnoreCase(result.get("TestScript").get(i)) && Integer.parseInt(currentIteration.toString()) == Integer.parseInt(result.get("Iteration").get(i))) {
//                    dataValue = result.get(colName).get(i);
//                    break;
//                }
//            }
//
//            return dataValue;
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            logger.error(e.getMessage());
//            logger.error("StackTrace:" + e.getStackTrace());
//
//            throw e;
//        }
//
//    }
//
//
//    /// <summary>
//    /// This method will create a directory for Business Module
//    /// </summary>
//    /// <param name="resultsPath"></param>
//    /// <param name="scenario"></param>
//    /// <returns></returns>
//    public String CreateModuleFolder(String resultsPath, String module) {
//
//        try {
//
//            File dir = new File(resultsPath + "/" + module);
//            if (!dir.exists()) dir.mkdirs();
//
//
//            return (resultsPath + "/" + module);
//
//
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            logger.error(e.getMessage());
//            logger.error("StackTrace:" + e.getStackTrace());
//
//            throw e;
//        }
//    }
//
//
//    /// <summary>
//    /// This method is to create the Scenario folder for the current executing scenario
//    /// </summary>
//    ///  Author: Jigesh Shah
//    /// <param name="resultsPath">Path of the Results Folder</param>
//    /// <param name="scenario">Name of the Scenario</param>
//    /// <returns></returns>
//    public String CreateScenarioFolder(String resultsPath, String scenario) {
//
//        try {
//
//            File dir = new File(resultsPath + "/" + scenario);
//            if (!dir.exists()) dir.mkdirs();
//            return (resultsPath + "/" + scenario);
//
//
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            logger.error(e.getMessage());
//            logger.error("StackTrace:" + e.getStackTrace());
//
//            throw e;
//        }
//    }
//
//    /// <summary>
//    ///  This method is to create the current Browser folder within the scenario folder
//    /// </summary>
//    ///  Author: Jigesh Shah
//    /// <param name="scenarioFolderPath">Path of the Scenario Folder</param>
//    /// <param name="browser">Browser Type</param>
//    /// <returns></returns>
//    public String CreateBrowserFolder(String scenarioFolderPath, String browser) {
//
//        try {
//            File dir = new File(scenarioFolderPath + "/" + browser);
//            if (!dir.exists()) dir.mkdirs();
//            return (scenarioFolderPath + "/" + browser);
//
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            logger.error(e.getMessage());
//            throw e;
//        }
//    }
//
//
//    public Map<String, String> getORElements(String ORPath, String ORFileNames) {
//        HashMap<String, String> ObjRep = new HashMap<>();
//        ArrayList<String> ORFiles = (ArrayList<String>) List.of(ORFileNames.split(";"));
//
//        for (String ORFile : ORFiles) {
//            try {
//
//                String propFile = ORPath + ORFile + ".txt";
//                RetreiveProperties RP = new RetreiveProperties();
//                Map<String, String> TempObjRep = RP.getProperties(propFile);
//
//
//                for (String key : TempObjRep.keySet()) {
//
//                    if (!ObjRep.containsKey(key)) {
//                        ObjRep.put(key, TempObjRep.get(key));
//                    } else {
//                        ObjRep.replace(key, TempObjRep.get(key));
//                    }
//                }
//
//
//            } catch (Exception e1) {
//                logger.info(e1.getMessage());
//            }
//
//        }
//        return ObjRep;
//    }
//
//
//    /**
//     * Author: Santosh Kumar Anupati
//     * Method Name: getObjectFromObjectMap
//     * Description: This method is to retrieve object from the particular module ObjectMap.properties file for the key given
//     * Return Type: String
//     *
//     * @throws Throwable
//     */
//    public String getObjectFromObjectMap(String key, String scenario, String HomePath) {
//        String value = "";
//
//        try {
//
//
//            String propFile = HomePath + "/test/resources/ObjectRepository/" + scenario + ".txt";
//            RetreiveProperties RP = new RetreiveProperties();
//            Map<String, String> ObjRep = RP.getProperties(propFile);
//
//
//            if (!key.equals(""))
//                value = ObjRep.get(key);
//            else
//                value = null;
//        } catch (Exception e1) {
//            logger.info(e1.getMessage());
//        }
//        return value;
//    }

import com.github.javafaker.Faker;
import common_utilities.common.PoiReadExcel;
import common_utilities.common.RetreiveProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static api_utilities.api_common.APIUtil.r;

public class Util {

        private static final String TEST_SCRIPT_PREFIX = "TestScript::";
        private static final String TEST_DATA_PREFIX ="TestData";
    private static final String TEST_ITERATION_PREFIX ="Iteration";

    private static final String TEST_TS_PREFIX ="TestScript";


        private static final Logger logger = LoggerFactory.getLogger(Util.class);

        public enum Mode {
            ALPHA, ALPHANUMERIC, NUMERIC
        }

        public static String getRootPath() {
            return Paths.get("").toAbsolutePath().toString();
        }

        public Util() {
            // Default constructor
        }


        public Map<String, ArrayList<String>> getDictionaryFromPOI(String path, String sheetName, String clause) {

                ArrayList<String> whereClause = new ArrayList<>();
                whereClause.add(clause);
                return PoiReadExcel.fetchWithCondition(path, sheetName, whereClause);

        }

        public Map<String, String> getTestData(String tdPath, String testDataSheet, String testCase, Integer currentIteration) {

                List<String> testDataSheets = Arrays.asList(testDataSheet.split(";"));
                Map<String, String> testData = new HashMap<>();

                for (String td : testDataSheets) {
                    String testDataPath = String.format("%s%s.csv", tdPath, td);

                    ArrayList<String> whereClause = new ArrayList<>();
                    whereClause.add(TEST_SCRIPT_PREFIX + testCase);
                    whereClause.add(TEST_ITERATION_PREFIX + currentIteration);
                    Map<String, ArrayList<String>> result = PoiReadExcel.fetchWithCondition(testDataPath, TEST_DATA_PREFIX, whereClause);

                    for (Map.Entry<String, ArrayList<String>> entry : result.entrySet()) {
                        testData.put(entry.getKey(), entry.getValue().get(0));
                    }
                }
                return testData;

        }

        public static String getCurrentDate() {
                LocalDateTime today = LocalDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                String date = today.format(format);
                return date.replace(":", "_").replace(" ", "_").replace(".", "_").replace("-", "_");

        }

        public static String getCurrentTime() {

                DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss.sss");
                String result = dateFormat.format(Calendar.getInstance().getTime());
                return result.replace(":", "_").replace(" ", "_").replace(".", "_");

        }






        public Map<String, String> getORElements(String orPath, String orFileNames) {
            Map<String, String> objRep = new HashMap<>();
            List<String> orFiles = Arrays.asList(orFileNames.split(";"));

            for (String orFile : orFiles) {
                try {
                    String propFile = String.format("%s%s.txt", orPath, orFile);
                    RetreiveProperties rp = new RetreiveProperties();
                    Map<String, String> tempObjRep = rp.getProperties(propFile);

                    for (Map.Entry<String, String> entry : tempObjRep.entrySet()) {
                        objRep.put(entry.getKey(), entry.getValue());
                    }
                } catch (Exception e) {
                    logger.error("Error occurred while getting OR elements: {}", e.getMessage(), e);
                }
            }
            return objRep;
        }

        public String getObjectFromObjectMap(String key, String scenario, String homePath) {
            try {
                String propFile = String.format("%s/test/resources/ObjectRepository/%s.txt", homePath, scenario);
                RetreiveProperties rp = new RetreiveProperties();
                Map<String, String> objRep = rp.getProperties(propFile);
                return objRep.getOrDefault(key, null);
            } catch (Exception e) {
                logger.error("Error occurred while getting object from object map: {}", e.getMessage(), e);
                return null;
            }
        }


        public Map<String, String> getObjectFromCommonRep(String homePath) {
            Map<String, String> objRep = new HashMap<>();
            try {
                String propFile = homePath + "/test/resources/ObjectRepository/Common.txt";
                RetreiveProperties rp = new RetreiveProperties();
                objRep = rp.getProperties(propFile);
            } catch (FileNotFoundException e) {
                logger.error("File not found: {}", e.getMessage());
            } catch (IOException e) {
                logger.error("IO Exception: {}", e.getMessage());
            } catch (Exception e) {
                logger.error("Exception: {}", e.getMessage(), e);
            }
            return objRep;
        }

        public String getDataValueForAppiumTC(String colName, String testCase, String dataPath, Integer currentIteration) {
            String dataValue = "";

                ArrayList<String> whereClause = new ArrayList<>();
                whereClause.add(TEST_SCRIPT_PREFIX + testCase);
                whereClause.add(TEST_ITERATION_PREFIX + currentIteration);
                Map<String, ArrayList<String>> result = PoiReadExcel.fetchWithCondition(dataPath, TEST_DATA_PREFIX, whereClause);

                for (int i = 0; i < result.get(TEST_TS_PREFIX).size(); i++) {
                    if (testCase.equals(result.get(TEST_TS_PREFIX).get(i)) && currentIteration.equals(Integer.parseInt(result.get(TEST_ITERATION_PREFIX).get(i)))) {
                        dataValue = result.get(colName).get(i);
                        break;
                    }
                }

            return dataValue;
        }

        public String generateRandomString(int length, Mode mode) {
            StringBuilder buffer = new StringBuilder();
            String characters = switch (mode) {
                case ALPHA -> "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                case ALPHANUMERIC -> "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                case NUMERIC -> "1234567890";
            };
            for (int i = 0; i < length; i++) {
                int index = r.nextInt(characters.length());
                buffer.append(characters.charAt(index));
            }
            return buffer.toString();
        }

        public String createDateFolder(String homePath) {

                File dir = new File(homePath + "/Results");
                if (!dir.exists()) dir.mkdirs();

                String date = "Run_" + Util.getCurrentDate();
                String resultsFolder = homePath + "/Results/" + date;
                dir = new File(resultsFolder);
                if (!dir.exists()) dir.mkdirs();

                return resultsFolder;

        }

        public String createDirectory(String directoryPath, String directoryName) {

                String directoryFullPath = directoryPath + File.separator + directoryName;
                File dir = new File(directoryFullPath);
                if (!dir.exists()) dir.mkdirs();

                return directoryFullPath;

        }

        public String generateFirstName() {
            return new Faker().name().firstName();
        }

        public String generateLastName() {
            return new Faker().name().lastName();
        }

        public String getRandom(String value) {
            if (value.toLowerCase().contains("string")) {
                return generateRandomString(r.nextInt(16) + 5, Mode.ALPHA);
            } else if (value.toLowerCase().contains("name")) {
                return generateFirstName();
            } else if (value.toLowerCase().contains("dob")) {
                return getDateOfBirth(value);
            } else if (value.toLowerCase().contains("ssn")) {
                return getRandomSSN();
            } else if (value.toLowerCase().contains("ssn_dash")) {
                return getRandomSSN("-");
            } else if (value.toLowerCase().contains("psuedossn")) {
                return getRandomSSN();
            } else if (value.toLowerCase().contains("psuedossn_dash")) {
                return getRandomSSN("-");
            } else if (value.toLowerCase().contains("individualid")) {
                return generateRandomString(9, Mode.NUMERIC);
            } else if (value.toLowerCase().contains("number")) {
                int length = value.split(";").length > 1 ? Integer.parseInt(value.split(";")[1]) : 10;
                return generateRandomString(length, Mode.NUMERIC);
            } else if (value.toLowerCase().contains("alphanum")) {
                return generateRandomString(30, Mode.ALPHANUMERIC);
            } else if (value.toLowerCase().contains("uuid") || value.toLowerCase().contains("guid")) {
                return UUID.randomUUID().toString();
            }
            return value;
        }

        public String getDateOfBirth(String dataValue, String... dateTimeFormat) {
            String dateFormat = dateTimeFormat.length == 0 ? "MM/dd/yyyy" : dateTimeFormat[0];

                String[] randomDob = dataValue.split(";");
                int days = switch (randomDob.length) {
                    case 2 -> (Integer.parseInt(randomDob[1]) * 365) + (Integer.parseInt(randomDob[1]) / 4);
                    case 3 -> (Integer.parseInt(randomDob[1]) * 365) + (Integer.parseInt(randomDob[2]) * 30) + (Integer.parseInt(randomDob[1]) / 4);
                    case 4 -> (Integer.parseInt(randomDob[1]) * 365) + (Integer.parseInt(randomDob[2]) * 30) + (Integer.parseInt(randomDob[3]));
                    default -> 0;
                };
                LocalDate localDate = LocalDate.now().minusDays((long)r.nextInt(days) + 1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
                return localDate.format(formatter);

        }

        public String getRandomSSN(String... delimiter) {
            if (delimiter.length == 0) {
                delimiter = new String[]{""};
            }
            Integer iThree = getRandomNumber(132, 921);
            Integer iTwo = getRandomNumber(12, 83);
            Integer iFour = getRandomNumber(1423, 9211);
            return String.join(delimiter[0], iThree.toString(), iTwo.toString(), iFour.toString());
        }

        public String getRandomPsuedoSSN(String... delimiter) {
            if (delimiter.length == 0) {
                delimiter = new String[]{""};
            }
            Integer iThree = getRandomNumber(911, 988);
            Integer iTwo = getRandomNumber(12, 83);
            Integer iFour = getRandomNumber(1423, 9211);
            return String.join(delimiter[0], iThree.toString(), iTwo.toString(), iFour.toString());
        }

        public int getRandomNumber(int min, int max) {
            return r.nextInt(max - min) + min;
        }

    public Map<String, String> getTestData(String tdPath, String testDataSheet, String testCase, Integer currentIteration, String... defaultTestDataFormat) throws InterruptedException {
        if (defaultTestDataFormat.length == 0) {
            defaultTestDataFormat = new String[]{".csv"};
        }

        ArrayList<String> testDataSheets = new ArrayList<>(List.of(testDataSheet.split(";")));
        HashMap<String, String> testData = new HashMap<>();

        for (String td : testDataSheets) {
            String testDataPath = tdPath + td + defaultTestDataFormat[0];
            ArrayList<String> whereClause = new ArrayList<>();
            whereClause.add(TEST_SCRIPT_PREFIX + testCase);
            whereClause.add("Iteration::" + currentIteration);
            Map<String, ArrayList<String>> result = PoiReadExcel.fetchWithCondition(testDataPath, TEST_DATA_PREFIX, whereClause);

            if (result.isEmpty()) {
                logger.error("Blank column in Test Data - There is no data in the column for Iteration {} of test case {}", currentIteration, testCase);
            }

            for (Map.Entry<String, ArrayList<String>> entry : result.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().get(0);
                if (value.toLowerCase().trim().startsWith("random_")) {
                    value = getRandom(value);
                    Thread.sleep(500); // Consider reducing or removing this
                    logger.info("Random Value Added in {} = {}", key, value);
                }
                testData.put(key, value);
            }
        }
        return testData;
    }



//----------------------------
//    public Map<String, String> getObjectFromCommonRep(String HomePath) {
//        String value = "";
//        Map<String, String> ObjRep = new HashMap<>();
//        try {
//
//
//            String propFile = HomePath + "/test/resources/ObjectRepository/Common.txt";
//            RetreiveProperties RP = new RetreiveProperties();
//            ObjRep = RP.getProperties(propFile);
//
//
//        } catch (FileNotFoundException e1) {
//            logger.info(e1.getMessage());
//        } catch (IOException e1) {
//            logger.info(e1.getMessage());
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            logger.error(e.getMessage());
//            logger.error("StackTrace:" + e.getStackTrace());
//
//        }
//        return ObjRep;
//    }
//
//
//    /**
//     * Author: Prashant Thakuri
//     * Method Name: getDataForAppiumTC
//     * Description: This method will return back the data from the test Data sheet for Appium Test cases.
//     * Return Type: String
//     */
//
//    public String getDataValueForAppiumTC(String colName, String testCase, String dataPath, Integer currentIteration) {
//        String dataValue = "";
//        String dbPath = dataPath;
//
//
//        try {
//
//            ArrayList<String> whereClause = new ArrayList<>();
//            whereClause.add("TestScript::" + testCase);
//            whereClause.add("Iteration::" + currentIteration.toString());
//            Map<String, ArrayList<String>> result = PoiReadExcel.fetchWithCondition(dbPath, "TestData", whereClause);
//
//
//            for (int i = 0; i < result.get("TestScript").size(); i++) {
//                if (testCase.equals(result.get("TestScript").get(i)) && Integer.parseInt(currentIteration.toString()) == Integer.parseInt(result.get("Iteration").get(i))) {
//                    dataValue = result.get(colName).get(i);
//                    break;
//                }
//            }
//
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            throw e;
//        }
//        return dataValue;
//
//
//    }
//
//
//    public String generateRandomString(int length, Mode mode) {
//
//        StringBuilder buffer = new StringBuilder();
//        String characters = "";
//
//        switch (mode) {
//
//            case ALPHA:
//                characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//                break;
//
//            case ALPHANUMERIC:
//                characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//                break;
//
//            case NUMERIC:
//                characters = "1234567890";
//                break;
//        }
//        int charactersLength = characters.length();
//
//        for (int i = 0; i < length; i++) {
//
//            int index = r.nextInt(charactersLength);
//
//            buffer.append(characters.charAt(index));
//        }
//        return buffer.toString();
//    }
//
//
//    public String CreateDateFolder(String homePath) {
//        try {
//            File dir = new File(homePath + "/Results");
//            if (!dir.exists()) dir.mkdirs();
//
//
//            String date = "Run_" + Util.getCurrentDate();
//
//            String resultsFolder = homePath + "/Results/" + date;
//
//
//            dir = new File(resultsFolder);
//            if (!dir.exists())
//                dir.mkdirs();
//
//            return resultsFolder;
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            logger.error(e.getMessage());
//            logger.error(String.valueOf("StackTrace:") + e.getStackTrace());
//
//            throw e;
//        }
//    }
//
//
//    public String createDirectory(String DirectoryPath, String DirectoryName) {
//        try {
//            String DirectoryFulPath = DirectoryPath + "/" + DirectoryName;
//            File dir = new File(DirectoryFulPath);
//            if (!dir.exists()) dir.mkdirs();
//
//            return DirectoryFulPath;
//
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            logger.error(e.getMessage());
//            logger.error(String.format("%s", "StackTrace:") + e.getStackTrace());
//
//            throw e;
//        }
//    }
//
//    public String generateFirstName() {
//        Faker faker = new Faker();
//        return faker.name().firstName();
//    }
//
//    public String generateLastName() {
//        Faker faker = new Faker();
//        return faker.name().lastName();
//    }
//
//
//    Random r = new Random();
//
//    public String getRandom(String value) {
//        if (value.toLowerCase().contains("string")) {
//
//            value = generateRandomString(r.nextInt(20 - 5) + 5, Mode.ALPHA);
//        }
//        if (value.toLowerCase().contains("name")) {
//
//            value = generateFirstName();
//        } else if (value.toLowerCase().contains("dob")) {
//            value = getDateOfBirth(value);
//        } else if (value.toLowerCase().contains("ssn")) {
//            value = getRandomSSN();
//        } else if (value.toLowerCase().contains("ssn_dash")) {
//            value = getRandomSSN("-");
//        } else if (value.toLowerCase().contains("psuedossn")) {
//            value = getRandomSSN();
//        } else if (value.toLowerCase().contains("psuedossn_dash")) {
//            value = getRandomSSN("-");
//        } else if (value.toLowerCase().contains("individualid")) {
//            value = generateRandomString(9, Mode.NUMERIC);
//        } else if (value.toLowerCase().contains("number")) {
//            ArrayList<String> values = (ArrayList<String>) List.of(value.split(";"));
//            int length = values.size() > 1 ? Integer.parseInt(values.get(1)) : 10;
//            value = generateRandomString(length, Mode.NUMERIC);
//        } else if (value.toLowerCase().contains("alphanum")) {
//            value = generateRandomString(30, Mode.ALPHANUMERIC);
//        } else if (value.toLowerCase().contains("uuid") || value.toLowerCase().contains("guid")) {
//            UUID guid = UUID.randomUUID();
//            value = guid.toString();
//        }
//
//        return value;
//    }
//
//
//    public String getDateOfBirth(String dataValue, String... datTimeFormat) {
//        String dfformat = "MM/dd/yyyy";
//        if (datTimeFormat.length == 0) {
//
//            dfformat = "MM/dd/yyyy";
//        }
//        String Date = "";
//        int Daterange;
//        try {
//            String[] rondomdob = dataValue.split(";");
//            int count = rondomdob.length;
//
//            int NoOfdays = (Integer.parseInt(dataValue.split(";")[1])) * 365 + ((Integer.parseInt(dataValue.split(";")[1])) / 4);
//            int min = NoOfdays + 1;
//            int max = NoOfdays + 364;
//            Daterange = r.nextInt(max - min) + min;
//
//            LocalDate Ldate = LocalDate.now();
//            if (count == 2) {
//                Ldate = Ldate.minusDays(Daterange);
//                DateTimeFormatter format = DateTimeFormatter.ofPattern(dfformat);
//                Date = Ldate.format(format);
//            } else if (count == 3) {
//                NoOfdays = ((Integer.parseInt(dataValue.split(";")[1])) * 365) + (((Integer.parseInt(dataValue.split(";")[1]) / 4) + ((Integer.parseInt(dataValue.split(";")[2]) * 30))));
//                min = NoOfdays + 1;
//                max = NoOfdays + 29;
//                Daterange = r.nextInt(max - min) + min;
//                Ldate = Ldate.minusDays(Daterange);
//                DateTimeFormatter format = DateTimeFormatter.ofPattern(dfformat);
//                Date = Ldate.format(format);
//            } else if (count == 4) {
//                NoOfdays = ((Integer.parseInt(dataValue.split(";")[1])) * 365) + Integer.parseInt(dataValue.split(";")[1]) / 4 + Integer.parseInt(dataValue.split(";")[2]) * 30 + Integer.parseInt(dataValue.split(";")[3]) * 1;
//                Ldate = Ldate.minusDays(NoOfdays);
//                DateTimeFormatter format = DateTimeFormatter.ofPattern(dfformat);
//                Date = Ldate.format(format);
//            }
//
//        } catch (Exception e) {
//            logger.info(e.toString());
//            logger.info(e.getMessage());
//            logger.error("Unable to Add Date Of Birth due to error: " + e.toString());
//            throw e;
//        }
//        return Date;
//    }
//
//
//    public String getRandomSSN(String... delimiter) {
//        if (delimiter == null) {
//            delimiter[0] = "";
//        }
//
//        Integer iThree = getRandomNumber(132, 921);
//        Integer iTwo = getRandomNumber(12, 83);
//        Integer iFour = getRandomNumber(1423, 9211);
//        return iThree.toString() + delimiter[0] + iTwo.toString() + delimiter[0] + iFour.toString();
//    }
//
//
//    public String getRandomPsuedoSSN(String... delimiter) throws NullPointerException {
//        if (delimiter == null) {
//            delimiter[0] = "";
//        }
//
//        Integer iThree = getRandomNumber(911, 988);
//        Integer iTwo = getRandomNumber(12, 83);
//        Integer iFour = getRandomNumber(1423, 9211);
//        return iThree.toString() + delimiter[0] + iTwo.toString() + delimiter[0] + iFour.toString();
//    }
//
//    Random getrandom = new Random();
//
//    public int getRandomNumber(int min, int max) {
//
//        return getrandom.nextInt(max - min) + min;
//
//    }
//
//    public Map<String, String> getTestData(String TDPath, String TestDataSheet, String testCase, Integer currentIteration, String... DefaultTestDataFormat) throws Exception {
//
//        if (DefaultTestDataFormat == null) {
//            DefaultTestDataFormat[0] = ".csv";
//
//        }
//        try {
//
//
//            ArrayList<String> TestDataSheets = (ArrayList<String>) List.of(TestDataSheet.split(";"));
//            HashMap<String, String> TestData = new HashMap<>();
//
//            for (String TD : TestDataSheets) {
//                String TestDataPath = TDPath + TD + DefaultTestDataFormat[0];
//
//
//                ArrayList<String> whereClause_TestData = new ArrayList<>();
//                whereClause_TestData.add("TestScript::" + testCase);
//                whereClause_TestData.add("Iteration::" + currentIteration.toString());
//                Map<String, ArrayList<String>> result = PoiReadExcel.fetchWithCondition(TestDataPath, "TestData", whereClause_TestData);
//
//
//                if (result.isEmpty()) {
//
//                    logger.error(String.valueOf("Blank column in Test Data - There is no data in the column  for the Iteration ") + currentIteration.toString() + String.valueOf(" of test case ") + testCase);
//                }
//
//                for (String key : result.keySet()) {
//
//                    if (result.get(key).get(0).toLowerCase().trim().startsWith("random_")) {
//                        result.get(key).set(0, getRandom(result.get(key).get(0)));
//                        Thread.sleep(500);
//                        logger.info(String.valueOf("Random Value Added in ") + key + String.valueOf(" = ") + result.get(key).get(0));
//                    }
//
//                    if (!TestData.containsKey(key)) {
//                        TestData.put(key, result.get(key).get(0));
//                    } else {
//                        TestData.replace(key, result.get(key).get(0));
//                    }
//                }
//
//            }
//            return TestData;
//        } catch (Exception e) {
//
//
//            logger.info(e.getMessage());
//            logger.error(e.getMessage());
//            logger.error(String.format("%s", "StackTrace:") + e.getStackTrace());
//
//
//            throw e;
//        }
//
//    }
//
//
//    public static String convertFromSQLDateToJAVADate(String sqlDate) {
//        String CustomDate = null;
//        if (sqlDate != null) {
//            LocalDate tradeDate = LocalDate.parse(sqlDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
//            CustomDate = tradeDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
//        }
//        return CustomDate;
//    }
//
//    public String modifyDateFormat(String Date) {
//
//        Date = Date.replace("-", "/");
//        LocalDate tradeDate = LocalDate.parse(Date, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
//        return tradeDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
//
//
//    }
//
//    public String modifyDateTimeFormattodate(String Date) {
//        String[] date = Date.split(" ");
//        Date = date[0].replace("-", "/");
//        LocalDate tradeDate = LocalDate.parse(Date, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
//        return tradeDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
//
//    }
//
//    public Map<String, String> getTestData(String TDPath, String TestDataSheet, String Environment, String currentIteration, String... DefaultTestDataFormat) throws Exception {
//
//
//        if (DefaultTestDataFormat == null) {
//            DefaultTestDataFormat[0] = ".csv";
//
//        }
//
//
//        ArrayList<String> TestDataSheets = (ArrayList<String>) List.of(TestDataSheet.split(";"));
//        HashMap<String, String> TestData = new HashMap<>();
//        for (String TD : TestDataSheets) {
//            String TestDataPath = TDPath + TD + DefaultTestDataFormat;
//
//            ArrayList<String> whereClause_TestData = new ArrayList<>();
//            whereClause_TestData.add("Iteration::" + currentIteration);
//            Map<String, ArrayList<String>> result = PoiReadExcel.fetchWithCondition(TestDataPath, Environment, whereClause_TestData);
//
//
//            if (result.isEmpty()) {
//
//                logger.error(String.valueOf("Blank column in Test Data - There is no data in the column  for the Iteration ") + currentIteration + String.valueOf(" of Environment ") + Environment);
//            }
//
//
//            for (String key : result.keySet()) {
//                if (result.get(key).get(0).toLowerCase().trim().startsWith("random_")) {
//                    result.get(key).set(0, getRandom(result.get(key).get(0)));
//                    Thread.sleep(500);
//                    logger.info("Random Value Added in {} = {}", key, result.get(key).get(0));
//
//                }
//
//                if (!TestData.containsKey(key)) {
//                    TestData.put(key, result.get(key).get(0));
//                } else {
//                    TestData.replace(key, result.get(key).get(0));
//                }
//            }
//
//
//        }
//        return TestData;
//
//
//    }
//
//    public String createResultsFolder(String homePath) {
//
//        File dir = new File(homePath + "/Results");
//        if (!dir.exists()) dir.mkdirs();
//
//        String date = getCurrentDate();
//
//        String ResultsFolder_Date = homePath + "/Results/" + date;
//
//        dir = new File(ResultsFolder_Date);
//        if (!dir.exists()) dir.mkdirs();
//
//        String Time = "Run_" + getCurrentTime();
//
//        String ResultsFolder_Time = ResultsFolder_Date + "/" + Time;
//
//
//        dir = new File(ResultsFolder_Time);
//        if (!dir.exists()) dir.mkdirs();
//
//
//        return ResultsFolder_Time;
//
//
//    }
//
//
//    public Map<String, ArrayList<String>> getScreenTCData(String ScreenName, String TestCaseName, String TestDataLocation, String TestDataMappingFileName, String TestDataMappingSheetName, String iteration) {
//
//        ArrayList<String> whereClause_TestData = new ArrayList<>();
//        whereClause_TestData.add("ScreenName::" + ScreenName);
//        Map<String, ArrayList<String>> result = PoiReadExcel.fetchWithCondition(TestDataMappingFileName, TestDataMappingSheetName, whereClause_TestData);
//
//        ArrayList<String> whereClause_TestData2 = new ArrayList<>();
//        whereClause_TestData2.add("TestCase::" + TestCaseName);
//        whereClause_TestData2.add("Iteration::" + iteration);
//        return PoiReadExcel.fetchWithCondition(TestDataLocation + "\\" + result.get("TestDataFileName").get(0), result.get("TestDataSheetName").get(0), whereClause_TestData2);
//
//
//    }
//
//
//    public Properties loadProperties(String Filepath) {
//
//        Properties prop = new Properties();
//
//        try (FileInputStream propsInput = new FileInputStream(Filepath)) {
//            prop.load(propsInput);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return prop;
//
//    }
//

    public Properties loadProperties(String filepath) {

        Properties prop = new Properties();

        try (FileInputStream propsInput = new FileInputStream(filepath)) {
            prop.load(propsInput);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prop;

    }

    public Map<String, ArrayList<String>> getScreenTCData(String screenName, String testCaseName, String testDataLocation, String testDataMappingFileName, String testDataMappingSheetName, String iteration) {

        ArrayList<String> whereClauseTestData = new ArrayList<>();
        whereClauseTestData.add("ScreenName::" + screenName);
        Map<String, ArrayList<String>> result = PoiReadExcel.fetchWithCondition(testDataMappingFileName, testDataMappingSheetName, whereClauseTestData);

        ArrayList<String> whereClauseTestData2 = new ArrayList<>();
        whereClauseTestData2.add("TestCase::" + testCaseName);
        whereClauseTestData2.add("Iteration::" + iteration);
        return PoiReadExcel.fetchWithCondition(testDataLocation + "\\" + result.get("TestDataFileName").get(0), result.get("TestDataSheetName").get(0), whereClauseTestData2);


    }


    }