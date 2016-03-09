package apeha.allinone.gui.spell;

import apeha.allinone.item.ItemBuilder;
import apeha.allinone.item.Property;
import apeha.allinone.item.spell.SpellableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

public class Modify {

    private static final String CANCEL = "Отмена";
    private static final String SPELLED_TEXT = "Наложено заклятие еще:";
    private static final String ITEM_INFORMATION = "Информация о предмете";
    private static final Point SIZE = new Point(400, 400);
    private static final String ITEM_NAME = "Название предмета";
    private static final String CHANGE = "Изменить";

    private SpellGUI spellGui = null;
    private Shell shell = null;
    private Group group = null;
    private Text info = null;
    private Text dateAsString = null;
    private Button btnCancel = null;
    private Button btnModify = null;
    private SpellableItem item = null;
    private Label label = null;
    private Label lName = null;
    private boolean dataWasChanged = false;

    public Modify(SpellGUI spellGui) {
        this.spellGui = spellGui;
        this.createUI();
        this.item = this.spellGui.getSelecteditem();
        this.lName.setText(item.getName());

        String comment = item.getComment();
        if (comment == null) {
            this.info.setText(item.getProperties());
        } else {
            this.info.setText(item.getProperties() + "\n\n" + comment);
        }

        this.dateAsString.setText(item.getTimeLeft());
        this.shell.open();
    }

    private void createUI() {
        GridData gridData5 = new GridData();
        gridData5.horizontalSpan = 2;
        gridData5.horizontalAlignment = GridData.FILL;
        gridData5.verticalAlignment = GridData.FILL;

        GridData gdHgrab = new GridData();
        gdHgrab.horizontalAlignment = GridData.FILL;
        gdHgrab.verticalAlignment = GridData.CENTER;
        gdHgrab.grabExcessHorizontalSpace = true;

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = true;

        shell = new Shell(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setLayout(gridLayout);
        shell.setText(CHANGE);

        lName = new Label(shell, SWT.LEFT);
        lName.setText(ITEM_NAME);
        lName.setLayoutData(gridData5);

        createGroup();
        btnCancel = new Button(shell, SWT.PUSH);
        btnCancel.setText(CANCEL);
        btnCancel.setLayoutData(gdHgrab);
        btnCancel.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                shell.dispose();
            }
        });

        btnModify = new Button(shell, SWT.PUSH);
        btnModify.setText(CHANGE);
        btnModify.setLayoutData(gdHgrab);
        btnModify.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                int selectedIndex = spellGui.table.getSelectionIndex();
                String leftDays = Property.formatProperty(Property.SPELLED,
                        dateAsString.getText());

                if (dataWasChanged && (leftDays != null)) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(item.getName() + "\n");

                    Map<Property, String> propertiesAndValues = item
                            .getPropertiesAndValues();

                    for (Property property : propertiesAndValues.keySet()) {
                        builder.append(Property.formatProperty(property,
                                propertiesAndValues.get(property)) + "\n");
                    }

                    builder.append(leftDays);

                    item = ItemBuilder.createSpellableItem(builder.toString());
                    String comment = getComment();
                    if (comment != null) {
                        item.setComment(comment);
                    }

                    List<SpellableItem> currentItems = spellGui
                            .getCurrentItems();
                    currentItems.remove(selectedIndex);
                    currentItems.add(selectedIndex, item);

                    spellGui.setSaved(false);
                    spellGui.tableRefresh(selectedIndex);
                }

                shell.dispose();
            }
        });

        shell.pack();
        shell.setSize(SIZE);
        spellGui.setInnerShellPosition(shell);
    }

    private void createGroup() {
        GridData gsHgrab = new GridData();
        gsHgrab.horizontalAlignment = GridData.FILL;
        gsHgrab.verticalAlignment = GridData.FILL;
        gsHgrab.grabExcessHorizontalSpace = true;

        GridLayout gridLayout = new GridLayout(2, true);

        GridData gdHgrabVgrabHspan2 = new GridData();
        gdHgrabVgrabHspan2.horizontalAlignment = GridData.FILL;
        gdHgrabVgrabHspan2.verticalAlignment = GridData.FILL;
        gdHgrabVgrabHspan2.grabExcessHorizontalSpace = true;
        gdHgrabVgrabHspan2.grabExcessVerticalSpace = true;
        gdHgrabVgrabHspan2.horizontalSpan = 2;

        GridData gdHgrabVgrab = new GridData();
        gdHgrabVgrab.horizontalAlignment = GridData.FILL;
        gdHgrabVgrab.verticalAlignment = GridData.FILL;
        gdHgrabVgrab.grabExcessHorizontalSpace = true;
        gdHgrabVgrab.grabExcessVerticalSpace = true;

        group = new Group(shell, SWT.NONE);
        group.setLayoutData(gdHgrabVgrabHspan2);
        group.setLayout(gridLayout);
        group.setText(ITEM_INFORMATION);

        info = new Text(group, SWT.MULTI | SWT.V_SCROLL);
        info.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        info.addListener(SWT.Modify, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (!dataWasChanged) {
                    dataWasChanged = info.isFocusControl();
                }
            }
        });

        label = new Label(group, SWT.LEFT);
        label.setText(SPELLED_TEXT);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        dateAsString = new Text(group, SWT.SINGLE | SWT.BORDER);
        dateAsString.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false));
        dateAsString.addListener(SWT.Modify, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (!dataWasChanged) {
                    dataWasChanged = dateAsString.isFocusControl();
                }
            }
        });
    }

    private String getComment() {
        StringBuilder builder = new StringBuilder();

        BufferedReader reader = new BufferedReader(new StringReader(
                info.getText()));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) {
                    builder.append(line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String comment = builder.toString();
        if (comment.length() > 0) {
            return comment.trim();
        } else {
            return null;
        }
    }
}
