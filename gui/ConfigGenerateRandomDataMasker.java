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
 * Erstellt Eingabefelder für Klasse GenerateRandomDataMasker
 * 
 * @author Kathrin
 *
 */

public class ConfigGenerateRandomDataMasker {

	private boolean okDateTime = false;
	private boolean okPeriod = false;
	private boolean okShiftConstant = false;
	private boolean okDiscreteDistribution = false;
	private Button btnCancel;
	private Button btnOK;
	private DiscreteDistributionSelection disc1;
	private Group group;
	private Label lblShiftConstant;
	private Label lblDateTime;
	private Label lblPeriod;
	private Text txtShiftConstant;
	private Text txtDateTime;
	private Text txtPeriod;

	/**
	 * Methode, die ein Coposite erzeugt, in dem ein Eigabefeld für
	 * ShiftConstant,DateTime, Period und noch ein Composite für eine
	 * Distribution erstellt wird
	 * 
	 * @param s1
	 * @param x
	 * @param y
	 */
	public ConfigGenerateRandomDataMasker(Shell s, int x, int y) {
		group = new Group(s, SWT.SHADOW_IN | SWT.H_SCROLL | SWT.V_SCROLL);
		group.setText("ConfigGenerateRandomDataMasker");
		GridLayout gridLayout = new GridLayout(2, true);
		gridLayout.verticalSpacing = 0;
		group.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		gridData.grabExcessVerticalSpace = true;
		// gridData.horizontalSpan = 1;
		group.setLayoutData(gridData);

		lblShiftConstant = new Label(group, SWT.NONE);
		txtShiftConstant = new Text(group, SWT.BORDER);
		lblDateTime = new Label(group, SWT.NONE);
		txtDateTime = new Text(group, SWT.BORDER);
		lblPeriod = new Label(group, SWT.NONE);
		txtPeriod = new Text(group, SWT.BORDER);

		setLabelText(lblShiftConstant, txtShiftConstant, "ShiftConstant:", "0",
				0);
		setLabelText(lblDateTime, txtDateTime, "DateTime:", "2015-06-03 20:15",
				2);
		setLabelText(lblPeriod, txtPeriod, "Period:", "05y 04m 03d 01h", 3);

		txtShiftConstant.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkShiftConstant();
				checkOK();
			}
		});
		txtDateTime.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkDateTime();
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
		gridLayout.verticalSpacing = 0;
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
		checkDateTime();
		checkPeriod();
		checkDiscreteDistribution();
		checkOK();
	}

	private void checkDateTime() {
		okDateTime = RegEx.regExDate(txtDateTime.getText());
		if(okDateTime){
			txtDateTime.setForeground(txtDateTime.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		}else{
			txtDateTime.setForeground(txtDateTime.getDisplay().getSystemColor(SWT.COLOR_RED));
		}
	}

	private void checkDiscreteDistribution() {
		okDiscreteDistribution = disc1.getOK();
	}

	private void checkOK() {
		if (okShiftConstant && okDateTime && okPeriod && okDiscreteDistribution) {
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
