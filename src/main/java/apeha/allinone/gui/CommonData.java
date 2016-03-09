package apeha.allinone.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

public class CommonData {
    public static final String ADDING_HINT = "Вставьте вещи скопированные из "
            + "Дома бойцов или Кузницы.";

    public static final String FONT_NAME = Display.getDefault().getSystemFont()
            .getFontData()[0].getName();
    public static final int DEFAULT_FONT_SIZE = Display.getDefault()
            .getSystemFont().getFontData()[0].getHeight();
    public static final Font DEFAULT_FONT = new Font(Display.getDefault(),
            FONT_NAME, (int) (DEFAULT_FONT_SIZE * 0.9), SWT.NORMAL);
    public static final Font BOLD_FONT = new Font(Display.getDefault(),
            FONT_NAME, (int) (DEFAULT_FONT_SIZE * 0.9), SWT.BOLD);

    public static final String FOUND_ITEMS_TEXT = "Найдено предметов: ";
    public static final String SEARCH_BTN_TEXT = "Найти вещи";
    public static final String SEARCH_TYPE_TEXT = "Тип поиска:";
    public static final String ITEM_LIMIT = "Количество вещей:";
    public static final String ITEMS_TEXT = "Предметы:";
    public static final String REQUIRED_LEVEL = "Требуемый уровень:";
    public static final String PRICE_IN_SHOP = "Цена в магазине:";

    public static final String SEARCH_TYPE_TOOLTIP = "\"Строгий\" - поиск предметов ТОЛЬКО с выбранными параметрами.\n"
            + "\"Оптимальный\" - \"Мягкий\" поиск предметов и сортировка по разнице между нужными и ненужными параметрами.\n"
            + " \"Мягкий\" - поиск предметов с ЛЮБЫМ из выбранных параметров.";

    public static final int[] SEARCH_LIMITS = {5, 10, 20, 50, 100};

    private CommonData() {
    }
}
