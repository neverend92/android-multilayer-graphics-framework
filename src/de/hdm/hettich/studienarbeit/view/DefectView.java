/**
 * @(#)DefectView.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.view;

import de.hdm.hettich.studienarbeit.bo.Defect;
import android.content.Context;
import android.widget.ImageView;

/**
 * Die Klasse <code>DefectView</code> erbt von <code>ImageView</code> und
 * übernimmt die Darstellung eines <code>Defect</code>s.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class DefectView extends ImageView {

	/**
	 * <code>Defect</code>, der in diesem <code>DefectView</code> dargestellt
	 * wird.
	 */
	private Defect defect;

	/**
	 * Konstruktor, der verhanden sein muss, da <code>DefectView</code> von
	 * <code>ImageView</code> erbt. Die Sichtbarkeit ist auf protected gesetzt,
	 * sodass ein Zugriff von außen nicht möglich ist und stattdessen der
	 * Konstruktor mit mehreren Parametern verwendet werden muss.
	 * 
	 * @param context
	 */
	protected DefectView(Context context) {
		super(context);
	}

	/**
	 * Konstruktor, der ein neuen <code>DefectView</code> erstellt, dabei wird
	 * der zugehörige <code>Defect</code> gesetzt.
	 * 
	 * @param context
	 * @param defect
	 */
	public DefectView(Context context, Defect defect) {
		super(context);
		this.defect = defect;
	}

	/**
	 * @return the defect
	 */
	public Defect getDefect() {
		return defect;
	}

	/**
	 * @param defect
	 *            the defect to set
	 */
	public void setDefect(Defect defect) {
		this.defect = defect;
	}

}
