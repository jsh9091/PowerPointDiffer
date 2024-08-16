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

package com.horvath.pptdiffer.gui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.net.URL;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import com.horvath.pptdiffer.application.Debugger;

/**
 * Class that defines the main application window. 
 * @author jhorvath
 */
public class PpdWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static PpdWindow window = null;
	MainPanel mainPanel;
	
	// locations for application icon
	public static final String APP_ICON_16X = "/resources/PPD-icon-16px.png";
	public static final String APP_ICON_32X = "/resources/PPD-icon-64px.png";
	public static final String APP_ICON_64X = "/resources/PPD-icon-16px.png";
	public static final String APP_ICON_128X = "/resources/PPD-icon-128px.png";
	public static final String APP_ICON_512X = "/resources/PPD-icon-512px.png";
	
	/**
	 * Constructor. 
	 */
	private PpdWindow() {
		super();
		init();
	}
	
	/**
	 * Returns an instance of the window. 
	 * @return PpdWindow
	 */
	public static PpdWindow getWindow() {
		if (window == null) {
			window = new PpdWindow();
		}
		return window;
	}
	
	/**
	 * Initializes the main window. 
	 */
	private void init() {
		Debugger.printLog("Starting init of the GUI", this.getClass().getName());
		
		setTitle("PowerPoint Differ");
		
		setLayout(new BorderLayout());
		
		mainPanel = new MainPanel();
		
		setPreferredSize(mainPanel.getPreferredSize());
		setMinimumSize(new Dimension(mainPanel.getPreferredSize()));
		add(mainPanel);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// center window horizontally on screen
		this.setLocation(dim.width / 2 - this.getSize().width / 2, 
				// center the window vertically in the top half of the screen
				dim.height / 4 - this.getSize().height / 4);
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		URL url = PpdWindow.class.getResource(APP_ICON_512X);
		ImageIcon icon = new ImageIcon(url);
		
		setIconImage(icon.getImage());
	}
	
	/**
	 * Updates the contents of the application window.
	 */
	public void updateGUI() {
		mainPanel.updatePanel();
	}
	
	/**
	 * Disables the GUI with a wait cursor. 
	 * Intended to be used during complicated operations. 
	 */
	public void guiWait() {
		Debugger.printLog("Pausing the GUI", this.getClass().getName());
		Component glasspane = this.getGlassPane();
		glasspane.setVisible(true);
		glasspane.addMouseListener(new MouseAdapter() {});
		glasspane.addKeyListener(new KeyAdapter() {});
		glasspane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
	
	/**
	 * Restores the user's ability to interact with the main GUI window. 
	 */
	public void guiResume() {
		Debugger.printLog("Resuming the GUI", this.getClass().getName());
		Component glasspane = this.getGlassPane();
		glasspane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		glasspane.setVisible(false);
	}
	
	/**
	 * Displays a simple pop-up message with an OK button. 
	 * @param title String 
	 * @param message String
	 */
	public void simpleMessagePopup(String title, String message) {
		simpleMessagePopup(title, message, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Displays a simple pop-up message with an OK button. 
	 * Configurable with a level setting (Example: info, warning, error).
	 * @param title String 
	 * @param message String 
	 * @param level int (recommend using JOptionPane level constant)
	 */
	public void simpleMessagePopup(String title, String message, int level) {
		Debugger.printLog("Displaying Pop-up: Title: " + title + " Message: " 
				+ message, this.getClass().getName());
		
		if (level == JOptionPane.INFORMATION_MESSAGE) {
			// display the message with application icon
			JOptionPane.showMessageDialog(this, message, title, level, getAppIcon());
			
		} else {
			// for non-information levels, show default icons
			JOptionPane.showMessageDialog(this, message, title, level);
		}
	}

	public MainPanel getMainPanel() {
		return mainPanel;
	}
	
	/**
	 * Returns a scaled version of the application appropriate for display in a JOptionPane.
	 * @return ImageIcon 
	 */
	public static ImageIcon getAppIcon() {
		URL url = PpdWindow.class.getResource(APP_ICON_128X);
		ImageIcon icon = new ImageIcon(url);
		// scale the image so it looks good
		Image image = icon.getImage();
		Image scaledImage = image.getScaledInstance(60, 60, Image.SCALE_DEFAULT);
		return new ImageIcon(scaledImage);
	}

}
