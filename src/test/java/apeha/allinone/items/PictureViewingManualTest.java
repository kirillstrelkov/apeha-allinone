package apeha.allinone.items;

import apeha.allinone.db.DbHandler;
import apeha.allinone.gui.ItemGui;
import apeha.allinone.item.Item;
import apeha.allinone.search.ItemWithScore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class PictureViewingManualTest {

    Shell sShell = null;
    Composite label = null;
    Combo combo = null;
    Button button = null;

    ItemGui<Item> itemGui = null;

    Thread animatedThread = null;

    public static void main(String[] args) {
        Display display = Display.getDefault();
        PictureViewingManualTest thisClass = new PictureViewingManualTest();
        thisClass.createSShell();
        thisClass.sShell.open();

        while (!thisClass.sShell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

    private void createCombo() {
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.horizontalSpan = 2;
        gridData2.verticalAlignment = GridData.FILL;
        combo = new Combo(sShell, SWT.NONE);
        combo.setLayoutData(gridData2);
        combo.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event arg0) {
                if (itemGui != null) {
                    itemGui.removeAll();
                }

                String name = combo.getText();
                drawAnImage(name);
            }
        });
    }

    private void createSShell() {
        GridData gridGrabAll = new GridData();
        gridGrabAll.horizontalAlignment = GridData.FILL;
        gridGrabAll.grabExcessHorizontalSpace = true;
        gridGrabAll.grabExcessVerticalSpace = true;
        gridGrabAll.verticalAlignment = GridData.FILL;

        GridData gridFillHoriz = new GridData();
        gridFillHoriz.horizontalAlignment = GridData.FILL;
        gridFillHoriz.verticalAlignment = GridData.FILL;

        GridLayout gridLayout1 = new GridLayout();

        sShell = new Shell();
        sShell.setText("Image Viewer");
        sShell.setLayout(gridLayout1);
        sShell.setSize(new Point(650, 550));
        label = new Composite(sShell, SWT.DOUBLE_BUFFERED);
        label.setLayoutData(gridGrabAll);
        createCombo();

        button = new Button(sShell, SWT.NONE);
        button.setLayoutData(gridFillHoriz);
        button.setText("Load all items");
        button.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event arg0) {
                String[] specialItems = {"Белый крест", "Кастет Удивления",
                        "Меч ведьмы", "Меч волшебника", "Пламя ярости",
                        "Стальные когти", "Щит солнца", "Сюрикены",
                        "Сюрикены Ученика", "Сякены", "Сякены Бронзовые",};

                for (String name : specialItems) {
                    combo.add(name);
                }
                // All items in db
                // List<Item> items = DbHandler.getItems();
                // for (Item item : items) {
                // combo.add(item.getName());
                // }
            }
        });
    }

    private void drawAnImage(final String selection) {
        System.out.println(selection);
        ItemWithScore pass = new ItemWithScore(
                DbHandler.getItemByName(selection));
        itemGui = ItemGui.newItemGui(label, pass);
    }
}
