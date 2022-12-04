package com.schedulemaster.app.view.content;

import com.schedulemaster.app.view.ComponentGroup;

public abstract class ContentForm extends ComponentGroup {
    public enum Content {
        Home, LectureBag, MagicWizard, MagicSelector, Login
    }
    public abstract void load();
}
