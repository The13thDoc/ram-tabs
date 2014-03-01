package guis;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerListModel;

import abc.notation.Tune;
import abc.parser.TuneParser;
import abc.ui.swing.JScoreComponent;

import observerPattern.Notifier;
import observerPattern.Observer;
import randomHelpers.RandomHelpers;

/**
 * Creates the GUI for a musical staff.
 * @author RAM
 * @version 1.0
 * @created 04-Jan-2010 7:16:49 AM
 * 
 * @version 2.2
 * @since 08/8/2011
 * - Updated updateNotes(). It now determines the length of the note
 * based on the amount of gaps after it.
 */
@SuppressWarnings("serial")
public class StaffUI extends JPanel implements Notifier {

	/**
	 * Receives events for StaffUI.
	 */
	private StaffUIEventHandler event;
	/**
	 * Constant for treble clef
	 */
	public static final int TREBLE = 1;
	/**
	 * Constant for bass clef
	 */
	public static final int BASS = 2;

	private Observer observer;
	private final int ID = UIController.STAFF_UI;

	protected JScoreComponent scoreUI;

	private Tune tune;

	private StringBuffer strTune;
	private String title = "";
	private String composer = "";
	private String notesStandard = "";//"D,E,D^D|E,E,FF|E,EEE,|E^DE,E,";
	private ArrayList<ArrayList<String>> standard;
	private String notesTransposed = "B,D,B,C|D,D,DD|D,^C^CD,|^CCD,D,";
	private String measure = "";
	private String m = "4/4";
	private String key = "C";
	/**
	 * "1/4" = quarter note
	 * "1/8" = eigth note
	 * ...etc.
	 */
	private String beatNote = "1/4";
	/**
	 * beatNote = beatPerNote
	 * 
	 * "1/4 = beatPerNote"
	 */
	private String beatPerNote = "127";

	private int columns;
	private int strings;

	protected JSpinner spinBeatPerNote;

	protected JFormattedTextField fieldBeat;

	private GridBagConstraints gridBag;

	private JTextArea text;

	private JPanel panelCenter;

	private JScrollPane scrollBar;

