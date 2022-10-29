package com.schedulemaster.app.view;

public interface ContentForm extends ComponentForm {
    enum Content {
        Home, LectureBag, MagicWizard, MagicSelector
    }
    void load();
}
