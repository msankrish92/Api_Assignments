package dailyAssignments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class OpenWeather {

	@Test
	public void getWeatherForcast() {
		RestAssured.baseURI = "http://api.openweathermap.org/";
		Map<String, String> paramMap = new LinkedHashMap<>();
		paramMap.put("id", "1264527,1262180,1273294,1177654,1258899,1275339,1277333,1267254");
		paramMap.put("units", "metric");
		paramMap.put("appid", "df2e98029ce5450ae566fc404de48bba");

		Response response = RestAssured.given().log().all().formParams(paramMap).when().get("data/2.5/group").then()
				.extract().response();

		JsonPath jsonResponse = response.jsonPath();

		Map<String, String> cityWeather = new HashMap<String, String>();
		for (int i = 0; i < 8; i++) {
			cityWeather.put(jsonResponse.getString("list[" + i + "].name"),
					jsonResponse.getString("list[" + i + "].weather.main"));
		}
		System.out.println(cityWeather);
		System.out.println("====================================");
		System.out.println("City with Haze and Rain as weather are: ");
		for(Map.Entry<String, String> a : cityWeather.entrySet()) {
			if(a.getValue().equals("[Haze]")||a.getValue().equals("[Rain]")) {
				System.out.print(a.getKey() + ",");
			}
		}
		System.out.println();
		System.out.println("====================================");
		
	}
	
	@Test
	public void getVolumeofRain() throws ParseException {



		//	https://api.openweathermap.org/data/2.5/onecall?lat=19.01&lon=72.85&start=1591362395&end=1591621595&exclude=daily&appid=c397236a177654c953b206cf4304b40f


		RestAssured.baseURI= "http://api.openweathermap.org/data/2.5/onecall";


		//coverting Date Time to epoch time format

		String startDate = "2020-06-08 09:18:03.000";
		String endDate = "2020-06-10 09:18:03.000";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date sDate = df.parse(startDate);
		Date eDate = df.parse(endDate);
		long epochStartDate = sDate.getTime();
		long epochEndDate = eDate.getTime();

		System.out.println(epochStartDate);


		Map<String, String> parameterMap = new HashMap<String, String>();

		parameterMap.put("lat", "19.0");
		parameterMap.put("lon", "72.85");
		parameterMap.put("exclude", "daily");
		parameterMap.put("start", String.valueOf(epochStartDate));
		parameterMap.put("end", String.valueOf(epochEndDate));
		parameterMap.put("appid", "c397236a177654c953b206cf4304b40f");


		Response response = RestAssured
				.given()
				.log().all() 
				.params(parameterMap)
				.accept(ContentType.JSON)
				.get();

		if(response.getStatusCode()==200) {
			System.out.println(" Valid Response Returned");
		}

		response.prettyPrint();

		JsonPath jsonPath = response.jsonPath();
		List<Object> hourList = jsonPath.getList("hourly");
		int size = hourList.size();
		float rainfall, totalVolume =0.0f;


		for (int i =0 ; i<size ; i++) {

			if (jsonPath.get("hourly["+i+"].rain")!=null)
			{

				rainfall = jsonPath.get("hourly["+i+"].rain.1h");
				totalVolume =totalVolume+ rainfall;
			}
		}

		System.out.println("Total Volume :" + totalVolume + "mm");
	}

}

