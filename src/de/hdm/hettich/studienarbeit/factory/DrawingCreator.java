/**
 * @(#)DrawingCreator.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.factory;

import java.net.URI;
import java.util.ArrayList;

import android.util.Log;

import de.hdm.hettich.studienarbeit.DemoDrawingFrame;
import de.hdm.hettich.studienarbeit.bo.Drawing;
import de.hdm.hettich.studienarbeit.bo.Tile;
import de.hdm.hettich.studienarbeit.bo.TileMap;
import de.hdm.hettich.studienarbeit.utile.Coordinate;

/**
 * Die Klasse <code>DrawingCreator</code> erstellt aus einem
 * <code>DemoDrawingFrame</code> ein <code>Drawing</code>, mit dem sp�ter
 * gearbeitet wird. Die Informationen die momentan aus dem
 * <code>DemoDrawingFrame</code> stammen k�nnten sp�ter bspw. auch in einer
 * XML-Datei stehen. Somit soll die Anzeige mehrere Pl�ne etwas einfacher
 * gemacht werden.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class DrawingCreator {

	/**
	 * Die Methode erstellt das <code>Drawing</code>-Objekt anhand der, durch
	 * das <code>DemoDrawingFrame</code> �bergebenen Informationen.
	 * 
	 * @return Drawing das erstellte Drawing
	 */
	public static Drawing createDrawing(DemoDrawingFrame drawingFrame) {

		// Ergebnis Drawing definieren, f�r den sp�teren Zugriff.
		Drawing drawing;
		/*
		 * Pr�fen, ob die Anzahl der Zoom-Ebenen, der ArrayList-Gr��e der
		 * Kachel-Gr��e und der ArrayList-Gr��e der Kachel-Anzahl entspricht.
		 */
		if (drawingFrame.countZoomLevels == drawingFrame.tileSizes.size()
				&& drawingFrame.countZoomLevels == drawingFrame.levelDimensions
						.size()) {

			ArrayList<URI[][]> pngFiles = new ArrayList<URI[][]>();

			/*
			 * Pr�fen, welche Option gesetzt wurde, um die PNG-Kacheln zu
			 * �bergeben. Es sind drei Optionen vorhanden: Ein Bild f�r das
			 * gesamte Drawing, ein Bild pro TileMap, ein Bild pro Tile
			 * (Produktiveinsatz)
			 */
			if (drawingFrame.pngFileSingle != null
					&& drawingFrame.pngFileEachTileMap == null
					&& drawingFrame.pngFileEachTile == null) {
				// Ein Bild f�r das gesamte Drawing.

				// Alle Zoom-Stufen durchlaufen.
				for (int i = 0; i < drawingFrame.countZoomLevels; i++) {
					// Dimensionen der aktuellen Zoom-Stufe abrufen.
					Coordinate dimension = drawingFrame.levelDimensions.get(i);
					/*
					 * Initialisierung des File-Arrays f�r die aktuelle
					 * Zoom-Ebene.
					 */
					URI[][] files = new URI[dimension.getX()][dimension.getY()];
					/*
					 * Durchlaufen aller Dimensions-Positionen (in x- und
					 * y-Richtung)
					 */
					for (int x = 0; x < dimension.getX(); x++) {
						for (int y = 0; y < dimension.getY(); y++) {
							// Setzen des global g�ltigen Bildes.
							files[x][y] = drawingFrame.pngFileSingle;
						}
					}
					/*
					 * Hinzuf�gen des Array der Zoom-Ebene zur allgemeinen
					 * ArrayList.
					 */
					pngFiles.add(files);
				}
			} else if (drawingFrame.pngFileSingle == null
					&& drawingFrame.pngFileEachTileMap != null
					&& drawingFrame.pngFileEachTile == null) {
				// Ein Bild pro TileMap.

				// Alle Dimensionen durchlaufen.
				for (int i = 0; i < drawingFrame.countZoomLevels; i++) {
					// Dimensionen der aktuellen Zoom-Stufe abrufen.
					Coordinate dimension = drawingFrame.levelDimensions.get(i);
					/*
					 * Initialisierung des File-Arrays f�r die aktuelle
					 * Zoom-Ebene.
					 */
					URI[][] files = new URI[dimension.getX()][dimension.getY()];
					/*
					 * Durchlaufen aller Dimensions-Positionen (in x- und
					 * y-Richtung)
					 */
					for (int x = 0; x < dimension.getX(); x++) {
						for (int y = 0; y < dimension.getY(); y++) {
							// Setzen des pro Ebene g�ltigen Bildes.
							files[x][y] = drawingFrame.pngFileEachTileMap
									.get(i);
						}
					}
					/*
					 * Hinzuf�gen des Array der Zoom-Ebene zur allgemeinen
					 * ArrayList.
					 */
					pngFiles.add(files);
				}

			} else if (drawingFrame.pngFileSingle == null
					&& drawingFrame.pngFileEachTileMap == null
					&& drawingFrame.pngFileEachTile != null) {
				// Ein Bild pro Tile.

				// Einfach die bereits vorhandene ArrayList �bergeben.
				pngFiles = drawingFrame.pngFileEachTile;
			} else {
				Log.e("DrawingCreator",
						"PNG-Tiles wurden nicht richtig definiert.");
				return null;
			}

			/*
			 * Gr��e der Ebene mit Zoom-Faktor eins berechnen, damit diese als
			 * Initial-Ebene hinzugef�gt werden kann. In diesem Fall hat diese
			 * Ebene den Index 1.
			 */
			Coordinate dimension = drawingFrame.levelDimensions.get(1);
			Coordinate tileSize = drawingFrame.tileSizes.get(1);
			// Berechnung der initialen TileMap-Referenz-Gr��e
			Coordinate initialSize = new Coordinate(dimension.getX()
					* tileSize.getX(), dimension.getY() * tileSize.getY());

			// Ergebnis-Drawing initialisieren.
			drawing = new Drawing(initialSize);

			// Setzen der oben initialiserten Defect-ArrayList.
			drawing.setDefects(drawingFrame.defects);

			/*
			 * Nochmals alle Zoom-Ebenen durchlaufen, um nun pro Zoom-Ebene eine
			 * TileMap zu erstellen.
			 */
			for (int i = 0; i < drawingFrame.countZoomLevels; i++) {
				/*
				 * Eine <code>TileMap</code> erstellen @see #createTileMap() und
				 * diese dem <code>Drawing</code> hinzuf�gen
				 */
				TileMap tileMap = createTileMap(
						drawingFrame.levelDimensions.get(i),
						drawingFrame.tileSizes.get(i),
						drawingFrame.scaleFactors.get(i), pngFiles.get(i));
				drawing.addTileMap(tileMap);
				/*
				 * Pr�fen, ob dies die initiale TileMap ist, wenn ja wird diese
				 * im Drawing gesetzt.
				 */
				if (tileMap.getSize().equals(initialSize)) {
					drawing.setAcitveTileMap(tileMap);
				}
			}
		} else {
			Log.e("DrawingCreator",
					"Die Anzahl der Zoom-Stufen stimmt nicht mit der "
							+ "Gr��e der �bergebenen Array-Listen �berein.");
			return null;
		}

		return drawing;
	}

	/**
	 * Diese Methode erstellt ein <code>TileMap</code>-Objekt, das dann Teil des
	 * <code>Drawing</code>s ist, anhand der �bergebenen Parameter.
	 * 
	 * @param dimension
	 * @param size
	 * @param scaleFactor
	 * @param pngFile
	 * @return TileMap die erstellte TileMap
	 */
	private static TileMap createTileMap(Coordinate dimension, Coordinate size,
			float scaleFactor, URI[][] pngFile) {
		// Erstellen des TileMap-Objekts
		TileMap tileMap = new TileMap(dimension, scaleFactor);

		/*
		 * Durchlaufen der �bergebenen Plangr��e (dimension) in x- und
		 * y-Richtung. An jeder Position wird ein neues Tile hinzugef�gt.
		 */
		for (int x = 0; x < dimension.getX(); x++) {
			for (int y = 0; y < dimension.getY(); y++) {
				// Hinzuf�gen einer neuen Tile an der aktuellen Position.
				tileMap.addTile(new Tile(size, pngFile[x][y]));
			}
		}

		return tileMap;
	}

}