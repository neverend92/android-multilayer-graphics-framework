/**
 * @(#)Tools.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.utile;

/**
 * Die Klasse <code>Tools</code> beinhaltet einige allgemeine mathematische
 * Operationen, die keiner anderen Klasse speziell zugeordnet werden k�nnen,
 * durch die �ffentlichen, statischen Methoden ist ein direkter Zugriff von
 * au�en m�glich.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class Tools {

	/**
	 * Bestimmt anhand der �bergebenen Koordinaten der mittleren
	 * <code>Tile</code> und den Dimensionen der <code>TileMap</code> die
	 * Position im eindimensionalen Array der <code>TileView</code>s des
	 * <code>TileMapView</code>.
	 * 
	 * @param centerTile
	 * @param dimension
	 * @return int Position im eindimensionalen Array
	 */
	public static int convertMatrixPos2Int(Coordinate centerTile,
			Coordinate dimension) {
		return centerTile.getX() * dimension.getX() + centerTile.getY();
	}

	/**
	 * Berechnet die L�nge eines �bergebenen Vektors, der hier in Form einer
	 * <code>Coordinate</code> �bergeben wird.
	 * 
	 * @param c
	 * @return double L�nge des Vektors
	 */
	public static double calculateVectorLength(Coordinate c) {
		return Math.sqrt(c.getX() * c.getX() + c.getY() * c.getY());
	}

}
