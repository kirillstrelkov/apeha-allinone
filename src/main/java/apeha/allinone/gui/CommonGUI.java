package apeha.allinone.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class CommonGUI {
	private static final String BACK_TO_MAIN = "Назад";
	public Label exceptionInfo;
	protected Shell shell;
	protected Composite mainComposite;
	protected Button back;
	protected Composite composite;
	protected Listener backListener = new Listener() {

		@Override
		public void handleEvent(Event e) {
			goBack();
		}
	};

	public CommonGUI(Composite composite) {
		mainComposite = composite;
		shell = composite.getShell();
		createCommonUI();
	}

	private void createCommonUI() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		GridData gdButton = new GridData(SWT.LEFT, SWT.FILL, false, false);
		GridData gdComposite = new GridData(SWT.FILL, SWT.FILL, true, true);
		gdComposite.horizontalSpan = 2;

		mainComposite.setLayout(layout);
		mainComposite.layout();

		back = new Button(mainComposite, SWT.PUSH);
		back.setText(BACK_TO_MAIN);
		back.setLayoutData(gdButton);
		back.addListener(SWT.Selection, backListener);
		exceptionInfo = new Label(mainComposite, SWT.CENTER);
		exceptionInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		composite = new Composite(mainComposite, SWT.DOUBLE_BUFFERED);
		composite.setLayoutData(gdComposite);
		shell.pack();
		shell.setSize(500, 500);
	}

	public void goBack() {
		back.dispose();
		exceptionInfo.dispose();
		composite.dispose();
		new MainGUI(mainComposite);
	}

}
