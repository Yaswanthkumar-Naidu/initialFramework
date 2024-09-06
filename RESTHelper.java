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

import api_utilities.models.APIModel;
import api_utilities.test_settings.APITestSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RESTHelper 
{

	final Logger logger = LoggerFactory.getLogger(RESTHelper.class.getName());

	public Response executRESTAPI(APIModel iModel,String apiType) throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException	{
		Response responsePost = null ;


		Map<String, String>  defaultHeaders = iModel.getHeaderData();
		String jsonDataInFile="";
		jsonDataInFile=iModel.gettRequestData();
		String certificatePath=iModel.getCertificatePath();
		String certificatePassword=iModel.getCertificatePassword();		

		String serviceURL=iModel.getServiceUrl();
		KeyStore keyStore = null;
		SSLConfig config = null;
		String patch = "PATCH";
		String delete = "DELETE";
		if(APITestSettings.isKeyStoreConfigurationRequired())
		{
		

		try {
			keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(
					new FileInputStream(certificatePath),
					certificatePassword.toCharArray());

		} catch (Exception ex) {
			logger.info("Error while loading keystore >>>>>>>>>");
			ex.printStackTrace();
		}
		}
		if (iModel.isMockRequired())
		{
			try
			{

				FileWriter file;

				file = new FileWriter(iModel.getMockData().getMockLocation());

				file.write(iModel.getMockData().getMockFileContent());
				file.flush();
				file.close();
			}
			catch (Exception e)
			{
				logger.error("Unable to create the mock File===> {}",  iModel.getMockData().getMockFileName());
				logger.error(e.getMessage());
				logger.error(Arrays.toString(e.getStackTrace()));

			
			}
		}




		if(defaultHeaders.isEmpty())
		{
			if(iModel.isCertificateRequired())
			{
				if(APITestSettings.isKeyStoreConfigurationRequired() && keyStore != null) {

		@SuppressWarnings("deprecation")
		org.apache.http.conn.ssl.SSLSocketFactory clientAuthFactory = new org.apache.http.conn.ssl.SSLSocketFactory(keyStore, certificatePassword);

					// set the config in rest assured
					config = new SSLConfig().with().sslSocketFactory(clientAuthFactory).and().allowAllHostnames();
					RestAssured.config = RestAssured.config().sslConfig(config);
				}
				

				if(apiType.equalsIgnoreCase("GET"))
				{
					responsePost =		
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.expect()
							.when()
							.get(serviceURL);
				}


				if(apiType.equalsIgnoreCase("POST"))
				{
					responsePost =		
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.expect()
							.when()
							.post(serviceURL);
				}
				else  if(apiType.equalsIgnoreCase("PUT"))
				{
					responsePost =		
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.expect()
							.when()
							.put(serviceURL);
				}
				else if(apiType.equalsIgnoreCase(patch))
				{
					responsePost =		
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.expect()
							.when()
							.patch(serviceURL);
				}
				else if(apiType.equalsIgnoreCase("DELETE"))
				{
					responsePost =		
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.expect()
							.when()
							.delete(serviceURL);
				}
			}
			else
			{
				if(apiType.equalsIgnoreCase("GET"))
				{
					responsePost =		
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.contentType(ContentType.JSON)
							.expect()
							.when()
							.get(serviceURL);	
				}
				if(apiType.equalsIgnoreCase("POST"))
				{
					responsePost =		
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.contentType(ContentType.JSON)
							.expect()
							.when()
							.post(serviceURL);	
				}
				if(apiType.equalsIgnoreCase("PUT"))
				{
					responsePost =		
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.contentType(ContentType.JSON)
							.expect()
							.when()
							.put(serviceURL);	
				}
				if(apiType.equalsIgnoreCase(patch))
				{
					responsePost =		
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.contentType(ContentType.JSON)
							.expect()
							.when()
							.patch(serviceURL);	
				}
				if(apiType.equalsIgnoreCase(delete))
				{
					responsePost =		
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.contentType(ContentType.JSON)
							.expect()
							.when()
							.delete(serviceURL);	
				}
			}
		}

		else
		{
			if(iModel.isCertificateRequired())
			{
				RestAssured.config = RestAssured.config().sslConfig(
						new SSLConfig().trustStore(APITestSettings.getTrustStoreLocation(),APITestSettings.getTrustStorePassword())
						.keyStore(certificatePath, certificatePassword));

				if(apiType.equalsIgnoreCase("GET"))
				{

					responsePost =
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.headers(defaultHeaders)
							.expect()
							.when()
							.get(serviceURL);
				}

				if(apiType.equalsIgnoreCase("POST"))
				{

					responsePost =
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.headers(defaultHeaders)
							.expect()
							.when()
							.post(serviceURL);
				}
				if(apiType.equalsIgnoreCase("PUT"))
				{

					responsePost =
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.headers(defaultHeaders)
							.expect()
							.when()
							.put(serviceURL);
				}
				if(apiType.equalsIgnoreCase(patch))
				{

					responsePost =
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.headers(defaultHeaders)
							.expect()
							.when()
							.patch(serviceURL);
				}
				if(apiType.equalsIgnoreCase(delete))
				{

					responsePost =
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.headers(defaultHeaders)
							.expect()
							.when()
							.delete(serviceURL);
				}
			}
			else
			{
				if(apiType.equalsIgnoreCase("GET"))
				{

					responsePost =
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.headers(defaultHeaders)
							.expect()
							.when()
							.get(serviceURL);
				}
				if(apiType.equalsIgnoreCase("POST"))
				{

					responsePost =
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.headers(defaultHeaders)
							.expect()
							.when()
							.post(serviceURL);
				}
				if(apiType.equalsIgnoreCase("PUT"))
				{

					responsePost =
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.headers(defaultHeaders)
							.expect()
							.when()
							.put(serviceURL);
				}
				if(apiType.equalsIgnoreCase(patch))
				{

					responsePost =
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.body(jsonDataInFile)
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.headers(defaultHeaders)
							.expect()
							.when()
							.patch(serviceURL);
				}
				if(apiType.equalsIgnoreCase(delete))
				{

					responsePost =
							RestAssured.given()
							.urlEncodingEnabled(false)
							.relaxedHTTPSValidation()
							.config(RestAssured.config())
							.contentType(ContentType.JSON)
							.headers(defaultHeaders)
							.expect()
							.when()
							.delete(serviceURL);
				}
			}
		}
		
		logger.info("-------{} ==> {} ------",iModel.getModule(), iModel.getInterfaceName()+"--------------");
				
		logger.info("------------------------serviceURL----------------------------------------");
			
		logger.info("------------------------APITestSettings.getTrustStoreLocation()-----------------------------------------");
		
		logger.info("------------------------APITestSettings.getTrustStorePassword()-----------------------------------------");
				
		logger.info("------------------------certificatePath-----------------------------------------");
				
		logger.info("------------------------certificatePassword-----------------------------------------");
			
		logger.info("--------------------------Request--------------------------");
		
		logger.info(jsonDataInFile);
		
		logger.info("--------------------------Response--------------------------");
		
		logger.info("-------{} ==> {} ------",iModel.getModule(), iModel.getInterfaceName()+"--------------");
		
		logger.info("------------------------serviceURL-----------------------------------------");
		
		logger.info("--------------------------Request--------------------------");
		
		logger.info(jsonDataInFile);
		
		logger.info("--------------------------Response--------------------------");
		
		return responsePost;
	}
}