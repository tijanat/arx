package org.deidentifer.arx.masking;

/**
 * Testet Eingaben auf Gültigkeit
 * 
 * @author Kathrin
 *
 */
public class RegEx {
	/**
	 * Testet, ob in Eingabefeld für Datum ein gültiges Datum eingegeben wurde
	 * 
	 * @param s
	 * @return
	 */
	public static boolean regExDate(String s) {
		int schaltjahr = 0;
		/**
		 * Der String muss Länge 16 haben(yyyy-mm-dd hh:mm)
		 */
		if (s.length() != 16) {
			return false;
		}

		/**
		 * Es wird getestet, ob Trennzeichen (-, ,:) an der richtigen Stelle
		 * stehen
		 */

		if ("-".indexOf(s.substring(4, 5)) == -1) {
			return false;
		}
		if ("-".indexOf(s.substring(7, 8)) == -1) {
			return false;
		}
		if (" ".indexOf(s.substring(10, 11)) == -1) {
			return false;
		}
		if (":".indexOf(s.substring(13, 14)) == -1) {
			return false;
		}
		/**
		 * Jahresangabe(ersten 4 Zeichen) darf nur Zahlen enthalten
		 */
		String y = (s.substring(0, 4));
		for (int i = 0; i < y.length(); i++) {
			if ("0123456789".indexOf(y.substring(i, i + 1)) == -1)
				return false;
		}

		/**
		 * Jahresangabe soll zwischen 1900 und 3000 liegen(=> für
		 * Schaltjahrbestimmung wichig)
		 */

		int yy = Integer.valueOf(y);
		if ((yy <= 1900) || (yy > 3000))
			return false;
		if ((yy % 4) == 0)
			schaltjahr = 1;
		else
			schaltjahr = 0;

		/**
		 * Monatesangabe darf nur Zahlen enthalten (Stellen 5 und 6)
		 */

		String m = (s.substring(5, 7));
		for (int i = 0; i < m.length(); i++) {
			if ("0123456789".indexOf(m.substring(i, i + 1)) == -1)
				return false;
		}
		/**
		 * Montat kleiner 1 oder größer 12 ungültig
		 */
		int mm = Integer.valueOf(m);
		if ((mm < 1) || (mm > 12)) {
			return false;
		}

		/**
		 * Tagesangabe darf nur Zahlen enthalten (Stellen 8 und 9)
		 */
		String d = (s.substring(8, 10));
		for (int i = 0; i < d.length(); i++) {
			if ("0123456789".indexOf(d.substring(i, i + 1)) == -1)
				return false;
		}
		/**
		 * Tag kleiner 1 und größer 31 ungültig
		 */
		int dd = Integer.valueOf(d);
		if (dd < 1)
			return false;
		if (dd > 31)
			return false;

		/**
		 * Monate Februar, April, Juni, September und November nur 30 Tage
		 */

		if (((dd == 31) && (mm == 2)) || ((dd == 31) && (mm == 4))
				|| ((dd == 31) && (mm == 6)) || ((dd == 31) && (mm == 9))
				|| ((dd == 31) && (mm == 11))) {
			return false;
		}

		/**
		 * Februar nur 28 Tage, im Schaltjahr 29
		 */

		if ((dd == 30) && (mm == 2)) {
			return false;
		}
		if ((dd == 29) && (mm == 2) && (schaltjahr == 0)) {
			return false;
		}

		/**
		 * Stundenangabe darf nur Zahlen enthalten
		 */
		String h = (s.substring(11, 13));
		for (int i = 0; i < h.length(); i++) {
			if ("0123456789".indexOf(h.substring(i, i + 1)) == -1)
				return false;
		}
		/**
		 * Stundenangabe darf nur zwischen 0 und 23 liegen
		 */
		int hh = Integer.valueOf(h);
		if ((hh < 0) || (hh > 23))
			return false;
		/**
		 * Minutenangabe darf nur Zahlen enthalten
		 */
		String mi = (s.substring(14, 16));
		for (int i = 0; i < mi.length(); i++) {
			if ("0123456789".indexOf(mi.substring(i, i + 1)) == -1)
				return false;
		}
		/**
		 * Minutenangabe darf nur zwischen 0 und 59 liegen
		 */
		int minute = Integer.valueOf(mi);
		if ((minute < 0) || (minute > 59))
			return false;
		return true;
	}

