package externals;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import abc.ui.swing.JScoreComponent;

import manuscript.Manuscript;
import guis.StaffUI;
import guis.TablatureUI;
import guis.UIController;

/**
 * 
 * @author RAM
 * 
 * @version 1.8
 * @since 08/31/2010
 * No longer writes on an extra extension when saving
 * and writing files. Overwrites without complications.
 *
 */
public class FileHandler {

	private Manuscript parent;
	private final String SAVE_EXTENSION = ".rt";
	private final String WRITE_EXTENSION = ".tab";
	private final String SCORE_EXTENSION = ".png";
	private File currentFile;
	private File currentWriteFile;

	/**
	 * Constructor.
	 * @param parent
	 */
	public FileHandler(Manuscript parent){
		this.parent = parent;
	}

	/**
	 * Returns the file name, excluding the extension.
	 * @return
	 */
	private String getFileName(){
		if(currentFile.getName().contains(".")){
			return currentFile.getName().substring(0, currentFile.getName().lastIndexOf("."));
		}else{
			return currentFile.getName();
		}
	}

	/**
	 * Save to a file chosen by the user.
	 * @param ArrayList<TablatureUI> - array of all TablatureUIs
	 * @return boolean - success
	 */
	public boolean saveAs(File file, ArrayList<TablatureUI> tab){
		currentFile = file;
		return save(tab);
	}
	/**
	 * Saves the current Tab as a .rt file. This is NOT
	 * to be altered by the user!
	 * Save - Automagically saves the file to the current file name.
	 * @param ArrayList<TablatureUI> - array of all TablatureUIs
	 * @return boolean - success
	 */
	public boolean save(ArrayList<TablatureUI> tab){
		try{
			currentFile.setWritable(true);

			PrintWriter printer = new PrintWriter(new File(currentFile.getParent(), 
					getFileName() + SAVE_EXTENSION));
			printer.println(parent.getTabName());

			for(int i = 0; i < tab.size(); i++){//get each tablature
				TablatureUI tablature = tab.get(i);
				int strings = tablature.getStrings();
				int frets = tablature.getColumns();
				ListModel model = null;

				printer.println(parent.getNumberOfStrings(i));
				printer.println(parent.getFrets(i));
				printer.println(parent.getTuningUI(i).getTuning());
				printer.println(parent.getMeasureName(i));

				//collect the models from each JList
				for(int string = 1; string <= strings; string++){//get string
					for(int fret = 0; fret <= frets + 1; fret++){//get fret
						String text = "";
						model = tablature.getModel(string, fret);//get model

						//get each character from the model
						//(should only be 3 characters each)
						//'|--', 'xxx', '--|'
						for(int m = 0; m < model.getSize(); m++){//get index of model
							text += model.getElementAt(m).toString();//get character
						}
						printer.print(text + "\t");//print as 1 string (ex: |-- \t --- \t 0 \t 1 \t --| \t
					}
					printer.println();//carriage return for next string
				}
				printer.println("<LYRICS>");
				printer.println(parent.getLyrics(i).trim());
				printer.println("</LYRICS>");
			}
			currentFile.setWritable(false);
			printer.close();
			return true;
		}catch(IOException e){
			JOptionPane.showMessageDialog(parent.parent, e.getMessage());
			return false;
		}catch(NullPointerException e){
			return false;
		}
	}

	/**
	 * Writes to a file chosen by the user.
	 * @param ArrayList<TablatureUI> array of all TablatureUIs
	 * @return success/failure
	 */
	public boolean writeAs(File file, ArrayList<UIController> tab){
		currentWriteFile = file;
		return write(tab);
	}

	/**
	 * Writes the the current tab to a text file.
	 * This is for the user. Saves with extension .tab.
	 * @param ArrayList(TablatureUI) array of all TablatureUIs
	 * @return success/failure
	 */
	public boolean write(ArrayList<UIController> tab){
		try{
			PrintWriter printer = new PrintWriter(new File(currentWriteFile.getParent(), 
					getFileName() + WRITE_EXTENSION));
			printer.println(parent.getTabName());

			for(int i = 0; i < tab.size(); i++){//get each tablature
				UIController cntrl = tab.get(i);
				TablatureUI tablature = cntrl.getTablatureUI();
				int strings = tablature.getStrings();
				int frets = tablature.getColumns();
				ListModel model = null;
				boolean displayTuning = cntrl.getTuningUI().isDisplayTuning();

				if(displayTuning){
					printer.println(cntrl.getTuningUI().getTuning());
				}
				printer.println();//carriage return for measure
				printer.println(cntrl.getName());
				//				printer.println();//carriage return for first string

				//collect the models from each JList
				for(int string = 1; string <= strings; string++){//get string
					for(int fret = 0; fret <= frets + 1; fret++){//get fret
						String text = "";
						model = tablature.getModel(string, fret);//get model

						//get each character from the model
						//(should only be 3 characters each)
						//'|--', 'xxx', '--|'
						text += model.getElementAt(0).toString();//get character
//						if(text.contains("--|")){
//							//print out string
//							fret = frets + 2;//break out of loop
//						}else 
							if(text.length() == 1){
							text+= "--";
						}else if(text.length() == 2){
							text+= "-";
						}
						printer.print(text);//print as 1 string (ex: |-- \t --- \t 0 \t 1 \t --| \t
					}
					printer.println();//carriage return for next string
				}
				printer.println(cntrl.getLyricUI().getText());
				//				printer.println();//carriage return for next measure
			}
			printer.close();
			return true;
		}catch(IOException e){
			JOptionPane.showMessageDialog(parent.parent, e.getMessage());
			return false;
		}catch(NullPointerException e){
			return false;
		}
	}

