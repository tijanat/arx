import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Erstellt Eingabefelder für Klasse ReplaceDictMasker
 * 
 * @author Kathrin
 *
 */

public class ConfigReplaceDictMasker {

	private Group group;
	private Label lblReplacementList;
	private Text txtReplacementList;
	private Button btnCancel;
	private Button btnOK;

	/**
	 * Methode, die ein Coposite erzeugt, in dem ein Textfeld erstellt wird
	 * 
	 * @param s1
	 * @param x
	 * @param y
	 */

	public ConfigReplaceDictMasker(Shell s, int x, int y) {
		group = new Group(s, SWT.SHADOW_IN | SWT.H_SCROLL | SWT.V_SCROLL);
		group.setText("ConfigReplaceDictMasker");
		GridLayout gridLayout = new GridLayout(2, true);
		group.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		group.setLayoutData(gridData);

		lblReplacementList = new Label(group, SWT.NONE);
		gridData = new GridData();
		lblReplacementList.setLayoutData(gridData);
		txtReplacementList = new Text(group, SWT.BORDER);
		gridData = new GridData();
		txtReplacementList.setLayoutData(gridData);

		setLabelText(lblReplacementList, txtReplacementList,
				"ReplacementList:", "1,2,3,4", 5);

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
