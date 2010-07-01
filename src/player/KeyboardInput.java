package player;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

	private static final int KEY_COUNT = 256;

	private static KeyboardInput instance;

	private enum KeyState {
		RELEASED, // Not down
		PRESSED, // Down, but not the first time
		ONCE
		// Down for the first time
	}

	// Current state of the keyboard
	private boolean[] currentKeys = null;

	// Polled keyboard state
	private KeyState[] keys = null;

	public static KeyboardInput Instance() {
		if (KeyboardInput.instance == null)
			KeyboardInput.instance = new KeyboardInput();
		return KeyboardInput.instance;
	}

	public KeyboardInput() {
		this.currentKeys = new boolean[KeyboardInput.KEY_COUNT];
		this.keys = new KeyState[KeyboardInput.KEY_COUNT];
		for (int i = 0; i < KeyboardInput.KEY_COUNT; ++i)
			this.keys[i] = KeyState.RELEASED;
	}

	public synchronized void poll() {
		for (int i = 0; i < KeyboardInput.KEY_COUNT; ++i)
			// Set the key state
			if (this.currentKeys[i]) {
				// If the key is down now, but was not
				// down last frame, set it to ONCE,
				// otherwise, set it to PRESSED
				if (this.keys[i] == KeyState.RELEASED)
					this.keys[i] = KeyState.ONCE;
				else
					this.keys[i] = KeyState.PRESSED;
			}
			else
				this.keys[i] = KeyState.RELEASED;
	}

	public synchronized void flush() {// NOTE: unsure about 'synchronised'
		for (int i = 0; i < KeyboardInput.KEY_COUNT; ++i)
			this.keys[i] = KeyState.RELEASED;
	}

	public synchronized boolean anyKeyDown() { //synchronised?
		for (KeyState currentKey : this.keys)
			if (currentKey == KeyState.PRESSED)
				return true;
		return false;
	}

	public boolean keyDown(int keyCode) {
		return (this.keys[keyCode] == KeyState.ONCE) || (this.keys[keyCode] == KeyState.PRESSED);
	}

	public boolean keyDownOnce(int keyCode) {
		return this.keys[keyCode] == KeyState.ONCE;
	}

	public synchronized void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if ((keyCode >= 0) && (keyCode < KeyboardInput.KEY_COUNT))
			this.currentKeys[keyCode] = true;
	}

	public synchronized void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if ((keyCode >= 0) && (keyCode < KeyboardInput.KEY_COUNT))
			this.currentKeys[keyCode] = false;
	}

	public void keyTyped(KeyEvent e) {
	// Not needed
	}
}
