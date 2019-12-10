/**
 * TODO
 */
package graphics;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import insides.FileTree;
import insides.Item;
import insides.Project;

public class GUIProjectView extends JFrame
{
	private static final long serialVersionUID = 426473126064216924L;
	
	private FileTree theFileTree;
	
	private Project theProject;
	
	private GridBagConstraints constraints;
	
	private JScrollPane scrollPane;
	
	private JList itemList;
	
	/**
	 * TODO
	 * @param fileTree
	 * @param project
	 */
	public GUIProjectView(FileTree fileTree, Project project)
	{
		theFileTree = fileTree;
		theProject = project;
		constraints = new GridBagConstraints();
		//itemInfo = Paths.get(theProject.getPath() + "\\" + theProject.getName() + "-itemdata.txt").toFile();
		//no - Sam
		setTitle(theProject.getName());
		setBounds(100, 100, 800, 600);
		setMinimumSize(new Dimension(640, 480));
		getContentPane().setLayout(new GridBagLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		
		JLabel title = new JLabel(theProject.getName());
		title.setFont(new Font("Tahoma", Font.BOLD, 28));
		setConstraints(0,0,1,1,0.8,0.05);
		add(title, constraints);
		
		createAddRemove();
		createDesc();
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		scrollPane = new JScrollPane(panel);
		
		itemList = loadItems();
		itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(itemList);
		setConstraints(0,2,3,1,1,0.85);
		add(scrollPane, constraints);
	}
	
	/**
	 * TODO
	 */
	private void createAddRemove()
	{
		final JButton removeItem = new JButton("Remove Item");
		setConstraints(1,0,1,1,0.1,0.05);
		removeItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Item selected = (Item) itemList.getSelectedValue();
				int confirm = JOptionPane.showConfirmDialog(GUIProjectView.this, "Are you sure you want to remove \"" + selected + "\"?", "Remove Item", JOptionPane.YES_NO_OPTION);
				if (confirm == 0) theFileTree.delete(selected, theProject);
				if (theProject.getContents().size() == 0) removeItem.setEnabled(false);
				refresh();
				System.out.println("Item removed...");
			}
		});
		add(removeItem, constraints);
		
		JButton addItem = new JButton("Add Item");
		setConstraints(2,0,1,1,0.1,0.05);
		addItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new GUIAddItem(theFileTree, theProject, GUIProjectView.this);
			}
		});
		add(addItem, constraints);
		
		if (theProject.getContents().size() == 0) removeItem.setEnabled(false);
	}
	
	private void createDesc()
	{
		JTextArea desc = new JTextArea(getDesc(theProject));
		desc.setEditable(false);
		desc.setFont(new Font("Tahoma", Font.BOLD, 16));
		setConstraints(0,1,3,1,1,0.15);
		add(desc, constraints);
	}
	
	private String getDesc(Project project)
	{
		String projectDesc;
		Map<String, String> projectProps = theFileTree.getProperties(project);
		if(projectProps == null) return "No Description.";
		else projectDesc = projectProps.get("desc");
		if(projectDesc == null) return "No Description.";
		else return projectDesc;
	}
	
	/**
	 * Builds the list of Items contained within the project.
	 * Last Edited: 12/9/2019
	 * @author Dylan
	 * @return the JList of Items.
	 */
	public JList loadItems()
	{
		final JList list = new JList(theProject.getContents().toArray());
		list.setFont(new Font("Tahoma", Font.BOLD, 24));
		list.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2 || e.getClickCount() == 3)
				{
					Item select = (Item) list.getSelectedValue();
					new GUIItemView(select, theFileTree);
//					try
//					{
//						Desktop.getDesktop().open(file);
//					}
//					catch (IOException e1)
//					{
//						e1.printStackTrace();
//					}
				}
			}
		});
		return list;
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
	
	/**
	 * TODO
	 */
	public void refresh()
	{
		itemList = loadItems();
		itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(itemList);
	}
}
