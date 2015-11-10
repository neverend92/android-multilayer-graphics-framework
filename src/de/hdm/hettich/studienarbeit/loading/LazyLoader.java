/**
 * @(#)LazyLoader.java
 * 1.0, 2013-03-04 
 */
package de.hdm.hettich.studienarbeit.loading;

import java.util.ArrayList;

import android.util.Log;

import de.hdm.hettich.studienarbeit.bo.Viewport;
import de.hdm.hettich.studienarbeit.service.TileGarbageService;
import de.hdm.hettich.studienarbeit.utile.Coordinate;
import de.hdm.hettich.studienarbeit.utile.Tools;
import de.hdm.hettich.studienarbeit.view.TileMapView;
import de.hdm.hettich.studienarbeit.view.TileView;

/**
 * Die Klasse <code>LazyLoader</code> übernimmt den kompletten Nachlade-Prozess,
 * hier werden die zuladenden <code>Tile</code>s identifiziert und das Laden
 * dieser angestoßen. Der Ladeprozess ist in eine extra <code>Thread</code>
 * ausgelagert, sodass dieser jederzeit (bspw. bei einer erneuten Verschiebung
 * des Plans) beendet werden kann.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class LazyLoader {

	/**
	 * Der <code>TileMapView</code>, in dem die <code>TileView</code>s geladen
	 * werden sollen.
	 */
	private TileMapView tileMapView;

	/**
	 * Der Viewport stellt die aktuelle sichtbare Fläche auf dem Bildschirm dar,
	 * er wird bei Verschiebung des Gesamtplans immer mit verschoben.
	 */
	private Viewport viewport;

	/**
	 * Thread in den der Ladevorgang ausgeführt wird, dies muss so geschehen, da
	 * es möglich sein soll diesen Vorgang jederzeit (z.B. beim erneuten
	 * Verschieben der Anzeige) zu stoppen.
	 */
	private static Thread loadingThread;

	/**
	 * Instanz des <code>LazyLoader</code>s zwischenspeichern, um sicher zu
	 * stellen, das dieser nur genau einmal initialisiert ist. Durch den
	 * Bezeichner <code>static</code> ist diese Variable für jede andere
	 * eventuelle Instanz dieser Klasse gleich gesetzt.
	 */
	private static LazyLoader lazyLoader = null;

	/**
	 * Konstruktor, der eine neue Instanz des <code>LazyLoader</code>s erstellt,
	 * dabei wird der zu ladende <code>TileMapView</code>, sowie der aktuelle
	 * <code>Viewport</code> übergeben.
	 * 
	 * @param tileMapView
	 * @param viewport
	 */
	protected LazyLoader(TileMapView tileMapView, Viewport viewport) {
		this.tileMapView = tileMapView;
		this.viewport = viewport;
	}

	/**
	 * Statische Methode, die aufgerufen werden kann, um eine Instanz des
	 * <code>LazyLoader</code>s zurück zubekommen, dabei wird entweder die
	 * bereits vorhandene Instanz zurückgegeben, oder wenn noch keine exisitiert
	 * eine neue angelegt und diese in der Klassenvariable
	 * {@link LazyLoader#lazyLoader} abgelegt.
	 * 
	 * @param tileMapView
	 * @param viewport
	 * @return LazyLoader entweder den bereits vorhandenen, oder einen neuen
	 *         LazyLoader
	 */
	public static LazyLoader lazyLoader(TileMapView tileMapView,
			Viewport viewport) {
		if (lazyLoader == null) {
			lazyLoader = new LazyLoader(tileMapView, viewport);
		}
		if (!lazyLoader.getTileMapView().equals(tileMapView)
				|| !lazyLoader.getViewport().equals(viewport)) {
			Log.e("LazyLoader", "Neuer LazyLoader erstellt.");
			lazyLoader = new LazyLoader(tileMapView, viewport);
		}
		return lazyLoader;
	}

	/**
	 * @return the tileMapView
	 */
	public TileMapView getTileMapView() {
		return tileMapView;
	}

	/**
	 * @param tileMapView
	 *            the tileMapView to set
	 */
	public void setTileMapView(TileMapView tileMapView) {
		this.tileMapView = tileMapView;
	}

	/**
	 * @return the viewport
	 */
	public Viewport getViewport() {
		return viewport;
	}

	/**
	 * @param viewport
	 *            the viewport to set
	 */
	public void setViewport(Viewport viewport) {
		this.viewport = viewport;
	}

	/**
	 * Initialisiert den Ladevorgang für den übergebenen
	 * <code>TileMapView</code>. Es werden die zu ladenden <code>Tile</code>s
	 * ermittelt und anschließend deren Laden angestoßen.
	 */
	public void load() {

		/*
		 * Wenn bereits ein Thread läuft, oder vorhanden ist, dann ist
		 * loadingTask nicht null, deshalb muss der laufende Task zunächst
		 * unterbrochen werden und dann null gesetzt werden. Das expliziete
		 * Stoppen eines Thread ist in Android nicht möglich. Zudem müssen alle
		 * bereits gestarteten asnychronen Lade-Tasks gestoppt werden.
		 */
		if (loadingThread != null) {
			// "Stoppen" des Threads.
			loadingThread.interrupt();
			// "Löschen" des Threads.
			loadingThread = null;
			// Beenden des AsyncTasks.
			BitmapConverter.cancelTasks();
		}

		// Erstellen eines neuen Threads.
		loadingThread = new Thread() {

			@Override
			public void run() {
				// Dimensionen der TileMap (des Plans) abrufen.
				Coordinate dimension = tileMapView.getTileMap().getDimension();

				// Größe der einzelnen Tiles der Ebene abrufen.
				Coordinate tileSize = tileMapView.getTileMap().getTileSize();

				// Bildschirm-Mitte in Pixel abrufen.
				Coordinate center = viewport.calculateCenter(tileMapView
						.getTileMap().getSize());

				/*
				 * Berechnung der Anzahl der Level, die geladen werden müssen,
				 * damit der Bildschirm komplett gefüllt ist.
				 */
				int loadingLevel = calculateLoadingLevel(tileSize);

				/*
				 * Mittlere Kachel anhand der Bildschirm-Mitte und der
				 * Kachel-Größe berechen.
				 */
				Coordinate centerTile = calculateCenterTile(center, tileSize);

				// Alle Nachbarn der Reihenfolge nach abarbeiten.
				for (Coordinate tilePos : calculateNeighbours(centerTile,
						dimension, loadingLevel)) {
					if (!loadingThread.isInterrupted()) {
						/*
						 * Neuer TileGarbageService wird instanziert und bei
						 * Bedarf werden Tiles vergessen, damit Platz für das
						 * Laden neuer Tiles ist.
						 */
						new TileGarbageService().cleanIfNecessary(tileMapView,
								tilePos);

						/*
						 * Den zugehörigen TileView ausfindig machen (siehe auch
						 * convertMatrixPos2Index) und den Ladeprozess an dieser
						 * Stelle anstoßen.
						 */
						TileView tileView = (TileView) tileMapView
								.getChildAt(Tools.convertMatrixPos2Int(tilePos,
										dimension));

						/*
						 * Diesen TileView laden. Dazu wird ein neuer
						 * BitmapConverter instanziert und diesem den zu
						 * ladenden TileView als Parameter übergeben.
						 */
						BitmapConverter converter = new BitmapConverter(
								tileView);

						// Start des Ladevorgangs.
						converter.loadImage();
					} else {
						BitmapConverter.cancelTasks();
					}
				}

				Log.e("LazyLoader", "LazyLoader ist fertig!");
			}
		};

		// Starten des erstellten Threads.
		loadingThread.start();
	}

	/**
	 * Gibt die Koordinaten der mittleren Kachel zurück.
	 * 
	 * @param center
	 * @param tileSize
	 * @return Coordinate die Position der mittleren Kachel
	 */
	public Coordinate calculateCenterTile(Coordinate center, Coordinate tileSize) {
		return new Coordinate(center.getX() / tileSize.getX(), center.getY()
				/ tileSize.getY());
	}

	/**
	 * Gibt die Anzahl der Level zurück, die geladen werden müssen, damit das
	 * komplette Display gefüllt ist.
	 * 
	 * @param tileSize
	 * @return int das LoadingLevel bis zu dem geladen werden muss
	 */
	public int calculateLoadingLevel(Coordinate tileSize) {
		int viewportX = this.viewport.getSize().getX();
		int viewportY = this.viewport.getSize().getY();

		double tempLoadingLevel = 0;

		/*
		 * Prüfen, welche Ausrichtung der Viewport hat, das heißt welche Seite
		 * die Größere ist. Anhand dieser wird das Lade-Level berechnet.
		 */
		if (viewportX > viewportY) {
			tempLoadingLevel = (viewportX / 2) / tileSize.getX();
		} else {
			tempLoadingLevel = (viewportY / 2) / tileSize.getY();
		}

		return (int) Math.floor(tempLoadingLevel);
	}

	/**
	 * Gibt die Koordinaten der Nachbarn der übergebenen mittleren
	 * <code>Tile</code> auf dem EINEN übergebenen Level zurück. Mit Hilfe der
	 * Dimensionen der <code>TileMap</code> wird abgefangen, wenn ein
	 * potentieller Nachbar außerhalb des Plans liegen würde.
	 * 
	 * @param level
	 * @return ArrayList<Coordinate> die Nachbarn auf einem bestimmten Level
	 */
	public ArrayList<Coordinate> calculateNeighboursOfLevel(
			Coordinate centerTile, Coordinate dimension, int level) {
		// Ergebnis ArrayList initialisieren.
		ArrayList<Coordinate> neighbours = new ArrayList<Coordinate>();

		/*
		 * Start-Werte initialieren. Die Werte sind relative Werte zur mittleren
		 * Kachel (0, 0). Es wird mit der rechten, oberen Ecke (level, -level)
		 * begonnen, von dort wird zur rechten, unteren Ecke (level, level)
		 * gegangen. Von dort weiter zur linken, unteren Ecke (-level, level),
		 * dann weiter zur linken, oberen Ecke (-level, -level) und von dort
		 * wieder zum Start. Die Startecke wird natürlich nur genau einmal
		 * hinzugefügt
		 */
		int x = level;
		int y = -level;

		/*
		 * Die Koordinate, dienen zur Zwischenberechnung der absoulten
		 * Koordinaten. Diese Koordinaten müssen ja nochmals geprüft werden, ob
		 * sie wirklich im Plan liegen.
		 */
		Coordinate tempCoordinate;

		/*
		 * Nun sind die Werte:
		 * 
		 * x = level
		 * 
		 * y = -level
		 * 
		 * Von der rechten, oberen Ecke noch unten zur rechten, unteren Ecke
		 * arbeiten.
		 */
		for (; y < level; y++) {
			tempCoordinate = new Coordinate(x + centerTile.getX(), y
					+ centerTile.getY());
			// Prüfen, ob Koordinate im Plan liegt, nur dann hinzufügen.
			if (checkCoordinate(tempCoordinate, dimension)) {
				neighbours.add(new Coordinate(tempCoordinate.getX(),
						tempCoordinate.getY()));
			}
		}

		/*
		 * Nun sind die Werte:
		 * 
		 * x = level
		 * 
		 * y = level
		 * 
		 * Von der rechten, unteren Ecke nach links zur linken, unteren Ecke
		 * arbeiten.
		 */
		for (; x > -level; x--) {
			tempCoordinate = new Coordinate(x + centerTile.getX(), y
					+ centerTile.getY());
			// Prüfen, ob Koordinate im Plan liegt, nur dann hinzufügen.
			if (checkCoordinate(tempCoordinate, dimension)) {
				neighbours.add(new Coordinate(tempCoordinate.getX(),
						tempCoordinate.getY()));
			}
		}

		/*
		 * Nun sind die Werte:
		 * 
		 * x = -level
		 * 
		 * y = level
		 * 
		 * Von der linken, unteren Ecke nach oben zur linken, oberen Ecke
		 * arbeiten.
		 */
		for (; y > -level; y--) {
			tempCoordinate = new Coordinate(x + centerTile.getX(), y
					+ centerTile.getY());
			// Prüfen, ob Koordinate im Plan liegt, nur dann hinzufügen.
			if (checkCoordinate(tempCoordinate, dimension)) {
				neighbours.add(new Coordinate(tempCoordinate.getX(),
						tempCoordinate.getY()));
			}
		}

		/*
		 * Nun sind die Werte:
		 * 
		 * x = -level
		 * 
		 * y = -level
		 * 
		 * Von der linken, oberen Ecke nach rechts zur rechten, oberen Ecke
		 * arbeiten. Dabei wird die Start-Koordinate NICHT erneunt hinzugefügt,
		 * deshalb läuft die Schleife nur bis kleiner 'level' und nicht bis
		 * kleiner gleich 'level'.
		 */
		for (; x < level; x++) {
			tempCoordinate = new Coordinate(x + centerTile.getX(), y
					+ centerTile.getY());
			// Prüfen, ob Koordinate im Plan liegt, nur dann hinzufügen.
			if (checkCoordinate(tempCoordinate, dimension)) {
				neighbours.add(new Coordinate(tempCoordinate.getX(),
						tempCoordinate.getY()));
			}
		}

		return neighbours;
	}

	/**
	 * Gibt die Koordinaten der Nachbarn der übergebenen mittleren
	 * <code>Tile</code> und zwar auf allen Ebenen bis zum übergebenen Level.
	 * Dabei wird jede Ebene durch den Aufruf der Methode @see
	 * {@link LazyLoader#calculateNeighboursOfLevel(Coordinate, Coordinate, int)}
	 * ermittelt und dann zu einer gesamten <code>ArrayList</code>
	 * zusammengefügt. Dabei wird auf die mittlere Kachel selbst hinzugefügt, da
	 * diese ebenfalls geladen werden muss.
	 * 
	 * @param centerTile
	 * @param dimension
	 * @param level
	 * @return ArrayList<Coordinate> die Nachbarn bis zu einem bestimmten Level
	 */
	public ArrayList<Coordinate> calculateNeighbours(Coordinate centerTile,
			Coordinate dimension, int level) {
		// Ergebis ArrayList initialisieren.
		ArrayList<Coordinate> neighbours = new ArrayList<Coordinate>();

		// Mittlere Kachel hinzufügen.
		neighbours.add(centerTile);

		// Alle Ebenen, ab Ebene 1 bis zum übergebenen Level hinzufügen.
		for (int i = 1; i <= level; i++) {
			/*
			 * Aufruf der Methode, die alle Nachbarn einer speziellen Ebene (i)
			 * zurückgibt, danach hinzufügen der ArrayList zur
			 * Ergebnis-ArrayList.
			 */
			neighbours.addAll(calculateNeighboursOfLevel(centerTile, dimension,
					i));
		}

		return neighbours;
	}

	/**
	 * Prüft, ob eine übergebene <code>Coordinate</code> in den übergebenen
	 * Dimensionen liegt. Das heißt, dass die <code>Coordinate</code> weder im
	 * negativen Bereich (kleiner null), noch größer als die Dimesionen der
	 * <code>TileMap</code>.
	 * 
	 * @param tempCoordinate
	 * @param dimension
	 * @return boolean true wenn die Koordinate noch im Plan liegt
	 */
	public boolean checkCoordinate(Coordinate tempCoordinate,
			Coordinate dimension) {
		return (tempCoordinate.getX() < dimension.getX()
				&& tempCoordinate.getY() < dimension.getY()
				&& tempCoordinate.getX() >= 0 && tempCoordinate.getY() >= 0);
	}
}
