package apeha.allinone.gui.calc;

import apeha.allinone.calc.Calculator;
import apeha.allinone.common.Utils;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

import java.math.BigDecimal;

public class CalcHelp {
    private static final String INCORRECT_PRICE_MIN_MAX = "Неправильная цена: '%s' Цена должна быть от '%s' до '%s'";
    private static final String INCORRECT_DATA = "Неверно введены данные: '%s'";
    private static final String UNKNOWN_OBJECT = "Неизвестный объект: '%s'";
    private static final String UNABLE_TO_CALCULATE = "Данные введены неверно: '%s' - проверьте их либо используйте ползунок.";

    CalculatorGUI gui = null;

    public CalcHelp(CalculatorGUI calculatorGUI) {
        this.gui = calculatorGUI;
    }

    void initialize() {
        updatePriceBuyOrPriceSell(gui.valPercentBuy);
        updatePriceBuyOrPriceSell(gui.valPercentSell);
        updateCostOrIncome(gui.labelValBuyCost);
        updateCostOrIncome(gui.labelValSellIncome);
        calculate();
    }

    void eventSliderChanged(Scale scale) {
        boolean isBuy = scale == gui.scalePercentBuy;
        boolean isSell = scale == gui.scalePercentSell;
        boolean isBlues = scale == gui.scaleBlues;
        int selection = scale.getSelection();

        if (isBuy) {
            gui.valPercentBuy.setText(Calculator.intToBigDecimal(selection)
                    .toString() + "%");
            updatePriceBuyOrPriceSell(gui.valPercentBuy);
            updateCostOrIncome(gui.labelValBuyCost);
        } else if (isSell) {
            gui.valPercentSell.setText(Calculator.intToBigDecimal(selection)
                    .toString() + "%");
            updatePriceBuyOrPriceSell(gui.valPercentSell);
            updateCostOrIncome(gui.labelValSellIncome);
        } else if (isBlues)
            gui.textBlues.setText("+"
                    + Calculator.intToBigDecimal(selection).toString());
        else
            showWarning(String.format(UNKNOWN_OBJECT, scale));

        calculate();
    }

    void eventPercentChange(Text textWidget, String defaultTextVal) {
        String text = textWidget.getText();
        String pattern = "\\d+\\.\\d+";
        Scale scale;

        boolean isBuy = textWidget == gui.valPercentBuy;
        boolean isSell = textWidget == gui.valPercentSell;
        boolean isBlues = textWidget == gui.textBlues;

        if (isBuy)
            scale = gui.scalePercentBuy;
        else if (isSell)
            scale = gui.scalePercentSell;
        else if (isBlues)
            scale = gui.scaleBlues;
        else {
            gui.exceptionInfo
                    .setText(String.format(UNKNOWN_OBJECT, textWidget));
            return;
        }

        if (inputValueIsCorrect(text, "\\d{2,3}\\.\\d{2}\\%")) {
            int val = Utils.getInteger(pattern, text) * 100
                    + Utils.getInteger("\\.\\d+", text);

            if (5000 <= val && val <= 10000) {
                scale.setSelection(val);
                updatePriceBuyOrPriceSell(textWidget);
            } else {
                showWarning(String.format(UNABLE_TO_CALCULATE, text));
                return;
            }

            if (isBuy)
                updateCostOrIncome(gui.labelValBuyCost);
            else if (isSell)
                updateCostOrIncome(gui.labelValSellIncome);
            calculate();
        } else
            gui.exceptionInfo.setText(String.format(UNABLE_TO_CALCULATE, text));
    }

    private void showWarning(String text) {
        gui.exceptionInfo.setText(text);
    }

