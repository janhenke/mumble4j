package de.taujhe.mumble4j.client;

import javax.swing.*;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

/**
 * Main application class.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public class MumbleClientApplication extends JFrame
{
	public MumbleClientApplication()
	{
		super("Mumble4j Client");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void showWindow()
	{
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(String[] args)
	{
		FlatMacDarkLaf.setup();

		final MumbleClientApplication clientApplication = new MumbleClientApplication();
		SwingUtilities.invokeLater(clientApplication::showWindow);
	}
}
