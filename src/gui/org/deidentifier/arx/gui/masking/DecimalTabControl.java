package org.deidentifier.arx.gui.masking;

import org.deidentifer.arx.masking.ChangeableComposite;
import org.deidentifer.arx.masking.ConfigConstantShiftDecimalMasker;
import org.deidentifer.arx.masking.ConfigGenerateRandomDecimalMasker;
import org.deidentifer.arx.masking.ConfigRandomShiftDecimalMasker;
import org.deidentifier.arx.gui.resources.Resources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class DecimalTabControl extends Composite {

	private Table decimalTabTable;
	private Composite rightColumn;
	private ConfigGenerateRandomDecimalMasker configGenerateRandomDecimal;
	private ConfigConstantShiftDecimalMasker configConstantShiftDecimalMasker;
	private ConfigRandomShiftDecimalMasker configRandomShiftDecimalMasker;
	
	public DecimalTabControl(Composite arg0, int arg1, Resources resources) {
		super(arg0, arg1);
		this.setLayout(new GridLayout(2, false));
		
		Composite leftColumn = new Composite(this, SWT.NONE);
		leftColumn.setLayout(new GridLayout(1, false));
        GridData leftData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        leftData.minimumHeight = 300;
	    leftData.minimumWidth = 200;
        leftColumn.setLayoutData(leftData);    
  
        decimalTabTable = new Table(leftColumn,SWT.BORDER);
        GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        tableData.minimumHeight = 200;
        tableData.minimumWidth = 200;
        decimalTabTable.setLayoutData(tableData);
        decimalTabTable.setHeaderVisible(false);

        TableItem item1 = new TableItem(decimalTabTable, SWT.NONE);
		item1.setText("Generate random decimal");
		item1.setImage(resources.getImage("RandomDec.png"));
        
		TableItem item2 = new TableItem(decimalTabTable, SWT.NONE);
		item2.setText("Shift decimal constantly");
		item2.setImage(resources.getImage("constant_shift.png"));

		TableItem item3 = new TableItem(decimalTabTable, SWT.NONE);
		item3.setText("Shift decimal randomly");
		item3.setImage(resources.getImage("random_shift.png"));
		
		decimalTabTable.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				TableItem selectedItems[] = decimalTabTable.getSelection();
				if (selectedItems.length == 1){
					TableItem selected = selectedItems[0];
					
					int indexFrom = selected.toString().indexOf('{')+1;
					int length = selected.toString().length()-1;
					String selectedName = selected.toString().substring(indexFrom, length);
					
					if(selectedName.equals("Generate random decimal")){
						showStringComponentAndHideOthers(configGenerateRandomDecimal);
						
					} else if (selectedName.equals("Shift decimal constantly")){
						showStringComponentAndHideOthers(configConstantShiftDecimalMasker);
						
					} else if (selectedName.equals("Shift decimal randomly")){
						showStringComponentAndHideOthers(configRandomShiftDecimalMasker);	
							
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
        
        configGenerateRandomDecimal = new ConfigGenerateRandomDecimalMasker(rightColumn, 0, 0);
        configConstantShiftDecimalMasker = new ConfigConstantShiftDecimalMasker(rightColumn, 0, 0);
        configRandomShiftDecimalMasker = new ConfigRandomShiftDecimalMasker(rightColumn, 0, 0);
	
		GridData parentData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		this.setLayoutData(parentData);
	}
	
	private void showStringComponentAndHideOthers(ChangeableComposite component){
		configGenerateRandomDecimal.hide();
		configConstantShiftDecimalMasker.hide();
		configRandomShiftDecimalMasker.hide();
		component.show();
	
	}

}