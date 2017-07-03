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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.mhisoft.common.event.EventDispatcher;
import org.mhisoft.common.event.EventType;
import org.mhisoft.common.event.MHIEvent;
import org.mhisoft.common.util.FileUtils;
import org.mhisoft.common.util.StringUtils;
import org.mhisoft.wallet.WalletMain;
import org.mhisoft.wallet.action.ActionResult;
import org.mhisoft.wallet.action.BackupAction;
import org.mhisoft.wallet.action.ChangePasswordAction;
import org.mhisoft.wallet.action.CloseWalletAction;
import org.mhisoft.wallet.action.ImportWalletAction;
import org.mhisoft.wallet.action.NewWalletAction;
import org.mhisoft.wallet.action.OpenWalletFileAction;
import org.mhisoft.wallet.action.SaveWalletAction;
import org.mhisoft.wallet.model.WalletItem;
import org.mhisoft.wallet.model.WalletModel;
import org.mhisoft.wallet.model.WalletSettings;
import org.mhisoft.wallet.service.BeanType;
import org.mhisoft.wallet.service.ServiceRegistry;

/**
 * Description: The Wallet Form UI
 *
 * @author Tony Xue
 * @since Mar, 2016
 */
public class WalletForm {

	JFrame frame;

	JTree tree;
	JPanel mainPanel;


	JTextField fldName;
	JTextField fldURL;
	RSyntaxTextArea fldNotes;
	JPasswordField fldPassword;
	JTextField fldUserName;
	JTextField fldAccountNumber;


	JSpinner fldFontSize;


	JTabbedPane tabbedPane1;
	JButton btnTogglePasswordView;
	JSplitPane splitPanel;
	JButton btnAddNode;
	JButton btnDeleteNode;
	JButton btnMoveNode;

	JPanel treeButtonPanel;
	public JButton btnEditForm;
	public JButton btnSaveForm;
	public JButton btnCancelEdit;
	public JButton btnClose;

	JLabel labelName;
	JLabel labelURL;
	JLabel labelUsername;
	JLabel labelNotes;
	JLabel labelPassword;
	JLabel labelAccount;
	JLabel labelFontSize;

	JTextField fldExpMonth;
	JTextField fldExpYear;
	JTextField fldFilter;
	JList itemList;
	JTextField fldAccountType;
	JTextField fldPhone;
	JTextField fldDetail1;
	JTextField fldDetail2;
	JTextField fldDetail3;
	JLabel labelPin;
	JLabel labelExpMonth;
	JLabel labelExpYear;
	JLabel labelAccountType;
	JLabel labelPhone;
	JLabel labelDetail1;
	JLabel labelDetail2;
	JLabel labelDetail3;
	JLabel labelLastMessage2;
	JButton btnFilter;
	JButton btnClearFilter;
	private JScrollPane itemListPanel;
	private JScrollPane treePanel;
	private JPanel filterPanel;
	private JPanel treeListPanel;

	private JPanel rightMainPanel;
	private JScrollPane detailFormScrollPane;
	private JPanel buttonPanel;
	JLabel labelCVC;
	JPasswordField fldCVC;
	JPasswordField fldPin;
	public JLabel labelCurrentOpenFile;
	private JTextField fldIdleTimeout;
	private JLabel labelIdleTimeOut;
	private JTextArea textAreaDebug;
	private JLabel labelLastMessage;
	JButton btnCollapse;
	JLabel imageLabel;
	JButton btnLaunchURL;
	private JButton btnLoadImage;
	private JPanel imagePanel;

	private JScrollPane rightScrollPane;


	JMenuBar menuBar;
	public JMenu menuFile;
	public JMenuItem menuOpen, menuNew, menuClose, menuImport, menuBackup, menuChangePassword, menuOpenRecent;
	//JRadioButtonMenuItem rbMenuItem;
	//JCheckBoxMenuItem cbMenuItem;

	List<Component> componentsList;
	WalletModel model;
	TreeExploreView treeExploreView;
	ListExplorerView listExploreView;
	ItemDetailView itemDetailView;


	boolean hidePassword = true;
	boolean treeExpanded = true;

	public WalletModel getModel() {
		return model;
	}

	public void setModel(WalletModel model) {
		this.model = model;
	}

