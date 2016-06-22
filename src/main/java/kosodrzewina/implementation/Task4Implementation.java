package kosodrzewina.implementation;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import kosodrzewina.model.Sound;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Task4Implementation {
	
	public static Sound globalSound;
	
	/**
	 * RUN
	 */	
	public static List<double[][]> run(Sound originalSound, int M, int R, int L, int fc,
			int window, int zeros) {
		// Sound parts
		List<double[]> resultParts = new ArrayList<double[]>();
		
		List<double[][]> resultData = new ArrayList<double[][]>();
		
		// Chart Data
		List<double[]> fftMag = new ArrayList<double[]>();
		List<double[]> fftPha = new ArrayList<double[]>();
		List<double[]> fftCalcMag = new ArrayList<double[]>();
		List<double[]> fftCalcPha = new ArrayList<double[]>();

		// Clone original sound
		Sound modifiedSound = new Sound(originalSound);
		modifiedSound.setNumChannels(1);

		// [1] Divide original sound ( windows )
		List<double[]> soundParts = divideSoundWindows(originalSound, M, R);
		// [1] Window functions
		int N = M + L - 1;
		calcWindows(soundParts, N, window, zeros);

		// [2] Calc filter
		int fs = 44100;
		double[] filter = calcFilter(fc, fs, L);
		
		// [3] Window with filter
		List<double[]> filterL = new ArrayList<double[]>();
		filterL.add(filter);
		calcWindows(filterL, N, window, zeros);
		filter = filterL.get(0);
		
		// [4] Filter fft
		double[] filterFFT = calcFFT(filter);
		// [4] Filter magnitude
		double[] filterMagnitude = calcSpectrumMagnitude(filterFFT);
		
		System.out.println("N: " + N);
		System.out.println("part size: " + soundParts.get(0).length);
		System.out.println("filter size: " + filter.length);
		
		double[] fft, spectrum, mult, windowMag, windowPha;
		for (int i = 0; i < soundParts.size(); i++) {

			// [4] Window fft
			fft = calcFFT(soundParts.get(i));
			
			// [4] Window Magnitude
			windowMag = calcSpectrumMagnitude(fft);
			fftMag.add(windowMag);
			// Window Phase
			windowPha = calcSpectrumPhase(fft);
			fftPha.add(windowPha);
			fftCalcPha.add(windowPha);

			
			// [4] mult Window and Filter
			mult = tableMult(windowMag, filterMagnitude);
			
			// [5] back to complex
			double[] reverseFFT = backToComplex(mult, windowPha);
			// [5] reverse FFT
			double[] part = reverseFFT(reverseFFT);
			resultParts.add(part);
			
			// FFT filter Magnitude
			fftCalcMag.add(mult);
			
			
			
		}
		
		// Generate modified Sound
		partsToSound(modifiedSound, resultParts, R);
		globalSound = modifiedSound;
		
		// Chart Data
		resultData.add( listToDouble2d(fftMag) );
		resultData.add( listToDouble2d(fftCalcMag) );
		resultData.add( listToDouble2d(fftPha) );
		resultData.add( listToDouble2d(fftCalcPha) );
		
		return resultData;
	}
	
	/**
	 * PRIVS
	 */
	private static List<double[]> divideSoundWindows(Sound sound, int partLength, int distance) {
		int soundLength = sound.getNumFrames();
		int partsCount = (soundLength-partLength)/distance;
		List<double[]> soundParts = new ArrayList<double[]>();

		// full length parts
		double[] part;
		for(int i = 0; i < partsCount; i++) {		
			part = new double[partLength];
			for(int j = 0; j < partLength; j++) {
				part[j] = sound.getFrames()[0][distance*i + j];
			}
			soundParts.add( part );
		}
		
		return soundParts;
	}
	private static void partsToSound(Sound sound, List<double[]> resultParts, int distance) {
		double[] result = new double[ sound.getFrames()[0].length ];
		
		for(int i = 0; i < resultParts.size(); i++) {
			for(int j = 0; j < resultParts.get(i).length; j++) {
				result[i*distance + j] += resultParts.get(i)[j];
			}
		}
			
		
	}
	private static double[] calcFFT(double[] frames) {
		// place frames to real part
		double fftFrames[] = new double[frames.length * 2];	
		for(int i = 0; i < frames.length; i++)
			fftFrames[i*2] = frames[i];
		
		// FFT
		DoubleFFT_1D fft = new DoubleFFT_1D(frames.length);
		fft.complexForward(fftFrames);
		
		return fftFrames;
	}
	private static double[] reverseFFT(double[] frames) {
		int size = frames.length/2;
		double[] result = new double[size];
		
		DoubleFFT_1D fft = new DoubleFFT_1D(size);
		fft.complexInverse(frames, true);
		
		for(int i = 0; i < size; i++)
			result[i] = frames[2*i];
		
		return result;
			
	}
	private static double[] calcSpectrumMagnitude(double[] fft) {
		int size = fft.length/2;
		double[] magnitude = new double[size];
		
		for (int i = 0; i < size; i++)
			magnitude[i] = Math.sqrt(Math.pow(fft[2 * i], 2) + Math.pow(fft[2 * i + 1], 2));

		return magnitude;
	}
	private static double[] calcSpectrumPhase(double[] fft) {
		int size = fft.length/2;
		double[] phase = new double[size];
		
		for(int i = 0; i < size; i++) {
			phase[i] = Math.atan2( fft[2*i+1], fft[2*i]	);
		}
		
		return phase;
	}
	private static double[][] listToDouble2d(List<double[]> list) {
		double[][] array = new double[list.size()][];
		
		for(int i = 0; i < list.size(); i++)
			array[i] = list.get(i);
		
		return array;
	}
	private static double[] tableMult(double[] tab1, double[] tab2) {
		double[] result = new double[tab1.length];
		
		for(int i = 0; i < tab2.length; i++) 
			result[i] = tab1[i]*tab2[i];
		
		return result;
	}
	private static double[] backToComplex(double[] mag, double[] pha) {
		double[] result = new double[mag.length*2];
		
		for(int i = 0; i < pha.length; i++) {
			result[2*i] = mag[i] * cos(pha[i]);
			result[2*i+1] = mag[i] * sin(pha[i]);
		}
		
		return result;
	}
	/**
	 * Windows
	 */
	private static void calcWindows(List<double[]> parts, int N, int window, int zeros) {
		
		for(int i = 0; i < parts.size(); i++)
			if(window == 0)
				parts.set(i, rectangleWindow(parts.get(i), window, zeros) );
		
	}
	private static double[] rectangleWindow(double[] table, int N, int zeros) {
		double[] result = new double[N];
		int M = table.length;
		
		if(zeros == 0)
			for(int i = 0; i < N; i++)
				if( i < M)
					result[i] = table[i];
				else
					result[i] = 0;
		else if(zeros == 1)
			for(int i = 0; i < N; i++)
				if( i < M/2 || i > N-(M/2) )
					result[i] = table[i];
				else
					result[i] = 0;
		
		
		return result;
	}
	/**
	 * filter
	 */
	private static double[] calcFilter(int fc, int fs, int L) {
		double[] result = new double[L];
		
		for(int i = 0; i < L; i++)
			if(i == L/2)
				result[i] = (2*fc)/fs;
			else {
				double nominator = Math.sin( ( (2*PI*fc)/fs )*( i-(L/2) ) );
				double denominator = PI * ( i-(L/2) );
				result[i] = nominator/denominator;
			}

		return result;
	}

}











