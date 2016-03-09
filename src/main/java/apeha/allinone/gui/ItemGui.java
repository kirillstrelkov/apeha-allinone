package apeha.allinone.gui;

import apeha.allinone.db.DbHandler;
import apeha.allinone.item.Item;
import apeha.allinone.item.Property;
import apeha.allinone.search.ItemWithScore;
import apeha.allinone.search.SearchType;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import java.io.ByteArrayInputStream;
import java.util.Map;

public class ItemGui<E extends Item> {
    private static final Point SIZE = new Point(280, 310);
    private static final int BORDER = 5;
    private static final int WIDGET_HEIGHT = 20;
    private static final int COMMENT_SHIFT = 20;
    private static final Rectangle IMAGE_SIZE = new Rectangle(5, 5, 75, 75);

    private Composite composite = null;
    private Canvas canvasImage = null;
    private Label labelHeadText = null;
    private Label labelMainText = null;
    private Label labelName = null;
    private Label labelComment = null;
    private SearchType type = null;
    private E item = null;

    private ItemGui() {
    }

    public static void main(String[] args) {
        Display display = Display.getDefault();
        final Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());
        final CCombo combo = new CCombo(shell, SWT.READ_ONLY);
        for (Item item : DbHandler.getItems())
            combo.add(item.getName());

        final Composite composite = new Composite(shell, SWT.BORDER);
        composite.setLayout(new RowLayout(SWT.HORIZONTAL));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        combo.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                for (Control control : composite.getChildren())
                    control.dispose();

                Item item2 = DbHandler.getItemByName(combo.getText());
                ItemWithScore iws = new ItemWithScore(item2);
                iws.computeScore();
                ItemGui.newItemGui(composite, item2);
                ItemGui.newItemGui(composite, iws, SearchType.STRONG);
                composite.pack();
                composite.layout();
            }
        });

        combo.select(0);

        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public static ItemGui<ItemWithScore> newItemGui(Composite parent,
                                                    ItemWithScore item, SearchType type) {
        ItemGui<ItemWithScore> itemGui = new ItemGui<ItemWithScore>();
        itemGui.createUI(parent, item, type);
        return itemGui;
    }

    public static ItemGui<Item> newItemGui(Composite parent, Item item) {
        ItemGui<Item> itemGui = new ItemGui<Item>();
        itemGui.createUI(parent, item, null);
        return itemGui;
    }

    private void createUI(Composite parent, E item, SearchType type) {
        composite = new Composite(parent, SWT.DOUBLE_BUFFERED);
        this.item = item;
        this.type = type;

        composite.setLayout(null);
        composite.setSize(SIZE);
        composite.addListener(SWT.Dispose, new Listener() {

            @Override
            public void handleEvent(Event event) {
                removeAll();
            }
        });

        final int style = SWT.NONE;
        canvasImage = new Canvas(composite, style);
        canvasImage.setLayout(new FillLayout());
        canvasImage.setBounds(IMAGE_SIZE);
        canvasImage.addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(Event arg0) {
                Rectangle rect = canvasImage.getBounds();

                int y = rect.y;
                int width = rect.width;
                int height = rect.height;

                labelName.setBounds(width + 2 * BORDER, y, SIZE.x - width - 3
                        * BORDER, WIDGET_HEIGHT);

                Rectangle bounds = labelName.getBounds();
                int iy = bounds.y + bounds.height + BORDER;

                int labelHeight;
                if (height < BORDER + 2 * WIDGET_HEIGHT) {
                    labelHeight = WIDGET_HEIGHT;
                } else {
                    labelHeight = y + height - iy;
                }

                labelHeadText.setBounds(width + 2 * BORDER, iy, SIZE.x - width
                        - 3 * BORDER, labelHeight);

                bounds = labelHeadText.getBounds();
                iy = bounds.y + bounds.height + BORDER;
                labelMainText.setBounds(BORDER, iy, SIZE.x - 2 * BORDER, SIZE.y
                        - iy - BORDER);
            }
        });

        labelName = new Label(composite, style);
        labelHeadText = new Label(composite, style);
        labelMainText = new Label(composite, style);

        if (item instanceof ItemWithScore) {
            labelComment = new Label(composite, style);
            labelComment.setBounds(new Rectangle(BORDER, BORDER, SIZE.x - 2
                    * BORDER, WIDGET_HEIGHT));
        }

        fillWithData();
    }

    private void fillWithData() {
        drawImage();

        String properties = item.getProperties();
        Map<Property, String> propertiesAndValues = item
                .getPropertiesAndValues();
        String durab = Property.formatProperty(Property.DURABILITY,
                propertiesAndValues.get(Property.DURABILITY));
        properties = properties.replaceAll(durab, "");

        String topComment;
        String tooltip;

        if (item instanceof ItemWithScore) {
            ItemWithScore iws = ItemWithScore.class.cast(item);
            int goodStats = iws.getGoodStats();
            int badStats = iws.getBadStats();

            if (type != null && type.equals(SearchType.OPTIMAL)) {
                topComment = "\tКоличество статов: "
                        + String.valueOf(goodStats - badStats);
                tooltip = String.format(
                        "Разница между нужными и ненужными статами: %d\n"
                                + "Количество нужных статов: %d\n"
                                + "Количество ненужных статов: %d", goodStats
                                - badStats, goodStats, badStats);
            } else {
                topComment = "\tКоличество статов: " + goodStats;
                tooltip = String
                        .format("Количество нужных статов: %d\n"
                                        + "Количество ненужных статов: %d", goodStats,
                                badStats);
            }

            Color foregroundColor = Display.getCurrent().getSystemColor(
                    SWT.COLOR_BLUE);

            labelComment.setText(topComment);
            labelComment.setForeground(foregroundColor);
            labelComment.setToolTipText(tooltip);
        }

        labelName.setText(item.getName());
        labelHeadText.setText(durab);
        labelMainText.setText(properties.trim());
    }

    private void drawImage() {
        ImageLoader loader = new ImageLoader();
        byte[] decbytes = Base64.decodeBase64(item.getImageSrc());
        final ImageData[] imageDatas = loader.load(new ByteArrayInputStream(
                decbytes));
        final Display display = Display.getCurrent();
        Image scaledImage;

        if (item.getName().equals("Кольцо Старого Мастера")) {
            scaledImage = new Image(display, imageDatas[0].scaledTo(30, 34));
        } else {
            scaledImage = new Image(display, imageDatas[0]);
        }

        final Image image = scaledImage;
        Rectangle rect = image.getBounds();
        int y;
        if (item instanceof ItemWithScore) {
            y = COMMENT_SHIFT + 2 * BORDER;
        } else {
            y = BORDER;
        }
        canvasImage.setBounds(BORDER, y, rect.width, rect.height);

        canvasImage.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                e.gc.drawImage(image, 0, 0);
            }
        });
    }

    public void removeAll() {
        item = null;

        for (Control control : composite.getChildren()) {
            control.dispose();
        }

        composite.dispose();
    }

}