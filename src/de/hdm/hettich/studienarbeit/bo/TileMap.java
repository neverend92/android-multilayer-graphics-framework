/**
 * @(#)TileMap.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.bo;

import java.util.ArrayList;

import de.hdm.hettich.studienarbeit.utile.Coordinate;

/**
 * Eine <code>TileMap</code> ist ein Teil eines <code>Drawing</code>s und
 * repr�sentiert eine einzelne Zoomstufe eines Gesamtplans. Die
 * <code>TileMap</code> enth�lt in zweidimensionaler Anordnung mehrere
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
	 * Zweidimensionaler Tile-Array, der die einzelnen Tile-Elemente h�lt, dabei
	 * ist die Anordnung wichtig: Es wird von einem "normalen" Koordinatensystem
	 * ausgegangen, das den Ursprung oben links hat. Die horizontale Achse
	 * stellt die x-Achse dar, die vertikale Achse folglich die y-Achse.
	 */
	private Tile[][] tiles;

	/**
	 * Skalierungsfaktor relativ zur im Drawing angegeben Referenzgr��e.
	 * 
	 */
	private float scaleFactor;

	/**
	 * Konstruktur, der ein neues TileMap-Objekt anhand der �bergebenen
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
	 * Konstruktor, der ein neues TileMap-Objekt anhand des �bergebenes
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
	 * Diese Methode erm�glicht das einfach hinzuf�gen eines Tile zum
	 * Tile-Array. Dabei wird die n�chste freie Position im Tile-Array gesucht
	 * und das �bergebene Tile dann dort abgelegt. Die F�llung des Tile-Arrays
	 * l�uft spaltenweise.
	 * 
	 * @param tile
	 */
	public void addTile(Tile tile) {
		// Ermittlen der n�chsten freien Position.
		Coordinate pos = this.getNextFreePosition();
		/*
		 * Pr�fen, ob es �berhaupt eine freie Position gibt.
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
	 * Ermittelt die n�chste freie Position im Tile-Array.
	 * 
	 * @return Point n�chste freie Position im Tile-Array
	 */
	public Coordinate getNextFreePosition() {
		// Pr�fung, ob bereits ein Tile-Array initialisiert wurde.
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
			 * Tile angef�gt werden!
			 */
			throw new Error("Tile-Array wurde noch nicht initialisiert.");
		}
		/*
		 * Wenn es keine freie Stelle gibt wurde bis hier noch kein Point
		 * zur�ckgegeben, also wird hier null zur�ckgegeben.
		 */
		return null;
	}

	/**
	 * Gibt die Tile an einer bestimmten Position zur�ck.
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
	 * Gibt die Tiles eines definierten Bereich im Plan zur�ck.
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
	 * Gibt die Gr��e der <code>TileMap</code> zur�ck, die sich aus der Anzahl
	 * der <code>Tile</code>s und der Gr��e des <code>Tile</code>s berechnet.
	 * 
	 * Dabei wird davon ausgegangen, dass alle <code>Tile</code>s einer
	 * </code>TileMap</code> gleich gro� sind (dies ist bei der Initialisierung
	 * einer <code>TileMap</code> gegeben).
	 * 
	 * @return Coordinate die Gr��e der TileMap
	 */
	public Coordinate getSize() {
		/*
		 * Repr�sentativ wird die erste Zeile und die erste Tile verwendet. Der
		 * x-Wert der Gesamtgr��e ergibt sich durch Multiplikation der Anzahl
		 * der Spalten wird mit der Gr��e der Tile in x-Richtung, der y-Wert
		 * durch Multiplikation der Anzahl der Spalten mit der Gr��e der Tile in
		 * y-Richtung.
		 */
		return new Coordinate(tiles.length * tiles[0][0].getSize().getX(),
				tiles[0].length * tiles[0][0].getSize().getY());
	}

	/**
	 * Gibt die Kachel-Gr��e dieser Ebene zur�ck, da durch die Initialisierung
	 * gew�hrleistet ist, dass alle Kacheln gleich gro� sind, kann repr�sentativ
	 * die erste Kachel verwendet werden.
	 * 
	 * @return Coordinate die Gr��e der Tiles dieser TileMap
	 */
	public Coordinate getTileSize() {
		return tiles[0][0].getSize();
	}

	/**
	 * Gibt die Dimesionen des Plans zur�ck. Dabei wird einfach die Gr��e des
	 * <code>tiles</code>-Arrays gepr�ft.
	 * 
	 * @return Coordinate die Dimensionen (Anzahl der Tiles) dieser TileMap
	 */
	public Coordinate getDimension() {
		return new Coordinate(tiles.length, tiles[0].length);
	}

	/**
	 * Gibt einen zweidimensionalen <code>Tile</code>-Array zur�ck, der
	 * grunds�tzlich dem {@link TileMap#tiles}-Array dieser Klasse entspricht,
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
		 * bei jeder Tile gepr�ft, ob sie geladen ist. Sollte die Tile noch
		 * nicht geladen sein, dann wird sie aus dem Array entfernt.
		 */
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				// Pr�fen, ob Kachel geladen ist.
				if (!tiles[i][j].isLoaded()) {
					// Wenn nicht, dann wird auch nichts in Array gesetzt (null)
					result[i][j] = null;
				} else {
					// Wenn geladen, dann wird diese Kachel gesetzt.
					result[i][j] = tiles[i][j];
				}
			}
		}

		// Ergebnis-Array wird zur�ckgegeben.
		return result;
	}

}
