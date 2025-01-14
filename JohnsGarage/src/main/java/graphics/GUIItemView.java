/**
 * GUIItemView is the window which displays stored info for the items
 * Last Edited 12/9/19
 * @author James
 */
package graphics;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import insides.FileTree;
import insides.Item;

public class GUIItemView extends JFrame {

private static final long serialVersionUID = 426473126064216924L;
	
	private FileTree theFileTree;
	
	private GridBagConstraints constraints;
	
	private String itemName;
	
	/**
	 * Builds the GUIItemView, used for seeing descriptions and giving options for dealing with Items.
	 * Last Edited: 12/9/2019
	 * @author James
	 * @param theItem associated item for viewing
	 * @param tree main FileTree for file representation
	 */
	public GUIItemView(final Item theItem, FileTree tree) {
		
		theFileTree = tree;
		constraints = new GridBagConstraints();
		itemName = theItem.getName();
		int i = itemName.toString().lastIndexOf('.');
		String extension = "";
		if (i > 0) extension = itemName.toString().substring(i);
		itemName = itemName.replace(extension, "");
		
		setTitle(theItem.getName());
		setBounds(200, 200, 600, 400);
		setMinimumSize(new Dimension(600, 400));
		getContentPane().setLayout(new GridBagLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);	
		
		JLabel title = new JLabel(itemName);
		title.setFont(new Font("Tahoma", Font.BOLD, 24));
		setConstraints(0,0,1,1,0.8,0.05);
		add(title, constraints);

		JTextArea desc = new JTextArea(getDesc(theItem));
		desc.setEditable(false);
		desc.setFont(new Font("Tahoma", Font.BOLD, 20));
		setConstraints(0,1,2,1,.3,0.95);
		add(desc, constraints);
		
		JButton file = new JButton("Open " + theItem.getName());
		file.setFont(new Font("Tahoma", Font.BOLD, 24));
		setConstraints(0,2,1,2,0.2,.05);
		add(file, constraints);
		file.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					Desktop.getDesktop().open(theItem.getPath().toFile());
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		
		JButton close = new JButton("Close");
		close.setFont(new Font("Tahoma", Font.BOLD, 24));
		setConstraints(1,2,1,1,0.2,.05);
		add(close, constraints);
		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
	}
	
	/**
	 * gets the Description property of an Item.
	 * Last Edited: 12/10/2019
	 * @author Sam, Dylan
	 * @param item Item to get description of
	 * @return the Description
	 */
	private String getDesc(Item item)
	{
		String itemDesc;
		Map<String, String> itemProps = theFileTree.getProperties(item);
		if(itemProps == null) return "No Description.";
		else itemDesc = itemProps.get("desc");
		if(itemDesc == null) return "No Description.";
		else return itemDesc;
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
