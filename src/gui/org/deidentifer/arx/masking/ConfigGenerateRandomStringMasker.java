package org.deidentifer.arx.masking;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 * Erstellt Eingabefelder für Klasse GenerateRandomStringMasker
 * 
 * @author Kathrin
 *
 */
public class ConfigGenerateRandomStringMasker extends ChangeableComposite {

	private boolean okInt = false;
	private Label lblInt;
	private Label lblBooleanAlphabetic;
	private Label lblBooleanNumeric;
	private Label lblChar;
	private Label lblU;
	private Text txtInt;
	private Text txtChar;
	private List listFeld;
	private Button btnCancel;
	private Button btnOK;
	private Button btnAdd;
	private Button btnDel;
	private Button btnAlphabetic;
	private Button btnNumeric;

	/**
	 * Methode, die ein Coposite erzeugt, in dem zwei Eigabefelder und zwei
	 * Checkboxen erstellt werden
	 * 
	 * @param s1
	 * @param x
	 * @param y
	 */
	public ConfigGenerateRandomStringMasker(Composite s, int x, int y) {
		super(s);
	//	group = new Group(s, SWT.SHADOW_IN | SWT.H_SCROLL | SWT.V_SCROLL);
		group.setText("Generate random string");
		GridLayout gridLayout = new GridLayout(2, true);
		group.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		gridData.horizontalSpan = 1;
		group.setLayoutData(gridData);

		lblInt = new Label(group, SWT.NONE);
		lblInt.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER,
				false, false));
		txtInt = new Text(group, SWT.BORDER);
		txtInt.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				false, false));

		setLabelText(lblInt, txtInt, "Integer:", "-1", 0);

		lblBooleanAlphabetic = new Label(group, SWT.NONE);
		lblBooleanAlphabetic.setText("Alphabetic characters?");
		lblBooleanAlphabetic.setLayoutData(new GridData(GridData.FILL,
				GridData.CENTER, false, false));
		btnAlphabetic = new Button(group, SWT.CHECK);
		btnAlphabetic.setLayoutData(new GridData(GridData.FILL,
				GridData.CENTER, true, true));

		lblBooleanNumeric = new Label(group, SWT.NONE);
		lblBooleanNumeric.setText("Numeric characters?");
		lblBooleanNumeric.setLayoutData(new GridData(GridData.FILL,
				GridData.CENTER, false, false));
		btnNumeric = new Button(group, SWT.CHECK);
		btnNumeric.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				true, true));

		lblChar = new Label(group, SWT.NONE);
		lblChar.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				false, false));
		lblChar.setText("Char[]");

		listFeld = new List(group, SWT.BORDER | SWT.V_SCROLL
				| SWT.SCROLLBAR_OVERLAY);
		listFeld.setLayoutData(gridData);

		txtChar = new Text(group, SWT.BORDER);
		txtChar.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				false, false));
		txtChar.setTextLimit(1);

		btnAdd = new Button(group, SWT.NONE);
		gridData = new GridData(GridData.END, GridData.FILL, false, true);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 1;
		btnAdd.setLayoutData(gridData);
		btnAdd.setText("ADD");
		btnAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				listFeld.add(txtChar.getText());
				btnDel.setEnabled(true);

			}
		});

		lblU = new Label(group, SWT.NONE);
		lblU.setSize(50,50);
		btnDel = new Button(group, SWT.NONE);
		gridData = new GridData(GridData.END, GridData.FILL, false, true);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 1;
		btnDel.setLayoutData(gridData);
		btnDel.setText("DEL");
		btnDel.setEnabled(false);
		btnDel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				if (listFeld.getSelectionIndex() >= 1) {
					btnDel.setEnabled(true);
					listFeld.remove(listFeld.getSelectionIndex());
				} else {
					btnDel.setEnabled(false);
					listFeld.remove(listFeld.getSelectionIndex());
				}
			}
		});

		txtInt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkInt();
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
		hide();

	}

	private void checkInt() {
		okInt = RegEx.regExInt(txtInt.getText());
		if (okInt) {
			txtInt.setForeground(txtInt.getDisplay().getSystemColor(
					SWT.COLOR_BLACK));
		} else {
			txtInt.setForeground(txtInt.getDisplay().getSystemColor(
					SWT.COLOR_RED));
		}
	}

	private void checkOK() {
		if (okInt) {
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
}
