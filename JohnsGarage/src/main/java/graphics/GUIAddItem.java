/**
 * GUIAddItem is the pop-up window which allows users to add items and their information to a project
 * Last Edited: 12/9/19
 * @author Dylan, James
 */
package graphics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import insides.FileTree;
import insides.Item;
import insides.Project;

public class GUIAddItem extends JFrame
{
	
	private static final long serialVersionUID = -234094099040318584L;

	private FileTree theFileTree;
	
	private Project theProject;
	
	private GUIProjectView theViewer;
	
	private GridBagConstraints constraints;
	
	private JTextField nameText;
	
	private JTextArea descText;
	
	private Path location;
	
	private JLabel filePath;
	
	/**
	 * Creates the add item window
	 * @param root
	 * @param parent
	 * @param viewer
	 */
	public GUIAddItem(FileTree root, Project parent, GUIProjectView viewer)
	{
		theFileTree = root;
		theProject = parent;
		theViewer = viewer;
		constraints = new GridBagConstraints();
		
		setTitle("Add Item");
		setBounds(200, 200, 400, 300);
		setResizable(false);
		getContentPane().setLayout(new GridBagLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		
		createName();
		createDesc();
		createSelect();
		createConfirm();
	}
	
	/**
	 * Builds the jlabel for name
	 */
	private void createName()
	{
		JLabel name = new JLabel("Name:");
		setConstraints(0,0,1,1,0.1,0.05);
		add(name, constraints);
		
		nameText = new JTextField();
		setConstraints(1,0,3,1,0.9,0.05);
		add(nameText, constraints);
	}
	
	/**
	 * Builds the description area
	 */
	private void createDesc()
	{
		JLabel desc = new JLabel("Description:");
		setConstraints(0,1,4,1,1,0.05);
		add(desc, constraints);
		
		descText = new JTextArea();
		descText.setLineWrap(true);
		descText.setWrapStyleWord(true);
		setConstraints(0,2,4,1,1,0.8);
		add(descText, constraints);
	}
	
	/**
	 * Builds the file chooser/select file option
	 */
	private void createSelect()
	{
		JButton select = new JButton("Select File");
		setConstraints(0,3,1,1,0.1,0.05);
		add(select, constraints);
		
		filePath = new JLabel();
		setConstraints(1,3,3,1,0.9,0.05);
		add(filePath, constraints);
		
		select.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showOpenDialog(GUIAddItem.this) == JFileChooser.APPROVE_OPTION)
				{
					location = fileChooser.getSelectedFile().toPath();
					filePath.setText(location.toString());
				}
			}
		});
	}
	
	/**
	 * Builds the confirm button and adds the item to the file tree
	 */
	private void createConfirm()
	{
		JPanel emptyspace1 = new JPanel();
		setConstraints(0,4,1,1,0.1,0.05);
		add(emptyspace1, constraints);
		
		JPanel emptyspace2 = new JPanel();
		setConstraints(1,4,1,1,0.7,0.05);
		add(emptyspace2, constraints);
		
		JButton confirm = new JButton("OK");
		setConstraints(2,4,1,1,0.1,0.05);
		add(confirm, constraints);
		
		JButton cancel = new JButton("Cancel");
		setConstraints(3,4,1,1,0.1,0.05);
		add(cancel, constraints);
		
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GUIAddItem.this.dispose();
			}
		});
		
		confirm.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (nameText.getText().equals("")) JOptionPane.showMessageDialog(GUIAddItem.this, "You must enter a name", "Error", JOptionPane.ERROR_MESSAGE);
				else if (location == null) JOptionPane.showMessageDialog(GUIAddItem.this, "You must choose a file", "Error", JOptionPane.ERROR_MESSAGE);
				else
				{
					String extension = "";
					int i = location.toString().lastIndexOf('.');
					if (i > 0) extension = location.toString().substring(i);
					String newName = nameText.getText() + extension;
					Item temp = theFileTree.newItem(newName, location, theProject);
					theFileTree.changeProperty(temp, "desc", descText.getText());
					theViewer.refresh();
					GUIAddItem.this.dispose();
				}
			}
		});
	}
	
	/**
	 * Sets the constraints of the GUI components using grid bag layout
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
