package kosodrzewina.model;

import javax.sound.sampled.AudioFormat.Encoding;

public class Sound {

	private byte[] soundTab;
	private boolean bigEndian;
	private int channels;
	private Encoding encoding;
	private double frameRate;
	private int sampleSizeInBits;
	
	
	public Sound(byte[] soundTab, boolean bigEndian, int channels, Encoding encoding2, double frameRate, int sampleSizeInBits){
		this.soundTab = soundTab;
		this.bigEndian = bigEndian;
		this.channels = channels;
		this.encoding = encoding2;
		this.frameRate = frameRate;
		this.sampleSizeInBits = sampleSizeInBits;
	}
	
	public byte[] getSoundTab() {
		return soundTab;
	}
	public void setSoundTab(byte[] soundTab) {
		this.soundTab = soundTab;
	}
	public boolean isBigEndian() {
		return bigEndian;
	}
	public void setBigEndian(boolean bigEndian) {
		this.bigEndian = bigEndian;
	}
	public int getChannels() {
		return channels;
	}
	public void setChannels(int channels) {
		this.channels = channels;
	}
	public boolean getEncoding() {
		if(this.encoding.toString().contains("SIGNED")) return true;
		else return false;
	}
	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
	}
	public float getFrameRate() {
		return (float) frameRate;
	}
	public void setFrameRate(Double frameRate) {
		this.frameRate = frameRate;
	}

	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}

	public void setSampleSizeInBits(int sampleSizeInBits) {
		this.sampleSizeInBits = sampleSizeInBits;
	}
	
}
