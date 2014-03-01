package manuscript;

import externals.FileHandler;
import guis.LyricUI;
import guis.StaffUI;
import guis.TablatureUI;
import guis.TransposeUI;
import guis.TuningUI;
import guis.UIController;

import javax.swing.*;

import main.OsUtils;
import main.TabApp;
import main.TabApplet;

import observerPattern.Observer;
import observerPattern.Notifier;

import java.util.*;
import java.awt.*;
import java.io.File;

/**
 * The GUI that contains both the TablatureUI and StaffUI. Operates seperately
 * from the TabApp.
 * 
 * @author RAM
 * @version 1.0
 * @since 04-Jan-2010 7:16:49 AM
 * 
 * @version 1.8
 * @since 08/31/2010 Has the ability to move measures. This seems to be a bit
 *        glitchy.
 * 
 * @version 2.0
 * @since 04/18/2011 - ManuscriptStorage was merged into Manuscript. - Creation
 *        of the panel that holds each UI was moved into UIController.
 * 
 * @version 2.2
 * @since 09/3/2011 - Menu items for measures put into here.
 */
public class Manuscript implements Notifier {

	/**
	 * The parent component (TabApp).
	 */
	public TabApp parent;
	public TabApplet parentApplet;
	/**
	 * Used to sort each measure.
	 */
	public JTabbedPane tabbedPane;
	/**
	 * ScrollPane used for each measure.
	 */
	private JScrollPane scrollBar;
	/**
	 * The currently selected tab in the JTabbedFrame
	 */
	private int selectedTab;
	/**
	 * The black sheep icon for the UI.
	 */
	private Icon iconBlackSheep;// = new ImageIcon("./InkyBuckTrans.png");
	/**
	 * Receives all events created by Manuscript.
	 */
	private ManuscriptUIEventHandler event;
	/**
	 * Observer TabApp.
	 */
	private Observer observer;
	/**
	 * Handles saves, loads, writes, etc.
	 */
	private FileHandler file;

	public int firstIndex;
	public int lastIndex;

	private int fontType = OsUtils.CURRENT_OS;

	/**
	 * Name of the Tab.
	 */
	private String tabName;
	/**
	 * Contains the 'compenents' arrays. Each index corresponds to a tab on the
	 * JTabbedPane (each index is for a measure).
	 */
	private ArrayList<UIController> measure;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            TabApplet
	 */
	public Manuscript(TabApplet parent) {
		this.parentApplet = parent;
		iconBlackSheep = null;
		construct();
	}

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            TabApp
	 */
	public Manuscript(TabApp parent, Icon icon) {
		this.parent = parent;
		iconBlackSheep = icon;
		construct();
	}

