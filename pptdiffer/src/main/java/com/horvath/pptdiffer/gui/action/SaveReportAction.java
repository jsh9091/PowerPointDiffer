/*
 * MIT License
 * 
 * Copyright (c) 2024 Joshua Horvath
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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.command.io.WriteReportCmd;
import com.horvath.pptdiffer.exception.PpdException;
import com.horvath.pptdiffer.gui.PpdWindow;

/**
 * Action for saving a file comparison report.
 * @author jhorvath
 */
public class SaveReportAction extends OpenSaveAsAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent event) {
		Debugger.printLog("Allow user to set report name and location. ", this.getClass().getName());

		// allow user to set a file name and location for report 
		JFileChooser chooser = new JFileChooser(getLastFolder());
		chooser.setDialogTitle("Save Report File");
		
		chooser.setSelectedFile(new File("report.txt"));
		
		// display the dialog for user to select a file
		int returnValue = chooser.showSaveDialog(PpdWindow.getWindow());

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			// get the file selected by the user
			File file = chooser.getSelectedFile();
			PpdWindow window = PpdWindow.getWindow();
			
			try {
				window.guiWait();
				
				// call and execute command 
				WriteReportCmd cmd = new WriteReportCmd(file);
				cmd.perform();

			} catch (PpdException ex) {
				Debugger.printLog(ex.getLocalizedMessage(), this.getClass().getName());
				window.guiResume();
				window.simpleMessagePopup("Write Error", 
						"There was a problem writing the report file.", JOptionPane.ERROR_MESSAGE);

			} finally {
				window.guiResume();
			}
		}
	}
}
