package player;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import extensions.Vector2D;

public class MouseInput implements MouseListener, MouseMotionListener {
	private static final MouseInput instance = new MouseInput();
	//TODO MouseInput dragging support
	private long[] lastClickTime;
	private boolean[] buttonPressed;
	private Vector2D position;

	public enum Button {
		BUTTON1, BUTTON2, BUTTON3
	};

	public static MouseInput Instance() {
		return MouseInput.instance;
	}

	private MouseInput() {
		this.position = new Vector2D(0, 0);
		this.buttonPressed = new boolean[3];
		this.lastClickTime = new long[3];
		this.flush();
	}

	public synchronized void flush() {
		this.flushButtons();
		this.flushClickTimes();
		this.flushPosition();
	}

	public synchronized void flushButtons() {
		for (int i = 0; i < 3; i++)
			this.buttonPressed[i] = false;
	}

	public synchronized void flushClickTimes() {
		for (int i = 0; i < 3; i++)
			this.lastClickTime[i] = 0;
	}

	public synchronized void flushPosition() {
		this.position.x = 0;
		this.position.y = 0;
	}

	public boolean buttonPresed(Button button) {
		switch (button) {
			case BUTTON1:
				return this.buttonPressed[0];
			case BUTTON2:
				return this.buttonPressed[1];
			case BUTTON3:
				return this.buttonPressed[2];
		}
		return false;
	}

	public long buttonLastClicked(Button button) {
		switch (button) {
			case BUTTON1:
				return this.lastClickTime[0];
			case BUTTON2:
				return this.lastClickTime[1];
			case BUTTON3:
				return this.lastClickTime[2];
		}
		return -1;
	}

	public Vector2D getPosition() {
		return this.position;
	}

	public synchronized void mouseClicked(MouseEvent e) {
		switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				this.lastClickTime[0] = System.currentTimeMillis();
				break;
			case MouseEvent.BUTTON2:
				this.lastClickTime[1] = System.currentTimeMillis();
				break;
			case MouseEvent.BUTTON3:
				this.lastClickTime[2] = System.currentTimeMillis();
				break;
		}
	}

	public synchronized void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				this.buttonPressed[0] = true;
				break;
			case MouseEvent.BUTTON2:
				this.buttonPressed[1] = true;
				break;
			case MouseEvent.BUTTON3:
				this.buttonPressed[2] = true;
				break;
		}
	}

	public synchronized void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				this.buttonPressed[0] = false;
				break;
			case MouseEvent.BUTTON2:
				this.buttonPressed[1] = false;
				break;
			case MouseEvent.BUTTON3:
				this.buttonPressed[2] = false;
				break;
		}
	}

	public synchronized void mouseMoved(MouseEvent e) {
		this.position.x = e.getX();
		this.position.y = e.getY();
	}

	public synchronized void mouseDragged(MouseEvent e) {
		this.position.x = e.getX();
		this.position.y = e.getY();
	}

	public void mouseEntered(MouseEvent arg0) {
	// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
	// TODO Auto-generated method stub

	}

}
