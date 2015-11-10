/**
 * @(#)TileView.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.view;

import de.hdm.hettich.studienarbeit.bo.Tile;
import android.content.Context;
import android.widget.ImageView;

/**
 * Die Klasse <code>TileView</code> erbt von <code>ImageView</code> und
 * übernimmt die Darstellung eines <code>Tile</code>s.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class TileView extends ImageView {

	/**
	 * <code>Tile</code>, die in diesem <code>TileView</code> dargestellt wird.
	 */
	private Tile tile;

	/**
	 * Konstruktor, der verhanden sein muss, da <code>TileView</code> von
	 * <code>ImageView</code> erbt. Die Sichtbarkeit ist auf protected gesetzt,
	 * sodass ein Zugriff von außen nicht möglich ist und stattdessen der
	 * Konstruktor mit mehreren Parametern verwendet werden muss.
	 * 
	 * @param context
	 */
	protected TileView(Context context) {
		super(context);
	}

	/**
	 * Konstruktor, der im Applikationskontext einen neuen <code>TileView</code>
	 * erstellt und die übergebene <code>Tile</code> setzt.
	 * 
	 * @param context
	 * @param tile
	 */
	public TileView(Context context, Tile tile) {
		super(context);
		this.setTile(tile);
	}

	/**
	 * @return the tile
	 */
	public Tile getTile() {
		return tile;
	}

	/**
	 * @param tile
	 *            the tile to set
	 */
	public void setTile(Tile tile) {
		this.tile = tile;
	}

}
