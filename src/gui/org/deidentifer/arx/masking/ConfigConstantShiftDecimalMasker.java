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
import org.eclipse.swt.widgets.Text;

/**
 * Erstellt Eingabefelder für Klasse ConstantShiftDecimalMasker
 * 
 * @author Kathrin
 *
 */

public class ConfigConstantShiftDecimalMasker extends ChangeableComposite{

	private boolean okShiftDistance = false;
	private Button btnCancel;
	private Button btnOK;
	protected Label lblShiftDistance;
	protected Text txtShiftDistance;
	public double shiftDistance;
	public ResultConstantShiftDecimalMasker returnwert = new ResultConstantShiftDecimalMasker();

	/**
	 * Methode, die ein Coposite erzeugt, in dem ein Eigabefeld für
	 * ShiftDistance erstellt wird
	 * 
	 * @param s1
	 * @param x
	 * @param y
	 */
	public ConfigConstantShiftDecimalMasker(Composite s,int x, int y) {
		super(s);
	    
		group.setText("Constant shift decimal");
		GridLayout gridLayout = new GridLayout(2, true);
		group.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		group.setLayoutData(gridData);

		lblShiftDistance = new Label(group, SWT.NONE);
		txtShiftDistance = new Text(group, SWT.BORDER);

		setLabelText(lblShiftDistance, txtShiftDistance, "Shift distance:",
				"0.0", 1);

		txtShiftDistance.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkShiftDistance();
				checkOK();
			}
		});

		btnCancel = new Button (group, SWT.PUSH);
		btnCancel.setText("Cancel");
		gridData = new GridData (GridData.FILL, GridData.END,true,true);
		
		btnCancel.setLayoutData(gridData);
		btnCancel.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				getResult(null);
		}
		});
		
		btnOK = new Button(group, SWT.PUSH);
		btnOK.setText("OK");
		gridData = new GridData(GridData.FILL, GridData.END, true, true);
		btnOK.setLayoutData(gridData);
		btnOK.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				returnwert.setShiftDistance(Double.parseDouble((txtShiftDistance.getText())));
				getResult(Double.toString(returnwert.getShiftDistance()));
				//System.out.println(returnwert.getShiftDistance());
				
			}
		});
		hide();

		
	}
	private void getResult(String i){
		System.out.println(i);
	}

	private void checkOK() {
		if (okShiftDistance) {
			btnOK.setText("OK");
			btnOK.setEnabled(true);
		} else {
			btnOK.setText("OK");
			btnOK.setEnabled(false);
		}
	}

	private void checkShiftDistance() {
		okShiftDistance = RegEx.regExDouble(txtShiftDistance.getText());
		if(okShiftDistance){
			txtShiftDistance.setForeground(txtShiftDistance.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		}else{
			txtShiftDistance.setForeground(txtShiftDistance.getDisplay().getSystemColor(SWT.COLOR_RED));
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
		gridData.horizontalAlignment = GridData.FILL;
		txt1.setLayoutData(gridData);
		txt1.setText(val1);
		
		/**
		 * Wenn das Label eines Parameter der Verteilung leer ist, werden das
		 * entsprechende Label und Textfeld nicht angezeigt
		 * 
		 */
		if (lab1 == "") {
			lbl1.setVisible(false);
			txt1.setVisible(false);
		} else {
			lbl1.setVisible(true);
			txt1.setVisible(true);
		}
	}

}
