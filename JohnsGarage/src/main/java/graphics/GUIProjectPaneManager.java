package graphics;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import insides.FileTree;
import insides.Project;
import insides.Tab;

public class GUIProjectPaneManager extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8240251102196104852L;

	private FileTree theFileTree;
	
	private HashMap<Tab, GUIProjectPane> panes;
	
	private Tab activePane;
	
	public GUIProjectPaneManager(FileTree fileTree)
	{
		setLayout(new CardLayout());
		theFileTree = fileTree;
		panes = new HashMap<Tab, GUIProjectPane>();
		activePane = new Tab(null, "_home");
		
		JLabel title = new JLabel("Jon's Gahraj");
		title.setFont(new Font("Tahoma", Font.BOLD, 48));
		JPanel home = new JPanel();
		home.setBackground(Color.WHITE);
		home.add(title);
		add(home, activePane.toString());
		createPanes();
	}
	
	private void createPanes()
	{
		for (Tab t : theFileTree.getTabs())
		{
			GUIProjectPane newPane = new GUIProjectPane(theFileTree, t);
			panes.put(t, newPane);
			add(newPane, t.toString());
		}
	}
	
	public void addPane(Tab t)
	{
		GUIProjectPane newPane = new GUIProjectPane(theFileTree, t);
		panes.put(t, newPane);
		add(newPane, t.toString());
	}
	
	public void removePane(Tab t)
	{
		GUIProjectPane temp = panes.get(t);
		if (activePane == t)
		{
			CardLayout layout = (CardLayout) this.getLayout();
			layout.show(this, "_home");
		}
		remove(temp);
		panes.remove(t);
	}
	
	public Project getSelected()
	{
		return panes.get(activePane).getSelected();
	}
	
	public void setPane(Tab t)
	{
		activePane = t;
	}
}
