package folderMonitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.NoSuchFileException;
import java.text.CharacterIterator;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
	        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	            if ("Nimbus".equals(info.getName())) {
	                UIManager.setLookAndFeel(info.getClassName());
	                break;
	            }
	        }
	    } catch (Exception e) {
	        try {
	            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	        } catch (Exception ex) {
	        }
	    }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JScrollPane displayScrollPane;
	private JTextArea displayArea = new JTextArea();
	private JPanel panel;
	private JPanel closeUppanel, lowPanel;
	private JLabel lblHeading;
	private JButton btnAddNewDirectory;
	
	private String directories;
	private JRadioButton[] dirRadio;
	private ButtonGroup buttonGroup = new ButtonGroup();

	private JLabel lblSelectedDirName;
	private JLabel lblSelectedDirSize;
	private JLabel lblUsableSpace;
	private JButton btnNewButton_1;
	private JFileChooser fc=new JFileChooser();  
	private JButton btnNewButton_2, clearButt;
	private String watching;
	private File watchFileSelected;
	private String[] dirs;
	private List<String> dirList = new ArrayList<String>();
	private DirectoryProperties directoryProperties;
	private JLabel lblLastModified;
	private JLabel lblNoOfFiles;
	private JPanel upPanel;
	private JButton btnHome;
	private JProgressBar progressBar;
	private JLabel lblMonitoring;
	private Timer timer;
	private Map<String, Object> storeProperties = new HashMap<String, Object>();
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem saveUpdatesMenuItem;
	private JMenuItem aboutMenuItem;
	private JMenuItem exitMenuItem;
	private JMenu directoryMenu;
	private JMenuItem emptyDirMenuItem;
	private JLabel directoryFull;
	
	private Settings settings = new Settings();
	
	
	private boolean firstlaunch = true;
	private String who = "User";
	
	public MainWindow() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/folderMonitor/LOGO1.png")));
		setTitle("DIRECTORY MONITOR");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 900);
		
		this.setVisible(true);
		this.setLocationRelativeTo(getParent());
		
		directories = settings.getDirectories();
		
		dirs = directories.split("#");
		dirList = Stream.of(dirs).collect(Collectors.toCollection(ArrayList::new));
		System.out.println(dirList);
		
		
		InetAddress hname = null;
		try {
			hname = java.net.InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		who = System.getProperty("user.name")+", "+hname;
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainWindow.this, "Developed by Ayoade Joseph for Chip-Code - 2021", "ABOUT",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnNewMenu.add(aboutMenuItem);
		
		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnNewMenu.add(exitMenuItem);
		
		directoryMenu = new JMenu("Directory");
		menuBar.add(directoryMenu);
		
		emptyDirMenuItem = new JMenuItem("Empty Directory");
		directoryMenu.add(emptyDirMenuItem);
		
		saveUpdatesMenuItem = new JMenuItem("Save Updates to Folder");
		directoryMenu.add(saveUpdatesMenuItem);
		saveUpdatesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat now = new SimpleDateFormat("dd MMM yyyy HHmmss");
				String filename = watching+"/Directory Report "+now.format(new Date());
				String text = displayArea.getText();
				
				try (PrintWriter out = new PrintWriter(filename)) {
				    out.println(text);
				    JOptionPane.showMessageDialog(MainWindow.this, "Updates have been saved to "+watching, "LOG SAVED!",JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MainWindow.this, "Could not save file as a result of: "+e1.getMessage(), "ISSUES",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		
		alertMenu = new JMenu("Alert");
		menuBar.add(alertMenu);
		
		emailMenuItem = new JMenuItem("Email Settings");
		emailMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MailDialog();
			}
		});
		alertMenu.add(emailMenuItem);
		
		directoryFull = new JLabel("  | Directory is full! Delete old files");
		menuBar.add(directoryFull);
		
		lblEmailAlert = new JLabel("  | Email Alert is not configured");
		menuBar.add(lblEmailAlert);
	    
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		displayScrollPane = new JScrollPane();
		
		displayScrollPane.setViewportView(displayArea);
		displayArea.setBorder(new CompoundBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "UPDATES", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)), new BevelBorder(BevelBorder.LOWERED, null, null, null, null)));
		displayArea.setBackground(Color.LIGHT_GRAY);
		displayArea.setWrapStyleWord(true);
		displayArea.setLineWrap(true);
		contentPane.add(displayScrollPane, BorderLayout.CENTER);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "CONTROLS", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setAutoscrolls(true);
		panel.setPreferredSize(new Dimension(250, 500));
		displayScrollPane.setRowHeaderView(panel);
		panel.setLayout(new BorderLayout());
		
		lowPanel = new JPanel();
		lowPanel.setBorder(null);
		lowPanel.setAutoscrolls(true);
		lowPanel.setPreferredSize(new Dimension(200, 250));
		panel.add(lowPanel, BorderLayout.SOUTH);
		lowPanel.setLayout(new GridLayout(8, 0, 0, 0));
		
		lblSelectedDirName = new JLabel("Directory:");
		lowPanel.add(lblSelectedDirName);
		
		lblSelectedDirSize = new JLabel("Dir. Size:");
		lowPanel.add(lblSelectedDirSize);
		
		lblUsableSpace = new JLabel("Max. Size:");
		lowPanel.add(lblUsableSpace);
		
		btnNewButton_2 = new JButton("Unwatch");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkIfSelectionMade(buttonGroup)) {
					if(JOptionPane.showConfirmDialog(MainWindow.this, "Are you sure you want to remove "+watching+ "?") == JOptionPane.YES_OPTION) {
						dirs = removeDirectory(watching);
						System.out.println("dirs:"+dirs);
						
						if(dirs != null) {
							System.out.println("new dir size:"+dirs.length);
							reloadDirectory(dirs);
							refreshDirectoryList();
						}else {
							settings.updateHomeSettings("Home");
							 refreshDirectoryList();
						}
					}
				}
			}
		});
		
		lblLastModified = new JLabel("Modified:");
		lowPanel.add(lblLastModified);
		
		lblNoOfFiles = new JLabel("No. Of Files:");
		lowPanel.add(lblNoOfFiles);
		lowPanel.add(btnNewButton_2);
		
		btnNewButton_1 = new JButton("View Folder");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(new File(watching));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		lowPanel.add(btnNewButton_1);
		
		
		
		clearButt = new JButton("Unwatch All");
		lowPanel.add(clearButt);
		clearButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(MainWindow.this, "Are you sure you want to remove all directories?") == JOptionPane.YES_OPTION) {
					settings.updateHomeSettings("Home");
					refreshDirectoryList();
				}
			}
		});
		
		closeUppanel = new JPanel();
		closeUppanel.setLayout(new GridLayout(7, 0, 0, 0));
		panel.add(closeUppanel, BorderLayout.CENTER);
		
		progressBar = new JProgressBar();
		progressBar.setForeground(Color.YELLOW);
		progressBar.setMaximum(10);
		closeUppanel.add(progressBar);

		
		upPanel = new JPanel();
		upPanel.setPreferredSize(new Dimension(200, 160));
		panel.add(upPanel, BorderLayout.NORTH);
		GridBagLayout gbl_upPanel = new GridBagLayout();
		gbl_upPanel.columnWidths = new int[]{34, 169, 0};
		gbl_upPanel.rowHeights = new int[]{16, 82, 0, 0, 0};
		gbl_upPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_upPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		upPanel.setLayout(gbl_upPanel);
		
		lblHeading = new JLabel("Add Directories to Watch");
		GridBagConstraints gbc_lblHeading = new GridBagConstraints();
		gbc_lblHeading.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblHeading.insets = new Insets(0, 0, 5, 0);
		gbc_lblHeading.gridx = 1;
		gbc_lblHeading.gridy = 0;
		upPanel.add(lblHeading, gbc_lblHeading);
		lblHeading.setVerticalAlignment(SwingConstants.BOTTOM);
		lblHeading.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblHeading.setHorizontalAlignment(SwingConstants.CENTER);
		
		btnAddNewDirectory = new JButton("");
		GridBagConstraints gbc_btnAddNewDirectory = new GridBagConstraints();
		gbc_btnAddNewDirectory.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddNewDirectory.anchor = GridBagConstraints.NORTH;
		gbc_btnAddNewDirectory.gridx = 1;
		gbc_btnAddNewDirectory.gridy = 1;
		upPanel.add(btnAddNewDirectory, gbc_btnAddNewDirectory);
		btnAddNewDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int i=fc.showOpenDialog(MainWindow.this);    
			    if(i==JFileChooser.APPROVE_OPTION){    
			        File f=fc.getSelectedFile();    
			        String filepath=f.getPath();    
			        System.out.println(filepath);
			        addDirectory(filepath);
			        
			        refreshDirectoryList();
					

			    }
			    SwingUtilities.updateComponentTreeUI(MainWindow.this);
			}
		});
		btnAddNewDirectory.setHorizontalTextPosition(SwingConstants.CENTER);
		btnAddNewDirectory.setIcon(new ImageIcon(MainWindow.class.getResource("/folderMonitor/addx.png")));
		
		btnHome = new JButton("Select Home Directory");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object selected = JOptionPane.showInputDialog(null, "Select the default directory to monitor?", "Selection", JOptionPane.DEFAULT_OPTION, null, dirs, "0");
				if ( selected != null ){//null if the user cancels. 
				    String selectedString = selected.toString();
				    System.out.println(selectedString);

				    String cc="";
					if(dirList != null) {
						dirList.set(0, selectedString);
						for(String zz : dirList)cc+=zz+"#";
						settings.updateHomeSettings(cc);
					}
					refreshDirectoryList();
				}else{
				    System.out.println("User cancelled");
				}
			}
		});
		GridBagConstraints gbc_btnHome = new GridBagConstraints();
		gbc_btnHome.insets = new Insets(0, 0, 5, 0);
		gbc_btnHome.gridx = 1;
		gbc_btnHome.gridy = 2;
		upPanel.add(btnHome, gbc_btnHome);
		
		lblMonitoring = new JLabel("Monitoring...");
		lblMonitoring.setFont(new Font("LiSong Pro", Font.PLAIN, 13));
		lblMonitoring.setForeground(Color.ORANGE);
		GridBagConstraints gbc_lblMonitoring = new GridBagConstraints();
		gbc_lblMonitoring.gridwidth = 2;
		gbc_lblMonitoring.insets = new Insets(0, 0, 0, 5);
		gbc_lblMonitoring.anchor = GridBagConstraints.WEST;
		gbc_lblMonitoring.gridx = 0;
		gbc_lblMonitoring.gridy = 3;
		upPanel.add(lblMonitoring, gbc_lblMonitoring);
		btnHome.setVisible(false);
		
		dirRadio = new JRadioButton[dirs.length];
		
		if(dirs.length>0) {
			if(dirs.length>1) {
				btnHome.setVisible(true);
			}
			for(int x=0; x<dirs.length; x++) {
				try {
				selectWatchFile(x);
				}catch(Exception r) {
					r.printStackTrace();
					JOptionPane.showMessageDialog(MainWindow.this, "You have no permission to access "+dirs[x], "ILLEGAL ACTION",JOptionPane.ERROR_MESSAGE);
					}
				
				dirRadio[x].setName(dirs[x]);
				
				dirRadio[x].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JRadioButton q = (JRadioButton) e.getSource();
						
						watching = q.getName();
						
						if(watching.equals("Home")) {
							watching = "src";
						}
						watchFileSelected = new File(watching);
						try {
							directoryProperties = new DirectoryProperties(watchFileSelected);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(MainWindow.this, "You have no permission to access "+watchFileSelected, "ILLEGAL ACTION",JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
						System.out.println("watching:"+watching);
						
						if(directoryProperties != null) {
							loadDisplay();
						}
					}
				});
				buttonGroup.add(dirRadio[x]);
				closeUppanel.add(dirRadio[x]);
			}
		}
		
		
		timer = new Timer(500, new ActionListener() {
			Color liveIndicate = Color.ORANGE;	 int xy = 0;
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(firstlaunch) sendEmail("FIRST LAUNCH FROM "+who);
				firstlaunch = false;
				
				if(liveIndicate == Color.ORANGE) {
					liveIndicate = Color.PINK;
				}else liveIndicate = Color.ORANGE;
				
				xy+=2;
				lblMonitoring.setForeground(liveIndicate);
				progressBar.setForeground(liveIndicate);
				progressBar.setValue(xy);
				if(xy == 10) {
					xy=0;
					checkDirectory(liveIndicate);
					
				}
				
			}
		});
		
		timer.start();
		
		
	}
	
	int attentionSeeker=0;
	int emaillauncher = 0;
	private JMenu alertMenu;
	private JMenuItem emailMenuItem;
	private JLabel lblEmailAlert;
	protected void checkDirectory(Color liveIndicate) {
		try {
			
			Map<String, Object> inicialStoreProperties = directoryProperties.getStoreProperties();
			
			directoryProperties = new DirectoryProperties(watchFileSelected);
			storeProperties = directoryProperties.getStoreProperties();
			long inicial = (long) inicialStoreProperties.get("FolderSize");
			long current = (long) storeProperties.get("FolderSize");
			//compare size
			if( inicial != current) {
				displayArea.append("\n\nFolder size has changed:"+inicialStoreProperties.get("FolderSize")+" Bytes to "+storeProperties.get("FolderSize")+" Bytes");
				long change = current - inicial;
				if(change>0)displayArea.append("\n"+change+" Bytes of data added to directory");
				else displayArea.append("\n"+change+" Bytes of data removed to directory");
			}
			
			long inicialModified = (long) inicialStoreProperties.get("LastModified");
			long currentModified = (long) storeProperties.get("LastModified");
			//compare LAST time it was modified
			if( inicialModified != currentModified) {
				displayArea.append("\n\nFolder Modified:"+inicialStoreProperties.get("LastModified")+" to "+storeProperties.get("LastModified"));
			}
			
			//Detect change in number of files
			int inicialNoOfFiles = (int) inicialStoreProperties.get("NoOfFiles");
			int currentNoOfFiles = (int) storeProperties.get("NoOfFiles");
			
			if( inicialNoOfFiles != currentNoOfFiles) {
				displayArea.append("\n\nChange in No of Files:"+inicialStoreProperties.get("NoOfFiles")+" files to "+storeProperties.get("NoOfFiles")+" files");
				int changes = currentNoOfFiles - inicialNoOfFiles;
				if(changes>0)displayArea.append("\n"+changes+" file added to directory");
				else displayArea.append("\n"+changes+" file removed from directory");
			}
			
			if(current >= (directoryProperties.getUsableFreeSpace() - 10000)) {
				displayArea.append("\n\nSTORAGE FULL: Please delete some files from storage to avoid server failure.");
				directoryFull.setForeground(liveIndicate);
				directoryFull.setText("|    Storage: "+humanReadableByteCountSI(directoryProperties.getFolderSize())+" of "+ humanReadableByteCountSI(directoryProperties.getUsableFreeSpace())+" FULL (Delete Files)");
				attentionSeeker++;	emaillauncher++;
				
				try {
					alert();
				} catch (LineUnavailableException | InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(emaillauncher);
				if(attentionSeeker == 10) {
					JOptionPane.showMessageDialog(MainWindow.this, "Please delete some files from storage to avoid server failure.", "STORAGE FULL",JOptionPane.WARNING_MESSAGE);
					displayArea.append("\n\nSTORAGE FULL: Please delete some files from storage to avoid server failure.");
					attentionSeeker = 0;
				}
				
				if(emaillauncher >= 200) {
					System.out.println("STORAGE FULL: Please delete some files from storage to avoid server failure.\n");
					sendEmail("STORAGE FULL: Please delete some files from storage to avoid server failure.\n\n\n "+displayArea.getText());
					emaillauncher =0;
				}
			}else {
				directoryFull.setText("|    Storage: "+humanReadableByteCountSI(directoryProperties.getFolderSize())+" of "+ humanReadableByteCountSI(directoryProperties.getUsableFreeSpace()));
				directoryFull.setForeground(Color.GREEN);
			}
			
			
		} catch (NoSuchFileException e1) {
			e1.printStackTrace();
		}
		
	}


	protected boolean checkIfSelectionMade(ButtonGroup group) {
		List<AbstractButton> listRadioButton = Collections.list(group.getElements());
		for (AbstractButton button : listRadioButton) {
		    if(button.isSelected())return true;
		}
		JOptionPane.showMessageDialog(MainWindow.this, "Select the directory you want to remove.", "NO SELECTION",JOptionPane.INFORMATION_MESSAGE);
		return false;
	}


	void selectWatchFile(int x) {
		System.out.println("accepting:"+x);
		File watchFile = new File(dirs[x]);
		if(x==0) {
			System.out.println("x is:"+dirs[x]);
			dirRadio[x] = new JRadioButton("Watching>>"+watchFile.getName());
			dirRadio[x].setSelected(true);
			watching = dirs[x];
			
			if(watching.equals("Home")) {
				watching = "src";
			}
			watchFileSelected = new File(watching);
			System.out.println("W:"+watchFileSelected);
			try {
				directoryProperties = new DirectoryProperties(watchFileSelected);
				System.out.println("WXY:"+directoryProperties.getNoOfFiles());
			} catch (NoSuchFileException e1) {
				e1.printStackTrace();
			}
			System.out.println("watching:"+watching);
			
			if(directoryProperties != null) {
				loadDisplay();
			}
		}else dirRadio[x] = new JRadioButton(watchFile.getName());
	}
	
	protected void loadDisplay() {
		lblSelectedDirName.setText( "Dir. Name: "+directoryProperties.getFolderName());
		lblSelectedDirSize.setText("Dir. Size: "+humanReadableByteCountSI(directoryProperties.getFolderSize()));
		lblUsableSpace.setText("Max. Space: "+humanReadableByteCountSI(directoryProperties.getUsableFreeSpace()));
		lblLastModified.setText("Modified: "+ convertTime(directoryProperties.getLastModified()));
		lblNoOfFiles.setText("No of Files: "+ directoryProperties.getNoOfFiles());
		List<String> filesAvai = directoryProperties.getDirFiles();
		String filesInDir = "Directory Name: "+directoryProperties.getFolderName()+"\n"+"Directory Size: "+humanReadableByteCountSI(directoryProperties.getFolderSize())+"\n"
		+"Usable Space: "+humanReadableByteCountSI(directoryProperties.getUsableFreeSpace())+"\n"+"Modified Last On: "+ convertTime(directoryProperties.getLastModified())+"\n"
				+"No of Files in Directory: "+ directoryProperties.getNoOfFiles()+"\n\n"+"FILES IN THE DIRECTORY\n";
		int r=1;
		for(String fil : filesAvai) {
			filesInDir+= (r++)+". "+fil+"\n";
		}
		
		displayArea.setText(filesInDir);
		
		if(directoryProperties.getFolderSize() >= (directoryProperties.getUsableFreeSpace() - 10000)) {
			JOptionPane.showMessageDialog(MainWindow.this, "Please delete some files from storage to avoid server failure.", "STORAGE FULL",JOptionPane.WARNING_MESSAGE);
			lblSelectedDirSize.setText("Dir. Size: "+humanReadableByteCountSI(directoryProperties.getFolderSize()) +" FULL (Delete files)");
			lblSelectedDirSize.setForeground(Color.red);
			
			System.out.println("STORAGE FULL: Please delete some files from storage to avoid server failure.\n");
			sendEmail("STORAGE FULL: Please delete some files from storage to avoid server failure.\n\n\n "+displayArea.getText());
		}else {
			lblSelectedDirSize.setForeground(Color.GREEN);
		}
		
	}

	boolean addDirectory(String dirToAdd) {
		try {
			String cc="";
			if(dirList != null) {
				dirList.add(dirToAdd);
				for(String zz : dirList)cc+=zz+"#";
				settings.updateHomeSettings(cc);
			}
			return true;
		}catch(Exception z) {
			return false;
		}
	}
	
	String[] removeDirectory(String dirToRemove) {
		try {
			for(int u=0; u<dirList.size(); u++) {
				if(dirToRemove.equals(dirList.get(u))) {
					dirList.remove(u);
					break;
				}
			}
			
			if(dirList.size()>0) {
				dirs = new String[dirList.size()];
				for(int a=0; a<dirList.size(); a++)dirs[a] = dirList.get(a);
			}
			return dirs;
		}catch(Exception a) {
			return null;
		}
	}
	
	void reloadDirectory(String[] dirs) {
		String newDirs="";
		if(dirs.length>0)
		for (String d : dirs) {
			newDirs+=d+"#";
		}
		if(newDirs.length()>0)settings.updateHomeSettings(newDirs);
		else settings.updateHomeSettings("Home");
	}
	
	public String convertTime(long time){
	    Date date = new Date(time);
	    Format format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
	    return format.format(date);
	}
	
	public String humanReadableByteCountSI(long bytes) {
	    if (-1000 < bytes && bytes < 1000) {
	        return bytes + " B";
	    }
	    CharacterIterator ci = new StringCharacterIterator("kMGTPE");
	    while (bytes <= -999_950 || bytes >= 999_950) {
	        bytes /= 1000;
	        ci.next();
	    }
	    return String.format("%.1f %cB", bytes / 1000.0, ci.current());
	}
	
	
	void refreshDirectoryList() {
		settings = new Settings();
		directories = settings.getDirectories(); 
		
		dirs = directories.split("#");
		System.out.println(dirs.length);
		
		buttonGroup = clearButtonGroup(buttonGroup);
		System.out.println("Refreshing");
		
		closeUppanel.removeAll();
		closeUppanel.setVisible(false);
		closeUppanel.revalidate();
		
		dirRadio = new JRadioButton[dirs.length];
		
		if(dirs.length>0) {
			if(dirs.length>1) {
				btnHome.setVisible(true);
			}
			for(int x=0; x<dirs.length; x++) {
				
				File watchFile = new File(dirs[x]);

				
				dirRadio[x] = new JRadioButton(watchFile.getName());
				dirRadio[x].setName(dirs[x]);
				
				if(x==0) {
					System.out.println("x is:"+dirs[x]);
					dirRadio[x] = new JRadioButton("Watching>>"+watchFile.getName());
					dirRadio[x].setSelected(true);
					watching = dirs[x];
					
					if(watching.equals("Home")) {
						watching = "src";
					}
					watchFileSelected = new File(watching);
					System.out.println("W:"+watchFileSelected);
					try {
						directoryProperties = new DirectoryProperties(watchFileSelected);
						System.out.println("WXY:"+directoryProperties.getNoOfFiles());
					} catch (NoSuchFileException e1) {
						e1.printStackTrace();
					}
					
					if(directoryProperties != null) {
						loadDisplay();
					}
				}
				
				dirRadio[x].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JRadioButton q = (JRadioButton) e.getSource();
						
						watching = q.getName();
						System.out.println("watching:"+watching);
						if(watching.equals("Home")) {
							watching = "src";
						}
						watchFileSelected = new File(watching);
						try {
							directoryProperties = new DirectoryProperties(watchFileSelected);
						} catch (NoSuchFileException e1) {
							e1.printStackTrace();
						}
						System.out.println("watching:"+watching);
						
						if(directoryProperties != null) {
							lblSelectedDirName.setText( "Dir. Name: "+directoryProperties.getFolderName());
							lblSelectedDirSize.setText("Dir. Size: "+humanReadableByteCountSI(directoryProperties.getFolderSize()));
							lblUsableSpace.setText("Max. Space: "+humanReadableByteCountSI(directoryProperties.getUsableFreeSpace()));
							lblLastModified.setText("Modified: "+ convertTime(directoryProperties.getLastModified()));
							lblNoOfFiles.setText("No of Files: "+ directoryProperties.getNoOfFiles());
							List<String> filesAvai = directoryProperties.getDirFiles();
							String filesInDir = "Directory Name: "+directoryProperties.getFolderName()+"\n"+"Directory Size: "+humanReadableByteCountSI(directoryProperties.getFolderSize())+"\n"
							+"Usable Space: "+humanReadableByteCountSI(directoryProperties.getUsableFreeSpace())+"\n"+"Modified Last On: "+ convertTime(directoryProperties.getLastModified())+"\n"
									+"No of Files in Directory: "+ directoryProperties.getNoOfFiles()+"\n\n"+"FILES IN THE DIRECTORY\n";
							int r=1;
							for(String fil : filesAvai) {
								filesInDir+= (r++)+". "+fil+"\n";
							}
							
							displayArea.setText(filesInDir);
						}
					}
				});
				

				
				buttonGroup.add(dirRadio[x]);
				closeUppanel.add(dirRadio[x]);
				
				closeUppanel.setVisible(true);
				closeUppanel.revalidate();
				
			}
		}
	}
	
	ButtonGroup clearButtonGroup(ButtonGroup bg) {
		 Enumeration<AbstractButton> e =bg.getElements(); 
		    while(e.hasMoreElements()) {
		        AbstractButton b = e.nextElement();
		        bg.remove(b);
		    }
		    return bg;
		}
	
	void alert() throws LineUnavailableException, InterruptedException {
		AlertSpeaker.tone(1000,100);
	    Thread.sleep(1000);
	    AlertSpeaker.tone(100,1000);
	    Thread.sleep(1000);
	    AlertSpeaker.tone(5000,100);
	    Thread.sleep(1000);
	    AlertSpeaker.tone(400,500);
	    Thread.sleep(1000);
	    AlertSpeaker.tone(400,500, 0.2);
	}
	
	void sendEmail(String text) {
		boolean checkPw = false;
		boolean checktoEml = true;
		if(!settings.getFromPassword().equals("xyz")) {
			lblEmailAlert.setText("  |  Alert Email is:"+settings.getToEmail());
			checkPw = true;
		}
		
		if(settings.getToEmail().equals("admin@iq-joy.com")) {
			lblEmailAlert.setText("  |  Default Email is not configured." );
			checktoEml = false;
		}
		System.out.println("Bool:"+checkPw+", "+checktoEml);
		System.out.println("data:"+settings.getToEmail()+", "+ settings.getDomain()+", "+settings.getFromEmail()+","+settings.getFromPassword());
		
		if(checkPw && checktoEml)new SendEmail(settings.getToEmail(), settings.getDomain(), settings.getFromEmail(), settings.getFromPassword(), text, who);
		else JOptionPane.showMessageDialog(MainWindow.this, "Remember to configure your Alert email.", "IMPORTANT",JOptionPane.INFORMATION_MESSAGE);
	}

}
