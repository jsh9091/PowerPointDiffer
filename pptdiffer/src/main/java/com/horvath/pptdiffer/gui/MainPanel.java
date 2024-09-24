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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.horvath.pptdiffer.application.PpdState;
import com.horvath.pptdiffer.gui.action.CompareAction;
import com.horvath.pptdiffer.gui.action.FileSelectAction;
import com.horvath.pptdiffer.gui.action.OpenManualAction;
import com.horvath.pptdiffer.utility.FileMode;  

/**
 * Panel that defines the main components of the application window.
 * @author jhorvath
 */
public final class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JLabel fileA_Label;
	private JTextField fileA_TextField;
	private JButton fileA_SelectBtn;
	
	private JLabel fileB_Label;
	private JTextField fileB_TextField;
	private JButton fileB_SelectBtn;
	
	private JButton helpBtn;
	private JPanel helpPanel;
	private JButton compareBtn;
	
	/**
	 * Constructor.
	 */
	public MainPanel() {
		super();
		
		initializePanel();
		initializeComponents();
		configureComponents();
		layoutComponents();
	}
	
	/**
	 * Updates the contents of the panel. 
	 */
	public void updatePanel() {
		configureComponents();
	}

	/**
	 * Initializes the dialog itself.
	 */
	private void initializePanel() {
		final int midWidth = 600;
		final int height = 190;
		
		this.setMinimumSize(new Dimension(midWidth, height));
		this.setPreferredSize(new Dimension(midWidth, height));

		setLayout(new GridBagLayout());
	}
	
	/**
	 * Initializes the components of the dialog. 
	 */
	private void initializeComponents() {
		fileA_Label = new JLabel();
		fileA_TextField = new JTextField();
		fileA_SelectBtn = new JButton();
		fileA_SelectBtn.addActionListener(
				new FileSelectAction(FileMode.File_A));
		
		fileB_Label = new JLabel();
		fileB_TextField = new JTextField();
		fileB_SelectBtn = new JButton();
		fileB_SelectBtn.addActionListener(
				new FileSelectAction(FileMode.File_B));
		
		helpBtn = new JButton();
		helpBtn.addActionListener(new OpenManualAction());
		helpPanel = new JPanel();
		helpPanel.setLayout(new BorderLayout());
		helpPanel.add(helpBtn, BorderLayout.WEST);
		
		compareBtn = new JButton();
		compareBtn.addActionListener(new CompareAction());
	}
	
	/**
	 * Configures the initial settings of the GUI components.
	 */
	private void configureComponents() {
		final String selectButtonText = "Select PPTX";
		PpdState state = PpdState.getInstance();
		
		fileA_Label.setText("File A");
		fileA_TextField.setEditable(false);
		setTextFieldValue(fileA_TextField, state.getFileA());
		fileA_SelectBtn.setText(selectButtonText);
		
		fileB_Label.setText("File B");
		fileB_TextField.setEditable(false);
		setTextFieldValue(fileB_TextField, state.getFileB());
		fileB_SelectBtn.setText(selectButtonText);
		
		helpBtn.setText("Help");
		helpBtn.setToolTipText("Open help documentation.");
		
		compareBtn.setText("Compare");
		compareBtn.setToolTipText("With two files selected, click to compare the files.");
		compareBtn.setEnabled(state.isReadForDiff());
	}
	
	/**
	 * Performs layout operations of the GUI components within the panel.
	 */
	private void layoutComponents() {
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.PAGE_START;
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(fileA_Label, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 0.5;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(fileA_TextField, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		gbc.insets = new Insets(0, 20, 0, 0);
		add(fileA_SelectBtn, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		gbc.insets = new Insets(10, 0, 0, 0);
		add(fileB_Label, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(fileB_TextField, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 3;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		gbc.insets = new Insets(0, 20, 0, 0);
		add(fileB_SelectBtn, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.insets = new Insets(10, 0, 0, 0);
		add(helpPanel, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 3;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		gbc.insets = new Insets(10, 20, 0, 0);
		add(compareBtn, gbc);
	}

	/**
	 * Sets the content of the file text field based on application state.
	 * @param textField JTextField
	 * @param file File 
	 */
	private void setTextFieldValue(JTextField textField, File file) {
		if (file == null) {
			textField.setText("");
			textField.setToolTipText(null);
			
		} else {
			textField.setText(file.getParentFile().getName() + File.separator + file.getName());
			textField.setToolTipText(file.getAbsolutePath());
		}
	}

	public JTextField getFileA_TextField() {
		return fileA_TextField;
	}

	public JButton getFileA_SelectBtn() {
		return fileA_SelectBtn;
	}

	public JTextField getFileB_TextField() {
		return fileB_TextField;
	}

	public JButton getFileB_SelectBtn() {
		return fileB_SelectBtn;
	}

	public JButton getCompareBtn() {
		return compareBtn;
	}

	@Override
	public String toString() {
		return "MainPanel [fileA_Label=" + fileA_Label.getText() + ", fileA_TextField=" + fileA_TextField.getText()
				+ ", fileA_SelectBtn=" + fileA_SelectBtn.getText() + ", fileB_Label=" + fileB_Label.getText()
				+ ", fileB_TextField=" + fileB_TextField.getText() + ", fileB_SelectBtn=" + fileB_SelectBtn.getText()
				+ ", compareBtn=" + compareBtn.getText() + "]";
	}
	
}
