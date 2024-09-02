package reusable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import testsettings.TestRunSettings;

public class RenameDownloadedFile 
{
	private RenameDownloadedFile() {
        
    }
	
	private static final Logger logger =LoggerFactory.getLogger(RenameDownloadedFile.class.getName());
	
	public static String getCurrentDate()
    {
        try
        {
        	LocalDateTime today = LocalDateTime.now();

            String date = today.toLocalDate().toString();
            date = date.replace(":", "_");
            date = date.replace(" ", "_");
            date = date.replace(".", "_");
            date = date.replace("-", "_");
            return date;
        }
        catch (Exception e)
        {
            logger.error("{} current date not found",e.getMessage());
        }
		return null;
    }


    public static String getCurrentTime()
    {
        try
        {
        	LocalDateTime today = LocalDateTime.now();

            String result = today.toLocalTime().toString();

            result = result.replace(":", "_");
            result = result.replace(" ", "_");
            result = result.replace(".", "_");
            return result;

        }
        catch (Exception e)
        {
        	 logger.error("{} current time not found",e.getMessage());
        }
		return null;

    }

    public static String createDirectory(String directoryPath, String directoryName)
    {
        try
        {
        	String dele = "/"; 
        	String directoryFulPath = directoryPath + dele + directoryName;
        	File dir = new File(directoryFulPath);
            if (!dir.exists()) dir.mkdirs();
            
            return directoryFulPath;

        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	throw e;

        }
    }
    
    class FileRenameException extends Exception {
        public FileRenameException(String message) {
            super(message);
        }
    }

    public static boolean renameAndPlaceFile(String downloadedFile, String renameTo, String folder) throws IOException, InterruptedException {
       
            String home = System.getProperty("user.home");
            String filePath = TestRunSettings.artifactsPath + "//PDF//TargetPDFfile//" + folder;
            Path directory = Paths.get(filePath);

            if (Files.exists(directory)) {
                try (Stream<Path> pathStream = Files.walk(directory)) {
                    pathStream
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                }
            }

            Files.createDirectories(directory);
            Thread.sleep(10);

            String renamedFileName = renameTo + ".PDF";
            String dele = "/";
            Path newFilePath = Paths.get(filePath, dele, renamedFileName);

            Path originalFile = Paths.get(home, "Downloads", downloadedFile);
          
            try {
                Files.move(originalFile, newFilePath, StandardCopyOption.REPLACE_EXISTING);
                logger.error("File Successfully Renamed and saved to destination folder");
                return true;
            } catch (IOException e) {
            	  logger.error("{} Error",e.getMessage());
            }
			return false;
    }
    
    public static boolean renameAndPlaceFile(String downloadedFile, String renameTo)
    {
    	try {
    		String home = System.getProperty("user.home");
			String filePath = TestRunSettings.artifactsPath+"//PDF//TargetPDFfile";
			String renamedFileName=renameTo+".PDF";
			String dele = "/";
			filePath = filePath + dele +renamedFileName;
			File originalFile = new File(home + "/Downloads/" + downloadedFile);			
	    	File newFile = new File(filePath);
	        boolean flag = originalFile.renameTo(newFile);	 
	        if (flag) {
	        	logger.error("{} found", flag);
	        }
	        else {
	        	logger.error("{}", flag);
	        }
	        
	    
		return originalFile.renameTo(newFile);
    	}
    	catch(Exception e) 
    	{
    		logger.error("{}", e.getMessage());
    	}
		return false;
    }
}
