/**
 * @(#)DemoDrawingFrame.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import android.os.Environment;
import de.hdm.hettich.studienarbeit.bo.Defect;
import de.hdm.hettich.studienarbeit.utile.Coordinate;

/**
 * Die Klasse <code>DemoDrawingFrame</code> soll ein <code>Drawing</code> in
 * Rohform repr�sentieren, so wie es beispielsweise aus einer XML-Datei gelesen
 * werden k�nnte. Die Attribute dieser Klasse beinhalten alle Informationen, die
 * zur Erstellung eines <code>Drawing</code> notwendig sind.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class DemoDrawingFrame {

	/**
	 * Anzahl der Zoom-Ebenen, die das Demo-Drawing haben soll.
	 */
	public final int countZoomLevels = 3;

	/**
	 * Die Skalierungsfaktoren der einzelnen Zoom-Stufen.
	 */
	public final ArrayList<Float> scaleFactors = new ArrayList<Float>(
			Arrays.asList(0.5f, 1.0f, 2.0f));

	/**
	 * Die Kachel-Gr��en der Tiles der einzelnen Stufen.
	 */
	public final ArrayList<Coordinate> tileSizes = new ArrayList<Coordinate>(
			Arrays.asList(new Coordinate(200, 200), new Coordinate(200, 200),
					new Coordinate(200, 200)));

	/**
	 * Die Anzahl der Kacheln (x mal y), der jeweiligen Zoom-Ebene.
	 */
	public final ArrayList<Coordinate> levelDimensions = new ArrayList<Coordinate>(
			Arrays.asList(new Coordinate(10, 10), new Coordinate(20, 20),
					new Coordinate(40, 40)));

	/**
	 * Verzeichnis der SD-Karte f�r die sp�tere Verwendung.
	 */
	public final URI sdCardDir = Environment.getExternalStorageDirectory()
			.toURI();

	/**
	 * Dieses Attribut ist gesetzt, wenn es f�r den gesamten Plan nur ein Bild
	 * geben soll. {@link #pngFileEachTileMap} und
	 * {@link #pngFileEachTile} m�ssen gleichzeitig null sein.
	 * <p>
	 * Es soll dadurch erm�glicht werden eine Demo-Ansicht der Anwendung zu
	 * erzeugen, ohne jedem <code>Tile</code> ein explizietes Bild zuzuordnen.
	 */
	public final URI pngFileSingle = null;

	/**
	 * Dieses Attribut ist gesetzt, wenn es f�r jede Plan-Ebene (Zoomstufe) nur
	 * ein Bild geben soll. {@link #pngFileSingle} und
	 * {@link #pngFileEachTile} m�ssen gleichzeitig null sein.
	 * <p>
	 * Es soll dadurch erm�glicht werden eine Demo-Ansicht der Anwendung zu
	 * erzeugen, ohne jedem <code>Tile</code> ein explizietes Bild zuzuordnen.
	 * 
	 */
	public final ArrayList<URI> pngFileEachTileMap = new ArrayList<URI>(
			Arrays.asList(sdCardDir.resolve("tileLayer1.png"),
					sdCardDir.resolve("tileLayer2.png"),
					sdCardDir.resolve("tileLayer3.png")));

	/**
	 * Dieses Attribut ist gesetzt, wenn es f�r jedes einzelnes
	 * <code>Tile</code> ein Bild geben soll.
	 * {@link #pngFileSingle} und
	 * {@link #pngFileEachTileMap} m�ssen gleichzeitig null sein.
	 */
	public final ArrayList<URI[][]> pngFileEachTile = null;

	/**
	 * Die Standard-Gr��e der <code>Defect</code>-Icons. Jedes Icons wird in
	 * dieser Gr��e im Plan dargestellt, die Gr��e der Icons variiert je nach
	 * Zoomstufe nicht. Das hei�t die Icons werden immer gleich angezeigt,
	 * lediglich die Position �ndert sich.
	 */
	public final Coordinate defectSize = new Coordinate(50, 50);

	/**
	 * Die <code>Defect</code>s, also die im Drawing vorhandenen Fehler bzw.
	 * Notizen. Ein <code>Defect</code> kann von einen bestimmten Typ sein
	 * (Video, Audio, Text, etc.) dies wird momentan noch durch einen simplen
	 * <code>String</code> gel�st, sp�ter k�nnen nat�rlich unterschiedliche
	 * Icons f�r jeden unterschiedlichen Typ verwendet werden. Neben dem Typ
	 * wird einem Defect noch die Position bei Zoomfaktor 1 und die Gr��e
	 * mitgegeben.
	 */
	public final ArrayList<Defect> defects = new ArrayList<Defect>(
			Arrays.asList(new Defect("Text", new Coordinate(200, 200),
					defectSize), new Defect("Video", new Coordinate(435, 200),
					defectSize), new Defect("Audio",
					new Coordinate(1000, 1000), defectSize)));

}
