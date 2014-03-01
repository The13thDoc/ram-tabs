package main;

/**
 * Based on code from written by VonC form stackoverflow.com.
 * 
 * Determines whether or not the OS being used in Windows or Linux.
 * This will need to be updated if Mac or other OSs are used.
 * @author RAM
 * @since 06/26/2011
 *
 */
public final class OsUtils {

	/**
	 * Name of the OS.
	 */
	private static String OS = null;
	/**
	 * Windows type
	 */
	public static final int WIN_FONT = 0;
	/**
	 * Linux type
	 */
	public static final int UNIX_FONT = 1;
	/**
	 * Current OS. -1 if not set.
	 */
	public static int CURRENT_OS = -1;
	
	/**
	 * Returns the OS name.
	 * @return String
	 */
	public static String getOsName()
	{
		if(OS == null) { 
			OS = System.getProperty("os.name");
		}
		return OS;
	}
	
	/**
	 * Sets the OS indicator for the settings.
	 */
	public static void setOSIndicator(){
		//initialize the OS name
		getOsName();
		
		if(isWindows()){
			CURRENT_OS = WIN_FONT;
		}
		if(isLinux()){
			CURRENT_OS = UNIX_FONT;
		}
	}
	
	/**
	 * Determines if the OS name is Windows.
	 * @return boolean
	 */
	public static boolean isWindows()
	{
		return getOsName().startsWith("Windows");
	}

	/**
	 * Determines if the OS name is Linux.
	 * @return boolean
	 */
	public static boolean isLinux() {
		return getOsName().startsWith("Linux");
	}
}
