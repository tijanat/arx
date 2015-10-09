import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Erstellt Eingabefelder für Klasse ConstantShiftDateMasker
 * 
 * @author Kathrin
 *
 */
public class ConfigConstantShiftDateMasker {

	private boolean okShiftPeriod = false;
	private Button btnOK;
	private Button btnCancel;
	private Group group;
	private Label lblShiftPeriod;
	private Text txtShiftPeriod;
	private ResultConstantShiftDateMasker returnwert = new ResultConstantShiftDateMasker();

	/**
	 * Methode, die ein Coposite erzeugt, in dem ein Eigabefeld für ShiftPeriod
	 * erstellt wird
	 * 
	 * @param s1
	 * @param x
	 * @param y
	 */
	public ConfigConstantShiftDateMasker(Shell s,int x, int y) {
		group = new Group(s, SWT.SHADOW_IN | SWT.H_SCROLL | SWT.V_SCROLL);
		group.setText("ConfigConstantShiftDateMasker");
		GridLayout gridLayout = new GridLayout(2, true);
		group.setLayout(gridLayout);
		GridData gridData = new GridData(SWT.H_SCROLL | SWT.V_SCROLL);
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.horizontalSpan = 1;
		group.setLayoutData(gridData);
		group.computeSize(SWT.MAX, SWT.MAX);

		lblShiftPeriod = new Label(group, SWT.NONE);
		txtShiftPeriod = new Text(group, SWT.BORDER);

		setLabelText(lblShiftPeriod, txtShiftPeriod, "ShiftPeriod:",
				"05y 04m 03d 01h", 3);

		txtShiftPeriod.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkShiftPeriod();
				checkOK();
			}
		});

		
		btnCancel = new Button (group, SWT.PUSH);
		btnCancel.setText("Cancel");
		gridData = new GridData (GridData.FILL, GridData.END,true,true);
		
		btnCancel.setLayoutData(gridData);
		btnCancel.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				returnwert.setShiftPeriod(null);
				getResult(null);
//				System.out.println(returnwert.getShiftPeriod());
			}
		});
		btnOK = new Button(group, SWT.PUSH);
		btnOK.setText("OK");
		gridData = new GridData(GridData.FILL, GridData.END, true, true);
		btnOK.setLayoutData(gridData);
		btnOK.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				returnwert.setShiftPeriod(txtShiftPeriod.getText());
				getResult(returnwert.shiftPeriod);
//				System.out.println(returnwert.getShiftPeriod());
				
			}
		});
		
	
		
		checkShiftPeriod();

	}

	private void getResult(String i){
		System.out.println(i);
	}
	
	private void checkOK() {
		if (okShiftPeriod) {
			btnOK.setText("OK");
			btnOK.setEnabled(true);
		} else {
			btnOK.setText("OK");
			btnOK.setEnabled(false);
		}
	}

	private void checkShiftPeriod() {
		okShiftPeriod = RegEx.regExPeriod(txtShiftPeriod.getText());
		if(okShiftPeriod){
			txtShiftPeriod.setForeground(txtShiftPeriod.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		}else{
			txtShiftPeriod.setForeground(txtShiftPeriod.getDisplay().getSystemColor(SWT.COLOR_RED));
		}
	}

	/**
	 * Methode, die Labels + Textfelder für verschiedene Parameter erstellt. Mit
	 * int typ wird angegeben, ob Int, Double,Date,Period oder ein String
	 * erwartet wird => Aufruf RegEx
	 * 
	 * @param l1
	 * @param lx1
	 * @param lx2
	 * @param lx3
	 * @param lx4
	 * @param t1
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
