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

                //String path = "sounds/artificial/diff/270Hz.wav";
                String path = "sounds/seq/DWK_violin.wav";
                String partLenght = "1024";
                String[] args = {path, partLenght};
                Run.main(args);
                //ChartPanel cp = MethodsFFT.chart();
                
                //frame.getContentPane().add(cp);
            }
        });

    }
}
