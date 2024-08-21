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

package com.horvath.pptdiffer;

import java.io.File;

import com.horvath.pptdiffer.command.io.ParsePptxCmd;
import com.horvath.pptdiffer.engine.model.PptxSlideShow;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Performs PPTX diff-ing operations and makes result data available. 
 * @author jhorvath 
 */
public final class Differ {

	private File rawFileA;
	private File rawFileB;
	
	private PptxSlideShow fileA;
	private PptxSlideShow fileB;

	/**
	 * Tracks if both files are the same exact file, including meta data. 
	 */
	boolean sameFile;
	
	/**
	 * Constructor. 
	 * @param fileA File
	 * @param fileB File 
	 */
	public Differ(File fileA, File fileB) throws PpdException {
		this.rawFileA = fileA;
		this.rawFileB = fileB;
		loadAndParseFiles();
	}
	
	/**
	 * Loads and parses data from files. 
	 * 
	 * @throws PpdException
	 */
	private void loadAndParseFiles() throws PpdException {
		ParsePptxCmd cmd = new ParsePptxCmd(rawFileA, rawFileB);
		cmd.perform();
		
		fileA = cmd.getFileModelA();
		fileB = cmd.getFileModelB();
	}

	public File getRawFileA() {
		return rawFileA;
	}

	public File getRawFileB() {
		return rawFileB;
	}

	public PptxSlideShow getFileA() {
		return fileA;
	}

	public PptxSlideShow getFileB() {
		return fileB;
	}

	public boolean isSameFile() {
		return sameFile;
	}
	 
}
