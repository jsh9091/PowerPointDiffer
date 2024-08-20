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

package com.horvath.pptdiffer.command.loadfile;

import java.io.File;
import java.util.logging.Level;

import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.application.PpdState;
import com.horvath.pptdiffer.command.PpdCommand;
import com.horvath.pptdiffer.utility.FileMode;

/**
 * Loads the selected file into the application state.
 * @author jhorvath
 */
public class LoadFileCmd extends PpdCommand {
	
	private FileMode mode;
	private File file;
	
	public static final String ERROR_MODE_MUST_NOT_BE_NULL = "The file mode cannot be null.";
	public static final String ERROR_FILE_IS_NULL = "The file must not be null.";
	public static final String ERROR_FILE_NOT_FOUND = "The file was not found.";
	
	/**
	 * Constructor. 
	 * @param mode FileMode
	 * @param file File
	 */
	public LoadFileCmd(FileMode mode, File file) {
		this.mode = mode;
		this.file = file;
	}

	@Override
	public void perform() {
		// perform error checking
		if (this.mode == null) {
			this.message = ERROR_MODE_MUST_NOT_BE_NULL;
			Debugger.printLog(this.message, this.getClass().getName(), Level.WARNING);
			return;
		}
		if (file == null) {
			this.message = ERROR_FILE_IS_NULL;
			Debugger.printLog(this.message, this.getClass().getName(), Level.WARNING);
			return;
		}
		if (!file.exists()) {
			this.message = ERROR_FILE_NOT_FOUND;
			Debugger.printLog(this.message + " " + this.file.getAbsolutePath(), this.getClass().getName(), Level.WARNING);
			return;
		}
		
		Debugger.printLog("Load file " + this.file.getName() + " as " + this.mode, this.getClass().getName());
		
		PpdState state = PpdState.getInstance();

		if (this.mode == FileMode.File_A) {
			state.setFileA(file);

		} else if (this.mode == FileMode.File_B) {
			state.setFileB(file);
		}
		
		// because we are uploading a file, 
		// we want to be sure to clear any stale data from state
		state.setReport("");
	}
}
