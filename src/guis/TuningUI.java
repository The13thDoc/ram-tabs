package guis;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import manuscript.Manuscript;

import dualKeyMap.DualKeyMap;

import observerPattern.Notifier;
import observerPattern.Observer;
import scale.Scale;

/**
 * Stores all tuning information and UI for a measure.
 * @author RAM
 * @version 1.0
 * @since 11-Apr-2010 3:33:33 AM
 * 
 * @version 1.8
 * @since 08/31/2010
 * Now gives the user the option to display the tuning
 * when the Tab is written.
 * 
 * @version 2.0
 * @since 04/18/2011
 * - Set to update upon changing of tuning.
 */
@SuppressWarnings("serial")
public class TuningUI extends JPanel implements Notifier {

	/**
	 * Holds the JComboBox for each string.
	 */
	private DualKeyMap<Integer, JComboBox> combos;
	/**
	 * Waits for updates.
	 */
	private Observer observer;
	/**
	 * Contains the tuning for the measure.
	 * <string number, note>
	 */
	private DualKeyMap<Integer, String> tuning;
	
	private DualKeyMap<Integer, String> abc4jTuning;
	/**
	 * Number of strings.
	 */
	private int strings;
	/**
	 * Handles events.
	 */
	private TuningUIEventHandler event;
	private final int ID = UIController.TUNING_UI;
	private boolean displayTuning;;

	/**
	 * Constructor.
	 * 
	 * @param strings
	 */
	public TuningUI(int strings, Manuscript man){
		combos = new DualKeyMap<Integer, JComboBox>();
		tuning = new DualKeyMap<Integer, String>();
		abc4jTuning = new DualKeyMap<Integer, String>();
		event = new TuningUIEventHandler(this);

		this.strings = strings;
		init(man);
		setAbc4jTuning();
	}

	/**
	 * Constructor.
	 * 
	 * @param preset
	 */
	public TuningUI(String[] preset, Manuscript man){
		combos = new DualKeyMap<Integer, JComboBox>();
		tuning = new DualKeyMap<Integer, String>();
		abc4jTuning = new DualKeyMap<Integer, String>();

		this.strings = preset.length;
		init(man);
		setTuning(preset);
		setAbc4jTuning();
	}

	/**
	 * Initialize the UI.
	 */
	private void init(Manuscript man){
		setBackground(man.getHeaderBackground());
		setLayout(new FlowLayout(FlowLayout.LEADING));

		for(int i = strings; i > 0; i--){
			JComboBox combo = new JComboBox();

			combo.setPreferredSize(new Dimension(50, 30));
			combo.addActionListener(event);
			combo.setEnabled(true);

			ArrayList<String> notes = Scale.defaultScale.getMapB();

			for (int n = 0; n < notes.size(); n++) {
				combo.addItem(notes.get(n));
			}

			add(combo);
			combos.put(i, combo);
		}
		
		JCheckBox checkBoxTuning = new JCheckBox();
		checkBoxTuning.setEnabled(true);
		checkBoxTuning.setSelected(false);
		checkBoxTuning.addActionListener(event);
		checkBoxTuning.setBackground(man.getHeaderBackground());
		add(checkBoxTuning);
		
		setTuning(man.getPreset());
		setLoadComplete(true);
	}

	/**
	 * Returns the note on every string.
	 */
	public String getTuning(){
		String tuning = "";

		for(int i = strings; i > 0; i--){
			if(i == 1){//if last string (first index)
				tuning += this.tuning.getValue(i);
			}else{
				tuning += this.tuning.getValue(i) + ", ";
			}
		} 
		return tuning;
	}

	/**
	 * Returns the note for the specified string.
	 * 
	 * @param string
	 */
	public String getTuning(int string){
		return this.tuning.getValue(string);
	}

	/**
	 * Returns the note, from abc4j's tuning, for the specified string.
	 * 
	 * @param string
	 */
	public String getAbc4jTuning(int string){
		return this.abc4jTuning.getValue(string);
	}
	
	/**
	 * Sets the note to the specified string.
	 * 
	 * @param note
	 * @param string
	 */
	public void setTuning(String note, int string){
		this.tuning.put(string, note);
		this.combos.getValue(string).setSelectedItem(note);//update UI
	}

	/**
	 * Sets the tuning relative to abc4j's scale. Currently,
	 * this will remain a default scale relative to standard,
	 * six string tuning.
	 * @param string
	 */
	private void setAbc4jTuning(){
		this.abc4jTuning.put(1, "e");
		this.abc4jTuning.put(2, "B");
		this.abc4jTuning.put(3, "G");
		this.abc4jTuning.put(4, "D");
		this.abc4jTuning.put(5, "A,");
		this.abc4jTuning.put(6, "E,");
		this.abc4jTuning.put(7, "B,");
	}
	
	/**
	 * Sets the tuning.
	 * @param preset
	 */
	public void setTuning(String[] preset){
		for(int i = 1; i <= strings; i++){
			if(strings == 4){//for bass
				int y = i + 2;
				this.tuning.put(i, preset[y]);//record new tuning
				this.combos.getValue(i).setSelectedItem(preset[y]);//update UI
			}else if(strings == 5){
				int y = i + 1;
				this.tuning.put(i, preset[y]);//record new tuning
				this.combos.getValue(i).setSelectedItem(preset[y]);//update UI
			}else{
				this.tuning.put(i, preset[i]);//record new tuning
				this.combos.getValue(i).setSelectedItem(preset[i]);//update UI
			}
		}
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
		this.observer = null;
	}

	/**
	 * Updates the tuning upon user change.
	 */
	public void updateTuning(){
		for(int i = 1; i <= combos.size(); i++){
			String note = this.combos.getValue(i).getSelectedItem().toString();
			tuning.put(i, note);
		}
	}

	@Override
	public int getType() {
		return ID;
	}

	/**
	 * Returns whether or not the tuning should be written
	 * in the Tab file.
	 * 
	 * @return displayTuning
	 */
	public boolean isDisplayTuning() {
		return displayTuning;
	}

	/**
	 * Sets whether or not the tuning should be written
	 * in the Tab file.
	 * 
	 * @param displayTuning the displayTuning to set
	 */
	public void setDisplayTuning(boolean displayTuning) {
		this.displayTuning = displayTuning;
	}
	
	/**
	 * Sets whether or not loading is complete.
	 * @param b
	 */
	public void setLoadComplete(boolean b){
		event.setLoadComplete(b);
	}
}