import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TestShell {

	public static void main(String[] args) {
		new TestShell();
	}
	Text name;
	Text nummer;
	Label lbl1;

	Label lbl2;

	TestShell() {
		Display d = new Display();
		final Shell s = new Shell(d);
		s.setText("Das ist eine neue Shell");
		
		GridLayout gridLayout = new GridLayout(5,true);
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.horizontalSpacing=10;
		gridLayout.verticalSpacing=10;
		s.setLayout(gridLayout);
		GridData gridData = new GridData();
		s.setLayoutData(gridData);
		s.computeSize(SWT.MAX,SWT.MAX);
		

		
		ConfigConstantShiftDateMasker g1 = new ConfigConstantShiftDateMasker(s,400,50);

		ConfigConstantShiftDecimalMasker g2 = new ConfigConstantShiftDecimalMasker(
				s,400,50);

		ConfigGenerateRandomDataMasker g3 = new ConfigGenerateRandomDataMasker(
				s, 400, 50);

		ConfigGenerateRandomDecimalMasker g4 = new ConfigGenerateRandomDecimalMasker(
				s, 400, 50);

		ConfigGenerateRandomIntegerDecimalMasker g5 = new ConfigGenerateRandomIntegerDecimalMasker(
				s, 400, 50);

		ConfigGenerateRandomStringMasker g6 = new ConfigGenerateRandomStringMasker(
				s, 400, 50);

		ConfigMatchAndReplaceStringMasker g7 = new ConfigMatchAndReplaceStringMasker(
				s, 400, 50);

		ConfigRandomShiftDateMasker g8 = new ConfigRandomShiftDateMasker(s,
				400, 50);

		ConfigRandomShiftDecimalMasker g9 = new ConfigRandomShiftDecimalMasker(
				s, 400, 50);

		ConfigReplaceDictMasker g10 = new ConfigReplaceDictMasker(s, 400, 50);

		ConfigReplaceInstMasker g11 = new ConfigReplaceInstMasker(s, 400, 50);

		ConfigSplitAndReplaceStringMasker g12 = new ConfigSplitAndReplaceStringMasker(
				s, 400, 50);


		s.open();
		while (!s.isDisposed()) {
			if (!d.readAndDispatch())
				d.sleep();
		}
		d.dispose();

	}
}
