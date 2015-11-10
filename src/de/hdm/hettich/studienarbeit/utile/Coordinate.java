/**
 * @(#)Coordinate.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.utile;

/**
 * Diese Klasse dient dazu ein einfaches Integer-Wertepaar darzustellen. Dabei
 * kann entweder eine Position (also ein Punkt) dargestellt werden, oder auch
 * eine Größe (Höhe und Breite).
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class Coordinate {

	/**
	 * x-Wert der Koordinate; Negative-Werte können verwendet werden.
	 */
	private int x;

	/**
	 * y-Wert der Koordinate; Negative-Werte können verwendet werden.
	 */
	private int y;

	/**
	 * Leerer Konstruktor, der eine Instanz von <code>Coordinate</code> mit x-
	 * und y-Wert gleich null erstellt.
	 */
	public Coordinate() {
		this(0, 0);
	}

	/**
	 * Konstruktor, der eine Instanz von <code>Coordinate</code> erstellt, deren
	 * x- und y-Wert der übergebenen Coordinate entspricht.
	 * 
	 * @param c
	 */
	public Coordinate(Coordinate c) {
		this(c.getX(), c.getY());
	}

	/**
	 * Konstruktor, der eine Instanz von <code>Coordinate</code> mit übergebenem
	 * x- und y-Wert erstellt.
	 * 
	 * @param x
	 * @param y
	 */
	public Coordinate(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Vergleicht zwei <code>Coordinate</code>-Objekte miteinander. Die Objekte
	 * sind gleich, wenn deren x- und y-Werte gleich sind.
	 * 
	 * @param c
	 * @return boolean true wenn Werte der Objekte gleich sind, sonst false
	 */
	public boolean equals(Coordinate c) {
		return (this.x == c.getX() && this.y == c.getY());
	}

	/**
	 * Erhöht das Wertepaar um die übergebenen Integer-Werte. Es sind negative
	 * Werte möglich.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void add(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}

	/**
	 * Gibt einen String zurück, der bspw. auf der Konsole angezeigt werden
	 * kann.
	 */
	public String toString() {
		return "[" + this.x + " / " + this.y + "]";
	}

}
