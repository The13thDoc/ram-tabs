package guis;

import javax.swing.*;

import observerPattern.Observer;
import observerPattern.Notifier;

import java.util.*;
import java.awt.*;

/**
 * Generates the GUI for guitar tablature.
 * 
 * @author RAM
 * @version 1.0
 * @created 04-Jan-2010 7:16:50 AM
 */
/*
 * font for tabs: Courier New, regular, size 10?
 */
@SuppressWarnings("serial")
public class TablatureUI extends JPanel implements Notifier {

	/**
	 * Contains the arrays that hold the JLists. Each index corresponds to a
	 * string (based on index 1).
	 */
	private ArrayList<ArrayList<JList>> arrayStrings;
	/**
	 * Number of strings being used.
	 */
	private int strings;
	/**
	 * Receives events for TablatureUI.
	 */
	private TablatureUIEventHandler event;
	/**
	 * Number of columns being used. Defaulted to 32.
	 */
	private int columns = 32;
	/**
	 * Number of frets on the guitar. Defaulted to 24.
	 */
	private int frets = 24;

	private Observer observer;
	private int fontSize;
	private Dimension cellSize;
	private final int ID = UIController.TABLTURE_UI;

	/**
	 * Constructor. Accepts the number of strings needed.
	 * 
	 * @param numberOfStrings
	 */
	public TablatureUI(int numberOfStrings) {
		this.strings = numberOfStrings;

		arrayStrings = new ArrayList<ArrayList<JList>>(numberOfStrings + 1);
		arrayStrings.add(null);// fill index 0 with null
	}

	/**
	 * Starts the GUI process.
	 */
	public void generateUI() {
		setBackground(Color.white);
		setLayout(new FlowLayout(FlowLayout.LEADING));
		setBorder(BorderFactory.createEmptyBorder());
		addKeyListener(event);

		for (int i = 1; i <= strings; i++) {
			for (int z = 0; z <= columns + 1; z++) {
				add(arrayStrings.get(i).get(z));
			}
		}
		event = new TablatureUIEventHandler(this);
	}

	/**
	 * Sets the pre-made array of JLists for the tab model.
	 * 
	 * @param arrayStrings
	 */
	public void setTabModel(ArrayList<ArrayList<JList>> arrayStrings) {
		this.arrayStrings = arrayStrings;
	}

	/**
	 * Sets the note to the string and fret.
	 * 
	 * @param int - string
	 * @param int - fret
	 * @param int - note
	 * @return boolean - valid
	 */
	public boolean setNote(int string, int fret, int note) {
		DefaultListModel model = new DefaultListModel();

		if (0 <= note && note <= 24) {// check if fret is within range
			model.addElement(note);
			arrayStrings.get(string).get(fret).setModel(model);
			// keep the cell selected
			arrayStrings.get(string).get(fret).setSelectedIndex(0);
			return true;
		} else if (note == -1) {
			model.addElement("---");
			arrayStrings.get(string).get(fret).setModel(model);
			// keep the cell selected
			arrayStrings.get(string).get(fret).setSelectedIndex(0);
			return true;
		} else {// if negative or great than 24, do not set it to the model
			return false;
		}

	}

	/**
	 * Sets the note to the string and fret.
	 * 
	 * @param int - string
	 * @param int - fret
	 * @param String
	 *            - note
	 * @return boolean - valid
	 */
	public boolean setNote(int string, int fret, String note) {
		DefaultListModel model = new DefaultListModel();

		if (note.equals("-")) {
			model.addElement("---");
			arrayStrings.get(string).get(fret).setModel(model);
			// keep the cell selected
			arrayStrings.get(string).get(fret).setSelectedIndex(0);
			return true;
		} else if (note.contains("|")) {
			note = "--" + note.substring(1);
			model.addElement(note);
			arrayStrings.get(string).get(fret).setModel(model);
			// keep the cell selected
			arrayStrings.get(string).get(fret).setSelectedIndex(0);
			return true;
		} else if (note.compareToIgnoreCase("X") == 0) {
			model.addElement("X");
			arrayStrings.get(string).get(fret).setModel(model);
			// keep the cell selected
			arrayStrings.get(string).get(fret).setSelectedIndex(0);
			return true;
		} else {// if negative or great than 24, do not set it to the model
			return false;
		}
	}

