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
 * Enthält alle diskreten Verteilungsfunkionen + jeweilig geforderte Parameter
 * 
 * @author Kathrin
 *
 */
public class DiscreteDistributionSelection extends Composite {

	private Combo cmb2;
	private Group group;
	private Label lblParam1;
	private Label lblParam2;
	private Label lblParam3;
	private Label lblDistribution;
	private Text txtParam1;
	private Text txtParam2;
	private Text txtParam3;
	private int txtType1;
	private int txtType2;
	private int txtType3;

	DiscreteDistributionSelection(Composite c, int style) {
		super(c, style);
		group = new Group(c, SWT.SHADOW_IN | SWT.H_SCROLL | SWT.V_SCROLL);
		GridLayout gridLayout = new GridLayout(2, true);
		group.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		group.setLayoutData(gridData);

		lblDistribution = new Label(group, SWT.NONE);
		lblDistribution.setText("Distribution");
		gridData.horizontalSpan = 2;
		lblDistribution.setLayoutData(gridData);

		/**
		 * Dropdownmenü für Auswahl Verteilungen wird erstellt
		 */

		cmb2 = new Combo(group, SWT.DROP_DOWN);
		cmb2.add("BinomialDistribution");
		cmb2.add("EnumeratedIntegerDistribution");
		cmb2.add("GeometricDistribution");
		cmb2.add("HypergeometricDistribution");
		cmb2.add("PascalDistribution");
		cmb2.add("PoissonDistribution");
		cmb2.add("UniformIntegerDistribution");
		cmb2.add("ZipfDistribution");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		cmb2.setLayoutData(gridData);
		cmb2.select(-1);

		lblParam1 = new Label(group, SWT.NONE);
		gridData = new GridData(GridData.BEGINNING, GridData.FILL, true, false);
		// gridData.grabExcessHorizontalSpace=true;
		gridData.horizontalSpan = 1;
		lblParam1.setLayoutData(gridData);

		txtParam1 = new Text(group, SWT.BORDER);
		gridData = new GridData(GridData.FILL, GridData.END, false, true);
		gridData.horizontalSpan = 1;
		txtParam1.setLayoutData(gridData);

		lblParam2 = new Label(group, SWT.NONE);
		gridData = new GridData(GridData.BEGINNING, GridData.FILL, true, false);
		lblParam2.setLayoutData(gridData);

		txtParam2 = new Text(group, SWT.BORDER);
		gridData = new GridData(GridData.FILL, GridData.END, false, true);
		txtParam2.setLayoutData(gridData);

		lblParam3 = new Label(group, SWT.NONE);
		gridData = new GridData(GridData.BEGINNING, GridData.FILL, true, false);
		lblParam3.setLayoutData(gridData);

		txtParam3 = new Text(group, SWT.BORDER);
		gridData = new GridData(GridData.FILL, GridData.END, false, true);
		txtParam3.setLayoutData(gridData);

		cmb2.select(-1);

		/**
		 * SelectionListener reagiert auf Auswahl und zeigt entsprechende Labels
		 * und Textfelder an
		 */
		cmb2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateDistributionType();
			}
			});

		cmb2.select(0);
		updateDistributionType();
	}

	public void addModifyListener(ModifyListener modifyListener) {
		cmb2.addModifyListener(modifyListener);
		txtParam1.addModifyListener(modifyListener);
		txtParam2.addModifyListener(modifyListener);
		txtParam3.addModifyListener(modifyListener);

	}

	/**
	 * Rückgabe ob Eingabe ok an aufrufende Klasse
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

	private void updateDistributionType(){
		int j = cmb2.getSelectionIndex();
		if (j == 0) {
			txtType1 = 0;
			txtType2 = 1;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1, "Number of trials:",
					"0");
			setLabelText(lblParam2, txtParam2,
					"Probability of success:", "0.0");
			setLabelText(lblParam3, txtParam3, "", "");

		} else if (j == 1) {
			txtType1 = 1;
			txtType2 = 1;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1,
					"Probability mass function:", "1.0");
			setLabelText(lblParam2, txtParam2,
					"Probability mass function:", "2.0");
			setLabelText(lblParam3, txtParam3, "", "");
		} else if (j == 2) {
			txtType1 = 0;
			txtType2 = -1;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1,
					"Probability of success:", "50");
			setLabelText(lblParam2, txtParam2, "", "");
			setLabelText(lblParam3, txtParam3, "", "");
		} else if (j == 3) {
			txtType1 = 0;
			txtType2 = 0;
			txtType3 = 0;
			setLabelText(lblParam1, txtParam1, "Population size:",
					"500");
			setLabelText(lblParam2, txtParam2,
					"Number of successes in population:", "150");
			setLabelText(lblParam3, txtParam3, "Sample size:", "250");

		} else if (j == 4) {
			txtType1 = 0;
			txtType2 = 1;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1, "Number of successes:",
					"0");
			setLabelText(lblParam2, txtParam2,
					"Probability of success:", "0.0");
			setLabelText(lblParam3, txtParam3, "", "");

		} else if (j == 5) {
			txtType1 = 1;
			txtType2 = 1;
			txtType3 = 0;
			setLabelText(lblParam1, txtParam1, "Mean:", "0.0");
			setLabelText(lblParam2, txtParam2, "Convergence:", "1.0");
			setLabelText(lblParam3, txtParam3,
					"Max Number of iterations:", "50");

		} else if (j == 6) {
			txtType1 = 0;
			txtType2 = 0;
			txtType3 = -1;

			setLabelText(lblParam1, txtParam1, "Lower bounds:", "10");
			setLabelText(lblParam2, txtParam2, "Upper bounds:", "50");
			setLabelText(lblParam3, txtParam3, "", "");
		} else if (j == 7) {
			txtType1 = 0;
			txtType2 = 0;
			txtType3 = -1;
			setLabelText(lblParam1, txtParam1, "Number of elements:",
					"50");
			setLabelText(lblParam2, txtParam2, "Exponent:", "2");
			setLabelText(lblParam3, txtParam3, "", "");

		}
		group.layout();		
	}
	
	public void setLabelText(Label lbl1, final Text txt1, String lab1,
			String val1) {
		GridData gridData = new GridData();
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
		 * "GeometricDistribution" braucht nur einen Parameter,
		 * "HypergeometricDistribution" braucht 3)
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
