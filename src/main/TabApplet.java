package main;

import guiHelper.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.*;

import manuscript.Manuscript;
import observerPattern.Observer;
import observerPattern.Notifier;

import dualKeyMap.DualKeyMap;
import externals.UserPreferences;


/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
/**
 * Creates and controls the TabGUI interface.
 * 
 * @author RAM
 * @version 1.0
 * @since 04-Jan-2010 7:16:50 AM
 * 
 * @version 1.8
 * @since 08/31/2010
 * Implements moveMeasure() 
 * 
 * @version 1.9
 * @since 01/11/2011
 * - LyricUI added.
 * - Opening/loading tabs no longer causes issues.
 * 
 * @version 1.9a
 * @since 01/11/2011
 * - Version info added ("About" in "Help").
 * - Overall speed of application has drastically improved.
 * - StaffUI removed for the time being.
 * - Key is more accurate.
 * - Move function works when writing.
 * 
 * @version 1.9b
 * @since 01/16/2011
 * - A preview of the written tablature was added,
 * in a split pane with the TransposeUI. This allows
 * the user to accurately line up where palm mutes,
 * lyrics, etc. should be placed.
 * 
 * @version 1.9c
 * @since 01/22/2011
 * - Menu items cleaned up, mneumonics added.
 * - Writing, saving, and loading dialogs cleaned up.
 * - Save/Write and SaveAs/WriteAs added.
 * - Writing, saving, and loading directories go to
 * Tabs/RT Files folders. NOTE: they must be present
 * in the root folder.
 * - .rt files are made read-only.
 * - File name of Tab shows up in toolbar.
 * 
 * 
 * 
 * @version 2.0
 * @since 04/18/2011
 * - LINUX COMPATABILITY TESTS
 * 		- All setPreferredSize() methods have been removed, where applicable,
 * 		to allow Swing objects to "size themselves". This allows different system
 * 		fonts and sizes (linux, windows, mac) to be compatable.
 * 
 * @since 05/23/2011
 * - Added a menu to allow selection of Windows or Unix font. Changes the font
 * of the lyricUI and tabTextUI for each measure.
 * - Folders create themselves if they don't already exist.
 * 
 * @since 05/27/2011
 * - StaffUI added again. No longer takes 60 seconds to load.
 * 
 * @version 2.1
 * @since 06/05/2011
 * - Added ability to select multiple cells by holding Shift and mousing
 * over the cells. Unselect with Alt+Shift. Right-clicking also unselects a cell.
 * 
 * @version 2.1 BETA
 * @since 06/26/2011
 * - Removed the menu to allow selection of Windows or Unix font. The OS
 * and font is automatically determined by Java.
 * 
 * @version 2.2
 * @since 08/8/2011
 * @since 09/3/2011
 * - StaffUI updated.
 * - Menu items for measure have been removed and put into Manuscrpt/ManuscrptUIEventHandler.
 * 
 * NEED
 * - Figure out how Linux reads home directories/paths
 * 		- UPDATE: it seems to use the Home Folder (user's root folder)
 * 		as a default root folder.
 * 
 * @version 2.3
 * @since 9/30/2011
 * - The applet version was incorporated with the desktop version.
 * 
 * @version 2.4
 * @since 4/7/2012
 * - Better tab-dragging was implemented.
 */
@SuppressWarnings("serial")
public class TabApplet extends JApplet implements Observer {

	private String version = "2.4";

	protected JButton btnNewMeasure;
	/**
	 * Gives options for tuning presets.
	 */
	protected JComboBox comboTunePresets;
	protected JTextArea defaultArea;
	protected TabAppletEventHandler event;
	/**
	 * toolkit for frameImage
	 */
	public static Icon iconBlackSheep = null;
	/**
	 * image for media player frame
	 */
//	protected JMenuItem itemCopyMeasure;
	//    protected JMenuItem itemDefaultFolder;
//	protected JMenuItem itemDeleteMeasure;
	protected JMenuItem itemEventSource;
	protected JMenuItem itemKey;
	protected JMenuItem itemNameTab;
	protected JMenuItem itemNewTab;
	protected JMenuItem itemOpenTab;
//	protected JMenuItem itemRenameMeasure;
//	protected JMenuItem itemMoveMeasure;
	protected JMenuItem itemSaveTabAs;
	protected JMenuItem itemSaveTab;
	protected JMenuItem itemWriteTab;
	protected JMenuItem itemWriteTabAs;
	protected JMenuItem itemAbout;
//	protected JMenuItem itemWindowsFont;
//	protected JMenuItem itemUnixFont;

