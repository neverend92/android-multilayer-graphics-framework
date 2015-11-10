/**
 * @(#)Viewport.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.bo;

import de.hdm.hettich.studienarbeit.utile.Coordinate;

/**
 * Die Klasse <code>Viewport</code> repr�sentiert den aktuell auf dem Bildschirm
 * sichtbaren Auschnitt. Dieser hat eine bestimmte Position im Gesamtplan und
 * eine bestimmte Gr��e (mit der Bildschirm-Gr��e gleichzusetzen ist).
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class Viewport extends Document {

	/**
	 * Aktuelle Position des <code>Viewport</code>s im Gesamtplan, diese
	 * Position �ndert sich bei jedem Verschieben des <code>Drawing</code>s.
	 */
	private Coordinate position;

	/**
	 * Gr��e des <code>Viewport</code>s, entspricht momentan noch einfach der
	 * Display-Gr��e, auch wenn diese je nach Endger�t vom tats�chlich
	 * sichtbaren Bereich abweichen kann. Dies ist eine Notl�sung, da die
	 * notwendigen Funktionen zu schlichten Trennung von Gesamt-Bildschirm-Gr��e
	 * und sichtbaren Bereichs erst ab Android-API 17 vorhanden sind und diese
	 * Applikation mit Android-API 14 entwickelt wurde.
	 */
	private Coordinate size;

	/**
	 * Konstruktor der ein neues <code>Viewport</code>-Objekt erstellt, dabei
	 * werden Position und Gr��e �bergeben.
	 * 
	 * @param position
	 * @param size
	 */
	public Viewport(Coordinate position, Coordinate size) {
		this.position = position;
		this.size = size;
	}

	/**
	 * @return the position
	 */
	public Coordinate getPosition() {
		return position;
	}

	/**
	 * 
	 * @param position
	 *            the position to set
	 * @param tileMapSize
	 */
	public void setPosition(Coordinate position, Coordinate tileMapSize) {
		this.position = position;
		this.checkPosition(tileMapSize);
	}

	/**
	 * @return the size
	 */
	public Coordinate getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(Coordinate size) {
		this.size = size;
	}

	/**
	 * Ermittelt die Mitte des Viewports in Pixel-Gr��e, dabei wird beachtet,
	 * dass sich die Mitte bei einem Plan, der kleiner als der Bildschirm ist,
	 * nur durch die Plangr��e und nicht die Bildschirm-Gr��e berechenen l�sst.
	 * 
	 * @param tileMapSize
	 * @return Coordinate die Mitte des Viewports in Pixeln.
	 */
	public Coordinate calculateCenter(Coordinate tileMapSize) {
		// Pr�fen, ob Plan gr��er als das Display ist.
		if (tileMapSize.getX() >= this.size.getX()
				&& tileMapSize.getY() >= this.size.getY()) {
			/*
			 * Wenn ja, kann der Mittelpunkt anhand der Displayma�e berechnet
			 * werden.
			 */
			return new Coordinate(this.position.getX() + this.size.getX() / 2,
					this.position.getY() + this.size.getY() / 2);
		} else if (tileMapSize.getX() >= this.size.getX()) {
			/*
			 * Wenn der Plan nur in x-Richtung gr��er ist, muss die y-Richtung
			 * mit der Plan-Gr��e berechnet werden.
			 */
			return new Coordinate(this.position.getX() + this.size.getX() / 2,
					this.position.getY() + tileMapSize.getY() / 2);
		} else if (tileMapSize.getY() >= this.size.getY()) {
			/*
			 * Wenn der Plan nur in y-Richtung gr��er ist, muss die x-Richtung
			 * mit der Plan-Gr��e berechnet werden.
			 */
			return new Coordinate(
					this.position.getX() + tileMapSize.getX() / 2,
					this.position.getY() + this.size.getY() / 2);
		} else {
			/*
			 * Wenn der Plan weder in x- noch in y-Richtung gr��er wie das
			 * Display ist, berechnet sich der Mittelpunkt nach der Plan-Gr��e.
			 */
			return new Coordinate(
					this.position.getX() + tileMapSize.getX() / 2,
					this.position.getY() + tileMapSize.getY() / 2);
		}
	}

	/**
	 * Bewegt den Viewport um die angegebenen Integer-Werte. Es sind negative
	 * Werte m�glich.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void translate(int dx, int dy, Coordinate tileMapSize) {
		this.position.add(dx, dy);
		this.checkPosition(tileMapSize);
	}

	/**
	 * Pr�ft anhand der �bergebenen <code>TileMap</code>-Gr��e, ob die Position
	 * des <code>Viewport</code> realistisch ist und passt diese ggf. an. Immer
	 * wenn der Rand des <code>Drawing</code>s erreicht wird, scrollen die
	 * beiden <code>ScrollView</code>s weiter, das hei�t die Position des
	 * Viewport w�rde sich ins negative, oder ins positive immer weiter erh�hen,
	 * durch die Grenzen der <code>TileMap</code>-Gr��e und null ist es m�glich
	 * dies abzufangen.
	 * 
	 * @param tileMapSize
	 */
	public void checkPosition(Coordinate tileMapSize) {
		/*
		 * Pr�fen, ob das Scrollen den Plan in x-Richtung verlassen w�rde (weil
		 * man zu weit nach rechts gescrollt hat, also zu gro�em x-Wert), sollte
		 * dies der Fall sein, wird einfach die gr��tm�glichste Position des
		 * Viewport im Plan berechnet.
		 */
		if (this.getPosition().getX() + this.getSize().getX() > tileMapSize
				.getX()) {
			/*
			 * Der y-Wert bleibt hier gleich, da nur der x-Wert gepr�ft wird.
			 * Der x-Wert berechnet sich aus der Gr��e der TileMap, sowie der
			 * Gr��e des Viewports.
			 */
			this.position = new Coordinate(tileMapSize.getX()
					- this.getSize().getX(), this.getPosition().getY());
		}

		/*
		 * Pr�fen, ob das Scrollen den Plan in x-Richtung verlassen w�rde (weil
		 * man zu weit nach links gescrollt hat, also einem x-Wert kleiner
		 * null), sollte dies der Fall sein, wird einfach die gr��tm�glichste
		 * Position des Viewport im Plan berechnet.
		 */
		if (this.getPosition().getX() < 0) {
			/*
			 * Der y-Wert bleibt hier gleich, da nur der x-Wert gepr�ft wird.
			 * Der x-Wert wird auf null gesetzt.
			 */
			this.position = new Coordinate(0, this.getPosition().getY());
		}

		/*
		 * Pr�fen, ob das Scrollen den Plan in y-Richtung verlassen w�rde,
		 * sollte dies der Fall sein, wird einfach die gr��tm�glichste Position
		 * des Viewport im Plan berechnet.
		 */
		if (this.getPosition().getY() + this.getSize().getY() > tileMapSize
				.getY()) {
			/*
			 * Der x-Wert bleibt hier gleich, da nur der y-Wert gepr�ft wird.
			 * Der x-Wert berechnet sich aus der Gr��e der TileMap, sowie der
			 * Gr��e des Viewports.
			 */
			this.position = new Coordinate(this.getPosition().getX(),
					tileMapSize.getY() - this.getSize().getY());

		}

		/*
		 * Pr�fen, ob das Scrollen den Plan in y-Richtung verlassen w�rde (weil
		 * man zu weit nach links gescrollt hat, also einem y-Wert kleiner
		 * null), sollte dies der Fall sein, wird einfach die gr��tm�glichste
		 * Position des Viewport im Plan berechnet.
		 */
		if (this.getPosition().getY() < 0) {
			/*
			 * Der x-Wert bleibt hier gleich, da nur der y-Wert gepr�ft wird.
			 * Der y-Wert wird auf null gesetzt.
			 */
			this.position = new Coordinate(this.getPosition().getX(), 0);
		}
	}

}
