/**
 * @(#)Viewport.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.bo;

import de.hdm.hettich.studienarbeit.utile.Coordinate;

/**
 * Die Klasse <code>Viewport</code> repräsentiert den aktuell auf dem Bildschirm
 * sichtbaren Auschnitt. Dieser hat eine bestimmte Position im Gesamtplan und
 * eine bestimmte Größe (mit der Bildschirm-Größe gleichzusetzen ist).
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class Viewport extends Document {

	/**
	 * Aktuelle Position des <code>Viewport</code>s im Gesamtplan, diese
	 * Position ändert sich bei jedem Verschieben des <code>Drawing</code>s.
	 */
	private Coordinate position;

	/**
	 * Größe des <code>Viewport</code>s, entspricht momentan noch einfach der
	 * Display-Größe, auch wenn diese je nach Endgerät vom tatsächlich
	 * sichtbaren Bereich abweichen kann. Dies ist eine Notlösung, da die
	 * notwendigen Funktionen zu schlichten Trennung von Gesamt-Bildschirm-Größe
	 * und sichtbaren Bereichs erst ab Android-API 17 vorhanden sind und diese
	 * Applikation mit Android-API 14 entwickelt wurde.
	 */
	private Coordinate size;

	/**
	 * Konstruktor der ein neues <code>Viewport</code>-Objekt erstellt, dabei
	 * werden Position und Größe übergeben.
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
	 * Ermittelt die Mitte des Viewports in Pixel-Größe, dabei wird beachtet,
	 * dass sich die Mitte bei einem Plan, der kleiner als der Bildschirm ist,
	 * nur durch die Plangröße und nicht die Bildschirm-Größe berechenen lässt.
	 * 
	 * @param tileMapSize
	 * @return Coordinate die Mitte des Viewports in Pixeln.
	 */
	public Coordinate calculateCenter(Coordinate tileMapSize) {
		// Prüfen, ob Plan größer als das Display ist.
		if (tileMapSize.getX() >= this.size.getX()
				&& tileMapSize.getY() >= this.size.getY()) {
			/*
			 * Wenn ja, kann der Mittelpunkt anhand der Displaymaße berechnet
			 * werden.
			 */
			return new Coordinate(this.position.getX() + this.size.getX() / 2,
					this.position.getY() + this.size.getY() / 2);
		} else if (tileMapSize.getX() >= this.size.getX()) {
			/*
			 * Wenn der Plan nur in x-Richtung größer ist, muss die y-Richtung
			 * mit der Plan-Größe berechnet werden.
			 */
			return new Coordinate(this.position.getX() + this.size.getX() / 2,
					this.position.getY() + tileMapSize.getY() / 2);
		} else if (tileMapSize.getY() >= this.size.getY()) {
			/*
			 * Wenn der Plan nur in y-Richtung größer ist, muss die x-Richtung
			 * mit der Plan-Größe berechnet werden.
			 */
			return new Coordinate(
					this.position.getX() + tileMapSize.getX() / 2,
					this.position.getY() + this.size.getY() / 2);
		} else {
			/*
			 * Wenn der Plan weder in x- noch in y-Richtung größer wie das
			 * Display ist, berechnet sich der Mittelpunkt nach der Plan-Größe.
			 */
			return new Coordinate(
					this.position.getX() + tileMapSize.getX() / 2,
					this.position.getY() + tileMapSize.getY() / 2);
		}
	}

	/**
	 * Bewegt den Viewport um die angegebenen Integer-Werte. Es sind negative
	 * Werte möglich.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void translate(int dx, int dy, Coordinate tileMapSize) {
		this.position.add(dx, dy);
		this.checkPosition(tileMapSize);
	}

	/**
	 * Prüft anhand der übergebenen <code>TileMap</code>-Größe, ob die Position
	 * des <code>Viewport</code> realistisch ist und passt diese ggf. an. Immer
	 * wenn der Rand des <code>Drawing</code>s erreicht wird, scrollen die
	 * beiden <code>ScrollView</code>s weiter, das heißt die Position des
	 * Viewport würde sich ins negative, oder ins positive immer weiter erhöhen,
	 * durch die Grenzen der <code>TileMap</code>-Größe und null ist es möglich
	 * dies abzufangen.
	 * 
	 * @param tileMapSize
	 */
	public void checkPosition(Coordinate tileMapSize) {
		/*
		 * Prüfen, ob das Scrollen den Plan in x-Richtung verlassen würde (weil
		 * man zu weit nach rechts gescrollt hat, also zu großem x-Wert), sollte
		 * dies der Fall sein, wird einfach die größtmöglichste Position des
		 * Viewport im Plan berechnet.
		 */
		if (this.getPosition().getX() + this.getSize().getX() > tileMapSize
				.getX()) {
			/*
			 * Der y-Wert bleibt hier gleich, da nur der x-Wert geprüft wird.
			 * Der x-Wert berechnet sich aus der Größe der TileMap, sowie der
			 * Größe des Viewports.
			 */
			this.position = new Coordinate(tileMapSize.getX()
					- this.getSize().getX(), this.getPosition().getY());
		}

		/*
		 * Prüfen, ob das Scrollen den Plan in x-Richtung verlassen würde (weil
		 * man zu weit nach links gescrollt hat, also einem x-Wert kleiner
		 * null), sollte dies der Fall sein, wird einfach die größtmöglichste
		 * Position des Viewport im Plan berechnet.
		 */
		if (this.getPosition().getX() < 0) {
			/*
			 * Der y-Wert bleibt hier gleich, da nur der x-Wert geprüft wird.
			 * Der x-Wert wird auf null gesetzt.
			 */
			this.position = new Coordinate(0, this.getPosition().getY());
		}

		/*
		 * Prüfen, ob das Scrollen den Plan in y-Richtung verlassen würde,
		 * sollte dies der Fall sein, wird einfach die größtmöglichste Position
		 * des Viewport im Plan berechnet.
		 */
		if (this.getPosition().getY() + this.getSize().getY() > tileMapSize
				.getY()) {
			/*
			 * Der x-Wert bleibt hier gleich, da nur der y-Wert geprüft wird.
			 * Der x-Wert berechnet sich aus der Größe der TileMap, sowie der
			 * Größe des Viewports.
			 */
			this.position = new Coordinate(this.getPosition().getX(),
					tileMapSize.getY() - this.getSize().getY());

		}

		/*
		 * Prüfen, ob das Scrollen den Plan in y-Richtung verlassen würde (weil
		 * man zu weit nach links gescrollt hat, also einem y-Wert kleiner
		 * null), sollte dies der Fall sein, wird einfach die größtmöglichste
		 * Position des Viewport im Plan berechnet.
		 */
		if (this.getPosition().getY() < 0) {
			/*
			 * Der x-Wert bleibt hier gleich, da nur der y-Wert geprüft wird.
			 * Der y-Wert wird auf null gesetzt.
			 */
			this.position = new Coordinate(this.getPosition().getX(), 0);
		}
	}

}
