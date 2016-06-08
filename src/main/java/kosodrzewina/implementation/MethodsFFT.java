package kosodrzewina.implementation;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	// Vars
	public static double thresholdDivider = 20.0;
	public static double minThreshold = 8.0;
	public static double minMaxesDiff = 50.0;
	
	public static List<double[]> fftFramesStatic;
	// -----
	public static int[] statPartsHz;
	public static long staticSampleRate;

	/**
	 * @param originalSound
	 * @param partLength
	 */
	public static Sound generateSoundFourier(Sound originalSound, int partLength) {
		fftFramesStatic = new ArrayList<double[]>();
		
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
	private static List<double[]> generateTonesPro(List<double[]> soundParts, int[] partsHz, long soundSampleRate) {
		List<double[]> soundTones = new ArrayList<double[]>();
		
		double div = 1.1;
		
		int length;
		double[] tone;
		int time = 0;
		
		// 0
		length = soundParts.get(0).length;
		tone = new double[length];
		for(int j = 1; j < length; j++) {
			tone[j] = Math.sin( 
					2.0*Math.PI * partsHz[0] * 
					((double)time/soundSampleRate) 
					);
			time++;
		}
		soundTones.add(tone);	
		// 
		for(int i = 1; i < soundParts.size(); i++) {
			length = soundParts.get(i).length;
				
			tone = new double[length];
			for(int j = 1; j < length/div; j++) {
				tone[j] = Math.sin( 
						2.0*Math.PI * 
						( partsHz[i-1] + ( ((double)j/((double)length/(double)div))*(partsHz[i]-partsHz[i-1]) ) )
						* ((double)time/soundSampleRate) 
						);
				time++;
			}
			for(int j = (int)(length/div); j < length; j++) {
				tone[j] = Math.sin( 
						2.0*Math.PI * partsHz[i] * 
						((double)time/soundSampleRate) 
						);
				time++;
			}
			
			soundTones.add(tone);
		}
		
		return soundTones;
	}
	private static int findHzFFT(double[] frames, long soundSampleRate) {
		int resultHz;
		staticSampleRate = soundSampleRate;
		
		// place frames to real part
		double fftFrames[] = new double[frames.length * 2];	
		for(int i = 0; i < frames.length; i++)
			fftFrames[i*2] = frames[i];
		
		// FFT
		DoubleFFT_1D fft = new DoubleFFT_1D(frames.length);
		fft.complexForward(fftFrames);
		
		fftFramesStatic.add(fftFrames);
		
		// Find max
		//int max = findMax(fftFrames);
		double factor =(double) (fftFrames.length/2) / soundSampleRate;
		int max = findMedianMax( fftFramesStatic.size()-1, factor );
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
			if( val < Math.abs(fftFrames[i*2]) ) {
				val = Math.abs(fftFrames[i*2]);
				index = i;
			}
		
		return index;
	}
	private static int findMedianMax(int ind, double factor) {
		int index = 0;
		// get magnitude
		double[] magnitude = getMagnitude(ind);
		// find maxes
		List<Integer> maxes = findMaxes(magnitude);
		
		// calc factor
		double threshold = 0;
		for(int i = 0; i < maxes.size(); i++)
			if(threshold < magnitude[maxes.get(i)])
				threshold = magnitude[maxes.get(i)];
		threshold = threshold / thresholdDivider;
		if(threshold < minThreshold) threshold = minThreshold;
		
		// take maxes only if( max > factor )
		List<Integer> sievedMaxes = new ArrayList<Integer>();
		for(int i = 0; i < maxes.size(); i++)
			if(magnitude[maxes.get(i)] >= threshold)
				sievedMaxes.add(maxes.get(i));
		
		// calc distances and median
		if(sievedMaxes.size()<3)
			return 0;
		List<Double> dists = new ArrayList<Double>();
		double[] distances = new double[sievedMaxes.size()-2];	
		for(int i = 0; i < sievedMaxes.size()-2; i++)
			if( (sievedMaxes.get(i+1) - sievedMaxes.get(i))/factor > minMaxesDiff ) {
//				distances[i] = sievedMaxes.get(i+1) - sievedMaxes.get(i);
				dists.add( (double) (sievedMaxes.get(i+1) - sievedMaxes.get(i)) );
			}
		
		// parse list to array
		distances = new double[dists.size()];
		for(int i = 0; i < dists.size(); i++) 
			distances[i] = dists.get(i);
		// sort and median
		Arrays.sort(distances);
		index = Math.round( (float) distances[ distances.length/2 ]  );
		
		return index;
	}
	private static List<Integer> findMaxes(double[] magnitude) {
		List<Integer> maxes = new ArrayList<Integer>();
		
		double xL, L, Lx;
		for(int i = 1; i < magnitude.length-1; i++) {
			xL = magnitude[i-1];
			L = magnitude[i];
			Lx = magnitude[i+1];
			
			if( xL < L && L > Lx)
				maxes.add(i);
		}
			
		return maxes;
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

	private static double[] getMagnitude(int index) {
		int halfLng = fftFramesStatic.get(index).length/4;
		
		double magnitude[] = new double[halfLng];
		for (int i = 0; i < halfLng; i++)
			magnitude[i] = Math.sqrt(Math.pow(fftFramesStatic.get(index)[2 * i], 2) + Math.pow(fftFramesStatic.get(index)[2 * i + 1], 2));

		return magnitude;
	}
	public static ChartPanel chart(int index) {
		double[] dataset = getMagnitude(index);
		XYSeries series = new XYSeries("Magnitude part: "+index+", hz:"+statPartsHz[index]);
		
		double factor =(double) (fftFramesStatic.get(index).length/2) / staticSampleRate;
		for(int i = 0; i <dataset.length; i++)
			series.add(i/factor, dataset[i]);
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
	public static ChartPanel chartShort(int index, int divider) {
		double[] dataset = getMagnitude(index);
		XYSeries series = new XYSeries("Magnitude part: "+index+", hz:"+statPartsHz[index]);
		
		double factor =(double) (fftFramesStatic.get(index).length/2) / staticSampleRate;
		for(int i = 0; i <dataset.length/divider; i++)
			series.add(i/factor, dataset[i]);
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
	public static JFreeChart chartShortNP(int index, int divider) {
		double[] dataset = getMagnitude(index);
		XYSeries series = new XYSeries("Magnitude part: "+index+", hz:"+statPartsHz[index]);
		
		double factor =(double) (fftFramesStatic.get(index).length/2) / staticSampleRate;
		for(int i = 0; i <dataset.length/divider; i++)
			series.add(i/factor, dataset[i]);
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

		return chart;
	}
	
	
}
// TODO: zmienic robienie charta
