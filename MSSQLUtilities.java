package api_utilities.api_helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.*;

import api_utilities.TestSettings.APISessionData;


public class MSSQLUtilities {
	private static final Logger logger =LoggerFactory.getLogger(MSSQLUtilities.class.getName()); 	
	
	public static String readDBFrameworkDate() {
	    String date = "";
	    String query = "SELECT GetDate();";

	    try (Connection con = DriverManager.getConnection(APISessionData.envModel.ConnectionString);
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {

	        ArrayList<String> queryData = new ArrayList<>();
	        while (rs.next()) {
	            queryData.add(rs.getString(1));
	        }

	        if (!queryData.isEmpty() && queryData.get(0) != null) {
	            date = queryData.get(0);
	        }
	    } catch (SQLException e) {
	        logger.error("Error while reading DB framework date", e);
	    }

	    return date;
	}


	
	public String readDataDBValidation(String connectionString, String query) {
	    String data = "";

	    try (Connection con = DriverManager.getConnection(connectionString);
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {

	        ArrayList<String> queryData = new ArrayList<>();
	        while (rs.next()) {
	            queryData.add(rs.getString(1));
	        }

	        if (!queryData.isEmpty()) {
	            data = queryData.get(0);
	        }
	    } catch (SQLException e) {
	        logger.error("Error while reading data from database", e);
	    }

	    return data;
	}


	
	
	public void insertQueries(String connectionString, String tableName, List<String> headers, List<ArrayList<String>> queries) {
	    String columnNames = "(";

	    try (Connection con = DriverManager.getConnection(connectionString)) {
	        if (con != null) {
	            logger.info("Connection Successful!");
	        }
	        int j = 1;
	        for (String columnName : headers) {
	            if (j == 1) {
	                columnNames = columnNames + columnName;
	            } else {
	                columnNames = columnNames + "," + columnName;
	            }
	            j++;
	        }
	        columnNames = columnNames + ")";

	        for (ArrayList<String> rowData : queries) {
	            String query = "";
	            for (int i = 0; i < rowData.size(); i++) {
	                if (i == (rowData.size() - 1)) {
	                    query = query + "'" + rowData.get(i) + "');";
	                } else {
	                    query = query + "'" + rowData.get(i) + "',";
	                }
	            }
	            Thread.sleep(10);
	            query = "Insert into " + tableName + columnNames + "values(" + query;
	            try (Statement stmt = con.createStatement()) {
	                query = query.replace("'getDate()'", "getDate()");
	                logger.info(query);
	                stmt.execute(query);
	                logger.info("Report Inserted into DB");
	            }
	        }
	    } catch (Exception e) {
	        logger.error("Error while inserting queries", e);
	    }
	}
}

