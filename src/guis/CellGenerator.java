package guis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class CellGenerator {
	/**
	 * Contains the arrays that hold the JLists.
	 * Each index corresponds to a string (based on index 1).
	 */
	private ArrayList<ArrayList<JList>> arrayStringsForTab;
	/**
	 * Contains the arrays that hold the JLists.
	 * Each index corresponds to a string (based on index 1).
	 */
	private ArrayList<ArrayList<JList>> arrayStringsForTranspose;
	/**
	 * Number of strings being used.
	 */
	private int strings;
	/**
	 * Number of columns being used.
	 * Defaulted to 32.
	 */
	private int columns = 32;
	private int fontSize;
	private Dimension cellSize;
	/**
	 * Receives events for TransposeUI.
	 */
	//	private CellGenerator event;
	private TablatureUIEventHandler eventForTab;
//	private Observer observer;
	//	private final int ID = UIController.TRANSPOSE_UI;
//	private UIController parent;

	/**
	 * Constructor.
	 * @param int - strings
	 */
	public CellGenerator(UIController parent, int strings){
//		this.parent = parent;
		this.strings = strings;
		//		event = new CellGenerator(this);
		eventForTab = new TablatureUIEventHandler();
		arrayStringsForTab = new ArrayList<ArrayList<JList>>(strings + 1);
		arrayStringsForTranspose = new ArrayList<ArrayList<JList>>(strings + 1);
		arrayStringsForTab.add(null);//fill the 0 index with a null
		arrayStringsForTranspose.add(null);//fill the 0 index with a null
	}

	/**
	 * Constructor.
	 * @param int - strings
	 * @param int - columns
	 */
	public CellGenerator(UIController parent, int strings, int frets){
//		this.parent = parent;
		this.strings = strings;
		this.columns = frets;
		//		event = new CellGenerator(this);
		eventForTab = new TablatureUIEventHandler();
		arrayStringsForTab = new ArrayList<ArrayList<JList>>(strings + 1);
		arrayStringsForTranspose = new ArrayList<ArrayList<JList>>(strings + 1);
		arrayStringsForTab.add(null);//fill the 0 index with a null
		arrayStringsForTranspose.add(null);//fill the 0 index with a null
	}
	
	/**
	 * Sets the parent for this event handler.
	 * @param parent
	 */
	public void setTabParent(TablatureUI parent){
		eventForTab.setParent(parent);
	}
	
	/**
	 * Starts the GUI process.
	 */
	public void generateUI(){
		Color background = new Color(240,240,240);

		for(int i = 1; i <= strings; i++){
			ArrayList<JList> arrayFretsForTab = new ArrayList<JList>();
			ArrayList<JList> arrayFretsForTranspose = new ArrayList<JList>();

			for(int z = 0; z <= columns + 1; z++){
				DefaultListModel modelForTab = new DefaultListModel();
				DefaultListModel modelForTranspose = new DefaultListModel();

				CellUI fretForTab = new CellUI();
				fretForTab.setType(UIController.TABLTURE_UI);
				fretForTab.setBorder(BorderFactory.createEmptyBorder());
				fretForTab.setFixedCellHeight((int)cellSize.getHeight());
				fretForTab.setFixedCellWidth((int)cellSize.getWidth());
				fretForTab.addKeyListener(eventForTab);
				fretForTab.setFont(new Font("Courier New", Font.PLAIN, this.fontSize));
				fretForTab.setBackground(Color.white);
				fretForTab.setString(i);
				fretForTab.setFrets(columns);
				
				CellUI fretForTranspose = new CellUI();
				fretForTranspose.setType(UIController.TRANSPOSE_UI);
				fretForTranspose.setFixedCellHeight((int)cellSize.getHeight());
				fretForTranspose.setFixedCellWidth((int)cellSize.getWidth());
				fretForTranspose.setFont(new Font("Courier New", Font.BOLD, this.fontSize));
				fretForTranspose.setBackground(background);
				fretForTranspose.setString(i);
				fretForTranspose.setFrets(columns);
				
				fretForTab.addObserver(fretForTranspose);
				fretForTranspose.addObserver(fretForTab);
				
				arrayFretsForTab.add(fretForTab);
				arrayFretsForTranspose.add(fretForTranspose);

				if(z == 0){
					modelForTab.addElement("|--");
					fretForTab.setEnabled(false);
					
					modelForTranspose.addElement("|--");
					fretForTranspose.setEnabled(false);
				}else if(z == columns + 1){
					modelForTab.addElement("--|");
					fretForTab.setEnabled(false);
					
					modelForTranspose.addElement("--|");
					fretForTranspose.setEnabled(false);
				}else{
					modelForTab.addElement("---");
					fretForTab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					
					modelForTranspose.addElement("---");
					fretForTranspose.setEnabled(true);
				}
				fretForTab.setModel(modelForTab);
				fretForTranspose.setModel(modelForTranspose);
			}
			arrayStringsForTab.add(arrayFretsForTab);
			arrayStringsForTranspose.add(arrayFretsForTranspose);
		}
	}
	
	/**
	 * Returns the array of JLists used for the tab model.
	 * @return JLists representing the tab model
	 */
	public ArrayList<ArrayList<JList>> getTabModel(){
		return arrayStringsForTab;
	}
	
	/**
	 * Sets the font size.
	 * @param size
	 */
	public void setFontSize(int size){
		this.fontSize = size;
	}
	
	/**
	 * Sets the JList cell size.
	 * @param size
	 */
	public void setCellSize(Dimension size){
		this.cellSize = size;
	}
}
