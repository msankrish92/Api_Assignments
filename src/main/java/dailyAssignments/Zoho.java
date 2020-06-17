package org.Assignment;

import java.util.LinkedHashMap;
import java.util.Map;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Zoho {
	String customerId = new String();
//	RestAssured.baseURI = "https://subscriptions.zoho.com/";
	
	public RequestSpecification reqspec() {
		Map<String,String> paramMap = new LinkedHashMap<>();
		paramMap.put("Authorization", "Zoho-oauthtoken 1000.3fe85f83a92ad41b4813d651f75c7853.0a3e51b2b71b1f4bb01400785f9c9e27");
		paramMap.put("X-com-zoho-subscriptions-organizationid", "717394497");
		paramMap.put("Content-Type", "application/json;charset=UTF-8");
		RestAssured.baseURI = "https://subscriptions.zoho.com/";
		return RestAssured.given().log().all()
				.headers(paramMap);
	}
	
	
	
	@Test
	public void createCustomer() {
			
		Response response = reqspec()
				
				.body("{\r\n" + 
						"    \"display_name\": \"Mohan\",\r\n" + 
						"	\"email\": \"benjamin.george@bowmanfurniture.com\"\r\n" + 
						"}")
				.when()
				.post("api/v1/customers")
				.then()
				.assertThat()
				.statusCode(201)
				.extract()
				.response();
		response.prettyPrint();
		JsonPath jsonResponse = response.jsonPath();
		customerId = jsonResponse.getString("customer.customer_id");
					
	}
	
	@Test(dependsOnMethods = "createCustomer")
	public void updateCustomerDetails() {
		
		Response response = reqspec()
				.body("{\r\n" + 
						"    \"display_name\": \"Sanjay Krishnan\",\r\n" + 
						"    \"salutation\": \"Mr.\",\r\n" + 
						"    \"first_name\": \"Sanjay\",\r\n" + 
						"    \"last_name\": \"Krishnan\",\r\n" + 
						"    \"email\": \"msankrish92@gmail.com\",\r\n" + 
						"    \"company_name\": \"Cognizant\",\r\n" + 
						"    \"phone\": 9940157064,\r\n" + 
						"    \"mobile\": 9940157064,\r\n" + 
						"\"shipping_address\": {\r\n" + 
						"        \"attention\": \"Sanjay Krishnan\",\r\n" + 
						"        \"street\": \"nookampalayam road\",\r\n" + 
						"        \"city\": \"Chennai\",\r\n" + 
						"        \"state\": \"TamilNadu\",\r\n" + 
						"        \"zip\": 600100,\r\n" + 
						"        \"country\": \"India\",\r\n" + 
						"        \"fax\": 123456\r\n" + 
						"    }\r\n" + 
						"}")
				.when()
				.put("api/v1/customers/"+customerId)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				.extract()
				.response();
		
		response.prettyPrint();
	}
	
	@Test(dependsOnMethods = "updateCustomerDetails")
	public void removeCustomer() {
		Response response = reqspec()
		.when()
		.delete("api/v1/customers/"+customerId)
		.then()
		.log()
		.all()
		.assertThat()
		.statusCode(200)
		.extract()
		.response();
		
		response.prettyPrint();
		
	}
	
	
	
	
	
}
