package ru.tykvin;

import javax.swing.UIManager;

public class Loader {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        View view = new View();
        Controller controller = new Controller();
        Model model = new Model();
        controller.setView(view);
        controller.setModel(model);
        view.setModel(model);
        view.setController(controller);
        view.init();

    }

}
