package kosodrzewina.view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;

import kosodrzewina.implementation.MethodsFFT;
import kosodrzewina.implementation.Run;

public class App {
	public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Charts");

                frame.setSize(600, 400);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

                Run.main(null);
                ChartPanel cp = MethodsFFT.chart();

                frame.getContentPane().add(cp);
            }
        });

    }
}
