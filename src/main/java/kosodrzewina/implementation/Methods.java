package kosodrzewina.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kosodrzewina.model.Sound;

public class Methods {
	
	private static double numberProbe = 2048;
	private static int frameStart = 8000;
	/**
	 * Oblicza metode przejœ przez zero na surowo
	 * czyli totalne przejœcia przez 0
	 * @param s DŸwiêk
	 * @return czêstotliwoœc
	 */
	public static double zeroCrossingLine(Sound s){
		List<Long> frequencyList = new ArrayList<Long>();
		int j = 1;
		while((frameStart+(j*numberProbe)) < s.getSampleRate()-numberProbe){
			double count = frameStart+(j*numberProbe);
			double buffer[] = extractSoundTab(s.getFrames(),j);
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
			double periodsCountInSec = 44100/framesPerPeriod;
			frequencyList.add(Math.round(periodsCountInSec));
			j++;
		}
		return returnAverageFrequency(frequencyList);
//		return returnMedianFrequency(frequencyList);
	}
	
	/**
	 * Oblicza metode zero-crossing po upgrade
	 * czyli bardziej dok³adna metoda
	 * Dokonano ???TODO: napisa co dodano
	 * @param s
	 */
	public static double zeroCrossingLineUpdated(Sound s, double probe){
		List<Long> frequencyList = new ArrayList<Long>();
//		probe = 0.05;
		int j = 1;
		while((frameStart+(j*numberProbe)) < s.getSampleRate()-numberProbe){
			double count = frameStart+(j*numberProbe);
			double buffer[] = extractSoundTab(s.getFrames(),j);
			double numberOfZeroCrossings = 0;
			
			
			for(int i = 0 ; i < buffer.length-1 ; i++){
				if( (buffer[i] > buffer[i+1] && buffer[i] > probe && buffer[i+1] < probe)||
					(buffer[i] < buffer[i+1] && buffer[i] < -probe && buffer[i+1] > -probe)){
					numberOfZeroCrossings++;
				}
			}
			
			
	//		zeroCrossingRate = numberOfZeroCrossings / (double) (buffer.length - 1);
	//		return zeroCrossingRate;
			double numberOfPeriod = numberOfZeroCrossings/2;
			double framesPerPeriod = numberProbe/numberOfPeriod;
			double periodsCountInSec = 44100/framesPerPeriod;
			frequencyList.add(Math.round(periodsCountInSec));
			j++;
		}
		return returnAverageFrequency(frequencyList);
//		return returnMedianFrequency(frequencyList);
	}
	
	private static double[] extractSoundTab(double[][] tab, int count){
		double[][] temp = tab;
//		TODO:co jeœli ma 2 kana³y ???
		double[] buffer = new double[(int) numberProbe] ;
		int starter = (int) (frameStart+(count*numberProbe));
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

}
