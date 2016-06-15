package kosodrzewina.implementation;

import java.util.ArrayList;
import java.util.List;

import kosodrzewina.model.Sound;

public class ImpulseFilter {

	private Sound sound;
	private final double M,L,R;
	private final double fc;
	private final double zeroFill;
	private final int window;
	private static final double fs = 44100;
	
	private List<Double> parameters = new ArrayList<Double>();
	
	
	public ImpulseFilter(Sound sound, double M, double L, double R, double frequencyCut, double zeroFill, int window){
		this.sound = sound;
		this.M = M;
		this.L = L;
		this.R = R;
		this.fc = frequencyCut;
		this.zeroFill = zeroFill;
		this.window = window;
	}
	
	
	
	
	
	//wyznaczenie wsp�czynnik�w
	public List<Double> designateParametersH(){
		for (int i = 0; i < L-1; i++) {
			if(i == (L-1)/2)
				parameters.add(2*fc/fs);
			else{
				double counter = Math.sin( ((2*Math.PI*fc)/fs)*(i-(L-1)/2) );
				double denominator = Math.PI*(i-(L-1)/2);
				double result = counter/denominator;
				parameters.add(result);
			}
		}
		return parameters;
	}
	
	//n - 0,1,...,M-1
	private double HannWindow(double n){
		double counter = 2*Math.PI*n;
		double denominator = M-1;
		
		return 0.5-0.5*Math.cos(counter/denominator);
	}
	
	private double HammingWindow(double n){
		double counter = 2*Math.PI*n;
		double denominator = M-1;
		
		return 0.54-0.46*Math.cos(counter/denominator);
	}
	
	private double rectangleWindow(int n){
		
		//TODO: napisac warunek
		if(n > -1 && n < M){
			return 1;
		}else{
			return 0;
		}
	}
	
	//rodzaj okna
	public double getSelectedWindow(int n){
		double windowValue = 0;
		if(window == 0){
			windowValue = rectangleWindow(n);
		}else if(window == 1){
			windowValue = HannWindow(n);
		}else if(window ==2){
			windowValue = HammingWindow(n);
		}
		return windowValue;
	}
	
	public static List<Double> fill0(int lenght){
		List<Double> list = new ArrayList<Double>();
		for (int i = 0; i < lenght; i++) {
			list.set(i, (double) 0);
		}
		return list;
	}






	public Sound getSound() {
		return sound;
	}
	public void setSound(Sound sound) {
		this.sound = sound;
	}
	public double getM() {
		return M;
	}
	public double getL() {
		return L;
	}
	public double getR() {
		return R;
	}
	public double getFrequencyCut() {
		return fc;
	}
	public double getZeroFill() {
		return zeroFill;
	}
	public int getWindow() {
		return window;
	}

}