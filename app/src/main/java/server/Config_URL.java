package server;

//This class is for storing all URLs as a model of URLs

public class Config_URL
{
	//Base URL
	public static String base_URL = "http://172.16.1.33:10/Api_Siater/v2/";
	//public static String base_URL = "http://172.16.2.174:10/Api_Siater/v2/";
	//public static String base_URL = "http://ublapps.ubl.ac.id/Api_Siater/v2/";
	//Default configuration for WAMP - 80 is default port for WAMP and 10.0.2.2 is localhost IP in Android Emulator
	// Server user login url
	public static String URL = base_URL+"Api_Siater/";

	public static String listMhs = "http://192.168.43.244:10/Api_Siater/include/View_Absen_Mahasiswa.php";

	//params in API
	public static String TAG = "tag";
	public static String TAG_LOGIN = "login";
	public static String username = "username";
	public static String password = "password";
}