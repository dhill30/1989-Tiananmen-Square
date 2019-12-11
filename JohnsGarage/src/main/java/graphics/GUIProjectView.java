/**
 * The window for viewing a Project's associated items and its description.
 * Also controls the adding/removing of items in the Project.
 * Last Edited: 12/10/2019
 * @author Dylan
 */
package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	
	private JButton removeItem;
	
	private JList itemList;
	
	/**
	 * Creates the JFrame window for viewing a Project's contents/description
	 * Last Edited: 12/5/2019
	 * @author Dylan
	 * @param fileTree main FileTree for file representation
	 * @param project associated Project for viewing contents
	 */
	public GUIProjectView(FileTree fileTree, Project project)
	{
		theFileTree = fileTree;
		theProject = project;
		constraints = new GridBagConstraints();
		
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
	 * Creates the add/remove item buttons and their associated actions
	 * Last Edited: 12/10/2019
	 * @author Dylan
	 */
	private void createAddRemove()
	{
		removeItem = new JButton("Remove Item");
		setConstraints(1,0,1,1,0.1,0.05);
		removeItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Item selected = (Item) itemList.getSelectedValue();
				if (selected == null) JOptionPane.showMessageDialog(GUIProjectView.this, "You must select an item", "Error", JOptionPane.ERROR_MESSAGE);
				else
				{
					int confirm = JOptionPane.showConfirmDialog(GUIProjectView.this, "Are you sure you want to remove \"" + selected + "\"?", "Remove Item", JOptionPane.YES_NO_OPTION);
					if (confirm == 0) theFileTree.delete(selected, theProject);
					if (theProject.getContents().size() == 0) removeItem.setEnabled(false);
					System.out.println("Item removed...");
				}
				refresh();
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
	
	/**
	 * Creates the description area for the Project
	 * Last Edited: 12/9/2019
	 * @author Dylan
	 */
	private void createDesc()
	{
		JTextArea desc = new JTextArea(getDesc(theProject));
		desc.setEditable(false);
		desc.setFont(new Font("Tahoma", Font.BOLD, 16));
		setConstraints(0,1,3,1,1,0.15);
		add(desc, constraints);
	}
	
	/**
	 * gets the Description property of an Project.
	 * Last Edited: 12/10/2019
	 * @author Sam, Dylan
	 * @param project Project to get description of
	 * @return the Description
	 */
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
	 * @return the JList of Items
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
				}
			}
		});
		return list;
	}
	
	/**
	 * Returns the remove button
	 * Last Edited: 12/10/2019
	 * @author Dylan
	 * @return remove Project button
	 */
	public JButton getRemoveButton()
	{
		return removeItem;
	}
	
	/**
	 * Refreshes the Project view to display any changes
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 */
	public void refresh()
	{
		itemList = loadItems();
		itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(itemList);
	}
	
	/**
	 * Sets GridBag constraints. To be used before adding a component.
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 * @param x horizontal grid location
	 * @param y vertical grid locations
	 * @param w width of component
	 * @param h height of component
	 * @param wx horizontal weight
	 * @param wy vertical weight
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
