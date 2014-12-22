package ru.tykvin.loader;

import javax.swing.UIManager;
import javax.xml.bind.JAXBException;

import ru.tykvin.controller.Controller;
import ru.tykvin.model.Model;
import ru.tykvin.model.data.MainSourcer;
import ru.tykvin.view.View;

public class Loader {

    public static void main(String[] args) throws JAXBException {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        View view = new View();
        Controller controller = Controller.I;
        Model model = new Model();
        PropertyLoader propertyLoader = new PropertyLoader();
        model.setConfig(propertyLoader.unmarshall());
        controller.setView(view);
        controller.setModel(model);
        controller.setSourcer(new MainSourcer());
        view.setModel(model);
        view.setController(controller);
        view.init();

    }

}
