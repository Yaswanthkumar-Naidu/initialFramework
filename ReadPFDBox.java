package pdfvalidation;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.fit.pdfdom.PDFDomTree;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common_utilities.Utilities.Util;
import report_utilities.Model.TestCaseParam;
import report_utilities.Model.ExtentModel.ExtentUtilities;
import report_utilities.common.ReportCommon;
import uitests.testng.common.ExcelUtility;

public class ReadPFDBox {
	private static final Logger logger =LoggerFactory.getLogger(ReadPFDBox.class.getName());
	
	ExtentUtilities extentUtilities = new ExtentUtilities();
	private WebDriver driver;
	ReportCommon exceptionDetails = new ReportCommon();
	ReportCommon TestStepDetails = new ReportCommon();
	private static final String Status_Pass="PASS";
	private static final String Status_Fail="FAIL";
	private static final String Status_Done="DONE";
	Util util = new Util();
	
	ExcelUtility excelutil=new ExcelUtility();
	HashMap<String, ArrayList<String>> TestCaseData_DBQScreen = new HashMap<>();
	public ReadPFDBox(){ }
	public ReadPFDBox(WebDriver _driver,TestCaseParam testCaseParam) throws Exception { InitializePage(_driver,testCaseParam);
	}
	
	public void InitializePage(WebDriver _driver,TestCaseParam testCaseParam) throws Exception
	{
		driver = _driver;
		PageFactory.initElements(driver, this);
		ReportCommon TestStepLogDetails = new ReportCommon(); 
	}

	public void validatepdfvspdfCompareLine(TestCaseParam testCaseParam, String PdfSourceFile, String PdfTargetFile, String ResultFile, String ReplacedTextFile, String TargetTextFile, int count1) throws Exception 
	{
		try {
			LocalDateTime StartTime= LocalDateTime.now();
			
			File file1 = new File(PdfSourceFile);
			PDDocument document = PDDocument.load(file1);
			File file2 = new File(PdfTargetFile);
			PDDocument document2 = PDDocument.load(file2);
			int totalpageofDocument1 = getPageCount(document);
			int totalpageofDocument2 = getPageCount(document2);
			String Action;
			String ActionDescription;
			File directory;
			if(totalpageofDocument1==totalpageofDocument2) { 
				for(int i=1;i<(totalpageofDocument1-count1)+1;i++) {
					Action = "Verify PDF Page"+i;
					ActionDescription = "Verify PDF Page"+i;
					String Action1 = "Verified Page"+i+":: Pass";
					String ActionDescription1 = "Verified Page"+i+":: Pass";
					String testcontant2 = readPdfContent(document2,i,totalpageofDocument2-(totalpageofDocument2-i));
					
					directory = new File(TargetTextFile);
				    if (! directory.exists()){
				        directory.mkdirs();
				    }
					
				    try(BufferedWriter out2 = new BufferedWriter(new FileWriter(TargetTextFile+"\\TargetTextFile"+i+".txt",StandardCharsets.UTF_8)))
				    {
				    	out2.write("");
						out2.write(testcontant2);
						out2.close();
				    }
					
					StringBuilder stringBuilder = new StringBuilder();

					
					try(BufferedReader br=new BufferedReader(new FileReader(ReplacedTextFile+"\\"+"ReplacedTextFile"+i+".txt",StandardCharsets.UTF_8)))
					{
						String replacedtxtlines=br.readLine();
						while(replacedtxtlines!=null) 
						{
							stringBuilder.append(replacedtxtlines);
							replacedtxtlines=br.readLine();
							
						}
					}
					
					
					
					Thread.sleep(6000);
					String testcontant1 = stringBuilder.toString().trim().replace("\\s", "").replace("\\n", "").replace("\\t", "");

					
					testcontant2=testcontant2.trim().replaceAll("\\s", "").replace("\\n", "").replace("\\t", "");
					if(!testcontant1.equalsIgnoreCase(testcontant2)){
						String TC1 = testcontant2.replaceAll("\\s", "").replace("\\n", "").replace("\\t", "");
	
						String TargettxtFile=TargetTextFile+"\\TargetTextFile"+i+".txt";
						
						
						String replacedtxtFile=ReplacedTextFile+"\\ReplacedTextFile"+i+".txt";
						List<String> linenumber = textdiffutil(replacedtxtFile,TargettxtFile);
						directory = new File(ResultFile);
					    if (! directory.exists())
					    {
					        directory.mkdirs();
					    }
					    try(BufferedWriter out = new BufferedWriter(new FileWriter(ResultFile+"\\ResultFile"+i+".txt",StandardCharsets.UTF_8)))
					    {
					    	out.write("");
							out.write("Difference in lines of SourcefilePage"+i+"::\r\n"+linenumber);
							TestStepDetails.logPDFDetails(driver, testCaseParam, Action+"\r\n"+linenumber, ActionDescription+"\r\n"+linenumber, StartTime, Status_Fail,"Difference in lines of SourcefilePage"+i+"::\r\n"+linenumber);
							out.close();
					    }
						
						System.out.println("Source and Target Page"+i+":: Didn't match");
					}else {
						System.out.println("Page"+i+"are equal");
						TestStepDetails.logPDFDetailsPass(driver, testCaseParam, Action1, ActionDescription1, StartTime, Status_Pass);
					}
				}
			}else {
				System.out.println("Page count for Source and Target PDF mismatched");
			}
		}
		catch (Exception e)
		{Thread.sleep(0);
			throw e;
		}
	}
	
