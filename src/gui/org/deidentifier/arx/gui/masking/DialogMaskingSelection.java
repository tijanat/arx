/**
 * 
 */
package org.deidentifier.arx.gui.masking;
import org.deidentifer.arx.masking.ConfigGenerateRandomDataMasker;
import org.deidentifier.arx.gui.view.def.IDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author Tijana
 *
 */
public class DialogMaskingSelection extends TitleAreaDialog implements IDialog {

	private Tree tree;
	private Composite rightColumn;
	private ConfigGenerateRandomDataMasker configGenerateRandomData;

	/**
	 * @param parentShell
	 */
	public DialogMaskingSelection(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {

        parent.setLayout(new GridLayout(2, false));

        Composite leftColumn = new Composite(parent, SWT.NONE);
        leftColumn.setLayout(new GridLayout(1, false));
        GridData leftData = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1);
        leftData.minimumHeight = 300;
        leftColumn.setLayoutData(leftData);    
         
        String[] topLevel = new String[]{"Generate Random Value", "Replace", "Shift", "Shuffle"};
        
        tree = new Tree(leftColumn, SWT.BORDER | SWT.V_SCROLL);  
        GridData treeData = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
        treeData.minimumHeight = 400;
        tree.setLayoutData(treeData);
        tree.setHeaderVisible(true);
        tree.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				TreeItem selectedItems[] = tree.getSelection();
				if (selectedItems.length == 1){
					TreeItem selected = selectedItems[0];
					int indexFrom = selected.toString().indexOf('{')+1;
					int length = selected.toString().length()-1;
					String name = selected.toString().substring(indexFrom, length);
					
					if(name.equals("Data")){
						configGenerateRandomData.show();
					} else {
						configGenerateRandomData.hide();
					}

					
					System.out.println("selectedItem " + name);
				}				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
        
        TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
        column1.setText("Masking Method");
        column1.setWidth(200);
   
		for (int i = 0; i < topLevel.length; i++) {
			String parentItem = topLevel[i];
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(parentItem);
			
			String[] subItems;
			switch (i){
				case 0: //Generate Random Value
					subItems = new String[]{"Data", "Decimal", "Integer Decimal", "String"};
				break;
				case 1: // Replace
					subItems = new String[]{"Split & Replace String", "Dictionary", "Instances", "Match & Replace String"};
				break;
				case 2: //Shift
					subItems = new String[]{"Date", "Decimal"};
				break;
				default:
					subItems = new String[]{};
				break;
			}
			
			for (int j = 0; j < subItems.length; j++) {
				String subItemString = subItems[j];
				
				TreeItem subItem = new TreeItem(item, SWT.NONE);
				subItem.setText(subItemString);
				
				if (i==2){ //Shit (i==3) needs 2 levels
					String[] subSubItems = new String[]{"Random", "Constant"};
					new TreeItem(subItem, SWT.NONE).setText(subSubItems[0]);
					new TreeItem(subItem, SWT.NONE).setText(subSubItems[1]);

				}
			}
			
		}
  
		rightColumn = new Composite(parent, SWT.RIGHT | SWT.BORDER);
		rightColumn.setLayout(new GridLayout(1, false));
        GridData rightData = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1);
        rightData.minimumHeight = 300;
        rightData.minimumWidth = 600;
        rightColumn.setLayoutData(rightData);   
        
		configGenerateRandomData = new ConfigGenerateRandomDataMasker(rightColumn, 0, 0);
		configGenerateRandomData.hide();
		
		//TreeItem selectedItems[] = tree.getSelection();
		//if (selectedItems.length > 0){

		//}
		
		return parent;
	}

	@Override
	public void create() {
		super.create();
		this.setTitle("Masking Method Selection");

	}
	
	
}
