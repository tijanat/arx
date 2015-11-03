package org.deidentifer.arx.masking;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Erstellt Eingabefelder für Klasse ReplaceInstMasker
 * 
 * @author Kathrin
 *
 */

public class ConfigReplaceInstMasker extends ChangeableComposite {

	private Label lblReplacementValue;
	private Text txtReplacementValue;
	private Button btnCancel;
	private Button btnOK;

	/**
	 * Methode, die ein Coposite erzeugt, in dem ein Textfeld erstellt wird
	 * 
	 * @param s1
	 * @param x
	 * @param y
	 */

	public ConfigReplaceInstMasker(Composite s, int x, int y) {
		super(s);
	//	group = new Group(s, SWT.SHADOW_IN | SWT.H_SCROLL | SWT.V_SCROLL);
		group.setText("Replace instances");
		GridLayout gridLayout = new GridLayout(2, true);
		group.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		group.setLayoutData(gridData);

		lblReplacementValue = new Label(group, SWT.NONE);
		gridData = new GridData();
		lblReplacementValue.setLayoutData(gridData);
		txtReplacementValue = new Text(group, SWT.BORDER);
		gridData = new GridData();
		txtReplacementValue.setLayoutData(gridData);

		setLabelText(lblReplacementValue, txtReplacementValue,
				"Replacement value:", "0", 5);

		btnCancel = new Button (group, SWT.PUSH);
		btnCancel.setText("Cancel");
		gridData = new GridData (GridData.FILL, GridData.END,true,true);
		
		btnCancel.setLayoutData(gridData);
		btnOK = new Button(group, SWT.PUSH);
		btnOK.setText("OK");
		gridData = new GridData(GridData.FILL, GridData.END, true, true);
		btnOK.setLayoutData(gridData);
		hide();

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
