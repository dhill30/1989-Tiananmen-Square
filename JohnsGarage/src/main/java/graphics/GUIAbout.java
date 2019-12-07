package graphics;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import insides.Version;

public class GUIAbout extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5979766530012655663L;
	
	private static final Dimension FRAME_SIZE = new Dimension(550, 480);

	/**
	 * Create the dialog.
	 */
	public GUIAbout()
	{
		setLayout(null);
		setSize(FRAME_SIZE);
		setResizable(false);
		setTitle("About");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		
		JLabel logo = new JLabel();
		logo.setIcon(new ImageIcon("src/main/resources/logo.png"));
		logo.setBounds(-231, 105, 793, 322);
		add(logo);
		
		JLabel info = new JLabel("Developed By: Samuel Adams, Mike Fly, Dylan Hill, and James McHugh", SwingConstants.CENTER);
		info.setFont(info.getFont().deriveFont(15f));
		info.setBounds(10, 11, 521, 36);
		add(info);
		
		Version ver = null;
		try
		{
			ver = new Version();
		}
		catch (IOException e)
		{
			System.out.println("Could not fetch version number");
			e.printStackTrace();
		}
		JLabel version = new JLabel("Version Number: " + ver.getVersion(), SwingConstants.CENTER);
		version.setFont(version.getFont().deriveFont(16f));
		version.setBounds(10, 58, 521, 36);
		add(version);
	}
}
