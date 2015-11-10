/**
 * @(#)DrawingAdministration.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.factory;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import de.hdm.hettich.studienarbeit.bo.Defect;
import de.hdm.hettich.studienarbeit.bo.Drawing;
import de.hdm.hettich.studienarbeit.bo.TileMap;
import de.hdm.hettich.studienarbeit.bo.Viewport;
import de.hdm.hettich.studienarbeit.service.TileMapChanger;
import de.hdm.hettich.studienarbeit.view.HScroll;
import de.hdm.hettich.studienarbeit.view.VScroll;

/**
 * Die Klasse <code>DrawingAdministration</code> bildet ein <code>Drawing</code>
 * -Objekt in der Android-Sicht ab. Dabei wird die Umwandlung der einzelnen
 * Bestandteile des <code>Drawing</code>s, wie der <code>TileMap</code> und
 * deren <code>Tile</code>s, durch die <code>TileMap2ViewFactory</code> bzw. die
 * <code>Tile2ViewFactory</code> angesto�en.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class DrawingAdministration {

	/**
	 * <code>Drawing</code>-Objekt, das dargestellt werden soll und durch den
	 * Aufruf der Methode <code>createCalculateedView</code> umgewandelt wird.
	 */
	private Drawing drawing;

	/**
	 * Aktueller <code>Viewport</code> des Gesamtplans, der f�r die Erstellung
	 * der Zoom-Buttons ben�tigt wird.
	 */
	private Viewport viewport;

	/**
	 * Context der Applikation.
	 */
	private Context context;

	/**
	 * <code>RelativeLayout</code>, das als Wrapper f�r die <code>TileMap</code>
	 * und den Defect-Wrapper dient.
	 */
	private RelativeLayout wrapper;

	/**
	 * <code>RelativeLayout</code>, das alle <code>DefectView</code>s h�lt.
	 */
	private RelativeLayout wrapperDefects;

	/**
	 * Konstruktor, der eine neue <code>DrawingAdministration</code> erstellt,
	 * dabei werden das anzuzeigende <code>Drawing</code>, sowie der
	 * Applikations-Kontext und der <code>Viewport</code> �bergeben.
	 * 
	 * @param drawing
	 * @param context
	 * @param viewport
	 */
	public DrawingAdministration(Drawing drawing, Context context,
			Viewport viewport) {
		this.drawing = drawing;
		this.context = context;
		this.viewport = viewport;
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
	 * @return the wrapperDefects
	 */
	public RelativeLayout getWrapperDefects() {
		return wrapperDefects;
	}

	/**
	 * @param wrapperDefects
	 *            the wrapperDefects to set
	 */
	public void setWrapperDefects(RelativeLayout wrapperDefects) {
		this.wrapperDefects = wrapperDefects;
	}

	/**
	 * Erstellt das komplette RelativeLayout zur Anzeige, das das Drawing
	 * abbildet
	 * 
	 * @return RelativeLayout komplettes RelativeLayout
	 */
	public RelativeLayout createCalculatedView() {
		// RelativeLayout, das das ganze Konstrukt zusammenh�lt.
		RelativeLayout totalWrapper = new RelativeLayout(this.context);

		/*
		 * Angepasster horizontaler und vertikaler ScrollView, um diagonales
		 * Scrollen zu erm�glichen.
		 */
		HScroll hScroll = new HScroll(this.context);
		VScroll vScroll = new VScroll(this.context);

		// Hinzuf�gen von HScroll und VScroll
		totalWrapper.addView(hScroll);
		hScroll.addView(vScroll);

		/*
		 * Erstellen und Hinzuf�gen eines weiteren Wrapper, der die Bild-Layer
		 * h�lt.
		 */
		wrapper = new RelativeLayout(this.context);
		vScroll.addView(wrapper);

		/*
		 * Erstellen einer neuen TileMap2LayerFactory, die die TileMaps in einen
		 * TileMapView zur Anzeige umwandeln.
		 */
		TileMap2ViewFactory viewFactory = new TileMap2ViewFactory(this.context);

		/*
		 * Initiale Zoom-Stufe ausfindig machen, das hei�t die TileMap finden,
		 * deren Gr��e der im Drawing gespeicherten <code>initialSize</code>
		 * entspricht.
		 */
		for (TileMap tileMap : drawing.getTileMaps()) {
			// Pr�fen, ob diese TileMap die Initiale ist.
			if (tileMap.getSize().equals(drawing.getInitialSize())) {
				/*
				 * Wenn diese TileMap die Initiale ist, wird sie dem
				 * wrapper-RelativeLayout hinzugef�gt.
				 */
				wrapper.addView(viewFactory
						.createCalculatedTileMapView(tileMap));
			}
		}

		/*
		 * Erstellen einer neuen Defect2ViewFactory, die die Defects in mehrere
		 * DefectViews zur Anzegie umwandelt.
		 */
		Defect2ViewFactory defectFactory = new Defect2ViewFactory(this.context);

		// Erstellen des RelativeLayout f�r die Defects.
		wrapperDefects = new RelativeLayout(this.context);
		// Alle vorhandenen Defects durchlaufen.
		for (Defect defect : drawing.getDefects()) {
			/*
			 * Erstellen eines neuen DefectViews und hinzuf�gen zum
			 * wrapperDefects.
			 */
			wrapperDefects.addView(defectFactory
					.createCalculatedView(defect, 1));
		}
		wrapper.addView(wrapperDefects);

		/*
		 * Hinzuf�gen der Buttons zum Zoom In bzw. Zoom Out. Diese Buttons
		 * werden erstellt, da momentan ein Zoomen mittels Pinch-To-Zoom noch
		 * nicht m�glich ist, die Methoden f�r einen Zoomstufenwechsel aber
		 * schon geschaffen werden sollen. Die Realisierung findet also durch
		 * einen Button (+) und einen Button (-) statt. Bei Bet�tigung dieser
		 * Buttons erfolgt jeweils ein direkter Wechsel der anzugzeigenden
		 * TileMap. Stufenloses Zoomen wird zun�chst ebenfalls noch nicht
		 * unterst�tzt.
		 */
		/*
		 * LinearLayout mit vertikaler Ausrichtung, sodass die Buttons
		 * untereinander angezeigt werden k�nnen.
		 */
		LinearLayout llButtons = new LinearLayout(context);
		llButtons.setBackgroundColor(Color.WHITE);
		llButtons.setOrientation(LinearLayout.VERTICAL);

		/*
		 * Button Zoom In. Die n�chst detailliertere Ebene wird dadurch
		 * aufgerufen.
		 */
		Button btnZoomIn = new Button(context);
		btnZoomIn.setText("+");
		btnZoomIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TileMapChanger changer = new TileMapChanger(wrapper, drawing,
						viewport, context);
				changer.zoomIn();
			}
		});
		// Hinzuf�gen des Buttons zum LinearLayout
		llButtons.addView(btnZoomIn);

		/*
		 * Button zum Zoom Out. Die n�chst allgemeinere Ebene wird dadurch
		 * aufgerufen.
		 */
		Button btnZoomOut = new Button(context);
		btnZoomOut.setText("-");
		btnZoomOut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TileMapChanger changer = new TileMapChanger(wrapper, drawing,
						viewport, context);
				changer.zoomOut();
			}
		});
		// Hinzuf�gen des Buttons zum LinearLayout
		llButtons.addView(btnZoomOut);

		// Hinzuf�gen des LinearLayouts zum totalWrapper-RelativeLayout.
		totalWrapper.addView(llButtons);

		return totalWrapper;
	}

}
