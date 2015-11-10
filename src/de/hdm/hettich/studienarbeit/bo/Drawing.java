/**
 * @(#)Drawing.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.bo;

import java.util.ArrayList;

import de.hdm.hettich.studienarbeit.utile.Coordinate;

/**
 * Ein <code>Drawing</code> repr�sentiert eine Gesamtplan, es enth�lt mehrere
 * <code>TileMap</code>s, die die einzelnen Zoomstufen des Plans darstellen.
 * Jedem <code>Drawing</code> ist eine aktive <code>TileMap</code> zugeordnet,
 * welche im Moment angezeigt wird. Zudem wird eine Grundgr��e des Plans
 * gespeichert, anhand der sich die Skalierungsfaktoren ableiten.
 * 
 * @author Stefan Hettich
 */
public class Drawing extends Document {

	/**
	 * Jedes Drawing-Objekt enth�lt beliebig viele TileMaps, diese TileMaps
	 * stellen die einzelnen Zoomebenen dar. TileMaps beinhalten die einzelnen
	 * Tiles, die sp�ter angezeigt werden. Die TileMaps sind nach
	 * Detaillierungsgrad sortiert, das hei�t die allgemeinste Stufe steht an
	 * Index 0.
	 */
	private ArrayList<TileMap> tileMaps = new ArrayList<TileMap>();

	/**
	 * Die TileMap die aktuell angezeigt wird ist hier abgelegt.
	 */
	private TileMap acitveTileMap;

	/**
	 * Die initiale Gr��e des Drawings, das hei�t die Gr��e der TileMap mit
	 * Skalierungsfaktor 1.
	 */
	private Coordinate initialSize;

	/**
	 * Jedes Drawing-Objekt enth�lt beliebig viele <code>Defect</code>s, das
	 * hei�t
	 */
	private ArrayList<Defect> defects = new ArrayList<Defect>();

	/**
	 * Konstruktor, der ein Drawing-Objekt instanziiert.
	 * 
	 * @param tileMaps
	 * @param activeTileMap
	 * @param initialSize
	 * @param defects
	 */
	public Drawing(ArrayList<TileMap> tileMaps, TileMap activeTileMap,
			Coordinate initialSize, ArrayList<Defect> defects) {
		this.tileMaps = tileMaps;
		this.acitveTileMap = activeTileMap;
		this.initialSize = initialSize;
		this.defects = defects;
	}

	/**
	 * Konstrukor, der ebenfalls ein Drawing-Objekt instanziiert, die weiteren
	 * Parameter werden sp�ter gesetzt.
	 * 
	 * @param initialSize
	 */
	public Drawing(Coordinate initialSize) {
		this.initialSize = initialSize;
	}

	/**
	 * @return the tileMaps
	 */
	public ArrayList<TileMap> getTileMaps() {
		return tileMaps;
	}

	/**
	 * @param tileMaps
	 *            the tileMaps to set
	 */
	public void setTileMaps(ArrayList<TileMap> tileMaps) {
		this.tileMaps = tileMaps;
	}

	/**
	 * @return the acitveTileMap
	 */
	public TileMap getAcitveTileMap() {
		return acitveTileMap;
	}

	/**
	 * @param acitveTileMap
	 *            the acitveTileMap to set
	 */
	public void setAcitveTileMap(TileMap acitveTileMap) {
		this.acitveTileMap = acitveTileMap;
	}

	/**
	 * @return the initialSize
	 */
	public Coordinate getInitialSize() {
		return initialSize;
	}

	/**
	 * @param initialSize
	 *            the initialSize to set
	 */
	public void setInitialSize(Coordinate initialSize) {
		this.initialSize = initialSize;
	}

	/**
	 * @return the defects
	 */
	public ArrayList<Defect> getDefects() {
		return defects;
	}

	/**
	 * @param defects
	 *            the defects to set
	 */
	public void setDefects(ArrayList<Defect> defects) {
		this.defects = defects;
	}

	/**
	 * F�gt eine TileMap am Ende der ArrayList an.
	 * 
	 * @param tileMap
	 */
	public void addTileMap(TileMap tileMap) {
		this.tileMaps.add(tileMap);
	}

	/**
	 * F�gt eine TileMap an einer bestimmten Position in der ArrayList an.
	 * 
	 * @param tileMap
	 * @param location
	 */
	public void addTileMap(TileMap tileMap, int location) {
		this.tileMaps.add(location, tileMap);
	}

}
