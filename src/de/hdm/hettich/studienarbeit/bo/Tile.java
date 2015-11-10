/**
 * @(#)Tile.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.bo;

import java.net.URI;

import de.hdm.hettich.studienarbeit.utile.Coordinate;

/**
 * Die Klasse <code>Tile</code> repr�sentiert eine Kachel im Plan, sie wird
 * einer <code>TileMap</code> zugeordnet.
 * <p>
 * Die <code>Tile</code> hat eine bestimmte Gr��e, einen <code>byte[]</code> in
 * dem das zu ladende Bild hinterlegt ist, sowie eine <code>URI</code> zum auf
 * der SD-Karte befindlichen PNG.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class Tile extends Document {

	/**
	 * Gr��e der Tile.
	 */
	private Coordinate size;

	/**
	 * Byte-Array, der das Bild, das angezeigt werden soll beinhaltet. Ist
	 * <code>null</code>, wenn das Bild noch nicht gebraucht (sp�ter: aus der
	 * Datenbank geladen wurde).
	 */
	private byte[] image;

	/**
	 * URI zur zugeordneten PNG.
	 */
	private URI filename;

	/**
	 * Konstruktor, der ein neues Tile-Objekt erstellt, dabei wird die Gr��e des
	 * Tiles, sowie der Dateiname des zu ladenden Bilds �bergeben.
	 * 
	 * @param size
	 * @param filename
	 */
	public Tile(Coordinate size, URI filename) {
		this.size = size;
		this.filename = filename;
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
	 * @return the image
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}

	/**
	 * @return the filename
	 */
	public URI getFilename() {
		return filename;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(URI filename) {
		this.filename = filename;
	}

	/**
	 * @return the isLoaded
	 */
	public boolean isLoaded() {
		if (image == null) {
			return false;
		}
		return (image.length > 0);
	}

	/**
	 * "Vergisst" eine Tile, das hei�t das Byte-Array wird geleert und somit die
	 * Referenz zum Bild entfernt.
	 */
	public void forget() {
		this.image = null;
	}

	/**
	 * Gibt zur�ck, ob zwei <code>Tile</code>-Objekte gleich sind. Dabei wird
	 * der <code>byte[]</code>, sowie die Gr��e der Tile verglichen.
	 * 
	 * @param tile
	 * @return boolean boolean true wenn Werte der Objekte gleich sind, sonst false
	 */
	public boolean equals(Tile tile) {
		return (this.filename.equals(tile.getFilename())
				&& this.image == tile.getImage() && this.size.equals(tile
				.getSize()));
	}
}