	protected JLabel labelName;
	/**
	 * Instance of Manuscript
	 */
	protected Manuscript manuscript;
	protected JMenuBar menuBar;
//	protected JMenu menuEdit;
	protected JMenu menuFile;
//	protected JMenu menuFont;
	protected JMenu menuHelp;
	protected UserPreferences myPrefs;
	protected JPanel panelCenter;
	protected JPanel panelRightCorner;
	protected JPanel panelNorth;
	//    private Observer observer;
	private DualKeyMap<String, String[]> presets;
	
	private JFrame appletFrame;
	
	/**
	 * @param args    args
	 */
	public static void main(String[] args){
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
//		OsUtils.setOSIndicator();
		
		TabApplet tab = new TabApplet();
		tab.init();
	}

	/**
	 * Constructor for the TabGUI.
	 */
	public TabApplet() {
//		checkOS();
		manuscript = new Manuscript(this);
		event = new TabAppletEventHandler(this);
		menuBar = new JMenuBar();
		presets = new DualKeyMap<String, String[]>();
//		myPrefs = new UserPreferences();
//		myPrefs.loadPreferences();
//		init();
	}

	public void start(){
	}

	public void destroy(){
	}

	public void stop(){
	}
	
//	/**
//	 * Copies the selected measure.
//	 */
//	public void copyMeasure() {
//		if (manuscript.getTabCount() == 0) {
//		} else {
//			int index = manuscript.getSelectedTab();
//			String currentName = manuscript.getMeasureName(index);
//			String name = (String) JOptionPane.showInputDialog(this, null,
//					"Currently: " + currentName, JOptionPane.QUESTION_MESSAGE,
//					iconBlackSheep, null, currentName);
//			if (name == null) {
//			} else {
//				manuscript.copyMeasure(index, name);
//			}
//		}
//	}

	/**
	 * Creates the presets and populates the presets combo box.
	 */
	private void createPresets() {
		String[] standard = {"","E","B","G","D","A","E","B"};
		String[] c = {"","D","A","F","C","G","C","A"};
		String[] b = {"","C#","G#","E","B","F#","B","G#"};
		String[] aSharp = {"","C","G","D","A#","F","A#","G"};
		String[] a = {"","B","F#","B#","A","E","A","F#"};

		presets.put("Standard", standard);
		presets.put("C", c);
		presets.put("B", b);
		presets.put("A#", aSharp);
		presets.put("A", a);
	}

//	/**
//	 * Moves the selected tab to the chosen index.
//	 */
//	public void moveMeasure(){
//		if (manuscript.getTabCount() == 0) {
//		} else {
//			Object[] array = new Object[manuscript.getTabCount()];
//
//			for(int i = 0; i < manuscript.getTabCount(); i++){
//				array[i] = manuscript.getMeasureName(i);
//			}
//			int index = -1;
//
//			index = JOptionPane.showOptionDialog(this, "Where would you like to move it?",
//					"Choose a measure.", JOptionPane.OK_OPTION,
//					JOptionPane.QUESTION_MESSAGE, iconBlackSheep, array, array[manuscript.getSelectedTab()]);
//			if(index != -1){
//				manuscript.moveMeasure(index);
//			}
//		}
//	}
	
//	/**
//	 * Deletes the selected measure.
//	 */
//	public void deleteMeasure() {
//		if (manuscript.getTabCount() == 0) {
//		} else {
//			if (JOptionPane.showConfirmDialog(this,
//					"Are you sure you want to delete this measure?", "Delete: "
//					+ manuscript.getMeasureName(manuscript
//							.getSelectedTab()),
//							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
//							iconBlackSheep) == JOptionPane.YES_OPTION) {
//				manuscript.deleteMeasure(manuscript.getSelectedTab());
//
//				if (manuscript.getTabCount() == 0) {
//					panelCenter.removeAll();
//					panelCenter.add(defaultArea, BorderLayout.CENTER);
//				}
//			} else {
//			}
//		}
//	}

