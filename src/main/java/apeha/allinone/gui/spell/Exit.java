package apeha.allinone.gui.spell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.io.IOException;

public class Exit {

    private static final String SAVE = "Сохранить";
    private static final String CANCEL = "Отменить";
    private static final String CLOSE = "Закрыть";
    private static final String EXIT_WITHOUT_SAVE = "Выйти не сохраняя";
    private static final String MAIN_TEXT = "Данные были изменены, но не сохранены. Вы желаете их сохранить?";

    private Shell shellExit = null;
    private Label lExitAttention = null;
    private Button btnExitCloseWithoutSaving = null;
    private Button btnExitSave = null;
    private Button btnExitCancel = null;
    private SpellGUI spellGui = null;
    private boolean exit = false;

    public Exit(SpellGUI main, boolean exit) {
        this.spellGui = main;
        this.exit = exit;
        this.createShellExit();
        this.shellExit.open();
    }

    private void createShellExit() {
        GridData gdHfillVCenter = new GridData();
        gdHfillVCenter.horizontalAlignment = GridData.FILL;
        gdHfillVCenter.verticalAlignment = GridData.CENTER;

        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = false;
        gridData.horizontalSpan = 3;
        gridData.horizontalIndent = 0;

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.horizontalSpacing = 5;
        gridLayout.verticalSpacing = 5;
        gridLayout.marginWidth = 5;
        gridLayout.marginHeight = 5;
        gridLayout.makeColumnsEqualWidth = true;

        shellExit = new Shell(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shellExit.setText(CLOSE);
        shellExit.setLayout(gridLayout);

        lExitAttention = new Label(shellExit, SWT.HORIZONTAL | SWT.CENTER);
        lExitAttention.setText(MAIN_TEXT);
        lExitAttention.setLayoutData(gridData);

        btnExitCloseWithoutSaving = new Button(shellExit, SWT.PUSH);
        btnExitCloseWithoutSaving.setText(EXIT_WITHOUT_SAVE);
        btnExitCloseWithoutSaving.setLayoutData(gdHfillVCenter);
        btnExitCloseWithoutSaving.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                shellExit.dispose();
                if (exit)
                    spellGui.closeMainShell();
                else {
                    spellGui.goBack();
                    spellGui.isSaved = true;
                }
            }
        });

        btnExitCancel = new Button(shellExit, SWT.PUSH);
        btnExitCancel.setText(CANCEL);
        btnExitCancel.setLayoutData(gdHfillVCenter);
        btnExitCancel.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                shellExit.dispose();
            }
        });

        btnExitSave = new Button(shellExit, SWT.PUSH);
        btnExitSave.setText(SAVE);
        btnExitSave.setLayoutData(gdHfillVCenter);
        btnExitSave.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                FileDialog dialog = new FileDialog(shellExit, SWT.SAVE);
                dialog.setFilterExtensions(new String[]{"*.xml"});
                String fileName = dialog.open();
                if (fileName != null) {
                    try {
                        spellGui.saveItemsToFile(spellGui.getCurrentItems(),
                                fileName);
                        spellGui.isSaved = true;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    shellExit.dispose();
                    if (exit)
                        spellGui.closeMainShell();
                    else
                        spellGui.goBack();
                }
            }
        });

        shellExit.pack();
        spellGui.setInnerShellPosition(shellExit);
    }

}
