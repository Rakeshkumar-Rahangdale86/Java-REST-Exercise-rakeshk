package response.pojos;

public class ResponseBooking {
	
	private int bookingid;

    private Booking booking;

    public void setBookingid(int bookingid){
        this.bookingid = bookingid;
    }
    public int getBookingid(){
        return this.bookingid;
    }
    public void setBooking(Booking booking){
        this.booking = booking;
    }
    public Booking getBooking(){
        return this.booking;
    }

}
