package scale;

import java.util.ArrayList;

import dualKeyMap.DualKeyMap;

/**
 * 
 * @author RAM
 *
 * @version 1.8
 * @since 08/31/2010
 * Finds all possible fret options on a string for a given note.
 */
public class Scale {

	/**
	 * Unique instance of Scale.
	 */
	static Scale scale = new Scale();
	
	/**
	 * Contains each possible note (12). Default scale
	 * used by RAM Tabs.
	 */
	public static DualKeyMap<Integer, String> defaultScale;

	/**
	 * Contains the range of defaultScale used for abc4j's treble scale.
	 * 27 notes total. Ranges from low D (from bass clef), through
	 * middle C, to very high B (5 lines above high F).
	 */
	public static DualKeyMap<Integer, String> abc4jTrebleScale;
	/**
	 * Populates the list of defaultScale.
	 * Uses sharps instead of flats.
	 */
	private Scale(){
		createDefaultScale();
		createAbc4jTrebleScale();
	}

	/**
	 * Creates the defeault scale used by RAM Tabs.
	 */
	private void createDefaultScale(){
		defaultScale = new DualKeyMap<Integer, String>();

		defaultScale.put(0, "A");
		defaultScale.put(1,"A#");
		defaultScale.put(2,"B");
		defaultScale.put(3,"C");
		defaultScale.put(4,"C#");
		defaultScale.put(5,"D");
		defaultScale.put(6,"D#");
		defaultScale.put(7,"E");
		defaultScale.put(8,"F");
		defaultScale.put(9,"F#");
		defaultScale.put(10,"G");
		defaultScale.put(11,"G#");
	}
	
	/**
	 * Creates the treble scale used by abc4j.
	 */
	private void createAbc4jTrebleScale(){
		abc4jTrebleScale = new DualKeyMap<Integer, String>();

		abc4jTrebleScale.put(0, "^C,");
		abc4jTrebleScale.put(1,"D,");
		abc4jTrebleScale.put(2,"^D,");
		abc4jTrebleScale.put(3,"E,");
		abc4jTrebleScale.put(4,"F,");
		abc4jTrebleScale.put(5,"^F,");
		abc4jTrebleScale.put(6,"G,");
		abc4jTrebleScale.put(7,"^G,");
		abc4jTrebleScale.put(8,"A,");
		abc4jTrebleScale.put(9,"^A,");
		abc4jTrebleScale.put(10,"B,");
		abc4jTrebleScale.put(11,"C");
		abc4jTrebleScale.put(12, "^C");
		abc4jTrebleScale.put(13,"D");
		abc4jTrebleScale.put(14,"^D");
		abc4jTrebleScale.put(15,"E");
		abc4jTrebleScale.put(16,"F");
		abc4jTrebleScale.put(17,"^F");
		abc4jTrebleScale.put(18,"G");
		abc4jTrebleScale.put(19,"^G");
		abc4jTrebleScale.put(20,"A");
		abc4jTrebleScale.put(21,"^A");
		abc4jTrebleScale.put(22,"B");
		abc4jTrebleScale.put(23,"c");
		abc4jTrebleScale.put(24,"^c");
		abc4jTrebleScale.put(25,"d");
		abc4jTrebleScale.put(26,"^d");
		abc4jTrebleScale.put(27,"e");
		abc4jTrebleScale.put(28,"f");
		abc4jTrebleScale.put(29,"^f");
		abc4jTrebleScale.put(30,"g");
		abc4jTrebleScale.put(31,"^g");
		abc4jTrebleScale.put(32,"a");
		abc4jTrebleScale.put(33,"^a");
		abc4jTrebleScale.put(34,"b");
		abc4jTrebleScale.put(35,"c'");
		abc4jTrebleScale.put(36,"^c'");
		abc4jTrebleScale.put(37,"d'");
		abc4jTrebleScale.put(38,"^d'");
		abc4jTrebleScale.put(39,"e'");
		abc4jTrebleScale.put(40,"f'");
		abc4jTrebleScale.put(41,"^f'");
		abc4jTrebleScale.put(42,"g'");
		abc4jTrebleScale.put(43,"^g'");
		abc4jTrebleScale.put(44,"a'");
		abc4jTrebleScale.put(45,"^a'");
		abc4jTrebleScale.put(46,"b'");
	}
	
	/**
	 * Determines the actual note based on fret number.
	 * Takes the specified tuning into account.
	 * @param fret - The chosen fret number.
	 * @return String - The actual note.
	 */
	public static String transposeNote(String initialNote, int fret, DualKeyMap<Integer, String> scale){
		int index = 0;//the index of the note according to fret
		int startingNote = scale.getKey(initialNote);
		
		index = fret + startingNote;
		String note = null;//the actual note

		while(index >= 12){//reduces the index to be within 0 and 11
			index -= 12;
		};

		note = scale.getValue(index);
		return note;
	}
	
	/**
	 * Determines the actual note based on fret number.
	 * Takes the specified tuning into account.
	 * @param fret - The chosen fret number.
	 * @return String - The actual note.
	 */
	public static String transposeABC4JNote(String initialNote, int fret, DualKeyMap<Integer, String> scale){
		int index = 0;//the index of the note according to fret
		int startingNote = scale.getKey(initialNote);
		
		index = fret + startingNote;
		String note = null;//the actual note

		note = scale.getValue(index);
		return note;
	}

	/**
	 * Finds all possible frets on a particular string for the 
	 * desired note, given the string note and total number of frets available. 
	 * @param stringNote
	 * @param desiredNote
	 * @param totalFrets
	 * @return ArrayList<Integer>
	 */
	public static ArrayList<Integer> transposeFrets(String stringNote, String desiredNote, 
			int totalFrets, DualKeyMap<Integer, String> scale) {
		ArrayList<Integer> frets = new ArrayList<Integer>();
		try{
			int string = scale.getKey(stringNote);
			int desired = scale.getKey(desiredNote);
			int start = desired - string;
			int entry = start;

			do{
				if(entry >= 0 && entry <= totalFrets){
					frets.add(entry);
				}
				entry += 12;
			}while (entry <= totalFrets);

			return frets;
		}catch(ArrayIndexOutOfBoundsException e){
			frets.add(-1);
			return frets;
		}
	}
}
