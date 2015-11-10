/**
 * @(#)BitmapWorkerTask.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.loading;

import de.hdm.hettich.studienarbeit.bo.Tile;
import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Die Klasse <code>BitmapWorkerTask</code> ist von <code>AsyncTask</code>
 * abgeleitet und bietet die M�glichkeit Operationen im Hintergrund, ohne den
 * Hauptprogrammfluss zu blockieren, auszuf�heren. Hier werden die PNGs in
 * Bitmaps konvertiert.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class BitmapWorkerTask extends AsyncTask<Tile, Void, Bitmap> {

	/**
	 * BitmapConverter, der diesen <code>BitmapWorkerTask</code> erstellt hat.
	 * Die Referenz wird ben�tigt, um auf die process-Methoden zugreifen zu
	 * k�nnen und den <code>BitmapWorkerTask</code> nicht als NestedClass des
	 * <code>BitmapConverter</code> implementierren zu m�ssen.
	 */
	private BitmapConverter converter;

	/**
	 * Konstruktor, der einen neuen <code>BitmapWorkerTask</code> erstellt.
	 * 
	 * @param converter
	 */
	public BitmapWorkerTask(BitmapConverter converter) {
		this.converter = converter;
	}

	/**
	 * @return the converter
	 */
	public BitmapConverter getConverter() {
		return converter;
	}

	/**
	 * @param converter
	 *            the converter to set
	 */
	public void setConverter(BitmapConverter converter) {
		this.converter = converter;
	}

	@Override
	/**
	 * Diese Methode beinhaltet den Teil der Berechnung, die asynchron 
	 * im Hintergrund ausgef�hrt wird.
	 * 
	 * @param params
	 */
	protected Bitmap doInBackground(Tile... params) {

		/*
		 * Erster �bergebener Parameter wird ausgelesen, hier gibt es immer nur
		 * einen!
		 */
		Tile tile = params[0];

		// Instanziierung eines byte[] zur sp�teren Bef�llung.
		byte[] blob = null;

		// Pr�fen, ob Tile bereits geladen ist.
		if (!tile.isLoaded()) {
			/*
			 * Pr�fen, ob der Task bereits beendet wurde, wenn ja die Ausf�hrung
			 * dieser Methode abbrechen.
			 */
			if (!isCancelled()) {
				/*
				 * Tile ist nicht geladen, das hei�t der byte[] muss zun�chst
				 * noch aus dem PNG erzeugt werden.
				 */
				blob = this.converter.processByteArray(tile.getFilename());
				// Berechnetes byte[] wird dem Tile gesetzt.
				tile.setImage(blob);

				/*
				 * Achtung, diese Pause wird nur aus Demo-Zwecken eingef�hrt,
				 * damit das expliziete nacheinander stattfindende Lade besser
				 * sichtbar wird.
				 */
				try {
					// Der Thread wird f�r 500ms angehalten.
					Thread.sleep(500);
				} catch (InterruptedException e) {
					/*
					 * Hier muss eine Exception abgefangen werden, die geworfen
					 * werden kann, wenn ein schlafender Task beendet wird.
					 */
				}

			} else {
				return null;
			}

		}

		/*
		 * Pr�fen, ob der Task bereits beendet wurde, wenn ja die Ausf�hrung
		 * dieser Methode abbrechen.
		 */
		if (!isCancelled()) {
			/*
			 * Jetzt ist der byte[] auf jeden Fall vorhanden, somit kann jetzt
			 * anhand diesem das Bitmap erstellt werden.
			 */
			return this.converter.processBitmap(tile.getImage());
		} else {
			return null;
		}
	}

	@Override
	/**
	 * Dieser Teil wird nach Abschluss der <code>doInBackground</code>-Methode 
	 * ausgef�hrt, hier wird das berechnete <code>Bitmap</code> im 
	 * <code>TileView</code> gesetzt.
	 * 
	 * @param bitmap
	 */
	protected void onPostExecute(Bitmap bitmap) {
		/*
		 * Pr�fen, ob Bitmap wirklich gesetzt ist, es k�nnte ja bei
		 * Hintergrund-Prozess zu Schwierigkeiten gekommen sein.
		 */
		if (bitmap != null) {
			// Das Bitmap in den TileView laden.
			this.converter.getTileView().setImageBitmap(bitmap);
		}
		/*
		 * Da der Task abgeschlossen ist, kann er aus der Liste im
		 * BitmapConverter entfernet werden.
		 */
		BitmapConverter.removeTask(this);
	}

	@Override
	/**
	 * Dieser Teil wird ausgef�hrt, wenn ein Task beendet wird.
	 */
	protected void onCancelled() {
		/*
		 * Da der Task abgeschlossen (abgebrochen) ist, kann er aus der Liste im
		 * BitmapConverter entfernet werden.
		 */
		BitmapConverter.removeTask(this);
	}

}
