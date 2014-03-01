package guis;

import java.util.*;

import javax.swing.*;

import java.awt.*;

import observerPattern.Notifier;
import observerPattern.Observer;
import scale.Scale;
/**
 * Generates the GUI that displays all the transposed defaultScale
 * from the tablature.
 * @author main
 * @version 1.0
 * @since 02-March-2010 6:10:00 AM
 * 
 * @version 1.8
 * @since 08/31/2010
 * 
 * TransposeUI is now editable.
 *
 */
@SuppressWarnings("serial")
public class TransposeUI extends JPanel implements Notifier{

	/**
	 * Contains the arrays that hold the JLists.
	 * Each index corresponds to a string (based on index 1).
	 */
	private ArrayList<ArrayList<JList>> arrayStrings;
	/**
	 * Number of strings being used.
	 */
	private int strings;
	/**
	 * Number of columns being used.
	 * Defaulted to 32.
	 */
	private int columns = 32;
	/**
	 * Number of frets being used.
	 * Defaulted to 24.
	 */
	private int frets = 24;
	private int fontSize;
	private Dimension cellSize;
	/**
	 * Receives events for TransposeUI.
	 */
	private TransposeUIEventHandler event;
	private Observer observer;
	private final int ID = UIController.TRANSPOSE_UI;
	private UIController parent;

	/**
	 * Constructor.
	 * @param int - strings
	 */
	public TransposeUI(UIController parent, int strings){
		this.parent = parent;
		this.strings = strings;
		event = new TransposeUIEventHandler(this);
		arrayStrings = new ArrayList<ArrayList<JList>>(strings + 1);
		arrayStrings.add(null);//fill the 0 index with a null
	}

	/**
	 * Constructor.
	 * @param int - strings
	 * @param int - columns
	 */
	public TransposeUI(UIController parent, int strings, int frets){
		this.parent = parent;
		this.strings = strings;
		this.columns = frets;
		event = new TransposeUIEventHandler(this);
		arrayStrings = new ArrayList<ArrayList<JList>>(strings + 1);
		arrayStrings.add(null);//fill the 0 index with a null
	}

	/**
	 * Generates the GUI.
	 */
	public void generateGUI(){
		Color background = new Color(240,240,240);
		setBackground(background);
		setLayout(new FlowLayout(FlowLayout.LEADING));
		addKeyListener(event);
		addMouseListener(event);

		for(int i = 1; i <= strings; i++){
			ArrayList<JList> arrayFrets = new ArrayList<JList>();

			for(int z = 0; z <= columns + 1; z++){
				DefaultListModel model = new DefaultListModel();

				JList fret = new JList();
				fret.addKeyListener(event);
				fret.addMouseListener(event);
				fret.setFixedCellHeight((int)cellSize.getHeight());
				fret.setFixedCellWidth((int)cellSize.getWidth());
				fret.setFont(new Font("Courier New", Font.BOLD, this.fontSize));
				fret.setBackground(background);
				
				add(fret);
				arrayFrets.add(fret);

				if(z == 0){
					model.addElement("|--");
					fret.setEnabled(false);
				}else if(z == columns + 1){
					model.addElement("--|");
					fret.setEnabled(false);
				}else{
					model.addElement("---");//3 spaces == '--'
					fret.setEnabled(true);
				}
				fret.setModel(model);

			}
			arrayStrings.add(arrayFrets);
		}
	}

	/**
	 * Sets the note to the string and fret.
	 * @param int desired string
	 * @param int desired fret
	 * @param String desired note
	 */
	public void setNote(int string, int fret, String note){
		DefaultListModel model = new DefaultListModel();
		model.addElement(note);
		arrayStrings.get(string).get(fret).setModel(model);
		arrayStrings.get(string).get(fret).setToolTipText(getAvailableFrets(string, note));

		validate();
	}

	/**
	 * Gets all available frets the note can be played on for that string and tuning.
	 * @param string
	 * @param note
	 * @return string of frets.
	 */
	private String getAvailableFrets(int string, String note){
		String allFrets = "";
		ArrayList<Integer> frets = Scale.transposeFrets(parent.getTuningUI().getTuning(string), note, getFrets(), Scale.defaultScale);
		for(int i = 0; i < frets.size(); i++){
			if(i != (frets.size() - 1)){
				allFrets += frets.get(i) + ", ";
			}else{
				allFrets += frets.get(i);
			}
		}
		return allFrets;
	}
	
	/**
	 * Sets the user input to the selected indices.
	 * @param String input
	 * @return indication of successful input
	 */
	public boolean setInput(String input){
		if(Scale.defaultScale.getMapB().contains(input) || input.equals("-1") | input.equals("--|")){
			//good to go
		}
		else{
			return false;
		}
		for(int i = 1; i <= strings; i++){//current string
			for(int f = 1; f <= columns; f++){//current column/fret
				boolean nothingSelected = checkSelected(i, f);

				if(!nothingSelected){
					if(input.equals("-1")){
						setNote(i, f, "---");
					}else{
						setNote(i, f, input);
					}
				}
			}
		}
		return true;
	}

	/**
	 * Returns the note from the string and fret.
	 * @param int - string
	 * @param int - fret
	 * @return String - note
	 */
	public String getNote(int string, int fret){
		return arrayStrings.get(string).get(fret).getModel().getElementAt(0).toString();
	}

	/**
	 * Returns the number of strings.
	 * @return int - strings
	 */
	public int getStrings(){
		return this.strings;
	}

	/**
	 * Returns the number of columns.
	 * @return int - columns
	 */
	public int getColumns(){
		return this.columns;
	}

	/**
	 * Sets the number of strings.
	 * @param int - strings
	 */
	public void setStrings(int strings){
		this.strings = strings;
	}

	/**
	 * Sets the number of columns.
	 * @param int - columns
	 */
	public void setColumns(int frets){
		this.columns = frets;
	}

	/**
	 * Returns the number of frets.
	 * @return int - frets
	 */
	public int getFrets(){
		return this.frets;
	}

	/**
	 * Sets the number of frets.
	 * @param int - frets
	 */
	public void setFrets(int frets){
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
		//do nothing
	}

	/**
	 * Returns the font size.
	 * @return int
	 */
	public int getFontSize(){
		return this.fontSize;
	}

	/**
	 * Sets the font size.
	 * @param size
	 */
	public void setFontSize(int size){
		this.fontSize = size;
	}

	/**
	 * Returns the JList cell size.
	 * @return Dimension
	 */
	public Dimension getCellSize(){
		return this.cellSize;
	}

	/**
	 * Sets the JList cell size.
	 * @param size
	 */
	public void setCellSize(Dimension size){
		this.cellSize = size;
	}

	/**
	 * Determines if the JList is selected.
	 * @param string - desired guitar string
	 * @param fret - desired fret
	 * @return boolean - nothing selected
	 */
	public boolean checkSelected(int string, int fret){
		return arrayStrings.get(string).get(fret).isSelectionEmpty();
	}

	/**
	 * Returns the current user input.
	 * @return String - input
	 */
	public String getInput(){
		return event.getInput();
	}

	@Override
	public int getType() {
		return ID;
	}

	/**
	 * Returns the currently selected note.
	 * @return String - note
	 */
	public String getNote(){
		return event.getNote();
	}
}
