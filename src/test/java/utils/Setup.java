package utils;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

import org.testng.annotations.BeforeSuite;

import io.restassured.RestAssured;
import request.pojos.AuthBody;

public class Setup {
	
	private String authToken;

	@BeforeSuite
	public void setup() {
		RestAssured.baseURI = AppConstants.getBaseuri();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

		get("/ping").then().statusCode(201);
		AuthBody authbody = new AuthBody(AppConstants.getUsername(),AppConstants.getPassword());

		authToken = given().log().all().contentType("application/json").body(authbody).when().post("/auth").then().log()
				.all().extract().path("token");
		AppConstants.setAuthToken(authToken);
	}

}
