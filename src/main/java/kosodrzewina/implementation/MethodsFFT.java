package kosodrzewina.implementation;

import java.util.ArrayList;
import java.util.List;

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
	public static int hzResult;
	// -----
	public static int[] statPartsHz;
	
	/*
	 * UWAGA: Bierze srodek dzwieku.
	 */
	public static Sound fourierSpectrum(Sound originalSound) {
		Sound modifiedSound = new Sound(originalSound);
		int numFrames = modifiedSound.getNumFrames();
		
		
		double fftFrames[] = new double[numFrames];
		DoubleFFT_1D fft = new DoubleFFT_1D(fftFrames.length/2);
		
		// load from originalSound
		for(int i = 0; i < fftFrames.length/2; i++)
			fftFrames[i*2] = originalSound.getFrames()[0][i+(numFrames/2)];
		// FFT
		fft.complexForward(fftFrames);
		
		// Pre emfazy
//		preemphase(fftFrames, 0.99);
//		oknoGaussaCalosc(fftFrames, 0.99);
//		oknoHammingaCalosc(fftFrames);
//		oknoHanningaCalosc(fftFrames);
//		oknoBartlettaCalosc(fftFrames);
		
		// Zapis stanu fftFrames
		fftFramesStatic = fftFrames.clone();
		
		// Znalezienie pierwszego max
		int max = findMax(fftFrames);
		// obliczenie max w Hz
		double factor =(double) fftFrames.length / originalSound.getSampleRate();
//		System.out.println("factor: " + factor);
//		System.out.println( max/factor );
		max = (int) (max/factor);
		// Max w Hz
		System.out.println("czestotliwosc w Hz = " + max);
		
		
		// Zapis stanu max
		hzResult = max;
		
		// utworzenie tablicy z tonem hz
		double toneHz[][] = new double[1][originalSound.getFrames()[0].length];
		for(int i = 0; i < toneHz[0].length; i++) {
			toneHz[0][i] = Math.sin( 
					2.0*Math.PI * hzResult * 
					((double)i/modifiedSound.getSampleRate()) 
					);
		}
		// zapis tonu do modifiedSound
		modifiedSound.setFrames(toneHz);
		
		/*
		// inverse FFT
		fft.complexInverse(fftFrames, true);
		// save to modifiedSound
		for(int i = 0; i < fftFrames.length/2; i++)
			modifiedSound.getFrames()[0][i+(numFrames/2)] = fftFrames[i*2];
		*/
		
		return modifiedSound;
	}
	

	/**
	 * @param originalSound
	 * @param partLength
	 */
	public static Sound generateSoundFourier(Sound originalSound, int partLength) {
		
		// #1. Clone original sound
		Sound modifiedSound = new Sound(originalSound);
		
		// #2. Divide original sound
		List<double[]> soundParts = divideSound(originalSound, partLength);
		
		// #3. get each part Hz (FFT)
		int[] partsHz = new int[soundParts.size()];
		for(int i = 0; i < soundParts.size(); i++ ) {
			partsHz[i] = findHzFFT( soundParts.get(i), originalSound.getSampleRate() );
		}
		statPartsHz = partsHz;
		
		// #4. Generate tones for parts
		List<double[]> soundTones = generateTones(soundParts, partsHz, originalSound.getSampleRate());
		
		// #5. Merge tones and save them as modified sound frames
		double[] frames = mergeTones(soundTones, originalSound.getFrames()[0].length);
		double[][] fullFrames = new double[1][];
		fullFrames[0] = frames;
		modifiedSound.setFrames(fullFrames);

		return modifiedSound;
	}
	private static double[] mergeTones(List<double[]> soundTones, int framesLength) {
		double[] frames = new double[framesLength];
		
		double[] tone;
		int index = 0;
		for(int i = 0; i < soundTones.size(); i++) {
			tone = soundTones.get(i);
			for(int j = 0; j < tone.length; j++) {
				frames[index] = tone[j];
				index++;
			}
		}
		
		return frames;
	}
	private static List<double[]> generateTones(List<double[]> soundParts, int[] partsHz, long soundSampleRate) {
		List<double[]> soundTones = new ArrayList<double[]>();
		
		int length;
		double[] tone;
		for(int i = 0; i < soundParts.size(); i++) {
			length = soundParts.get(i).length;
			
			tone = new double[length];		
			for(int j = 0; j < length; j++) {
				tone[j] = Math.sin( 
						2.0*Math.PI * partsHz[i] * 
						((double)j/soundSampleRate) 
						);
			}
			
			soundTones.add(tone);
		}
		
		return soundTones;
	}
	private static int findHzFFT(double[] frames, long soundSampleRate) {
		int resultHz;
		
		// place frames to real part
		double fftFrames[] = new double[frames.length * 2];	
		for(int i = 0; i < frames.length; i++)
			fftFrames[i*2] = frames[i];
		
		// FFT
		DoubleFFT_1D fft = new DoubleFFT_1D(frames.length);
		fft.complexForward(fftFrames);
		
		// Find max
		int max = findMax(fftFrames);
		double factor =(double) fftFrames.length / soundSampleRate;
		max = (int) (max/factor);
		
		return max;
	}
	private static List<double[]> divideSound(Sound sound, int partLength) {
		int soundLength = sound.getNumFrames();
		int partsCount = soundLength/partLength;
		List<double[]> soundParts = new ArrayList<double[]>();

		// full length parts
		double[] part;
		for(int i = 0; i < partsCount; i++) {		
			part = new double[partLength];
			for(int j = 0; j < partLength; j++) {
				part[j] = sound.getFrames()[0][partLength*i + j];
			}
			soundParts.add( part );
		}
		// last part (not full sized)
		int lastLength = soundLength%partLength;
		if(lastLength > 0) {
			part = new double[ lastLength ];
			for(int j = 0; j < lastLength; j++) {
				part[j] = sound.getFrames()[0][partLength*partsCount + j];
			}
			soundParts.add( part );
		}
		
		return soundParts;
	}
	
	/*
	 * szuka tylko w pierwszej cwiartce
	 */
	private static int findMax(double fftFrames[]) {
		int index = 0;
		int range = fftFrames.length/4;
		
		double val = -1;
		for(int i = 0; i < range; i++)
			if( val < Math.abs(fftFrames[i]) ) {
				val = Math.abs(fftFrames[i]);
				index = i;
			}
		
		return index;
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
		int halfLng = 6000;
		double magnitude[] = new double[halfLng];
		
		for(int i = 0; i < halfLng; i++)
			magnitude[i] = Math.sqrt(
					Math.pow(fftFramesStatic[2*i], 2) + 
					Math.pow(fftFramesStatic[2*i+1], 2)
					);
		
		return magnitude;
	}
	
	public static ChartPanel chart() {
		double[] dataset = getPartMagnitude();
		XYSeries series = new XYSeries("Magnitude");
		for(int i = 0; i <dataset.length; i++)
			series.add(i*2, dataset[i]);
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
