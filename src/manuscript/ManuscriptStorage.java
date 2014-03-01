package manuscript;

import guis.LyricUI;
import guis.TablatureUI;
import guis.TransposeUI;
import guis.TuningUI;
import guis.UIController;

import java.util.*;

/**
 * Holds all the information for a Tab (name, tuning, etc.).
 * @author RAM
 * @version 1.0
 * @created 04-Jan-2010 7:16:49 AM
 * 
 * @deprecated
 * @since 04/18/2011
 */
public class ManuscriptStorage {

	/**
	 * Contains the name of each measure.
	 */
	//    private ArrayList<String> measureNames;
	/**
	 * Name of the Tab.
	 */
	private String tabName;
	/**
	 * Contains the 'compenents' arrays. Each index corresponds to a tab
	 * on the JTabbedPane (each index is for a measure).
	 */
	private ArrayList<UIController> measure;

	/**
	 * Constructor.
	 */
	public ManuscriptStorage(){
		//	measureNames = new ArrayList<String>();
		measure = new ArrayList<UIController>();
		setTabName("Untitled");
	}

	/**
	 * Returns the name of the measure.
	 * @param int - Index of measure.
	 * @return String - Measure name.
	 */
	public String getMeasureName(int index) {
		return measure.get(index).getName();
	}

	/**
	 * Returns the number of measures in this tab.
	 * @return int
	 */
	public int getNumberOfMeasure(){
		return measure.size();
	}
	/**
	 * Moves the measure in the array.
	 * @param src the measure to move
	 * @param dst the index to move it to
	 */
	public void moveMeasure(int src, int dst){
		UIController cntrl = measure.get(src);
		measure.remove(src);
		measure.add(dst, cntrl);
	}
	/**
	 * Add a measure name.
	 * @param String - Name of the measure.
	 */
	public void setMeasureName(int index, String name) {
		this.measure.get(index).setName(name);
	}

	/**
	 * Adds the name of a measure to the array
	 * @param String - name
	 */
	//    public void addMeasureName(String name){
	//    	this.measureNames.add(name);
	//    }
	/**
	 * Returns the name of the tab.
	 * @return String - Tab name.
	 */
	public String getTabName() {
		return tabName;
	}

	/**
	 * Sets the name of the tab.
	 * @param String - Tab name.
	 */
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	/**
	 * Returns the number of strings for the measure.
	 * @param measure
	 * @return int - strings
	 */
	public int getNumberOfStrings(int measure){
		return this.getTablatureUIFromMeasure(measure).getStrings();
	}

	/**
	 * Sets the number of frets for a measure.
	 * @param numberOfStrings
	 */
	public void setFrets(int measure, int frets){
		this.getTablatureUIFromMeasure(measure).setColumns(frets);
	}

	/**
	 * Returns the UIControllers.
	 * @return ArrayList<UIController>
	 */
	public ArrayList<UIController> getUIControllers(){
		return measure;
	}
	/**
	 * Returns the number of frets for the measure.
	 * @param measure
	 * @return int - strings
	 */
	public int getFrets(int measure){
		return this.getTablatureUIFromMeasure(measure).getColumns();
	}

	/**
	 * Adds a new UIController to the array.
	 * @param UIController
	 */
	public void addToMeasure(UIController controller){
		this.measure.add(controller);
	}

	/**
	 * Removes an array of components from the measure array.
	 * @param int - index
	 */
//	public void removeFromMeasure(int index){
//		this.measure.remove(index);
//	}

	/**
	 * Returns the TablatureUI from the selected measure.
	 * @param measure
	 * @return TablatureUI
	 */
	public TablatureUI getTablatureUIFromMeasure(int measure){
		return this.measure.get(measure).getTablatureUI();
	}

	/**
	 * Returns the LyricUI from the selected measure.
	 * @param measure
	 * @return LyricUI
	 */
	public LyricUI getLyricUIFromMeasure(int measure){
		return this.measure.get(measure).getLyricUI();
	}

	/**
	 * Returns the TuningUI from the selected measure.
	 * @param measure
	 * @return
	 */
	public TuningUI getTuningUI(int measure){
		return this.measure.get(measure).getTuningUI();
	}

	/**
	 * Returns the TransposeUI from the selected measure.
	 * @param measure
	 * @return TransposeUI
	 */
	public TransposeUI getTransposeUIFromMeasure(int measure){
		return this.measure.get(measure).getTransposeUI();
	}

	//    /**
	//     * Returns the StaffUI from the selected measure.
	//     * @param measure
	//     * @return StaffUI
	//     */
	//    public StaffUI getStaffUIFromMeasure(int measure){
	//	return this.measure.get(measure).getStaffUI();
	//    }

	/**
	 * Clears all attributes for deconstruction.
	 */
	public void close(){
		this.measure.clear();
		//	this.measureNames.clear();
		this.tabName = "Untitled";
	}

	/**
	 * Deletes the information corresponding to the
	 * selected measure.
	 * @param measure
	 */
	public void delete(int measure){
		this.measure.remove(measure);
		//	measureNames.remove(measure);
	}

	/**
	 * Refreshes all UIs for the selected measure.
	 * @param index
	 */
	public void validateAll(int index){
		this.measure.get(index).update();
	}
}