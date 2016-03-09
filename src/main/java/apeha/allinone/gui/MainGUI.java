package apeha.allinone.gui;

import apeha.allinone.gui.calc.CalculatorGUI;
import apeha.allinone.gui.fitting.SetSearchGui;
import apeha.allinone.gui.search.SearchGUI;
import apeha.allinone.gui.spell.SpellGUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.*;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class MainGUI {

    private Shell shell;
    private Button btnSearch;
    private Button btnCalc;
    private Button btnSpell;
    private Button btnSearchSet;
    private Composite composite;

    public MainGUI() {
        createUI();
    }

    public MainGUI(Composite composite) {
        this.composite = composite;
        this.shell = composite.getShell();
        createUI();
    }

    public static void main(String[] args) {
        Display display = Display.getDefault();
        MainGUI main = new MainGUI();
        main.shell.open();
        main.shell.layout();
        while (!main.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    protected void createUI() {
        if (shell == null)
            shell = new Shell();

        InputStream imageStream = this.getClass().getClassLoader().getResourceAsStream("nuk_clan.png");
        shell.setImage(new Image(Display.getCurrent(), imageStream));
        shell.setText("Программы для apeha.ru");
        shell.setLayout(new FillLayout());

        if (composite == null)
            composite = new Composite(shell, SWT.DOUBLE_BUFFERED);

        composite.setLayout(new GridLayout(1, true));

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        btnSpell = new Button(composite, SWT.PUSH);
        btnSpell.setLayoutData(gridData);
        btnSpell.setText(SpellGUI.SHELL_TITLE);
        btnSpell.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                clearMainWindow();
                new SpellGUI(composite);
            }
        });

        btnCalc = new Button(composite, SWT.PUSH);
        btnCalc.setLayoutData(gridData);
        btnCalc.setText(CalculatorGUI.SHELL_TITLE);
        btnCalc.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                clearMainWindow();
                new CalculatorGUI(composite);
            }
        });

        btnSearch = new Button(composite, SWT.PUSH);
        btnSearch.setLayoutData(gridData);
        btnSearch.setText(SearchGUI.SHELL_TITLE);
        btnSearch.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                clearMainWindow();
                new SearchGUI(composite);
            }
        });

        btnSearchSet = new Button(composite, SWT.PUSH);
        btnSearchSet.setLayoutData(gridData);
        btnSearchSet.setText(SetSearchGui.SHELL_TITLE);
        btnSearchSet.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                clearMainWindow();
                new SetSearchGui(composite);
            }
        });
        Link link = new Link(composite, SWT.NONE);
        link.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false));
        link.setText("<a>http://www.nukemaru.ru</a>");
        link.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(e.text));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }

        });

        shell.pack();
        shell.layout();

        shell.setSize(400, 230);
    }

    private void clearMainWindow() {
        for (Control kid : composite.getChildren()) {
            kid.dispose();
        }
    }

}
