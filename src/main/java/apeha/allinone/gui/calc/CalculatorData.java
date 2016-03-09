package apeha.allinone.gui.calc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

class CalculatorData {

    static final Color BLUE_COLOR = Display.getDefault().getSystemColor(
            SWT.COLOR_DARK_BLUE);
    static final Point SIZE = new Point(850, 470);
    static final String BLUES = "Синие сотки";
    static final String TOWN_HEISTED = "Город захвачен";
    static final String ITEM_TYPE = "Тип премета:";
    static final String ITEM_NAME = "Название:";
    static final String PROFIT = "Прибыль в обычных сотках:";
    static final String PROFIT_IN_BLUES = "Прибыль в синих сотках:";
    static final String COMMON_PROFIT = "Общая прибыль:";
    static final String BUY = "Покупка";
    static final String PERCENT_BUY = "Процент покупки:";
    static final String BUYING_PRICE = "Цена покупки:";
    static final String COSTS = "Затраты:";
    static final String SELL = "Продажа";
    static final String PERCENT_SELL = "Процент продажи:";
    static final String SELLING_PRICE = "Цена продажи:";
    static final String INCOME = "Доход:";

    private CalculatorData() {
    }
}
