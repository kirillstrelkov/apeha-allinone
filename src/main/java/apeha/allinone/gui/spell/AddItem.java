package apeha.allinone.gui.spell;

import apeha.allinone.gui.CommonData;
import apeha.allinone.item.spell.SpellableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.io.StringReader;
import java.util.List;

public class AddItem {

    private Shell shell = null;
    private Text textArea = null;
    private Button buttonWindowAdd = null;
    private Button buttonWindowCancel = null;
    private SpellGUI spellGui = null;

    public AddItem(SpellGUI spellGui) {
        this.spellGui = spellGui;
        this.createShell();
        this.shell.open();
    }

    private void createShell() {
        GridData gdHgrab = new GridData();
        gdHgrab.horizontalAlignment = GridData.FILL;
        gdHgrab.verticalAlignment = GridData.FILL;
        gdHgrab.grabExcessHorizontalSpace = true;

        GridData gdHgrabVgrab = new GridData();
        gdHgrabVgrab.horizontalAlignment = GridData.FILL;
        gdHgrabVgrab.grabExcessHorizontalSpace = true;
        gdHgrabVgrab.grabExcessVerticalSpace = true;
        gdHgrabVgrab.horizontalSpan = 2;
        gdHgrabVgrab.verticalAlignment = GridData.FILL;

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = true;

        shell = new Shell(SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
        shell.setLayout(gridLayout);
        shell.setSize(new Point(515, 479));
        shell.setText("Добавить вещи");
        spellGui.setInnerShellPosition(shell);

        textArea = new Text(shell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        textArea.setLayoutData(gdHgrabVgrab);
        textArea.setText(CommonData.ADDING_HINT);

        buttonWindowCancel = new Button(shell, SWT.NONE);
        buttonWindowCancel.setText("Отменить");
        buttonWindowCancel.setLayoutData(gdHgrab);
        buttonWindowCancel.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                shell.dispose();
            }
        });

        buttonWindowAdd = new Button(shell, SWT.NONE);
        buttonWindowAdd.setText("Добавить");
        buttonWindowAdd.setLayoutData(gdHgrab);
        buttonWindowAdd.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                List<SpellableItem> items = spellGui.getCurrentItems();
                List<SpellableItem> spellableItems = spellGui
                        .getSpellableItems(new StringReader(textArea.getText()));

                items.addAll(spellableItems);

                spellGui.loadItems(items);
                spellGui.setSaved(false);
                shell.dispose();
            }
        });

    }
}
