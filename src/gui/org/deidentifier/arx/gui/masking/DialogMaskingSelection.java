package org.deidentifier.arx.gui.masking;
import java.util.Arrays;

import org.deidentifer.arx.masking.ChangeableComposite;
import org.deidentifer.arx.masking.ConfigConstantShiftDateMasker;
import org.deidentifer.arx.masking.ConfigGenerateRandomDateMasker;
import org.deidentifer.arx.masking.ConfigGenerateRandomDecimalMasker;
import org.deidentifer.arx.masking.ConfigGenerateRandomIntegerDecimalMasker;
import org.deidentifer.arx.masking.ConfigGenerateRandomStringMasker;
import org.deidentifer.arx.masking.ConfigMatchAndReplaceStringMasker;
import org.deidentifer.arx.masking.ConfigRandomShiftDateMasker;
import org.deidentifer.arx.masking.ConfigReplaceDictMasker;
import org.deidentifer.arx.masking.ConfigReplaceInstMasker;
import org.deidentifer.arx.masking.ConfigSplitAndReplaceStringMasker;
import org.deidentifier.arx.gui.view.def.IDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
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
	private ConfigGenerateRandomDateMasker configGenerateRandomDate;
	private ConfigGenerateRandomDecimalMasker configGenerateRandomDecimal;
	private ConfigGenerateRandomIntegerDecimalMasker configGenerateRandomIntegerDecimalMasker;
	private ConfigGenerateRandomStringMasker configGenerateRandomStringMasker;
	private ConfigSplitAndReplaceStringMasker configSplitAndReplaceStringMasker;
	private ConfigReplaceDictMasker configReplaceDictMasker;
	private ConfigReplaceInstMasker configReplaceInstMasker;
	private ConfigMatchAndReplaceStringMasker configMatchAndReplaceStringMasker;
	private ConfigConstantShiftDateMasker configConstantShiftDateMasker;
	private ConfigRandomShiftDateMasker configRandomShiftDateMasker;
	int selectedFirstLevel = -1;
	
	/**
	 * @param parentShell
	 */
	public DialogMaskingSelection(Shell parentShell) {
		super(parentShell);
		//parentShell.setMinimumSize(500, 500);
		this.setShellStyle(this.getShellStyle() | SWT.RESIZE);
		
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		parent.setLayout(new GridLayout(1, false));
        TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
    
        TabItem stringTab = new TabItem(tabFolder, SWT.NONE);
        stringTab.setText("String");
        TabItem intTab = new TabItem(tabFolder, SWT.NONE);
        intTab.setText("Integer");
        TabItem decimalTab = new TabItem(tabFolder, SWT.NONE);
        decimalTab.setText("Decimal");
        TabItem dateTab = new TabItem(tabFolder, SWT.NONE); 
        dateTab.setText("Date");
        
		Composite outer = new Composite(tabFolder, SWT.NONE);
		outer.setLayout(new GridLayout(2, false));

		Composite leftColumn = new Composite(outer, SWT.NONE);
		leftColumn.setLayout(new GridLayout(1, false));
		
        GridData leftData = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1);
        leftData.minimumHeight = 300;
        leftData.minimumWidth = 200;
        leftColumn.setLayoutData(leftData);    
  
        final String[] topLevel = new String[]{"Generate random value", "Replace", "Shift", "Shuffle"};
        
        tree = new Tree(leftColumn,SWT.BORDER);
        GridData treeData = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
        treeData.minimumHeight = 270;
        treeData.minimumWidth = 200;
        tree.setLayoutData(treeData);
        //tree.setHeaderVisible(true);
        tree.setHeaderVisible(false);
        tree.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				TreeItem selectedItems[] = tree.getSelection();
				if (selectedItems.length == 1){
					TreeItem selected = selectedItems[0];
					TreeItem parentItem = selected.getParentItem();
					String parentName = "";
					if(parentItem != null){
						// toString gives {Date}
						int indexFrom = parentItem.toString().indexOf('{')+1;
						int length = parentItem.toString().length()-1;
						parentName = parentItem.toString().substring(indexFrom, length);
						if (Arrays.asList(topLevel).indexOf(parentName) >= 0) {
							selectedFirstLevel = Arrays.asList(topLevel).indexOf(parentName);
						}	
					}
					
					int indexFrom = selected.toString().indexOf('{')+1;
					int length = selected.toString().length()-1;
					String selectedName = selected.toString().substring(indexFrom, length);
					
					if(selectedName.equals("Date") && selectedFirstLevel == 0){
						showComponentAndHideOthers(configGenerateRandomDate);
						
					} else if (selectedName.equals("Decimal") && selectedFirstLevel == 0){
						showComponentAndHideOthers(configGenerateRandomDecimal);
						
					} else if (selectedName.equals("Integer decimal")){
						showComponentAndHideOthers(configGenerateRandomIntegerDecimalMasker);	
						
					} else if(selectedName.equals("String")){
						showComponentAndHideOthers(configGenerateRandomStringMasker);	
						
					} else if(selectedName.equals ("Split and replace string")){
						showComponentAndHideOthers(configSplitAndReplaceStringMasker);
						
					} else if(selectedName.equals ("Dictionary")){
						showComponentAndHideOthers(configReplaceDictMasker);	
						
					} else if(selectedName.equals ("Instances")){
						showComponentAndHideOthers(configReplaceInstMasker);		
						
					} else if(selectedName.equals ("Match and Replace String")){
						showComponentAndHideOthers(configMatchAndReplaceStringMasker);			
						
					} else if(selectedName.equals("Constant") && parentName.equals("Date")){
						showComponentAndHideOthers(configConstantShiftDateMasker);	
					
					} else if(selectedName.equals("Random") && parentName.equals("Date")){
						showComponentAndHideOthers(configRandomShiftDateMasker);
							
					} else {
						showComponentAndHideOthers(null);
					}
					
					rightColumn.pack();
					System.out.println("selectedItem " + selectedName);
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
				case 0: // Generate Random Value
					subItems = new String[]{"Date", "Decimal", "Integer decimal", "String"};
				break;
				case 1: // Replace
					subItems = new String[]{"Split and replace string", "Dictionary", "Instances", "Match and replace string"};
				break;
				case 2: // Shift
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
				
				if (i==2){ //Shift (i==3) needs 2 levels
					String[] subSubItems = new String[]{"Random", "Constant"};
					new TreeItem(subItem, SWT.NONE).setText(subSubItems[0]);
					new TreeItem(subItem, SWT.NONE).setText(subSubItems[1]);
				}
			}
		}

		rightColumn = new Composite(outer, SWT.RIGHT );
		GridLayout rightLayout = new GridLayout(1, false);
		rightLayout.marginHeight = 0;
		rightLayout.verticalSpacing = 0;
		rightLayout.marginTop = 5;
		rightColumn.setLayout(rightLayout);
	
