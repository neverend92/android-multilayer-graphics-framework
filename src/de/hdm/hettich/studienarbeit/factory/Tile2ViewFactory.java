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
 * Die Klasse <code>Tile2ViewFactory.java</code> ist f�r die Umwandlung eines
 * <code>Tile</code>s in einen <code>TileView</code> zust�nig. Der
 * <code>TileView</code> kommt letzendlich auf dem Ger�t zur Anzeige.
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
	 * Konstruktor, der eine neue Tile2ViewFactory erstellt. Ben�tigt context
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
	 * �bergeben. Am Ende wird ein <code>TileView</code> mit gr�nem Hintergrund
	 * und einem Margin links und oben, der der Position der <code>Tile</code>
	 * entspricht, zur�ckgegeben. Zudem wird dem <code>TileView</code> ein
	 * Padding von 1px angef�gt, damit bei der Anzeige nach dem Laden des Bildes
	 * ein gr�ner Rahmen zur�ck bleibt, der es leichter macht die Kacheln
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
		 * Setzen der LayoutParams Zun�chst Gr��e des TileView, dann die
		 * Abst�nde oben und links (dadurch werden die TileView nebeneinander
		 * angezeigt)
		 */
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// Breite und H�he des ImageViews.
		lp.width = tile.getSize().getX();
		lp.height = tile.getSize().getY();
		/*
		 * Margins beschreiben den Abstand eines Elements zum Rand. Durch setzen
		 * eines Margins links und oben kann das ImageView-Objekt im
		 * RelativeLayout positioniert werden. Durch den �bergebenen Parameter
		 * pos, der die Position in der TileMap beschreibt ist klar, die wie
		 * vielste Tile dargestellt werden soll. Es ist also lediglich eine
		 * Multiplizierung des Tile-Position mit der allgemeinen Tile-Gr��e
		 * notwendig, um den notwendigen Margin zu berechen.
		 */
		lp.leftMargin = pos.getX() * tile.getSize().getX();
		lp.topMargin = pos.getY() * tile.getSize().getY();

		// Dem ImageView werden die oben erstellten Parameter zugeordnet.
		view.setLayoutParams(lp);
		/*
		 * Zu DEMO-Zwecken wird jedem ImageView ein gr�nen Hintergrund verpasst
		 * und gleichzeitig ein Padding von 1px angef�gt, so dass jeder
		 * ImageView eine Art "Rahmen" besitzt, um die Abgrenzung zwischen den
		 * einzelnen ImageViews deutlicher zu machen.
		 */
		view.setBackgroundColor(Color.GREEN);
		view.setPadding(1, 1, 1, 1);

		// Zur�ckgeben des erstellten TileViews.
		return view;
	}
}
