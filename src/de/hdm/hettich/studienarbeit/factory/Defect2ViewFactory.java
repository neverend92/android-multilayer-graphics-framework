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
 * Die Klasse <code>Defect2ViewFactory.java</code> ist für die Umwandlung eines
 * <code>Defect</code>s in einen <code>DefectView</code> zustänig. Der
 * <code>DefectView</code> kommt letzendlich auf dem Gerät zur Anzeige.
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
	 * Konstruktor, der eine neue Defect2ViewFactory erstellt. Benötigt context
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
	 * übergeben. Am Ende wird ein <code>DefectView</code> mit schwarzem
	 * Hintergrund und einem Margin links und oben, der der Position des
	 * <code>Defect</code> entspricht, zurückgegeben. Zudem wird dem
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
		 * Setzen der LayoutParams Zunächst Größe des DefectView, dann die
		 * Abstände oben und links (dadurch werden die DefectView positioniert).
		 */
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// Breite und Höhe des ImageViews.
		lp.width = defect.getSize().getX();
		lp.height = defect.getSize().getY();

		/*
		 * Setzen des Abstands der DefectViews zur Positionierung auf dem
		 * Display. Die Position ist an der linken oberen Ecke ausgerichtet,
		 * deshalb muss die Hälfte der Defect-Größe abgezogen werden, um den
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
		 * Berührung des DefectViews klar wird, das dieser berührt wurde und
		 * nicht etwa der Plan. Dadurch soll eine Schnittstelle geschafft
		 * werden, die es später ermöglichen soll die Defect-Icons dynamisch zu
		 * erzeugen bzw. auch wieder zu löschen oder zu bearbeiten.
		 */
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * Erstellen eines Dialog-Builders, der die Erstellung des
				 * eigentlichen Dialogs übernimmt.
				 */
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				/*
				 * Der View von dem aus das Click-Event ausgelöst wurde wird
				 * abgerufen und in einen DefectView umgewandelt.
				 */
				DefectView defectView = (DefectView) v;
				/*
				 * Mit Hile des oben abgerufenen DefectView wird eine Nachricht
				 * erstellt, die später dem Nutzer bei einem Klick auf das
				 * Defect-Icon angezeigt werden soll. Dabei wird der Typ des
				 * Defects abgerufen.
				 */
				String message = "Das Icon wurde angeklickt. Es handelt sich um ein "
						+ defectView.getDefect().getTypeOfNote() + "-Icon.";
				// Die erstellte Nachricht wird dem Dialog zugeordnet.
				builder.setMessage(message);

				// Ein Button zum bestätigen des Dialogs wird angefügt.
				builder.setPositiveButton("ok", null);

				// Der Dialog wird erstellt.
				AlertDialog dialog = builder.create();
				// Dialog anzeigen.
				dialog.show();
			}
		});

		// Zurückgeben des erstellten Views.
		return view;
	}
}
