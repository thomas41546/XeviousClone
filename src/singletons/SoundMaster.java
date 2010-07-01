package singletons;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.HashMap;

//TODO implement sound lengths
public class SoundMaster {
	private static final SoundMaster instance = new SoundMaster();

	public enum Sound {
		SHOOT, EXPLOSION
	}

	public class Metadata {
		private AudioClip audioClip;
		private long clipLength;
		private long lastPlayTime;
		private boolean looping;

		public Metadata(AudioClip audioclip, long cliplengthMS) {
			this.audioClip = audioclip;
			this.clipLength = cliplengthMS;
			this.lastPlayTime = System.currentTimeMillis() - this.clipLength;
			this.looping = false;
		}

		public boolean setPlay() {
			if ((System.currentTimeMillis() - this.lastPlayTime >= this.clipLength)
					&& !this.looping) {
				this.lastPlayTime = System.currentTimeMillis();
				this.audioClip.play();
				return true;
			}
			else
				return false;
		}

		public void setLoop() {
			this.lastPlayTime = System.currentTimeMillis() - this.clipLength;
			this.looping = true;
			this.audioClip.loop();
		}

		public void setStop() {
			this.lastPlayTime = System.currentTimeMillis() - this.clipLength;
			this.looping = false;
			this.audioClip.stop();
		}

	}

	//for au 16pcm .wav file, ~176 bytes for 1ms

	private static final HashMap<Sound, Metadata> soundMapper = new HashMap<Sound, Metadata>();

	public void init(Applet app) {
		SoundMaster.soundMapper.put(Sound.SHOOT,
				new Metadata(app.getAudioClip(app.getDocumentBase(), "snd/shoot.wav"), 278));
		SoundMaster.soundMapper.put(Sound.EXPLOSION,
				new Metadata(app.getAudioClip(app.getDocumentBase(), "snd/explosion.wav"), 278));
	}

	public void play(Sound sound) {
		SoundMaster.soundMapper.get(sound).setPlay();
	}

	public void loop(Sound sound) {
		SoundMaster.soundMapper.get(sound).setLoop();
	}

	public void stop(Sound sound) {
		SoundMaster.soundMapper.get(sound).setStop();
	}

	public void stopall() {
		for (Sound sound : SoundMaster.soundMapper.keySet())
			this.stop(sound);
	}

	public static SoundMaster Instance() {
		return SoundMaster.instance;
	}

}
