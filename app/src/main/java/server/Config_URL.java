package server;

//This class is for storing all URLs as a model of URLs

public class Config_URL
{
	//Base URL
	//public static String base_URL = "http://192.168.43.244:10/Api_Siater/";
	public static String base_URL = "http://elearning.ubl.ac.id/API_Siater/";
	//Default configuration for WAMP - 80 is default port for WAMP and 10.0.2.2 is localhost IP in Android Emulator
	// Server user login url
	public static String URL = base_URL+"Api_Siater/";

	//params in API
	public static String TAG = "tag";
	public static String TAG_LOGIN = "login";
	public static String username = "username";
	public static String password = "password";
}