package externals;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Controls all user preference settings.
 * @author main
 *
 */
public class UserPreferences {

	/**
	 * Location on the screen.
	 */
	private Point										tabLocation;
	/**
	 * Size of frame.
	 */
	private Dimension									tabSize;
	/**
	 * File with saved preferences.
	 */
	private final File									PREFS_PATH = new File("tab_prefs");

	/**
	 * Constructor.
	 *
	 */
	public UserPreferences(){
		tabLocation = new Point();
		tabSize = new Dimension(1000, 700);

		try {
			PREFS_PATH.createNewFile();
		} catch (IOException e) {
		}
	}

	/**
	 * Saves the preferences.
	 */
	public void savePreferences(){
		try{
			PrintWriter writer = new PrintWriter(PREFS_PATH);

			writer.println(tabLocation.getX()+","+tabLocation.getY());
			writer.println(tabSize.getWidth()+","+tabSize.getHeight());

			writer.close();
		}
		catch(IOException e){
		}
	}

	/**
	 * Loads the preferences.
	 */
	public boolean loadPreferences(){
		try{
			Scanner reader = new Scanner(PREFS_PATH);
			String[] array;

			array = reader.nextLine().split(",");
			tabLocation.setLocation(Double.parseDouble(array[0]), Double.parseDouble(array[1]));

			array = reader.nextLine().split(",");
			tabSize.setSize(Double.parseDouble(array[0]), Double.parseDouble(array[1]));

			reader.close();
			return true;
		}catch (NoSuchElementException e){
			//blank file, no line
			return false;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * Sets the location of the tab frame.
	 * @param p
	 */
	public void setLocation(Point p){
		tabLocation = p;
	}

	/**
	 * Returns the location of the tab frame.
	 * @return Point
	 */
	public Point getLocation(){
		return tabLocation;
	}

	/**
	 * Sets the size of the tab frame.
	 * @param d
	 */
	public void setSize(Dimension d){
		tabSize = d;
	}

	/**
	 * Returns the size of the tab frame.
	 * @return Dimension
	 */
	public Dimension getSize(){
		return tabSize;
	}
}
