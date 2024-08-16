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

package com.horvath.pptdiffer.command.io;

import java.io.File;

import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.application.PpdState;
import com.horvath.pptdiffer.command.PpdCommand;
import com.horvath.pptdiffer.exception.PpdException;
import com.horvath.pptdiffer.io.TextFileWriter;

/**
 * Command for building final report and writing file to disk.
 * @author jhorvath
 */
public class WriteReportCmd extends PpdCommand {
	
	private File file;
	
	public static final String ERROR_NULL_FILE = "The report file must not be null.";
	private static final String EOL =  System.lineSeparator();
	
	/**
	 * Constructor. 
	 * @param file File 
	 */
	public WriteReportCmd(File file) {
		this.file = file;
	}

	@Override
	public void perform() throws PpdException {
		Debugger.printLog("Command for outputting report file.", this.getClass().getName());
		
		if (this.file == null) {
			throw new PpdException(ERROR_NULL_FILE);
		}
		
		PpdState state = PpdState.getInstance();
		
		StringBuilder sb = new StringBuilder();
		sb.append("PowerPoint File Comparison Report"); 
		sb.append(EOL);
		sb.append(EOL);
		sb.append("File A: ");
		sb.append(state.getFileA().getName());
		sb.append(EOL);
		sb.append("File B: ");
		sb.append(state.getFileB().getName());
		sb.append(EOL);
		sb.append(EOL);
		sb.append(state.getReport());
		
		final String report = sb.toString();
		
		TextFileWriter writer = new TextFileWriter(report, this.file);
		writer.write();
	}

}
