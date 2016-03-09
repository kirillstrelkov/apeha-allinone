package apeha.allinone.gui.search;

import apeha.allinone.common.Category;
import apeha.allinone.db.DbHandler;
import apeha.allinone.gui.CommonData;
import apeha.allinone.gui.CommonGUI;
import apeha.allinone.gui.ItemGui;
import apeha.allinone.gui.fitting.SetSearchGui;
import apeha.allinone.item.Property;
import apeha.allinone.search.ItemSearch;
import apeha.allinone.search.ItemWithScore;
import apeha.allinone.search.SearchType;
import com.google.common.collect.Lists;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchGUI extends CommonGUI {
    public static final String SHELL_TITLE = "Поиск вещей";
    private static final Point SIZE = new Point(1100, 620);
    private static final int COLUMNS = 2;
    private Button btnSearch;
    private ScrolledComposite scrolledComposite;
    private Composite rowComposite;
    private Combo cSearchType;
    private Label labelPrice;
    private Text textPrice;
    private Label labelLevel;
    private Combo cLevel;
    private Combo cLimit;
    private Combo cCategory;
    private Label lSearchType;
    private Label lFound;
    private Label lItems;
    private Label lLimit;
    private List<Button> params = Lists.newLinkedList();

    public SearchGUI(Shell shell) {
        super(shell);
        this.shell = shell;
        createUI();
    }

    public SearchGUI(Composite composite) {
        super(composite);
        createUI();
    }

    public static void main(String[] args) {
        // TODO remove
        Display display = Display.getDefault();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        Composite composite2 = new Composite(shell, SWT.DOUBLE_BUFFERED);
        SearchGUI searchGUI = new SearchGUI(composite2);
        searchGUI.shell.open();
        searchGUI.shell.layout();
        while (!searchGUI.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private void createScrolledComposite() {
        GridData gridData = new GridData();
        gridData.horizontalSpan = COLUMNS;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;

        scrolledComposite = new ScrolledComposite(composite, SWT.H_SCROLL
                | SWT.BORDER | SWT.V_SCROLL);
        scrolledComposite.setLayout(new FillLayout());
        scrolledComposite.setLayoutData(gridData);
        createComposite();
        scrolledComposite.setContent(rowComposite);
    }

    private void createComposite() {
        rowComposite = new Composite(scrolledComposite, SWT.DOUBLE_BUFFERED);
        rowComposite.setLayout(new RowLayout());
    }

    private void createUI() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = COLUMNS;
        gridLayout.makeColumnsEqualWidth = false;

        composite.setLayout(gridLayout);

        createItemParams();
        createSearchParams();
        createScrolledComposite();

        GridData gdSpan6HfillVcenter = new GridData();
        gdSpan6HfillVcenter.horizontalSpan = COLUMNS;
        gdSpan6HfillVcenter.horizontalAlignment = GridData.FILL;
        gdSpan6HfillVcenter.verticalAlignment = GridData.CENTER;

        lFound = new Label(composite, SWT.NONE);
        lFound.setText(CommonData.FOUND_ITEMS_TEXT);
        lFound.setLayoutData(gdSpan6HfillVcenter);

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

        Composite innerComposite = new Composite(composite, SWT.DOUBLE_BUFFERED);
        innerComposite.setLayoutData(gdFillAndHexpand);
        innerComposite.setLayout(layout);

        GridData gdHendVcenter = new GridData();
        gdHendVcenter.grabExcessHorizontalSpace = true;
        gdHendVcenter.horizontalAlignment = GridData.FILL;

        labelPrice = new Label(innerComposite, SWT.RIGHT);
        labelPrice.setText(CommonData.PRICE_IN_SHOP);
        labelPrice.setLayoutData(gdHendVcenter);

        textPrice = new Text(innerComposite, SWT.BORDER);
        textPrice.setText(DbHandler.TOP_PRICE);

        labelLevel = new Label(innerComposite, SWT.RIGHT);
        labelLevel.setText(CommonData.REQUIRED_LEVEL);
        labelLevel.setLayoutData(gdHendVcenter);

        cLevel = new Combo(innerComposite, SWT.READ_ONLY);

        lItems = new Label(innerComposite, SWT.RIGHT);
        lItems.setText(CommonData.ITEMS_TEXT);
        lItems.setLayoutData(gdHendVcenter);

        cCategory = new Combo(innerComposite, SWT.READ_ONLY);

        lLimit = new Label(innerComposite, SWT.RIGHT);
        lLimit.setText(CommonData.ITEM_LIMIT);
        lLimit.setLayoutData(gdHendVcenter);

        cLimit = new Combo(innerComposite, SWT.READ_ONLY);

        lSearchType = new Label(innerComposite, SWT.RIGHT);
        lSearchType.setText(CommonData.SEARCH_TYPE_TEXT);
        lSearchType.setLayoutData(gdHendVcenter);
        lSearchType.setToolTipText(CommonData.SEARCH_TYPE_TOOLTIP);

        cSearchType = new Combo(innerComposite, SWT.READ_ONLY);
        cSearchType.setToolTipText(CommonData.SEARCH_TYPE_TOOLTIP);

        btnSearch = new Button(innerComposite, SWT.PUSH);
        btnSearch.setText(CommonData.SEARCH_BTN_TEXT);
        btnSearch.setLayoutData(gdHspan2);

        btnSearch.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (rowComposite != null)
                    rowComposite.dispose();
                rowComposite = new Composite(scrolledComposite,
                        SWT.DOUBLE_BUFFERED);
                rowComposite.setLayout(new RowLayout());
                try {
                    int level = Integer.parseInt(cLevel.getText());
                    int limit = Integer.parseInt(cLimit.getText());
                    String price = textPrice.getText();
                    String category = cCategory.getText();
                    SearchType searchType = SearchType.getType(cSearchType
                            .getText());
                    List<String> selectedParams = getSelectedParams();

                    List<ItemWithScore> found = ItemSearch.findItems(price,
                            level, limit, category, searchType, selectedParams);
                    for (ItemWithScore iws : found)
                        ItemGui.newItemGui(rowComposite, iws, searchType);

                    rowComposite.pack();
                    scrolledComposite.setContent(rowComposite);
                    lFound.setText(CommonData.FOUND_ITEMS_TEXT
                            + String.valueOf(found.size()));
                } catch (Exception e2) {
                    lFound.setText("Ошибка при вводе данных.");
                }

            }

        });

        fillCombos();
    }

    private void createItemParams() {
        final int columns = 4;
        final GridData gBeginVfill = new GridData(SWT.BEGINNING, SWT.FILL,
                false, false);
        final GridData hBeginVfillHspan1 = new GridData(SWT.BEGINNING,
                SWT.FILL, false, false, columns, 1);

        Group group = new Group(composite, SWT.SHADOW_NONE);
        group.setText(SetSearchGui.PARAMETERS);
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
        List<String> listString = new ArrayList<String>();
        for (Button checkbox : params)
            if (checkbox.getSelection())
                listString.add(checkbox.getText());
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
        cCategory.add(Category.ALL.getType());
        List<String> categories = Category.getTypes();
        Collections.sort(categories);
        for (String cat : categories) {
            cCategory.add(cat);
        }
        cCategory.select(0);
    }
}
