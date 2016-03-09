package apeha.allinone.gui.spell;

import apeha.allinone.common.AppProperties;
import apeha.allinone.common.Utils;
import apeha.allinone.gui.CommonData;
import apeha.allinone.gui.CommonGUI;
import apeha.allinone.item.Item;
import apeha.allinone.item.Property;
import apeha.allinone.item.TextParser;
import apeha.allinone.item.spell.SpellableItem;
import apeha.allinone.spell.XMLIO;
import com.google.common.collect.Lists;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.joda.time.DateTime;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SpellGUI extends CommonGUI {
    public static final String SHELL_TITLE = "Проверка заклинаний";
    private static final String ENCODING = "UTF-8";
    private static final Font REMOVED_FONT = new Font(Display.getDefault(),
            CommonData.FONT_NAME, (int) (CommonData.DEFAULT_FONT_SIZE * 0.9),
            SWT.ITALIC);
    private static final Point SIZE = new Point(720, 580);
    private static final String SPELL_BTN_TEXT = "Заклясть";
    private static final String CHANGE_SPELL_TEXT = "Изменить";
    private static final String SPELL_UNTIL_LABEL = "Срок действия заклятия:";
    private static final String LEFT_DAYS_TEXT = "Наложено заклятие еще:";
    private static final String RESPELL_SINCE_TEXT = "Перезаклясть можно с:";
    private static final String SPELL_UNTIL_TEXT = "Заклинание действует до:";
    private static final String SPELL_TEXT = "Заклятие";
    private static final String DELETE_ITEM = "Удалить предмет";
    private static final String OPENED_FILE = "Открытый файл: ";
    private static final String ITEM_IS_NOT_SELECTED = "* Предмет не выбран.";
    private static final String ITEM_INFO_TEXT = "Информация о предмете";
    private static final String SAVE_TO_FILE_TEXT = "Сохранить в файл";
    private static final String ADD_ITEMS_TEXT = "Добавить вещи";
    private static final String OPEN_FILE_TEXT = "Открыть исходный файл";
    private static final String[] TABLE_COLUMNS = {"№", "Название предмета"};
    private static final String[] SPELL_VALUE = {"30 дней", "45 дней",
            "60 дней", "75 дней", "90 дней"};
    private static final int SPELL_BAR_MAX = 5 * 24 * 60;
    protected boolean isSaved = true;
    Table table = null;
    private Button btnOpen = null;
    private Button btnAdd = null;
    private Group groupItem = null;
    private Text textInfo = null;
    private Group groupSpell = null;
    private Label lUntillText = null;
    private Label lUntilShowDate = null;
    private Label lFromText = null;
    private Label lShowFromDate = null;
    private Label lLimitSpell = null;
    private Label lSpellUntillText = null;
    private Label lSpellUntill = null;
    private Button btnModify = null;
    private Button btnSpell = null;
    private Button btnSaveToFile = null;
    private CCombo cSpell = null;
    private ProgressBar spellBar = null;
    private Button btnDeleteItem = null;
    private List<SpellableItem> items = Lists.newArrayList();
    private AppProperties appProperties = null;

    public SpellGUI(Composite composite) {
        super(composite);
        this.appProperties = AppProperties.newAppProperties();
        createUI();
    }

    public static void main(String[] args) {
        // TODO remove
        Display display = Display.getDefault();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        Composite composite2 = new Composite(shell, SWT.DOUBLE_BUFFERED);
        SpellGUI spellGUI = new SpellGUI(composite2);
        spellGUI.shell.open();
        spellGUI.shell.layout();
        while (!spellGUI.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public void setInnerShellPosition(Shell innerShell) {
        Shell shell = composite.getShell();
        Point location = shell.getShell().getLocation();
        Point size = shell.getShell().getSize();
        Point innerSize = innerShell.getSize();
        innerShell.setLocation(location.x + (size.x - innerSize.x) / 2,
                location.y + (size.y - innerSize.y) / 2);
    }

    public void setSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    private void createUI() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = true;

        composite.setLayout(gridLayout);

        GridData gdTable = new GridData();
        gdTable.horizontalAlignment = GridData.FILL;
        gdTable.verticalAlignment = GridData.FILL;
        gdTable.grabExcessHorizontalSpace = true;
        gdTable.grabExcessVerticalSpace = true;
        gdTable.verticalSpan = 5;

        GridData gdHgrab = new GridData();
        gdHgrab.horizontalAlignment = GridData.FILL;
        gdHgrab.verticalAlignment = GridData.FILL;
        gdHgrab.grabExcessHorizontalSpace = true;
        gdHgrab.grabExcessVerticalSpace = false;

        table = new Table(composite, SWT.SINGLE | SWT.FULL_SELECTION);
        table.setLayoutData(gdTable);
        table.setHeaderVisible(false);
        table.setLinesVisible(true);

        for (String colName : TABLE_COLUMNS) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(colName);
        }

        btnOpen = new Button(composite, SWT.PUSH);
        btnOpen.setText(OPEN_FILE_TEXT);
        btnOpen.setLayoutData(gdHgrab);

        btnAdd = new Button(composite, SWT.PUSH);
        btnAdd.setText(ADD_ITEMS_TEXT);
        btnAdd.setLayoutData(gdHgrab);

        createItemGroup();
        createSpellGroup();

        btnSaveToFile = new Button(composite, SWT.PUSH);
        btnSaveToFile.setText(SAVE_TO_FILE_TEXT);
        btnSaveToFile.setLayoutData(gdHgrab);

        createListeners();

        shell.setText(SHELL_TITLE);
        shell.pack();
        shell.setSize(SIZE);
        doPostActions();
    }

    private void createListeners() {
        btnAdd.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                getAddItemShell();
            }
        });

        btnOpen.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                getOpenService();
            }
        });

        table.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                setLabels(table.getSelectionIndex());
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                getModifyShell();
            }
        });

        btnSaveToFile.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                getSaveService();
            }
        });

        btnDeleteItem.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                setSaved(false);
                if (items.size() > 0) {
                    int selectionIndex = table.getSelectionIndex();
                    try {
                        items.remove(selectionIndex);
                        textInfo.setText("");
                        btnModify.setVisible(false);
                    } catch (ArrayIndexOutOfBoundsException exc) {
                        textInfo.setText(ITEM_IS_NOT_SELECTED);
                    } finally {
                        int size = items.size();
                        if (selectionIndex < size)
                            tableRefresh(selectionIndex);
                        else if (size > 0) {
                            tableRefresh(size - 1);
                        } else {
                            tableRefresh();
                        }
                    }
                }
            }
        });

        btnModify.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                getModifyShell();
            }

        });
        btnSpell.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                setSaved(false);
                SpellableItem item = items.get(table.getSelectionIndex());
                item.spell(Integer.parseInt(getSpellValue()));
                tableRefresh(table.getSelectionIndex());
            }
        });

        back.removeListener(SWT.Selection, backListener);
        back.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event e) {
                if (isSaved) {
                    e.doit = true;
                    goBack();
                } else {
                    e.doit = false;
                    getExitUI(false);
                }
            }

        });

        shell.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent e) {
                if (isSaved)
                    e.doit = true;
                else {
                    e.doit = false;
                    getExitUI(true);
                }
            }
        });
    }

    private void createItemGroup() {
        GridData gdHgrab = new GridData();
        gdHgrab.horizontalAlignment = GridData.FILL;
        gdHgrab.verticalAlignment = GridData.FILL;
        gdHgrab.grabExcessHorizontalSpace = true;

        GridData gdHgrabVgrab = new GridData();
        gdHgrabVgrab.horizontalAlignment = GridData.FILL;
        gdHgrabVgrab.verticalAlignment = GridData.FILL;
        gdHgrabVgrab.grabExcessHorizontalSpace = true;
        gdHgrabVgrab.grabExcessVerticalSpace = true;

        groupItem = new Group(composite, SWT.SHADOW_NONE);
        groupItem.setText(ITEM_INFO_TEXT);
        groupItem.setLayout(new GridLayout());
        groupItem.setLayoutData(gdHgrabVgrab);

        textInfo = new Text(groupItem, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL
                | SWT.H_SCROLL);
        textInfo.setText("");
        textInfo.setLayoutData(gdHgrabVgrab);

        btnDeleteItem = new Button(groupItem, SWT.NONE);
        btnDeleteItem.setText(DELETE_ITEM);
        btnDeleteItem.setLayoutData(gdHgrab);
    }

    private void createSpellGroup() {
        final String defaultText = "";

        GridData gdHgrab = new GridData();
        gdHgrab.horizontalAlignment = GridData.FILL;
        gdHgrab.verticalAlignment = GridData.FILL;
        gdHgrab.grabExcessHorizontalSpace = true;

        GridLayout layout = new GridLayout(2, false);

        groupSpell = new Group(composite, SWT.SHADOW_NONE);
        groupSpell.setText(SPELL_TEXT);
        groupSpell.setLayout(layout);
        groupSpell.setLayoutData(gdHgrab);

        lUntillText = new Label(groupSpell, SWT.LEFT);
        lUntillText.setText(SPELL_UNTIL_TEXT);
        lUntillText.setLayoutData(gdHgrab);

        lUntilShowDate = new Label(groupSpell, SWT.RIGHT);
        lUntilShowDate.setText(defaultText);
        lUntilShowDate.setLayoutData(gdHgrab);

        lFromText = new Label(groupSpell, SWT.LEFT);
        lFromText.setText(RESPELL_SINCE_TEXT);
        lFromText.setLayoutData(gdHgrab);

        lShowFromDate = new Label(groupSpell, SWT.RIGHT);
        lShowFromDate.setText(defaultText);
        lShowFromDate.setLayoutData(gdHgrab);

        lSpellUntillText = new Label(groupSpell, SWT.LEFT);
        lSpellUntillText.setText(LEFT_DAYS_TEXT);
        lSpellUntillText.setLayoutData(gdHgrab);

        lSpellUntill = new Label(groupSpell, SWT.RIGHT);
        lSpellUntill.setText(defaultText);
        lSpellUntill.setLayoutData(gdHgrab);

        spellBar = new ProgressBar(groupSpell, SWT.HORIZONTAL);
        spellBar.setMaximum(SPELL_BAR_MAX);
        spellBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2,
                1));

        lLimitSpell = new Label(groupSpell, SWT.LEFT);
        lLimitSpell.setText(SPELL_UNTIL_LABEL);
        lLimitSpell.setVisible(false);
        lLimitSpell.setLayoutData(gdHgrab);

        cSpell = new CCombo(groupSpell, SWT.READ_ONLY);
        cSpell.setEditable(false);
        cSpell.setVisibleItemCount(SPELL_VALUE.length);
        cSpell.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        cSpell.setVisible(false);
        for (String val : SPELL_VALUE) {
            cSpell.add(val);
        }
        cSpell.select(0);

        btnModify = new Button(groupSpell, SWT.NONE);
        btnModify.setText(CHANGE_SPELL_TEXT);
        btnModify.setVisible(false);
        btnModify
                .setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        btnSpell = new Button(groupSpell, SWT.NONE);
        btnSpell.setText(SPELL_BTN_TEXT);
        btnSpell.setVisible(false);
        btnSpell.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    }

    private String getSpellValue() {
        return cSpell.getText().split(" ")[0];
    }

    private AddItem getAddItemShell() {
        return new AddItem(this);
    }

    private Exit getExitUI(boolean exit) {
        return new Exit(this, exit);
    }

    private Modify getModifyShell() {
        return new Modify(this);
    }

    List<SpellableItem> getSpellableItems(Reader reader) {
        List<Item> items = TextParser.getItems(reader);
        List<SpellableItem> spellItems = Lists.newArrayList();

        for (Item item : items) {
            Map<Property, String> propertiesAndValues = item
                    .getPropertiesAndValues();
            if (propertiesAndValues.containsKey(Property.SPELLED)) {
                spellItems.add(new SpellableItem(item));
            }
        }

        return spellItems;
    }

    private void getOpenService() {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                FileDialog dialog = new FileDialog(shell, SWT.OPEN);
                dialog.setFilterExtensions(new String[]{"*.xml", "*.txt"});

                String fileName = dialog.open();

                loadItemsFrom(fileName);
            }
        });
    }

    private void loadItemsFrom(String fileName) {
        List<SpellableItem> itemsFromXML = null;

        if (fileName != null) {
            exceptionInfo.setText(OPENED_FILE + fileName);

            Reader reader = null;
            try {
                FileInputStream fileOutputStream = new FileInputStream(fileName);
                reader = new InputStreamReader(fileOutputStream, ENCODING);
                itemsFromXML = new XMLIO().getItemsFromXML(reader);
                if (itemsFromXML == null || itemsFromXML.size() == 0) {
                    itemsFromXML = getSpellableItems(reader);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (itemsFromXML != null && itemsFromXML.size() != 0) {
                loadItems(itemsFromXML);
            }
        }
    }

    void loadItems(List<SpellableItem> items) {
        setItems(sortItems(items));

        String text = "*Заклясть можно %d предмет(ов)\n";
        String deletedItems = "*Срок действия заклятия истёк у %d предмет(ов):\n";

        int canBeSpelled = 0;
        int deleted = 0;
        for (SpellableItem item : items) {
            if (item.isSpellable()) {
                canBeSpelled++;
            } else if (!item.isAlive()) {
                deleted++;
                deletedItems = deletedItems.concat(String.format("\t%s\n",
                        item.getName()));
            }
        }
        if (deleted > 0) {
            deletedItems = String.format(deletedItems, deleted);
            text = String.format(text, canBeSpelled) + deletedItems;
        } else {
            text = String.format(text, canBeSpelled);
        }

        setSaved(true);
        tableRefresh();
        textInfo.setText(text);
    }

    List<SpellableItem> sortItems(List<SpellableItem> items) {
        Collections.sort(items, new Comparator<SpellableItem>() {

            @Override
            public int compare(SpellableItem o1, SpellableItem o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        return items;
    }

    private void getSaveService() {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                FileDialog dialog = new FileDialog(shell, SWT.SAVE);
                dialog.setFilterExtensions(new String[]{"*.xml"});
                String fileName = dialog.open();
                if (fileName != null)
                    try {
                        saveItemsToFile(items, fileName);
                        isSaved = true;
                        appProperties.setLoadAtStart(appProperties
                                .getLoadAtStart());
                        appProperties.setDefaultFile(fileName);
                        appProperties.setDefaultSpell(getSpellValue());
                        appProperties.save();
                    } catch (IOException e) {
                        e.printStackTrace();
                        isSaved = false;
                    }
            }
        });
    }

    void saveItemsToFile(List<SpellableItem> items, String filePath)
            throws UnsupportedEncodingException, FileNotFoundException {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        Writer writer = new OutputStreamWriter(fileOutputStream, ENCODING);
        new XMLIO().saveItemsTo(items, writer);
    }

    void tableRefresh(int selectIndex) {
        table.removeAll();

        int i = 1;

        for (SpellableItem item : items) {
            TableItem tableItem = new TableItem(table, SWT.None);
            tableItem.setText(new String[]{String.format("%2d", i),
                    item.getName()});

            if (!item.isAlive()) {
                tableItem.setFont(REMOVED_FONT);
                tableItem.setForeground(Display.getCurrent().getSystemColor(
                        SWT.COLOR_GRAY));
            } else if (item.isSpellable()) {
                if (item.isLessThen24hAlive()) {
                    tableItem.setForeground(Display.getCurrent()
                            .getSystemColor(SWT.COLOR_RED));
                } else {
                    tableItem.setForeground(Display.getCurrent()
                            .getSystemColor(SWT.COLOR_DARK_GREEN));
                }
            }

            i++;
        }

        for (TableColumn col : table.getColumns()) {
            col.pack();
        }

        if (selectIndex == -1) {
            resetLabels();
        } else {
            table.select(selectIndex);
            setLabels(selectIndex);
        }
    }

    void tableRefresh() {
        tableRefresh(-1);
    }

    private void setLabels(int selectIndex) {
        SpellableItem item = items.get(selectIndex);

        btnModify.setVisible(true);

        if (item.getComment() == null) {
            textInfo.setText(item.toString());
        } else {
            textInfo.setText(item.toString() + "\n" + "\n" + item.getComment());
        }

        lUntilShowDate.setText(Utils.dateFormat.print(item.getDate()));
        lShowFromDate.setText(Utils.dateFormat.print(item
                .getSpellStartCalendar()));

        if (item.isSpellable()) {
            lLimitSpell.setVisible(true);
            btnSpell.setVisible(true);
            cSpell.setVisible(true);
        } else {
            lLimitSpell.setVisible(false);
            btnSpell.setVisible(false);
            cSpell.setVisible(false);
        }

        lSpellUntill.setText(item.getTimeLeft());

        int remainingTimeInMins = getRemainingTimeInMins(item);
        if (remainingTimeInMins > SPELL_BAR_MAX) {
            spellBar.setSelection(SPELL_BAR_MAX);
        } else {
            spellBar.setSelection(remainingTimeInMins);
        }
    }

    private int getRemainingTimeInMins(SpellableItem item) {
        DateTime end = item.getDate();
        DateTime current = DateTime.now();

        long endMillis = end.getMillis();
        long currentMillis = current.getMillis();
        long minutes = (endMillis - currentMillis) / 60000;
        return (int) minutes;
    }

    private void resetLabels() {
        btnModify.setVisible(false);
        textInfo.setText("");
        lUntilShowDate.setText("");
        lShowFromDate.setText("");
        lSpellUntill.setText("");
        lLimitSpell.setVisible(false);
        btnSpell.setVisible(false);
        cSpell.setVisible(false);
        spellBar.setSelection(0);
    }

    List<SpellableItem> getCurrentItems() {
        return this.items;
    }

    SpellableItem getSelecteditem() {
        return this.items.get(table.getSelectionIndex());
    }

    void closeMainShell() {
        shell.dispose();
    }

    void setItems(List<SpellableItem> spellableItems) {
        this.items = spellableItems;
    }

    private void doPostActions() {
        boolean loadAtStart = this.appProperties.getLoadAtStart();
        String defaultFile = this.appProperties.getDefaultFile();

        if (loadAtStart && new File(defaultFile).exists()) {
            loadItemsFrom(defaultFile);
        }

        String defaultSpell = this.appProperties.getDefaultSpell();
        if (defaultSpell != null) {
            for (int i = 0; i < SPELL_VALUE.length; i++) {
                if (SPELL_VALUE[i].startsWith(defaultSpell)) {
                    cSpell.select(i);
                    break;
                }
            }
        }
    }
}
