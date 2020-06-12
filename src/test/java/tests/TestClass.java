package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import request.pojos.ReqBookingDates;
import request.pojos.ReqCreateBooking;
import response.pojos.Booking;
import response.pojos.ResponseBooking;
import utils.AppConstants;
import utils.Setup;
import utils.TestDataProvider;

class TestClass extends Setup {

	Integer toBeUpdatedBookingId;
	List<Integer> bookingIds = new ArrayList<Integer>();

	@BeforeClass
	public void setUpBookingToBeUpdated() {

		ReqBookingDates dates = new ReqBookingDates("2020-06-01", "2020-06-30");
		ReqCreateBooking booking = new ReqCreateBooking("Neil", "Armsstrong", 100, true, dates, "SwimmingPool");

		Response response = given().log().all().header("Content-Type", "application/json")
				.header("Accept", "application/json").body(booking).when().post("booking").then().log().all().extract()
				.response();
		ResponseBooking createdBooking = response.as(ResponseBooking.class);
		toBeUpdatedBookingId = createdBooking.getBookingid();
		bookingIds.add(toBeUpdatedBookingId);

	}

	// Delete all bookings after suite.
	@AfterClass
	public void tearDown() {
		for (Integer id : bookingIds) {
			given().log().all().header("Content-Type", "application/json").pathParam("id", id).when()
					.cookie("token", AppConstants.getAuthToken()).delete("booking/{id}").then().log().all().assertThat()
					.statusCode(201);

		}
	}

	@Test(priority = 0, dataProvider = "createBooking", dataProviderClass = TestDataProvider.class)
	public void createBookingTestAllFields(ReqCreateBooking booking) throws IOException, InterruptedException {

		Response response = given().log().all().header("Content-Type", "application/json")
				.header("Accept", "application/json").body(booking).when().post("booking").then().log().all().extract()
				.response();
		response.then().time(Matchers.lessThanOrEqualTo(9000L));
		if (response.getStatusCode() == 200) {

			ResponseBooking createdBooking = response.as(ResponseBooking.class);
			bookingIds.add(createdBooking.getBookingid());

			// assertions to check JSON fields/field names.
			assertThat(createdBooking, hasProperty("bookingid"));
			assertThat(createdBooking, hasProperty("booking"));
			assertThat(createdBooking.getBooking(), hasProperty("firstname"));
			assertThat(createdBooking.getBooking(), hasProperty("lastname"));
			assertThat(createdBooking.getBooking(), hasProperty("totalprice"));
			assertThat(createdBooking.getBooking(), hasProperty("depositpaid"));
			assertThat(createdBooking.getBooking(), hasProperty("bookingdates"));
			assertThat(createdBooking.getBooking().getBookingdates(), hasProperty("checkin"));
			assertThat(createdBooking.getBooking().getBookingdates(), hasProperty("checkout"));
			assertThat(createdBooking.getBooking(), hasProperty("additionalneeds"));

			// assertions to check JSON field values response vs request.
			assertEquals(createdBooking.getBooking().getFirstname(), booking.getFirstname());
			assertEquals(createdBooking.getBooking().getLastname(), booking.getLastname());
			assertEquals(createdBooking.getBooking().getTotalprice(), booking.getTotalprice());
			assertEquals(createdBooking.getBooking().getDepositpaid(), booking.getDepositpaid());
			assertEquals(createdBooking.getBooking().getBookingdates().getCheckin(),
					booking.getBookingdates().getCheckin());
			assertEquals(createdBooking.getBooking().getBookingdates().getCheckout(),
					booking.getBookingdates().getCheckout());
			assertEquals(createdBooking.getBooking().getAdditionalneeds(), booking.getAdditionalneeds());

			// assertion to check business rule for non-negative deposit
			assertThat(createdBooking.getBooking().getTotalprice(), greaterThanOrEqualTo(0));
		} else {
			// assertion to check for 400 bad request response code in case of invalid JSON
			// request body
			assertEquals(response.getStatusCode(), 400);
		}

	}

	@Test(priority = 1, dataProvider = "updateBooking", dataProviderClass = TestDataProvider.class)
	public void testUpdateBooking(String patch) throws IOException, InterruptedException {

		Response response = given().log().all().header("Content-Type", "application/json")
				.header("Accept", "application/json").pathParam("idparam", toBeUpdatedBookingId).body(patch).when()
				.cookie("token", AppConstants.getAuthToken()).patch("booking/{idparam}").then().log().all().assertThat()
				.statusCode(200).extract().response();
		response.then().time(Matchers.lessThanOrEqualTo(9000L));
		Booking updatedBooking = response.as(Booking.class);

		assertThat(updatedBooking, hasProperty("firstname"));
		assertThat(updatedBooking, hasProperty("lastname"));
		assertThat(updatedBooking, hasProperty("totalprice"));
		assertThat(updatedBooking, hasProperty("depositpaid"));
		assertThat(updatedBooking, hasProperty("bookingdates"));
		assertThat(updatedBooking.getBookingdates(), hasProperty("checkin"));
		assertThat(updatedBooking.getBookingdates(), hasProperty("checkout"));
		assertThat(updatedBooking, hasProperty("additionalneeds"));

	}

	@Test(dependsOnMethods = "testUpdateBooking", dataProvider = "getBooking", dataProviderClass = TestDataProvider.class)
	public void testGetBookingAfterUpdate(Booking expectedbooking) {

		Response response = given().log().all().header("Accept", "application/json")
				.pathParam("idparam", toBeUpdatedBookingId).when().get("booking/{idparam}").then().log().all().extract()
				.response();

		response.then().time(Matchers.lessThanOrEqualTo(9000L));
		
		if (response.getStatusCode() == 200) {

			Booking getupdatedBooking = response.as(Booking.class);

			assertThat(getupdatedBooking, hasProperty("firstname"));
			assertThat(getupdatedBooking, hasProperty("lastname"));
			assertThat(getupdatedBooking, hasProperty("totalprice"));
			assertThat(getupdatedBooking, hasProperty("depositpaid"));
			assertThat(getupdatedBooking, hasProperty("bookingdates"));
			assertThat(getupdatedBooking.getBookingdates(), hasProperty("checkin"));
			assertThat(getupdatedBooking.getBookingdates(), hasProperty("checkout"));
			assertThat(getupdatedBooking, hasProperty("additionalneeds"));

			assertEquals(getupdatedBooking.getFirstname(), expectedbooking.getFirstname());
			assertEquals(getupdatedBooking.getLastname(), expectedbooking.getLastname());
			assertEquals(getupdatedBooking.getTotalprice(), expectedbooking.getTotalprice());
			assertEquals(getupdatedBooking.getDepositpaid(), expectedbooking.getDepositpaid());
			assertEquals(getupdatedBooking.getBookingdates().getCheckin(),
					expectedbooking.getBookingdates().getCheckin());
			assertEquals(getupdatedBooking.getBookingdates().getCheckout(),
					expectedbooking.getBookingdates().getCheckout());
			assertEquals(getupdatedBooking.getAdditionalneeds(), expectedbooking.getAdditionalneeds());

		} else {
			// assertion to check for 404 not found response code in case resource removed/deleted before this request.
			assertEquals(response.getStatusCode(), 404);
		}
	}

}