	/**
	 * Starts the GUI process.
	 */
	public void init() {
		/*
		 * Frame icon
		 */
//		Toolkit frameImage = Toolkit.getDefaultToolkit();
//		Image imageFrameIcon = frameImage.getImage("./InkyBuckTrans.png");

		/*
		 * Frame features
		 */
		appletFrame = new JFrame("RAM Tabs Applet");
		appletFrame.setSize(1200, 500);
		appletFrame.setVisible(true);
		appletFrame.addWindowListener(event);
		appletFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		/*
		 * Container features
		 */
		Container tabContainer = appletFrame.getContentPane();
		tabContainer.setVisible(true);
		tabContainer.setLayout(new BorderLayout(1, 1));

		/*
		 * North panel features
		 */
		panelNorth = new JPanel();
		panelNorth.setLayout(new BorderLayout(1, 1));
		panelNorth.setBackground(new Color(150, 11, 11));
		//		panelNorth.setPreferredSize(new Dimension(100, 43));
		panelNorth.setBorder(BorderFactory.createEtchedBorder());
		tabContainer.add(panelNorth, BorderLayout.NORTH);

		/*
		 * Center panel features
		 */
		panelCenter = new JPanel();
		panelCenter.setLayout(new BorderLayout(1, 1));
		panelCenter.setBackground(Color.BLACK);

		defaultArea = new JTextArea("RAM");
		defaultArea.setBackground(Color.BLACK);
		defaultArea.setForeground(new Color(70, 11, 11));
		defaultArea.setFont(new Font("Arial", Font.PLAIN, 50));
		defaultArea.setEditable(false);
		defaultArea.setEnabled(false);

		panelCenter.add(defaultArea, BorderLayout.CENTER);
		tabContainer.add(panelCenter, BorderLayout.CENTER);

		/*
		 * Corner panel features
		 */
		panelRightCorner = new JPanel();
		panelRightCorner.setLayout(new FlowLayout(FlowLayout.LEADING));
		//		panelRightCorner.setPreferredSize(new Dimension(530, 43));
		panelRightCorner.setBackground(new Color(150, 11, 11));
		panelNorth.add(panelRightCorner, BorderLayout.EAST);

		/*
		 * Left corner panel features
		 */
		JPanel panelLeftCorner = new JPanel();
		panelLeftCorner.setLayout(new FlowLayout(FlowLayout.LEADING));
		//		panelLeftCorner.setPreferredSize(new Dimension(280, 43));
		panelLeftCorner.setBackground(new Color(150, 11, 11));
		panelNorth.add(panelLeftCorner, BorderLayout.WEST);

		/*
		 * Name label features
		 */
		labelName = new JLabel();
		labelName.setFont(new Font("Arial", Font.BOLD, 20));
		labelName.setForeground(Color.black);
		//		labelName.setPreferredSize(new Dimension(170, 30));
		labelName.setText(manuscript.getTabName());
		panelNorth.add(labelName, BorderLayout.CENTER);

		/*
		 * Tuning boxes
		 */
		comboTunePresets = new JComboBox();
		panelRightCorner.add(comboTunePresets);
		//		comboTunePresets.setPreferredSize(new Dimension(100, 30));
		comboTunePresets.addActionListener(event);
		comboTunePresets.setEnabled(false);
		comboTunePresets.setVisible(false);
		createPresets();

		for(int i = 0; i < presets.size(); i++){
			comboTunePresets.addItem(presets.getMapA().get(i));
		}

		/*
		 * New measure button features
		 */
		btnNewMeasure = new JButton("New Measure");
		//		btnNewMeasure.setPreferredSize(new Dimension(112, 30));
		btnNewMeasure.addActionListener(event);
		panelLeftCorner.add(btnNewMeasure);

		/*
		 * Toolbar menu features
		 */
		menuFile = new JMenu("File");
//		menuEdit = new JMenu("Measure");
//		menuFont = new JMenu("Font");
		menuHelp = new JMenu("Help");

		itemOpenTab = new JMenuItem("Open...");
		itemOpenTab.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_O, ActionEvent.CTRL_MASK));
