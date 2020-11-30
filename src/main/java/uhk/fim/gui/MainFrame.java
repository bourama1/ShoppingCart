package uhk.fim.gui;

import org.apache.commons.io.FilenameUtils;
import uhk.fim.files.FileWorks;
import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame implements ActionListener {
    // Tlačítka deklarujeme zde, abychom k nim měli přístup v metodě actionPerformed
    JButton btnInputAdd;
    JTextField txtInputName, txtInputPricePerPiece;
    JSpinner spInputPieces;

    // Labels
    JLabel lblTotalPrice;
    JLabel lblPurchasedPrice;

    ShoppingCart shoppingCart;
    ShoppingCartTableModel model;
    FileWorks file;

    public MainFrame(int width, int height) {
        super("PRO2 - Shopping cart - Matej Boura");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("logo.jpg").getImage());


        file = new FileWorks();
        // Vytvoříme košík (data)
        shoppingCart = new ShoppingCart();
        shoppingCart = file.loadFileCsv("storage.csv");
        update();
    }

    public void initGUI() {
        // Vytvoříme hlavní panel, do kterého budeme přidávat další (pod)panely.
        // Naším cílem při tvorbě GUI, je snaha jednotlivé komponenty zanořovat.
        JPanel panelMain = new JPanel(new BorderLayout());

        // MenuBar
        createMenuBar();

        // Vytvoříme další 3 panely. Panel pro prvky formuláře pro přidání položky.
        // Panel pro tabulku a panel pro patičku.
        JPanel panelInputs = new JPanel(new FlowLayout(FlowLayout.LEFT)); // FlowLayout LEFT - komponenty chceme zarovnat zleva doprava.
        JPanel panelTable = new JPanel(new BorderLayout());
        JPanel panelFooter = new JPanel(new BorderLayout());

        panelFooter.setBackground(Color.CYAN);

        // *** Formulář pro přidání položky ***
        // Název
        JLabel lblInputName = new JLabel("Název: ");
        txtInputName = new JTextField("", 15);
        // Cena za 1 kus
        JLabel lblInputPricePerPiece = new JLabel("Cena/kus: ");
        txtInputPricePerPiece = new JTextField("", 5);
        // Počet kusů
        JLabel lblInputPieces = new JLabel("Počet kusů: ");
        spInputPieces = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        // Tlačítka
        btnInputAdd = new JButton("Přidat");
        btnInputAdd.addActionListener(this); // Nastavení ActionListeneru - kdo obslouží kliknutí na tlačítko.

        // Přidání komponent do horního panelu pro formulář na přidání položky
        panelInputs.add(lblInputName);
        panelInputs.add(txtInputName);
        panelInputs.add(lblInputPricePerPiece);
        panelInputs.add(txtInputPricePerPiece);
        panelInputs.add(lblInputPieces);
        panelInputs.add(spInputPieces);
        panelInputs.add(btnInputAdd);

        // *** Tabulka ***
        JTable table = new JTable();
        table.setFillsViewportHeight(true);
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();

        // Tabulku propojíme s naším modelem
        model.setMainFrame(this);
        table.setModel(model);
        table.getColumn("Odebrat").setCellRenderer(buttonRenderer);
        table.addMouseListener(new JTableButtonMouseListener(table));
        // Tabulku přidáme do panelu a obalíme ji komponentou JScrollPane
        panelTable.add(new JScrollPane(table), BorderLayout.CENTER);

        // *** Patička ***
        lblTotalPrice = new JLabel("");
        panelFooter.add(lblTotalPrice, BorderLayout.WEST);
        lblPurchasedPrice = new JLabel("");
        panelFooter.add(lblPurchasedPrice, BorderLayout.EAST);

        // Přidání (pod)panelů do panelu hlavního
        panelMain.add(panelInputs, BorderLayout.NORTH);
        panelMain.add(panelTable, BorderLayout.CENTER);
        panelMain.add(panelFooter, BorderLayout.SOUTH);

        // Přidání hlavního panelu do MainFrame (JFrame)
        add(panelMain);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Soubor");
        fileMenu.add(new AbstractAction("Nový nákupní seznam") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                newFile();
            }
        });
        fileMenu.add(new AbstractAction("Otevřít") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loadFile();
            }
        });
        fileMenu.add(new AbstractAction("Uložit") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                file.saveFileCsv("storage.csv", shoppingCart);
            }
        });
        fileMenu.add(new AbstractAction("Uložit jako") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveFile();
            }
        });
        menuBar.add(fileMenu);

        JMenu aboutMenu = new JMenu("O programu");
        menuBar.add(aboutMenu);
        aboutMenu.add(new AbstractAction("Autor") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                author();
            }
        });

        setJMenuBar(menuBar);
    }

    // Při kliknutí na jakékoliv tlačítko se zavolá tato metoda.
    // Toho jsme docílili implementování rozhraní ActionListener a nastavením tlačítek např. btnInputAdd.addActionListener(this);
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Metoda se volá pro každé tlačítko, musíme tedy rozhodnout, co se má skutečně stát pro konkrétní tlačítka
        if (actionEvent.getSource() == btnInputAdd) {
            addProductToCart();
        }
    }

    private void addProductToCart() {
        if (!txtInputName.getText().isBlank()) {
            try {
                // Vytvořit novou položku
                ShoppingCartItem item = new ShoppingCartItem(txtInputName.getText().trim(), Double.parseDouble(txtInputPricePerPiece.getText().replace(",", ".")),
                        (int) spInputPieces.getValue(), false);
                // Přidat položku do košíku
                shoppingCart.addItem(item);
                // Refreshnout tabulku
                model.fireTableDataChanged();
                // Updatovat patičku
                updateFooter();

                JOptionPane.showMessageDialog(this, "Super! Přidáno.", "Úspěch", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Zadejte správný formát ceny a počtu kusů!", "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vyplňte název produktu!", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void update() {
        model = new ShoppingCartTableModel();
        model.setShoppingCart(shoppingCart);
        initGUI();
        updateFooter();
    }

    public void updateFooter() {
        lblTotalPrice.setText("Celková cena: " + String.format("%.2f", shoppingCart.getTotalPrice()) + " Kč");
        lblPurchasedPrice.setText("Cena zakoupených: " + String.format("%.2f", shoppingCart.getPurchasedPrice()) + " Kč  " +
                                "Cena nezakoupených: " + String.format("%.2f", shoppingCart.getUnpurchasedPrice()) + " Kč");
    }

    private void saveFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String fileName = fc.getSelectedFile().getAbsolutePath();
            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("csv")){
                file.saveFileCsv(fileName, shoppingCart);
            } else if (extension.equals("xml")){
                file.saveFileXML(fileName, shoppingCart);
            } else {
                JOptionPane.showMessageDialog(this, "Soubor tohoto typu nelze ulozit.");
            }
        }
    }

    private void loadFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String fileName = fc.getSelectedFile().getAbsolutePath();
            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("csv")){
                shoppingCart = file.loadFileCsv(fileName);
            } else if (extension.equals("xml")){
                shoppingCart = file.loadFileXML(fileName);
            } else {
                JOptionPane.showMessageDialog(this, "Soubor tohoto typu nelze otevrit.");
            }
            update();
        }
    }

    private void newFile() {
        int n = JOptionPane.showConfirmDialog(this, "Potvrzenim se smaze aktualni seznam, chcete pokracovat?");
        if (n != 0)
            return;
        shoppingCart = new ShoppingCart();
        update();
    }

    private void author() {
        JOptionPane.showMessageDialog(this, "\u00a9 Matěj Boura 2020");
    }

    private static class JTableButtonRenderer implements TableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return (JButton)value;
        }
    }

    private static class JTableButtonMouseListener extends MouseAdapter {
        private final JTable table;

        public JTableButtonMouseListener(JTable table) {
            this.table = table;
        }

        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX()); // get the coloum of the button
            int row    = e.getY()/table.getRowHeight(); //get the row of the button


            if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                Object value = table.getValueAt(row, column);
                if (value instanceof JButton) {

                    ((JButton)value).doClick();
                }
            }
        }
    }
}
