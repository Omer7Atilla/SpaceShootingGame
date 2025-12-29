package space;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

class SpaceFrame extends JFrame implements ActionListener{
	SpacePanel panel;
	JMenuBar bar;
	JMenu help;
	JMenu quit;
	JMenuItem about;
	JMenuItem instructions;
	JMenuItem exit;
	SpaceFrame(){
		panel = new SpacePanel();
		this.add(panel);
		bar = new JMenuBar();
		help = new JMenu("Help");
		quit = new JMenu("Quit");
		exit = new JMenuItem("Exit");
		about = new JMenuItem("About");
		instructions = new JMenuItem("Instructions");
		quit.add(exit);
		help.add(about);
		help.add(instructions);
		bar.add(quit);
		bar.add(help);
		exit.addActionListener(this);
		about.addActionListener(this);
		instructions.addActionListener(this);
		this.setJMenuBar(bar);
		this.setTitle("Space Invaders");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		panel.requestFocusInWindow();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == exit) {
			System.exit(0);
		}
		else if (e.getSource() == about) {
			JOptionPane.showMessageDialog(null, "Space Invaders Game by Ömer ATİLLA", "About Message", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (e.getSource() == instructions) {
			JOptionPane.showMessageDialog(null, "Shoot the most of the objects while dogding them", "How to play Message", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
