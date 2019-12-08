package graphics;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import insides.FileTree;
import insides.Item;
import insides.Project;

public class GUIItemView extends JFrame {

private static final long serialVersionUID = 426473126064216924L;
	
	private FileTree theFileTree;
	
	private Item theItem;
	
	private GridBagConstraints constraints;
	
	private String itemName;
	
	private String itemDesc;
	
	private Path itemPath;
	
	public GUIItemView(File theFile, File itemInfo) {
		itemName = theFile.getName();
		int i = itemName.toString().lastIndexOf('.');
		String extension = "";
		if (i > 0) extension = itemName.toString().substring(i);
		itemName = itemName.replace(extension, "");
		System.out.println("fj: " + extension);
		
		setTitle(theFile.getName());
		setBounds(200, 200, 600, 400);
		setMinimumSize(new Dimension(600, 400));
		getContentPane().setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);	
		
		JLabel title = new JLabel(itemName);
		title.setFont(new Font("Tahoma", Font.BOLD, 32));
		setConstraints(0,0,1,1,0.8,0.05);
		add(title, constraints);
		JTextArea desc = new JTextArea(getDesc(itemInfo));
		desc.setEditable(false);
		desc.setFont(new Font("Tahoma", Font.BOLD, 32));
		setConstraints(0,1,1,1,0.8,0.05);
		add(desc, constraints);
	}
	
	private String getDesc(File itemInfo) {
		try {
			Scanner scanner = new Scanner(itemInfo);
			String tempItemName = "Item Name: " + itemName;
			System.out.println(tempItemName);
			while(scanner.hasNextLine()) {
				if(scanner.nextLine().equals(tempItemName)) {
					itemDesc = scanner.nextLine();
					break;
				}
				System.out.println("1");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(itemDesc);
		//itemDesc.replace("Item Description: ", "");
		return itemDesc;
	}

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
