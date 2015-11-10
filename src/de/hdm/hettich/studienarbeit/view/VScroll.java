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
 * gewährleistet das vertikale Scrollen in der Anwendung, die Klasse muss
 * spezialisiert werden, da das onTouchEvent überschrieben werden muss, um das
 * diagonale Scrollen zu gewährleisten, denn nicht der einzelne ScrollView darf
 * das Event verarbeiten (sonst ist nur das Scrollen in eine Richtung möglich
 * und erst mit der nächsten Bildschirm-Berührung in die Andere), sondern eine
 * Überklasse, die dann an beide ScrollViews die gleiche Information weitergibt.
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
	 * Das TouchEvent wird nicht hier aufgerufen, sondern in der ausführbaren
	 * Klasse.
	 * 
	 * Dadurch wird das diagonale Scrollen ermöglicht.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

}
