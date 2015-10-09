package org.deidentifer.arx.masking;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Erstellt Eingabefelder für Klasse MatchAndReplaceStringMasker
 * 
 * @author Kathrin
 *
 */
public class ConfigMatchAndReplaceStringMasker {

	private Group group;
	private Label lblRegExPattern;
	private Label lblReplacementString;
	private Label lblReplacingAllMatches;
	private Label lblReplacingAllChars;
	private Text txtRegExPattern;
	private Text txtReplacementString;
	private Button btnCancel;
	private Button btnOK;
	private Button btnReplacingAllMatches;
	private Button btnReplacingAllChars;

	/**
	 * Methode, die ein Coposite erzeugt, in dem zwei Eigabefelder und zwei
	 * Checkboxen erstellt werden
	 * 
	 * @param s1
	 * @param x
	 * @param y
	 */
	public ConfigMatchAndReplaceStringMasker(Shell s, int x, int y) {
		group = new Group(s, SWT.SHADOW_IN | SWT.H_SCROLL | SWT.V_SCROLL);
		group.setText("ConfigMatchAndReplaceStringMasker");
		GridLayout gridLayout = new GridLayout(2, true);
		group.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		group.setLayoutData(gridData);

		lblRegExPattern = new Label(group, SWT.NONE);
		gridData = new GridData();
		lblRegExPattern.setLayoutData(gridData);
		txtRegExPattern = new Text(group, SWT.BORDER);
		gridData = new GridData();
		txtRegExPattern.setLayoutData(gridData);

		lblReplacementString = new Label(group, SWT.NONE);
		gridData = new GridData();
		lblReplacementString.setLayoutData(gridData);
		txtReplacementString = new Text(group, SWT.BORDER);
		gridData = new GridData();
		txtReplacementString.setLayoutData(gridData);

		lblReplacingAllMatches = new Label(group, SWT.NONE);
		lblReplacingAllMatches.setText("Replace all matches?");
		gridData = new GridData();
		lblReplacingAllMatches.setLayoutData(gridData);
		gridData = new GridData();
		btnReplacingAllMatches = new Button(group, SWT.CHECK);
		btnReplacingAllMatches.setLayoutData(gridData);

		lblReplacingAllChars = new Label(group, SWT.NONE);
		lblReplacingAllChars.setText("Replace all characters?");
		gridData = new GridData();
		lblReplacingAllChars.setLayoutData(gridData);
		btnReplacingAllChars = new Button(group, SWT.CHECK);
		gridData = new GridData();
		btnReplacingAllChars.setLayoutData(gridData);

		setLabelText(lblRegExPattern, txtRegExPattern, "RegExPattern:",
				"^.{0,5}", 5);
		setLabelText(lblReplacementString, txtReplacementString,
				"ReplacementString:", "*", 5);

		btnCancel = new Button (group, SWT.PUSH);
		btnCancel.setText("Cancel");
		gridData = new GridData (GridData.FILL, GridData.END,true,true);
		
		btnCancel.setLayoutData(gridData);
		btnOK = new Button(group, SWT.PUSH);
		btnOK.setText("OK");
		gridData = new GridData(GridData.FILL, GridData.END, true, true);
		btnOK.setLayoutData(gridData);

	}

	/**
	 * Methode, die Labels + Textfelder für verschiedene Parameter erstellt. Mit
	 * int typ wird angegeben, ob Int, Double,Date,Period oder ein String
	 * erwartet wird => Aufruf RegEx
	 * 
	 * @param lbl1
	 * @param lx1
	 * @param lx2
	 * @param lx3
	 * @param lx4
	 * @param txt1
	 * @param tx1
	 * @param tx2
	 * @param tx3
	 * @param tx4
	 * @param lab1
	 * @param val1
	 * @param typ
	 */

	public void setLabelText(Label lbl1, final Text txt1, String lab1,
			String val1, int typ) {
		GridData gridData = new GridData();
		lbl1.setLayoutData(gridData);
		lbl1.setText(lab1);
		gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		txt1.setLayoutData(gridData);
		txt1.setText(val1);
		
		if (lab1 == "") {
			lbl1.setVisible(false);
			txt1.setVisible(false);
		} else {
			lbl1.setVisible(true);
			txt1.setVisible(true);
		}
	}

}
