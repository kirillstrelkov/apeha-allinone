package apeha.allinone.gui.calc;

import apeha.allinone.calc.Calculator;
import apeha.allinone.common.Category;
import apeha.allinone.common.Utils;
import apeha.allinone.db.DbHandler;
import apeha.allinone.gui.CommonGUI;
import apeha.allinone.gui.ItemGui;
import apeha.allinone.item.Item;
import apeha.allinone.item.Property;
import com.google.common.collect.Lists;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CalculatorGUI extends CommonGUI {
    public static final String SHELL_TITLE = "Калькулятор торговца";
    private static String DEFAULT_TEXT = "1000.00";
    Button checkBoxBlues = null;
    Button checkBoxTownIsSeized = null;
    Text textPrice = null;
    Text textDurab = null;
    Text textProfit = null;
    Text priceBuy = null;
    Text valPercentBuy = null;
    Scale scalePercentBuy = null;
    Text priceSell = null;
    Text valPercentSell = null;
    Scale scalePercentSell = null;
    Text textBlues = null;
    Scale scaleBlues = null;
    Text textProfitInBlues = null;
    Text textAllProfit = null;
    Text labelValBuyCost = null;
    Text labelValSellIncome = null;
    private Shell shell = null;
    private Label labelPrice = null;
    private Label labelDurability = null;
    private Label labelProfit = null;
    private Group groupBuy = null;
    private Group groupSell = null;
    private Label labelPriceBuy = null;
    private Label labelPercentBuy = null;
    private Label labelPriceSell = null;
    private Label labelPercentSell = null;
    private Label labelProfitInBlues = null;
    private Label labelAllProfit = null;
    private Combo comboType = null;
    private Combo comboName = null;
    private Label labelType = null;
    private Label labelName = null;
    private Label labelBuyCost = null;
    private Label labelSellIncome = null;
    private Composite itemGuiComposite = null;
    private Map<Category, List<Item>> mainMap = null;
    private List<Item> items = null;
    private ItemGui<Item> itemGUI = null;
    private Item item = null;
    private CalcHelp help = null;

    public CalculatorGUI(Composite composite) {
        super(composite);
        shell = composite.getShell();
        help = new CalcHelp(this);
        createUI();
    }

    public static void main(String[] args) {
        // TODO remove
        Display display = Display.getDefault();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        Composite composite2 = new Composite(shell, SWT.DOUBLE_BUFFERED);
        CalculatorGUI gui = new CalculatorGUI(composite2);
        gui.shell.open();
        gui.shell.layout();
        while (!gui.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private void createUI() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.makeColumnsEqualWidth = true;
        composite.setLayout(gridLayout);

        createComposite();

        GridData gdRIGHT = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        GridData gdLeft = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        GridData gdHfillVcenter = new GridData(SWT.FILL, SWT.CENTER, false,
                false);

        checkBoxTownIsSeized = new Button(composite, SWT.CHECK);
        checkBoxTownIsSeized.setLayoutData(gdRIGHT);
        checkBoxTownIsSeized.setText(CalculatorData.TOWN_HEISTED);

        checkBoxBlues = new Button(composite, SWT.CHECK);
        checkBoxBlues.setLayoutData(gdLeft);
        checkBoxBlues.setText(CalculatorData.BLUES);

        labelType = new Label(composite, SWT.RIGHT);
        labelType.setLayoutData(gdHfillVcenter);
        labelType.setText(CalculatorData.ITEM_TYPE);
        comboType = new Combo(composite, SWT.READ_ONLY);
        comboType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        labelName = new Label(composite, SWT.RIGHT);
        labelName.setLayoutData(gdHfillVcenter);
        labelName.setText(CalculatorData.ITEM_NAME);
        labelName.setAlignment(SWT.RIGHT);

        comboName = new Combo(composite, SWT.DROP_DOWN);
        comboName
                .setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        labelDurability = new Label(composite, SWT.RIGHT);
        String probability = Property.DURABILITY.getName();
        labelDurability.setText(probability.substring(0, 1).toUpperCase()
                + probability.substring(1) + ":");
        labelDurability.setLayoutData(gdHfillVcenter);
        textDurab = new Text(composite, SWT.BORDER);
        textDurab.setText(DEFAULT_TEXT);
        textDurab.setTextLimit(10);

        labelPrice = new Label(composite, SWT.RIGHT);
        labelPrice.setText(Property.PRICE_IN_SHOP.getName() + ":");
        labelPrice.setLayoutData(gdHfillVcenter);
        textPrice = new Text(composite, SWT.BORDER);
        textPrice.setText(DEFAULT_TEXT);
        textPrice.setTextLimit(10);
        textPrice.setEditable(false);

        createGroupBuy();
        createGroupSell();

        labelProfit = new Label(composite, SWT.RIGHT);
        labelProfit.setText(CalculatorData.PROFIT);
        labelProfit.setLayoutData(gdHfillVcenter);
        textProfit = new Text(composite, SWT.READ_ONLY);

        labelProfitInBlues = new Label(composite, SWT.RIGHT);
        labelProfitInBlues.setText(CalculatorData.PROFIT_IN_BLUES);
        labelProfitInBlues.setVisible(false);
        labelProfitInBlues.setLayoutData(gdHfillVcenter);
        textProfitInBlues = new Text(composite, SWT.READ_ONLY);
        textProfitInBlues.setForeground(CalculatorData.BLUE_COLOR);
        textProfitInBlues.setVisible(false);

        labelAllProfit = new Label(composite, SWT.RIGHT);
        labelAllProfit.setText(CalculatorData.COMMON_PROFIT);
        labelAllProfit.setVisible(false);
        labelAllProfit.setLayoutData(gdHfillVcenter);
        textAllProfit = new Text(composite, SWT.READ_ONLY);
        textAllProfit.setVisible(false);

        shell.pack();

        loadData();
        shell.setText(SHELL_TITLE);
        shell.setSize(CalculatorData.SIZE);

        createListeners();
    }

    private void createListeners() {
        checkBoxTownIsSeized.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                help.updateCostOrIncome(labelValBuyCost);
                help.updateCostOrIncome(labelValSellIncome);
                help.calculate();
            }

        });

        checkBoxBlues.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (checkBoxBlues.getSelection()) {
                    textBlues.setVisible(true);
                    scaleBlues.setVisible(true);
                    labelAllProfit.setVisible(true);
                    labelProfitInBlues.setVisible(true);
                    textAllProfit.setVisible(true);
                    textProfitInBlues.setVisible(true);
                } else {
                    textBlues.setVisible(false);
                    scaleBlues.setVisible(false);
                    labelAllProfit.setVisible(false);
                    labelProfitInBlues.setVisible(false);
                    textAllProfit.setVisible(false);
                    textProfitInBlues.setVisible(false);
                }
                scaleBlues.setSelection(0);
                textBlues.setText("+"
                        + Calculator.intToBigDecimal(scaleBlues.getSelection()));
                help.calculate();
            }
        });

        comboType.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                fillComboName();
                loadItemByName();
            }
        });

        comboName.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                loadItemByName();
            }

        });

        textDurab.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                String durab = textDurab.getText();
                String price = textPrice.getText();
                BigDecimal calculateRepairCosts = Calculator
                        .calculateRepairCosts(durab, price);
                String correctDur = item.getPropertiesAndValues().get(
                        Property.DURABILITY);
                if (calculateRepairCosts == null
                        || !durab.substring(durab.indexOf("/") + 1).equals(
                        correctDur)) {
                    String maxDurab = item.getPropertiesAndValues().get(
                            Property.DURABILITY);
                    textDurab.setText(maxDurab + "/" + maxDurab);
                }
                help.calculate();
            }
        });

        valPercentBuy.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (valPercentBuy.isFocusControl())
                    help.eventPercentChange(valPercentBuy, "75.00%");
            }
        });

        scalePercentBuy.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                help.eventSliderChanged(scalePercentBuy);
            }

        });

        priceBuy.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (priceBuy.isFocusControl()) {
                    help.eventPriceChanged(priceBuy);
                }
            }
        });

        valPercentSell.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (valPercentSell.isFocusControl()) {
                    help.eventPercentChange(valPercentSell, "100.00%");
                }
            }
        });

        scalePercentSell.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                help.eventSliderChanged(scalePercentSell);
            }
        });

        priceSell.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                help.eventPriceChanged(priceSell);
            }
        });

        textBlues.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                help.eventPriceChanged(textBlues);
            }
        });

        scaleBlues.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                help.eventSliderChanged(scaleBlues);
            }
        });
    }

    private void loadData() {
        priceBuy.setText("780.00");
        priceSell.setText("1040.00");

        valPercentBuy.setText("75.00%");
        labelValBuyCost.setText("858.00");

        valPercentSell.setText("100.00%");
        labelValSellIncome.setText("1040.00");

        textBlues.setText("+0.00");

        mainMap = Utils.getSortedCategoryAndItem(DbHandler.getHandler()
                .getCategoryAndItems());

        fillComboType();
        fillComboName();
        loadItemByName();

        textPrice.setText(item.getPropertiesAndValues().get(
                Property.PRICE_IN_SHOP));
    }

    private void fillComboType() {
        comboType.removeAll();

        List<String> categories = Lists.newLinkedList(Category.getTypes());
        Collections.sort(categories);

        for (String cat : categories) {
            comboType.add(cat);
        }
        comboType.select(0);
    }

    private void fillComboName() {
        comboName.removeAll();
        items = mainMap.get(Category.getCategoryByType(comboType.getText()));
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            comboName.add(iterator.next().getName());
        }
        comboName.select(0);
    }

    private void loadItemByName() {
        if (itemGUI != null)
            itemGUI.removeAll();

        item = Utils.getItemFromList(comboName.getText(), items);
        Map<Property, String> properties = item.getPropertiesAndValues();

        itemGUI = ItemGui.newItemGui(itemGuiComposite, item);
        itemGuiComposite.pack();

        textPrice.setText(properties.get(Property.PRICE_IN_SHOP));
        String durabVal = properties.get(Property.DURABILITY);
        textDurab.setText(durabVal + "/" + durabVal);
        help.initialize();
    }

    private void createGroupBuy() {
        GridData gd2 = new GridData(SWT.FILL, SWT.CENTER, true, false);
        GridData gd4 = new GridData(GridData.FILL, GridData.FILL, false, false);

        GridLayout gridLayout2 = new GridLayout(2, false);

        groupBuy = new Group(composite, SWT.SHADOW_NONE);
        groupBuy.setText(CalculatorData.BUY);
        groupBuy.setLayout(gridLayout2);
        groupBuy.setLayoutData(gd4);

        labelPercentBuy = new Label(groupBuy, SWT.RIGHT);
        labelPercentBuy.setText(CalculatorData.PERCENT_BUY);
        labelPercentBuy.setLayoutData(gd2);
        valPercentBuy = new Text(groupBuy, SWT.LEFT);
        valPercentBuy.setTextLimit(7);
        valPercentBuy.setText(DEFAULT_TEXT);

        scalePercentBuy = new Scale(groupBuy, SWT.HORIZONTAL);
        scalePercentBuy.setMaximum(10000);
        scalePercentBuy.setMinimum(5000);
        scalePercentBuy.setSelection(7500);
        scalePercentBuy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 2, 1));

        labelPriceBuy = new Label(groupBuy, SWT.RIGHT);
        labelPriceBuy.setText(CalculatorData.BUYING_PRICE);
        labelPriceBuy.setLayoutData(gd2);
        priceBuy = new Text(groupBuy, SWT.LEFT);
        priceBuy.setTextLimit(7);
        priceBuy.setText(DEFAULT_TEXT);

        labelBuyCost = new Label(groupBuy, SWT.RIGHT);
        labelBuyCost.setText(CalculatorData.COSTS);
        labelBuyCost.setLayoutData(gd2);
        labelValBuyCost = new Text(groupBuy, SWT.LEFT | SWT.READ_ONLY);
        labelValBuyCost.setText(DEFAULT_TEXT);
    }

    private void createGroupSell() {
        GridData gridData13 = new GridData(GridData.END, GridData.CENTER,
                false, false);
        gridData13.horizontalSpan = 2;

        GridData gridData11 = new GridData(GridData.FILL, GridData.FILL, true,
                false);
        gridData11.horizontalSpan = 2;
        gridData11.horizontalIndent = 90;

        GridData gridData18 = new GridData(GridData.FILL, GridData.FILL, false,
                false);

        GridData gridData16 = new GridData(GridData.FILL, GridData.CENTER,
                true, false);

        GridData gridData12 = new GridData(GridData.FILL, GridData.FILL, true,
                false);
        gridData12.horizontalSpan = 2;

        GridData gridData19 = new GridData(GridData.FILL, GridData.FILL, true,
                true);

        GridLayout gridLayout1 = new GridLayout(2, false);

        groupSell = new Group(composite, SWT.NONE);
        groupSell.setText(CalculatorData.SELL);
        groupSell.setLayout(gridLayout1);
        groupSell.setLayoutData(gridData19);

        labelPercentSell = new Label(groupSell, SWT.RIGHT);
        labelPercentSell.setText(CalculatorData.PERCENT_SELL);
        labelPercentSell.setLayoutData(gridData16);
        valPercentSell = new Text(groupSell, SWT.LEFT);
        valPercentSell.setTextLimit(7);
        valPercentSell.setText(DEFAULT_TEXT);

        scalePercentSell = new Scale(groupSell, SWT.HORIZONTAL);
        scalePercentSell.setMaximum(10000);
        scalePercentSell.setMinimum(5000);
        scalePercentSell.setSelection(10000);
        scalePercentSell.setLayoutData(gridData12);

        labelPriceSell = new Label(groupSell, SWT.RIGHT);
        labelPriceSell.setText(CalculatorData.SELLING_PRICE);
        labelPriceSell.setLayoutData(gridData16);
        priceSell = new Text(groupSell, SWT.LEFT);
        priceSell.setTextLimit(7);
        priceSell.setText(DEFAULT_TEXT);

        labelSellIncome = new Label(groupSell, SWT.RIGHT);
        labelSellIncome.setText(CalculatorData.INCOME);
        labelSellIncome.setLayoutData(gridData16);
        labelValSellIncome = new Text(groupSell, SWT.LEFT | SWT.READ_ONLY);
        labelValSellIncome.setLayoutData(gridData18);
        labelValSellIncome.setText("1040.00");
        textBlues = new Text(groupSell, SWT.LEFT);
        textBlues.setText(DEFAULT_TEXT);
        textBlues.setTextLimit(7);
        textBlues.setVisible(false);
        textBlues.setForeground(CalculatorData.BLUE_COLOR);
        textBlues.setLayoutData(gridData13);

        scaleBlues = new Scale(groupSell, SWT.HORIZONTAL);
        scaleBlues.setIncrement(1);
        scaleBlues.setMaximum(20000);
        scaleBlues.setMinimum(0);
        scaleBlues.setVisible(false);
        scaleBlues.setLayoutData(gridData11);

    }

    private void createComposite() {
        GridData gridData24 = new GridData();
        gridData24.horizontalAlignment = GridData.FILL;
        gridData24.verticalAlignment = GridData.FILL;
        gridData24.grabExcessHorizontalSpace = true;
        gridData24.horizontalIndent = 0;
        gridData24.verticalSpan = 9;
        gridData24.grabExcessVerticalSpace = true;
        itemGuiComposite = new Composite(composite, SWT.DOUBLE_BUFFERED);
        itemGuiComposite.setLayout(new FillLayout());
        itemGuiComposite.setLayoutData(gridData24);
    }
}
