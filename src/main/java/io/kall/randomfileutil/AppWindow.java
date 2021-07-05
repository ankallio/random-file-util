package io.kall.randomfileutil;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;

public class AppWindow extends JFrame {
	
	private final RandomFileUtil util = new RandomFileUtil();
	
	private final JLabel lastDirLabel;
	private final JLabel lastFileLabel;
	private final JLabel lastFileSizeLabel;
	
	private final JLabel lblLastSearchDir;
	
	// /**
	// * Launch the application.
	// */
	// public static void main(String[] args) {
	// EventQueue.invokeLater(new Runnable() {
	// @Override
	// public void run() {
	// try {
	// AppWindow frame = new AppWindow();
	// frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }
	
	/**
	 * Create the frame.
	 */
	public AppWindow(String dir) {
		util.setLastSearchDir(dir);
		
		setTitle("Random");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 495, 426);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(e -> System.exit(0));
		mnFile.add(mntmExit);
		
		JPanel headerPanel = new JPanel();
		getContentPane().add(headerPanel, BorderLayout.NORTH);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		
		lblLastSearchDir = new JLabel("Last search dir");
		headerPanel.add(lblLastSearchDir);
		
		lastDirLabel = new JLabel("Last dir");
		headerPanel.add(lastDirLabel);
		
		lastFileLabel = new JLabel("Last file");
		headerPanel.add(lastFileLabel);
		
		lastFileSizeLabel = new JLabel("Last size");
		headerPanel.add(lastFileSizeLabel);
		
		JPanel centerPanel = new JPanel();
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		JButton btnNextRandomFile = new JButton("Next random file");
		btnNextRandomFile.addActionListener(e -> openRandom());
		centerPanel.add(btnNextRandomFile);
		
		JButton btnRenameLastOpened = new JButton("Rename last opened");
		btnRenameLastOpened.setEnabled(false);
		centerPanel.add(btnRenameLastOpened);
		
		JButton btnMoveFileTo = new JButton("Move file to another folder");
		btnMoveFileTo.setEnabled(false);
		centerPanel.add(btnMoveFileTo);
		
		JButton btnDeleteLastOpened = new JButton("Delete last opened file");
		btnDeleteLastOpened.setEnabled(false);
		centerPanel.add(btnDeleteLastOpened);
		
		JButton btnOpenLastFolder = new JButton("Open last folder");
		btnOpenLastFolder.setEnabled(false);
		centerPanel.add(btnOpenLastFolder);
		
		JButton btnSetSearchTerm = new JButton("Set search term");
		btnSetSearchTerm.setEnabled(false);
		centerPanel.add(btnSetSearchTerm);
	}
	
	public void openRandom() {
		try {
			util.openRandomFileFromLastSearchDir();
			updateLabels();
		} catch (IOException | FilesNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateLabels() {
		File file = util.getLastOpenedFile();
		lastFileLabel.setText("File: " + file.getName());
		lastDirLabel.setText("Dir: " + file.getParent());
		lastFileSizeLabel.setText("Size: " + FileUtils.byteCountToDisplaySize(file.length()));
		lblLastSearchDir.setText("Search in: " + util.getLastSearchDir());
	}
	
}
