/**
 * GUIAddProject is the pop-up window which allows users to add projects and their information to the file tree
 * Last Edited: 12/9/19
 * @author Dylan
 */
package graphics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import insides.FileTree;
import insides.Project;
import insides.Tab;

public class GUIAddProject extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1106706898326899745L;
	
	private FileTree theFileTree;
	
	private Tab theTab;
	
	private GUIProjectPane theViewer;
	
	private GridBagConstraints constraints;
	
	private JTextField nameText;
	
	private JTextArea descText;
	
	/**
	 * Creates the add project window
	 * @param root
	 * @param parent
	 * @param viewer
	 */
	public GUIAddProject(FileTree root, Tab parent, GUIProjectPane viewer)
	{
		theFileTree = root;
		theTab = parent;
		theViewer = viewer;
		constraints = new GridBagConstraints();
		
		setTitle("Add Project");
		setBounds(150, 150, 400, 300);
		setResizable(false);
		getContentPane().setLayout(new GridBagLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		
		createName();
		createDesc();
		createConfirm();
	}
	/*
	 * Creates the name jlabel
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
	
	/*
	 * Creates the description area
	 */
	private void createDesc()
	{
		JLabel desc = new JLabel("Description:");
		setConstraints(0,1,4,1,1,0.05);
		add(desc, constraints);
		
		descText = new JTextArea();
		descText.setLineWrap(true);
		descText.setWrapStyleWord(true);
		setConstraints(0,2,4,1,1,0.85);
		add(descText, constraints);
	}
	
	/*
	 * Creates the confirm and cancel JButtons and implements adding a project to the file tree
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
				GUIAddProject.this.dispose();
			}
		});
		
		confirm.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (nameText.getText().equals("")) JOptionPane.showMessageDialog(GUIAddProject.this, "You must enter a name", "Error", JOptionPane.ERROR_MESSAGE);
				else
				{
					Project temp = theFileTree.newProject(nameText.getText(), theTab);
					theFileTree.changeProperty(temp, "desc", descText.getText());
					theViewer.getRemoveButton().setEnabled(true);
					theViewer.refresh();
					GUIAddProject.this.dispose();
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
