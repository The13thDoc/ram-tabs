package guis;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Handles all events created by TransposeUI.
 * @author RAM
 * @version 1.0
 * @since 08/28/2010
 * TransposeUI now accepts input and relays it
 * to TablatureUI.
 */
public class TransposeUIEventHandler implements KeyListener, MouseListener {

	/**
	 * Instance of TransposeUI.
	 */
	private TransposeUI parent;
	/**
	 * Combines keys pressed to create input.
	 * Cleared when 'Enter' is pressed.
	 */
	private StringBuffer input;
//	private boolean keyEntered = false;
	private String note = "";

	/**
	 * Constructor.
	 * 
	 * @param transpose
	 */
	public TransposeUIEventHandler(TransposeUI transpose){
		parent = transpose;

		input = new StringBuffer("");
	}

	/**
	 * Returns the current user input.
	 * @return String - input
	 */
	public String getInput(){
		return input.toString();
	}

	/**
	 * Returns the currently selected note.
	 * @return String - note
	 */
	public String getNote(){
		return note;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			try{
				Integer.parseInt(input.toString());
			}catch(NumberFormatException ex){
				input.delete(0, 1);//removes a '?'
			}
			parent.setInput(input.toString());
			input = new StringBuffer("");//clear for next string of input
			this.parent.notifyObservers();
		}else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE){
			input = new StringBuffer("");//clear by user
			this.parent.notifyObservers();
		}else if(e.getKeyCode() == KeyEvent.VK_CONTROL){
		}else{
			input.append(e.getKeyChar());//Add number to input string	
			this.parent.notifyObservers();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	/**
	 * Sets the keyEntered boolean.
	 * @param boolean
	 */
//	public void setKeyEntered(boolean b){
//		this.keyEntered = b;
//	}

	/**
	 * Returns the keyEntered boolean.
	 * @return boolean
	 */
//	public boolean getKeyEntered(){
//		return this.keyEntered;
//	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
//		if(arg0.getSource() instanceof JList){
//			JList source = (JList)arg0.getSource();
//
//			note = source.getModel().getElementAt(0).toString();
//			parent.notifyObservers();
//		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}