/**
 * @(#)GlobalSettings.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.service;

/**
 * Die Klasse <code>GlobalSettings</code> führt Buch über die aktuelle
 * Auslastung des Arbeitsspeichers und schlägt ab der Überschreitung einer hier
 * definierten Grenze Alarm.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class GlobalSettings {

	/**
	 * Konstante die den maximalen Wert der prozentualen Speicherbelegung
	 * angibt.
	 */
	public final static float MAX_MEMORY_USAGE = 0.75f;

	/**
	 * Diese gibt zurück, ob aktuell wenig freier Arbeitsspeicher vorhanden ist.
	 * Dabei wird der gesamt verfügbare Speicher mit dem belegten Speicher
	 * verglichen. {@link GlobalSettings#MAX_MEMORY_USAGE} entscheidet bei wie
	 * viel Prozent Belegung der Speicher als "gering" angesehen werden soll.
	 * (Aktuell 75%, das heißt wenn 75% des Speichers belegt sind gibt diese
	 * Funktion <code>true</code>).
	 * 
	 * @return boolean true wenn die Speicherbelegung eine Grenze überschritten
	 *         hat.
	 */
	public boolean isLowMemory() {

		// Maximal verfügbarer Speicher.
		float maxMem = Runtime.getRuntime().maxMemory();

		// Aktuell belegter Speicher.
		float usedMem = Runtime.getRuntime().totalMemory();

		/*
		 * Entscheidung ob belgeter Speicher geteilt durch den gesamten Speicher
		 * größer der oben gesetzten Konstante ist.
		 */
		boolean result = ((usedMem / maxMem) >= MAX_MEMORY_USAGE);

		// Log.e("Memory", "is low: " + result + " Speicherstand: " + usedMem
		// + " / " + maxMem);

		return result;
	}

}