//		itemOpenTab.addActionListener(event);

		itemSaveTab = new JMenuItem("Save");
		itemSaveTab.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, ActionEvent.CTRL_MASK));
//		itemSaveTab.addActionListener(event);

		itemSaveTabAs = new JMenuItem("Save As...");
		itemSaveTabAs.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, ActionEvent.CTRL_MASK+ActionEvent.ALT_MASK));
//		itemSaveTabAs.addActionListener(event);

		itemNameTab = new JMenuItem("Name Tab");
		itemNameTab.addActionListener(event);

		itemNewTab = new JMenuItem("New");
		itemNewTab.addActionListener(event);

//		itemCopyMeasure = new JMenuItem("Copy Measure");
//		itemCopyMeasure.setAccelerator(KeyStroke.getKeyStroke(
//				KeyEvent.VK_C, ActionEvent.CTRL_MASK));
//		itemCopyMeasure.addActionListener(event);
//
//		itemDeleteMeasure = new JMenuItem("Delete Measure");
//		itemDeleteMeasure.setAccelerator(KeyStroke.getKeyStroke(
//				KeyEvent.VK_D, ActionEvent.CTRL_MASK));
//		itemDeleteMeasure.addActionListener(event);
//
//		itemRenameMeasure = new JMenuItem("Rename Measure");
//		itemRenameMeasure.setAccelerator(KeyStroke.getKeyStroke(
//				KeyEvent.VK_R, ActionEvent.CTRL_MASK));
//		itemRenameMeasure.addActionListener(event);
//
//		itemMoveMeasure = new JMenuItem("Move Measure");
//		itemMoveMeasure.setAccelerator(KeyStroke.getKeyStroke(
//				KeyEvent.VK_M, ActionEvent.CTRL_MASK));
//		itemMoveMeasure.addActionListener(event);

		itemWriteTab = new JMenuItem("Write");
		itemWriteTab.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_W, ActionEvent.CTRL_MASK));
//		itemWriteTab.addActionListener(event);

		itemWriteTabAs = new JMenuItem("Write As...");
		itemWriteTabAs.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_W, ActionEvent.CTRL_MASK+ActionEvent.ALT_MASK));
//		itemWriteTabAs.addActionListener(event);

//		itemWindowsFont = new JMenuItem("Windows Font");
//		itemWindowsFont.addActionListener(event);
		
//		itemUnixFont = new JMenuItem("Unix Font");
//		itemUnixFont.addActionListener(event);
		
		itemAbout = new JMenuItem("About");
		itemAbout.addActionListener(event);

		//	itemDefaultFolder = new JMenuItem("Change Default Folder");
		//	itemDefaultFolder.addActionListener(event);
		//	itemDefaultFolder
		//	.setToolTipText("Changes the folder where RAM Tabs are saved");

		itemKey = new JMenuItem("Key");
		itemKey.addActionListener(event);

		/*
		 * Add submenus to toolbar menus
		 */
		menuFile.add(itemNewTab);
//		menuFile.add(itemOpenTab);
//		menuFile.addSeparator();
//		menuFile.add(itemSaveTab);
//		menuFile.add(itemSaveTabAs);
//		menuFile.addSeparator();
//		menuFile.add(itemWriteTab);
//		menuFile.add(itemWriteTabAs);
		menuFile.addSeparator();
		menuFile.add(itemNameTab);
		//	menuFile.add(itemDefaultFolder);

//		menuEdit.add(itemRenameMeasure);
//		menuEdit.add(itemMoveMeasure);
//		menuEdit.add(itemCopyMeasure);
//		menuEdit.addSeparator();
//		menuEdit.add(itemDeleteMeasure);

