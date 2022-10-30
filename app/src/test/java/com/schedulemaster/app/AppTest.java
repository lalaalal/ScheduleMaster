package com.schedulemaster.app;

import com.schedulemaster.app.view.MainFrame;
import com.schedulemaster.app.view.TimeTableForm;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class AppTest {
    @Test
    public void testApp() {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setContentPane(new TimeTableForm().getPanel());
    }
}
