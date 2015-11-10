/**
 * @(#)TileGarbageService.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.service;

import de.hdm.hettich.studienarbeit.bo.Tile;
import de.hdm.hettich.studienarbeit.utile.Coordinate;
import de.hdm.hettich.studienarbeit.utile.Tools;
import de.hdm.hettich.studienarbeit.view.TileMapView;
import de.hdm.hettich.studienarbeit.view.TileView;

/**
 * Die Klasse <code>TileGarbageService</code> �bernimmt das ggf. notwendige
 * Entladen der Kacheln, sobald <code>GlobalSettings</code> wegen einem
 * Speicher�berlauf Alarm schl�gt.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class TileGarbageService {

	/**
	 * <code>GlobalSettings</code> beinhaltet die Informationen zur Auslastung
	 * des Arbeitsspeichers, diese werden zur Entscheidungsfindung, ob Kacheln
	 * entladen werden m�ssen oder nicht, gebraucht.
	 */
	private GlobalSettings settings;

	/**
	 * Konstruktor, der ein neuen <code>TileGarbageService</code> erstellt.
	 */
	public TileGarbageService() {
		this.settings = new GlobalSettings();
	}

	/**
	 * @return the settings
	 */
	public GlobalSettings getSettings() {
		return settings;
	}

	/**
	 * @param settings
	 *            the settings to set
	 */
	public void setSettings(GlobalSettings settings) {
		this.settings = settings;
	}

	/**
	 * Die Methode wird ausgef�hrt und pr�ft zun�chst, ob es notwendig ist
	 * bereits geladene Kacheln zu entladen (wenn der Speicher voll�uft). Ist
	 * die Speicherauslastung noch in Ordnung passiert nichts, anderenfalls wird
	 * das Entladen einer Kachel angesto�en. Dabei wird die, am weistesten von
	 * der zu ladenden Kachel entfernteste, Kachel identifieziert und wieder
	 * entladen, d.h. das <code>Bitmap</code> aus dem <code>TileView</code> und
	 * der <code>byte[]</code> aus dem <code>Tile</code> entfernt.
	 * 
	 * @param tileMapView
	 * @param tilePos
	 */
	public void cleanIfNecessary(TileMapView tileMapView, Coordinate tilePos) {
		// Pr�fen, ob der aktuelle Platz im Arbeitsspeicher niederig ist.
		if (settings.isLowMemory()) {
			/*
			 * Die bereits geladenen Tiles werden mit der Methode getLoadedTiles
			 * der Klasse TileMap ermittlet, dabei wird einfach der komplette
			 * Tile-Array zur�ckgegeben, die nicht geladenen Tiles werden null
			 * gesetzt.
			 */
			Tile[][] tiles = tileMapView.getTileMap().getLoadedTiles();

			double maxTileGap = 0;
			Coordinate mostDistantTilePos = null;

			// Komplettes Array durchlaufen
			for (int x = 0; x < tiles.length; x++) {
				for (int y = 0; y < tiles[x].length; y++) {
					/*
					 * Pr�fen, ob die Tile an der aktuellen Position nicht null
					 * ist.
					 */
					if (tiles[x][y] != null) {
						/*
						 * Berechnung des Abstands von zu ladender Tile und
						 * aktueller Tile.
						 */
						double tempTileGap = Tools
								.calculateVectorLength(new Coordinate(tilePos
										.getX() - x, tilePos.getY() - y));
						/*
						 * Pr�fen, ob aktuell maximaler Abstand �bertroffen
						 * wird.
						 */
						if (tempTileGap > maxTileGap) {
							/*
							 * Wenn aktuell maximaler Abstand �bertroffen wird,
							 * dann den neuen Abstand als maximalen setzen und
							 * die Position dieser Kachel zwischenspeichern.
							 */
							maxTileGap = tempTileGap;
							mostDistantTilePos = new Coordinate(x, y);
						}
					}
				}
			}

			/*
			 * Pr�fen, ob eine "am weitesten entfernteste" Kachel gefunden
			 * wurde. Wenn ja, wird der "Entlade"-Prozess angesto�en, das hei�t
			 * aus dem TileView wird das Bitmap entfernt und aus der Tile wird
			 * der byte[] entfernt.
			 */
			if (mostDistantTilePos != null) {
				// Log.e("Entladen", "Lade Kachel (" + tilePos.getX() + ","
				// + tilePos.getY() + ") Entlade Kachel ("
				// + mostDistantTilePos.getX() + ", "
				// + mostDistantTilePos.getY() + ")");
				/*
				 * Anhand der oben berechneten Koordinaten wird der zugeh�rige
				 * TileView ermittelt, sodass das geladene Bitmap dort entfernt
				 * werden kann und wieder das Platzhalter-Bild gesetzt wird.
				 */
				TileView tileView = (TileView) tileMapView.getChildAt(Tools
						.convertMatrixPos2Int(mostDistantTilePos, tileMapView
								.getTileMap().getDimension()));
				// TileView leeren.
				tileView.setImageBitmap(null);

				/*
				 * Anhand der oben berechneten Koordinaten wird der zugeh�rige
				 * Tile ermittelt, sodass der geladene byte[] dort entfernt
				 * werden kann.
				 */
				Tile tile = tiles[mostDistantTilePos.getX()][mostDistantTilePos
						.getY()];
				// Tile "vergessen", das hei�t byte[] entfernen.
				tile.forget();
			}
		}
	}

}
