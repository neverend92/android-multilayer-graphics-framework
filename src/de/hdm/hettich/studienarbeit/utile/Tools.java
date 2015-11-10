/**
 * @(#)Tools.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.utile;

/**
 * Die Klasse <code>Tools</code> beinhaltet einige allgemeine mathematische
 * Operationen, die keiner anderen Klasse speziell zugeordnet werden können,
 * durch die öffentlichen, statischen Methoden ist ein direkter Zugriff von
 * außen möglich.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class Tools {

	/**
	 * Bestimmt anhand der übergebenen Koordinaten der mittleren
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
	 * Berechnet die Länge eines übergebenen Vektors, der hier in Form einer
	 * <code>Coordinate</code> übergeben wird.
	 * 
	 * @param c
	 * @return double Länge des Vektors
	 */
	public static double calculateVectorLength(Coordinate c) {
		return Math.sqrt(c.getX() * c.getX() + c.getY() * c.getY());
	}

}
