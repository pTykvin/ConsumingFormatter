package ru.tykvin.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ru.tykvin.model.Model;
import ru.tykvin.model.data.MainSourcer;
import ru.tykvin.model.data.Source;
import ru.tykvin.view.View;

public class Controller {
	
    private View view;
    private Model model;
    private MainSourcer sourcer;

    public ActionListener getAddFileAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = view.requestFile();
                if (file != null) {
                    model.fireAddFile(file);
                }
            }
        };
    }

    public ActionListener getRemoveFileAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.fireRemoveFile();
                view.fireRemoveFile();
            }
        };
    }

    public ActionListener getBeginAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	List<Source> data;
                try {
                    data = sourcer.load(model.getListModel());
                    model.fireParseResult(data);
                    view.showReport();
                } catch (Exception e1) {
                    view.fireError(e1);
                }

            }
        };
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setSourcer(MainSourcer sourcer) {
        this.sourcer = sourcer;
    }

    public ListSelectionListener getSelectAction() {
        return new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = view.getSelectListIndex();
                view.fireSelectIndexAction(index);
                model.fireSelectIndexAction(index);
            }
        };
    }

	public Model getModel() {
		if (model == null) {
			model = new Model();
		}
		return model;
	}

}
