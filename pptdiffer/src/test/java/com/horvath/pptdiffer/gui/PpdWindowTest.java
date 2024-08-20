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

package com.horvath.pptdiffer.gui;

import com.horvath.pptdiffer.engine.AbstractTestHelper;
import com.horvath.pptdiffer.application.PpdState;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

/**
 * Lightweight testing of GUI window. 
 * Avoids clicking buttons that would cause other dialogs to appear.
 * @author jhorvath
 */
public class PpdWindowTest extends AbstractTestHelper {
	
	@Test
	public void ppdWindow_loadFiles_GuiUpdated() {
		PpdState state = PpdState.getInstance();
		state.setFileA(null);
		state.setFileB(null);
		
		PpdWindow window = PpdWindow.getWindow();
		// setting false is not necessary, but makes clear that the window will not be displayed visually during test
		window.setVisible(false);

		// the text fields are never hand editable
		Assert.assertFalse(window.getMainPanel().getFileA_TextField().isEditable());
		Assert.assertFalse(window.getMainPanel().getFileB_TextField().isEditable());

		// the text fields for File A and File B are empty by default
		Assert.assertTrue(window.getMainPanel().getFileA_TextField().getText().isEmpty());
		Assert.assertTrue(window.getMainPanel().getFileB_TextField().getText().isEmpty());
		
		// the tool-tips for the fields are null
		Assert.assertNull(window.getMainPanel().getFileA_TextField().getToolTipText());
		Assert.assertNull(window.getMainPanel().getFileB_TextField().getToolTipText());
		
		// compare button should not enabled until two files have become selected
		Assert.assertFalse(window.getMainPanel().getCompareBtn().isEnabled());
		
		// manually set a file A in the state
		File fileA = new File(BASIC_FILE_A);
		state.setFileA(fileA);
		
		// tell the window to update
		window.updateGUI();
		
		// build a control value
		String fileAControl = fileA.getParentFile().getName() + File.separator + fileA.getName();
		
		// check that the GUI updated as expected
		Assert.assertEquals(fileAControl, window.getMainPanel().getFileA_TextField().getText());
		Assert.assertEquals(fileA.getAbsolutePath(), window.getMainPanel().getFileA_TextField().getToolTipText());
		
		// compare button should not enabled until two files have become selected
		Assert.assertFalse(window.getMainPanel().getCompareBtn().isEnabled());
		
		// manually set a file A in the state
		File fileB = new File(BASIC_FILE_B);
		state.setFileB(fileB);
		
		// tell the window to update
		window.updateGUI();
		
		// build a control value
		String fileBControl = fileB.getParentFile().getName() + File.separator + fileB.getName();
		
		// check that the GUI updated as expected
		Assert.assertEquals(fileBControl, window.getMainPanel().getFileB_TextField().getText());
		Assert.assertEquals(fileB.getAbsolutePath(), window.getMainPanel().getFileB_TextField().getToolTipText());
		
		// compare button should now be enabled as two files are selected
		Assert.assertTrue(window.getMainPanel().getCompareBtn().isEnabled());

		// the text fields are never hand editable
		Assert.assertFalse(window.getMainPanel().getFileA_TextField().isEditable());
		Assert.assertFalse(window.getMainPanel().getFileB_TextField().isEditable());
	}

}