	/**
	 * Constructor. Accepts clef type.
	 * @param int - type (A StaffUI constant)
	 */
	public StaffUI(int type, String title, String measure, int columns, int strings){
		this.title = title;
		this.measure = measure;
		this.columns = columns;
		this.strings = strings;
		strTune = new StringBuffer("X:1\nT:" 
				+ /*title + */"\nC:" 
				+ composer + "\nR:"
				+ measure + "\nQ:" 
				+ beatNote + "=" 
				+ beatPerNote 
				+ "\nM:" 
				+ m 
				+ "\nK:" 
				+ key 
				+ "\n" 
				+ "|" 
				+ notesStandard 
				+ "|");
		gridBag = new GridBagConstraints();
		event = new StaffUIEventHandler(this);
		standard = new ArrayList<ArrayList<String>>();
		initializeNotes();

		switch(type){
		case TREBLE:
			//FINISH!!!............
			break;
		case BASS:
			//FINISH!!!............
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Initializes the array.
	 */
	private void initializeNotes(){
		standard.add(new ArrayList<String>());

		for(int fret = 1; fret <= columns; fret++){
			ArrayList<String> chord = new ArrayList<String>();
			chord.add("");

			for(int i = 1; i <= strings; i++){
				chord.add("");
			}
			standard.add(chord);
		}
	}

	/**
	 * Starts the GUI process.
	 */
	public void generateUI(){
		Color background = new Color(240,240,240);

		setBackground(background);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		/*
		 * ab4j UI
		 */
		{
			//		strTune = "X:1\nT:Migalhas de Amor\nC:Jacob do Bandolim\nR:Choro\nQ:1/4=80\nM:2/4\nK:Bb\nD2B|^F4-FGBd-|d4df2e|A4-ABce-|e2<d2z D2B|\n^F4-FGBd-|d4df2e|A4-ABce-|e2<d2z def-|(3f2f2f2fede|\ne4ed2c|(3d2d2d2dc2B|(3A2^C2=E (3A2G2A2|(3[G,2^F2]G2A2\\n(3[=B2D2]^c2d2|(3[=E2G,2]^F2G2 (3[A2^C2][G2=B2][^c=G]\n(3[^F2=e2]d2c2 AFDE-|E4 z D2B | ^F4-FGB[d-D-]|[d4D4]df2e|A4ABce|\ne2<d2zD2B|^F4 FGBd-|d4 df2d|A4-ABcd|d2>c2zdef|\n(3f2f2f2 (3f2e2d2|e4-(3e2d2c2 | (3d2d2d2dcBc-|(3c2_A,2E2 (3c2B2A2|(3[B2D2]A2G2 B4|\n(3B2A2G2(3A2G2^F2|A2<G2-G4 |:z FG^G Ad2A-|A4 AB2F | E2[=B,F,] ED2=B,|\n[DG,E,]C2=B, CDE[G-G,-]|[GG,]E2F Ge2A-|A4 Ac2A|(3G2D,2A,2 [GE2]B2A | (3G2F2=E2 (3F2D2B,2\n(3F2G2^G2 Ad2A-|A4 AB2F | (3E2F,2=B,2 ED2=B, | DC2=B, CEGd-|dc2d cBA[cD]\n[cD]B2d [BG,]AG[B-D,-]|[BD,]G2B AGFB-|1B8 :|2 B8 B,CD EDCB, | A,G,^F,E,D,";
			panelCenter = new JPanel();
			panelCenter.setLayout(new GridBagLayout());
			//			panelCenter.setLayout(new FlowLayout(FlowLayout.LEFT));

			GridBagConstraints gridBag = new GridBagConstraints();
			gridBag.gridx = 0;
			int count = 0;

			tune = new TuneParser().parse(strTune.toString());
			scoreUI = new JScoreComponent();
			scoreUI.setTune(tune);
			scoreUI.setVisible(true);

			text = new JTextArea(strTune.toString());

			gridBag.gridy = count;
			count ++;
			gridBag.insets = new Insets(5,5,5,5);
			panelCenter.add(scoreUI, gridBag);

			gridBag.gridy = count;
			count++;
			gridBag.insets = new Insets(5,5,5,5);
//			panelCenter.add(text, gridBag);

			//			panelCenter.add(scoreUI);
			//			panelCenter.add(text);

			scrollBar = new JScrollPane(panelCenter);
			scrollBar.setBorder(BorderFactory.createEtchedBorder());
			//	scrollBar.setPreferredSize(new Dimension(800,600));
			scrollBar.setVisible(true);
			scrollBar.setAutoscrolls(true);

			add(scrollBar, BorderLayout.CENTER);
		}
		initExtras();
	}

	/**
	 * Initializes the rest of the GUI.
	 */
	private void initExtras(){
		/*
		 * Upper panel
		 */
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setOpaque(false);

		add(panel, BorderLayout.NORTH);
		addMouseListener(event);

		/*
		 * JFormattedTextField
		 */
		{
			fieldBeat = new JFormattedTextField();
			fieldBeat.setVisible(true);
			fieldBeat.setPreferredSize(new Dimension(40,20));
			fieldBeat.setValue(beatNote);
			fieldBeat.addKeyListener(event);

			gridBag.gridx = 0;
			gridBag.gridy = 0;
			panel.add(fieldBeat, gridBag);
		}

		/*
		 * JSpinner
		 */
		{
			spinBeatPerNote = new JSpinner();
			spinBeatPerNote.setPreferredSize(new Dimension(40,20));
			spinBeatPerNote.addChangeListener(event);

			SpinnerListModel spinnerModel = new SpinnerListModel();
			ArrayList<Integer> model = new ArrayList<Integer>();

			//populate model
			for(int i = 1; i <= 300; i++){
				model.add(i);
			}
			//set model
			spinnerModel.setList(model);
			spinBeatPerNote.setModel(spinnerModel);
			try{
				spinBeatPerNote.setValue(Integer.parseInt(beatPerNote));
			}catch(NumberFormatException e){

			}
			setBeatPerNote(spinBeatPerNote.getValue().toString());

			gridBag.gridx = 1;
			gridBag.gridy = 0;
			panel.add(spinBeatPerNote, gridBag);
		}
	}

	public JScoreComponent getScoreComponet(){
		return this.scoreUI;
	}

	//	public void validateAll(){
	//		this.scoreUI.refresh();
	//		validate();
	//	}

	/**
	 * Sets the user input.
	 * 
	 * @param input
	 */
	public void setInput(String input){

	}

	@Override
	public void addObserver(Observer o) {
		this.observer = o;
	}

	@Override
	public void notifyObservers() {
		this.observer.update();
	}

	@Override
	public void removeObserver(Observer o) {
		//do nothing
	}

	@Override
	public int getType() {
		return ID;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the composer
	 */
	public String getComposer() {
		return composer;
	}

	/**
	 * @param composer the composer to set
	 */
	public void setComposer(String composer) {
		this.composer = composer;
	}

	/**
	 * @return the notes
	 */
	public String getNotesTransposed() {
		return notesTransposed;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotesTransposed(String notes) {
		this.notesTransposed = notes;
	}

	/**
	 * 
	 * @param notes Notes to append to the current defaultScale.
	 */
	public void appenedNotesTransposed(String notes){
		this.notesTransposed += notes;
	}

	/**
	 * @return the notes
	 */
	public String getNotesStandard() {
		return notesStandard;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotesStandard(String note, int fret, int string) {
		standard.get(fret).set(string, note);
		//		System.out.println(standard.get(fret));
		//		chord.set(string, note);
		//		this.standard.set(fret, chord);
		updateNotes();
		//		upDateStrTune();
	}

	/**
	 * Updates the string of notes with the current setup.
	 */
	private void updateNotes(){
		notesStandard = "";
		ArrayList<String> newNotes = new ArrayList<String>();
		newNotes.add(null);
		int count = 1;

		for(int fret = 1; fret < standard.size(); fret++){
			ArrayList<String> chord = standard.get(fret);
			String temp = "";
			String temp2 = "";

			for(int string = 1; string < chord.size(); string++){
				temp2 += standard.get(fret).get(string);
				temp2 = temp2.trim();
			}

			temp += "[" + temp2 + "]";
			if(temp2.length() == 0){
				//				notesStandard += " ";
				newNotes.add("");
				count++;
			}else{
				//				notesStandard += temp + count;
				try{
					newNotes.set((fret - count), (newNotes.get((fret - count)).trim() + count));
				}
				catch(NullPointerException e){
					//do nothing, the first index is null
				}
				catch(Exception e){
					e.printStackTrace();
				}
				newNotes.add(temp);
				count = 1;
			}
		}

		for(int i = 1; i < newNotes.size(); i++){
			notesStandard += newNotes.get(i);
		}
		notesStandard = RandomHelpers.trimRight(notesStandard, " ");
		//		System.out.println(notesStandard);
	}

	//	/**
	//	 * 
	//	 * @param notes Notes to append to the current notes.
	//	 */
	//	public void appenedNotesStandard(String notes){
	//		this.notesStandard += notes;
	//		upDateStrTune();
	//	}

	/**
	 * @return the measure
	 */
	public String getMeasure() {
		return measure;
	}

	/**
	 * @param measure the measure to set
	 */
	public void setMeasure(String measure) {
		this.measure = measure;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the beatNote
	 */
	public String getBeatNote() {
		return beatNote;
	}

	/**
	 * @param beatNote the beatNote to set
	 */
	public void setBeatNote(String beatNote) {
		this.beatNote = beatNote;
	}

	/**
	 * @return the beatPerNote
	 */
	public String getBeatPerNote() {
		return beatPerNote;
	}

	/**
	 * @param beatPerNote the beatPerNote to set
	 */
	public void setBeatPerNote(String beatPerNote) {
		this.beatPerNote = beatPerNote;
	}

	/**
	 * @return the strTune
	 */
	public StringBuffer getStrTune() {
		return strTune;
	}

	/**
	 * @param strTune the strTune to set
	 */
	public void upDateStrTune() {
		panelCenter.remove(scoreUI);
//		panelCenter.remove(text);

		this.strTune = new StringBuffer("X:1\nT:" + /*title +*/ "\nC:" + composer + "\nR:"+ measure + "\nQ:" + beatNote + "=" + beatPerNote + "\nM:" + key + "\n" + "|" + notesStandard + "|");

		text.setText(strTune.toString());

		tune = new TuneParser().parse(strTune.toString());
		scoreUI = new JScoreComponent();
		scoreUI.setTune(tune);
		scoreUI.setVisible(true);

		panelCenter.add(scoreUI);
//		panelCenter.add(text);
	}
}