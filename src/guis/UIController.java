package guis;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import observerPattern.Notifier;
import observerPattern.Observer;
import scale.Scale;
import main.OsUtils;
import manuscript.Manuscript;

/**
 * Controls a single group of UIs.
 * @author RAM
 * @version 1.0
 * @since 15-Apr-2010 3:58:26 AM
 * 
 * @version 1.8
 * @since 08/31/2010
 * - Allows the TransposeUI to alter the TablatureUI, as
 * well as display possible frets belonging to a note.
 * 
 * @version 1.9
 * @since 01/11/2011
 * - LyricUI added.
 * 
 * @version 2.0
 * @since 04/18/2011
 * - Creation of the panel that holds each UI was moved from
 * Manuscript into here.
 * - TuningUI set to update upon changing of tuning.
 */
public class UIController implements Observer {

	/**
	 * Instance of Manuscript.
	 */
	private Manuscript parent;
	/**
	 * Instance of StaffUI.
	 */
	private StaffUI staff;
	/**
	 * Instance of TablatureUI.
	 */
	private TablatureUI tab;
	/**
	 * Instance of LyricUI.
	 */
	private LyricUI lyric;
	/**
	 * Instance of transposeUI.
	 */
	private TransposeUI trans;
	/**
	 * Instance of TuningUI.
	 */
	private TuningUI tuning;
	/**
	 * Number of strings used.
	 */
	private int strings;
	/**
	 * Name of this UI(the measure)
	 */
	private String name;
	/**
	 * Dimension shared by all the components. Determined by the number 
	 * of strings being used.
	 */
	private Dimension size;
	private int fontSize = 14;
	private Dimension cellSize = new Dimension(26,22);
	//	private ArrayList<Integer> transposedFrets;
//	private JSplitPane splitPane;
	private JTextArea tablatureText;
	/**
	 * Contains each UI. Inserted into the JTabbedPane.
	 */
	private JPanel panel;

	/*
	 * Static IDs for UIs.
	 */
	public static final int TABLTURE_UI = 0;
	public static final int STAFF_UI = 1;
	public static final int TRANSPOSE_UI = 2;
	public static final int TUNING_UI = 3;
	public static final int LYRIC_UI = 4;

	private int fontType = OsUtils.CURRENT_OS;

	/**
	 * Constructor.
	 * 
	 * @param strings
	 */
	public UIController(int strings, Manuscript man, Dimension size, String name) {
		this.parent = man;
		this.strings = strings;
		this.size = size;
		this.name = name;
		//		transposedFrets = new ArrayList<Integer>();
	}

	/**
	 * Initialize the UI.
	 */
	public void generateUI(){
		fontType = parent.getFontType();

		tuning = new TuningUI(strings, parent);

		CellGenerator cells = new CellGenerator(this, strings);
		cells.setFontSize(fontSize);
		cells.setCellSize(cellSize);
		cells.generateUI();

		tab = new TablatureUI(strings);
		cells.setTabParent(tab);
		tab.setPreferredSize(size);//sets the size for the window PANEL
		//		tab.setFontSize(fontSize);
		//		tab.setCellSize(cellSize);
		tab.setTabModel(cells.getTabModel());
		tab.generateUI();

		trans = new TransposeUI(this, strings);
		trans.setPreferredSize(size);//sets the size for the window PANEL
		trans.setFontSize(fontSize);
		trans.setCellSize(cellSize);
		trans.generateGUI();

		lyric = new LyricUI();
		lyric.setPreferredSize(new Dimension((int)size.getWidth(), 200));//sets the size for the window PANEL
		lyric.setFontSize(fontSize);
		lyric.generateUI();
		lyric.setVisible(false);

		tablatureText = new JTextArea(getTablatureText());
		tablatureText.setPreferredSize(new Dimension((int)size.getWidth(), (int)size.getHeight()*6/10));//sets the size for the window PANEL
		//		tablatureText.setFont(new Font("Courier New", Font.PLAIN, this.fontSize));
		//		tablatureText.setFont(new Font("Courier 10 Pitch", Font.PLAIN, this.fontSize));//compatible with linux
		setFont(fontType);
		tablatureText.setEditable(false);
		tablatureText.setVisible(false);

//		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, trans, tablatureText);
//		//		splitPane.setPreferredSize(new Dimension((int)size.getWidth(), (int)size.getHeight()*5/3));
//		splitPane.setPreferredSize(new Dimension((int)size.getWidth(), (int)size.getHeight()+ 4));//sets the size for the window PANEL
//		//		splitPane.setDividerLocation(0);
//		splitPane.setDividerLocation(370);//sets the tabText UI closed upon creation
//		splitPane.setResizeWeight(1);
//		splitPane.setOneTouchExpandable(true);


		staff = new StaffUI(StaffUI.TREBLE, parent.getTabName(), name,
				tab.getColumns(), strings);

//		staff.setPreferredSize(size);
		staff.generateUI();
		staff.setVisible(false);

		addObservers();
		/*
		 * Add UIs to one panel to be inserted into the JTabedPane
		 */
		{
			panel = new JPanel();
			//		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
			panel.setLayout(new GridBagLayout());
			//	panel.setPreferredSize(new Dimension((int)size.getWidth(), height));//with StaffUI
			//		panel.setPreferredSize(new Dimension((int)size.getWidth(), height));//without StaffUI
			panel.setVisible(true);
			panel.setBackground(new Color(150, 11, 11));

			GridBagConstraints gridBag = new GridBagConstraints();
			gridBag.gridx = 1;
			int count = 0;

			gridBag.gridy = count;
			count++;
			gridBag.insets = new Insets(5,0,0,0);
			panel.add(tab, gridBag);

			gridBag.gridy = count;
			count++;
			gridBag.insets = new Insets(0,0,0,0);
			panel.add(trans, gridBag);
			
			gridBag.gridy = count;
			count++;
			gridBag.insets = new Insets(0,0,0,0);
			panel.add(tablatureText, gridBag);

			gridBag.gridy = count;
			count++;
			panel.add(lyric, gridBag);

			if(staff != null){
				gridBag.gridy = count;
				count++;
				panel.add(staff, gridBag);
			}
		}
	}

