package utils;

public class AppConstants {

	private static String authToken = "";

	private static final String baseURI = "https://restful-booker.herokuapp.com/";
	
	private static final String username="admin";
	
	private static final String password="password123";
	
	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return password;
	}

	public static String getBaseuri() {
		return baseURI;
	}

	public static String getAuthToken() {
		return authToken;
	}

	public static void setAuthToken(String authToken) {
		AppConstants.authToken = authToken;
	}

}
