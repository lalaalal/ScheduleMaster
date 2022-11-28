package com.schedulemaster.app.view;

import com.schedulemaster.misc.LinkedList;

public abstract class ComponentGroup extends ComponentForm {
    protected final LinkedList<ComponentForm> componentForms = new LinkedList<>();

    public void addComponentForm(ComponentForm componentForm) {
        componentForms.push(componentForm);
    }

    public void removeComponentForm(ComponentForm componentForm) {
        componentForms.remove(componentForm);
    }

    @Override
    public void onLocaleChange() {
        super.onLocaleChange();
        for (ComponentForm componentForm : componentForms)
            componentForm.onLocaleChange();
    }

    @Override
    public void onThemeChange() {
        super.onThemeChange();
        for (ComponentForm componentForm : componentForms)
            componentForm.onThemeChange();
    }
}
