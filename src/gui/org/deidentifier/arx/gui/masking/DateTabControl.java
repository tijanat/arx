package org.deidentifier.arx.gui.masking;

import org.deidentifer.arx.masking.ChangeableComposite;
import org.deidentifer.arx.masking.ConfigConstantShiftDateMasker;
import org.deidentifer.arx.masking.ConfigGenerateRandomDateMasker;
import org.deidentifer.arx.masking.ConfigRandomShiftDateMasker;
import org.deidentifier.arx.gui.resources.Resources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class DateTabControl extends Composite {
	
	private Table dateTabTable;
	private Composite rightColumn;
	private ConfigGenerateRandomDateMasker configGenerateRandomDate;
	private ConfigConstantShiftDateMasker configConstantShiftDateMasker;
	private ConfigRandomShiftDateMasker configRandomShiftDateMasker;
	
	public DateTabControl(Composite arg0, int arg1, Resources resources) {
		super(arg0, arg1);
		this.setLayout(new GridLayout(2, false));
		
		Composite leftColumn = new Composite(this, SWT.NONE);
		leftColumn.setLayout(new GridLayout(1, false));
        GridData leftData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        leftData.minimumHeight = 300;
        leftData.minimumWidth = 200;
        leftColumn.setLayoutData(leftData);  
        
  
        dateTabTable = new Table(leftColumn,SWT.BORDER);
        GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        tableData.minimumHeight = 200;
        tableData.minimumWidth = 200;
        dateTabTable.setLayoutData(tableData);
        dateTabTable.setHeaderVisible(false);

        TableItem item1 = new TableItem(dateTabTable, SWT.NONE);
		item1.setText("Generate random date");
		item1.setImage(resources.getImage("RandomDate.png"));
        
		TableItem item2 = new TableItem(dateTabTable, SWT.NONE);
		item2.setText("Shift date constantly");
		item2.setImage(resources.getImage("constant_shift.png"));

		TableItem item3 = new TableItem(dateTabTable, SWT.NONE);
		item3.setText("Shift date randomly");
		item3.setImage(resources.getImage("random_shift.png"));
		
		dateTabTable.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				TableItem selectedItems[] = dateTabTable.getSelection();
				if (selectedItems.length == 1){
					TableItem selected = selectedItems[0];
					
					int indexFrom = selected.toString().indexOf('{')+1;
					int length = selected.toString().length()-1;
					String selectedName = selected.toString().substring(indexFrom, length);
					
					if(selectedName.equals("Generate random date")){
						showStringComponentAndHideOthers(configGenerateRandomDate);
						
					} else if (selectedName.equals("Shift date constantly")){
						showStringComponentAndHideOthers(configConstantShiftDateMasker);
						
					} else if (selectedName.equals("Shift date randomly")){
						showStringComponentAndHideOthers(configRandomShiftDateMasker);	
							
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
        
        configGenerateRandomDate = new ConfigGenerateRandomDateMasker(rightColumn, 0, 0);
        configConstantShiftDateMasker = new ConfigConstantShiftDateMasker(rightColumn, 0, 0);
        configRandomShiftDateMasker = new ConfigRandomShiftDateMasker(rightColumn, 0, 0);
	
		GridData parentData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		this.setLayoutData(parentData);
	}
	
	private void showStringComponentAndHideOthers(ChangeableComposite component){
		configGenerateRandomDate.hide();
		configConstantShiftDateMasker.hide();
		configRandomShiftDateMasker.hide();
		component.show();
	
	}

}
