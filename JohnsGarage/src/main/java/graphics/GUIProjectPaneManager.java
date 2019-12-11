/**
 * The panel that controls which Tab view is currently being displayed based on
 * the last Tab the user double-clicked. Reverts to title screen if the currently
 * displayed Tab is removed.
 * Last Edited: 12/6/2019
 * @author Dylan
 */
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
	private static final long serialVersionUID = 8240251102196104852L;

	private FileTree theFileTree;
	
	private HashMap<Tab, GUIProjectPane> panes;
	
	private Tab activePane;
	
	/**
	 * Creates a JPanel for swapping between Tab views
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 * @param fileTree
	 */
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
	
	/**
	 * Creates all Tab view panels based on user's Tabs
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 */
	private void createPanes()
	{
		for (Tab t : theFileTree.getTabs())
		{
			GUIProjectPane newPane = new GUIProjectPane(theFileTree, t);
			panes.put(t, newPane);
			add(newPane, t.toString());
		}
	}
	
	/**
	 * Adds a Tab view panel based on the passed Tab
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 * @param t Tab to be added
	 */
	public void addPane(Tab t)
	{
		GUIProjectPane newPane = new GUIProjectPane(theFileTree, t);
		panes.put(t, newPane);
		add(newPane, t.toString());
	}
	
	/**
	 * Removed the associated Tab view panel based on passed Tab
	 * Last Edited: 12/6/2019
	 * @author Dylan
	 * @param t Tab to be removed
	 */
	public void removePane(Tab t)
	{
		GUIProjectPane temp = panes.get(t);
		if (activePane == t) ((CardLayout) this.getLayout()).show(this, "_home");
		remove(temp);
		panes.remove(t);
	}
	
	/**
	 * Returns the currently viewed Tab view panel
	 * Last Edited: 12/4/2019
	 * @author Dylan
	 * @return currently viewed Tab view panel
	 */
	public Project getSelected()
	{
		return panes.get(activePane).getSelected();
	}
	
	/**
	 * Switches the viewed Tab panel to the passed Tab
	 * Last Edited: 12/6/2019
	 * @author Dylan
	 * @param t Tab to be viewed
	 */
	public void setPane(Tab t)
	{
		activePane = t;
		((CardLayout) this.getLayout()).show(this, activePane.toString());
	}
}
