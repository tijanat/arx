import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Enthält alle kontinuierlichen Verteilungsfunkionen + jeweilig geforderte
 * Parameter
 * 
 * @author Kathrin
 *
 */

public class ContinousDistributionSelection extends Composite {

	private Combo cmb3;
	private Group group;
	private Label lblDistribution;
	private Label lblParam1;
	private Label lblParam2;
	private Label lblParam3;
	private Text txtParam1;
	private Text txtParam2;
	private Text txtParam3;
	private int txtType1;
	private int txtType2;
	private int txtType3;

	ContinousDistributionSelection(Composite c, int style) {
		super(c, style);
		group = new Group(c, SWT.SHADOW_IN | SWT.H_SCROLL | SWT.V_SCROLL);
		GridLayout gridLayout = new GridLayout(2, false);
		group.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		group.setLayoutData(gridData);

		lblDistribution = new Label(group, SWT.NONE);
		lblDistribution.setText("Distribution:");
		gridData.horizontalSpan = 2;
		lblDistribution.setLayoutData(gridData);
		/**
		 * Dropdownmenü zur Auswahl der verschiedenen Verteilungen wird erstellt
		 */
		cmb3 = new Combo(group, SWT.DROP_DOWN);
		cmb3.add("BetaDistribution");
		cmb3.add("CauchyDistribution");
		cmb3.add("ChiSquaredDistribution");
		cmb3.add("ConstantRealDistribution");
		cmb3.add("EnumeratedRealDistribution");
		cmb3.add("FDistribution");
		cmb3.add("GammaDistribution");
		cmb3.add("GumbelDistribution");
		cmb3.add("LaplaceDistribution");
		cmb3.add("LevyDistribution");
		cmb3.add("LogisticDistribution");
		cmb3.add("LogNormalDistribution");
		cmb3.add("NakagamiDistribution");
		cmb3.add("NormalDistribution");
		cmb3.add("ParetoDistribution");
		cmb3.add("TDistribution");
		cmb3.add("TriangularDistribution");
		cmb3.add("UniformRealDistribution");
		cmb3.add("WeilbullDistribution");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		cmb3.setLayoutData(gridData);
		cmb3.select(-1);

		lblParam1 = new Label(group, SWT.NONE);
		gridData = new GridData(GridData.BEGINNING, GridData.FILL, true, true);
		// gridData.grabExcessHorizontalSpace=true;
		gridData.horizontalSpan = 1;
		lblParam1.setLayoutData(gridData);

		txtParam1 = new Text(group, SWT.BORDER);
		gridData = new GridData(GridData.FILL, GridData.END, false, true);
		gridData.horizontalSpan = 1;
		txtParam1.setLayoutData(gridData);

		lblParam2 = new Label(group, SWT.NONE);
		gridData = new GridData(GridData.BEGINNING, GridData.FILL, true, true);
		lblParam2.setLayoutData(gridData);

		txtParam2 = new Text(group, SWT.BORDER);
		gridData = new GridData(GridData.FILL, GridData.END, false, true);
		txtParam2.setLayoutData(gridData);

		lblParam3 = new Label(group, SWT.NONE);
		gridData = new GridData(GridData.BEGINNING, GridData.FILL, true, true);
		lblParam3.setLayoutData(gridData);

		txtParam3 = new Text(group, SWT.BORDER);
		gridData = new GridData(GridData.FILL, GridData.END, false, true);
		txtParam3.setLayoutData(gridData);

		/**
		 * SelectionListener reagiert auf Auswahl und zeigt entsprechende Labels
		 * und Textfelder an
		 */

		this.cmb3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateDistributionType();
			
			}
		});
		cmb3.select(0);
		updateDistributionType();
	}

	private void updateDistributionType(){
		
		int j = cmb3.getSelectionIndex();
		if (j == 0) {
			txtType1 = 0;
			txtType2 = 1;
			txtType3 = 1;
			setLabelText(lblParam1, txtParam1, "Alpha:", "0");
			setLabelText(lblParam2, txtParam2, "Beta:", "0.0");
			setLabelText(lblParam3, txtParam3, "InverseCumAccuracy:",
					"0.0");
			System.out.println(txtType1);
			System.out.println(txtType2);
			System.out.println(txtType3);
		} else if (j == 1) {
			txtType1 = 1;
			txtType2 = 1;
			txtType3 = 1;
			setLabelText(lblParam1, txtParam1, "Median:", "1.0");
			setLabelText(lblParam2, txtParam2, "Scale:", "1.0");
			setLabelText(lblParam3, txtParam3, "InverseCumAccuracy:",
					"0.0");
		} else if (j == 2) {
			txtType1 = 0;
			txtType2 = 1;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1, "DegreesOfFreedom:",
					"5");
			setLabelText(lblParam2, txtParam2, "InverseCumAccuracy:",
					"0.0");
			setLabelText(lblParam3, txtParam3, "", "");
		} else if (j == 3) {
			txtType1 = 0;
			txtType2 = -1;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1, "Value:", "0");
			setLabelText(lblParam2, txtParam2, "", "");
			setLabelText(lblParam3, txtParam3, "", "");
		} else if (j == 4) {
			txtType1 = 0;
			txtType2 = 1;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1, "Singletons:", "10");
			setLabelText(lblParam2, txtParam2, "Probabilities:", "0.5");
			setLabelText(lblParam3, txtParam3, "", "");
		} else if (j == 5) {
			txtType1 = 1;
			txtType2 = 1;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1, "Mean:", "0.0");
			setLabelText(lblParam2, txtParam2, "InverseCumAccuracy:",
					"1.0");
			setLabelText(lblParam3, txtParam3, "", "");
		} else if (j == 6) {
			txtType1 = 0;
			txtType2 = 0;
			txtType3 = 0;
			setLabelText(lblParam1, txtParam1, "NumeratorDegreesOfFreedom:", "10");
			setLabelText(lblParam2, txtParam2, "DenumeratorDegreesOfFreedom:", "50");
			setLabelText(lblParam3, txtParam3, "InverseCumAccuracy:",
					"0");
		} else if (j == 7) {
			txtType1 = 0;
			txtType2 = 0;
			txtType3 = 0;
			setLabelText(lblParam1, txtParam1, "Shape:", "50");
			setLabelText(lblParam2, txtParam2, "Scale:", "2");
			setLabelText(lblParam3, txtParam3, "InverseCumAccuracy:",
					"0");
		}
		if (j == 8) {
			txtType1 = 0;
			txtType2 = 0;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1, "Mu:", "0");
			setLabelText(lblParam2, txtParam2, "Beta:", "0");
			setLabelText(lblParam3, txtParam3, "", "");
		} else if (j == 9) {
			txtType1 = 1;
			txtType2 = 1;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1, "Mu:", "1.0");
			setLabelText(lblParam2, txtParam2, "Beta:", "1.0");
			setLabelText(lblParam3, txtParam3, "", "");
		} else if (j == 10) {
			txtType1 = 0;
			txtType2 = 1;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1, "Mu:", "5");
			setLabelText(lblParam2, txtParam2, "C:", "0.0");
			setLabelText(lblParam3, txtParam3, "", "");
		} else if (j == 11) {
			txtType1 = 0;
			txtType2 = 0;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1, "Mu:", "0");
			setLabelText(lblParam2, txtParam2, "S:", "10");
			setLabelText(lblParam3, txtParam3, "", "");
		} else if (j == 12) {
			txtType1 = 1;
			txtType2 = 1;
			txtType3 = 1;
			setLabelText(lblParam1, txtParam1, "Scale:", "1.0");
			setLabelText(lblParam2, txtParam2, "Shape:", "0.5");
			setLabelText(lblParam3, txtParam3, "InverseCumAccuracy:",
					"0.0");
		} else if (j == 13) {
			txtType1 = 1;
			txtType2 = 1;
			txtType3 = 1;
			setLabelText(lblParam1, txtParam1, "Mu:", "0.0");
			setLabelText(lblParam2, txtParam2, "Omega:", "1.0");
			setLabelText(lblParam3, txtParam3, "InCumAccuracy:", "0.0");
		} else if (j == 14) {
			txtType1 = 0;
			txtType2 = 0;
			txtType3 = 0;
			setLabelText(lblParam1, txtParam1, "Mean:", "0");
			setLabelText(lblParam2, txtParam2, "Sd:", "1");
			setLabelText(lblParam3, txtParam3, "InverseCumAccuracy:",
					"0");
		} else if (j == 15) {
			txtType1 = 0;
			txtType2 = 0;
			txtType3 = 0;
			setLabelText(lblParam1, txtParam1, "Scale:", "1");
			setLabelText(lblParam2, txtParam2, "Shape:", "1");
			setLabelText(lblParam3, txtParam3, "InverseCumAccuracy:",
					"0");
		} else if (j == 16) {
			txtType1 = 0;
			txtType2 = 1;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1, "DegreesOfFreedom:",
					"0");
			setLabelText(lblParam2, txtParam2, "InverseCumAccuracy:",
					"0.0");
			setLabelText(lblParam3, txtParam3, "", "");
		} else if (j == 17) {
			txtType1 = 0;
			txtType2 = 1;
			txtType3 = 0;
			setLabelText(lblParam1, txtParam1, "Lower limit:", "10");
			setLabelText(lblParam2, txtParam2, "Upper limit:", "0.5");
			setLabelText(lblParam3, txtParam3, "Mode:", "0");
		} else if (j == 18) {
			txtType1 = 1;
			txtType2 = 1;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1, "Lower bounds:", "0.0");
			setLabelText(lblParam2, txtParam2, "Upper bounds:", "1.0");
			setLabelText(lblParam3, txtParam3, "", "");
		} else if (j == 19) {
			txtType1 = 0;
			txtType2 = 0;
			txtType3 = 0;
			setLabelText(lblParam1, txtParam1, "Shape:", "0");
			setLabelText(lblParam2, txtParam2, "Scale:", "0");
			setLabelText(lblParam3, txtParam3, "InverseCumAccuracy:",
					"0");
		}
		group.layout();
	}
	
	public void addModifyListener(ModifyListener modifyListener) {

		cmb3.addModifyListener(modifyListener);
		txtParam1.addModifyListener(modifyListener);
		txtParam2.addModifyListener(modifyListener);
		txtParam3.addModifyListener(modifyListener);

	}

	/**
	 * Rückgabe, ob Eingabe ok an aufrufende Klasse
	 * 
	 * @return
	 */

	public boolean getOK() {
		final boolean bol1;
		final boolean bol2;
		final boolean bol3;

		if (txtType1 == 0)
			bol1 = RegEx.regExInt(getText1());
		else if (txtType1 == 1)
			bol1 = RegEx.regExDouble(getText1());
		else
			bol1 = true;

		if (txtType2 == 0)
			bol2 = RegEx.regExInt(getText2());
		else if (txtType2 == 1)
			bol2 = RegEx.regExDouble(getText2());
		else
			bol2 = true;

		if (txtType3 == 0)
			bol3 = RegEx.regExInt(getText3());
		else if (txtType3 == 1)
			bol3 = RegEx.regExDouble(getText3());
		else
			bol3 = true;
		
		
		if (bol1) {
			System.out.println("Ich komme hierher true");
			txtParam1.setForeground(txtParam1.getDisplay().getSystemColor(
					SWT.COLOR_BLACK));

		} else {
			System.out.println("Ich komme hierher false");
			txtParam1.setForeground(txtParam1.getDisplay().getSystemColor(
					SWT.COLOR_RED));
		}
		if (bol2) {
			txtParam1.setForeground(txtParam1.getDisplay().getSystemColor(
					SWT.COLOR_BLACK));

		} else {
			txtParam1.setForeground(txtParam1.getDisplay().getSystemColor(
					SWT.COLOR_RED));
		}
		if (bol3) {
			txtParam1.setForeground(txtParam1.getDisplay().getSystemColor(
					SWT.COLOR_BLACK));

		} else {
			txtParam1.setForeground(txtParam1.getDisplay().getSystemColor(
					SWT.COLOR_RED));
		}

		return bol1 && bol2 && bol3;
		
		
	}

	/**
	 * Rückgabe der Eingabewerte an aufrufende Klasse
	 * 
	 * @return
	 */

	public String getText1() {
		return txtParam1.getText();
	}

	public String getText2() {
		return txtParam2.getText();
	}

	public String getText3() {
		return txtParam3.getText();
	}

	/**
	 * Erzeugt Label + Textfeld für Verteilung; mit int typ wird für jedes
	 * Textfeld der richtige RegEx aufgerufen(int=0,double=1)
	 */
	public void setLabelText(final Label lbl1, final Text txt1, String lab1,
			String val1) {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.minimumWidth = 10;
		lbl1.setLayoutData(gridData);
		lbl1.setText(lab1);
		gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		txt1.setLayoutData(gridData);
		txt1.setText(val1);

		/**
		 * Wenn das Label eines Parameter der Verteilung leer ist, werden das
		 * entsprechende Label und Textfeld nicht angezeigt (bsp.
		 * "ConstantRealDistribution" braucht nur einen Parameter,
		 * "BetaDistribution" braucht 3)
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
