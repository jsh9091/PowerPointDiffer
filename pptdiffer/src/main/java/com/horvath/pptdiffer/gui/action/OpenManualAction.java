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

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.gui.PpdWindow;

/**
 * Action for opening user manual from the GUI window. 
 * @author jhorvath
 */
public class OpenManualAction extends PpdAction {

	private static final long serialVersionUID = 1L;

	protected static final String USER_MANUAL = "PPD_Manual.pdf";
	protected static final String USER_MANUAL_LOCATION = "/resources/" + USER_MANUAL;

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			openManual(USER_MANUAL_LOCATION);
		} catch (IOException ex) {
			final String message = "Error opening user manual.";
			PpdWindow.getWindow().simpleMessagePopup("Error", message, JOptionPane.WARNING_MESSAGE);
			Debugger.printLog(message, this.getClass().getName(), Level.WARNING);
		}
	}

	/**
	 * Opens the manual in default application.
	 * 
	 * @param path String
	 * @throws IOException
	 */
	public void openManual(final String path) throws IOException {
		if (Desktop.isDesktopSupported()) {
			File tempFile = new File(USER_MANUAL);

			try (InputStream is = OpenManualAction.class.getResourceAsStream(path);
					FileOutputStream fos = new FileOutputStream(tempFile)) {

				// write out temporary file
				while (is.available() > 0) {
					fos.write(is.read());
				}

				// open the file in the default application for file type
				Desktop.getDesktop().open(tempFile);
				tempFile.deleteOnExit();
			}
		}
	}

}
