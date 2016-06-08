package kosodrzewina.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;

import kosodrzewina.implementation.MethodsFFT;
import kosodrzewina.implementation.Run;

public class App {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				JFrame frame = new testFrame();
//
//				frame.setSize(600, 400);
//				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//				frame.setVisible(true);
//
//				// String path = "sounds/artificial/diff/270Hz.wav";
//				String path = "sounds/seq/DWK_violin.wav";
//				String partLenght = "2048";
//				String[] args = { path, partLenght };
//				Run.main(args);
//				ChartPanel cp = MethodsFFT.chart(a);
//
//				frame.getContentPane().add(cp);
//				
//				// klawisze
//				frame.addKeyListener(this);
			}
		});

	}
	
}
