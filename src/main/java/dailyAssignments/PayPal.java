package dailyAssignments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PayPal {

	@DataProvider(name = "file")
	public String[] dataprovider() {
		String[] data = new String[2];
		data[0] = "product1.json";
		data[1] = "product2.json";
		return data;
	}
	
	List<String> productId = new ArrayList<String>();

	@Test(dataProvider = "file")
	public void payPal(String fileName) {
		File file = new File(fileName);
		RestAssured.baseURI = "https://api.sandbox.paypal.com/";	

		Response response = RestAssured.given().log().all().auth()
				.basic("Aew4f6IRVCE-lwwbMIVReSdDzSRy8KKQPTE5pMkKc9au0LGgDkbAxh0zkKxuAXYrd-mXQWwWdFjfqf8E",
						"EDz_g8SGz-xAo7-8ftE-0fKPdc-YTIT-QovJCBj6Uu_tOuhOhVay9BEmdhmfSMWNTcNHN1fZu027yPmy")
				.headers("Authorization",
						"Basic QWV3NGY2SVJWQ0UtbHd3Yk1JVlJlU2REelNSeThLS1FQVEU1cE1rS2M5YXUwTEdnRGtiQXhoMHprS3h1QVhZcmQtbVhRV3dXZEZqZnFmOEU6RUR6X2c4U0d6LXhBbzctOGZ0RS0wZktQZGMtWVRJVC1Rb3ZKQ0JqNlV1X3RPdWhPaFZheTlCRW1kaG1mU01XTlRjTkhOMWZadTAyN3lQbXk=")
				.contentType(ContentType.JSON).body(file).post("v1/catalogs/products").then().log().all().assertThat()
				.statusCode(201).extract().response();

		JsonPath jsonResponse = response.jsonPath();
		productId.add(jsonResponse.getString("id"));
		

	}
	
	@Test(dependsOnMethods = "payPal")
	public void createdProducts() {
		RestAssured.baseURI = "https://api.sandbox.paypal.com/";
		Response respone = RestAssured.given()
				.log().all()
				.auth()
				.basic("Aew4f6IRVCE-lwwbMIVReSdDzSRy8KKQPTE5pMkKc9au0LGgDkbAxh0zkKxuAXYrd-mXQWwWdFjfqf8E",
						"EDz_g8SGz-xAo7-8ftE-0fKPdc-YTIT-QovJCBj6Uu_tOuhOhVay9BEmdhmfSMWNTcNHN1fZu027yPmy")
				.headers("Authorization",
						"Basic QWV3NGY2SVJWQ0UtbHd3Yk1JVlJlU2REelNSeThLS1FQVEU1cE1rS2M5YXUwTEdnRGtiQXhoMHprS3h1QVhZcmQtbVhRV3dXZEZqZnFmOEU6RUR6X2c4U0d6LXhBbzctOGZ0RS0wZktQZGMtWVRJVC1Rb3ZKQ0JqNlV1X3RPdWhPaFZheTlCRW1kaG1mU01XTlRjTkhOMWZadTAyN3lQbXk=")
				.queryParam("page_size", 100,"page",1,"total_required",true)
				.when()
				.get("v1/catalogs/products")
				.then()
				.assertThat().statusCode(200).extract().response();
	
		JsonPath jsonResponse = respone.jsonPath();
		List<String> idList = jsonResponse.getList("products.id");
		System.err.println(idList);
		for (int i = 0; i < productId.size(); i++) {
			if(idList.contains(productId.get(i))) {
				System.out.println("Product found in the list: "  + productId.get(i));
			}
		}
	}
	

}
