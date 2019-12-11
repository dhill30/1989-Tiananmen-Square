/**
 * The panel for displaying the user's Tabs. Uses a JScrollPane containing a JList.
 * Last Edited: 12/4/2019
 * @author Dylan
 */
package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import insides.FileTree;
import insides.Tab;

public class GUITabPane extends JPanel
{
	private static final long serialVersionUID = -6346402233257430593L;
	
	private FileTree theFileTree;
	
	private GUIProjectPaneManager projectManager;
	
	private JList tabList;
	
	private JScrollPane scrollPane;
	
	/**
	 * Creates a JScrollPane of the user's Tabs.
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 * @param fileTree main FileTree for file representation
	 * @param pm GUIProjectPaneManager used for swapping active Tab
	 */
	public GUITabPane(FileTree fileTree, GUIProjectPaneManager pm)
	{
		setLayout(new BorderLayout());
		theFileTree = fileTree;
		projectManager = pm;
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		scrollPane = new JScrollPane(panel);
		
		tabList = loadTabs();
		tabList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tabList);
		add(scrollPane);
		
		JLabel title = new JLabel("Tabs");
		title.setFont(new Font("Tahoma", Font.BOLD, 20));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setColumnHeaderView(title);
	}
	
	/**
	 * Loads a JList of the user's Tabs
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 * @return JList of the user's Tabs
	 */
	public JList loadTabs()
	{
		List<Tab> tabs = theFileTree.getTabs();
		final JList list = new JList(tabs.toArray());
		list.setFont(new Font("Tahoma", Font.BOLD, 16));
		list.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2 || e.getClickCount() == 3)
				{
					Tab t = (Tab) list.getSelectedValue();
					projectManager.setPane(t);
				}
			}
		});
		return list;
	}
	
	/**
	 * Returns the currently selected Tab
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 * @return currently selected Tab
	 */
	public Tab getSelected()
	{
		return (Tab) tabList.getSelectedValue();
	}
	
	/**
	 * Refreshes the Tab pane to display any changes
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 */
	public void refresh()
	{
		tabList = loadTabs();
		tabList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tabList);
	}
}