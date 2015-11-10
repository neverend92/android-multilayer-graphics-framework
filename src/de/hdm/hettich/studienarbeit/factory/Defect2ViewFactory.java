/**
 * @(#)Defect2ViewFactory.java
 * 1.0, 2013-03-04
 */
package de.hdm.hettich.studienarbeit.factory;

import de.hdm.hettich.studienarbeit.bo.Defect;
import de.hdm.hettich.studienarbeit.view.DefectView;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Die Klasse <code>Defect2ViewFactory.java</code> ist f�r die Umwandlung eines
 * <code>Defect</code>s in einen <code>DefectView</code> zust�nig. Der
 * <code>DefectView</code> kommt letzendlich auf dem Ger�t zur Anzeige.
 * 
 * @author Stefan Hettich
 * 
 * @version 1.0, 2013-03-04
 * 
 */
public class Defect2ViewFactory {

	/**
	 * Context der Applikation
	 */
	private Context context;

	/**
	 * Konstruktor, der eine neue Defect2ViewFactory erstellt. Ben�tigt context
	 * als Parameter.
	 * 
	 * @param context
	 */
	public Defect2ViewFactory(Context context) {
		this.context = context;
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
	 * In dieser Methode wird der <code>DefectView</code> erzeugt, als
	 * Information werden <code>Defect</code> und der aktuelle Skalierungsfaktor
	 * �bergeben. Am Ende wird ein <code>DefectView</code> mit schwarzem
	 * Hintergrund und einem Margin links und oben, der der Position des
	 * <code>Defect</code> entspricht, zur�ckgegeben. Zudem wird dem
	 * <code>DefectView</code> ein <code>OnClickListener</code> zugeordnet, um
	 * Klicks auf das <code>Defect</code>-Icon abzufangen.
	 * 
	 * @param defect
	 * @param scaleFactor
	 * @return DefectView der erstellte DefectView
	 */
	public DefectView createCalculatedView(Defect defect, float scaleFactor) {
		// Erstellen eines neuen DefectViews.
		DefectView view = new DefectView(context, defect);

		/*
		 * Setzen der LayoutParams Zun�chst Gr��e des DefectView, dann die
		 * Abst�nde oben und links (dadurch werden die DefectView positioniert).
		 */
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// Breite und H�he des ImageViews.
		lp.width = defect.getSize().getX();
		lp.height = defect.getSize().getY();

		/*
		 * Setzen des Abstands der DefectViews zur Positionierung auf dem
		 * Display. Die Position ist an der linken oberen Ecke ausgerichtet,
		 * deshalb muss die H�lfte der Defect-Gr��e abgezogen werden, um den
		 * DefectView mittig zu positionieren.
		 */
		lp.leftMargin = (int) ((defect.getPosition().getX() * scaleFactor) - defect
				.getSize().getX() / 2);
		lp.topMargin = (int) ((defect.getPosition().getY() * scaleFactor) - defect
				.getSize().getY() / 2);

		// Dem ImageView werden die oben erstellten Parameter zugeordnet.
		view.setLayoutParams(lp);

		/*
		 * Schwarzen Hintergrund setzen, um das Icon zu indentifizieren.
		 */
		view.setBackgroundColor(Color.BLACK);

		/*
		 * Dem DefectView wird ein OnClickListener zugeordnet, damit bei einer
		 * Ber�hrung des DefectViews klar wird, das dieser ber�hrt wurde und
		 * nicht etwa der Plan. Dadurch soll eine Schnittstelle geschafft
		 * werden, die es sp�ter erm�glichen soll die Defect-Icons dynamisch zu
		 * erzeugen bzw. auch wieder zu l�schen oder zu bearbeiten.
		 */
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * Erstellen eines Dialog-Builders, der die Erstellung des
				 * eigentlichen Dialogs �bernimmt.
				 */
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				/*
				 * Der View von dem aus das Click-Event ausgel�st wurde wird
				 * abgerufen und in einen DefectView umgewandelt.
				 */
				DefectView defectView = (DefectView) v;
				/*
				 * Mit Hile des oben abgerufenen DefectView wird eine Nachricht
				 * erstellt, die sp�ter dem Nutzer bei einem Klick auf das
				 * Defect-Icon angezeigt werden soll. Dabei wird der Typ des
				 * Defects abgerufen.
				 */
				String message = "Das Icon wurde angeklickt. Es handelt sich um ein "
						+ defectView.getDefect().getTypeOfNote() + "-Icon.";
				// Die erstellte Nachricht wird dem Dialog zugeordnet.
				builder.setMessage(message);

				// Ein Button zum best�tigen des Dialogs wird angef�gt.
				builder.setPositiveButton("ok", null);

				// Der Dialog wird erstellt.
				AlertDialog dialog = builder.create();
				// Dialog anzeigen.
				dialog.show();
			}
		});

		// Zur�ckgeben des erstellten Views.
		return view;
	}
}
