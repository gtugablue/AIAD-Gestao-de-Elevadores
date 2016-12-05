package lift_management.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import repast.simphony.context.Context;
import repast.simphony.userpanel.ui.UserPanelCreator;

public class ConfigFrame extends JFrame {
	private Context context;
	
	public ConfigFrame(Context context) {
		this.context = context;
		
		this.setVisible(true);
		JButton button = new JButton("START");
		this.add(button);
	}
}
