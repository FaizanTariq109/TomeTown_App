package admin;

import admin.view.AdminPanel;

public class MainAdmin {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new AdminPanel();
        });
    }
}
