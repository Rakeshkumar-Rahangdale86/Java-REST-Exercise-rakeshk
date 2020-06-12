package request.pojos;

public class ReqPatchBookingDates {
 
	private ReqBookingDates bookingdates;

	public ReqPatchBookingDates() {
		
	}
	
	public ReqPatchBookingDates(ReqBookingDates bookingdates) {
		super();
		this.bookingdates = bookingdates;
	}

	public ReqBookingDates getBookingdates() {
		return bookingdates;
	}

	public void setBookingdates(ReqBookingDates bookingdates) {
		this.bookingdates = bookingdates;
	}
	
	
}
