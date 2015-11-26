package org.deidentifier.arx.gui.masking;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.gui.view.def.IDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * @author Tijana
 *
 */
public class DialogMaskingSelection extends TitleAreaDialog implements IDialog {

	/**
	 * @param parentShell
	 */
	private Resources resources;
	
	public DialogMaskingSelection(Shell parentShell) {
		super(parentShell);
		this.setShellStyle(this.getShellStyle() | SWT.RESIZE);
		this.resources = new Resources(parentShell);
	}

	@Override
	public void create() {
		super.create();
		this.getShell().setSize(600, 600);
		this.getShell().setMinimumSize(600, 600);
		this.setTitle("Masking Method Selection");
   
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		
		parent.setLayout(new GridLayout(1, false));
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
	    tabFolder.setLayout(new GridLayout());
	    GridData resizableData = new GridData(GridData.FILL_BOTH);
	    tabFolder.setLayoutData(resizableData);
        
        TabItem stringTab = new TabItem(tabFolder, SWT.NONE);
        stringTab.setText("String");
        TabItem intTab = new TabItem(tabFolder, SWT.NONE);
        intTab.setText("Integer");
        TabItem decimalTab = new TabItem(tabFolder, SWT.NONE);
        decimalTab.setText("Decimal");
        TabItem dateTab = new TabItem(tabFolder, SWT.NONE); 
        dateTab.setText("Date");
 
		Composite stringControl = new StringTabControl(tabFolder, SWT.NONE, resources);
		stringTab.setControl(stringControl);
		
		Composite integerControl = new IntegerTabControl(tabFolder, SWT.NONE, resources);
		intTab.setControl(integerControl);
		
		Composite decimalControl = new DecimalTabControl(tabFolder, SWT.NONE, resources);
		decimalTab.setControl(decimalControl);
		
		Composite dateControl = new DateTabControl(tabFolder, SWT.NONE, resources);
		dateTab.setControl(dateControl);
//		
		return parent;
	}

}