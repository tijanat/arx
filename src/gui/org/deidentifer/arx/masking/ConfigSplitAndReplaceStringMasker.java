package org.deidentifer.arx.masking;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Erstellt Eingabefelder für Klasse SplitAndReplaceStringMasker
 * 
 * @author Kathrin
 *
 */
public class ConfigSplitAndReplaceStringMasker {

	private boolean okReplaceGroup = false;
	private Group group;
	private Label lblSplitAtOccurrenceOf;
	private Label lblReplacementString;
	private Label lblReplaceGroup;
	private Label lblReplaceEachChar;
	private Text txtSplitAtOccurrenceOf;
	private Text txtReplacementString;
	private Text txtReplaceGroup;
	private Button btnCancel;
	private Button btnOK;
	private Button btnReplaceEchChar;

	/**
	 * Methode, die ein Coposite erzeugt, in dem drei Textfelder und eine
	 * Checkbox erstellt werden
	 * 
	 * @param s1
	 * @param x
	 * @param y
	 */

	public ConfigSplitAndReplaceStringMasker(Composite s, int x, int y) {
		group = new Group(s, SWT.SHADOW_IN | SWT.H_SCROLL | SWT.V_SCROLL);
		group.setText("Split and Replace String");
		GridLayout gridLayout = new GridLayout(2, false);
		group.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		group.setLayoutData(gridData);

		lblSplitAtOccurrenceOf = new Label(group, SWT.NONE);
		txtSplitAtOccurrenceOf = new Text(group, SWT.BORDER);
		lblReplacementString = new Label(group, SWT.NONE);
		txtReplacementString = new Text(group, SWT.BORDER);
		lblReplaceGroup = new Label(group, SWT.NONE);
		txtReplaceGroup = new Text(group, SWT.BORDER);

		lblReplaceEachChar = new Label(group, SWT.NONE);
		lblReplaceEachChar.setText("Replace each character?");
		gridData = new GridData();
		lblReplaceEachChar.setLayoutData(gridData);
		btnReplaceEchChar = new Button(group, SWT.CHECK);
		gridData = new GridData();
		btnReplaceEchChar.setLayoutData(gridData);

		setLabelText(lblSplitAtOccurrenceOf, txtSplitAtOccurrenceOf,
				"Split at occurrences of:", "@", 5);
		setLabelText(lblReplacementString, txtReplacementString,
				"Replacement String:", "*", 5);
		setLabelText(lblReplaceGroup, txtReplaceGroup,
				"Replace group with index:", "0", 0);

		txtReplaceGroup.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkInt();
				checkOK();
			}
		});

		btnCancel = new Button (group, SWT.PUSH);
		btnCancel.setText("Cancel");
		gridData = new GridData (GridData.FILL, GridData.END,true,true);
		
		btnCancel.setLayoutData(gridData);
		btnOK = new Button(group, SWT.PUSH);
		btnOK.setText("OK");
		gridData = new GridData(GridData.FILL, GridData.END, true, true);
		btnOK.setLayoutData(gridData);
	}

	private void checkInt() {
		okReplaceGroup = RegEx.regExIntPos(txtReplaceGroup.getText());
	}

	private void checkOK() {
		if (okReplaceGroup) {
			btnOK.setText("OK");
			btnOK.setEnabled(true);
		} else {
			btnOK.setText("OK");
			btnOK.setEnabled(false);
		}
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
	public void hide(){
		 ((GridData)group.getLayoutData()).exclude = true;
		group.setVisible(false);  
	}
	
	public void show(){
		((GridData)group.getLayoutData()).exclude = false; 
		group.setVisible(true);
	}
}