//		rightColumn.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
        GridData rightData = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
        rightData.minimumHeight = 350;
        rightData.minimumWidth = 350;
        rightColumn.setLayoutData(rightData);   
        
		configGenerateRandomDate = new ConfigGenerateRandomDateMasker(rightColumn, 0, 0);
		configGenerateRandomDecimal = new ConfigGenerateRandomDecimalMasker(rightColumn, 0, 0);
		configGenerateRandomIntegerDecimalMasker = new ConfigGenerateRandomIntegerDecimalMasker(rightColumn, 0, 0);
		configGenerateRandomStringMasker = new ConfigGenerateRandomStringMasker(rightColumn, 0, 0);
		configSplitAndReplaceStringMasker = new ConfigSplitAndReplaceStringMasker(rightColumn, 0, 0);
		configReplaceDictMasker = new ConfigReplaceDictMasker(rightColumn, 0, 0);
		configReplaceInstMasker = new ConfigReplaceInstMasker(rightColumn, 0, 0);
		configMatchAndReplaceStringMasker = new ConfigMatchAndReplaceStringMasker(rightColumn, 0, 0);
		configRandomShiftDateMasker = new ConfigRandomShiftDateMasker(rightColumn, 0, 0);
		configConstantShiftDateMasker = new ConfigConstantShiftDateMasker(rightColumn, 0, 0);

		Point rightColSize = configGenerateRandomDate.getSize();
		Point leftColSize = leftColumn.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		
		GridData parentData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		parentData.heightHint = Math.max(leftColSize.y, rightColSize.y) + 30;
	//	parentData.widthHint = leftColSize.x + rightColSize.x + 60;
		parentData.minimumWidth = 500;
		outer.setLayoutData(parentData);
		stringTab.setControl(outer);

		return parent;
	}

	@Override
	public void create() {
		super.create();
		this.setTitle("Masking Method Selection");
		    
	}
	
	private void showComponentAndHideOthers(ChangeableComposite component){
		configGenerateRandomDate.hide();
		configGenerateRandomDecimal.hide();
		configGenerateRandomStringMasker.hide();
		configGenerateRandomIntegerDecimalMasker.hide();
		configSplitAndReplaceStringMasker.hide();
		configReplaceDictMasker.hide();
		configReplaceInstMasker.hide();
		configMatchAndReplaceStringMasker.hide();
		configRandomShiftDateMasker.hide();
		configConstantShiftDateMasker.hide();
		component.show();
	}

}