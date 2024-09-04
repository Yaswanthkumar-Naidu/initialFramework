package report_utilities.ExcelReport;


import java.util.ArrayList;
import java.util.HashMap;

public class ExcelModel
{
	public String sheetName = "";
	public ArrayList<String> headerData = new ArrayList<>();
	public HashMap<Integer, ArrayList<String>> tableData = new HashMap<>();
}