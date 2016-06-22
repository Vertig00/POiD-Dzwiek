package kosodrzewina.implementation;

import java.util.ArrayList;
import java.util.List;

import kosodrzewina.model.Sound;

public class ImpulseFilter {

	private Sound sound;
	private final int M,L,R;	//M - d³ugoœ okna, L - d³ugoœc filtra, R - przesuniêcie
	private final double fo;
	private final double zeroFill;
	private final int window;
	private static final double fp = 44100;
	
	private List<Double> parameters = new ArrayList<Double>();
	private List<Double> soundList = new ArrayList<Double>();
	
	
	public ImpulseFilter(Sound sound, int M, int L, int R, double frequencyCut, double zeroFill, int window){
		this.sound = sound;
		this.M = M;
		this.L = L;
		this.R = R;
		this.fo = frequencyCut;
		this.zeroFill = zeroFill;
		this.window = window;
		copySound(sound);
		designateParametersH();
		WindowMultiplyParameters();
		
	}
	
	
	
	
	
	
	public void timeFilter(){
		for (int i = 0; i < sound.getFrames()[0].length; i += R) {
			List<Double> operationList = new ArrayList<Double>();
			List<Double> tempList = new ArrayList<Double>();
			FillListWithZerosAtEnd(i, operationList);				//wype³nienie zerami na koñcu
			
			
			copyList(operationList, tempList);						//skopiowanie listy
			
			
			
			
		}
	}
	
	
	
	//wyznaczenie wspó³czynników W podstawce by³o L ale od pêczka M
	public void designateParametersH(){
		for (int i = 0; i < M-1; i++) {
			if(i == (M-1)/2)
				parameters.add(2*fo/fp);
			else{
				double counter = Math.sin( ((2*Math.PI*fo)/fp)*(i-(M-1)/2) );
				double denominator = Math.PI*(i-(L-1)/2);
				double result = counter/denominator;
				parameters.add(result);
			}
		}
//		return parameters;
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
	
	
	private void WindowMultiplyParameters(){
		for (int i = 0; i < parameters.size(); i++) {
			parameters.set(i, parameters.get(i)*getSelectedWindow(i));
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
	
	private void copySound(Sound s){
		for (int i = 0; i < s.getFrames()[0].length; i++) {
			soundList.add(s.getFrames()[0][i]);
		}

		for (int i = 0; i < sound.getFrames()[0].length-M; i += R) {
			for(int j = 0; j < M; j++){
				soundList.set(j+i, 0.00);
			}	
		}
	}

	private void FillListWithZerosAtEnd(int i, List<Double> list){
		for(int j = i; j < M*2; j++){
			if(j<M)			list.add(sound.getFrames()[0][j]);
			else			list.add(0.00);
		}
	}
	
	private void copyList(List<Double> entry, List<Double> out){
		for(int i = 0; i < entry.size(); i++)
			out.add(entry.get(i));
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
		return fo;
	}
	public double getZeroFill() {
		return zeroFill;
	}
	public int getWindow() {
		return window;
	}


	public List<Double> getSoundList() {
		return soundList;
	}


	public void setSoundList(List<Double> soundList) {
		this.soundList = soundList;
	}

}
