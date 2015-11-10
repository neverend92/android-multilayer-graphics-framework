/**
 * @(#)TileMap2ViewFactory.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.factory;

import android.content.Context;
import de.hdm.hettich.studienarbeit.bo.TileMap;
import de.hdm.hettich.studienarbeit.utile.Coordinate;
import de.hdm.hettich.studienarbeit.view.TileMapView;

/**
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class TileMap2ViewFactory {

	/**
	 * Context der Applikation.
	 */
	private Context context;

	/**
	 * Konstruktor, der eine neue <code>TileMap2ViewFactory</code> erzeugt.
	 * 
	 * @param context
	 */
	public TileMap2ViewFactory(Context context) {
		this.setContext(context);
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * Diese Methode erstellt ein neues <code>TileMapView</code>-Objekt, anhand
	 * der übergebenen <code>TileMap</code>, dabei wird die Erstellung der
	 * einzelnen <code>TileView</code>s angestoßen.
	 * 
	 * @param tileMap
	 * @return TileMapView der erstellte TileMapView
	 */
	public TileMapView createCalculatedTileMapView(TileMap tileMap) {
		// Erstellen eines neuen TileMapViews.
		TileMapView view = new TileMapView(getContext(), tileMap);

		// Erstellen einer neuen Tile2ViewFactory.
		Tile2ViewFactory viewFactory = new Tile2ViewFactory(getContext());

		// Durchlaufen aller vorhandenen Tiles in x- und y-Richtung.
		for (int x = 0; x < tileMap.getTiles().length; x++) {
			for (int y = 0; y < tileMap.getTiles()[x].length; y++) {
				/*
				 * Hinzufügen, des berechneten TileViews, der bereits die
				 * notwendigen Margins enthält, sodass die Kacheln nebeneinander
				 * angezeigt werden.
				 */
				view.addView(viewFactory.createCalculatedView(
						tileMap.getTiles()[x][y], new Coordinate(x, y)));
			}
		}

		// Zurückgeben des berechneten TileMapViews.
		return view;
	}

}
