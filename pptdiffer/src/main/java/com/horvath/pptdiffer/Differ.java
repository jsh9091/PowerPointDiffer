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
	 * Returns the number of slides in File A.
	 * @return int
	 */
	public int fileA_SlideCount() {
		return this.ppdFileA.getSlideList().size();
	}
	
	/**
	 * Returns the number of slides in File B.
	 * @return int
	 */
	public int fileB_SlideCount() {
		return this.ppdFileB.getSlideList().size();
	}

	public File getRawFileA() {
		return rawFileA;
	}

	public File getRawFileB() {
		return rawFileB;
	}

	public XMLSlideShow getPoiXmlFileA() {
		return poiXmlFileA;
	}

	public XMLSlideShow getPoiXmlFileB() {
		return poiXmlFileB;
	}

	public PptxSlideShow getPpdFileA() {
		return ppdFileA;
	}

	public PptxSlideShow getPpdFileB() {
		return ppdFileB;
	}

	public boolean isSameFile() {
		return sameFile;
	}
	 
}