	/**
	 * Constructor.
	 */
	public void construct() {
		event = new ManuscriptUIEventHandler(this);
		if (parent != null) {
			observer = parent;
		} else {
			observer = parentApplet;
		}
		addObserver(observer);// this observes TabApp

		measure = new ArrayList<UIController>();
		setTabName("Untitled");

		file = new FileHandler(this);

		// instantiate the JTabbedPane immediately for use on any measure
		tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.CENTER);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setBackground(Color.lightGray);
		tabbedPane.setVisible(true);
		tabbedPane.addChangeListener(event);
		tabbedPane.addMouseListener(event);
		tabbedPane.addMouseMotionListener(event);
	}

	/**
	 * Creates a new measure containing a Tablature and Staff.
	 * 
	 * @param numberOfStrings
	 * @param measureName
	 */
	public void addMeasure(int numberOfStrings, String measureName) {
		init(numberOfStrings, measureName);
	}

	private void init(int numberOfStrings, String measureName) {
		int tabs = getTabCount();
		int actualStrings = 0;

		if (numberOfStrings < 4) {
			actualStrings = getActualNumberOfStrings(numberOfStrings);
		} else {
			actualStrings = numberOfStrings;
		}

		/*
		 * Create new GUIs
		 */
		Dimension size = null;
		// int width = 1370;//size 18 font
		int width = 1060;// size 13, 14 font
		// int width = 957;//size 12 font
		// int height = 525;//with StaffUI
		// int height = 380;//without StaffUI

		switch (actualStrings) {
		case 4:
			size = new Dimension(width, 125);
			break;
		case 5:
			size = new Dimension(width, 150);
			break;
		case 6:
			size = new Dimension(width, 175);
			break;
		case 7:
			size = new Dimension(width, 200);
			break;
		}

		UIController controller = new UIController(actualStrings, this, size,
				measureName);
		controller.generateUI();

		addToMeasure(controller);// Add array of new components to measure array

		/*
		 * Add UIs to tabbedPane
		 */
		scrollBar = new JScrollPane(controller.getUI());
		scrollBar.setBorder(BorderFactory.createEtchedBorder());
		// scrollBar.setPreferredSize(new Dimension(800,600));
		scrollBar.setVisible(true);
		scrollBar.setAutoscrolls(true);

		/*
		 * Create the new tab and insert the component
		 */
		tabbedPane.insertTab(measureName, iconBlackSheep, scrollBar,
		/* measureName */"Right-click me for menu options!", tabs);

		if (parent != null) {
			parent.changeTuningUI();
		} else {
			parentApplet.changeTuningUI();
		}

		tabbedPane.validate();
		controller.updateTransposeUI();
	}

	/**
	 * Copies the selected measure.
	 */
	public void copyMeasure(int measure, String name) {
		int strings = getNumberOfStrings(measure);// strings from original
		int frets = getTablatureUIFromMeasure(measure).getColumns();// frets
																	// from
																	// original

		addMeasure(strings, name);
		int newMeasure = this.getTabCount() - 1;

		for (int i = 1; i <= strings; i++) {// string
			for (int f = 0; f <= frets; f++) {// fret
				ListModel model = getTablatureUIFromMeasure(measure).getModel(
						i, f);
				// get original model

				loadNote(newMeasure, i, f, model.getElementAt(0).toString());
				// copy model to new measure
			}
		}
	}

	/**
	 * Moves the selected tab to the chosen index.
	 * 
	 * @param dst
	 *            destination index of the selected tab
	 */
	public void moveMeasure() {
		// int src = selectedTab;

		// Get all the properties
		Component comp = tabbedPane.getComponentAt(firstIndex);
		String label = tabbedPane.getTitleAt(firstIndex);
		Icon icon = tabbedPane.getIconAt(firstIndex);
		Icon iconDis = tabbedPane.getDisabledIconAt(firstIndex);
		String tooltip = tabbedPane.getToolTipTextAt(firstIndex);
		boolean enabled = tabbedPane.isEnabledAt(firstIndex);
		int keycode = tabbedPane.getMnemonicAt(firstIndex);
		int mnemonicLoc = tabbedPane.getDisplayedMnemonicIndexAt(firstIndex);
		Color fg = tabbedPane.getForegroundAt(firstIndex);
		Color bg = tabbedPane.getBackgroundAt(firstIndex);

		// Remove the tab
		tabbedPane.remove(firstIndex);

		// Add a new tab
		tabbedPane.insertTab(label, icon, comp, tooltip, lastIndex);

		// Restore all properties
		tabbedPane.setDisabledIconAt(lastIndex, iconDis);
		tabbedPane.setEnabledAt(lastIndex, enabled);
		tabbedPane.setMnemonicAt(lastIndex, keycode);
		tabbedPane.setDisplayedMnemonicIndexAt(lastIndex, mnemonicLoc);
		tabbedPane.setForegroundAt(lastIndex, fg);
		tabbedPane.setBackgroundAt(lastIndex, bg);
		tabbedPane.setSelectedIndex(lastIndex);

		moveMeasure(firstIndex, lastIndex);
	}

	/**
	 * Adds a new UIController to the array.
	 * 
	 * @param UIController
	 */
	public void addToMeasure(UIController controller) {
		measure.add(controller);
	}

	/**
	 * Moves the measure in the array.
	 * 
	 * @param src
	 *            the measure to move
	 * @param dst
	 *            the index to move it to
	 */
	public void moveMeasure(int src, int dst) {
		UIController cntrl = measure.get(src);
		measure.remove(src);
		measure.add(dst, cntrl);
	}

	/**
	 * Closes the Tab.
	 */
	public void closeTab() {
		event.setClosed(true);
		close();
		event.setClosed(false);
	}

	/**
	 * Delete the selected measure.
	 * 
	 * @param measure
	 */
	public void deleteMeasure(int measure) {
		tabbedPane.remove(measure);
		this.measure.remove(measure);
	}

	/**
	 * Loads the chosen Tab.
	 * 
	 * @param file
	 */
	public boolean loadTab(File file) {
		return this.file.loadTab(file);
	}

	/**
	 * Save the current tab.
	 */
	public boolean saveTab() {
		ArrayList<TablatureUI> tabs = new ArrayList<TablatureUI>();

		for (int i = 0; i < getTabCount(); i++) {// put all tablatures in an
													// array
			tabs.add(getTablatureUIFromMeasure(i));
		}
		return this.file.save(tabs);// give array to FileHandler
	}

	/**
	 * Save the current tab as the chosen file.
	 */
	public boolean saveAs(File file) {
		ArrayList<TablatureUI> tabs = new ArrayList<TablatureUI>();

		for (int i = 0; i < getTabCount(); i++) {// put all tablatures in an
													// array
			tabs.add(getTablatureUIFromMeasure(i));
		}
		return this.file.saveAs(file, tabs);// give array to FileHandler
	}

	/**
	 * Sets the name of the selected measure.
	 * 
	 * @param tabPaneIndex
	 * @param name
	 */
	public void setMeasureName(int index, String name) {
		measure.get(index).setName(name);
		tabbedPane.setTitleAt(index, name);
	}

	/**
	 * Adds a measure name to the array.
	 * 
	 * @param String
	 *            - name
	 */

	/**
	 * Sets the name of the tab.
	 * 
	 * @param String
	 *            - Tab name.
	 */
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	/**
	 * Sets the tuning.
	 * 
	 * @param measure
	 * @param string
	 * @param note
	 */
	public void setTuning(int measure, int string, String note) {
		this.getTuningUI(measure).setTuning(note, string);
	}

	/**
	 * Sets the tuning.
	 * 
	 * @param preset
	 */
	public void setTuning(int measure, String[] preset) {
		this.getTuningUI(measure).setTuning(preset);
	}

	/**
	 * Writes the tab to the current text file.
	 */
	public boolean writeTab() {
		boolean success = false;
		success = this.file.write(getUIControllers());// give array to
														// FileHandler
		file.writeScore(getUIControllers());
		return success;
	}

	/**
	 * Writes the tab to the chosen text file with extension .tab.
	 */
	public boolean writeTabAs(File file) {
		boolean success = false;
		success = this.file.writeAs(file, getUIControllers());// give array to
																// FileHandler
		this.file.writeScore(getUIControllers());
		return success;
	}

	/**
	 * Returns the UIControllers.
	 * 
	 * @return ArrayList<UIController>
	 */
	public ArrayList<UIController> getUIControllers() {
		return measure;
	}

	/**
	 * Converts the index value to the desired naumber of strings.
	 * 
	 * @return int - number of strings
	 */
	private int getActualNumberOfStrings(int indexValue) {
		int s = -1;

		switch (indexValue) {
		case 0:
			s = 4;
			break;
		case 1:
			s = 5;
			break;
		case 2:
			s = 6;
			break;
		case 3:
			s = 7;
			break;
		default:
			s = 6;
			break;
		}
		return s;
	}

	/**
	 * Returns the number of tabs in the tabbed pane.
	 * 
	 * @return int - number of tabs
	 */
	public int getTabCount() {
		return this.tabbedPane.getTabCount();
	}

	/**
	 * Returns the instance of JTabbedPane.
	 * 
	 * @return JTabbedPane
	 */
	public JTabbedPane getTabbedPane() {
		return this.tabbedPane;
	}

	/**
	 * Returns the number of columns used in the tab.
	 * 
	 * @param measure
	 * @return int
	 */
	public int getFrets(int measure) {
		return getTablatureUIFromMeasure(measure).getColumns();
	}

	/**
	 * Validates all the UIs under it.
	 */
	public void validateAll() {
		try {
			tabbedPane.validate();
			for (int i = 0; i < getTabCount(); i++) {
				measure.get(i).updateTransposeUI();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// do nothing. there are no strings. program has just started.
		}
	}

	/**
	 * Sets the input for the tab.
	 * 
	 * @param int - fret
	 * @return boolean
	 */
	public boolean setFretNote(int fret) {
		for (int i = 1; i <= getNumberOfStrings(selectedTab); i++) {// current
																	// string

			for (int f = 1; f < getFrets(selectedTab); f++) {// current
																// column/fret
				boolean selected = getTablatureUIFromMeasure(selectedTab)
						.checkSelected(i, f);

				if (selected) {
					boolean valid = getTablatureUIFromMeasure(selectedTab)
							.setNote(i, f, fret);

					if (!valid) {
						return false;
					}
				} else {
					// do nothing
				}
			}
		}
		return true;
	}

	/**
	 * Return the index of the selected tab.
	 * 
	 * @return int - index
	 */
	public int getSelectedTab() {
		return selectedTab;
	}

	/**
	 * Returns the number of strings for the measure.
	 * 
	 * @param measure
	 * @return int - strings
	 */
	public int getNumberOfStrings(int measure) {
		return this.getTablatureUIFromMeasure(measure).getStrings();
	}

	@Override
	public void addObserver(Observer o) {
		this.observer = o;
	}

	@Override
	public void notifyObservers() {
		observer.update();
	}

	/**
	 * Returns whether or not a key was entered.
	 * 
	 * @return boolean
	 */
	// public boolean getKeyEntered(){
	// TablatureUI tablature =
	// storage.getTablatureUIFromMeasure(getSelectedTab());
	// return tablature.getKeyEntered();
	// }
	@Override
	public void removeObserver(Observer o) {
		this.observer = null;
	}

	/**
	 * Returns the name of the tab.
	 * 
	 * @return String - Tab name.
	 */
	public String getTabName() {
		return tabName;
	}

	/**
	 * Returns the name of the measure.
	 * 
	 * @param int - Index of measure.
	 * @return String - Measure name.
	 */
	public String getMeasureName(int index) {
		return measure.get(index).getName();
	}

	// /**
	// * Sets the number of strings for a measure.
	// * @param numberOfStrings
	// */
	// public void setStrings(int measure, int numberOfStrings){
	// storage.setStrings(measure, numberOfStrings);
	// }

	/**
	 * Sets the number of frets for a measure.
	 * 
	 * @param numberOfStrings
	 */
	public void setFrets(int measure, int frets) {
		this.getTablatureUIFromMeasure(measure).setColumns(frets);
	}

	/**
	 * Sets the note to the string and fret. Used for loading Tabs.
	 * 
	 * @param measure
	 * @param string
	 * @param fret
	 * @param note
	 * @return
	 */
	public boolean loadNote(int measure, int string, int fret, String note) {
		return getTablatureUIFromMeasure(measure).loadNote(string, fret, note);
	}

	/**
	 * Clears all attributes for deconstruction.
	 */
	private void close() {
		measure.clear();
		tabName = "Untitled";

		tabbedPane.removeAll();
	}

	/**
	 * Returns the file extension used for saving tabs.
	 * 
	 * @return the SAVE_EXTENSION
	 */
	public String getSAVE_EXTENSION() {
		return this.file.getSAVE_EXTENSION();
	}

	/**
	 * Returns the file extension used for writing tabs.
	 * 
	 * @return the WRITE_EXTENSION
	 */
	public String getWRITE_EXTENSION() {
		return this.file.getWRITE_EXTENSION();
	}

	/**
	 * Gets the backgroung color of the header.
	 */
	public Color getHeaderBackground() {
		if (parent != null) {
			return this.parent.getHeaderBackground();
		} else {
			return this.parentApplet.getHeaderBackground();
		}
	}

	/**
	 * Returns the tuning for the selected preset.
	 * 
	 * @return String[]
	 */
	public String[] getPreset() {
		if (parent != null) {
			return this.parent.getPreset();
		} else {
			return this.parentApplet.getPreset();
		}
	}

	/**
	 * Sets the currently selected tab.
	 */
	public void updateSelectedTab() {
		this.selectedTab = tabbedPane.getSelectedIndex();
	}

	/**
	 * Updates the current user input.
	 */
	public void updateInput(String input) {
		if (parent != null) {
			parent.updateInput(input);
		} else {
			parentApplet.updateInput(input);
		}
	}

	@Override
	public int getType() {
		return -1;
	}

	/**
	 * Returns the string of lyrics for the selected measure.
	 * 
	 * @param measure
	 * @return String lyrics
	 */
	public String getLyrics(int measure) {
		return getLyricUIFromMeasure(measure).getText();
	}

	/**
	 * Sets the lyrics.
	 * 
	 * @param text
	 *            lyrics
	 */
	public void setLyrics(int measure, String text) {
		getLyricUIFromMeasure(measure).setText(text);
	}

	/**
	 * Returns the TablatureUI from the selected measure.
	 * 
	 * @param measure
	 * @return TablatureUI
	 */
	public TablatureUI getTablatureUIFromMeasure(int measure) {
		return this.measure.get(measure).getTablatureUI();
	}

	/**
	 * Returns the LyricUI from the selected measure.
	 * 
	 * @param measure
	 * @return LyricUI
	 */
	public LyricUI getLyricUIFromMeasure(int measure) {
		return this.measure.get(measure).getLyricUI();
	}

	/**
	 * Returns the Tablature text UI.
	 * 
	 * @return
	 */
	public JTextArea getTablatureTextUI(int measure) {
		return this.measure.get(measure).getTablatureTextUI();
	}

	/**
	 * Returns the TuningUI from the selected measure.
	 * 
	 * @param measure
	 * @return
	 */
	public TuningUI getTuningUI(int measure) {
		return this.measure.get(measure).getTuningUI();
	}

	/**
	 * Returns the TransposeUI from the selected measure.
	 * 
	 * @param measure
	 * @return TransposeUI
	 */
	public TransposeUI getTransposeUIFromMeasure(int measure) {
		return this.measure.get(measure).getTransposeUI();
	}

	/**
	 * Sets the font.
	 * 
	 * @param fontType
	 */
	public void setFont(int fontType) {
		this.fontType = fontType;
		for (int i = 0; i < measure.size(); i++) {
			this.measure.get(i).setFont(fontType);
		}
	}

	/**
	 * Returns the font type.
	 * 
	 * @return
	 */
	public int getFontType() {
		return this.fontType;
	}

	/**
	 * Returns the StaffUI from the selected measure.
	 * 
	 * @param measure
	 * @return StaffUI
	 */
	public StaffUI getStaffUIFromMeasure(int measure) {
		return this.measure.get(measure).getStaffUI();
	}
}