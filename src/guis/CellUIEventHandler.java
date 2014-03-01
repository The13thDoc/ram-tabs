package guis;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CellUIEventHandler implements MouseListener {

	/**
	 * Instance of CellUI
	 */
	private CellUI parent;

	public CellUIEventHandler(CellUI parent) {
		this.parent = parent;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		/*
		 * BUTTON1 - left BUTTON2 - wheel BUTTON3 - right
		 */
		if (e.isShiftDown()) {
			parent.setSelectedIndex(0);
		}
		if (e.isAltDown() && e.isShiftDown()) {
			parent.clearSelection();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			parent.setSelectedIndex(0);
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			parent.clearSelection();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
