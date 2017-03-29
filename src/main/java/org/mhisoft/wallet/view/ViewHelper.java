/*
 *
 *  * Copyright (c) 2014- MHISoft LLC and/or its affiliates. All rights reserved.
 *  * Licensed to MHISoft LLC under one or more contributor
 *  * license agreements. See the NOTICE file distributed with
 *  * this work for additional information regarding copyright
 *  * ownership. MHISoft LLC licenses this file to you under
 *  * the Apache License, Version 2.0 (the "License"); you may
 *  * not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied.  See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 *
 */

package org.mhisoft.wallet.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.mhisoft.wallet.model.WalletSettings;

/**
 * Description:
 *
 * @author Tony Xue
 * @since May, 2016
 */
public class ViewHelper {

	public static void setFontSize(List<Component> componetsList, int newFontSize) {
		WalletSettings.getInstance().setFontSize(newFontSize);
		for (Component component : componetsList) {
			Font original = component.getFont();
			Font newFont = original.deriveFont(Float.valueOf(newFontSize));
			component.setFont(newFont);

		}
	}

	public static void setFontSize(Component[] componetsList, int newFontSize) {
		if (componetsList != null)
			setFontSize(Arrays.asList(componetsList), newFontSize);
	}

	//set JDialog
	public static void setUIManagerFontSize() {
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, WalletSettings.getInstance().getFontSize());
		UIManager.put("OptionPane.messageFont", font);
		UIManager.put("OptionPane.buttonFont", font);
	}


	//recursivly set fonts.
	protected  static void setFileChooserFont(Component[] comp, int newFontSize) {
		for (int x = 0; x < comp.length; x++) {
			if (comp[x] instanceof Container)
				setFileChooserFont(((Container) comp[x]).getComponents(), newFontSize);
			try {
				Font original = comp[x].getFont();
				Font newFont = original.deriveFont(Float.valueOf(newFontSize));

				comp[x].setFont(newFont);


			} catch (Exception e) {
			}//do nothing
		}
	}

	/**
	 * Resgier allthe components in the jFrame.
	 *
	 * @param c
	 * @return
	 */
	public static List<Component> getAllComponents(final Container c) {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}

	public static void setFontSize(Container c, int newFontSize) {
		List<Component> components = getAllComponents(c);
		setFileChooserFont (components.toArray( new Component[components.size()] ), newFontSize);
	}


	public static String chooseFile(String defaultDir, String... extensions) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (defaultDir == null)
			defaultDir = WalletSettings.userHome;
		chooser.setCurrentDirectory(new File(defaultDir));
		if (extensions==null)
			extensions = new String[] {"dat"} ;
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("Wallet data files", extensions));

		//set font
		setFileChooserFont(chooser.getComponents(), WalletSettings.getInstance().getFontSize());

		int returnValue = chooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}


}
