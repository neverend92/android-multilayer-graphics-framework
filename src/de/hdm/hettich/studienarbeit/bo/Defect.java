/**
 * @(#)Defect.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.bo;

import de.hdm.hettich.studienarbeit.utile.Coordinate;

/**
 * Die Klasse <code>Defect</code> repr�sentiert einen Fehler oder eine Notiz die
 * einem Drawing hinzugef�gt werden kann. Ein <code>Defect</code> hat einen
 * bestimmten Typ (z.B. Video-Notiz, Audio-Kommentar, Text-Notiz, etc.) der
 * aktuell lediglich durch einen <code>String</code> gespeichert wird. Dem
 * <code>Defect</code> ist eine Position zugeordnet, die sich stehts auf die
 * initiale Zoomstufe (1) des Plans bezieht, somit lassen sich die Positionen in
 * allen anderen m�glichen Zoomstufen durch den Skalierungsfaktor berechnen. Die
 * Gr��e eines <code>Defect</code>s bei der sp�teren Anzeige ist immer gleich
 * gro�, das repr�sentative Icon wird also immer in gleicher Gr��e mittig zur
 * Position angezeigt.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class Defect extends Document {

	/**
	 * Beschreibt die Art der Defect-Notiz, kann sp�ter sehr vielseitig sein.
	 * Z.B. Video, Audio, Text, etc.
	 */
	private String typeOfNote;

	/**
	 * Position des Defect im initalen Plan, das hei�t diese Position muss bei
	 * einer unterschiedlichen Zoomstufen neu berechnet werden.
	 */
	private Coordinate position;

	/**
	 * Gr��e des angezeigten Icons f�r den <code>Defect</code>. Standardgem��
	 * ist diese 50x50
	 */
	private Coordinate size;

	/**
	 * Konstruktor, der ein neues Defect-Objekt instanziiert.
	 * 
	 * @param typeOfNote
	 * @param position
	 * @param size
	 */
	public Defect(String typeOfNote, Coordinate position, Coordinate size) {
		this.typeOfNote = typeOfNote;
		this.position = position;
		this.size = size;
	}

	/**
	 * @return the typeOfNote
	 */
	public String getTypeOfNote() {
		return typeOfNote;
	}

	/**
	 * @param typeOfNote
	 *            the typeOfNote to set
	 */
	public void setTypeOfNote(String typeOfNote) {
		this.typeOfNote = typeOfNote;
	}

	/**
	 * @return the position
	 */
	public Coordinate getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Coordinate position) {
		this.position = position;
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

}
