package kosodrzewina.model;

import java.util.Arrays;

import javax.sound.sampled.AudioFormat.Encoding;

public class Sound {

	private double[] soundTab;
//	private boolean bigEndian;
	private int channels;
	//	private Encoding encoding;
	private long frameRate;
	private int sampleSizeInBits;
	private double min, max;
	
	public Sound(){}
	
	

	public Sound(double[] soundTab, int channels, long frameRate, int sampleSizeInBits, double min, double max) {
		this.soundTab = soundTab;
		this.channels = channels;
		this.frameRate = frameRate;
		this.sampleSizeInBits = sampleSizeInBits;
		this.min = min;
		this.max = max;
	}



	public double[] getSoundTab() {
		return soundTab;
	}

//	public boolean isBigEndian() {
//		return bigEndian;
//	}

	public int getChannels() {
		return channels;
	}

//	public Encoding getEncoding() {
//		return encoding;
//	}

	public double getFrameRate() {
		return frameRate;
	}

	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}
	
	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public void setSoundTab(double[] soundTab) {
		this.soundTab = soundTab;
	}

//	public void setBigEndian(boolean bigEndian) {
//		this.bigEndian = bigEndian;
//	}

	public void setChannels(int channels) {
		this.channels = channels;
	}

//	public void setEncoding(Encoding encoding) {
//		this.encoding = encoding;
//	}

	public void setFrameRate(long frameRate) {
		this.frameRate = frameRate;
	}

	public void setSampleSizeInBits(int sampleSizeInBits) {
		this.sampleSizeInBits = sampleSizeInBits;
	}
	
	public void setMin(double min) {
		this.min = min;
	}

	public void setMax(double max) {
		this.max = max;
	}
	
	public Sound setSoundTab1(double[] soundTab) {
		this.soundTab = soundTab;
		return null;
	}

//	public Sound setBigEndian1(boolean bigEndian) {
//		this.bigEndian = bigEndian;
//		return null;
//	}

	public Sound setChannels1(int channels) {
		this.channels = channels;
		return null;
	}

//	public Sound setEncoding1(Encoding encoding) {
//		this.encoding = encoding;
//		return null;
//	}

	public Sound setFrameRate1(long frameRate) {
		this.frameRate = frameRate;
		return null;
	}

	public Sound setSampleSizeInBits1(int sampleSizeInBits) {
		this.sampleSizeInBits = sampleSizeInBits;
		return null;
	}
	
	public Sound setMin1(double min) {
		this.min = min;
		return null;
	}

	public Sound setMax1(double max) {
		this.max = max;
		return null;
	}

	@Override
	public String toString() {
		return "Sound["+ /*"soundTab=" + Arrays.toString(soundTab) +*/ ", channels=" + channels
				+  ", frameRate=" + frameRate + ", sampleSizeInBits=" + sampleSizeInBits
				+ ", min=" + min + ", max=" + max +	"]";
	}
	
}
