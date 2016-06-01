package kosodrzewina.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kosodrzewina.model.Sound;

public class Methods {
	
	private static double numberProbe = 2048;
	private static int frameStart = 8000;
	private static int frameNextStart = 8000;
	
	public double[][] newSound;
	
	/**
	 * Oblicza metode przejœ przez zero na surowo
	 * czyli totalne przejœcia przez 0
	 * @param s DŸwiêk
	 * @return czêstotliwoœc
	 */
	public static double zeroCrossingLine(Sound s){
		List<Long> frequencyList = new ArrayList<Long>();
		List<Long> frequencySec = new ArrayList<Long>();
		Sound s2 = new Sound(s);
		double[][] newSound = s2.getFrames();
		int allSamplesCount = s.getFrames()[0].length;
		int allSamplesCountTemp = s.getFrames()[0].length;
		double samplePerSecond = s.getSampleRate();
		int c = 0;
		if(allSamplesCount % (int)s.getSampleRate() == 0)	c = allSamplesCount / (int)s.getSampleRate();
		else 												c = allSamplesCount / (int)s.getSampleRate()+1;
		
		
		for(int p = 0 ; p < c; p++) {
			if(allSamplesCountTemp - (int)s.getSampleRate() >= 0){
				samplePerSecond = s.getSampleRate();
				allSamplesCountTemp -= s.getSampleRate();
			}else{
				samplePerSecond = allSamplesCountTemp;
			}
			double krok = 0;
			if(p == 0){
				krok = frameStart;
			}else{
				krok = frameNextStart;
			}
			
			int j = 0;
			List<Long> tempList = new ArrayList<Long>();
			while((krok+(j*numberProbe)) < samplePerSecond- 2048){
				double count = krok+(j*numberProbe);
				double buffer[] = extractSoundTab(s.getFrames(),j,p,s.getSampleRate(),krok);
				double numberOfZeroCrossings = 0;
				for(int i = 1 ; i < buffer.length ; i++){
					if(buffer[i] * buffer[i-1] < 0){
						numberOfZeroCrossings++;
					}
				}
		//		zeroCrossingRate = numberOfZeroCrossings / (double) (buffer.length - 1);
		//		return zeroCrossingRate;
				double numberOfPeriod = numberOfZeroCrossings/2;
				double framesPerPeriod = numberProbe/numberOfPeriod;
				double periodsCountInSec = samplePerSecond/framesPerPeriod;
				frequencyList.add(Math.round(periodsCountInSec));
				tempList.add(Math.round(periodsCountInSec));
				j++;
			}
			frequencySec.add((long)returnAverageFrequency(tempList));
		}
		s2.setFrames(newSound);
		FileSaver.saveWav(s2);
		System.out.println(frequencySec.toString());
		if(frequencySec.size()>1 &&
		   frequencySec.get(frequencySec.size()-2) - frequencySec.get(frequencySec.size()-1) >400) 
			frequencySec.remove(frequencySec.size()-1);
		return returnAverageFrequency(frequencySec);
	}
	
	
	
	/**
	 * Oblicza metode zero-crossing po upgrade
	 * czyli bardziej dok³adna metoda
	 * Dokonano ???TODO: napisa co dodano
	 * @param s
	 */
	public static double zeroCrossingLineUpdated(Sound s, double probe){
		List<Long> frequencyList = new ArrayList<Long>();
		List<Long> frequencySec = new ArrayList<Long>();
		Sound s2 = new Sound(s);
		double[][] newSound = s2.getFrames();
		int allSamplesCount = s.getFrames()[0].length;
		int allSamplesCountTemp = s.getFrames()[0].length;
		double samplePerSecond = s.getSampleRate();
		int c = 0;
		if(allSamplesCount % (int)s.getSampleRate() == 0)	c = allSamplesCount / (int)s.getSampleRate();
		else 												c = allSamplesCount / (int)s.getSampleRate()+1;
		
		for(int p = 0 ; p < c; p++) {
			if(allSamplesCountTemp - (int)s.getSampleRate() >= 0){
				samplePerSecond = s.getSampleRate();
				allSamplesCountTemp -= s.getSampleRate();
			}else{
				samplePerSecond = allSamplesCountTemp;
			}
			
			double krok = 0;
			if(p == 0)	krok = frameStart;
			else		krok = frameNextStart;
			
			int j = 0;
			List<Long> tempList = new ArrayList<Long>();
			while((krok+(j*numberProbe)) < samplePerSecond- 2048){
				double count = krok+(j*numberProbe);
				double buffer[] = extractSoundTab(s.getFrames(),j,p,s.getSampleRate(),krok);
				double numberOfZeroCrossings = 0;
				buffer = filter(buffer);
				for(int i = 0 ; i < buffer.length-1 ; i++){
					
					newSound[0][(int) (krok+(j*numberProbe)+i)] = buffer[i];
					if( (buffer[i] > buffer[i+1] && buffer[i] > probe && buffer[i+1] < probe)||
						(buffer[i] < buffer[i+1] && buffer[i] < -probe && buffer[i+1] > -probe)){
						numberOfZeroCrossings++;
					}
				}
//			for(int i = 1 ; i < buffer.length ; i++){
//				if(buffer[i] * buffer[i-1] < 0){
//					numberOfZeroCrossings++;
//				}
//			}
				double numberOfPeriod = numberOfZeroCrossings/2;
				double framesPerPeriod = numberProbe/numberOfPeriod;
				double periodsCountInSec = samplePerSecond/framesPerPeriod;
				frequencyList.add(Math.round(periodsCountInSec));
				tempList.add(Math.round(periodsCountInSec));
				j++;
			}
			frequencySec.add((long) returnAverageFrequency(tempList));
		}
		
		double[][] soundtab = new double[1][2048*frequencyList.size()]; 
		for(int k = 0; k < frequencyList.size(); k++){
			Double[] tab = generateSinusoidalSignal(1, frequencyList.get(k), 2048, 44100);
			for(int l = 0 ;l <2048;l++){
				soundtab[0][l+k*2048] = tab[l];
			}
		}
		
		
		s2.setFrames(soundtab);
		FileSaver.saveWav(s2);
		System.out.println(frequencySec.toString());
		
		if(frequencySec.size()>1 &&
		   frequencySec.get(frequencySec.size()-2) - frequencySec.get(frequencySec.size()-1) >400) 
				frequencySec.remove(frequencySec.size()-1);
	return returnAverageFrequency(frequencySec);
	}
	
