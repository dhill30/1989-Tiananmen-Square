package graphics;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import insides.FileTree;
import insides.Tab;

public class GUIProjectPaneManager extends JPanel
{
	private FileTree theFileTree;
	
	private HashMap<Tab, GUIProjectPane> panes;
	
	private String activePane;
	
	public GUIProjectPaneManager(FileTree fileTree)
	{
		setLayout(new CardLayout());
		theFileTree = fileTree;
		panes = new HashMap<Tab, GUIProjectPane>();
		activePane = "_home";
		
		JPanel home = new JPanel();
		home.setBackground(Color.WHITE);
		JLabel title = new JLabel("Jon's Gahraj");
		title.setFont(new Font("Tahoma", Font.BOLD, 48));
		home.add(title);
		add(home, "_home");
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
		if (activePane.equals(t.toString()))
		{
			CardLayout layout = (CardLayout) this.getLayout();
			layout.show(this, "_home");
		}
		remove(temp);
		panes.remove(t);
	}
	
	public void setPane(Tab t)
	{
		activePane = t.toString();
	}
}
