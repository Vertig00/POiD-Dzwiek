package kosodrzewina.model;

public class Sound {
	
	private double[][] frames;
	private int numChannels;
	private int validBits;
	private long sampleRate;
	
	public double[][] getFrames() {
		return frames;
	}
	public void setFrames(double[][] frames) {
		this.frames = frames;
	}
	public int getNumChannels() {
		return numChannels;
	}
	public void setNumChannels(int numChannels) {
		this.numChannels = numChannels;
	}
	public int getValidBits() {
		return validBits;
	}
	public void setValidBits(int validBits) {
		this.validBits = validBits;
	}
	public long getSampleRate() {
		return sampleRate;
	}
	public void setSampleRate(long sampleRate) {
		this.sampleRate = sampleRate;
	}
	
	public int getNumFrames() {
		return frames[0].length;
	}

	public void info() {
		System.out.println("Sound class:");
		System.out.println("  frames channels:number - " + numChannels + ":" + frames[0].length);
		System.out.println("  valid Bits - " + validBits);
		System.out.println("  sample Rate - " + sampleRate);
		System.out.println("  time - " + (frames[0].length/sampleRate) );
		System.out.println("");
	}
	
	public Sound(Sound sound) {
		this.frames = new double[sound.getFrames().length][];
		for(int i = 0; i < sound.getFrames().length; i++)
			this.frames[i] = sound.getFrames()[i].clone();
		this.numChannels = sound.numChannels;
		this.validBits = sound.getValidBits();
		this.sampleRate = sound.getSampleRate();
	}
	
}




