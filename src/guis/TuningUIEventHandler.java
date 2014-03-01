package guis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

/**
 * Handles all event created by TuningUI.
 * @author RAM
 * @version 1.0
 * @created 11-Apr-2010 3:33:37 AM
 */
public class TuningUIEventHandler implements ActionListener {

	/**
	 * Instance of TuningUI.
	 */
	private TuningUI parent;
	private boolean loadComplete = false;

	public TuningUIEventHandler(TuningUI parent){
		this.parent = parent;


	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() instanceof JComboBox){
//			System.out.println("not doing it");
			if(loadComplete){
				parent.updateTuning();
//				parent.notifyObservers();
//				System.out.println("----did it");
			}
		}
		
		if(event.getSource() instanceof JCheckBox){
			JCheckBox source = (JCheckBox)event.getSource();
			if(source.isSelected()){
				parent.setDisplayTuning(true);
			}else{
				parent.setDisplayTuning(false);
			}
		}
	}

	/**
	 * Sets whether or not loading is complete.
	 * @param b
	 */
	public void setLoadComplete(boolean b){
		this.loadComplete = b;
	}

}