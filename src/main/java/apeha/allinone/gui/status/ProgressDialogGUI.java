package apeha.allinone.gui.status;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class ProgressDialogGUI {
	private static final int WIDTH = 400;

	private Shell shell;
	private Composite composite;
	private Label lStatus;
	private ProgressBar progressBar;
	private Button stop;
	private Status status;

	public ProgressDialogGUI(String title, Status status) {
		this.status = status;
		shell = new Shell(SWT.CLOSE | SWT.APPLICATION_MODAL);
		shell.setText(title);
		shell.setLayout(new FillLayout());
		composite = new Composite(shell, SWT.NONE);
		createUI();
		shell.pack();
		shell.layout();
		shell.pack();
		Point size = shell.getSize();
		shell.setSize(WIDTH, size.y);
	}

	public static void main(String[] args) {
		Display display = Display.getDefault();
		final ProgressDialogGUI progressDialogGUI = new ProgressDialogGUI("",
				null);

		// display.asyncExec(new Runnable() {
		//
		// @Override
		// public void run() {
		// int max = 10;
		// int min = 0;
		// progressDialogGUI.setMaximumForProgressBar(max);
		// for (int i = min; i < max; i++) {
		// progressDialogGUI.setStatus("Status" + i);
		// progressDialogGUI.incrementProgressBar();
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// });

		while (!progressDialogGUI.shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public Shell getShell() {
		return shell;
	}

	public void executeWatchingThread() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				final ProgressDialogGUI gui = ProgressDialogGUI.this;
				while (!status.isDone()) {
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							if (!status.isDone()) {
								String currentStatus = status.getStatus();
								if (!gui.getStatus().equals(currentStatus)) {
									gui.setStatus(currentStatus);
								}
							}
						}
					});
				}
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						gui.close();
					}
				});
			}
		};
		new Thread(runnable, "ProgressUpdateThread").start();
	}

	private void createUI() {
		this.composite.setLayout(new GridLayout(2, false));

		lStatus = new Label(composite, SWT.HORIZONTAL | SWT.CENTER);
		lStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2,
				1));
		progressBar = new ProgressBar(composite, SWT.INDETERMINATE
				| SWT.HORIZONTAL);
		progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		stop = new Button(composite, SWT.PUSH);
		stop.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		stop.setText("Остановить");

		stop.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				close();
			}
		});
	}

	private String getStatus() {
		return lStatus.getText();
	}

	private void setStatus(String status) {
		lStatus.setText(status);
	}

	public void setSelection(int value) {
		if (value <= progressBar.getMaximum()) {
			progressBar.setSelection(value);
		} else {
			status.setDone(true);
		}
	}

	private synchronized void close() {
		status.setDone(true);
		if (!this.shell.isDisposed()) {
			this.shell.close();
		}
	}

	public void show() {
		Display display = Display.getDefault();

		while (!this.shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
