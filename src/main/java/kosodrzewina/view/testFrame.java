package kosodrzewina.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;

import kosodrzewina.implementation.MethodsFFT;
import kosodrzewina.implementation.Run;

public class testFrame extends JFrame implements KeyListener {
	
	private static int a;
	private ChartPanel oldChart;
	
	public testFrame() {
		super("Charts");
		a=0;
		
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		// String path = "sounds/artificial/diff/270Hz.wav";
		String path = "sounds/seq/DWK_violin.wav";
		String partLenght = "2048";
		String[] args = { path, partLenght };
		Run.main(args);

		// wykres
		placeChart(a);

		// klawisze
		addKeyListener(this);
		
		
	}
	private void refreshChart(int a) {
		getContentPane().remove(oldChart);
		
		placeChart(a);
	}
	private void placeChart(int a) {
		ChartPanel cp = MethodsFFT.chartShort(a, 4);
		oldChart = cp;
		getContentPane().add(cp);

		this.pack();
	}
	
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent arg0) {
		int code = arg0.getKeyCode();
        keysCheck(code);	
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void keysCheck(int c) {
		// nastepny
		outerloop:
		if( c == 39) {
			if(MethodsFFT.statPartsHz.length-1 <= a)
				break outerloop;
			a++;
			refreshChart(a);
		}
		// poprzedni
		outerloop:
		if( c == 37) {
			if(a <= 0)
				break outerloop;
			a--;
			refreshChart(a);
		}
	}

}
