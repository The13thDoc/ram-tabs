package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import javax.swing.*;

/**
 * Handles action events in the GUI.
 * @author main
 * @version 1.0
 * @since 04-Jan-2010 7:16:49 AM
 * 
 * @version 1.8
 * @since 08/31/2010
 * Implements moveMeasure()
 * 
 * @version 2.2
 * @since 09/3/2011
 * - Menu items for measure have been removed and put into Manuscrpt/ManuscrptUIEventHandler.
 */
public class TabAppEventHandler implements ActionListener, WindowListener {

	private TabApp parent;

	/**
	 * Constructor.
	 * @param tabApp TabApp
	 */
	public TabAppEventHandler(TabApp tabApp){
		parent = tabApp;
	}

	/**
	 * 
	 * @param event
	 */
	public void actionPerformed(ActionEvent event){


		if(event.getSource() instanceof JButton){

			JButton source = (JButton)event.getSource();

			if(source == parent.btnNewMeasure){//Handles actions caused by pressing the "New Measure" button
				//				parent.saved = false;
				int strings = 0;
				String name = null;

				strings = parent.newMeasureStringsPrompt();

				if(strings == -1){
					//Cancel, do nothing
				}else{
					name = parent.newMeasureNamePrompt();
					if(name == null){
						//cancel, do nothing
					}else{
						parent.newMeasure(strings, name);
						parent.comboTunePresets.setEnabled(true);
						parent.comboTunePresets.setVisible(true);
					}
				}
			}
		}

		if(event.getSource() instanceof JMenuItem){
			try{
				JMenuItem source = (JMenuItem)event.getSource();

				if(source == parent.itemNameTab){
					parent.setTabName();
				}
				if(source == parent.itemSaveTab){
					parent.save();
				}
				if(source == parent.itemSaveTabAs){
					parent.saveAs();
				}
				if(source == parent.itemOpenTab){
					//				parent.showSaveMessage();

					if(parent.openTabPrompt()){
						if(!parent.comboTunePresets.isEnabled()){
							//						parent.setUpTuning();
							//						parent.enableTuning(true);
							//						parent.setAllowUpdate(true);
							//						parent.swtichMeasureTuning();
						}
					}
				}
				if(source == parent.itemAbout){
					parent.showAbout();
				}
				if(source == parent.itemNewTab){
					parent.newTab();
				}
//				if(source == parent.itemCopyMeasure){
//					//					parent.copyMeasure();
//					throw new UnsupportedOperationException();
//				}
//				if(source == parent.itemDeleteMeasure){
//					//					parent.deleteMeasure();
//					throw new UnsupportedOperationException();
//				}
//				if(source == parent.itemRenameMeasure){
//					//				parent.setMeasureName();
//					throw new UnsupportedOperationException();
//				}
//				if(source == parent.itemMoveMeasure){
//					//				parent.moveMeasure();
//					throw new UnsupportedOperationException();
//				}
				if(source == parent.itemWriteTab){
					parent.write();
				}
				if(source == parent.itemWriteTabAs){
					parent.writeAs();
				}
				//			if(source == parent.itemWindowsFont){
				//				parent.setFont(OsUtils.WIN_FONT);	
				//			}
				//			if(source == parent.itemUnixFont){
				//				parent.setFont(OsUtils.UNIX_FONT);	
				//			}
				if(source == parent.itemKey){
					parent.showKey();
				}
				//			parent.validateAll();
			}catch(UnsupportedOperationException e){
				JOptionPane.showMessageDialog(parent, "No longer supported!! Right-click the measure instead!");
			}
		}

		if(event.getSource() instanceof JComboBox){

			JComboBox source = (JComboBox)event.getSource();

			if(source == parent.comboTunePresets){
				if(parent.manuscript.getTabCount() > 0){
					parent.setPreset();
					//					parent.validateAll();
				}
			}
		}
	}

	/**
	 * 
	 * @param arg0
	 */
	@Override
	public void windowActivated(WindowEvent arg0){

	}

	/**
	 * 
	 * @param arg0
	 */
	@Override
	public void windowClosed(WindowEvent arg0){
		parent.savePreferences();
	}

	/**
	 * 
	 * @param event
	 */
	public void windowClosing(WindowEvent event){

	}

	/**
	 * 
	 * @param arg0
	 */
	@Override
	public void windowDeactivated(WindowEvent arg0){

	}

	/**
	 * 
	 * @param arg0
	 */
	@Override
	public void windowDeiconified(WindowEvent arg0){

	}

	/**
	 * 
	 * @param arg0
	 */
	@Override
	public void windowIconified(WindowEvent arg0){

	}

	/**
	 * 
	 * @param arg0
	 */
	@Override
	public void windowOpened(WindowEvent arg0){

	}

}