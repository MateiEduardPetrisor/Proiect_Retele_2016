package common;

import java.io.Serializable;

public class Coordinates implements Serializable {
	private static final long serialVersionUID = 1L;
	private int xC;
	private int yC;

	public int getxC() {
		return xC;
	}

	public void setxC(int xC) {
		this.xC = xC;
	}

	public int getyC() {
		return yC;
	}

	public void setyC(int yC) {
		this.yC = yC;
	}

	public Coordinates(int xC, int yC) {
		super();
		this.xC = xC;
		this.yC = yC;
	}

	public Coordinates() {

	}

	public static void main(String[] args) {

	}

	@Override
	public String toString() {
		return getxC() + "," + getyC();
	}
}
