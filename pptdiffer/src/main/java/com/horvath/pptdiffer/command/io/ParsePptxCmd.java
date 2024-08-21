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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.poi.xslf.usermodel.XMLSlideShow;

import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.command.PpdCommand;
import com.horvath.pptdiffer.engine.model.PptxSlideShow;
import com.horvath.pptdiffer.exception.PpdException;

public final class ParsePptxCmd extends PpdCommand {
	
	// standard file objects to read in
	private File rawFileA;
	private File rawFileB;
	
	// slide-show files for internal processing
	private XMLSlideShow pptA;
	private XMLSlideShow pptB;
	
	// PPD data models to return
	private PptxSlideShow fileModelA;
	private PptxSlideShow fileModelB;
	
	public static final String ERROR_FILE_NULL = "File cannot be null:";
	public static final String ERROR_FILE_NOT_EXIST = "File does not exist:";
	public static final String ERROR_FILE_NOT_PPTX = "File is not a .pptx file:";
	
	/**
	 * Constructor. 
	 * @param fileA File 
	 * @param fileB File 
	 */
	public ParsePptxCmd(File fileA, File fileB) {
		this.rawFileA = fileA;
		this.rawFileB = fileB;
		this.fileModelA = new PptxSlideShow();
		this.fileModelB = new PptxSlideShow();
	}

	@Override
	public void perform() throws PpdException {
		
		Debugger.printLog("Load and parse PPTX files",this.getClass().getName());
		
		success = false;

		nullCheck();
		filesExistsCheck();
		filesArePptxCheck();
		// TODO exact same file check
		
		loadPptxFiles();
		
		this.fileModelA.setFileName(rawFileA.getName());
		this.fileModelB.setFileName(rawFileB.getName());
		
		success = true;
	}
	
	/**
	 * Performs null checking operations on raw files.
	 * 
	 * @throws PpdException
	 */
	private void nullCheck() throws PpdException {
		if (rawFileA == null || rawFileB == null) {
			String message = ERROR_FILE_NULL;
			if (rawFileA == null) {
				message = message + " file A"; 
			}
			if (rawFileB == null) {
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
	private void filesExistsCheck() throws PpdException {
		if (!rawFileA.exists() || !rawFileB.exists()) {
			String message = ERROR_FILE_NOT_EXIST;
			if (!rawFileA.exists()) {
				message = message + " " + rawFileA.getName();
			}
			if (!rawFileB.exists()) {
				message = message + " " + rawFileB.getName();
			}
			throw new PpdException(message);
		}
	}
	
	
	/**
	 * Checks that the provided files are PowerPoint (.PPTX) files. 
	 * 
	 * @throws PpdException 
	 */
	private void filesArePptxCheck() throws PpdException {
		final String suffix = ".pptx";
		if (!rawFileA.getName().toLowerCase().endsWith(suffix) || 
				!rawFileB.getName().toLowerCase().endsWith(suffix)) {
			
			String message = ERROR_FILE_NOT_PPTX;
			
			if (!rawFileA.getName().toLowerCase().endsWith(suffix)) {
				message = message + " " + rawFileA.getName();
			}
			if (!rawFileB.getName().toLowerCase().endsWith(suffix)) {
				message = message + " " + rawFileB.getName();
			}
			throw new PpdException(message);
		}
	}
	
	/**
	 * Reads in the file data from disk, and creates POI slide-show files 
	 * for processing.
	 * 
	 * @throws PpdException
	 */
	private void loadPptxFiles() throws PpdException {
		Debugger.printLog("loadPptxFiles()", this.getClass().getName());

		try (FileInputStream fisA = new FileInputStream(this.rawFileA);
				FileInputStream fisB = new FileInputStream(this.rawFileA)) {

			this.pptA = new XMLSlideShow(fisA);
			this.pptB = new XMLSlideShow(fisB);

		} catch (IOException ex) {
			Debugger.printLog(ex.getMessage(), this.getClass().getName(), Level.SEVERE);
			throw new PpdException(ex.getMessage(), ex);
		}
	}

	public PptxSlideShow getFileModelA() {
		return fileModelA;
	}

	public PptxSlideShow getFileModelB() {
		return fileModelB;
	}

}
