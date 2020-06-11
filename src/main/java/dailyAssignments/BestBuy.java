package dailyAssignments;

import java.util.LinkedHashMap;
import java.util.Map;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class BestBuy {

	@Test
	public void StoreNameNearToPostalCode() {
		RestAssured.baseURI = "https://api.bestbuy.com/";
		Map<String, String> paramMap = new LinkedHashMap<>();
		paramMap.put("format", "json");
		paramMap.put("show", "name,address,distance,region");
		paramMap.put("pageSize", "1");
		paramMap.put("apiKey", "qUh3qMK14GdwAs9bO59QRSCJ");

		Response response = RestAssured.given().log().all().params(paramMap).when().get("v1/stores(area(02886,10))")
				.then().assertThat().statusCode(200).extract().response();

//		response.prettyPrint();
		JsonPath jsonResponse = response.jsonPath();
		String name = jsonResponse.getString("stores.name");
		String address = jsonResponse.getString("stores.address");
		String distance = jsonResponse.getString("stores.distance");
		System.out.println("======================================");
		System.out.println("Name of the shop is: " + name);
		System.out.println("Address is: " + address);
		System.out.println("Distance is" + distance);
		System.out.println("======================================");

	}

	@Test
	public void findAllCannonProducts() {
		Map<String, String> paramMap = new LinkedHashMap<>();
		paramMap.put("format", "json");
		paramMap.put("show", "sku,name,salePrice");
		paramMap.put("apiKey", "qUh3qMK14GdwAs9bO59QRSCJ");

		Response response = RestAssured.given().log().all().formParams(paramMap).when()
				.get("v1/products(manufacturer=canon&salePrice>1000&salePrice<1500)").then().assertThat()
				.statusCode(200).extract().response();

		response.prettyPrint();

	}

	@Test
	public void getRegularAndSellingPrice() {
		Map<String, String> paramMap = new LinkedHashMap<>();
		paramMap.put("format", "json");
		paramMap.put("show", "regularPrice,salePrice");
		paramMap.put("apiKey", "qUh3qMK14GdwAs9bO59QRSCJ");

		Response response = RestAssured.given().log().all().formParams(paramMap).when().get("v1/products(sku=6382722)")
				.then().assertThat().statusCode(200).extract().response();

		JsonPath jsonResponse = response.jsonPath();

		String regularPrice = jsonResponse.getString("products.regularPrice");
		String salePrice = jsonResponse.getString("products.salePrice");

		System.out.println("=================================");
		System.out.println("The Regular Price is: " + regularPrice);
		System.out.println("The Sale Price now is: " + salePrice);
		System.out.println("=================================");
	}

	@Test
	public void inStoreAvailability() {
		Map<String, String> paramMap = new LinkedHashMap<>();
		
		paramMap.put("postalCode", "02886");
		paramMap.put("apiKey", "qUh3qMK14GdwAs9bO59QRSCJ");
		
		Response respone = RestAssured.given()
				.log()
				.all()
				.params(paramMap)
				.when()
				.get("v1/products/6382722/stores.json")
				.then()
				.assertThat()
				.statusCode(200)
				.extract()
				.response();
		
		JsonPath jsonRespone = respone.jsonPath();
		System.out.println("=====================================");
		if(jsonRespone.getBoolean("ispuEligible")){
			System.out.println("Available in Store");
		}else {
			System.out.println("Not Availabe in Store");
		}
		System.out.println("=====================================");
	}

}
