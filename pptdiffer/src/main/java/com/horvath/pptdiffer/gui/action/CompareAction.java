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

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.application.PpdState;
import com.horvath.pptdiffer.command.compare.CompareCmd;
import com.horvath.pptdiffer.gui.PpdWindow;

/**
 * Calls the command to run the difference operations.
 * @author jhorvath
 */
public class CompareAction extends PpdAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		Debugger.printLog("Action for comparing files.", this.getClass().getName());

		PpdState state = PpdState.getInstance();
		
		if (state.isReadForDiff()) {
			PpdWindow window = PpdWindow.getWindow();
			// lock the GUI with a 'wait' cursor
			window.guiWait();
			
			// run the comparison between the files
			CompareCmd cmd = new CompareCmd();
			cmd.perform();
			
			// unlock the GUI and restore the cursor 
			window.guiResume();

			if (cmd.isSuccess()) {
				// prepare dialog contents
				JTextArea textArea = new JTextArea(PpdState.getInstance().getReport());
//				textArea.setFont(font);
				textArea.setEditable(false);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				textArea.setMargin(new Insets(5,5,5,5));
				JScrollPane scrollPane = new JScrollPane(textArea);
				scrollPane.setPreferredSize(new Dimension(300, 200));
				String[] buttons = {"Save Report", "Close"};

				// display report dialog
				int userResponse = JOptionPane.showOptionDialog(window, scrollPane, 
						"Comparison Report", 0, JOptionPane.INFORMATION_MESSAGE, PpdWindow.getAppIcon(), buttons, null); 

				
				// if user wants to save the report
				if (userResponse == 0) {
					JButton btn = new JButton();
					btn.setAction(new SaveReportAction());
					btn.doClick();
				}

			} else {
				// there was a problem performing the comparison 
				Debugger.printLog(cmd.getMessage(), this.getClass().getName(), Level.SEVERE);
				PpdWindow.getWindow().simpleMessagePopup("Application Error", cmd.getMessage());
			}
			
		} else {
			Debugger.printLog("Action called when state not ready for diff.", this.getClass().getName(), Level.WARNING);
		}
	}

}
