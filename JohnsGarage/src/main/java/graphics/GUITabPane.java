package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.Action;
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
	/**
	 * 
	 */
	private static final long serialVersionUID = -6346402233257430593L;
	
	private FileTree theFileTree;
	
	private JList tabList;
	
	private JScrollPane scrollPane;
	
	public GUITabPane(FileTree fileTree)
	{
		setLayout(new BorderLayout());
		theFileTree = fileTree;
		
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
	
	public JList loadTabs()
	{
		List<Tab> tabs = theFileTree.getTabs();
		JList list = new JList(tabs.toArray());
		list.setFont(new Font("Tahoma", Font.BOLD, 16));
		return list;
	}
	
	public Tab getSelected()
	{
		return (Tab) tabList.getSelectedValue();
	}
	
	public void refresh()
	{
		tabList = loadTabs();
		tabList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tabList);
	}
}