	public TreeExploreView getTreeExploreView() {
		return treeExploreView;
	}

	public WalletForm() {

		model = new WalletModel();
		treeExploreView = new TreeExploreView(frame, model, tree, this);
		listExploreView = new ListExplorerView(frame, model, itemList, this);
		itemDetailView = new ItemDetailView(model, this);

		ServiceRegistry.instance.registerSingletonService(this);

		// Put client property
		fldPassword.putClientProperty("JPasswordField.cutCopyAllowed", true);


//		fldName.getDocument().addDocumentListener(new MyDocumentListener(fldName, "name", model));
//		fldName.getDocument().addDocumentListener(new MyDocumentListener(fldName, "URL", model));

		btnEditForm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnEditForm", null));
				itemDetailView.editDetailAction();
			}
		});
		btnCancelEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnCancelEdit", null));
				itemDetailView.cancelEditAction();
			}
		});


		ActionListener saveFormListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnSaveForm", null));

				//save button click , it is two fold. save the item detail edit
				//Or save the whole model when model is modified from import for instance.
				saveButtonClicked();

			}
		};


		btnSaveForm.addActionListener(saveFormListener);

		ActionListener uploadImageListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnLoadImage", null));
				String imageFile = ViewHelper.chooseFile(null, "png", "gif", "jpg", "jpeg");

				//todo validate size.
				//File f = new File(imageFile) ;
				//if (f.get)

				model.getCurrentItem().addOrReplaceAttachment(imageFile);
				LoadImageWorker loadImageWorker = new LoadImageWorker();
				loadImageWorker.execute();

				ServiceRegistry.instance.getWalletModel().setModified(true);


			}
		};

		btnLoadImage.addActionListener(uploadImageListener);


		btnTogglePasswordView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnTogglePasswordView", null));
				hidePassword = !hidePassword;
				updatePasswordChar();
			}
		});


		btnLaunchURL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnLaunchURL", null));

				if (StringUtils.hasValue(fldURL.getText())) {
					FileUtils.launchURL(fldURL.getText().trim());
				}
			}
		});

		//constructor
		btnAddNode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnAddNode", null));
				treeExploreView.addItem();
			}
		});

		btnDeleteNode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnDeleteNode", null));
				treeExploreView.removeItem();
			}
		});

		btnMoveNode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnMoveNode", null));
				treeExploreView.moveItem();

			}
		});

		btnFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnFilter", null));
				doFilter();

			}
		});
		btnClearFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnClearFilter", null));

				clearFilter();

			}
		});
		btnFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnFilter", null));
					doFilter();
				}
			}
		});
		btnClearFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnClearFilter", null));
					clearFilter();
				}
			}
		});

		btnCollapse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnCollapse", null));
				expandCollapseTree();
			}
		});

		fldIdleTimeout.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				//
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (StringUtils.hasValue(fldIdleTimeout.getText())) {
					try {
						long l = Long.parseLong(fldIdleTimeout.getText());
						if (l < 5) //min 5
							l = 5;
						WalletSettings.getInstance().setIdleTimeout(l);

					} catch (NumberFormatException e1) {
						//

					}
				}

				fldIdleTimeout.setText(Long.valueOf(WalletSettings.getInstance().getIdleTimeout()).toString());
			}
		});


		KeyListener keyListener = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {


				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					if (e.getSource() == btnEditForm) {
						EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnEditForm", null));
						itemDetailView.editDetailAction();
					} else if (e.getSource() == btnCancelEdit) {
						EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnCancelEdit", null));
						itemDetailView.cancelEditAction();
					} else if (e.getSource() == btnSaveForm) {
						saveFormListener.actionPerformed(null);
					} else if (e.getSource() == btnTogglePasswordView) {
						EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnTogglePasswordView", null));
						hidePassword = !hidePassword;
						updatePasswordChar();
					} else if (e.getSource() == btnAddNode) {
						EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnAddNode", null));
						treeExploreView.addItem();
					} else if (e.getSource() == btnDeleteNode) {
						EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnDeleteNode", null));
						treeExploreView.removeItem();
					} else if (e.getSource() == btnMoveNode) {
						EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnMoveNode", null));
						treeExploreView.moveItem();
					} else if (e.getSource() == btnCollapse) {
						EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnCollapse", null));
						expandCollapseTree();
					} else if (e.getSource() == btnLaunchURL) {
						EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "btnLaunchURL", null));
						if (StringUtils.hasValue(fldURL.getText())) {
							FileUtils.launchURL(fldURL.getText().trim());
						}
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		};


		btnEditForm.addKeyListener(keyListener);
		btnCancelEdit.addKeyListener(keyListener);
		btnSaveForm.addKeyListener(keyListener);
		btnTogglePasswordView.addKeyListener(keyListener);
		btnAddNode.addKeyListener(keyListener);
		btnDeleteNode.addKeyListener(keyListener);
		btnMoveNode.addKeyListener(keyListener);
		btnCollapse.addKeyListener(keyListener);
		btnLaunchURL.addKeyListener(keyListener);

		fldFilter.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "fldFilter", null));
					doFilter();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});

		treeExpanded = WalletSettings.getInstance().isTreeExpanded();


	} //end of form construstor

	public enum TreePanelMode {
		tree, filter
	}

	private void expandCollapseTree() {
		treeExpanded = !treeExpanded;
		WalletSettings.getInstance().setTreeExpanded(treeExpanded);
		if (treeExpanded)
			treeExploreView.expandTree();
		else
			treeExploreView.collapseTree();
	}


	private void switchMode(TreePanelMode mode) {
		switch (mode) {
			case tree:
				treeExploreView.setSelectionToCurrentNode();

				fldFilter.setText("");
				itemListPanel.setVisible(false);
				treePanel.setVisible(true);


				treeListPanel.validate();

				//buttons
				btnDeleteNode.setEnabled(true);
				btnMoveNode.setEnabled(true);
				btnAddNode.setEnabled(true);
				btnCollapse.setEnabled(true);

				break;

			case filter:
				itemListPanel.setVisible(true);
				treePanel.setVisible(false);
				btnDeleteNode.setEnabled(false);
				btnMoveNode.setEnabled(false);
				btnAddNode.setEnabled(false);
				btnCollapse.setEnabled(false);

				treeListPanel.validate();

		}
	}


	public void doFilter() {
		if (fldFilter.getText() != null && fldFilter.getText().trim().length() > 0) {
			switchMode(TreePanelMode.filter);

			listExploreView.filterItems(fldFilter.getText());
		}
	}

	public void clearFilter() {
		switchMode(TreePanelMode.tree);
	}

	public void resetHidePassword() {
		hidePassword = true;
		updatePasswordChar();
	}

	public void updatePasswordChar() {
		if (hidePassword) {
			fldPassword.setEchoChar('*');
			fldCVC.setEchoChar('*');
			fldPin.setEchoChar('*');
		} else {
			fldPassword.setEchoChar((char) 0);
			fldCVC.setEchoChar((char) 0);
			fldPin.setEchoChar((char) 0);
		}
	}


	public boolean hasUnsavedData() {
		return itemDetailView.currentMode != DisplayMode.view;
	}

	public JFrame getFrame() {
		return frame;
	}

	public JSplitPane getSplitPanel() {
		return splitPanel;
	}

	public void init() {
		frame = new JFrame("MHISoft eVault " + WalletMain.version);
		frame.setContentPane(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		frame.setPreferredSize(new Dimension(WalletSettings.getInstance().getDimensionX(), WalletSettings.getInstance().getDimensionY()));


//removes the title bar with X button.
//		frame.setUndecorated(true);
//		frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

		frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);

		frame.pack();

		DialogUtils.create(frame);

		componentsList = ViewHelper.getAllComponents(frame);
		componentsList.add(itemList);

		setupMenu();


		/*position it*/
		frame.setLocationRelativeTo(null);  // *** this will center your app ***
		//based on mouse location.
		//		PointerInfo a = MouseInfo.getPointerInfo();
		//		Point b = a.getLocation();
		//		int x = (int) b.getX();
		//		int y = (int) b.getY();
		//		frame.setLocation(x + 100, y);

		loadInPreferences();


		setupFontSpinner();

		itemListPanel.setVisible(false);
		frame.setVisible(true);

		jreDebug();
		tree.setModel(null);


		resetForm();


		//remove the X buttons
		//frame.setUndecorated(true);


//		Runtime.getRuntime().addShutdownHook(new Thread()
//		{
//			@Override
//			public void run() {
//				CloseWalletAction closeWalletAction = ServiceRegistry.instance.getService(BeanType.prototype, CloseWalletAction.class);
//				closeWalletAction.execute();
//			}
//		});

		// start the image loading SwingWorker in a background thread
		//loadimages.execute();

	}


	private class ThumbnailAction extends AbstractAction {
		/**
		 * Shows the full image in the main area and sets the application title.
		 */
		public void actionPerformed(ActionEvent e) {
//			photographLabel.setIcon(displayPhoto);
//			setTitle("Icon Demo: " + getValue(SHORT_DESCRIPTION).toString());
		}

	}


	public void jreDebug() {
		textAreaDebug.setText("");
		//if (WalletModel.debug) {
		textAreaDebug.append("\n");
		textAreaDebug.append(WalletMain.BUILD_DETAIL + "\n");
		textAreaDebug.append("\n");
		textAreaDebug.append("java.home=" + System.getProperty("java.home") + "\n");
		textAreaDebug.append("java.specification.version=" + System.getProperty("java.specification.version") + "\n");
		textAreaDebug.append("java.vendor=" + System.getProperty("java.vendor") + "\n");
		textAreaDebug.append("java.vendor.url=" + System.getProperty("java.vendor.url") + "\n");
		textAreaDebug.append("java.version=" + System.getProperty("java.version") + "\n");
		textAreaDebug.append("user.home=" + System.getProperty("user.home") + "\n");
	}


	/**
	 * OpenRecentFilesActionListener
	 */
	class OpenRecentFilesActionListener implements ActionListener {
		private String fileName;

		public OpenRecentFilesActionListener(String fn) {
			this.fileName = fn;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "menuOpenRecent", null));

			CloseWalletAction closeWalletAction = ServiceRegistry.instance.getService(BeanType.prototype
					, CloseWalletAction.class);
			ActionResult r = closeWalletAction.execute();

			if (r.isSuccess()) {
				OpenWalletFileAction openWalletFileAction = ServiceRegistry.instance.getService(BeanType.singleton
						, OpenWalletFileAction.class);
				r = openWalletFileAction.execute(fileName);

				//move this file to the first on the list
				WalletSettings.getInstance().moveFront(fileName);

			}
		}
	}

	;

	protected void setupMenu() {

		ActionListener closeAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CloseWalletAction closeWalletAction = ServiceRegistry.instance.getService(BeanType.prototype, CloseWalletAction.class);
				ActionResult r = closeWalletAction.execute();
				if (r.isSuccess())
					exit();

			}
		};

		//menu
		//Create the menu bar.
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menuFile);
		menuOpen = new JMenuItem("Open", KeyEvent.VK_O);
		menuFile.add(menuOpen);

		menuOpenRecent = new JMenu("Open recent files");
		menuFile.add(menuOpenRecent);
		populateRecentFilesMenu();



		menuNew = new JMenuItem("New Vault", KeyEvent.VK_N);
		menuFile.add(menuNew);

		menuImport = new JMenuItem("Import and Merge", KeyEvent.VK_I);
		menuFile.add(menuImport);

		menuBackup = new JMenuItem("Backup", KeyEvent.VK_B);
		menuFile.add(menuBackup);

		menuChangePassword = new JMenuItem("Change Password", KeyEvent.VK_P);
		menuFile.add(menuChangePassword);

		menuClose = new JMenuItem("Quit", KeyEvent.VK_Q);
		menuFile.add(menuClose);


		componentsList.add(menuBar);
		componentsList.add(menuFile);
		componentsList.add(menuOpen);
		componentsList.add(menuNew);
		componentsList.add(menuClose);
		componentsList.add(menuChangePassword);
		componentsList.add(menuImport);
		componentsList.add(menuBackup);
		componentsList.add(menuOpenRecent);


		menuClose.addActionListener(closeAction);
		btnClose.addActionListener(closeAction);
		menuOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "menuOpen", null));

				CloseWalletAction closeWalletAction = ServiceRegistry.instance.getService(BeanType.prototype, CloseWalletAction.class);
				ActionResult r = closeWalletAction.execute();
				if (r.isSuccess()) {
					OpenWalletFileAction openWalletFileAction = ServiceRegistry.instance.getService(BeanType.singleton, OpenWalletFileAction.class);
					r = openWalletFileAction.execute();
				}
			}
		});
		menuChangePassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "menuChangePassword", null));


				ChangePasswordAction action = ServiceRegistry.instance.getService(BeanType.prototype, ChangePasswordAction.class);
				action.execute();


			}
		});
		menuImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "menuImport", null));

				ImportWalletAction importWalletAction = ServiceRegistry.instance.getService(BeanType.singleton, ImportWalletAction.class);
				importWalletAction.execute();
			}
		});

		menuBackup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "menuBackup", null));

				BackupAction backupAction = ServiceRegistry.instance.getService(BeanType.singleton, BackupAction.class);
				backupAction.execute();
				//
			}
		});

		menuNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "menuNew", null));

				NewWalletAction action = ServiceRegistry.instance.getService(BeanType.singleton, NewWalletAction.class);
				action.execute();

			}
		});


	}

	public void refreshRecentFilesMenu() {
		JMenu m = ((JMenu) menuOpenRecent) ;
		for (Component component : m.getMenuComponents()) {
			m.remove(component);
			componentsList.remove(component) ;
		}
		populateRecentFilesMenu();

	}

	private void populateRecentFilesMenu() {

		for (String rf : WalletSettings.getInstance().getRecentFiles()) {
			JMenuItem m = new JMenuItem(rf);
			m.addActionListener(new OpenRecentFilesActionListener(rf));
			menuOpenRecent.add(m);
			componentsList.add(m);
		}
	}

	void loadInPreferences() {
		//divider location
		splitPanel.setDividerLocation(WalletSettings.getInstance().getDividerLocation());
		ViewHelper.setFontSize(this.componentsList, WalletSettings.getInstance().getFontSize());
	}

	public void exit() {
		frame.dispose();
	}

	public void addRecentFile(final String rf) {
		JMenuItem m = new JMenuItem(rf);
		m.addActionListener(new OpenRecentFilesActionListener(rf));
		menuOpenRecent.add(m);

		//adjust the new item's font size.
		List<Component> dummyList = new ArrayList<>();
		dummyList.add(m);
		ViewHelper.setFontSize(dummyList, WalletSettings.getInstance().getFontSize());
	}


	/**
	 * Use the font spinner to increase and decrease the font size.
	 */
	public void setupFontSpinner() {

		int fontSize = WalletSettings.getInstance().getFontSize();

		SpinnerModel spinnerModel = new SpinnerNumberModel(fontSize, //initial value
				10, //min
				fontSize + 20, //max
				2); //step
		fldFontSize.setModel(spinnerModel);
		fldFontSize.addChangeListener(new ChangeListener() {
										  @Override
										  public void stateChanged(ChangeEvent e) {
											  EventDispatcher.instance.dispatchEvent(new MHIEvent(EventType.UserCheckInEvent, "fldFontSize", null));

											  SpinnerModel spinnerModel = fldFontSize.getModel();
											  int newFontSize = (Integer) spinnerModel.getValue();
											  ViewHelper.setFontSize(componentsList, newFontSize);

										  }
									  }
		);


	}


	void createUIComponents() {
		fldNotes = new RSyntaxTextArea();
		fldNotes.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
		fldNotes.setCodeFoldingEnabled(true);
	}


	public DisplayMode getDisplayMode() {
		return itemDetailView.getDisplayMode();
	}

	//fires the ViewModeChangeEvent
	public void setDisplayMode(final DisplayMode mode) {
		itemDetailView.setDisplayMode(mode);
	}


	public void displayWalletItemDetails(final WalletItem item, final DisplayMode mode) {
		itemDetailView.displayWalletItemDetails(item, mode);
	}


	public void loadTree() {
		treeExploreView.setupTreeView();
		listExploreView.setupListView();

		btnSaveForm.setVisible(model.isModified() || isDetailModified());

		menuBackup.setEnabled(true);
		menuChangePassword.setEnabled(true);
		menuClose.setEnabled(true);
		menuImport.setEnabled(true);
		menuOpen.setEnabled(true);

	}

	/**
	 * Reset the view to empty
	 */
	public void resetForm() {
		//reset model with empty data.
		model.setCurrentItem(null);
		model.setCurrentItem(null);
		model.setModified(false);
		clearFilter();
		//todo disable all the buttons.

		treeExploreView.closeTree();
		itemDetailView.closeView();
		listExploreView.closeView();

		menuBackup.setEnabled(false);
		menuChangePassword.setEnabled(false);
		menuClose.setEnabled(true);
		menuImport.setEnabled(false);
		menuOpen.setEnabled(true);

		labelLastMessage.setVisible(false);


	}


	public boolean isDetailModified() {
		return
				((getDisplayMode() == DisplayMode.add || getDisplayMode() == DisplayMode.edit)
						//compare the current item in the model with the data on the item detail form.
						&& (itemDetailView.isModified()));

	}


	//called when node changes
	public boolean saveCurrentEdit(boolean askToSave) {
		return save(askToSave, false);
	}


	//called from the save button click
	private boolean saveButtonClicked() {
		return save(false, true);
	}


	private boolean save(boolean askToSave, boolean checkModelModified) {
		boolean ret = false;
		boolean modelModified = checkModelModified && model.isModified();

		if (isDetailModified() || modelModified) {
			// when model is modified, we want the save button to show up but  don't prompt user ask for save every time
			//change nodes.
			if (askToSave) {
				if (DialogUtils.getConfirmation(ServiceRegistry.instance.getWalletForm().getFrame()
						, "Save the changes?") == Confirmation.YES) {
					_save();

				} else {
					//not choose to save now, mark the model modified
					model.setModified(true);
				}

			} else {
				_save();
			}
		} else {
			if (!model.isModified())
				setDisplayMode(DisplayMode.view);

		}

		return ret;

	}


	private void _save() {
		itemDetailView.updateToModel();    //model current item is updated.
		//save file
		SaveWalletAction saveWalletAction = ServiceRegistry.instance.getService(BeanType.singleton, SaveWalletAction.class);
		saveWalletAction.execute();
		//model.setModified(false);   save action does it.
		getTreeExploreView().updateNameChange(model.getCurrentItem());
		//
		model.setModified(false);
	}


	public void setMessage(String s) {
		if (labelLastMessage != null) {
			labelLastMessage.setText(s);
			labelLastMessage.setVisible(true);
			clearMessageLater();
		}
	}


	Timer t = null;

	private void clearMessageLater() {

		try {
			if (t != null)
				t.cancel();
		} catch (Exception e) {
			//e.printStackTrace();
		}

		t = new Timer(true);

		t.schedule(new TimerTask() {
			@Override
			public void run() {
				labelLastMessage.setVisible(false);
				labelLastMessage.setText("");
			}
		}, new Timestamp(System.currentTimeMillis() + 10 * 1000));
	}


	public void loadOptionsIntoView() {
		labelCurrentOpenFile.setText(WalletSettings.getInstance().getLastFile());
		fldIdleTimeout.setText(Long.valueOf(WalletSettings.getInstance().getIdleTimeout()).toString());
	}


	/**
	 * Resizes an image using a Graphics2D object backed by a BufferedImage.
	 *
	 * @param srcImg - source image to scale
	 * @param w      - desired width
	 * @param h      - desired height
	 * @return - the new resized image
	 */
	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	/**
	 * SwingWorker class that loads the images a background thread and calls publish
	 * when a new one is ready to be displayed.
	 * <p>
	 * We use Void as the first SwingWroker param as we do not need to return
	 * anything from doInBackground().
	 */
	//load image from model.getCurrentItem().getAttachmentEntry()


	class LoadImageWorker extends SwingWorker<Void, ThumbnailAction> {


		@Override
		protected Void doInBackground() throws Exception {
			if (model.getCurrentItem().getOrCreateAttachmentEntry().getFileName() != null) {

				ImageIcon icon = new ImageIcon(model.getCurrentItem().getOrCreateAttachmentEntry().getFileName());

				//
				//int scaledHeight = icon.getIconHeight() * fldNotes.getWidth() / icon.getIconWidth();
				//ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(), fldNotes.getWidth(), scaledHeight));


				imageLabel.setIcon(icon);
				imageLabel.setVisible(true);


			} else {
				imageLabel.setIcon(null);
			}
			return null;
		}

	}


	public void loadImage() {
		LoadImageWorker loadImageWorker = new LoadImageWorker();
		loadImageWorker.execute();
	}







}

