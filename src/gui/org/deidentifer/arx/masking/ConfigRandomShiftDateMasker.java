package org.deidentifer.arx.masking;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Erstellt Eingabefelder für Klasse GenerateRandomShiftDateMasker
 * 
 * @author Kathrin
 *
 */
public class ConfigRandomShiftDateMasker extends ChangeableComposite {

	private boolean okShiftConstant=false;
	private boolean okPeriod=false;
	private boolean okDiscreteDistribution=false;
	private DiscreteDistributionSelection disc1;
	private Label lblShiftConstant;
	private Label lblPeriod;
	private Text txtShiftConstant;
	private Text txtPeriod;
	private Button btnCancel;
	private Button btnOK;

	/**
	 * Methode, die ein Coposite erzeugt, in dem zwei Eigabefelder und eine neue
	 * Composite für eine Verteilung erstellt werden
	 * 
	 * @param s1
	 * @param x
	 * @param y
	 */

	public ConfigRandomShiftDateMasker(Composite s, int x, int y) {
		super(s);
		//group = new Group(s, SWT.SHADOW_IN|SWT.H_SCROLL|SWT.V_SCROLL);
		group.setText("Random shift date");
		GridLayout gridLayout = new GridLayout(2,true);
		group.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		group.setLayoutData(gridData);

		lblShiftConstant = new Label(group, SWT.NONE);
		gridData = new GridData();
		lblShiftConstant.setLayoutData(gridData);
		txtShiftConstant = new Text(group, SWT.BORDER);
		gridData = new GridData();
		txtShiftConstant.setLayoutData(gridData);
		lblPeriod = new Label(group, SWT.NONE);
		gridData = new GridData();
		lblPeriod.setLayoutData(gridData);
		txtPeriod = new Text(group, SWT.BORDER);
		gridData = new GridData();
		txtPeriod.setLayoutData(gridData);

		setLabelText(lblShiftConstant,txtShiftConstant,"Shift constant:","0",
				0);
		setLabelText(lblPeriod, txtPeriod, "Period:", "05y 04m 03d 01h", 3);
		
		txtShiftConstant.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkShiftConstant();
				checkOK();
			}
		});
		
		txtPeriod.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkPeriod();
				checkOK();
			}
		});
		
		
		disc1 = new DiscreteDistributionSelection(group, 0);
		gridLayout = new GridLayout();
		gridLayout.verticalSpacing=0;
		gridData.grabExcessHorizontalSpace = true;
		disc1.setLayout(gridLayout);
		disc1.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkDiscreteDistribution();
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
		
		checkShiftConstant();
		checkDiscreteDistribution();
		checkPeriod();
		checkOK();
		hide();

	}
	
	private void checkDiscreteDistribution() {
		okDiscreteDistribution = disc1.getOK();
//		if(okDiscreteDistribution){
//			disc1.setForeground(disc1.getDisplay().getSystemColor(SWT.COLOR_BLACK));
//		}else{
//			disc1.setForeground(disc1.getDisplay().getSystemColor(SWT.COLOR_RED));
//		}
	}

	private void checkOK() {
		if (okShiftConstant && okDiscreteDistribution&&okPeriod) {
			btnOK.setText("OK");
			btnOK.setEnabled(true);
		} else {
			btnOK.setText("OK");
			btnOK.setEnabled(false);
		}
	}

	private void checkPeriod() {
		okPeriod = RegEx.regExPeriod(txtPeriod.getText());
		if(okPeriod){
			txtPeriod.setForeground(txtPeriod.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		}else{
			txtPeriod.setForeground(txtPeriod.getDisplay().getSystemColor(SWT.COLOR_RED));
		}
	}

	private void checkShiftConstant() {
		okShiftConstant = RegEx.regExInt(txtShiftConstant.getText());
		if(okShiftConstant){
			txtShiftConstant.setForeground(txtShiftConstant.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		}else{
			txtShiftConstant.setForeground(txtShiftConstant.getDisplay().getSystemColor(SWT.COLOR_RED));
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

	public void setLabelText(Label lbl1,final Text txt1, String lab1, String val1,
			int typ) {
		GridData gridData = new GridData();
		lbl1.setLayoutData(gridData);
		lbl1.setText(lab1);
		gridData = new GridData();
		gridData.grabExcessHorizontalSpace=true;
		gridData.horizontalAlignment=SWT.FILL;
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