	/**
	 * Testet Felder, die Double erwarten, ob Eingabe nur Zahlen und (optional)
	 * genau einen Punkt enthalten
	 * 
	 * @param s
	 * @return
	 */

	public static boolean regExDouble(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (".0123456789".indexOf(s.substring(i, i + 1)) == -1)
				return false;
		}
		int anz = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.substring(i, i + 1).equals("."))
				anz++;
		}
		if (anz > 1)
			return false;
//		if ((s.equals(".")))
//			return false;
		if (s.equals(""))
			return false;
		return true;
	}

	/**
	 * Testet Felder für Integereingaben, ob Eingabe nur Zahlen enthält
	 * 
	 * @param s
	 * @return
	 */

	public static boolean regExInt(String s) {
		for (int i = 0; i < s.length(); i++) {
			if ("-0123456789".indexOf(s.substring(i, i + 1)) == -1)
				return false;
		}
		for(int i = 1; i<s.length();i++){
			if("-".indexOf(s.substring(i,i+1))==0)
				return false;
		}
		int anz = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.substring(i, i + 1).equals("-"))
				anz++;
		}
		if (anz > 1)
			return false;

		if (s.equals(""))
			return false;
		return true;
	}
	
	public static boolean regExIntPos(String s) {
		for (int i = 0; i < s.length(); i++) {
			if ("0123456789".indexOf(s.substring(i, i + 1)) == -1)
				return false;
		}
		if (s.equals(""))
			return false;
		return true;
	}

	/**
	 * Testet, ob Zeitraum nur gültige Eingaben enthält(Zahlen, y(für
	 * year),m(month),d(day),h(hour) und -). - wird deshabl mitaufgenommen, da
	 * es manchmal sinnvoller ist, beispielsweise 3 d -1h anzugeben statt 2 d 23
	 * h(Sommer-/Winterzeitumstellung etc.).
	 * 
	 * @param s
	 * @return
	 */

	public static boolean regExPeriod(String s) {
		if (s.length() < 15)
			return false;
		for (int i = 0; i < s.length(); i++) {
			if ("-0123456789ymdh ".indexOf(s.substring(i, i + 1)) == -1)
				return false;
		}
		/**
		 * Es wird getestet, ob die Buchstaben y,m,d,h nur jeweils genau einmal
		 * vorkommen
		 */
		int anzY = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.substring(i, i + 1).equals("y"))
				anzY++;
		}
		if (anzY != 1)
			return false;

		int anzM = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.substring(i, i + 1).equals("m"))
				anzM++;
		}
		if (anzM != 1)
			return false;

		int anzD = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.substring(i, i + 1).equals("d"))
				anzD++;
		}
		if (anzD != 1)
			return false;

		int anzH = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.substring(i, i + 1).equals("h"))
				anzH++;
		}
		if (anzH != 1)
			return false;

		/**
		 * Testet, ob der Zeitraum in der richtigen Reihenfolge angegeben
		 * wurde(Jahre vor Monaten vor Tagen vor Stunden)
		 * 
		 * @param s
		 * @return
		 */

		int py;
		int pm;
		int pd;
		int ph;
		py = Integer.valueOf(s.indexOf("y"));
		pm = Integer.valueOf(s.indexOf("m"));
		pd = Integer.valueOf(s.indexOf("d"));
		ph = Integer.valueOf(s.indexOf("h"));
		if ((py > pm) || (py > pd) || (py > ph))
			return false;
		if ((pm > pd) || (pm > ph))
			return false;
		if (pd > ph)
			return false;
		return true;
	}

}
