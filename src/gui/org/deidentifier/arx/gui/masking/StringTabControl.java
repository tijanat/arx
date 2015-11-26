package org.deidentifier.arx.gui.masking;

import org.deidentifer.arx.masking.ChangeableComposite;
import org.deidentifer.arx.masking.ConfigGenerateRandomStringMasker;
import org.deidentifer.arx.masking.ConfigMatchAndReplaceStringMasker;
import org.deidentifer.arx.masking.ConfigSplitAndReplaceStringMasker;
import org.deidentifier.arx.gui.resources.Resources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class StringTabControl extends Composite {
	
	private Table stringTabTable;
	private Composite rightColumn;
	private ConfigGenerateRandomStringMasker configGenerateRandomStringMasker;
	private ConfigSplitAndReplaceStringMasker configSplitAndReplaceStringMasker;
	private ConfigMatchAndReplaceStringMasker configMatchAndReplaceStringMasker;

	public StringTabControl(Composite arg0, int arg1, Resources resources) {
		super(arg0, arg1);
		this.setLayout(new GridLayout(2, false));
		
		Composite leftColumn = new Composite(this, SWT.NONE);
		leftColumn.setLayout(new GridLayout(1, false));
        GridData leftData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        leftData.minimumHeight = 300;
	    leftData.minimumWidth = 200;
        leftColumn.setLayoutData(leftData);    
  
        stringTabTable = new Table(leftColumn,SWT.BORDER);
        GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        tableData.minimumHeight = 200;
        tableData.minimumWidth = 200;
        stringTabTable.setLayoutData(tableData);
        stringTabTable.setHeaderVisible(false);

        TableItem item1 = new TableItem(stringTabTable, SWT.NONE);
		item1.setText("Generate random string");
		item1.setImage(resources.getImage("Random.png"));
        
		TableItem item2 = new TableItem(stringTabTable, SWT.NONE);
		item2.setText("Match and replace string");
		item2.setImage(resources.getImage("Match.png"));

		TableItem item3 = new TableItem(stringTabTable, SWT.NONE);
		item3.setText("Split and replace string");
		item3.setImage(resources.getImage("Split.png"));
		
		stringTabTable.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				TableItem selectedItems[] = stringTabTable.getSelection();
				if (selectedItems.length == 1){
					TableItem selected = selectedItems[0];
					
					int indexFrom = selected.toString().indexOf('{')+1;
					int length = selected.toString().length()-1;
					String selectedName = selected.toString().substring(indexFrom, length);
					
					if(selectedName.equals("Generate random string")){
						showStringComponentAndHideOthers(configGenerateRandomStringMasker);
						
					} else if (selectedName.equals("Match and replace string")){
						showStringComponentAndHideOthers(configMatchAndReplaceStringMasker);
						
					} else if (selectedName.equals("Split and replace string")){
						showStringComponentAndHideOthers(configSplitAndReplaceStringMasker);	
							
					} else {
						showStringComponentAndHideOthers(null);
					}
					
					rightColumn.pack();
				}				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});

		rightColumn = new Composite(this, SWT.RIGHT );
		GridLayout rightLayout = new GridLayout(1, false);
		rightLayout.marginHeight = 0;
		rightLayout.verticalSpacing = 0;
		rightLayout.marginTop = 5;
		rightColumn.setLayout(rightLayout);
	
//		rightColumn.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
        GridData rightData = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
        rightData.minimumHeight = 350;
        rightData.minimumWidth = 320;
        rightColumn.setLayoutData(rightData);   
        
		configGenerateRandomStringMasker = new ConfigGenerateRandomStringMasker(rightColumn, 0, 0);
		configSplitAndReplaceStringMasker = new ConfigSplitAndReplaceStringMasker(rightColumn, 0, 0);
		configMatchAndReplaceStringMasker = new ConfigMatchAndReplaceStringMasker(rightColumn, 0, 0);
	
		GridData parentData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		this.setLayoutData(parentData);
	}
	
	private void showStringComponentAndHideOthers(ChangeableComposite component){
		configGenerateRandomStringMasker.hide();
		configSplitAndReplaceStringMasker.hide();
		configMatchAndReplaceStringMasker.hide();
		component.show();
	}
}
