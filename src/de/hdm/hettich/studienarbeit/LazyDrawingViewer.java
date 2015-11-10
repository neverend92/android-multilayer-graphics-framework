/**
 * @(#)LazyDrawingViewer.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit;

import de.hdm.hettich.studienarbeit.bo.Drawing;
import de.hdm.hettich.studienarbeit.bo.Viewport;
import de.hdm.hettich.studienarbeit.factory.DrawingAdministration;
import de.hdm.hettich.studienarbeit.factory.DrawingCreator;
import de.hdm.hettich.studienarbeit.loading.LazyLoader;
import de.hdm.hettich.studienarbeit.utile.Coordinate;
import de.hdm.hettich.studienarbeit.view.HScroll;
import de.hdm.hettich.studienarbeit.view.TileMapView;
import de.hdm.hettich.studienarbeit.view.VScroll;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Die <code>MainAcitvity</code> ist eine ausf�hrbare <code>Activity</code> die
 * beim Start der App aufgerufen wird. Sie st��t die Generierung eines Demo-
 * <code>Drawing</code>s an und st��t die Umwandlung in eine Darstellung an.
 * Zudem wird hier das Touch-Event verarbeitet und an die beiden
 * <code>ScrollView</code>s weitergegeben, dabei wird auch der Nachladeprozess
 * angesto�en.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class LazyDrawingViewer extends Activity {

	/**
	 * Diese Variablen werden ben�tigt, um die Touch-Events zu verarbeiten.
	 * <code>mx</code> stellt die Ber�hrungsposition in x-Richtung dar.
	 */
	private float mx;
	/**
	 * Diese Variablen werden ben�tigt, um die Touch-Events zu verarbeiten.
	 * <code>my</code> stellt die Ber�hrungsposition in y-Richtung dar.
	 */
	private float my;

	/**
	 * <code>Viewport</code> beschreibt den sichtbaren Bereich der Anwendung,
	 * die Gr��e des <code>Viewport</code>s berechnet sich aus der
	 * <code>Display</code>-Gr��e des Endger�ts. Hier muss eventuell geschaut
	 * werden, wie die Gr��e der Title- und Notification-Bar berechnet werden
	 * kann, um diese dann abzuziehen.
	 */
	private Viewport viewport;

	/**
	 * Der <code>LazyLoader</code> �bernimmt das Nachladen der Kacheln, er
	 * identifiziert die zu ladenden Kacheln und st��t den Lade-Prozess an.
	 */
	private LazyLoader loader;

	/**
	 * Das <code>RelativeLayout</code> h�lt das gesamte "Sandwich" zusammen,
	 * alle weiteren Views sind Childs dieses Views.
	 */
	private RelativeLayout rl;

	/**
	 * Der <code>HScroll</code> gew�hrleistet das horizontale Scrollen, er
	 * leitet sich vom <code>HorizontalScrollView</code> ab und beinhaltet
	 * einige geringf�gige �nderungen, so dass diagonales Scrollen m�glich wird.
	 */
	private HScroll hScroll;

	/**
	 * Der <code>VScroll</code> gew�hrleistet das vertikale Scrollen, er leitet
	 * sich vom <code>ScrollView</code> ab und beinhaltet einige geringf�gige
	 * �nderungen, so dass diagonales Scrollen m�glich wird.
	 */
	private VScroll vScroll;

	/**
	 * Ein weiteres <code>RelativeLayout</code>, das die einzelnen Ebenen des
	 * Plans zusammenh�lt, deshalb auch wrapper.
	 */
	private RelativeLayout wrapper;

	/**
	 * Der <code>TileMapView</code> der initial angezeigt wird. Ein
	 * <code>TileMapView</code> ist wiederrum eine Spezialisierung eines
	 * <code>RelativeLayout</code> und beinhaltet die Kacheln, die in
	 * <code>TileView</code>s angzeigt werden.
	 */
	private TileMapView tileMapView;

	/**
	 * Diese Methode wird beim Erstellen der <code>Activity</code> aufgerufen
	 * und beinhaltet alle Operationen, die beim Starten der gesamten
	 * Applikation ausgef�hrt werden m�ssen. Das sind neben dem Abrufen,
	 * speziellen Hardware-Informationen des Endger�ts, auf dem die App
	 * ausgef�hrt wird, vor allem die Erstellung der Grundstruktur des
	 * "Sandwichs".
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * Abrufen des aktuellen Displays, sodass dessen Gr��e abgerufen werden
		 * kann.
		 */
		Display display = this.getWindowManager().getDefaultDisplay();
		/*
		 * Erstellen des Viewport, anhand der Gr��e des oben abgerufenen
		 * Displays. Die Position des Viewports wird initial auf (0, 0), also
		 * oben links gesetzt.
		 */
		viewport = new Viewport(new Coordinate(0, 0), new Coordinate(
				display.getWidth(), display.getHeight()));

		// Erstellen des Drawing
		Drawing drawing = DrawingCreator.createDrawing(new DemoDrawingFrame());

		/*
		 * Erstellung einer neuen DrawingAdministration, die das �bergebene
		 * Drawing in ein Layout umwandelt.
		 */
		DrawingAdministration drawingAdministration = new DrawingAdministration(
				drawing, this, viewport);

		/*
		 * Mit dem Aufruf der getCalculatedView-Methode wird ein RelativeLayout
		 * erstellt, das die komplette Struktur des Drawing enth�lt.
		 */
		this.rl = drawingAdministration.createCalculatedView();

		// HScroll wird als erster Child des RelativeLayouts abgerufen.
		this.hScroll = (HScroll) this.rl.getChildAt(0);
		// VScroll wird als erster Child des HScrolls abgerufen.
		this.vScroll = (VScroll) this.hScroll.getChildAt(0);
		// wrapper wird als erster Child des VScrolls abgerufen.
		this.wrapper = (RelativeLayout) this.vScroll.getChildAt(0);
		// tileMapView wird als zweites Child des wrappers abgerufen.
		this.tileMapView = (TileMapView) this.wrapper.getChildAt(0);

		/*
		 * Instanzierung des LazyLoaders, mit der �bergabe des aktuellen
		 * TileMapView und des Viewports.
		 */
		loader = LazyLoader.lazyLoader(tileMapView, viewport);

		// Starten des Ladevorgangs durch den LazyLoader.
		loader.load();

		// Setzen dieses RelativeLayouts.
		setContentView(rl);
	}

	/**
	 * onTouchEvent wird �berschrieben.
	 * 
	 * Touch-Verschiebungen werden sowohl an vScroll wie auch hScroll
	 * weitergegeben, somit wird das diagonale Scrollen erm�glicht. Davor haben
	 * die beiden View, die Touch-Gesten getrennt von einander registriert, das
	 * hei�t wenn es sich um eine horizontale Bewegung handelte wurde nur der
	 * HorizontalScrollView aktiv, anderenfalls nur der ScrollView.
	 * 
	 * @param event
	 */
	public boolean onTouchEvent(MotionEvent event) {
		float curX, curY;

		switch (event.getAction()) {
		// Die Touch-Geste beginnt, durch Ber�hrung des Displays.
		case MotionEvent.ACTION_DOWN:
			// Initiale Position speichern.
			mx = event.getX();
			my = event.getY();
			break;
		// Bewegung, die w�hrend dem Touch-Event "passiert"
		case MotionEvent.ACTION_MOVE:
			// Position nach Verschiebung zwischenspeichern.
			curX = event.getX();
			curY = event.getY();

			// Touch-Event verarbeiten.
			this.handleTouchMove(curX, curY);

			// Neue Position speichern.
			mx = curX;
			my = curY;
			break;
		case MotionEvent.ACTION_UP:
			// Position nach Verschiebung zwischenspeichern.
			curX = event.getX();
			curY = event.getY();

			// Touch-Event verarbeiten.
			this.handleTouchMove(curX, curY);

			Log.e("TEST",
					"VScroll: " + vScroll.getScrollX() + " / "
							+ vScroll.getScrollY());
			Log.e("TEST",
					"HScroll: " + hScroll.getScrollX() + " / "
							+ hScroll.getScrollY());

			/*
			 * Ansto�en des Ladeprozesses, dieser wird beabsichtigt erst beim
			 * Loslassen des Bildschirms gestartet, da dieser Prozess sonst zu
			 * oft angesto�en werden w�rde.
			 */
			this.tileMapView = (TileMapView) this.wrapper.getChildAt(0);
			loader = LazyLoader.lazyLoader(tileMapView, viewport);
			loader.load();

			break;
		}

		return true;
	}

	/**
	 * Bearbeitet die Bewegung beim Touch-Event, die bei einer Bewegung und beim
	 * Loslassen des Displays ausgef�hrt werden muss. Die Bewegungen werden an
	 * <code>HScroll</code>, <code>VScroll</code> und den <code>Viewport</code>
	 * weitergegeben.
	 * 
	 * @param curX
	 * @param curY
	 */
	private void handleTouchMove(float curX, float curY) {
		// Zwischenspeichern der aktuellen Bewegungsweite.
		int dx = (int) (mx - curX);
		int dy = (int) (my - curY);

		// Viewport ebenfalls verschieben.
		viewport.translate(dx, dy, tileMapView.getTileMap().getSize());

		// Werte an ScrollViews �bergeben.
		vScroll.scrollTo(viewport.getPosition().getX(), viewport.getPosition()
				.getY());
		hScroll.scrollTo(viewport.getPosition().getX(), viewport.getPosition()
				.getY());
	}

}
