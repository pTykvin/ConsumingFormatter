package ru.tykvin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.Executors;

public class Controller {

    private View view;
    private Model model;

    public ActionListener getAddFileAction() {
        return makeAsynchCall(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File file = view.requestFile();
                if (file != null) {
                    model.addFile(file);
                }
            }
        });
    }

    private final ActionListener makeAsynchCall(final ActionListener action) {
        return new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                Executors.newCachedThreadPool().execute(new Runnable() {

                    @Override
                    public void run() {
                        action.actionPerformed(e);
                        view.updateView();
                    }
                });
            }
        };
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setModel(Model model) {
        this.model = model;
    }

}
