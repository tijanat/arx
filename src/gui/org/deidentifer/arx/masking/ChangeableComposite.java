package org.deidentifer.arx.masking;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class ChangeableComposite extends Composite {
	public Group group;
	
	public ChangeableComposite(Composite parent) {
		super(parent, SWT.NONE);
		GridLayout layoutData = new GridLayout(1, false);
		layoutData.marginHeight = 0;
		layoutData.marginWidth = 0;
		layoutData.verticalSpacing = 0;
		layoutData.horizontalSpacing = 0;
		this.setLayout(layoutData);
		group = new Group(this, SWT.SHADOW_IN | SWT.H_SCROLL | SWT.V_SCROLL);
	}

	public void hide(){
		((GridData)group.getLayoutData()).exclude = true;
		group.setVisible(false);
	}
	
	public void show(){
		((GridData)group.getLayoutData()).exclude = false; 
		group.setVisible(true);
	}
	
}
