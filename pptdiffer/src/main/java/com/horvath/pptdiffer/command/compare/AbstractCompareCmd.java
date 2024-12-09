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

package com.horvath.pptdiffer.command.compare;

import com.horvath.pptdiffer.command.PpdCommand;
import com.horvath.pptdiffer.engine.model.PptxSlideShow;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Abstract class for comparing slide data. 
 * @author jhorvath 
 */
public abstract class AbstractCompareCmd extends PpdCommand {
	
	public final static String ERROR_NULL_SLIDESHOW = "PptxSlideShow must not be null.";
	public final static String ERROR_NEGATIVE_INDEX = "Index cannot be a negative number.";
	
	/**
	 * Helper method to validate given slide index.
	 * Returns true if the given index exists in the slide count of the given PowerPoint file (zero based index).
	 * 
	 * @param index int 
	 * @param slideshow PptxSlideShow
	 * @throws PpdException
	 * @return boolean
	 */
	protected boolean rangeCheck(int index, PptxSlideShow slideshow) throws PpdException {
		boolean result = false;
		
		if (slideshow == null) {
			throw new PpdException(ERROR_NULL_SLIDESHOW);
		}
		
		if (index < 0) {
			throw new PpdException(ERROR_NEGATIVE_INDEX);
		}
		
		try {
			slideshow.getSlideList().get(index);
			// if we get here, then in range
			result = true;
			
		} catch (IndexOutOfBoundsException ex) {
			return false;
			
		} catch (Exception ex) {
			throw new PpdException("Unexpected Exception in: " + slideshow.getFileName() + " " + ex.getMessage(), ex);
		}
		return result; 
	}

}
