/**
 * @(#)BitmapConverter.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.loading;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import de.hdm.hettich.studienarbeit.view.TileView;

/**
 * Die Klasse <code>BitmapConverter</code> stößt die Umwandlung eines PNGs zu
 * einem Bitmap an, dabei wird ein <code>BitmapWorkerTask</code> erstellt, der
 * die jeweilige Umwandlung übernimmt, sodass die Hauptanwendung nicht blockiert
 * wird.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class BitmapConverter {

	/**
	 * <code>TileView</code> der geladen werden soll.
	 */
	private TileView tileView;

	/**
	 * Hier werden alle Task gespeichert, die durch den BitmapConverter
	 * gestartet werden, dadurch ist es möglich alle laufenden Task zu beenden.
	 */
	private static ArrayList<BitmapWorkerTask> tasks = new ArrayList<BitmapWorkerTask>();

	/**
	 * Konstruktor, der einen neuen <code>BitmapConverter</code> erstellt.
	 * 
	 * @param tileView
	 */
	public BitmapConverter(TileView tileView) {
		this.tileView = tileView;
	}

	/**
	 * @return the tileView
	 */
	public TileView getTileView() {
		return tileView;
	}

	/**
	 * @param tileView
	 *            the tileView to set
	 */
	public void setTileView(TileView tileView) {
		this.tileView = tileView;
	}

	/**
	 * @return the tasks
	 */
	public static ArrayList<BitmapWorkerTask> getTasks() {
		return tasks;
	}

	/**
	 * @param tasks
	 *            the tasks to set
	 */
	public static void setTasks(ArrayList<BitmapWorkerTask> tasks) {
		BitmapConverter.tasks = tasks;
	}

	/**
	 * Fügt den übergebenen Task der <code>ArrayList</code> hinzu. Der Task kann
	 * somit ggf. beendet werden.
	 * 
	 * @param task
	 */
	public static void addTask(BitmapWorkerTask task) {
		BitmapConverter.tasks.add(task);
	}

	/**
	 * Entfernt ein bestimmtes Task-Objekt, z.B. wenn dieser Task erfolgreich
	 * abgeschlossen ist.
	 * 
	 * @param task
	 */
	public static void removeTask(BitmapWorkerTask task) {
		BitmapConverter.tasks.remove(task);
	}

	/**
	 * Diese Methode stößt den Ladevorgang an, dabei wird ein
	 * <code>AsyncTask</code> in Form eines <code>BitmapWorkerTask</code>
	 * erstellt.
	 */
	public void loadImage() {
		/*
		 * Der asynchrone Task wird erstellt, in ihm wird der komplette
		 * Umwandlungsvorgang vorgenommen. Dabei wird zunächst geprüft, ob eine
		 * Tile bereits geladen ist bzw. ein byte[] gesetzt ist. Ist kein byte[]
		 * gesetzt wird dieser zunächst aus dem übergebenen PNG erstellt. Danach
		 * wird aus diesem byte[] ein Bitmap erstellt, das dem TileView
		 * zugeordnet wird.
		 */
		BitmapWorkerTask task = new BitmapWorkerTask(this);
		/*
		 * Ablegen des Tasks in der ArrayList, damit er bei Bedarf beendet
		 * werden kann.
		 */
		BitmapConverter.addTask(task);
		// Explizietes Ausführen des Tasks.
		task.execute(this.tileView.getTile());
	}

	/**
	 * Beendet alle momentan laufenden Tasks.
	 */
	public static void cancelTasks() {
		// Alle abgelegten Task werden durchlaufen.
		for (BitmapWorkerTask task : tasks) {
			// Prüfen, ob dieser Task noch vorhanden (nicht null) ist.
			if (task != null) {
				// Der Task wird beendet, auch wenn er gerade läuft.
				task.cancel(true);
			}
		}
	}

	/**
	 * Verarbeitet einen <code>byte[]</code> in ein <code>Bitmap</code>, das
	 * dann angezeigt werden kann.
	 * 
	 * @param blob
	 * @return Bitmap das berechnete Bitmap
	 */
	public Bitmap processBitmap(byte[] blob) {
		// Log.d(TAG, "Start processBitmap");
		Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
		// Log.d(TAG, "Ende processBitmap");
		return bitmap;
	}

	/**
	 * Verarbeitet einen Pfad zu einem PNG in einen <code>byte[]</code>, der
	 * dann in einer <code>Tile</code> gespeichert wird.
	 * 
	 * @param pngFile
	 * @return byte[] der berechnete Byte-Array
	 */
	public byte[] processByteArray(URI pngFile) {
		// Log.d(TAG, "Start processByteArray");

		/*
		 * File anhand der übergebenen URI erstellen. File wird benötigt, um
		 * später die Größe des byte[] zu bestimmen.
		 */
		File file = new File(pngFile);

		/*
		 * Ergebnis-Byte-Array wird bereits hier erstellt, damit er später
		 * zurückgegeben werden kann, auch wenn es im try-Block zu einem Fehler
		 * kommt. Sollte dies der Fall sein, wird null zurückgegeben,
		 * anderenfalls der berechnete byte[].
		 */
		byte[] result = null;

		try {
			/*
			 * FileInputStream> erstellen, sodass das File ausgelesen werden
			 * kann.
			 */
			FileInputStream fis = new FileInputStream(file);

			/*
			 * Ergebnis-Byte-Array wird mit der Länge des Files initialisiert.
			 */
			result = new byte[(int) file.length()];

			/*
			 * Lesen der Datei und gleichzeitiges übertragen in den oben
			 * erstellten <code>byte[]</code>.
			 */
			fis.read(result);

			// Schließen des <code>FileInputStreams</code>
			fis.close();

		} catch (FileNotFoundException e) {
			/*
			 * Sollte die übergebene Datei nicht vorhanden sein, ist
			 * <code>file</code> gegebenenfalls <code>null</code>, deshalb würde
			 * es zu einem Fehler bei der Erstellung des
			 * <code>FileInputStreams</code> kommen, dies wird hier abgefangen.
			 */
			e.printStackTrace();

		} catch (IOException e) {
			/*
			 * Beim Lesen der Datei kann es im <code>FileInputStream</code> zu
			 * Fehlern kommen, diese werden hier abgefangen.
			 */
			e.printStackTrace();

		}

		// Rückgabe des <code>byte[]<code>
		return result;
	}

}