	public static List<String> textdiffutil(String path1, String path2) throws IOException {
	    List<String> lines1;
	    List<String> lines2;
	    List<String> delta1 = new ArrayList<>();

	    try (FileReader fileReader1 = new FileReader(path1);
	         FileReader fileReader2 = new FileReader(path2)) {
	        lines1 = readLines(fileReader1);
	        lines2 = readLines(fileReader2);
	    }
	    return delta1;
	}


	public static List<String> readLines(final Reader reader) throws IOException {
		final BufferedReader bufReader = toBufferedReader(reader);
		final List<String> list = new ArrayList<>();
		String line;
		while ((line = bufReader.readLine()) != null) {
			
			list.add(line.trim().replaceAll("\\s", "").replaceAll("\\n", "").replaceAll("\\t", ""));
		}
		return list;
	}
	public static BufferedReader toBufferedReader(final Reader reader) {
		return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
	}
	public static String readPdfContent(PDDocument document,int firstpage, int lastpage) throws IOException {
		
		PDFTextStripper pdfStripper = new PDFTextStripper();
		pdfStripper.setStartPage(firstpage);
		pdfStripper.setEndPage(lastpage);
		String text = pdfStripper.getText(document);
		return text.trim().replaceAll("\\s", "").replace("\\n", "").replace("\\t", "");
	}
	public static int getPageCount(PDDocument doc) {
		
		int pageCount = doc.getNumberOfPages();
		return pageCount;
	}
	
	public static boolean compareImage() { 
		try {
			File fileA = new File("C:/Users/sahlalwani/Documents/GeeksforGeeks.png");
			File fileB = new File("C:/Users/sahlalwani/Documents/GeeksforGeeks1.png");
			
			BufferedImage biA = ImageIO.read(fileA);
			DataBuffer dbA = biA.getData().getDataBuffer();
			int sizeA = dbA.getSize(); 
			BufferedImage biB = ImageIO.read(fileB);
			DataBuffer dbB = biB.getData().getDataBuffer();
			int sizeB = dbB.getSize();
			
			if(sizeA == sizeB) {
				for(int i=0; i<sizeA; i++) { 
					if(dbA.getElem(i) != dbB.getElem(i)) {
						return false;
					}
				}
				return true;
			}
			else {
				return false;
			}
		} 
		catch (Exception e) { 
			System.out.println("Failed to compare image files ...");
			return false;
		}
	}
	public static float compareImagePer() {
		File fileA = new File("C:/Users/sahlalwani/Documents/GeeksforGeeks.png");
		File fileB = new File("C:/Users/sahlalwani/Documents/GeeksforGeeks1.png");
		float percentage = 0;
		try {
			
			BufferedImage biA = ImageIO.read(fileA);
			DataBuffer dbA = biA.getData().getDataBuffer();
			int sizeA = dbA.getSize();
			BufferedImage biB = ImageIO.read(fileB);
			DataBuffer dbB = biB.getData().getDataBuffer();
			int sizeB = dbB.getSize();
			int count = 0;
			
			if (sizeA == sizeB) {
				for (int i = 0; i < sizeA; i++) {
					if (dbA.getElem(i) == dbB.getElem(i)) {
						count = count + 1;
					}
				}
				percentage = (count * 100) / sizeA;
			} else {
				System.out.println("Both the images are not of same size");
			}
		} catch (Exception e) {
			System.out.println("Failed to compare image files ...");
		}
		return percentage;
	}

	public static void PixltoPixlComapre()
	{
	
		BufferedImage imgA = null;
		BufferedImage imgB = null;
		
		try {
			
			File fileA = new File("C:/Users/sahlalwani/Documents/GeeksforGeeks.png");
			File fileB = new File("C:/Users/sahlalwani/Documents/GeeksforGeeks1.png");
			
			imgA = ImageIO.read(fileA);
			imgB = ImageIO.read(fileB);
		}
		
		catch (IOException e) {
			
			System.out.println(e);
		}
		
		int width1 = imgA.getWidth();
		int width2 = imgB.getWidth();
		int height1 = imgA.getHeight();
		int height2 = imgB.getHeight();
		
		if ((width1 != width2) || (height1 != height2))
			
			System.out.println("Error: Images dimensions"
					+ " mismatch");
		else {
			
			long difference = 0;
			
			for (int y = 0; y < height1; y++) {
				
				for (int x = 0; x < width1; x++) {
					int rgbA = imgA.getRGB(x, y);
					int rgbB = imgB.getRGB(x, y);
					int redA = (rgbA >> 16) & 0xff;
					int greenA = (rgbA >> 8) & 0xff;
					int blueA = (rgbA)&0xff;
					int redB = (rgbB >> 16) & 0xff;
					int greenB = (rgbB >> 8) & 0xff;
					int blueB = (rgbB)&0xff;
					difference += Math.abs(redA - redB);
					difference += Math.abs(greenA - greenB);
					difference += Math.abs(blueA - blueB);
				}
			}
			
			double total_pixels = width1 * height1 * 3;
			
			double avg_different_pixels
			= difference / total_pixels;
			
			double percentage
			= (avg_different_pixels / 255) * 100;
		}
	}

	public static void PDFtoHTML(String sourcepdf, String outputfile) throws IOException {
		
		PDDocument document = PDDocument.load(new File(sourcepdf));
		try {
			PDFDomTree parser = new PDFDomTree();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Writer output = new PrintWriter(outputfile, "utf-8");
		output.close();
	}
	
	public static String Targetfiletxt(String Targetfile, String PlaceHolder, String TestData, int Noofplhld) {
		String outputfilename = "C://Users//sahlalwani//Documents//STOMACH-pdf-replacedtest12341.pdf";
		return outputfilename;
	}
}

