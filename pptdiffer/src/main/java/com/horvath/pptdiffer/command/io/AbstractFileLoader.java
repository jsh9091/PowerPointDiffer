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

package com.horvath.pptdiffer.command.io;

import java.io.File;

import com.horvath.pptdiffer.command.PpdCommand;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Abstract class for shared code in verifying PPTX files for reading.
 * @author jhorvath 
 */
public abstract class AbstractFileLoader extends PpdCommand {

	public static final String ERROR_FILE_NULL = "File cannot be null:";
	public static final String ERROR_FILE_NOT_EXIST = "File does not exist:";
	public static final String ERROR_FILE_NOT_PPTX = "File is not a .pptx file:";

	/**
	 * Performs null checking operations on raw files.
	 * 
	 * @throws PpdException
	 */
	protected void nullCheck(File fileA, File fileB) throws PpdException {
		if (fileA == null || fileB == null) {
			String message = ERROR_FILE_NULL;
			if (fileA == null) {
				message = message + " file A"; 
			}
			if (fileB == null) {
				message = message + " file B"; 
			}
			throw new PpdException(message);
		}
	}
	
	/**
	 * Verifies that both files actually exist. 
	 * 
	 * @throws PpdException
	 */
	protected void filesExistsCheck(File fileA, File fileB) throws PpdException {
		if (!fileA.exists() || !fileB.exists()) {
			String message = ERROR_FILE_NOT_EXIST;
			if (!fileA.exists()) {
				message = message + " " + fileA.getName();
			}
			if (!fileB.exists()) {
				message = message + " " + fileB.getName();
			}
			throw new PpdException(message);
		}
	}
	
	/**
	 * Checks that the provided files are PowerPoint (.PPTX) files. 
	 * 
	 * @throws PpdException 
	 */
	protected void filesArePptxCheck(File fileA, File fileB) throws PpdException {
		final String suffix = ".pptx";
		if (!fileA.getName().toLowerCase().endsWith(suffix) || 
				!fileB.getName().toLowerCase().endsWith(suffix)) {
			
			String message = ERROR_FILE_NOT_PPTX;
			
			if (!fileA.getName().toLowerCase().endsWith(suffix)) {
				message = message + " " + fileA.getName();
			}
			if (!fileB.getName().toLowerCase().endsWith(suffix)) {
				message = message + " " + fileB.getName();
			}
			throw new PpdException(message);
		}
	}
	
}
