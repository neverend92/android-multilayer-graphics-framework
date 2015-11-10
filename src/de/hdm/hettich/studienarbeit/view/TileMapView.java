/**
 * @(#)TileMapView.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.view;

import java.util.ArrayList;

import de.hdm.hettich.studienarbeit.bo.TileMap;
import android.content.Context;
import android.widget.RelativeLayout;

/**
 * Die Klasse <code>TileMapView</code> erbt von <code>RelativeLayout</code> und
 * übernimmt die Darstellung einer <code>TileMap</code>.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class TileMapView extends RelativeLayout {

	/**
	 * <code>TileMap</code>, die in diesem <code>TileMapView</code> dargestellt
	 * wird.
	 */
	private TileMap tileMap;

	/**
	 * Konstruktor, der verhanden sein muss, da <code>TileMapView</code> von
	 * <code>RelativeLayout</code> erbt. Die Sichtbarkeit ist auf protected
	 * gesetzt, sodass ein Zugriff von außen nicht möglich ist und stattdessen
	 * der Konstruktor mit mehreren Parametern verwendet werden muss.
	 * 
	 * @param context
	 */
	protected TileMapView(Context context) {
		super(context);
	}

	/**
	 * Konstruktor, der einen <code>TileMapView</code> erstellt und die
	 * anzuzeigende <code>TileMap</code> setzt.
	 * 
	 * @param context
	 * @param tileMap
	 */
	public TileMapView(Context context, TileMap tileMap) {
		super(context);
		this.tileMap = tileMap;
	}

	/**
	 * @return the tileMap
	 */
	public TileMap getTileMap() {
		return tileMap;
	}

	/**
	 * @param tileMap
	 *            the tileMap to set
	 */
	public void setTileMap(TileMap tileMap) {
		this.tileMap = tileMap;
	}

	/**
	 * Gibt alle <code>TileView</code>s des <code>TileMapView</code>s zurück,
	 * dazu werden einfach alle Childs des <code>TileMapView</code>s abgerufen.
	 * 
	 * @return ArrayList<TileView> alle TileViews
	 */
	public ArrayList<TileView> getTileViews() {
		// Ergebnis ArrayList initialisieren
		ArrayList<TileView> result = new ArrayList<TileView>();

		// Durchlaufen aller Childs.
		for (int i = 0; i < this.getChildCount(); i++) {
			/*
			 * Hinzufügen des aktuellen Childs zur Ergebis-ArrayList und die
			 * Konvertierung in einen TileView
			 */
			result.add((TileView) this.getChildAt(i));
		}

		return result;
	}

}
