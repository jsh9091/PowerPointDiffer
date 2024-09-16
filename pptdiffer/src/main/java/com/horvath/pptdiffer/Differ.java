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

import org.apache.poi.xslf.usermodel.XMLSlideShow;

import com.horvath.pptdiffer.command.compare.GenerateReportTextCmd;
import com.horvath.pptdiffer.command.compare.GetSlideNameForCompareCmd;
import com.horvath.pptdiffer.command.compare.GetSlideTextForCompareCmd;
import com.horvath.pptdiffer.command.io.LoadPptxCmd;
import com.horvath.pptdiffer.command.parse.ParsePptxCmd;
import com.horvath.pptdiffer.engine.model.PptxSlideShow;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Performs PPTX diff-ing operations and makes result data available. 
 * @author jhorvath 
 */
public final class Differ {

	private File rawFileA;
	private File rawFileB;
	
	private XMLSlideShow poiXmlFileA;
	private XMLSlideShow poiXmlFileB;
	
	private PptxSlideShow ppdFileA;
	private PptxSlideShow ppdFileB;

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
		loadFiles();
		parseFiles();
	}
	
	/**
	 * Loads files and parses data into POI XML objects.  
	 * 
	 * @throws PpdException
	 */
	private void loadFiles() throws PpdException {
		LoadPptxCmd cmd = new LoadPptxCmd(rawFileA, rawFileB);
		cmd.perform();
		
		poiXmlFileA = cmd.getPoiFileA();
		poiXmlFileB = cmd.getPoiFileB();
		sameFile = cmd.isExactlySameFile();
	}
	
	/**
	 * Parses POI XML objects into PPD model objects. 
	 */
	private void parseFiles() throws PpdException {
		ParsePptxCmd cmd = new ParsePptxCmd(this.poiXmlFileA, this.poiXmlFileB);
		cmd.perform();
		
		this.ppdFileA = cmd.getPpdFileA();
		this.ppdFileA.setFileName(this.rawFileA.getName());
		
		this.ppdFileB = cmd.getPpdFileB();
		this.ppdFileB.setFileName(this.rawFileB.getName());
	}
	
	/**
	 * Generates and returns an analysis report of the comparisons of the two files. 
	 * 
	 * @return String
	 * @throws PpdException
	 */
	public String generateReport() throws PpdException {
		String result = "";
		
		GenerateReportTextCmd cmd = new GenerateReportTextCmd(this);
		cmd.perform();
		
		if (cmd.isSuccess()) {
			result = cmd.getReportText();
		}
		
		return result;
	}
	
	/* File & slide Comparisons section */ 
	
	public boolean isSameFile() {
		return sameFile;
	}

	/**
	 * Returns the number of slides in File A.
	 * @return int
	 */
	public int slideCount_fileA() {
		return this.ppdFileA.getSlideList().size();
	}
	
	/**
	 * Returns the number of slides in File B.
	 * @return int
	 */
	public int slideCount_fileB() {
		return this.ppdFileB.getSlideList().size();
	}
	
	/**
	 * Gets the name of the slide in File A for a given index.
	 * 
	 * @param index int 
	 * @return String
	 * @throws PpdException
	 */
	public String slideName_fileA(int index) throws PpdException {		
		GetSlideNameForCompareCmd cmd = new GetSlideNameForCompareCmd(index, this.ppdFileA);
		cmd.perform();
		
		return cmd.getSlideName();
	}
	
	/**
	 * Gets the name of the slide in File B for a given index.
	 * Zero based index value. 
	 * 
	 * @param index int 
	 * @return String 
	 * @throws PpdException
	 */
	public String slideName_fileB(int index) throws PpdException {
		GetSlideNameForCompareCmd cmd = new GetSlideNameForCompareCmd(index, this.ppdFileB);
		cmd.perform();
		
		return cmd.getSlideName();
	}
	
	/**
	 * Gets the parsed text from File A for a given index value. 
	 * Parsed text does not include same whitespace from actual file. 
	 * Zero based index value. 
	 * 
	 * @param index int
	 * @return String
	 * @throws PpdException
	 */
	public String slideText_fileA(int index) throws PpdException {
		GetSlideTextForCompareCmd  cmd = new GetSlideTextForCompareCmd(index, this.ppdFileA);
		cmd.perform();
		
		return cmd.getSlideText();
	}

	/**
	 * Gets the parsed text from File B for a given index value. 
	 * Parsed text does not include same whitespace from actual file. 
	 * Zero based index value. 
	 * 
	 * @param index int
	 * @return String
	 * @throws PpdException
	 */
	public String slideText_fileB(int index) throws PpdException {
		GetSlideTextForCompareCmd  cmd = new GetSlideTextForCompareCmd(index, this.ppdFileB);
		cmd.perform();
		
		return cmd.getSlideText();
	}
	
	/* Getters and Setters section */ 

	protected XMLSlideShow getPoiXmlFileA() {
		return poiXmlFileA;
	}

	protected XMLSlideShow getPoiXmlFileB() {
		return poiXmlFileB;
	}

	/**
	 * Returns the parsed data model for File A, upon which comparison tests can be performed. 
	 * @return PptxSlideShow
	 */
	public PptxSlideShow getPpdFileA() {
		return ppdFileA;
	}

	/**
	 * Returns the parsed data model for File B, upon which comparison tests can be performed. 
	 * @return PptxSlideShow
	 */
	public PptxSlideShow getPpdFileB() {
		return ppdFileB;
	}
	 
}
