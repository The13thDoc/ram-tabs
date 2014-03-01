package guis;


import javax.swing.*;

import main.OsUtils;

import observerPattern.Observer;
import observerPattern.Notifier;

import java.awt.*;
/**
 * Generates the GUI for Lyrics.
 * @author RAM
 * @version 1.0
 * @created 01/11/2011
 */
@SuppressWarnings("serial")
public class LyricUI extends JPanel implements Notifier {

	private Observer observer;
	private int fontSize;
	private final int ID = UIController.STAFF_UI;;
	
	private JTextArea text;

	/**
	 * Constructor. Accepts the number of strings needed.
	 * @param numberOfStrings
	 */
	public LyricUI(){
	}

	/**
	 * Starts the GUI process.
	 */
	public void generateUI(){
		setBackground(Color.white);
		setLayout(new BorderLayout());

		text = new JTextArea();
		
//		this.setFont(parent.getFontType());
		text.setEditable(true);
		text.setEnabled(true);
		text.setVisible(true);
		text.setAutoscrolls(true);
		
		JScrollPane scrollBar = new JScrollPane(text);
		scrollBar.setBorder(BorderFactory.createEtchedBorder());
		scrollBar.setVisible(true);
		scrollBar.setAutoscrolls(true);
		
		add(scrollBar, BorderLayout.CENTER);
	}

	@Override
	public void addObserver(Observer o) {
		this.observer = o;
	}

	@Override
	public void notifyObservers() {
		this.observer.update(this);
	}

	@Override
	public void removeObserver(Observer o) {
		//do nothing
	}

	/**
	 * Returns the font size.
	 * @return int
	 */
	public int getFontSize(){
		return this.fontSize;
	}

	/**
	 * Sets the font size.
	 * @param size
	 */
	public void setFontSize(int size){
		this.fontSize = size;
	}

	@Override
	public int getType() {
		return ID;
	}
	
	/**
	 * Returns the text.
	 * @return String lyrics
	 */
	public String getText(){
		return text.getText();
	}
	
	/**
	 * Sets the text.
	 * @param text lyrics
	 */
	public void setText(String text){
		this.text.setText(text);
	}
	
	/**
	 * Sets the font.
	 * @param fontType
	 */
	public void setFont(int fontType){
		switch(fontType){
		case OsUtils.WIN_FONT:
			text.setFont(new Font("Courier New", Font.PLAIN, this.fontSize));
			break;
		case OsUtils.UNIX_FONT:
			text.setFont(new Font("Courier 10 Pitch", Font.PLAIN, this.fontSize));
			break;
		}
	}
}