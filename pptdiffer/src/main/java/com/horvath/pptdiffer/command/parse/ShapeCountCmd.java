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

package com.horvath.pptdiffer.command.parse;

import org.apache.poi.xslf.usermodel.XSLFSlide;

import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.command.io.AbstractFileLoader;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Command for counting shapes on a given slide. 
 */
public class ShapeCountCmd extends AbstractFileLoader {
	
	private XSLFSlide slide;
	private int count;
	public final static String ERROR_SLIDE_NULL = "The slide cannot be null.";

	/**
	 * Constructor. 
	 * @param slide XSLFSlide
	 */
	public ShapeCountCmd(XSLFSlide slide) {
		this.slide = slide;
	}
	
	@Override
	public void perform() throws PpdException {
		Debugger.printLog("Count shapes on slide.", this.getClass().getName());
		
		if (this.slide == null) {
			throw new PpdException(ERROR_SLIDE_NULL);
		}
		
		this.count = slide.getShapes().size();
	}

	/**
	 * Returns the result data for the number of shapes on the given slide.
	 * @return int
	 */
	public int getCount() {
		return count;
	}

}
