/**
 * @(#)Tile2ViewFactory.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.factory;

import de.hdm.hettich.studienarbeit.bo.Tile;
import de.hdm.hettich.studienarbeit.utile.Coordinate;
import de.hdm.hettich.studienarbeit.view.TileView;
import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Die Klasse <code>Tile2ViewFactory.java</code> ist für die Umwandlung eines
 * <code>Tile</code>s in einen <code>TileView</code> zustänig. Der
 * <code>TileView</code> kommt letzendlich auf dem Gerät zur Anzeige.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class Tile2ViewFactory {

	/**
	 * Context der Applikation
	 */
	private Context context;

	/**
	 * Konstruktor, der eine neue Tile2ViewFactory erstellt. Benötigt context
	 * als Parameter.
	 * 
	 * @param context
	 */
	public Tile2ViewFactory(Context context) {
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
	 * In dieser Methode wird der <code>TileView</code> erzeugt, als Information
	 * werden <code>Tile</code> und die Position in der <code>TileMap</code>
	 * übergeben. Am Ende wird ein <code>TileView</code> mit grünem Hintergrund
	 * und einem Margin links und oben, der der Position der <code>Tile</code>
	 * entspricht, zurückgegeben. Zudem wird dem <code>TileView</code> ein
	 * Padding von 1px angefügt, damit bei der Anzeige nach dem Laden des Bildes
	 * ein grüner Rahmen zurück bleibt, der es leichter macht die Kacheln
	 * voneinander abzugrenzen.
	 * 
	 * @param tile
	 * @param pos
	 * @return TileView der erstellte TileView
	 */
	public TileView createCalculatedView(Tile tile, Coordinate pos) {
		// Erstellen eines neuen TileViews.
		TileView view = new TileView(context, tile);

		/*
		 * Setzen der LayoutParams Zunächst Größe des TileView, dann die
		 * Abstände oben und links (dadurch werden die TileView nebeneinander
		 * angezeigt)
		 */
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// Breite und Höhe des ImageViews.
		lp.width = tile.getSize().getX();
		lp.height = tile.getSize().getY();
		/*
		 * Margins beschreiben den Abstand eines Elements zum Rand. Durch setzen
		 * eines Margins links und oben kann das ImageView-Objekt im
		 * RelativeLayout positioniert werden. Durch den übergebenen Parameter
		 * pos, der die Position in der TileMap beschreibt ist klar, die wie
		 * vielste Tile dargestellt werden soll. Es ist also lediglich eine
		 * Multiplizierung des Tile-Position mit der allgemeinen Tile-Größe
		 * notwendig, um den notwendigen Margin zu berechen.
		 */
		lp.leftMargin = pos.getX() * tile.getSize().getX();
		lp.topMargin = pos.getY() * tile.getSize().getY();

		// Dem ImageView werden die oben erstellten Parameter zugeordnet.
		view.setLayoutParams(lp);
		/*
		 * Zu DEMO-Zwecken wird jedem ImageView ein grünen Hintergrund verpasst
		 * und gleichzeitig ein Padding von 1px angefügt, so dass jeder
		 * ImageView eine Art "Rahmen" besitzt, um die Abgrenzung zwischen den
		 * einzelnen ImageViews deutlicher zu machen.
		 */
		view.setBackgroundColor(Color.GREEN);
		view.setPadding(1, 1, 1, 1);

		// Zurückgeben des erstellten TileViews.
		return view;
	}
}
