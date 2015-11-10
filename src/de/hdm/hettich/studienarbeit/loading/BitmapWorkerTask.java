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
 * abgeleitet und bietet die Möglichkeit Operationen im Hintergrund, ohne den
 * Hauptprogrammfluss zu blockieren, auszufüheren. Hier werden die PNGs in
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
	 * Die Referenz wird benötigt, um auf die process-Methoden zugreifen zu
	 * können und den <code>BitmapWorkerTask</code> nicht als NestedClass des
	 * <code>BitmapConverter</code> implementierren zu müssen.
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
	 * im Hintergrund ausgeführt wird.
	 * 
	 * @param params
	 */
	protected Bitmap doInBackground(Tile... params) {

		/*
		 * Erster übergebener Parameter wird ausgelesen, hier gibt es immer nur
		 * einen!
		 */
		Tile tile = params[0];

		// Instanziierung eines byte[] zur späteren Befüllung.
		byte[] blob = null;

		// Prüfen, ob Tile bereits geladen ist.
		if (!tile.isLoaded()) {
			/*
			 * Prüfen, ob der Task bereits beendet wurde, wenn ja die Ausführung
			 * dieser Methode abbrechen.
			 */
			if (!isCancelled()) {
				/*
				 * Tile ist nicht geladen, das heißt der byte[] muss zunächst
				 * noch aus dem PNG erzeugt werden.
				 */
				blob = this.converter.processByteArray(tile.getFilename());
				// Berechnetes byte[] wird dem Tile gesetzt.
				tile.setImage(blob);

				/*
				 * Achtung, diese Pause wird nur aus Demo-Zwecken eingeführt,
				 * damit das expliziete nacheinander stattfindende Lade besser
				 * sichtbar wird.
				 */
				try {
					// Der Thread wird für 500ms angehalten.
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
		 * Prüfen, ob der Task bereits beendet wurde, wenn ja die Ausführung
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
	 * ausgeführt, hier wird das berechnete <code>Bitmap</code> im 
	 * <code>TileView</code> gesetzt.
	 * 
	 * @param bitmap
	 */
	protected void onPostExecute(Bitmap bitmap) {
		/*
		 * Prüfen, ob Bitmap wirklich gesetzt ist, es könnte ja bei
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
	 * Dieser Teil wird ausgeführt, wenn ein Task beendet wird.
	 */
	protected void onCancelled() {
		/*
		 * Da der Task abgeschlossen (abgebrochen) ist, kann er aus der Liste im
		 * BitmapConverter entfernet werden.
		 */
		BitmapConverter.removeTask(this);
	}

}
