package org.deidentifier.arx.gui.masking;

import org.eclipse.swt.widgets.Composite;
import org.deidentifer.arx.masking.ChangeableComposite;
import org.deidentifer.arx.masking.ConfigGenerateRandomIntegerDecimalMasker;
import org.deidentifier.arx.gui.resources.Resources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class IntegerTabControl extends Composite {
	
	private Table integerTabTable;
	private Composite rightColumn;
	private ConfigGenerateRandomIntegerDecimalMasker configGenerateRandomIntegerDecimalMasker;
	
	public IntegerTabControl(Composite arg0, int arg1, Resources resources) {
		super(arg0, arg1);
		this.setLayout(new GridLayout(2, false));
		
		Composite leftColumn = new Composite(this, SWT.NONE);
		leftColumn.setLayout(new GridLayout(1, false));
        GridData leftData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        leftData.minimumHeight = 300;
	    leftData.minimumWidth = 200;
        leftColumn.setLayoutData(leftData);    
  
        integerTabTable = new Table(leftColumn,SWT.BORDER);
        GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        tableData.minimumHeight = 200;
        tableData.minimumWidth = 200;
        integerTabTable.setLayoutData(tableData);
        integerTabTable.setHeaderVisible(false);

		TableItem item = new TableItem(integerTabTable, SWT.NONE);
		item.setText("Generate random integer");
		item.setImage(resources.getImage("IntDec.png"));

		integerTabTable.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				TableItem selectedItems[] = integerTabTable.getSelection();
				if (selectedItems.length == 1){
					TableItem selected = selectedItems[0];
					
					int indexFrom = selected.toString().indexOf('{')+1;
					int length = selected.toString().length()-1;
					String selectedName = selected.toString().substring(indexFrom, length);
					
					if(selectedName.equals("Generate random integer")){
						showStringComponentAndHideOthers(configGenerateRandomIntegerDecimalMasker);	
							
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
        
        configGenerateRandomIntegerDecimalMasker = new ConfigGenerateRandomIntegerDecimalMasker(rightColumn, 0, 0);
        
		GridData parentData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		this.setLayoutData(parentData);
	}
	
	private void showStringComponentAndHideOthers(ChangeableComposite component){
		configGenerateRandomIntegerDecimalMasker.hide();
		component.show();
	
	}

}
