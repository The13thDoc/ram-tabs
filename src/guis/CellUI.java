package guis;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import observerPattern.Notifier;
import observerPattern.Observer;

@SuppressWarnings("serial")
public class CellUI extends JList implements Notifier, Observer{

	/**
	 * Observer.
	 */
	private CellUI observer;
	/**
	 * ID (TablatureUI or TransposeUI).
	 */
	public int ID;
	/**
	 * String number.
	 */
	public int string;
	/**
	 * Number of frets.
	 */
	public int frets;
	/**
	 * Receives events for CellUI.
	 */
	private CellUIEventHandler event;

	/**
	 * Constructor.
	 */
	public CellUI(){
		super();

		event = new CellUIEventHandler(this);

		this.addMouseListener(event);
	}

	/**
	 * Sets the note.
	 * @param String - note
	 * @return boolean - valid
	 */
	public boolean setNote(String note){
		DefaultListModel model = new DefaultListModel();
		model.addElement(note);
		setModel(model);

		return true;
	}

	/**
	 * Gets the note/symbol.
	 * @return String - note/symbol
	 */
	public String getNote(){
		return this.getModel().getElementAt(0).toString();
	}
	
	/**
	 * Sets the string number.
	 * @param string
	 */
	public void setString(int string){
		this.string = string;
	}

	/**
	 * Returns the string number.
	 * @return
	 */
	public int getString(){
		return this.string;
	}
	
	/**
	 * Sets the number of frets.
	 * @param frets
	 */
	public void setFrets(int frets){
		this.frets = frets;
	}
	
	/**
	 * Returns the number of frets.
	 * @return
	 */
	public int getFrets(){
		return frets;
	}
	
//	/**
//	 * Updates TransposeUI with information from TablatureUI.
//	 */
//	public void updateTransposeUI(){
//		String note = observer.getModel().getElementAt(0).toString();
//		String fretNote = "";
//		int fret = -1;
//
//		try{
//			fret = Integer.parseInt(note);//get the fret,if one exists, from the column
//		}catch(NumberFormatException e){
//			//the fret is blank or "--|"
//			if(note.equals("--|")){
//				fretNote = "--|";
//			}else{
//				fretNote = "---";
//			}
//			//			if(staff != null){
//			//				staff.setNotesStandard(" ", f, i);//Set the standard note in TransposeUI
//			//			}
//		}
//		fretNote = Scale.transposeNote(note, fret, Scale.defaultScale);//transpose the note
//		//		abc4jFretNote = Scale.transposeABC4JNote(abc4jNote, fret, Scale.abc4jTrebleScale);//transpose the note
//		//		if(staff != null){
//		//			staff.setNotesStandard(abc4jFretNote, f, i);//Set the standard note in TransposeUI
//		//		}
//		setNote(fretNote);//Set the actual note in TransposeUI
//
//	}

//	/**
//	 * Updates TablatureUI with information from TransposeUI.
//	 */
//	public void updateTablatureUI(){
//		try{
//			Integer fretNote = -1;
//			String stringNote = UIController.tuning.getTuning(string);//Get the current note the string is tuned to from TablatureUI
//
//
//			String note = observer.getNote();//get the fret,if one exists, from the column
//
//			if(note == "-1"){
//				//the fret is blank
//				setNote(note);//Set the actual note in TablatureUI
//			}else{
//				//						transposedFrets = Scale.transposeFrets(stringNote, trans.getNote(), tab.getFrets(), Scale.defaultScale);
//				fretNote = Scale.transposeFrets(stringNote, note, frets, Scale.defaultScale).get(0);
//				setNote(fretNote.toString());//Set the actual note in TablatureUI
//			}
//		}catch(ArrayIndexOutOfBoundsException e){
//			e.printStackTrace();
//		}catch(NumberFormatException e){
//			e.printStackTrace();
//		}catch(NullPointerException e){
//			e.printStackTrace();
//		}
//	}

	@Override
	public void addObserver(Observer o) {
		this.observer = (CellUI)o;
	}

	@Override
	public void removeObserver(Observer o) {
		this.observer = null;
	}

	@Override
	public void notifyObservers() {
		this.observer.update(this);
	}

	/**
	 * Sets the UI type.
	 * @param type
	 * @return
	 */
	public void setType(int type){
		ID = type;
	}

	@Override
	public int getType() {
		return ID;
	}

	@Override
	public void update(Notifier s) {
		int type = s.getType();

		switch(type){

		case (UIController.TABLTURE_UI):
//			updateTransposeUI();
		break;

		case (UIController.TRANSPOSE_UI):
//			updateTablatureUI();
		break;
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
}