	private static double[] filter(double[] tab){
		
		for(int i = 2 ; i < tab.length -2; i++){
			tab[i] = (tab[i-2]+tab[i-1] + tab[i] + tab[i+1]+tab[i+1])/5;
		}
		
		for(int i = 1 ; i < tab.length -1; i++){
			tab[i] = (tab[i-1] + tab[i] + tab[i+1])/3;
		}
		
		return tab;
	}
	
	/**
	 * TODO: Widmo fouriera
	 * @return
	 */
	public static double fourierSpectrum(){
		
		return (Double) null;
	}
	
	private static double[] extractSoundTab(double[][] tab, int count, int per, long l, double krok){
		double[][] temp = tab;
		double[] buffer = new double[(int) numberProbe] ;
		int starter = (int) (krok+(count*numberProbe)+(l*per));
//		if(starter < frames){
			for(int i = 0; i < numberProbe; i++){
				buffer[i] = temp[0][starter+i];
			}
			return buffer;
//		}
//		return null;
	}
	
	private static double returnMedianFrequency(List<Long> list){
		Collections.sort(list);
		
	    int middle = list.size()/2;
	    if (list.size()%2 == 1) {
	        return list.get(middle);
	    } else {
	        return (list.get(middle-1) + list.get(middle)) / 2.0;
	    }
	}
	
	private static double returnAverageFrequency(List<Long> list){
		Collections.sort(list);
		double sum = 0;
		for(int i = 0 ; i<list.size();i++){
			sum += list.get(i);
		}
		return sum/list.size();
	}
	
	private static double oknoGaussa(double n, double N, double alfa){
		double licznik = n-(N-1)/2;
		double mianownik = alfa*(N-1)/2;
		double b = Math.pow(-0.5*(licznik/mianownik),2);
		double value = Math.pow(Math.E, b);
		
		return value;
	}
	
	private static double oknoHamminga(double n, double N){
		double licznik = 2*Math.PI*n;
		double mianownik = N-1;
		double value = 0.53836 - 0.46164 * Math.cos(licznik/mianownik);
		return value;
	}

	private static double oknoHanninga(double n, double N){
		double licznik = 2*Math.PI*n;
		double mianownik = N-1;
		double value = 0.5*(1-Math.cos(licznik/mianownik));
		return value;
	}
	
	private static double oknoBartletta(double n, double N){
		double part = N-1;
		double value = (2/part)*((part/2)-Math.abs(n-(part/2)));
		return value;
	}
	
	private static double W(double n, double k, double N){
		double i = Math.sqrt(-1);
		//TODO: poprawic i
		return Math.cos((2*Math.PI*n*k)/N)-i*Math.sin((2*Math.PI*n*k)/N);
	}
	
	
    public static Double[] generateSinusoidalSignal(double amplitude, double frequency, double time, double samplingFrequency){
        Double[] samples = new Double[(int) (time)];
        double T = 1.0/frequency;
        for(double i = 0; i < samples.length; i++){
            double t = i / samplingFrequency;
            samples[(int) i] = computeSinusoidalSignalValue(amplitude, t, T);
        }

        return samples;
    }

    private static double computeSinusoidalSignalValue(double amplitude, double t, double T){
        return amplitude * Math.sin(((2*Math.PI)*(t))/T);
    }
	
}






