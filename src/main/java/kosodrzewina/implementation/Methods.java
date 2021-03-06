package kosodrzewina.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kosodrzewina.model.Sound;

public class Methods {
	
	private static double numberProbe = 0;
	private static int frameStart = 0;
	
	public static double getNumberProbe() {
		return numberProbe;
	}

	public static void setNumberProbe(double numberProbe) {
		Methods.numberProbe = numberProbe;
	}
	
	public double[][] newSound;
	
	/**
	 * Oblicza metode przej� przez zero na surowo
	 * czyli totalne przej�cia przez 0
	 * @param s D�wi�k
	 * @return cz�stotliwo�c
	 */
	public static double zeroCrossingLine(Sound s){
		List<Long> frequencyList = new ArrayList<Long>();
		int allSamplesCount = s.getFrames()[0].length;
		double samplePerSecond = s.getSampleRate();
			
			int j = 0;
			List<Long> tempList = new ArrayList<Long>();
			while((frameStart+(j*numberProbe)) < allSamplesCount- numberProbe){
				double count = frameStart+(j*numberProbe);
				double buffer[] = extractSoundTab(s.getFrames(),j);
				double numberOfZeroCrossings = 0;
				for(int i = 1 ; i < buffer.length ; i++){
					if(buffer[i] * buffer[i-1] < 0){
						numberOfZeroCrossings++;
					}
				}
				double numberOfPeriod = numberOfZeroCrossings/2;
				double framesPerPeriod = numberProbe/numberOfPeriod;
				double periodsCountInSec = allSamplesCount/framesPerPeriod;
				frequencyList.add(Math.round(periodsCountInSec));
				tempList.add(Math.round(periodsCountInSec));
				j++;
			}
		return returnMedianFrequency(frequencyList);
	}
	
	
	
	/**
	 * Oblicza metode zero-crossing po upgrade
	 * czyli bardziej dok�adna metoda
	 * Dokonano ???TODO: napisa co dodano
	 * @param s
	 */
//	public static double zeroCrossingLineUpdated(Sound s, double percent){
//		List<Long> frequencyList = new ArrayList<Long>();
//		List<Long> frequencySec = new ArrayList<Long>();
//		Sound s2 = new Sound(s);
//		double[][] newSound = s2.getFrames();
//		int allSamplesCount = s.getFrames()[0].length;
//		int allSamplesCountTemp = s.getFrames()[0].length;
//		double samplePerSecond = s.getSampleRate();
//		int c = 0;
//		if(allSamplesCount % (int)s.getSampleRate() == 0)	c = allSamplesCount / (int)s.getSampleRate();
//		else 												c = allSamplesCount / (int)s.getSampleRate()+1;
//		
//		for(int p = 0 ; p < c; p++) {
//			if(allSamplesCountTemp - (int)s.getSampleRate() >= 0){
//				samplePerSecond = s.getSampleRate();
//				allSamplesCountTemp -= s.getSampleRate();
//			}else{
//				samplePerSecond = allSamplesCountTemp;
//			}
//			
//			double krok = 0;
//			if(p == 0)	krok = frameStart;
//			else		krok = frameNextStart;
//			
//			int j = 0;
//			List<Long> tempList = new ArrayList<Long>();
//			while((krok+(j*numberProbe)) < samplePerSecond- numberProbe){
//				double count = krok+(j*numberProbe);
//				double buffer[] = extractSoundTab(s.getFrames(),j,p,s.getSampleRate(),krok);
//				double numberOfZeroCrossings = 0;
//				buffer = filter(buffer);
//				double probe = squareSum(buffer);
//				for(int i = 0 ; i < buffer.length-1 ; i++){
//					
//					newSound[0][(int) (krok+(j*numberProbe)+i)] = buffer[i];
//					if( (buffer[i] > buffer[i+1] && buffer[i] > probe && buffer[i+1] < probe)||
//						(buffer[i] < buffer[i+1] && buffer[i] < -probe && buffer[i+1] > -probe)){
//						numberOfZeroCrossings++;
//					}
//				}
////			for(int i = 1 ; i < buffer.length ; i++){
////				if(buffer[i] * buffer[i-1] < 0){
////					numberOfZeroCrossings++;
////				}
////			}
//				double numberOfPeriod = numberOfZeroCrossings/2;
//				double framesPerPeriod = numberProbe/numberOfPeriod;
//				double periodsCountInSec = samplePerSecond/framesPerPeriod;
//				frequencyList.add(Math.round(periodsCountInSec));
//				tempList.add(Math.round(periodsCountInSec));
//				j++;
//			}
//			frequencySec.add((long) returnAverageFrequency(tempList));
//		}
//		
////		double[][] soundtab = new double[1][2048*frequencyList.size()]; 
////		for(int k = 0; k < frequencyList.size(); k++){
////			Double[] tab = generateSinusoidalSignal(1, frequencyList.get(k), numberProbe, 44100);
////			for(int l = 0 ;l <2048;l++){
////				soundtab[0][l+k*2048] = tab[l];
////			}
////		}
////		
////		
////		s2.setFrames(soundtab);
////		FileSaver.saveWav(s2);
////		System.out.println(frequencySec.toString());
////		
////		if(frequencySec.size()>1 &&
////		   frequencySec.get(frequencySec.size()-2) - frequencySec.get(frequencySec.size()-1) >400) 
////				frequencySec.remove(frequencySec.size()-1);
////	return returnAverageFrequency(frequencySec);
//		return returnMedianFrequency(frequencyList);
//	}
	
	
	
	
	
