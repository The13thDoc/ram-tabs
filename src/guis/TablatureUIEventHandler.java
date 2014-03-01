package guis;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles all events created by TablatureUI.
 * 
 * @author RAM
 * @version 1.0
 * @created 04-Jan-2010 7:16:50 AM
 */
public class TablatureUIEventHandler implements KeyListener {

	/**
	 * Instance of TablatureUI.
	 */
	private TablatureUI parent;
	/**
	 * Combines keys pressed to create input. Cleared when 'Enter' is pressed.
	 */
	private StringBuffer input;

	/**
	 * Constructor.
	 * 
	 * @param tablature
	 */
	public TablatureUIEventHandler(TablatureUI tablature) {
		parent = tablature;
		input = new StringBuffer("");
	}

	/**
	 * Constructor.
	 */
	public TablatureUIEventHandler() {
		input = new StringBuffer("");
	}

	/**
	 * Sets the parent for this event handler.
	 * 
	 * @param parent
	 */
	public void setParent(TablatureUI parent) {
		this.parent = parent;
	}

	/**
	 * Returns the current user input.
	 * 
	 * @return String - input
	 */
	public String getInput() {
		return input.toString();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
			parent.setInput(input.toString());
			input = new StringBuffer("");// clear for next string of input
			this.parent.notifyObservers();
			break;
		case KeyEvent.VK_BACK_SPACE:
		case KeyEvent.VK_DELETE:
			input = new StringBuffer("");// clear by user
			break;
		case KeyEvent.VK_F5:
			this.parent.notifyObservers();
			break;
		case KeyEvent.VK_ESCAPE:
			parent.unselectAll();
			break;
		default:
			input.append(e.getKeyChar());
			break;
		}

		// if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		// parent.setInput(input.toString());
		// input = new StringBuffer("");// clear for next string of input
		// this.parent.notifyObservers();
		// } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE
		// || e.getKeyCode() == KeyEvent.VK_DELETE) {
		// input = new StringBuffer("");// clear by user
		// // this.parent.notifyObservers();
		// // }else if(e.getKeyCode() == KeyEvent.VK_CONTROL){
		// } else if (e.getKeyCode() == KeyEvent.VK_F5) {
		// this.parent.notifyObservers();
		// } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		// parent.unselectAll();
		// } else {
		// input.append(e.getKeyChar());// Add number to input string
		// // this.parent.notifyObservers();
		// }
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
