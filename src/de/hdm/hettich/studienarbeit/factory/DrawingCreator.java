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
 * <code>DemoDrawingFrame</code> ein <code>Drawing</code>, mit dem später
 * gearbeitet wird. Die Informationen die momentan aus dem
 * <code>DemoDrawingFrame</code> stammen könnten später bspw. auch in einer
 * XML-Datei stehen. Somit soll die Anzeige mehrere Pläne etwas einfacher
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
	 * das <code>DemoDrawingFrame</code> übergebenen Informationen.
	 * 
	 * @return Drawing das erstellte Drawing
	 */
	public static Drawing createDrawing(DemoDrawingFrame drawingFrame) {

		// Ergebnis Drawing definieren, für den späteren Zugriff.
		Drawing drawing;
		/*
		 * Prüfen, ob die Anzahl der Zoom-Ebenen, der ArrayList-Größe der
		 * Kachel-Größe und der ArrayList-Größe der Kachel-Anzahl entspricht.
		 */
		if (drawingFrame.countZoomLevels == drawingFrame.tileSizes.size()
				&& drawingFrame.countZoomLevels == drawingFrame.levelDimensions
						.size()) {

			ArrayList<URI[][]> pngFiles = new ArrayList<URI[][]>();

			/*
			 * Prüfen, welche Option gesetzt wurde, um die PNG-Kacheln zu
			 * übergeben. Es sind drei Optionen vorhanden: Ein Bild für das
			 * gesamte Drawing, ein Bild pro TileMap, ein Bild pro Tile
			 * (Produktiveinsatz)
			 */
			if (drawingFrame.pngFileSingle != null
					&& drawingFrame.pngFileEachTileMap == null
					&& drawingFrame.pngFileEachTile == null) {
				// Ein Bild für das gesamte Drawing.

				// Alle Zoom-Stufen durchlaufen.
				for (int i = 0; i < drawingFrame.countZoomLevels; i++) {
					// Dimensionen der aktuellen Zoom-Stufe abrufen.
					Coordinate dimension = drawingFrame.levelDimensions.get(i);
					/*
					 * Initialisierung des File-Arrays für die aktuelle
					 * Zoom-Ebene.
					 */
					URI[][] files = new URI[dimension.getX()][dimension.getY()];
					/*
					 * Durchlaufen aller Dimensions-Positionen (in x- und
					 * y-Richtung)
					 */
					for (int x = 0; x < dimension.getX(); x++) {
						for (int y = 0; y < dimension.getY(); y++) {
							// Setzen des global gültigen Bildes.
							files[x][y] = drawingFrame.pngFileSingle;
						}
					}
					/*
					 * Hinzufügen des Array der Zoom-Ebene zur allgemeinen
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
					 * Initialisierung des File-Arrays für die aktuelle
					 * Zoom-Ebene.
					 */
					URI[][] files = new URI[dimension.getX()][dimension.getY()];
					/*
					 * Durchlaufen aller Dimensions-Positionen (in x- und
					 * y-Richtung)
					 */
					for (int x = 0; x < dimension.getX(); x++) {
						for (int y = 0; y < dimension.getY(); y++) {
							// Setzen des pro Ebene gültigen Bildes.
							files[x][y] = drawingFrame.pngFileEachTileMap
									.get(i);
						}
					}
					/*
					 * Hinzufügen des Array der Zoom-Ebene zur allgemeinen
					 * ArrayList.
					 */
					pngFiles.add(files);
				}

			} else if (drawingFrame.pngFileSingle == null
					&& drawingFrame.pngFileEachTileMap == null
					&& drawingFrame.pngFileEachTile != null) {
				// Ein Bild pro Tile.

				// Einfach die bereits vorhandene ArrayList übergeben.
				pngFiles = drawingFrame.pngFileEachTile;
			} else {
				Log.e("DrawingCreator",
						"PNG-Tiles wurden nicht richtig definiert.");
				return null;
			}

			/*
			 * Größe der Ebene mit Zoom-Faktor eins berechnen, damit diese als
			 * Initial-Ebene hinzugefügt werden kann. In diesem Fall hat diese
			 * Ebene den Index 1.
			 */
			Coordinate dimension = drawingFrame.levelDimensions.get(1);
			Coordinate tileSize = drawingFrame.tileSizes.get(1);
			// Berechnung der initialen TileMap-Referenz-Größe
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
				 * diese dem <code>Drawing</code> hinzufügen
				 */
				TileMap tileMap = createTileMap(
						drawingFrame.levelDimensions.get(i),
						drawingFrame.tileSizes.get(i),
						drawingFrame.scaleFactors.get(i), pngFiles.get(i));
				drawing.addTileMap(tileMap);
				/*
				 * Prüfen, ob dies die initiale TileMap ist, wenn ja wird diese
				 * im Drawing gesetzt.
				 */
				if (tileMap.getSize().equals(initialSize)) {
					drawing.setAcitveTileMap(tileMap);
				}
			}
		} else {
			Log.e("DrawingCreator",
					"Die Anzahl der Zoom-Stufen stimmt nicht mit der "
							+ "Größe der übergebenen Array-Listen überein.");
			return null;
		}

		return drawing;
	}

	/**
	 * Diese Methode erstellt ein <code>TileMap</code>-Objekt, das dann Teil des
	 * <code>Drawing</code>s ist, anhand der übergebenen Parameter.
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
		 * Durchlaufen der übergebenen Plangröße (dimension) in x- und
		 * y-Richtung. An jeder Position wird ein neues Tile hinzugefügt.
		 */
		for (int x = 0; x < dimension.getX(); x++) {
			for (int y = 0; y < dimension.getY(); y++) {
				// Hinzufügen einer neuen Tile an der aktuellen Position.
				tileMap.addTile(new Tile(size, pngFile[x][y]));
			}
		}

		return tileMap;
	}

}