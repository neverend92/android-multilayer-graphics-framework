/**
 * @(#)TileMap.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.bo;

import java.util.ArrayList;

import de.hdm.hettich.studienarbeit.utile.Coordinate;

/**
 * Eine <code>TileMap</code> ist ein Teil eines <code>Drawing</code>s und
 * repräsentiert eine einzelne Zoomstufe eines Gesamtplans. Die
 * <code>TileMap</code> enthält in zweidimensionaler Anordnung mehrere
 * <code>Tile</code>s. Zudem ist jeder <code>TileMap</code> ein spezieller
 * Skalierungsfaktor zugeordnet.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class TileMap extends Document {

	/**
	 * Zweidimensionaler Tile-Array, der die einzelnen Tile-Elemente hält, dabei
	 * ist die Anordnung wichtig: Es wird von einem "normalen" Koordinatensystem
	 * ausgegangen, das den Ursprung oben links hat. Die horizontale Achse
	 * stellt die x-Achse dar, die vertikale Achse folglich die y-Achse.
	 */
	private Tile[][] tiles;

	/**
	 * Skalierungsfaktor relativ zur im Drawing angegeben Referenzgröße.
	 * 
	 */
	private float scaleFactor;

	/**
	 * Konstruktur, der ein neues TileMap-Objekt anhand der übergebenen
	 * Dimension der TileMap (wie viele Tile - a x b) und dem Skalierungsfaktor
	 * erstellt.
	 * 
	 * @param dimension
	 * @param scaleFactor
	 */
	public TileMap(Coordinate dimension, float scaleFactor) {
		this.tiles = new Tile[dimension.getX()][dimension.getY()];
		this.scaleFactor = scaleFactor;
	}

	/**
	 * Konstruktor, der ein neues TileMap-Objekt anhand des übergebenes
	 * Tile-Array und dem Skalierungsfaktor erstellt.
	 * 
	 * @param tiles
	 * @param scaleFactor
	 */
	public TileMap(Tile[][] tiles, float scaleFactor) {
		this.tiles = tiles;
		this.scaleFactor = scaleFactor;
	}

	/**
	 * @return the tiles
	 */
	public Tile[][] getTiles() {
		return tiles;
	}

	/**
	 * @param tiles
	 *            the tiles to set
	 */
	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	/**
	 * @return the scaleFactor
	 */
	public float getScaleFactor() {
		return scaleFactor;
	}

	/**
	 * @param scaleFactor
	 *            the scaleFactor to set
	 */
	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	/**
	 * Diese Methode ermöglicht das einfach hinzufügen eines Tile zum
	 * Tile-Array. Dabei wird die nächste freie Position im Tile-Array gesucht
	 * und das übergebene Tile dann dort abgelegt. Die Füllung des Tile-Arrays
	 * läuft spaltenweise.
	 * 
	 * @param tile
	 */
	public void addTile(Tile tile) {
		// Ermittlen der nächsten freien Position.
		Coordinate pos = this.getNextFreePosition();
		/*
		 * Prüfen, ob es überhaupt eine freie Position gibt.
		 * 
		 * @see getNextFreePosition
		 */
		if (pos != null) {
			// An diesem freien Platz Tile ablegen.
			tiles[pos.getX()][pos.getY()] = tile;
		} else {
			throw new Error("Tile-Array ist bereits voll.");
		}
	}

	/**
	 * Ermittelt die nächste freie Position im Tile-Array.
	 * 
	 * @return Point nächste freie Position im Tile-Array
	 */
	public Coordinate getNextFreePosition() {
		// Prüfung, ob bereits ein Tile-Array initialisiert wurde.
		if (tiles != null) {
			/*
			 * Durchlaufen des Tile-Arrays, bis eine leere Stelle gefunden
			 * wurde.
			 */
			for (int x = 0; x < tiles.length; x++) {
				for (int y = 0; y < tiles[x].length; y++) {
					if (tiles[x][y] == null) {
						return new Coordinate(x, y);
					}
				}
			}
		} else {
			/*
			 * Wenn noch kein Tile-Array initialisiert wurde, kann auch kein
			 * Tile angefügt werden!
			 */
			throw new Error("Tile-Array wurde noch nicht initialisiert.");
		}
		/*
		 * Wenn es keine freie Stelle gibt wurde bis hier noch kein Point
		 * zurückgegeben, also wird hier null zurückgegeben.
		 */
		return null;
	}

	/**
	 * Gibt die Tile an einer bestimmten Position zurück.
	 * 
	 * @param x
	 * @param y
	 * @return Tile die an Position (x, y) befindliche Tile
	 */
	public Tile getTile(int x, int y) {
		return this.tiles[x][y];
	}

	/**
	 * Setzt eine Tile an einer bestimmten Positon.
	 * 
	 * @param x
	 * @param y
	 * @param tile
	 */
	public void setTile(int x, int y, Tile tile) {
		this.tiles[x][y] = tile;
	}

	/**
	 * Gibt die Tiles eines definierten Bereich im Plan zurück.
	 * 
	 * @param x
	 * @param y
	 * @param dx
	 * @param dy
	 * @return ArrayList<Tile> die Kacheln im angegebenen Bereich
	 */
	public ArrayList<Tile> getArea(int x, int y, int dx, int dy) {
		ArrayList<Tile> result = new ArrayList<Tile>();

		for (int i = 0; i < dx; i++) {
			for (int j = 0; j < dy; j++) {
				result.add(this.tiles[x + i][y + j]);
			}
		}

		return result;
	}

	/**
	 * Gibt die Größe der <code>TileMap</code> zurück, die sich aus der Anzahl
	 * der <code>Tile</code>s und der Größe des <code>Tile</code>s berechnet.
	 * 
	 * Dabei wird davon ausgegangen, dass alle <code>Tile</code>s einer
	 * </code>TileMap</code> gleich groß sind (dies ist bei der Initialisierung
	 * einer <code>TileMap</code> gegeben).
	 * 
	 * @return Coordinate die Größe der TileMap
	 */
	public Coordinate getSize() {
		/*
		 * Repräsentativ wird die erste Zeile und die erste Tile verwendet. Der
		 * x-Wert der Gesamtgröße ergibt sich durch Multiplikation der Anzahl
		 * der Spalten wird mit der Größe der Tile in x-Richtung, der y-Wert
		 * durch Multiplikation der Anzahl der Spalten mit der Größe der Tile in
		 * y-Richtung.
		 */
		return new Coordinate(tiles.length * tiles[0][0].getSize().getX(),
				tiles[0].length * tiles[0][0].getSize().getY());
	}

	/**
	 * Gibt die Kachel-Größe dieser Ebene zurück, da durch die Initialisierung
	 * gewährleistet ist, dass alle Kacheln gleich groß sind, kann repräsentativ
	 * die erste Kachel verwendet werden.
	 * 
	 * @return Coordinate die Größe der Tiles dieser TileMap
	 */
	public Coordinate getTileSize() {
		return tiles[0][0].getSize();
	}

	/**
	 * Gibt die Dimesionen des Plans zurück. Dabei wird einfach die Größe des
	 * <code>tiles</code>-Arrays geprüft.
	 * 
	 * @return Coordinate die Dimensionen (Anzahl der Tiles) dieser TileMap
	 */
	public Coordinate getDimension() {
		return new Coordinate(tiles.length, tiles[0].length);
	}

	/**
	 * Gibt einen zweidimensionalen <code>Tile</code>-Array zurück, der
	 * grundsätzlich dem {@link TileMap#tiles}-Array dieser Klasse entspricht,
	 * jedoch nur die bereits geladenen <code>Tile</code>s gesetzt sind, die
	 * noch nicht geladenen <code>Tile</code>s werden einfach <code>null</code>
	 * gesetzt.
	 * 
	 * @return Tile[][] die bereits geladenen Tiles
	 */
	public Tile[][] getLoadedTiles() {
		// Ergebnis ArrayList initialisieren.
		Tile[][] result = new Tile[tiles.length][tiles[0].length];

		/*
		 * Durchlaufen des kompletten zweidimensionalen Tile-Arrays, dabei wird
		 * bei jeder Tile geprüft, ob sie geladen ist. Sollte die Tile noch
		 * nicht geladen sein, dann wird sie aus dem Array entfernt.
		 */
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				// Prüfen, ob Kachel geladen ist.
				if (!tiles[i][j].isLoaded()) {
					// Wenn nicht, dann wird auch nichts in Array gesetzt (null)
					result[i][j] = null;
				} else {
					// Wenn geladen, dann wird diese Kachel gesetzt.
					result[i][j] = tiles[i][j];
				}
			}
		}

		// Ergebnis-Array wird zurückgegeben.
		return result;
	}

}
