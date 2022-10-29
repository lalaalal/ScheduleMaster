package com.schedulemaster.app.view;

import javax.swing.*;

public interface ContentForm {
    enum Content {
        Home, LectureBag, MagicWizard, MagicSelector
    }

    JPanel getPanel();
    void load();
}
