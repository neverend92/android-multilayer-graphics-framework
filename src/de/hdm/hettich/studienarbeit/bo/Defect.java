/**
 * @(#)Defect.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.bo;

import de.hdm.hettich.studienarbeit.utile.Coordinate;

/**
 * Die Klasse <code>Defect</code> repräsentiert einen Fehler oder eine Notiz die
 * einem Drawing hinzugefügt werden kann. Ein <code>Defect</code> hat einen
 * bestimmten Typ (z.B. Video-Notiz, Audio-Kommentar, Text-Notiz, etc.) der
 * aktuell lediglich durch einen <code>String</code> gespeichert wird. Dem
 * <code>Defect</code> ist eine Position zugeordnet, die sich stehts auf die
 * initiale Zoomstufe (1) des Plans bezieht, somit lassen sich die Positionen in
 * allen anderen möglichen Zoomstufen durch den Skalierungsfaktor berechnen. Die
 * Größe eines <code>Defect</code>s bei der späteren Anzeige ist immer gleich
 * groß, das repräsentative Icon wird also immer in gleicher Größe mittig zur
 * Position angezeigt.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class Defect extends Document {

	/**
	 * Beschreibt die Art der Defect-Notiz, kann später sehr vielseitig sein.
	 * Z.B. Video, Audio, Text, etc.
	 */
	private String typeOfNote;

	/**
	 * Position des Defect im initalen Plan, das heißt diese Position muss bei
	 * einer unterschiedlichen Zoomstufen neu berechnet werden.
	 */
	private Coordinate position;

	/**
	 * Größe des angezeigten Icons für den <code>Defect</code>. Standardgemäß
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
