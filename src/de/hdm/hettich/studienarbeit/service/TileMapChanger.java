/**
 * @(#)TileMapChanger.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.service;

import de.hdm.hettich.studienarbeit.bo.Defect;
import de.hdm.hettich.studienarbeit.bo.Drawing;
import de.hdm.hettich.studienarbeit.bo.TileMap;
import de.hdm.hettich.studienarbeit.bo.Viewport;
import de.hdm.hettich.studienarbeit.factory.Defect2ViewFactory;
import de.hdm.hettich.studienarbeit.factory.TileMap2ViewFactory;
import de.hdm.hettich.studienarbeit.loading.LazyLoader;
import de.hdm.hettich.studienarbeit.utile.Coordinate;
import de.hdm.hettich.studienarbeit.view.HScroll;
import de.hdm.hettich.studienarbeit.view.TileMapView;
import de.hdm.hettich.studienarbeit.view.VScroll;
import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Die Klasse <code>TileMapChanger</code> ist für den Zoomstufen-Wechsel bei der
 * Anzeige eines <code>Drawing</code>s zuständig. Sowohl das Drücken des
 * ZoomIn-, als auch des ZoomOut-Buttons wird hier bearbeitet, dabei wird die
 * aktuelle Zoomstufe vergessen und die neue Zoomstufen angezeigt.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class TileMapChanger {

	/**
	 * <code>RelativeLayout</code>, das den </code>TileMapView</code> hält und
	 * zunächst geleert werden muss, um dann wieder mit dem aktuellen
	 * <code>TileMapView</code> gefüllt zu werden.
	 */
	private RelativeLayout wrapper;

	/**
	 * <code>Drawing</code>, das gerade angezeigt wird und indem ein
	 * Zoomstufen-Wechsel stattfindet.
	 */
	private Drawing drawing;

	/**
	 * <code>Context</code> der Applikation.
	 */
	private Context context;

	/**
	 * <code>Viewport</code> zu Bestimmung der aktuellen Anzeige im Plan und die
	 * Umwandlung in der neuen Zoomstufe, sodass die Anzeige-Position auch dort
	 * stimmt.
	 */
	private Viewport viewport;

	/**
	 * Konstruktor, der einen <code>TileMapChanger</code> erstellt.
	 * 
	 * @param wrapper
	 * @param drawing
	 * @param viewport
	 * @param context
	 */
	public TileMapChanger(RelativeLayout wrapper, Drawing drawing,
			Viewport viewport, Context context) {
		this.wrapper = wrapper;
		this.drawing = drawing;
		this.viewport = viewport;
		this.context = context;
	}

	/**
	 * @return the wrapper
	 */
	public RelativeLayout getWrapper() {
		return wrapper;
	}

	/**
	 * @param wrapper
	 *            the wrapper to set
	 */
	public void setWrapper(RelativeLayout wrapper) {
		this.wrapper = wrapper;
	}

	/**
	 * @return the drawing
	 */
	public Drawing getDrawing() {
		return drawing;
	}

	/**
	 * @param drawing
	 *            the drawing to set
	 */
	public void setDrawing(Drawing drawing) {
		this.drawing = drawing;
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
	 * Diese Methode wird aufgerufen, wenn ein Reinzoomen stattfinden soll.
	 * Dabei wird die nächst detailliertere TileMap zur Anzeige gebracht, sofern
	 * diese verfügbar ist.
	 */
	public void zoomIn() {
		// Aktuell aktive TileMap abrufen.
		TileMap activeTileMap = drawing.getAcitveTileMap();
		// Position der TileMap in Array-List abrufen.
		int posInList = drawing.getTileMaps().indexOf(activeTileMap);
		if (posInList < drawing.getTileMaps().size() - 1) {
			TileMap newTileMap = drawing.getTileMaps().get(posInList + 1);
			/*
			 * Übergabe an die Methode initZoom, die Operationen vornimmt, die
			 * sowohl beim Reinzommen, als auch beim Rauszoomen ausgeführt
			 * werden müssen.
			 */
			initZoom(newTileMap);
		} else {
			Toast.makeText(context,
					"Die maximale Zoomstufe wurde bereits erreicht.",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Diese Methode wird aufgerufen, wenn ein Rauszoomen stattfinden soll.
	 * Dabei wird die nächst allgemeinere TileMap zur Anzeige gebracht, sofern
	 * diese verfügbar ist.
	 */
	public void zoomOut() {
		// Aktuell aktive TileMap abrufen.
		TileMap activeTileMap = drawing.getAcitveTileMap();
		// Position der TileMap in Array-List abrufen.
		int posInList = drawing.getTileMaps().indexOf(activeTileMap);
		/*
		 * Prüfen, ob aktuelle Zoomebene an einer größeren Position als 0 steht,
		 * das heißt, ob es noch weitere allgemeinere TileMaps gibt.
		 */
		if (posInList > 0) {
			// Die nächst allgemeinere TileMap wird abgerufen.
			TileMap newTileMap = drawing.getTileMaps().get(posInList - 1);
			/*
			 * Übergabe an die Methode initZoom, die Operationen vornimmt, die
			 * sowohl beim Reinzommen, als auch beim Rauszoomen ausgeführt
			 * werden müssen.
			 */
			initZoom(newTileMap);
		} else {
			/*
			 * Sollte es keine allgemeinere TileMap mehr geben, wird dies bei
			 * Klick auf den Button durch eine kurze Nachricht mitgeteilt.
			 */
			Toast.makeText(context,
					"Die minimale Zoomstufe wurde bereits erreicht.",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Hier werden die Operationen zusammengefasst, die sowohl beim ZoomIn, als
	 * auch beim ZoomOut benötigt werden.
	 * 
	 * @param tileMap
	 */
	public void initZoom(TileMap tileMap) {

		// Abrufen des "alten" Skalierungsfaktors.
		float oldScaleFactor = drawing.getAcitveTileMap().getScaleFactor();
		// Abrufen des "neuen" Skalierungsfaktors.
		float newScaleFactor = tileMap.getScaleFactor();
		// Berechnung des "relativen" Skalierungsfaktors.
		float scaleFactor = newScaleFactor / oldScaleFactor;

		// Setzen der neuen aktiven TileMap.
		drawing.setAcitveTileMap(tileMap);

		// "Alte" Position des Viewport abrufen.
		Coordinate oldPos = viewport.getPosition();

		// Berechnen der neuen Position des Viewports.
		Coordinate newPos = calculateNewPosition(oldPos, scaleFactor,
				viewport.getSize());

		// Setzen der neuen Position des Viewports.
		viewport.setPosition(newPos, tileMap.getSize());

		// Alle vorhandenen TileMapViews werden entfernt.
		this.wrapper.removeAllViews();

		// Anhand der neuen TileMap wird ein neuer TileMapView erstellt.
		TileMap2ViewFactory viewFactory = new TileMap2ViewFactory(context);
		wrapper.addView(viewFactory.createCalculatedTileMapView(tileMap));

		/*
		 * Dieser Handler muss erstellt werden, da die ScrollViews (hScroll und
		 * vScroll) die Veränderugen ihre Childs nur mit Verzögerung
		 * registieren. Die Änderung des TileMapViews wird also nicht sofort
		 * behandelt und das führte zu Fehler beim scrollen in den ScrollViews,
		 * da diese nur bis zu den alten Grenzen scrollten. Jetzt wurde ein
		 * kleiner Delay von 100ms eingebaut, der die Verschiebung somit erst
		 * verzögert beginnen lässt, was das Problem löst.
		 */
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// hScroll und vScroll werden abgerufen.
				VScroll vScroll = (VScroll) wrapper.getParent();
				HScroll hScroll = (HScroll) vScroll.getParent();

				// Scrollen zu neuer Position des Viewports.
				vScroll.scrollTo(viewport.getPosition().getX(), viewport
						.getPosition().getY());
				hScroll.scrollTo(viewport.getPosition().getX(), viewport
						.getPosition().getY());
			}

		}, 100);

		/*
		 * Erstellen einer neuen Defect2ViewFactory, die die Defects in mehrere
		 * DefectViews zur Anzegie umwandelt.
		 */
		Defect2ViewFactory defectFactory = new Defect2ViewFactory(this.context);

		// Erstellen des RelativeLayout für die Defects.
		RelativeLayout wrapperDefects = new RelativeLayout(this.context);
		// Alle vorhandenen Defects durchlaufen.
		for (Defect defect : drawing.getDefects()) {
			/*
			 * Erstellen eines neuen DefectViews und hinzufügen zum
			 * wrapperDefects.
			 */
			wrapperDefects.addView(defectFactory.createCalculatedView(defect,
					newScaleFactor));
		}
		wrapper.addView(wrapperDefects);

		// Initialisieren des Ladevorgangs in neuem TileMapView.
		TileMapView tileMapView = (TileMapView) wrapper.getChildAt(0);
		LazyLoader loader = LazyLoader.lazyLoader(tileMapView, viewport);
		loader.load();
	}

	/**
	 * Diese Methode berechnet die neue Position des <code>Viewport</code> bei
	 * einer Vergrößerung bzw. einer Verkleinerung.
	 * 
	 * @param oldPos
	 * @param scaleFactor
	 * @param displaySize
	 * @return Coordinate die neue Position des Viewports
	 */
	public Coordinate calculateNewPosition(Coordinate oldPos,
			float scaleFactor, Coordinate displaySize) {
		/*
		 * Alte Position wird mit dem Skalierungsfaktor multipliziert, dadurch
		 * wird der Viewport zunächst an die linken oberen Ecke gleichgesetzt.
		 */
		float x = oldPos.getX() * scaleFactor;
		float y = oldPos.getY() * scaleFactor;

		/*
		 * Nun muss der Viewport noch so verschoben werden, dass sich der
		 * Mittelpunkt der alten Zoomstufe mit dem Mittelpunkt in der neuen
		 * Zoomstufe deckt. Dazu wird die Hälfte des Displays mit der relativen
		 * Skalierung multipliziert (das heißt wenn von Zoomstufe 1 auf
		 * Zoomstufe 2 umgeschalten wird ist der relative Zoomfaktor 1; wenn von
		 * Zoomstufe 2 auf Zoomstufe 1 umgeschalten wird ist der relative
		 * Zoomfaktor 0,5).
		 */
		x += (displaySize.getX() / 2 * (scaleFactor - 1));
		y += (displaySize.getY() / 2 * (scaleFactor - 1));

		// Rückgabe der berechneten Position.
		return new Coordinate((int) x, (int) y);
	}

}
