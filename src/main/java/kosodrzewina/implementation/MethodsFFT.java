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
	
	/*
	 * UWAGA: Bierze srodek dzwieku.
	 */
	public static Sound fourierSpectrum(Sound originalSound) {
		System.out.println("   fourierSpercturm:start");
		Sound modifiedSound = new Sound(originalSound);
		int numFrames = modifiedSound.getNumFrames();
		
		
		double fftFrames[] = new double[numFrames];
		DoubleFFT_1D fft = new DoubleFFT_1D(fftFrames.length/2);
		
		// load from originalSound
		for(int i = 0; i < fftFrames.length/2; i++)
			fftFrames[i*2] = originalSound.getFrames()[0][i+(numFrames/2)];
		// FFT
		System.out.println("   fourierSpercturm:fft:start");
		fft.complexForward(fftFrames);
		System.out.println("   fourierSpercturm:fft:end");
		
		// Pre emfazy
//		preemphase(fftFrames, 0.9);
//		oknoGaussaCalosc(fftFrames, 0.95);
//		oknoHammingaCalosc(fftFrames);
//		oknoHanningaCalosc(fftFrames);
//		oknoBartlettaCalosc(fftFrames);
		
		// Zapis stanu fftFrames
		fftFramesStatic = fftFrames.clone();
		
		// TODO
		
		// inverse FFT
		System.out.println("   fourierSpercturm:inversefft:start");
		fft.complexInverse(fftFrames, true);
		System.out.println("   fourierSpercturm:inversefft:end");
		// save to modifiedSound
		for(int i = 0; i < fftFrames.length/2; i++)
			modifiedSound.getFrames()[0][i+(numFrames/2)] = fftFrames[i*2];
		
		System.out.println("   fourierSpercturm:end");
		return modifiedSound;
	}
	
	private static void oknoGaussaCalosc(double fftFrames[], double alpha) {
		int count = fftFrames.length/2;
		
		for(int i = 0; i < count; i++)
			fftFrames[2*i] = fftFrames[2*i] * oknoGaussa(i, count, alpha);
	}
	
	private static double oknoGaussa(double n, double N, double alfa){
		double licznik = n-(N-1)/2;
		double mianownik = alfa*(N-1)/2;
		double b = Math.pow(-0.5*(licznik/mianownik),2);
		double value = Math.pow(Math.E, b);
		
		return value;
	}
	
	private static void oknoHammingaCalosc(double fftFrames[]) {
		int count = fftFrames.length/2;
		
		for(int i = 0; i < count; i++)
			fftFrames[2*i] = fftFrames[2*i] * oknoHamminga(i, count);
	}
	
	private static double oknoHamminga(double n, double N){
		double licznik = 2*Math.PI*n;
		double mianownik = N-1;
		double value = 0.53836 - 0.46164 * Math.cos(licznik/mianownik);
		return value;
	}
	
	private static void oknoHanningaCalosc(double fftFrames[]) {
		int count = fftFrames.length/2;
		
		for(int i = 0; i < count; i++)
			fftFrames[2*i] = fftFrames[2*i] * oknoHanninga(i, count);
	}
	
	private static double oknoHanninga(double n, double N){
		double licznik = 2*Math.PI*n;
		double mianownik = N-1;
		double value = 0.5*(1-Math.cos(licznik/mianownik));
		return value;
	}
	
	private static void oknoBartlettaCalosc(double fftFrames[]) {
		int count = fftFrames.length/2;
		
		for(int i = 0; i < count; i++)
			fftFrames[2*i] = fftFrames[2*i] * oknoBartletta(i, count);
	}
	
	private static double oknoBartletta(double n, double N){
		double part = N-1;
		double value = (2/part)*((part/2)-Math.abs(n-(part/2)));
		return value;
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
	private static double[] getPartMagnitude() {
		int halfLng = 2000;
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
		XYSeries series = new XYSeries("Magnitude");
		for(int i = 0; i <dataset.length; i++)
			series.add(i, dataset[i]);
		XYSeriesCollection data = new XYSeriesCollection(series);
		
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Spectrums",          // chart title
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
