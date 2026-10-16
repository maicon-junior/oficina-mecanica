package br.edu.univille.poo;

import br.edu.univille.poo.model.database.DatabaseCreator;
import br.edu.univille.poo.model.database.DatabaseInitialize;
import br.edu.univille.poo.view.principal.PrincipalView;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        System.setProperty("flatlaf.useWindowDecorations", "false");
        System.setProperty("flatlaf.useNativeLibrary", "false");

        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            UIManager.put("Component.arc", 10);
            UIManager.put("Button.arc", 10);
            UIManager.put("TextComponent.arc", 10);

            try {
                DatabaseCreator.create();
                DatabaseInitialize.init();
            } catch (RuntimeException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "Não foi possível conectar ao MySQL.\n\n"
                                + "Verifique se o MySQL está ligado e se o usuário e a senha\n"
                                + "estão corretos na classe ConnectionFactory.\n\n"
                                + "Detalhe: " + e.getMessage(),
                        "Erro no banco de dados",
                        JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            }

            PrincipalView frame = new PrincipalView();
            frame.setVisible(true);
        });
    }
}
