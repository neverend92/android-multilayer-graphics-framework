/**
 * @(#)VScroll.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Die Klasse <code>VScroll</code> erbt von <code>ScrollView</code> und
 * gew�hrleistet das vertikale Scrollen in der Anwendung, die Klasse muss
 * spezialisiert werden, da das onTouchEvent �berschrieben werden muss, um das
 * diagonale Scrollen zu gew�hrleisten, denn nicht der einzelne ScrollView darf
 * das Event verarbeiten (sonst ist nur das Scrollen in eine Richtung m�glich
 * und erst mit der n�chsten Bildschirm-Ber�hrung in die Andere), sondern eine
 * �berklasse, die dann an beide ScrollViews die gleiche Information weitergibt.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class VScroll extends ScrollView {

	public VScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public VScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VScroll(Context context) {
		super(context);
	}

	/**
	 * Das TouchEvent wird nicht hier aufgerufen, sondern in der ausf�hrbaren
	 * Klasse.
	 * 
	 * Dadurch wird das diagonale Scrollen erm�glicht.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

}