	/**
	 * Compiles all the scores into one and writes it to a file.
	 * @param tab
	 * @return
	 */
	public boolean writeScore(ArrayList<UIController> tab){
		try{
			ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
			int width = 0;
			int height = 0;
			String scoreXMLString = "";
			String path = currentWriteFile.getParent() + "\\Scores";

			File scorePath = new File(path);
			if(!scorePath.exists()){
				scorePath.mkdir();
			}
			
			File score = new File(path, 
					getFileName() + SCORE_EXTENSION);
			
			File scoreXML = new File(path, 
					getFileName() + ".txt");
			
			PrintWriter writer = new PrintWriter(scoreXML);

			for(int i = 0; i < tab.size(); i++){//get each tablature
				UIController cntrl = tab.get(i);
				StaffUI staff = cntrl.getStaffUI();
				JScoreComponent scoreUI = staff.getScoreComponet();

				//for each measure
				BufferedImage bufferedImage = new BufferedImage((int)scoreUI.getWidth(), (int)scoreUI.getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D bufferedImageGfx = (Graphics2D)bufferedImage.createGraphics();
				bufferedImageGfx.setColor(scoreUI.getBackground());//Color.WHITE);
				bufferedImageGfx.fillRect(0, 0, (int)bufferedImage.getWidth(), (int)bufferedImage.getHeight());
				scoreUI.drawIn(bufferedImageGfx);

				images.add(bufferedImage);
				if(scoreUI.getWidth() > width){
					width = scoreUI.getWidth();
				}
				height += scoreUI.getHeight() +10;
				
				scoreXMLString += staff.getStrTune() + "\r\n";
			}
			//for final compilation
			BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics g = combined.getGraphics();
			
			int currentHeight = 0;
			
			for(int i = 0; i < images.size(); i++){//get each tablature
				
				if(i == 0){
					g.drawImage(images.get(i), 0, 0, null);
				}else{
					g.drawImage(images.get(i), 0, currentHeight, null);
				}
				currentHeight += images.get(i).getHeight();
			}

			writer.println(scoreXMLString);
			writer.close();
			
			ImageIO.write(combined, "png", score);
			return true;
		}catch(IOException e){
			JOptionPane.showMessageDialog(parent.parent, e.getMessage());
			return false;
		}catch(NullPointerException e){
			return false;
		}
	}

	/**
	 * Loads the user-chosen tab from an .rtf file.
	 * @param file
	 * @return
	 */
	public boolean loadTab(File file){
		try{
			currentFile = file;
			Integer measure = 0;
			String[] note = null;
			String[] tabLine = null;
			Scanner reader = new Scanner(file);

			parent.setTabName(reader.nextLine());
			int strings = 0;
			String currentLine = "";
			while(reader.hasNextLine()){
				Integer status = measure + 1;
				parent.parent.setToolbarTitle("RAM's Tab Creator           " + "Loading....." + status.toString());

				if(currentLine.equals("</LYRICS>")){
					currentLine = reader.nextLine();
				}
				if(currentLine.equals("")){
					strings = Integer.parseInt(reader.nextLine());
				}else{
					strings = Integer.parseInt(currentLine);
				}
				parent.parent.newMeasure(strings, "");//create the measure in the manuscript.
				reader.nextLine();


				note = reader.nextLine().split(",");
				for(int i = 0; i < note.length; i++){
					parent.getTuningUI(measure).setLoadComplete(false);
					parent.setTuning(measure, note.length - i, note[i].trim());
					parent.getTuningUI(measure).setLoadComplete(true);
				}

				parent.setMeasureName(measure, reader.nextLine());

				for(int q = 1; q <= strings; q++){
					tabLine = reader.nextLine().split("\t");
					for(int i = 0; i < tabLine.length; i++){
						parent.loadNote(measure, q, i, tabLine[i]);			
					}
				}
				if(reader.hasNextLine()){
					currentLine = reader.nextLine();
					if(currentLine.equals("<LYRICS>")){
						String lyrics = "";
						currentLine = reader.nextLine();
						while(!currentLine.equals("</LYRICS>")){
							lyrics += currentLine + "\n";
							currentLine = reader.nextLine();
						}
						parent.setLyrics(measure, lyrics);
					}
				}
				measure++;
			}
			parent.parent.setToolbarTitle("RAM's Tab Creator - " + file.getName());
			reader.close();
			return true;
		}catch(IOException e){
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns the file extension used for saving tabs.
	 * @return the SAVE_EXTENSION
	 */
	public String getSAVE_EXTENSION() {
		return SAVE_EXTENSION;
	}

	/**
	 * Returns the file extension used for writing tabs.
	 * @return the WRITE_EXTENSION
	 */
	public String getWRITE_EXTENSION() {
		return WRITE_EXTENSION;
	}
}

