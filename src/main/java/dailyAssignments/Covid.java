package dailyAssignments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import groovyjarjarantlr4.v4.parse.GrammarTreeVisitor.outerAlternative_return;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Covid {
	public static void main(String[] args) {
		RestAssured.baseURI = "https://covid-19.dataflowkit.com/v1";

		Response response = RestAssured.given().log().all().contentType(ContentType.JSON).when().get();

		JsonPath jsonResponse = response.jsonPath();
		List<String> countryList = jsonResponse.getList("Country_text");

		List<String> newCasestext = jsonResponse.getList("'New Cases_text'");

		Map<String, Integer> countryAndNewCases = new LinkedHashMap<String, Integer>();
		for (int i = 1; i < countryList.size(); i++) {
			if (!(newCasestext.get(i) == "" || newCasestext.get(i) == null)) {
				int count = Integer.parseInt(newCasestext.get(i).replaceAll("[^0-9]", ""));

				countryAndNewCases.put(countryList.get(i), count);
			}
		}

		List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(countryAndNewCases.entrySet());

		Collections.sort(entries, new Comparator<Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {

				return o1.getValue().compareTo(o2.getValue());
			}
		});
		System.out.println("------------------------------------------");
		System.out.println("Top 5 Country with Highest New Cases");
		for (int i = 0; i < 5; i++) {
			System.out.println(i + 1 + "." + entries.get(i));
		}
		System.out.println("------------------------------------------");
		System.out.println("Top 5 Country with lowest New Deaths Cases");
		int j = 1;
		for (int i = entries.size() - 1; i >= entries.size() - 5; i--) {
			System.out.println(j + "." + entries.get(i));
			j++;
		}

		response = RestAssured.given().contentType(ContentType.JSON).when().get("India")

				.then().assertThat().statusCode(200).extract().response();
		System.out.println("--------------------------------------------");

		String output = response.getTime() < 600 ? "Response Time is less than 600 ms"
				: "Response Time is not less than 600 ms";
		System.out.println(output);
		System.out.println("--------------------------------------------");
		if (response.contentType().equals("application/json")) {
			System.out.println("Content type is Json");
		}

	}
}