//		menuFont.add(itemWindowsFont);
//		menuFont.add(itemUnixFont);
		
		menuHelp.add(itemKey);
		menuHelp.add(itemAbout);

		/*
		 * Add menus to toolbar
		 */
		menuBar.add(menuFile);
//		menuBar.add(menuEdit);
//		menuBar.add(menuFont);
		menuBar.add(menuHelp);

		appletFrame.setJMenuBar(menuBar);

		/*
		 * Refresh the UI
		 */
		appletFrame.validate();

	}

	/**
	 * New measure.
	 * 
	 * @param strings
	 * @param name
	 */
	public void newMeasure(int strings, String name) {
		if (manuscript.getTabCount() == 0) {
			panelCenter.removeAll();
			panelCenter.add(manuscript.getTabbedPane(), BorderLayout.CENTER);
		}
		manuscript.addMeasure(strings, name);
	}

	/**
	 * Prompts the user for the measure name.
	 * 
	 * @return String - name
	 */
	public String newMeasureNamePrompt() {
		String name;

		name = JOptionPane.showInputDialog(null,
				"What would you like to name this measure?",
				"Name the measure", JOptionPane.QUESTION_MESSAGE);

		return name;
	}

	/**
	 * Prompts the user for the desired number of strings.
	 * 
	 * @return int - string number
	 */
	public int newMeasureStringsPrompt() {
		Object[] array = { 4, 5, 6, 7 };
		int strings;

		strings = JOptionPane.showOptionDialog(null, "How many strings?",
				"Choose a tab", JOptionPane.OK_OPTION,
				JOptionPane.QUESTION_MESSAGE, iconBlackSheep, array, array[2]);

		return strings;
	}

	/**
	 * Close the current Tab and start a blank one.
	 */
	public void newTab() {
		int o = JOptionPane.showConfirmDialog(null, "Do you want to save this Tab?",
				"Closing", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, iconBlackSheep);

		if(o == JOptionPane.YES_OPTION){
			save();
			manuscript.closeTab();
			appletFrame.setTitle("RAM's Tab Creator");
			updateTabName();
		}
		if(o == JOptionPane.NO_OPTION){
			manuscript.closeTab();
			appletFrame.setTitle("RAM's Tab Creator");
			updateTabName();
		}
//		validate();
	}

	/**
	 * Saves the Tab. If no file currently exists, saveAs will be
	 * automatically prompted.
	 */
	public void save() {
		if(!manuscript.saveTab()){
			saveAs();
		}else{
			JOptionPane.showConfirmDialog(null, "Tab has been saved.",
					"Success!", JOptionPane.PLAIN_MESSAGE,
					JOptionPane.INFORMATION_MESSAGE, iconBlackSheep);
		}
	}

	/**
	 * Prompts the user to choose a file.
	 */
	public boolean saveAs() {
		String ext = manuscript.getSAVE_EXTENSION();

		File savePath = new File("./RT Files");
		if(!savePath.exists()){
			savePath.mkdir();
		}
		JFileChooser chooser = new JFileChooser("./RT Files");
		TextSifter sifter = new TextSifter(ext);
		sifter.setDescription("RAM Tab file (" + ext + ")");
		chooser.setFileFilter(sifter);
		chooser.addChoosableFileFilter(sifter);
		chooser.setApproveButtonText("Save");
		chooser.setApproveButtonToolTipText("Save");
		chooser.setDialogTitle("Save as...");
		chooser.setMultiSelectionEnabled(false);
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);

		if (chooser.showOpenDialog(null) == JFileChooser.CANCEL_OPTION) {
			return false;
		}else{
			if(manuscript.saveAs(chooser.getSelectedFile())){
				JOptionPane.showConfirmDialog(null, "Tab has been saved.",
						"Success!", JOptionPane.PLAIN_MESSAGE,
						JOptionPane.INFORMATION_MESSAGE, iconBlackSheep);
			}else{
				return false;
			}
		}
		return true;
	}

	/**
	 * Writes the Tab. If no file currently exists, saveAs will be
	 * automatically prompted.
	 */
	public void write() {
		if(!manuscript.writeTab()){
			writeAs();
		}else{
			JOptionPane.showConfirmDialog(null, "Tab has been written.",
					"Success!", JOptionPane.PLAIN_MESSAGE,
					JOptionPane.INFORMATION_MESSAGE, iconBlackSheep);
		}
	}

	/**
	 * Prompts the user to choose a file.
	 */
	public boolean writeAs() {
		String ext = manuscript.getWRITE_EXTENSION();

		File savePath = new File("./Tabs");
		if(!savePath.exists()){
			savePath.mkdir();
		}
		JFileChooser chooser = new JFileChooser("./Tabs");
		TextSifter sifter = new TextSifter(ext);
		sifter.setDescription("Tab file (" + ext + ")");
		chooser.setFileFilter(sifter);
		chooser.addChoosableFileFilter(sifter);
		chooser.setApproveButtonText("Write");
		chooser.setApproveButtonToolTipText("Write");
		chooser.setDialogTitle("Write");
		chooser.setMultiSelectionEnabled(false);
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);

		if (chooser.showOpenDialog(null) == JFileChooser.CANCEL_OPTION) {
			return false;
		}else{
			if (manuscript.writeTabAs(chooser.getSelectedFile())) {
				JOptionPane.showConfirmDialog(null, "Tab has been written.",
						"Success!", JOptionPane.PLAIN_MESSAGE,
						JOptionPane.INFORMATION_MESSAGE, iconBlackSheep);
			}else{
				return false;
			}
		}
		return true;
	}

	/**
	 * Prompts the user to choose a file.
	 */
	public boolean openTabPrompt() {
		String ext = manuscript.getSAVE_EXTENSION();

		File savePath = new File("./RT Files");
		if(!savePath.exists()){
			savePath.mkdir();
		}
		JFileChooser chooser = new JFileChooser("./RT Files");
		TextSifter sifter = new TextSifter(ext);
		sifter.setDescription("RAM Tab (" + ext + ")");
		chooser.setFileFilter(sifter);
		chooser.addChoosableFileFilter(sifter);
		chooser.setApproveButtonText("Open");
		chooser.setApproveButtonToolTipText("Open");
		chooser.setDialogTitle("Open...");
		chooser.setMultiSelectionEnabled(false);
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);

		if (chooser.showOpenDialog(null) == JFileChooser.CANCEL_OPTION) {
			return false;
		} else {
			manuscript.closeTab();
			appletFrame.setTitle("RAM's Tab Creator           " + "Loading.....");
			panelCenter.removeAll();
			panelCenter.add(manuscript.getTabbedPane(), BorderLayout.CENTER);
			manuscript.loadTab(chooser.getSelectedFile());
			comboTunePresets.setEnabled(true);
			comboTunePresets.setVisible(true);
			updateTabName();
			validateAll();
			return true;
		}
	}

	/**
	 * Sets input to the selected indices on the tab.
	 * 
	 * @param input
	 * @return boolean
	 */
	public boolean setInput(StringBuffer input) {
		try {
			int fret = Integer.parseInt(input.toString());
			manuscript.setFretNote(fret);

			return true;
		} catch (NumberFormatException e) {
			// input was not an integer
			return false;
		}
	}