	/**
	 * Sets the note to the string and fret. Used for loading Tabs.
	 * 
	 * @param string
	 * @param fret
	 * @param note
	 * @return
	 */
	public boolean loadNote(int string, int fret, String note) {
		DefaultListModel model = new DefaultListModel();

		model.addElement(note);
		arrayStrings.get(string).get(fret).setModel(model);
		return true;
	}

	/**
	 * Determines if the JList is selected.
	 * 
	 * @param string
	 * @param fret
	 * @return boolean - nothing selected
	 */
	public boolean checkSelected(int string, int fret) {
		return arrayStrings.get(string).get(fret).isSelectionEmpty();
	}

	/**
	 * Returns the note from the string and fret.
	 * 
	 * @param int string
	 * @param int fret
	 * @return String - note(fret number)
	 */
	public String getNote(int string, int fret) {
		String note = arrayStrings.get(string).get(fret).getModel()
				.getElementAt(0).toString();

		return note;
	}

	/**
	 * Returns the number of strings.
	 * 
	 * @return int - strings
	 */
	public int getStrings() {
		return this.strings;
	}

	/**
	 * Returns the number of columns.
	 * 
	 * @return int - columns
	 */
	public int getColumns() {
		return this.columns;
	}

	/**
	 * Sets the number of strings.
	 * 
	 * @param int - strings
	 */
	public void setStrings(int strings) {
		this.strings = strings;
	}

	/**
	 * Sets the number of columns.
	 * 
	 * @param int - columns
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * Returns the number of frets.
	 * 
	 * @return int - columns
	 */
	public int getFrets() {
		return this.frets;
	}

	/**
	 * Sets the number of frets.
	 * 
	 * @param int - columns
	 */
	public void setFrets(int frets) {
		this.frets = frets;
	}

	@Override
	public void addObserver(Observer o) {
		this.observer = o;
	}

	@Override
	public void notifyObservers() {
		this.observer.update(this);
	}

	@Override
	public void removeObserver(Observer o) {
		// do nothing
	}

	/**
	 * Sets the user input to the selected indices.
	 * 
	 * @param String
	 *            - input
	 * @return boolean - valid entry
	 */
	public boolean setInput(String input) {
		for (int i = 1; i <= strings; i++) {// current string
			for (int f = 1; f <= columns; f++) {// current column/fret
				boolean nothingSelected = checkSelected(i, f);
				boolean flag = true;// if true, input is an int, otherwise a
									// String

				if (!nothingSelected) {
					try {
						Integer.parseInt(input.toString());
					} catch (NumberFormatException e) {
						flag = false;
					}
					if (flag) {
						setNote(i, f, Integer.parseInt(input.toString()));
					} else {
						setNote(i, f, input);
					}
				}
			}
		}
		return true;
	}

	/**
	 * Returns the current user input.
	 * 
	 * @return String - input
	 */
	public String getInput() {
		return event.getInput();
	}

	/**
	 * Sets the keyEntered boolean.
	 * 
	 * @param boolean
	 */
	// public void setKeyEntered(boolean b){
	// event.setKeyEntered(b);
	// }

	/**
	 * Returns the keyEntered boolean.
	 * 
	 * @return boolean
	 */
	// public boolean getKeyEntered(){
	// return event.getKeyEntered();
	// }

	/**
	 * Returns the ListModel for the specified JList. This is only used by the
	 * FileHandler for saving and loading.
	 * 
	 * @param string
	 * @param fret
	 * @return ListModel
	 */
	public ListModel getModel(int string, int fret) {
		return arrayStrings.get(string).get(fret).getModel();
	}

	/**
	 * Returns the font size.
	 * 
	 * @return int
	 */
	public int getFontSize() {
		return this.fontSize;
	}

	/**
	 * Sets the font size.
	 * 
	 * @param size
	 */
	public void setFontSize(int size) {
		this.fontSize = size;
	}

	/**
	 * Returns the JList cell size.
	 * 
	 * @return Dimension
	 */
	public Dimension getCellSize() {
		return this.cellSize;
	}

	/**
	 * Sets the JList cell size.
	 * 
	 * @param size
	 */
	public void setCellSize(Dimension size) {
		this.cellSize = size;
	}

	@Override
	public int getType() {
		return ID;
	}

	/**
	 * Unselects every cell.
	 */
	public void unselectAll() {
		for (int i = 1; i <= strings; i++) {// current string
			for (int f = 1; f <= columns; f++) {// current column/fret
				arrayStrings.get(i).get(f).clearSelection();
			}
		}
	}
}