	/**
	 * Returns the main JPanel that contians each UI.
	 * @return
	 */
	public JPanel getUI(){
		return panel;
	}

//	/**
//	 * Return the split pane containing the StaffUI
//	 * and text area.
//	 */
//	public JSplitPane getSplitPane(){
//		return splitPane;
//	}
	
	/**
	 * Returns the tablatureUI's contents as it would be written to a tab.
	 * @return String
	 */
	public String getTablatureText(){
		String fretNote = "";
		String section = "";
		for(int i = 1; i <= strings; i++){//current string
			for(int f = 0; f <= tab.getColumns() + 1; f++){//current column/fret
				section = tab.getNote(i, f);

				if(section.length() == 1){
					section+= "--";
				}
				if(section.length() == 2){
					section+= "-";
				}
				fretNote += section;
			}
			if(i != strings){
				fretNote +="\n";
			}
		}
		return fretNote;
	}

	/**
	 * Updates the tablture text.
	 */
	public void updateTablatureText(){
		String fretNote = "";
		String section = "";
		for(int i = 1; i <= strings; i++){//current string
			for(int f = 0; f <= tab.getColumns() + 1; f++){//current column/fret
				section = tab.getNote(i, f);

				if(section.length() == 1){
					section+= "--";
				}
				if(section.length() == 2){
					section+= "-";
				}
				fretNote += section;
			}
			if(i != strings){
				fretNote +="\n";
			}
		}
		tablatureText.setText(fretNote);
		tablatureText.validate();
	}

	/**
	 * Adds itself as an Observer to each UI.
	 */
	public void addObservers(){
		tab.addObserver(this);
		lyric.addObserver(this);
		trans.addObserver(this);
		tuning.addObserver(this);

		if(staff != null){
			staff.addObserver(this);
		}
	}

	/**
	 * Returns StaffUI.
	 */
	public StaffUI getStaffUI(){
		return this.staff;
		//		throw new UnsupportedOperationException();
	}

	/**
	 * Returns TablatureUI.
	 */
	public TablatureUI getTablatureUI(){
		return this.tab;
	}

	/**
	 * Returns LyricUI.
	 */
	public LyricUI getLyricUI(){
		return this.lyric;
	}

	/**
	 * Returns TransposeUI.
	 */
	public TransposeUI getTransposeUI(){
		return this.trans;
	}

	/**
	 * Returns TuningUI.
	 */
	public TuningUI getTuningUI(){
		return this.tuning;
	}
	
	/**
	 * Returns the Tablature text UI.
	 * @return
	 */
	public JTextArea getTablatureTextUI(){
		return this.tablatureText;
	}

	/**
	 * Sets the name of this measure.
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
		if(staff != null){
			staff.setMeasure(name);
		}
	}

	/**
	 * Gets the name of this measure.
	 * @return String
	 */
	public String getName(){
		return this.name;
	}
	/**
	 * Updates StaffUI.
	 */
	public void updateStaffUI(){

	}