//	/**
//	 * Sets the name of the measure.
//	 */
//	public void setMeasureName() {
//		try{
//			if (manuscript.getTabCount() == 0) {
//			} else {
//				int index = manuscript.getSelectedTab();
//				String currentName = manuscript.getMeasureName(index);
//				String name = (String) JOptionPane.showInputDialog(this, null,
//						"Currently: " + currentName, JOptionPane.QUESTION_MESSAGE,
//						iconBlackSheep, null, currentName);
//				if (name == null) {
//				} else {
//					manuscript.setMeasureName(index, name);
//				}
//			}
//		}catch (NullPointerException e) {
//		}catch (Exception e) {
//			JOptionPane.showMessageDialog(this, e.getStackTrace());
//		}
//	}

	/**
	 * Sets the tuning to the selected preset.
	 */
	public void setPreset() {
		String item = comboTunePresets.getSelectedItem().toString();
		manuscript.setTuning(manuscript.getSelectedTab(), presets.getValue(item));
		appletFrame.validate();
	}

	/**
	 * Sets the tab name.
	 */
	public void setTabName() {
		String currentName = manuscript.getTabName();
		String name = (String) JOptionPane.showInputDialog(null, "Currently: "
				+ currentName, "Title", JOptionPane.QUESTION_MESSAGE,
				iconBlackSheep, null, currentName);

		if (name == null || name.length() == 0) {
		} else {
			manuscript.setTabName(name);
			updateTabName();
		}
	}

	/**
	 * Displays the key.
	 */
	public void showKey() {
		URI uri;
		try {
			uri = new URI("http://code.google.com/p/ram-tabs/w/list");

			int o = JOptionPane.showConfirmDialog(null,
					"Would you like to go to the wiki to learn more about RAM Tabs?",
					"Key", JOptionPane.YES_NO_OPTION);

			if(o == JOptionPane.YES_OPTION){
				if (Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					desktop.browse(uri);

				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Unable to open link.",
					"Broken", JOptionPane.INFORMATION_MESSAGE);
		}catch (URISyntaxException e1) {
			JOptionPane.showMessageDialog(null,
					"Unable to open link.",
					"Broken", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Prompots the user to save the Tab.
	 */
	public void showSaveMessage() {
		int o = JOptionPane.showConfirmDialog(null, "Would you like to save?",
				"Save", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, iconBlackSheep);

		if (o == JOptionPane.YES_OPTION) {
			saveAs();
		}
		if (o == JOptionPane.NO_OPTION || o == JOptionPane.CANCEL_OPTION) {
			// parent.myPrefs.setMyLocation(parent.getLocation());
			// parent.myPrefs.setMySize(parent.getSize());
			// parent.myPrefs.savePreferences();
			// System.exit(0);
		}
	}

	@Override
	public void update() {
		changeTuningUI();
	}

	@Override
	public void update(Notifier s) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Updates the current user input.
	 */
	public void updateInput(String input) {
		appletFrame.setTitle("RAM's Tab Creator           " + input);
	}

	/**
	 * Updates the Tab name.
	 */
	public void updateTabName() {
		labelName.setText(manuscript.getTabName());
	}

	/**
	 * Validate the GUI.
	 */
	public void validateAll() {
		manuscript.validateAll();
		appletFrame.validate();
	}

	/**
	 * Sets the current settings in the preferences.
	 */
	public void savePreferences(){
//		myPrefs.setLocation(getLocation());
//		myPrefs.setSize(getSize());
//		myPrefs.savePreferences();
	}

	/**
	 * Switches the tuning UI based on the selected measure.
	 */
	public void changeTuningUI(){
		panelRightCorner.removeAll();

		panelRightCorner.add(this.comboTunePresets);
		panelRightCorner.add(manuscript.getTuningUI(manuscript.getSelectedTab()));
		appletFrame.validate();
	}

	/**
	 * Gets the backgroung color of the header.
	 */
	public Color getHeaderBackground(){
		return this.panelNorth.getBackground();
	}

	/**
	 * Returns the tuning for the selected preset.
	 * @return String[]
	 */
	public String[] getPreset(){
		return this.presets.getValue(this.comboTunePresets.getSelectedItem());
	}

	/**
	 * Sets the title in the toolbar.
	 */
	public void setToolbarTitle(String title){
		appletFrame.setTitle(title);
		appletFrame.validate();
	}

	/**
	 * Sets the font.
	 * @param fontType
	 */
	public void setFont(int fontType){
		manuscript.setFont(fontType);
	}
	
	/**
	 * Displays version information.
	 */
	public void showAbout(){
		JOptionPane.showMessageDialog(null,
				"RAM's Tab Creator\n\nVersion " + version + "\n\nA Product of RAM§0f†",
				"Information", JOptionPane.INFORMATION_MESSAGE, iconBlackSheep);
	}
}