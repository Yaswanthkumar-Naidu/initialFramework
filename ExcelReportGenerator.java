package api_utilities.Reports;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import api_utilities.Models.APIReportModel;
import api_utilities.TestSettings.APITestSettings;

public class ExcelReportGenerator {

	private static final Logger logger =LoggerFactory.getLogger(ExcelReportGenerator.class.getName());
	static Workbook workbook = new XSSFWorkbook();
	static Sheet sheet = workbook.createSheet("Report");
	static int rowNum = 1;

	public void startExcelReport() {
		Row row = sheet.createRow(0);

		sheet.setColumnWidth(0, 8000);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 8000);
		sheet.setColumnWidth(3, 8000);
		sheet.setColumnWidth(4, 8000);
		CellStyle headerCellStyle = createHeaderCellStyle(workbook);

		Cell cellInterfaceName = row.createCell(0);
		cellInterfaceName.setCellStyle(headerCellStyle);
		cellInterfaceName.setCellValue("InterfaceName");
		
		Cell cellHeadURL = row.createCell(1);
		cellHeadURL.setCellStyle(headerCellStyle);
		cellHeadURL.setCellValue("URL");

		Cell cellHeadRequest = row.createCell(2);
		cellHeadRequest.setCellStyle(headerCellStyle);
		cellHeadRequest.setCellValue("Request");

		Cell cellHeadResponse = row.createCell(3);
		cellHeadResponse.setCellStyle(headerCellStyle);
		cellHeadResponse.setCellValue("Response");

		Cell cellHeadStatus = row.createCell(4);
		cellHeadStatus.setCellStyle(headerCellStyle);
		cellHeadStatus.setCellValue("Status");

	}

	public void addTestStep(List<APIReportModel> report,String interfaceName) {

		Row row = sheet.createRow(rowNum++);

		Cell cellInterface = row.createCell(0);
		cellInterface.setCellValue(interfaceName);
		
		Cell cellURL = row.createCell(1);
		cellURL.setCellValue(report.get(0).URL);

		Cell cellRequest = row.createCell(2);
		cellRequest.setCellValue(report.get(0).Request);

		Cell cellResponse = row.createCell(3);
		cellResponse.setCellValue(report.get(0).Response);

		CellStyle passCellStyle = createPassCellStyle(workbook);
		CellStyle failCellStyle = createFailCellStyle(workbook);

		for(APIReportModel apiReportModel:report)
		{

			if(!(apiReportModel.ActualResponse.equals(""))) {
				Cell cellStatus = row.createCell(4);
				if(apiReportModel.TestStepResult.equalsIgnoreCase("PASS"))
				{
					cellStatus.setCellStyle(passCellStyle);
				}
				else if(apiReportModel.TestStepResult.equalsIgnoreCase("FAIL"))
				{
					cellStatus.setCellStyle(failCellStyle);
				}

				cellStatus.setCellValue(apiReportModel.TestStepResult);
			}
		}
	}

	public void generateExcelReport() {

		
		// Save the workbook
		try {
			LocalDateTime currentDateTime = LocalDateTime.now();
			String timeStamp = currentDateTime.toString().substring(0, currentDateTime.toString().indexOf("."));
			timeStamp = timeStamp.replace("-", "_");
			timeStamp = timeStamp.replace(":", "_");
			String path =APITestSettings.ResultsPath+File.separator+"Report"+APITestSettings.ExcelFilepath;
			FileOutputStream outputStream = new FileOutputStream(path);
			workbook.write(outputStream);
			outputStream.close();
			workbook.close();
			logger.info("Excel report generated successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static CellStyle createHeaderCellStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		
		return cellStyle;
	}

	private static CellStyle createPassCellStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
	
		return cellStyle;
	}

	private static CellStyle createFailCellStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
	
		return cellStyle;
	}
}