	/**
	 * Updates TablatureUI with information from TransposeUI.
	 */
	public void updateTablatureUI(){
		try{
			for(int i = 1; i <= strings; i++){//current string
				int fretNote = -1;
				String stringNote = tuning.getTuning(i);//Get the current note the string is tuned to from TablatureUI

				for(int f = 1; f < tab.getColumns(); f++){//current column/fret
					String note = trans.getNote(i, f);//get the fret,if one exists, from the column

					if(note == "-1"){
						//the fret is blank
						tab.setNote(i, f, note);//Set the actual note in TablatureUI
					}else{
						//						transposedFrets = Scale.transposeFrets(stringNote, trans.getNote(), tab.getFrets(), Scale.defaultScale);
						fretNote = Scale.transposeFrets(stringNote, note, tab.getFrets(), Scale.defaultScale).get(0);
						tab.setNote(i, f, fretNote);//Set the actual note in TablatureUI
					}

				}
			}
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}catch(NumberFormatException e){
			e.printStackTrace();
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}

	/**
	 * Updates TransposeUI with information from TablatureUI.
	 */
	public void updateTransposeUI(){
		try{
			for(int i = 1; i <= strings; i++){//current string
				String fretNote = "";
				String abc4jFretNote = "";
				String note = tuning.getTuning(i);//Get the current note the string is tuned to from TabApp
				String abc4jNote = tuning.getAbc4jTuning(i);//Get the current note (abc4j) the string is tuned to from TabApp
				for(int f = 1; f < tab.getColumns(); f++){//current column/fret
					int fret = -1;
					try{
						fret = Integer.parseInt(tab.getNote(i, f));//get the fret,if one exists, from the column

						fretNote = Scale.transposeNote(note, fret, Scale.defaultScale);//transpose the note
						abc4jFretNote = Scale.transposeABC4JNote(abc4jNote, fret, Scale.abc4jTrebleScale);//transpose the note
						if(staff != null){
							staff.setNotesStandard(abc4jFretNote, f, i);//Set the standard note in TransposeUI
						}
					}catch(NumberFormatException e){
						//the fret is blank or "--|"
						if(tab.getNote(i, f).equals("--|")){
							fretNote = "--|";
						}else{
							fretNote = "---";
						}
						if(staff != null){
							staff.setNotesStandard(" ", f, i);//Set the standard note in TransposeUI
						}
					}
					trans.setNote(i, f, fretNote);//Set the actual note in TransposeUI
				}
			}
			staff.upDateStrTune();
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}catch(NumberFormatException e){
			e.printStackTrace();
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}

	/**
	 * Updates TuningUI.
	 */
	public void updateTuningUI(){

	}

	@Override
	public void update(Notifier s) {
		int type = s.getType();

		switch(type){

		case (TABLTURE_UI):
			updateTransposeUI();
		break;

		case (STAFF_UI):
			break;

		case (TRANSPOSE_UI):
			updateTablatureUI();
		break;

		case (TUNING_UI):
			updateTransposeUI();
		break;

		}
		updateTablatureText();
		//		parent.updateInput(tab.getInput() + "<>" + trans.getInput() + "<>" + transposedFretsToString());
		validateAll();
	}

	/**
	 * Sets the font.
	 * @param fontType
	 */
	public void setFont(int fontType){
		this.fontType = fontType;

		switch(fontType){
		case OsUtils.WIN_FONT:
			tablatureText.setFont(new Font("Courier New", Font.PLAIN, this.fontSize));
			lyric.setFont(OsUtils.WIN_FONT);
			break;
		case OsUtils.UNIX_FONT:
			tablatureText.setFont(new Font("Courier 10 Pitch", Font.PLAIN, this.fontSize));
			lyric.setFont(OsUtils.UNIX_FONT);
			break;
		}
		validateAll();
	}

	/**
	 * Returns a string of all the available frets the note
	 * occurs on.
	 * 
	 * @return the optional frets
	 */
	//	public String transposedFretsToString(){
	//		String frets = "";
	//
	//		for(int i = 0; i < transposedFrets.size(); i++){
	//			if(i == transposedFrets.size() - 1){
	//				frets += transposedFrets.get(i);
	//			}else{
	//				frets += transposedFrets.get(i) + ", ";
	//			}
	//		}
	//		return frets;
	//	}
	/**
	 * Validates all UIs.
	 */
	private void validateAll(){
		tab.validate();
		trans.validate();
		if(staff != null){
			staff.validate();
		}
		tablatureText.validate();
	}

	/**
	 * Returns the font type.
	 * @return
	 */
	public int getFontType(){
		return this.fontType;
	}

	@Override
	public void update() {
		//		if(tab.getKeyEntered() || trans.getKeyEntered()){
		//			parent.updateInput(tab.getInput() + "|" + trans.getInput());
		//		}
		//		updateTransposeUI();
		//		validateAll();
	}
}