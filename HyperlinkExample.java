package report_utilities.ExcelReport;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Demonstrates how to create hyperlinks.
 */
public class HyperlinkExample {


    public static void main(String[]args) throws IOException {
 
       try( Workbook wb = new XSSFWorkbook())
       { 
        CreationHelper createHelper = wb.getCreationHelper();

        //cell style for hyperlinks
        //by default hyperlinks are blue and underlined
        CellStyle hLinkStyle = wb.createCellStyle();
        Font hLinkFfont = wb.createFont();
        hLinkFfont.setUnderline(Font.U_SINGLE);
        hLinkFfont.setColor(IndexedColors.BLUE.getIndex());
        hLinkStyle.setFont(hLinkFfont);

        Cell cell;
        Sheet sheet = wb.createSheet("Hyperlinks");
        //URL
        cell = sheet.createRow(0).createCell(0);
        cell.setCellValue("URL Link");

        Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
        link.setAddress("http://poi.apache.org/");
        cell.setHyperlink(link);
        cell.setCellStyle(hLinkStyle);

        //link to a file in the current directory
        cell = sheet.createRow(1).createCell(0);
        cell.setCellValue("File Link");
        link = createHelper.createHyperlink(HyperlinkType.FILE);
        link.setAddress("C:/Test/TestReport1.jpeg");
        cell.setHyperlink(link);
        cell.setCellStyle(hLinkStyle);

        //e-mail link
        cell = sheet.createRow(2).createCell(0);
        cell.setCellValue("Email Link");
        link = createHelper.createHyperlink(HyperlinkType.EMAIL);
        //note, if subject contains white spaces, make sure they are url-encoded
        link.setAddress("mailto:poi@apache.org?subject=Hyperlinks");
        cell.setHyperlink(link);
        cell.setCellStyle(hLinkStyle);

        //link to a place in this workbook

        //create a target sheet and cell
        Sheet sheet2 = wb.createSheet("Target Sheet");
        sheet2.createRow(0).createCell(0).setCellValue("Target Cell");

        cell = sheet.createRow(3).createCell(0);
        cell.setCellValue("Worksheet Link");
        Hyperlink link2 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
        link2.setAddress("'Target Sheet'!A1");
        cell.setHyperlink(link2);
        cell.setCellStyle(hLinkStyle);

        FileOutputStream out = new FileOutputStream("c:\\Test\\hyperinks.xlsx");
        wb.write(out);
        out.close();
        
        wb.close();
    	}
    	catch(Exception e) 
    	{
    		e.printStackTrace();
    		throw e;
    	}
    }
}