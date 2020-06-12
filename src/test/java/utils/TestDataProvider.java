package utils;

import org.testng.annotations.DataProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import request.pojos.ReqBookingDates;
import request.pojos.ReqCreateBooking;
import request.pojos.ReqPatchAdditionalNeeds;
import request.pojos.ReqPatchBookingDates;
import request.pojos.ReqPatchDepositPaid;
import request.pojos.ReqPatchFirstName;
import request.pojos.ReqPatchLastName;
import request.pojos.ReqPatchTotalPrice;
import response.pojos.Booking;
import response.pojos.BookingDates;

public class TestDataProvider {

	private String firstname = "";
	private String lastname = "";
	private String totalprice = "";
	private String depositpaid = "";
	private String additionalneed = "";
	private String bookingdates = "";

	ObjectMapper mapper = new ObjectMapper();

	ReqBookingDates dates = new ReqBookingDates("2020-06-01", "2020-06-30");

	ReqBookingDates nullDates = new ReqBookingDates();
	
		
	// all valid inputs- positive test case
	ReqCreateBooking validPositive = new ReqCreateBooking("Tom", "Moody", 100, true, dates, "Breakfast");

	// all valid inputs but deposit paid is negative - negative business test case -
	//currently API is saving negative values, so this test will fail as per assertion in test method
	ReqCreateBooking validPositiveInvalidBusinessData = new ReqCreateBooking("Licifer", "Moody", -100, true, dates,
			"Breakfast");

	// first name is blank --bad request expected, 400 response code --currently API is returning 500 so this test will fail
	ReqCreateBooking blankFirsrName = new ReqCreateBooking("", "McDonald's", 1000, true, dates, "Breakfast");

	// last name is null --bad request expected, 400 response code --currently API is returning 500 so this test will fail
	ReqCreateBooking nullLastName = new ReqCreateBooking("Steve", null, 200, true, dates, "tt");

	// booking dates are null --bad request expected, 400 response code --currently API is returning 500 so this test will fail
	ReqCreateBooking nullBookingDates = new ReqCreateBooking("Mark", "Moody", 0, true, nullDates, "Breakfast");

	@DataProvider(name = "createBooking")
	public Object[][] creatBookingData() {
		return new Object[][] { { validPositive }, { validPositiveInvalidBusinessData }, { blankFirsrName },
				{ nullLastName }, { nullBookingDates } };
	}

	
	//Data for updating each field separately
	
	ReqPatchFirstName reqPatchFirstName = new ReqPatchFirstName("Robert");
	ReqPatchLastName reqPatchLastName = new ReqPatchLastName("Swan");
	ReqPatchTotalPrice reqPatchTotalPrice = new ReqPatchTotalPrice(500);
	ReqPatchDepositPaid reqPatchDepositPaid = new ReqPatchDepositPaid(false);
	ReqPatchAdditionalNeeds reqPatchAdditionalNeeds = new ReqPatchAdditionalNeeds("Bonefire");
	ReqBookingDates reqBookingDates = new ReqBookingDates("2020-01-31", "2020-02-10");
	ReqPatchBookingDates reqPatchBookingDates = new ReqPatchBookingDates(reqBookingDates);

	@DataProvider(name = "updateBooking")
	public Object[][] updateBooking() {

		try {
			firstname = mapper.writeValueAsString(reqPatchFirstName);
			lastname = mapper.writeValueAsString(reqPatchLastName);
			totalprice = mapper.writeValueAsString(reqPatchTotalPrice);
			depositpaid = mapper.writeValueAsString(reqPatchDepositPaid);
			bookingdates = mapper.writeValueAsString(reqPatchBookingDates);
			additionalneed = mapper.writeValueAsString(reqPatchAdditionalNeeds);

		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}

		return new Object[][] { { firstname }, { lastname }, { totalprice }, { depositpaid }, { bookingdates },
				{ additionalneed } };
	}

	
	
	@DataProvider(name = "getBooking")
	public Object[][] getBooking() {

	//setting below booking as expected booking for GET method with same values used in above data provider for updating booking.	
		Booking booking = new Booking();
		BookingDates dates = new BookingDates();
		dates.setCheckin("2020-01-31");
		dates.setCheckout("2020-02-10");
		booking.setFirstname("Robert");
		booking.setLastname("Swan");
		booking.setTotalprice(500);
		booking.setDepositpaid(false);
		booking.setBookingdates(dates);
		booking.setAdditionalneeds("Bonefire");

		return new Object[][] { { booking } };
	}

}
