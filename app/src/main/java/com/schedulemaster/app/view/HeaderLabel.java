package com.schedulemaster.app.view;

import javax.swing.*;
import java.awt.*;

public class HeaderLabel extends JLabel {
    public static final Font headerFont = UIManager.getDefaults().getFont("Label.font").deriveFont(18f);

    public HeaderLabel() {
        setFont(headerFont);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setFont(UIManager.getDefaults().getFont("Label.font").deriveFont(18f));
    }
}