    void eventPriceChanged(Text textWidget) {
        String text = textWidget.getText();
        String regexp = "\\d{1,4}\\.\\d{2}";
        boolean blues = textWidget == gui.textBlues;

        if (blues)
            regexp = "\\+\\d{1,4}\\.\\d{2}";

        if (inputValueIsCorrect(text, regexp)) {
            BigDecimal current = Calculator.stringToBigDecimal(text.replaceAll(
                    "\\+", ""));
            BigDecimal max;
            BigDecimal min;

            if (blues) {
                max = Calculator.stringToBigDecimal("200.00");
                min = Calculator.stringToBigDecimal("0.00");
            } else {
                max = Calculator.stringToBigDecimal(gui.textPrice.getText());
                min = Calculator.stringToBigDecimal(max.divide(
                        new BigDecimal("2")).toString());
            }
            if (current.compareTo(max) <= 0 && current.compareTo(min) >= 0) {
                if (blues) {
                    int selection = Utils.getInteger("\\d+", text) * 100
                            + Utils.getInteger("\\.\\d+", text);
                    gui.scaleBlues.setSelection(selection);
                } else {
                    updateCostOrIncome(gui.labelValBuyCost);
                    updateCostOrIncome(gui.labelValSellIncome);
                }

                calculate();
            } else {
                String msg = String.format(INCORRECT_PRICE_MIN_MAX,
                        current.toString(), min.toString(), max.toString());
                showWarning(msg);
            }
        } else
            showWarning(String.format(INCORRECT_DATA, text));
    }

    void updatePriceBuyOrPriceSell(Text valPercentBuyOrSell) {
        int scaleSelection = -1;
        Text priceWidget = null;

        if (valPercentBuyOrSell == gui.valPercentBuy) {
            scaleSelection = gui.scalePercentBuy.getSelection();
            priceWidget = gui.priceBuy;
        } else if (valPercentBuyOrSell == gui.valPercentSell) {
            scaleSelection = gui.scalePercentSell.getSelection();
            priceWidget = gui.priceSell;
        } else
            showWarning(String.format(UNKNOWN_OBJECT, valPercentBuyOrSell));

        if (scaleSelection != -1 && priceWidget != null) {
            String price = gui.textPrice.getText();
            BigDecimal value = Calculator.intToBigDecimal(scaleSelection)
                    .multiply(Calculator.stringToBigDecimal(price))
                    .divide(new BigDecimal(100));
            priceWidget.setText(Calculator.formatBigDecimal(value));
        }
    }

    void updateCostOrIncome(Text labelValCostOrIncome) {
        if (labelValCostOrIncome == gui.labelValBuyCost) {
            String priceBuy = gui.priceBuy.getText();
            boolean townIsSeized = gui.checkBoxTownIsSeized.getSelection();
            gui.labelValBuyCost.setText(Calculator.formatBigDecimal(Calculator
                    .calculateCosts(priceBuy, townIsSeized)));
        } else if (labelValCostOrIncome == gui.labelValSellIncome) {
            String priceSell = gui.priceSell.getText();
            boolean townIsSeized = gui.checkBoxTownIsSeized.getSelection();
            gui.labelValSellIncome.setText(Calculator
                    .formatBigDecimal(Calculator.calculateIncome(priceSell,
                            townIsSeized)));
        } else
            showWarning(String.format(UNKNOWN_OBJECT, labelValCostOrIncome));
    }

    private boolean inputValueIsCorrect(String text, String regexp) {
        return text.matches(regexp);
    }

    void calculate() {
        String price = gui.textPrice.getText();
        String durab = gui.textDurab.getText();
        String extra = gui.textBlues.getText();
        boolean townIsSeized = gui.checkBoxTownIsSeized.getSelection();
        BigDecimal profit;

        String priceOfBuy = gui.priceBuy.getText();
        String priceOfSell = gui.priceSell.getText();
        try {
            profit = Calculator.calculateProfit(price, durab, priceOfSell,
                    priceOfBuy, extra, townIsSeized);

            if (gui.checkBoxBlues.getSelection()) {
                BigDecimal profitInBlues = Calculator.calcProfitInBlues(extra,
                        townIsSeized);
                gui.textProfit.setText(profit.subtract(profitInBlues)
                        .toString());
                gui.textProfitInBlues.setText(profitInBlues.toString());
                gui.textAllProfit.setText(profit.toString());
            } else {
                gui.textProfit.setText(profit.toString());
            }

            gui.exceptionInfo.setText("");
        } catch (Exception e) {
        }
    }

}
