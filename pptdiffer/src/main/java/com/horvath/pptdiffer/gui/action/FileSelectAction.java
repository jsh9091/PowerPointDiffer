/*
 * MIT License
 * 
 * Copyright (c) 2021 Joshua Horvath
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.horvath.pptdiffer.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.application.PpdState;
import com.horvath.pptdiffer.command.loadfile.LoadFileCmd;
import com.horvath.pptdiffer.gui.PpdWindow;
import com.horvath.pptdiffer.utility.FileMode;

/**
 * Action class for allowing users to select files for comparing.
 * @author jhorvath
 */
public class FileSelectAction extends OpenSaveAsAction {

	private static final long serialVersionUID = 1L;
	
	private FileMode mode;
	
	/**
	 * Constructor. 
	 * @param fileMode FileMode
	 */
	public FileSelectAction(FileMode fileMode) {
		this.mode = fileMode;
	}

	/**
	 * Allows user to select a file to be later compared in a diff operation.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Debugger.printLog("Offer user option to select file: " + this.mode, this.getClass().getName());
		
		// create the "open as" dialog
		JFileChooser chooser = new JFileChooser(getLastFolder());
		chooser.setDialogTitle("Select a PowerPoint file");
		
		// automatically filter out non-PPTX files
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PowerPoint Files", "pptx", "PPTX");
		chooser.setFileFilter(filter);
		
		// display the dialog for user to select a file
		int returnValue = chooser.showOpenDialog(null);
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			
			// update the folder location for future Open/Save As dialogs
			setLastFolder(selectedFile.getParentFile().getAbsolutePath());
			
			// verify that the user actually gave us a PowerPoint file
			if (!selectedFile.getName().toLowerCase().endsWith("pptx")) {

				final String wrongFiletypeMessage = "The selected file " 
						+ selectedFile.getName() 
						+ " was not a valid PowerPoint (.pptx) file.";
				
				Debugger.printLog(wrongFiletypeMessage, this.getClass().getName(), Level.WARNING);
				PpdWindow.getWindow().simpleMessagePopup("Wrong File Type", 
						wrongFiletypeMessage, JOptionPane.WARNING_MESSAGE);
			
			} else {
				// we have a valid looking PowerPoint file
				
				if (checkFileAlreadyLoaded(selectedFile)) {
					// the user as selected a file that has already been loaded
					final String alreadyLoadedMessagePart1 = "The selected file ";
				    final String alreadyLoadedMessagePart2 = " appears to have been already loaded.";
					
					Debugger.printLog(alreadyLoadedMessagePart1 
							+ selectedFile.getAbsolutePath() 
							+ alreadyLoadedMessagePart2, 
							this.getClass().getName(), Level.WARNING);
					PpdWindow.getWindow().simpleMessagePopup("File Selection Problem", 
							alreadyLoadedMessagePart1 
							+ selectedFile.getName() 
							+ alreadyLoadedMessagePart2, JOptionPane.WARNING_MESSAGE);

				} else {
					// create command to update the state
					new LoadFileCmd(this.mode, selectedFile).perform();
									
					// prompt GUI to update the window 
					PpdWindow.getWindow().updateGUI();
				}
			}
		}
	}

	/**
	 * Checks if the selected file has already been uploaded into the state in the opposite 
	 * file mode. Returns true if the file has already been loaded, false otherwise.
	 * Note: If the user attempts to select a file for file A that has already been 
	 * uploaded as file A, the method will return false.
	 * @param file File
	 * @return boolean 
	 */
	private boolean checkFileAlreadyLoaded(File file) {
		boolean fileAreadyLoaded = false;
		
		final String filePath = file.getAbsolutePath();
		PpdState state = PpdState.getInstance();
		
		if (mode == FileMode.File_A) {
			// check against file B
			if (state.getFileB() != null && filePath.equals(state.getFileB().getAbsolutePath())) {
				fileAreadyLoaded = true;
			}
			
		} else if (mode == FileMode.File_B) {
			// check against file A
			if (state.getFileA() != null && filePath.equals(state.getFileA().getAbsolutePath())) {
				fileAreadyLoaded = true;
			}
		}
		
		return fileAreadyLoaded;
	}
}
