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

import javax.swing.filechooser.FileSystemView;

/**
 * Shared functionality for Open As and Save As dialogs. 
 * @author jhorvath
 */
public abstract class OpenSaveAsAction extends PpdAction {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Establishes the most recent folder location for Open / Save As dialogs. 
	 * Initially set to the root level of the user's OS user folder.
	 */
	private static String lastFolder = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

	protected static String getLastFolder() {
		return lastFolder;
	}

	protected static void setLastFolder(String lastFolderPath) {
		lastFolder = lastFolderPath;
	}

}
