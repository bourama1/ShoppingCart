package uhk.fim.gui;

import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ShoppingCartTableModel extends AbstractTableModel {
    // V modelu potřebujeme referenci na data
    private ShoppingCart shoppingCart;
    private MainFrame main;

    public void setMainFrame(MainFrame main){
        this.main = main;
    }

    @Override
    public int getRowCount() {
        return shoppingCart.getItems().size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    // Tato metoda se volá, když se tabulka dotazuje hodnotu v buňce. Tedy pro kažkou buňku.
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Řádek tabulky (rowIndex) odpovídá pozici položky v seznamu položek
        ShoppingCartItem item = shoppingCart.getItems().get(rowIndex);
        // Podle indexu sloupce vracíme atribut položky
        switch (columnIndex) {
            case 0:
                return item.getName();
            case 1:
                return item.getPricePerPiece();
            case 2:
                return item.getPieces();
            case 3:
                return item.getTotalPrice();
            case 4:
                return item.isPurchased();
            case 5:
                final JButton button = new JButton("Odebrat");
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        int n = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(button),
                                "Opravdu chcete smazat tento výrobek?");
                        if (n == 0)
                            deleteItem(rowIndex);
                        main.update();
                    }
                });
                return button;
            default:
                return null;
        }
    }

    // Tato metoda se volá, když se tabulka dotazuje na názvy sloupců
    @Override
    public String getColumnName(int columnIndex) {
        // Podle indexu sloupce vracíme název
        switch (columnIndex) {
            case 0:
                return "Název";
            case 1:
                return "Cena/kus";
            case 2:
                return "Počet kusů";
            case 3:
                return "Cena celkem";
            case 4:
                return "Zakoupeno";
            case 5:
                return "Odebrat";
            default:
                return null;
        }
    }

    // Tato metoda se volá, když se tabulka dotazuje "typ" sloupce
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Double.class;
            case 2:
                return Integer.class;
            case 3:
                return Double.class;
            case 4:
                return Boolean.class;
            case 5:
                return JButton.class;
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 4;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        ShoppingCartItem row = shoppingCart.getItems().get(rowIndex);
        if(4 == columnIndex) {
            row.setPurchased((Boolean) aValue);
        }
        main.updateCart();
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    private void deleteItem(int row) {
        shoppingCart.getItems().remove(row);
    }
}
