/**
 * The main GUI window for the project, and also the entry point.
 * Last Edited: 12/4/2019
 * @author Dylan
 */
package graphics;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import insides.FileTree;
import insides.Project;
import insides.Tab;

public class GUIMain
{
	private JFrame mainFrame;
	
	private GridBagConstraints constraints;
	
	private static FileTree theFileTree;
	
	private GUITabPane tabs;

	private GUIProjectPaneManager projectManager;
	
	/**
	 * Basic constructor.
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 */
	private GUIMain()
	{
		initialize();
	}
	
	/**
	 * Starts the program and initializes the FileTree
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 * @param args
	 */
	public static void main(String[] args)
	{
		theFileTree = new FileTree(false); //Bool is for if it's a test
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					GUIMain window = new GUIMain();
					window.mainFrame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				theFileTree.close();
			}
		});
	}
	
	/**
	 * Creates the basic UI components for the main UI window.
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 */
	private void initialize()
	{
		mainFrame = new JFrame("Jon's Gahraj");
		mainFrame.setBounds(50, 50, 800, 600);
		mainFrame.setMinimumSize(new Dimension(640, 480));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new GridBagLayout());
		
		projectManager = new GUIProjectPaneManager(theFileTree);
		tabs = new GUITabPane(theFileTree, projectManager);
		
		constraints = new GridBagConstraints();
		createMenu();
		createTabPane();
		createHomePane();
		createTabButtons();
		createExport();
		
	}
	
	/**
	 * Creates the MenuBar used to access the about screen
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 */
	private void createMenu()
	{
		JMenuBar menu = new JMenuBar();
		mainFrame.setJMenuBar(menu);
		
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				new GUIAbout();
			}
		});
		menu.add(about);
	}
	
	/**
	 * Sets graphical settings for the GUITabPane
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 */
	private void createTabPane()
	{
		setConstraints(0, 0, 2, 1, 0.2, 0.95);
		mainFrame.add(tabs, constraints);
		tabs.setVisible(true);
	}
	
	/**
	 * Creates the center window used to display projects once a tab is selected.
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 */
	private void createHomePane()
	{
		setConstraints(2, 0, 5, 1, 0.8, 0.95);
		mainFrame.add(projectManager, constraints);
	}
	
	/**
	 * Builds the add and remove tab buttons below the tab list, and the actions associated with them.
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 */
	private void createTabButtons()
	{
		JButton addTab = new JButton("Add Tab");
		setConstraints(0, 1, 1, 1, 0.1, 0.05);
		mainFrame.add(addTab, constraints);
		
		final JButton removeTab = new JButton("Remove Tab");
		setConstraints(1, 1, 1, 1, 0.1, 0.05);
		mainFrame.add(removeTab, constraints);
		
		addTab.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String name = JOptionPane.showInputDialog(mainFrame, "Enter Tab name:", null);
				if (name != null && !name.equals(""))
				{
					Tab temp = theFileTree.newTab(name);
					projectManager.addPane(temp);
					removeTab.setEnabled(true);
					System.out.println("Tab added...");
				}
				tabs.refresh();
			}
		});
		
		removeTab.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Tab selected = tabs.getSelected();
				if (selected == null) JOptionPane.showMessageDialog(mainFrame, "You must select a Tab", "Error", JOptionPane.ERROR_MESSAGE);
				else
				{
					int confirm = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to remove \"" + selected + "\"?", "Remove Tab", JOptionPane.YES_NO_OPTION);
					if (confirm == 0)
					{
						theFileTree.delete(selected, theFileTree.getRoot());
						projectManager.removePane(selected);
						if (theFileTree.getTabs().size() == 0) removeTab.setEnabled(false);
						System.out.println("Tab removed...");
					}
				}
				tabs.refresh();
			}
		});
		
		if (theFileTree.getTabs().size() == 0) removeTab.setEnabled(false);
	}

	/**
	 * TODO
	 */
	private void createExport()
	{
		JPanel emptyspace = new JPanel();
		setConstraints(2, 1, 4, 1, 0.7, 0.05);
		mainFrame.add(emptyspace, constraints);
		
		JButton exportBtn = new JButton("Export");
		setConstraints(6, 1, 1, 1, 0.1, 0.05);
		exportBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Project toExport = projectManager.getSelected();
				if (toExport != null)
				{
					JFileChooser fileChooser = new JFileChooser();
					if (fileChooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION)
					{
						Path location = fileChooser.getSelectedFile().toPath();
						theFileTree.export(toExport, location);
					}
					System.out.println("Exported...");
				}
				else
				{
					JOptionPane.showMessageDialog(mainFrame, "Please select a Project to export.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mainFrame.add(exportBtn, constraints);
	}
	
	/**
	 * TODO
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param wx
	 * @param wy
	 */
	private void setConstraints(int x, int y, int w, int h, double wx, double wy)
	{
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		constraints.weightx = wx;
		constraints.weighty = wy;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
	}
}