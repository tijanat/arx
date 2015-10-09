package org.deidentifer.arx.masking;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Erstellt Eingabefelder für Klasse GenerateRandomDecimalMasker
 * 
 * @author Kathrin
 *
 */
public class ConfigGenerateRandomDecimalMasker {

	private boolean okShiftConstant = false;
	private boolean okContinousDistribution = false;
	private Button btnCancel;
	private Button btnOK;
	private ContinousDistributionSelection cont1;
	private Group group;
	private Label lblShiftConstant;
	private Text txtShiftConstant;

	/**
	 * Methode, die ein Coposite erzeugt, in dem ein Eigabefeld für
	 * ShiftConstant und eine neue Composite für eine Verteilung erstellt wird
	 * 
	 * @param s1
	 * @param x
	 * @param y
	 */
	public ConfigGenerateRandomDecimalMasker(Shell s, int x, int y) {
		group = new Group(s, SWT.SHADOW_IN | SWT.H_SCROLL | SWT.V_SCROLL);
		group.setText("ConfigGenerateRandomDecimalMasker");
		GridLayout gridLayout = new GridLayout(2, true);
		group.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		gridData.horizontalSpan = 1;
		group.setLayoutData(gridData);

		lblShiftConstant = new Label(group, SWT.NONE);
		gridData = new GridData();
		lblShiftConstant.setLayoutData(gridData);
		txtShiftConstant = new Text(group, SWT.BORDER);
		gridData = new GridData();
		txtShiftConstant.setLayoutData(gridData);

		setLabelText(lblShiftConstant, txtShiftConstant, "ShiftConstant:",
				"0.0", 1);

		txtShiftConstant.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkShiftConstant();
				checkOK();
			}
		});

		cont1 = new ContinousDistributionSelection(group, 0);
		gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridData.grabExcessHorizontalSpace = true;
		cont1.setLayout(gridLayout);

		cont1.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkContinousDistribution();
				checkOK();
			}
		});

		btnCancel = new Button(group, SWT.PUSH);
		btnCancel.setText("Cancel");
		gridData = new GridData(GridData.FILL, GridData.END, true, true);

		btnCancel.setLayoutData(gridData);
		btnOK = new Button(group, SWT.PUSH);
		btnOK.setText("OK");
		gridData = new GridData(GridData.FILL, GridData.END, true, true);
		btnOK.setLayoutData(gridData);

		checkShiftConstant();
		checkContinousDistribution();
		checkOK();
	}

	private void checkContinousDistribution() {
		okContinousDistribution = cont1.getOK();
		if (okContinousDistribution) {
			cont1.setForeground(cont1.getDisplay().getSystemColor(
					SWT.COLOR_BLACK));
		} else {
			cont1.setForeground(cont1.getDisplay()
					.getSystemColor(SWT.COLOR_RED));
		}
	}

	private void checkOK() {
		if (okShiftConstant && okContinousDistribution) {
			btnOK.setText("OK");
			btnOK.setEnabled(true);
		} else {
			btnOK.setText("OK");
			btnOK.setEnabled(false);
		}
	}

	private void checkShiftConstant() {
		okShiftConstant = RegEx.regExDouble(txtShiftConstant.getText());
		if (okShiftConstant) {
			txtShiftConstant.setForeground(txtShiftConstant.getDisplay()
					.getSystemColor(SWT.COLOR_BLACK));
		} else {
			txtShiftConstant.setForeground(txtShiftConstant.getDisplay()
					.getSystemColor(SWT.COLOR_RED));
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
	// public void getResult(){
	// shiftConstant=Double.parseDouble(txtShiftConstant.getText());
	// }
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
