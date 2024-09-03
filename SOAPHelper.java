package api_utilities.api_helpers;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api_utilities.Models.APIModel;
import api_utilities.TestSettings.APITestSettings;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class SOAPHelper 
{
	
	final Logger logger = LoggerFactory.getLogger(SOAPHelper.class.getName());

	public Response executeSOAPAPI(APIModel iModel , String iteration)throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException
	{
		Response responsePost ;

		Map<String, String>  defaultHeaders = iModel.GetHeaderData();
		String jsonDataInFile="";
		 jsonDataInFile=iModel.gettRequestData();
		String certificatePath=iModel.getCertificatePath();
		String certificatePassword=iModel.getCertificatePassword();		
		KeyStore keyStore = null;
		SSLConfig config = null;

		try {
		        keyStore = KeyStore.getInstance("PKCS12");
		        keyStore.load(
		                new FileInputStream(certificatePath),
		                certificatePassword.toCharArray());

		    } catch (Exception ex) {
		        logger.error("Error while loading keystore >>>>>>>>>");
		        ex.printStackTrace();
		    }																					  
		if (iModel.isMockRequired())
		{
			try (FileWriter file = new FileWriter(iModel.GetMockData().MockLocation)) {
			    file.write(iModel.GetMockData().MockFileContent);
			    file.flush();
			} 
			catch (Exception e)
			{
				logger.error("Unable to create the mock File===> {}",iModel.GetMockData().MockFileName);
				logger.error(e.getMessage());
				logger.error(Arrays.toString(e.getStackTrace()));
				
			
			}
		}

		if(defaultHeaders.isEmpty())
		{
			if(iModel.isCertificateRequired())
			{
				if (keyStore != null) {

			        org.apache.http.conn.ssl.SSLSocketFactory clientAuthFactory = new org.apache.http.conn.ssl.SSLSocketFactory(keyStore, certificatePassword);

			        // set the config in rest assured
			        config = new SSLConfig().with().sslSocketFactory(clientAuthFactory).and().allowAllHostnames();
			        RestAssured.config = RestAssured.config().sslConfig(config);
				}	
			responsePost =		
			RestAssured.given()
            .urlEncodingEnabled(false)
            .relaxedHTTPSValidation()
            .body(jsonDataInFile)
            .config(RestAssured.config())
            .contentType(ContentType.XML)
            .expect()
            .when()
			.post(iModel.getServiceUrl());
			}
			else
			{
				responsePost =		
						RestAssured.given()
			            .urlEncodingEnabled(false)
			            .relaxedHTTPSValidation()
			            .body(jsonDataInFile)
			            .contentType(ContentType.XML)
			            .expect()
			            .when()
						.post(iModel.getServiceUrl());	
			}
		}
			
		else
		{
			if(iModel.isCertificateRequired())
			{
				RestAssured.config = RestAssured.config().sslConfig(
			            new SSLConfig().trustStore(APITestSettings.TrustStoreLocation,APITestSettings.TrustStorePassword)
			                    .keyStore(certificatePath, certificatePassword));

				if (keyStore != null) {

			        org.apache.http.conn.ssl.SSLSocketFactory clientAuthFactory = new org.apache.http.conn.ssl.SSLSocketFactory(keyStore, certificatePassword);

			        // set the config in rest assured
			        config = new SSLConfig().with().sslSocketFactory(clientAuthFactory).and().allowAllHostnames();
			        RestAssured.config = RestAssured.config().sslConfig(config);
				}	

			responsePost =
					RestAssured.given()
		            .urlEncodingEnabled(false)
		            .relaxedHTTPSValidation()
		            .body(jsonDataInFile)
		            .config(RestAssured.config())
		            .contentType(ContentType.XML)
							.headers(defaultHeaders)
				            .expect()
				            .when()
							.post(iModel.getServiceUrl());
			}
			else
			{
				responsePost =
						RestAssured.given()
			            .urlEncodingEnabled(false)
			            .relaxedHTTPSValidation()
			            .body(jsonDataInFile)
			            .config(RestAssured.config())
			            .contentType(ContentType.XML)
								.headers(defaultHeaders)
					            .expect()
					            .when()
								.post(iModel.getServiceUrl());
				
			}
		}
		logger.info("-----------------------------------------------------------------------------------");
		logger.info("-------" + iModel.getModule() + "==>" + iModel.getInterfaceName() + "--------------");
		logger.info("-----------------------------------------------------------------------------------");
		logger.info("-----------------------------------------------------------");
		logger.info("--------------------------Request--------------------------");
		logger.info("-----------------------------------------------------------");
		logger.info(jsonDataInFile);
		logger.info("-----------------------------------------------------------");
		logger.info("--------------------------Response--------------------------");
		logger.info("-----------------------------------------------------------");
		logger.info(responsePost.asString());

		
		logger.info("-----------------------------------------------------------------------------------");
		logger.info("-------" + iModel.getModule() + "==>" + iModel.getInterfaceName() + "--------------");
		logger.info("-----------------------------------------------------------------------------------");
		logger.info("-----------------------------------------------------------");
		logger.info("--------------------------Request--------------------------");
		logger.info("-----------------------------------------------------------");
		logger.info(jsonDataInFile);
		logger.info("-----------------------------------------------------------");
		logger.info("--------------------------Response--------------------------");
		logger.info("-----------------------------------------------------------");
		logger.info(responsePost.asString());

		return responsePost;
		
	}	 
}