package pdfvalidation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.regex.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import report_utilities.Model.TestCaseParam;
import report_utilities.Model.ExtentModel.PageDetails;
import report_utilities.common.ReportCommon;

import static testsettings.TestRunSettings.driver;

public class ReplacePlaceholders
{
	private static final Logger logger = LoggerFactory.getLogger(ReplacePlaceholders.class.getName());
	ReportCommon exceptionDetails = new ReportCommon();

	// Method to load replacement data from a text file
	private Map<String, String> loadReplacementData(String dataFilePath) throws IOException {
		Map<String, String> replacementData = new HashMap<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath, StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// Assume data file contains lines in the format "placeholder=value"
				String[] parts = line.split("=", 2);
				if (parts.length == 2) {
					replacementData.put(parts[0].trim(), parts[1].trim());
				}
			}
		}

		return replacementData;
	}

	// this is just a temporary method, created for demo, will be removed once we have templates in place
	public void addPlaceholdersToTextFiles(String sourceTextFileLocation, String intermediateTextFileLocation) throws IOException {

		File sourceFolder = new File(sourceTextFileLocation);
		File[] files = sourceFolder.listFiles((dir, name) -> name.endsWith(".txt"));

		if (files != null) {
			getFiles(sourceTextFileLocation, intermediateTextFileLocation, files);
		} else {
			logger.info("No text files found in the source directory: {}", sourceTextFileLocation);
		}
	}

	private void getFiles(String sourceTextFileLocation, String intermediateTextFileLocation, File[] files)
			throws IOException {
		for (File file : files) {
			// Read content from the source files
			List<String> lines = Files.readAllLines(file.toPath());
			StringBuilder updatedContent = new StringBuilder();

			// Iterate through each line and add placeholders as needed
			for (String line : lines) {
				// Check and replace specific unwanted content with placeholders
				if (line.matches(".*\\bA Vinith\\b.*")) {
					line = line.replaceAll("\\bA Vinith\\b", "{{CHILD_NAME}}");
				} else if (line.matches(".*\\b1/11/2024\\b.*")) {
					line = line.replaceAll("\\b1/11/2024\\b", "{{PLACEMENT_DATE}}");
				} else if (line.matches(".*\\b4/30/2015\\b.*")) {
					line = line.replaceAll("\\b4/30/2015\\b", "{{DATE_OF_BIRTH}}");
				}

				// Append the updated line to the content
				updatedContent.append(line).append(System.lineSeparator());
			}

			// Define the path for the intermediate text file
			File intermediateFolder = new File(intermediateTextFileLocation);
			if (!intermediateFolder.exists()) {
				intermediateFolder.mkdirs();
			}

			// Write the updated content to the intermediate file location
			Files.write(Paths.get(intermediateTextFileLocation, file.getName()), updatedContent.toString().getBytes());
			
			logger.info("Added Place Holders --> temp step: {}", sourceTextFileLocation);
		}
	}

	public void replaceData(TestCaseParam testCaseParam, PageDetails pageDetails,String sourceTextFilePath, String replacedTextFilePath, String replacementDataFilePath) throws Exception {
		LocalDateTime startTime= LocalDateTime.now();
		Map<String, String> replacementData = loadReplacementData(replacementDataFilePath);

		List<String> files = listOfAllFile(sourceTextFilePath);
		int filesCount = files.size();

		for (int i = 0; i < filesCount; i++) {
			StringBuilder stringBuilder = new StringBuilder();

			// Reading the content of each file
			try (FileReader fileReader = new FileReader(sourceTextFilePath + "\\" + files.get(i), StandardCharsets.UTF_8);
				 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
				int character;
				while ((character = bufferedReader.read()) != -1) {
					stringBuilder.append((char) character);
				}

			} catch (IOException e) {

				continue; // Skip to the next file if an error occurs
			}

			String fileContent = stringBuilder.toString();

			// Replace placeholders with actual values
			for (Map.Entry<String, String> entry : replacementData.entrySet()) {
				// Check if entry key is within curly braces and handle replacement accordingly
				if (fileContent.contains(entry.getKey())) {
					fileContent = fileContent.replaceAll("\\{\\{" + entry.getKey() + "\\}\\}", entry.getValue());
				} else {
					fileContent = fileContent.replaceAll(entry.getKey(), entry.getValue());
				}
			}

			// Create output directory if it doesn't exist
			File directory = new File(replacedTextFilePath);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			int count = i + 1;
			// Writing the updated content to the output file
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(replacedTextFilePath + "\\" + "ReplacedTextFile" + count + ".txt"), StandardCharsets.UTF_8))) {
				writer.write(fileContent);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("{} Error reading file:", e.getMessage());
				exceptionDetails.logExceptionDetails(driver, testCaseParam, pageDetails.PageActionName, pageDetails.PageActionDescription, startTime, e);
			}
			logger.info("Texts replaced successfully.");
		}
	}

	public static List<String> getFileNamesInAlphanumericOrder(String directoryPath) {
		File directory = new File(directoryPath);

		List<String> fileNames = new ArrayList<>();

		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();

			if (files != null) {
				Arrays.sort(files, new AlphanumericFileComparator());
				for (File file : files) {
					if (file.isFile()) {
						fileNames.add(file.getName());
					}
				}
			}
		}

		return fileNames;
	}

	static class AlphanumericFileComparator implements Comparator<File> {
		public int compare(File a, File b) {
			return a.getName().compareTo(b.getName());
		}
	}

	public static void main(String[] args) {
		String directoryPath = "your_directory_path_here";
		List<String> fileNames = getFileNamesInAlphanumericOrder(directoryPath);

		if (fileNames.isEmpty()) {
			logger.info("No files found in the directory.");
		} else {
			logger.info("File names in alphanumeric order:");
			for (String fileName : fileNames) {
				logger.info("File Name: {}", fileName);
			}
		}
	}


	// Method to list all files in a directory
	public List<String> listOfAllFile(String path) {
		File directoryPath = new File(path);
		if (!directoryPath.exists() || !directoryPath.isDirectory()) {
			logger.info("Directory does not exist or is not a directory: {}", directoryPath);
			return new ArrayList<>(); // Or handle this scenario appropriately
		}
		String[] contents = directoryPath.list();
		if (contents == null) {
			logger.info("No files found in directory: {}", directoryPath);
			return new ArrayList<>();
		}

		ArrayList<String> fileNames = new ArrayList<>();
		logger.info("List of files and directories in the specified directory:");
		for (String content : contents) {
			fileNames.add(content);
			fileNames.addAll(Arrays.asList(contents));
		}

		Collections.sort(fileNames, new NaturalOrderComparator());
		return fileNames;
	}


	public List<String> listOfFile(String path) {
		File directoryPath = new File(path);
		if (!directoryPath.exists() || !directoryPath.isDirectory()) {
			logger.info("Directory does not exist or is not a directory: {}", directoryPath);
			return new ArrayList<>(); // Or handle this scenario appropriately
		}
		//List of all files and directories
		String[] contents = directoryPath.list();
		if (contents == null) {
			logger.info("No files found in directory: {}", directoryPath);
			return new ArrayList<>();
		}

		ArrayList<String> fileNames = new ArrayList<>();
		logger.info("List of files and directories in the specified directory:");
		for(int i=0; i<contents.length; i++)
		{
			fileNames.addAll(Arrays.asList(contents[i]));
		}


		Collections.sort(fileNames, new NaturalOrderComparator());


		return fileNames;
	}

	static class NaturalOrderComparator implements Comparator<String> {
		private static final Pattern PATTERN = Pattern.compile("(\\D*)(\\d+)(.*)");

		public int compare(String a, String b) {
			Matcher matcherA = PATTERN.matcher(a);
			Matcher matcherB = PATTERN.matcher(b);

			if (matcherA.matches() && matcherB.matches()) {
				String prefixA = matcherA.group(1);
				String prefixB = matcherB.group(1);

				int result = prefixA.compareTo(prefixB);

				if (result == 0) {
					int numA = Integer.parseInt(matcherA.group(2));
					int numB = Integer.parseInt(matcherB.group(2));
					result = Integer.compare(numA, numB);
				}

				return result;
			}

			// Fallback to default string comparison if parsing fails
			return a.compareTo(b);
		}
	}
	
	public static void deletedirectory(String path) {
	    Path directory = Paths.get(path);
	    try {
	        try (Stream<Path> pathStream = Files.walk(directory)) {
	            pathStream
	                 .sorted((p1, p2) -> p2.compareTo(p1))
	                 .map(Path::toFile)
	                 .forEach(File::delete);
	        }
	        logger.info("Successfully deleted directory: {}", path);
	    } catch (IOException e) {
	        logger.error("Error deleting directory: {}", path, e);
	    }
	}
	
}
