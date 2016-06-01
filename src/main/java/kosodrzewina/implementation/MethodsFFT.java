package kosodrzewina.implementation;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import kosodrzewina.model.Sound;

public class MethodsFFT {
	
	public static double[] fftFramesStatic; 
	
	public static Sound fourierSpectrum(Sound originalSound) {
		System.out.println("   fourierSpercturm:start");
		Sound modifiedSound = new Sound(originalSound);
		int numFrames = modifiedSound.getNumFrames();
		
		DoubleFFT_1D fft = new DoubleFFT_1D(numFrames);
		double fftFrames[] = new double[numFrames*2];
		
		// load from originalSound
		for(int i = 0; i < numFrames; i++)
			fftFrames[i*2] = originalSound.getFrames()[0][i];
		// FFT
		System.out.println("   fourierSpercturm:fft:start");
		fft.complexForward(fftFrames);
		System.out.println("   fourierSpercturm:fft:end");
		
		// TODO
		//preemphase(fftFrames, 0.9);
		fftFramesStatic = fftFrames.clone();
		
		// inverse FFT
		System.out.println("   fourierSpercturm:inversefft:start");
		fft.complexInverse(fftFrames, true);
		System.out.println("   fourierSpercturm:inversefft:end");
		// save to modifiedSound
		for(int i = 0; i < numFrames; i++)
			modifiedSound.getFrames()[0][i] = fftFrames[i*2];
		
		System.out.println("   fourierSpercturm:end");
		return modifiedSound;
	}
	
	private static void preemphase(double fftFrames[], double alpha) {
		
		for(int i = 1; i < fftFrames.length/2; i++)
			fftFrames[2*i] = fftFrames[2*i] 
					- (alpha*fftFrames[2*(i-1)] );
		
	}
	
	private static double[] getMagnitude() {
		int halfLng = fftFramesStatic.length/2;
		double magnitude[] = new double[halfLng];
		
		for(int i = 0; i < halfLng; i++)
			magnitude[i] = Math.sqrt(
					Math.pow(fftFramesStatic[2*i], 2) + 
					Math.pow(fftFramesStatic[2*i+1], 2)
					);
		
		return magnitude;
	}
	
	public static ChartPanel chart() {
		double[] dataset = getMagnitude();
		XYSeries series = new XYSeries("PROtest");
		for(int i = 0; i <dataset.length; i++)
			series.add(i, dataset[i]);
		XYSeriesCollection data = new XYSeriesCollection(series);
		
		JFreeChart chart = ChartFactory.createXYLineChart(
				"testPro",          // chart title
				"X",
				"Y",
				data,                // data
				PlotOrientation.VERTICAL,
	            true,                // include legend
	            true,
	            false);
		ChartPanel cp = new ChartPanel(chart);
		
		return cp;
	}
	
}
