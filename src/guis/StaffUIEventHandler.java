package guis;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFormattedTextField;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Handles all events created by StaffUI.
 * @author RAM
 * @version 1.0
 * @created 04-Jan-2010 7:16:49 AM
 */
public class StaffUIEventHandler implements KeyListener, ChangeListener, MouseListener, ActionListener{

	/**
	 * Instance of StaffUI.
	 */
	private StaffUI parent;

	private JMenuItem itemHide;

	private JPopupMenu menuPopUp;

	public void finalize() throws Throwable {

	}

	/**
	 * Constructor.
	 * 
	 * @param staff
	 */
	public StaffUIEventHandler(StaffUI staff){
		this.parent = staff;
		createPopUpMenu();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() instanceof JSpinner){
			String input = parent.spinBeatPerNote.getValue().toString();
			parent.setBeatPerNote(input);
			parent.upDateStrTune();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getSource() instanceof JFormattedTextField){
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				String input = parent.fieldBeat.getText();
				parent.setBeatNote(input);
				parent.upDateStrTune();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3){
			menuPopUp.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void createPopUpMenu(){
		menuPopUp = new JPopupMenu();

		itemHide = new JMenuItem("Hide");
		itemHide.addActionListener(this);
		menuPopUp.add(itemHide);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() instanceof JMenuItem){

			JMenuItem source = (JMenuItem)event.getSource();

			if(source == itemHide){
				parent.setVisible(false);
			}
		}
	}
}