	public static double zeroCrossingLineUpdated(Sound s, double percent){
		List<Long> frequencyList = new ArrayList<Long>();
		int allSamplesCount = s.getFrames()[0].length;
		double samplePerSecond = s.getSampleRate();
		Sound temp = new Sound(s);
			
			int j = 0;
			List<Long> tempList = new ArrayList<Long>();
			while((frameStart+(j*numberProbe)) < allSamplesCount- numberProbe){
				double count = frameStart+(j*numberProbe);
				double buffer[] = extractSoundTab(s.getFrames(),j);
				double numberOfZeroCrossings = 0;
				buffer = filter(buffer);
				double probe = squareSum(buffer,percent);
				for(int i = 0 ; i < buffer.length-1 ; i++){
					
					if( (buffer[i] > buffer[i+1] && buffer[i] > probe && buffer[i+1] < probe)||
						(buffer[i] < buffer[i+1] && buffer[i] < -probe && buffer[i+1] > -probe)){
						numberOfZeroCrossings++;
					}
				}
				double numberOfPeriod = numberOfZeroCrossings/2;
				double framesPerPeriod = numberProbe/numberOfPeriod;
				double periodsCountInSec = allSamplesCount/framesPerPeriod;
				frequencyList.add(Math.round(periodsCountInSec));
				tempList.add(Math.round(periodsCountInSec));
				j++;
			}
		
			//generowanie d�wi�ku
		double[][] soundtab = new double[1][(int) (numberProbe*frequencyList.size())]; 
		for(int k = 0; k < frequencyList.size(); k++){
			double Amp = 0;
			double buffer[] = extractSoundTab(temp.getFrames(),k);
			for(int i = 0 ; i < numberProbe;i++){
				if(Amp < buffer[i])	Amp = buffer[i];
			}

			Double[] tab = generateSinusoidalSignal(Amp, frequencyList.get(k), numberProbe, 44100);
			for(int l = 0 ;l <numberProbe;l++){
				soundtab[0][(int) (l+k*numberProbe)] = tab[l];
			}
		}
		temp.setFrames(soundtab);
		FileSaver.saveWav(temp);
		
		return returnMedianFrequency(frequencyList);
	}
	
	
	
	
	
	
	private static double squareSum(double[] tab, double percent){
		double sum = 0;
		percent = percent/100;
		for(int i = 0; i< tab.length; i++){
			sum += Math.pow(tab[i], 2);
		}
//		System.out.println("suma kwadrat�w: "+sum);
//		System.out.println("Po procent: " + sum);
		return sum * percent;
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
	
	private static double[] extractSoundTab(double[][] tab, int count){
		double[][] temp = tab;
		double[] buffer = new double[(int) numberProbe] ;
		int starter = (int) (frameStart+(count*numberProbe));
//		if(starter < frames){
			for(int i = 0; i < numberProbe; i++){
				buffer[i] = temp[0][starter+i];
				System.out.println(buffer[i]);
			}
			return buffer;
//		}
//		return null;
	}
	
	private static double returnMedianFrequency(List<Long> list){
		Collections.sort(list);
		System.out.println(list.toString());
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






