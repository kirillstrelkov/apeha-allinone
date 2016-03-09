package apeha.allinone.gui.fitting;

import apeha.allinone.common.Category;
import apeha.allinone.common.Utils;
import apeha.allinone.db.DbHandler;
import apeha.allinone.fitting.SearchedSet;
import apeha.allinone.gui.CommonData;
import apeha.allinone.gui.CommonGUI;
import apeha.allinone.gui.status.ProgressDialogGUI;
import apeha.allinone.gui.status.Status;
import apeha.allinone.item.Item;
import apeha.allinone.item.Property;
import apeha.allinone.search.SearchType;
import com.google.common.collect.Lists;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SetSearchGui extends CommonGUI {

    public static final String PARAMETERS = "Параметры";
    public static final String SHELL_TITLE = "Подбор комплекта";
    private static final String CANCEL = "Отменить";
    private static final String EXCEPT = "Исключить";
    private static final String SKIP_ITEMS = "Исключить предметы";
    private static final String WAS_STOPPED_TEXT = "Поиск был остановлен.";
    private static final String NOT_FOUND_TEXT = "Подходящие комплекты не найдены.\n"
            + "Попробуйте поменять стоимость вещи или комплекта.\n"
            + "Попробуйте изменить параметры поиска или тип поиска.";
    private static final String SET_PRICE = "Стоимость комплекта:";
    private static final String SKIP_INFO = "Введите названия предметов которые надо исключить из подбора.";
    private static final String SKIPPED_ITEMS = "Исключенные предметы";
    private static final String ITEMS_IN_SET = "Предметы в комплекте";
    private static final String SEARCH_BTN_TEXT = "Подобрать комплекты";
    private static final String ITEM_LIMIT = "Количество комплектов:";
    private static final String PRICE_IN_SHOP = "Стоимость вещи:";
    private static final Point SIZE = new Point(1100, 720);
    private static final int COLUMNS = 2;
    private static final String PROGRESS_TITLE_TEXT = "Поиск всевозможных вариантов...";
    private Status status;
    private Button btnSearch;
    private Button btnSkippedItems;
    private ScrolledComposite scrolledComposite;
    private Composite rowComposite;
    private Combo cSearchType;
    private Label labelPrice;
    private Text textPrice;
    private Label labelSetPrice;
    private Text textSetPrice;
    private Label labelLevel;
    private Combo cLevel;
    private Combo cLimit;
    private Label lSearchType;
    private Label lLimit;
    private List<Button> categories = Lists.newLinkedList();
    private List<Button> params = Lists.newLinkedList();
    private List<String> itemsToSkip = Lists.newLinkedList();
    private Map<Category, List<Item>> categoryAndItems;

    public SetSearchGui(Shell shell) {
        super(shell);
        this.shell = shell;
        createUI();
    }

    public SetSearchGui(Composite composite) {
        super(composite);
        categoryAndItems = DbHandler.getCategoryAndItems();
        createUI();
    }

    public static void main(String[] args) {
        // TODO remove
        Display display = Display.getDefault();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        Composite composite2 = new Composite(shell, SWT.DOUBLE_BUFFERED);
        SetSearchGui searchGUI = new SetSearchGui(composite2);
        searchGUI.shell.open();
        searchGUI.shell.layout();
        while (!searchGUI.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public void setInnerShellPosition(Shell innerShell) {
        Point location = shell.getShell().getLocation();
        Point size = shell.getShell().getSize();
        Point innerSize = innerShell.getSize();
        innerShell.setLocation(location.x + (size.x - innerSize.x) / 2,
                location.y + (size.y - innerSize.y) / 2);
    }

    private void createScrolledComposite() {
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.verticalSpan = 1;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;

        scrolledComposite = new ScrolledComposite(composite, SWT.H_SCROLL
                | SWT.BORDER | SWT.V_SCROLL);
        scrolledComposite.setLayout(new FillLayout());
        scrolledComposite.setLayoutData(gridData);

    }

    private void createUI() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = COLUMNS;
        gridLayout.makeColumnsEqualWidth = false;

        composite.setLayout(gridLayout);

        createItemParams();
        createSearchParams();
        createCategories();
        createScrolledComposite();

        shell.setText(SHELL_TITLE);
        shell.pack();
        shell.setSize(SIZE);
    }

    private void createSearchParams() {
        final GridData gdFillAndHexpand = new GridData(SWT.FILL, SWT.FILL,
                true, false);
        final GridLayout layout = new GridLayout(2, false);
        final GridData gdHspan2 = new GridData(SWT.FILL, SWT.CENTER, true,
                false, 2, 1);

        GridData gdHendVcenter = new GridData();
        gdHendVcenter.grabExcessHorizontalSpace = true;
        gdHendVcenter.horizontalAlignment = GridData.FILL;

        Composite innerComposite = new Composite(composite, SWT.DOUBLE_BUFFERED);
        innerComposite.setLayoutData(gdFillAndHexpand);
        innerComposite.setLayout(layout);

        labelPrice = new Label(innerComposite, SWT.RIGHT);
        labelPrice.setText(PRICE_IN_SHOP);
        labelPrice.setLayoutData(gdHendVcenter);

        textPrice = new Text(innerComposite, SWT.BORDER);
        textPrice.setText(DbHandler.TOP_PRICE);

        labelSetPrice = new Label(innerComposite, SWT.RIGHT);
        labelSetPrice.setText(SET_PRICE);
        labelSetPrice.setLayoutData(gdHendVcenter);

        textSetPrice = new Text(innerComposite, SWT.BORDER);
        textSetPrice.setText(new BigDecimal(DbHandler.TOP_PRICE).multiply(
                new BigDecimal(17)).toString());

        labelLevel = new Label(innerComposite, SWT.RIGHT);
        labelLevel.setText(CommonData.REQUIRED_LEVEL);
        labelLevel.setLayoutData(gdHendVcenter);

        cLevel = new Combo(innerComposite, SWT.READ_ONLY);

        lLimit = new Label(innerComposite, SWT.RIGHT);
        lLimit.setText(ITEM_LIMIT);
        lLimit.setLayoutData(gdHendVcenter);

        cLimit = new Combo(innerComposite, SWT.READ_ONLY);

        lSearchType = new Label(innerComposite, SWT.RIGHT);
        lSearchType.setText(CommonData.SEARCH_TYPE_TEXT);
        lSearchType.setLayoutData(gdHendVcenter);
        lSearchType.setToolTipText(CommonData.SEARCH_TYPE_TOOLTIP);

        cSearchType = new Combo(innerComposite, SWT.READ_ONLY);
        cSearchType.setToolTipText(CommonData.SEARCH_TYPE_TOOLTIP);

        btnSearch = new Button(innerComposite, SWT.PUSH);
        btnSearch.setText(SEARCH_BTN_TEXT);
        btnSearch.setLayoutData(gdHspan2);
        btnSearch.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                String maxPrice = textPrice.getText();
                String maxSetPrice = textSetPrice.getText();
                int maxLevel = Utils.getInteger("\\d+", cLevel.getText());
                int limit = Utils.getInteger("\\d+", cLimit.getText());
                SearchType searchType = SearchType.getType(cSearchType
                        .getText());
                List<Category> selectedCategories = getSelectedCategories();
                List<String> selectedParams = getSelectedParams();

                if (rowComposite != null) {
                    for (Control control : rowComposite.getChildren()) {
                        control.dispose();
                    }
                }

                rowComposite = new Composite(scrolledComposite,
                        SWT.DOUBLE_BUFFERED);
                rowComposite.setBackgroundMode(SWT.INHERIT_FORCE);
                rowComposite.setLayout(new RowLayout());

                findSets(maxPrice, maxSetPrice, maxLevel, limit, searchType,
                        selectedCategories, selectedParams);
            }
        });

        fillCombos();
    }

    private void createCategories() {
        final GridLayout layout = new GridLayout(11, false);
        final GridData layoutData = new GridData(SWT.BEGINNING, SWT.CENTER,
                false, false, 1, 1);

        Composite innerComposite = new Composite(composite, SWT.DOUBLE_BUFFERED);
        innerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                false, 2, 1));
        innerComposite.setLayout(new GridLayout(2, false));

        Group categoriesComposite = new Group(innerComposite, SWT.SHADOW_NONE);
        categoriesComposite.setText(ITEMS_IN_SET);
        categoriesComposite.setLayoutData(layoutData);
        categoriesComposite.setLayout(layout);

        final Button button = new Button(categoriesComposite, SWT.CHECK);
        button.setText(Category.ALL.getType());
        button.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                boolean selection = button.getSelection();
                for (Button button : categories)
                    if (selection != button.getSelection())
                        button.setSelection(selection);
            }
        });

        for (Category cat : categoryAndItems.keySet()) {
            if (!cat.equals(Category.SHOOTING_ARMS)) {
                Button button2 = new Button(categoriesComposite, SWT.CHECK);
                if (!cat.equals(Category.SHIELDS))
                    button2.setSelection(true);
                button2.setText(cat.getType());
                categories.add(button2);
            }
        }
        btnSkippedItems = new Button(innerComposite, SWT.PUSH);
        btnSkippedItems.setText(SKIP_ITEMS);
        btnSkippedItems.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false));
        btnSkippedItems.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                final Shell innerShell = new Shell(shell, SWT.PRIMARY_MODAL
                        | SWT.CLOSE | SWT.RESIZE);
                innerShell.setSize(400, 400);
                innerShell.setText(SKIPPED_ITEMS);
                innerShell.setLayout(new GridLayout(2, true));
                setInnerShellPosition(innerShell);

                final Text txtItems = new Text(innerShell, SWT.MULTI
                        | SWT.V_SCROLL);
                if (itemsToSkip.size() == 0) {
                    txtItems.setText(SKIP_INFO);
                }

                for (String item : itemsToSkip) {
                    txtItems.append(item + "\n");
                }

                txtItems.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                        true, 2, 1));

                Button btnCancel = new Button(innerShell, SWT.PUSH);
                btnCancel.setText(CANCEL);
                btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                        false));
                btnCancel.addListener(SWT.Selection, new Listener() {

                    @Override
                    public void handleEvent(Event event) {
                        innerShell.close();
                    }
                });

                Button btnExcept = new Button(innerShell, SWT.PUSH);
                btnExcept.setText(EXCEPT);
                btnExcept.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                        false));
                btnExcept.addListener(SWT.Selection, new Listener() {

                    @Override
                    public void handleEvent(Event event) {
                        itemsToSkip.clear();
                        for (String line : txtItems.getText().split("\n")) {
                            itemsToSkip.add(line.trim());
                        }
                        innerShell.close();
                    }
                });

                innerShell.open();
            }
        });

        categoriesComposite.pack();
    }

    private void createItemParams() {
        final int columns = 4;
        final GridData gBeginVfill = new GridData(SWT.BEGINNING, SWT.FILL,
                false, false);
        final GridData hBeginVfillHspan1 = new GridData(SWT.BEGINNING,
                SWT.FILL, false, false, columns, 1);

        Group group = new Group(composite, SWT.SHADOW_NONE);
        group.setText(PARAMETERS);
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = columns;

        group.setLayout(gridLayout);

        for (int i = 0; i < Property.STATS.size(); i++) {
            Property stat = Property.STATS.get(i);
            Button checkbox = new Button(group, SWT.CHECK);
            checkbox.setText(stat.getName());
            params.add(checkbox);

            if (i > 1) {
                checkbox.setLayoutData(gBeginVfill);

                int index = i - 2;
                stat = Property.MASTERIES.get(index);
                checkbox = new Button(group, SWT.CHECK);
                checkbox.setText(stat.getName());
                params.add(checkbox);

                stat = Property.GUARDS.get(index);
                checkbox = new Button(group, SWT.CHECK);
                checkbox.setText(stat.getName());
                params.add(checkbox);

                stat = Property.GUARDS_OF_ENEMY.get(index);
                checkbox = new Button(group, SWT.CHECK);
                checkbox.setText(stat.getName());
                params.add(checkbox);
            } else
                checkbox.setLayoutData(hBeginVfillHspan1);
        }

        group.pack();
    }

    private List<String> getSelectedParams() {
        List<String> listString = new LinkedList<String>();

        for (Button checkbox : params)
            if (checkbox.getSelection())
                listString.add(checkbox.getText());

        return listString;
    }

    private List<Category> getSelectedCategories() {
        List<Category> listString = new LinkedList<Category>();

        for (Button checkbox : categories)
            if (!checkbox.getText().equals(Category.ALL.getType())
                    && checkbox.getSelection())
                listString.add(Category.getCategoryByType(checkbox.getText()));

        return listString;
    }

    private void fillCombos() {
        for (int i = 1; i <= DbHandler.TOP_LEVEL; i++)
            cLevel.add(String.valueOf(i));
        cLevel.select(cLevel.getItemCount() - 1);

        for (String type : SearchType.getTypes())
            cSearchType.add(type);
        cSearchType.select(1);

        for (int limit : CommonData.SEARCH_LIMITS)
            cLimit.add(String.valueOf(limit));

        cLimit.select(0);
    }

    private void findSets(final String maxPrice, final String maxSetPrice,
                          final int maxLevel, final int limit, final SearchType searchType,
                          final List<Category> selectedCategories,
                          final List<String> selectedParams) {
        status = new Status();
        ProgressDialogGUI progressGUI = new ProgressDialogGUI(
                PROGRESS_TITLE_TEXT, status);
        this.setInnerShellPosition(progressGUI.getShell());
        progressGUI.getShell().setVisible(true);
        progressGUI.executeWatchingThread();

        final SelectionWithStatus selection = new SelectionWithStatus(status,
                maxSetPrice, maxPrice, maxLevel, limit, searchType,
                selectedCategories, selectedParams, itemsToSkip);

        Thread findThread = new Thread(new Runnable() {

            @Override
            public void run() {
                final List<SearchedSet> selectSets = selection.findSets();
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        if (selectSets != null && selectSets.size() > 0)
                            for (SearchedSet set : selectSets) {
                                Text text = new Text(rowComposite, SWT.MULTI
                                        | SWT.READ_ONLY);
                                text.setText(set.toString());
                            }
                        else if (selectSets == null) {
                            Label text = new Label(rowComposite, SWT.NONE);
                            text.setText(WAS_STOPPED_TEXT);
                        } else {
                            Label text = new Label(rowComposite, SWT.NONE);
                            text.setText(NOT_FOUND_TEXT);
                        }

                        rowComposite.pack();
                        scrolledComposite.setContent(rowComposite);
                    }
                });
                status.setDone(true);
            }
        });
        findThread.setName("SearchingThread");
        findThread.setDaemon(true);
        findThread.start();

        Thread stoppingThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (!status.isDone()) {
                }
                selection.setStopped(true);
            }
        });
        stoppingThread.setName("SelectionStoppingThread");
        stoppingThread.setDaemon(true);
        stoppingThread.start();
    }
}