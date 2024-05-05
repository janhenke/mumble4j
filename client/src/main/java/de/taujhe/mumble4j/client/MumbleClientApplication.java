package de.taujhe.mumble4j.client;

import javax.swing.*;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

/**
 * Main application class.
 *
 * @author Jan Henke (Jan.Henke@taujhe.de)
 */
public class MumbleClientApplication
{
	private final JFrame mainFrame;

	public MumbleClientApplication()
	{
		mainFrame = new JFrame("Mumble4j Client");
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void showWindow()
	{
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	public static void main(String[] args)
	{
		FlatMacDarkLaf.setup();

		final MumbleClientApplication clientApplication = new MumbleClientApplication();

		SwingUtilities.invokeLater(clientApplication::showWindow);
	}
}
