package com.darwinsys.swingui.framework;

import java.awt.Graphics;

/**
 * name and purpose.
 */
public interface Model {
	
	// FILE MENU STUFF 
	
	public void openFile(String string);
	
	public void newFile(String fileName);

	public void saveFile();
	
	public void saveFile(Object object);

	public void closeFile();

	public void doPrint(Graphics g);

	/**
	 * @return The current filename.
	 */
	public String getFileName();

	// EDIT MENU STUFF

	public void clearSelection();
	
	public void copySelection();

	public void pasteSelection();
	
	public void undo();
	
	public void redo();

	// MISCELLANY
	
	/**
	 * @param i
	 */
	public void exit(int i);

}
