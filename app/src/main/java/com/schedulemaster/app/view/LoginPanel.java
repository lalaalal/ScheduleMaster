package com.schedulemaster.app.view;

import java.awt.*;
import javax.swing.*;
import com.intellij.uiDesigner.core.*;
import net.miginfocom.swing.*;

public class LoginPanel {
    public LoginPanel() {
        initComponents();
        frame1.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        frame1 = new JFrame();
        var panel1 = new JPanel();
        button1 = new JButton();
        textField1 = new JTextField();
        textField2 = new JTextField();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        button2 = new JButton();
        button3 = new JButton();

        //======== frame1 ========
        {
            var frame1ContentPane = frame1.getContentPane();

            //======== panel1 ========
            {
                panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
            }

            //---- button1 ----
            button1.setText("\ub85c\uadf8\uc778");

            //---- label1 ----
            label1.setText("Schedule Master");
            label1.setFont(new Font("\ub9d1\uc740 \uace0\ub515", Font.BOLD, 22));

            //---- label2 ----
            label2.setText("ID");

            //---- label3 ----
            label3.setText("PWD");

            //---- button2 ----
            button2.setText("\uc544\uc774\ub514 \ucc3e\uae30");

            //---- button3 ----
            button3.setText("\ube44\ubc00\ubc88\ud638 \ucc3e\uae30");

            GroupLayout frame1ContentPaneLayout = new GroupLayout(frame1ContentPane);
            frame1ContentPane.setLayout(frame1ContentPaneLayout);
            frame1ContentPaneLayout.setHorizontalGroup(
                frame1ContentPaneLayout.createParallelGroup()
                    .addGroup(GroupLayout.Alignment.TRAILING, frame1ContentPaneLayout.createSequentialGroup()
                        .addContainerGap(461, Short.MAX_VALUE)
                        .addComponent(button2)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button3)
                        .addGap(93, 93, 93))
                    .addGroup(frame1ContentPaneLayout.createSequentialGroup()
                        .addGap(175, 175, 175)
                        .addComponent(panel1, GroupLayout.PREFERRED_SIZE, 0, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(593, Short.MAX_VALUE))
                    .addGroup(frame1ContentPaneLayout.createSequentialGroup()
                        .addContainerGap(276, Short.MAX_VALUE)
                        .addGroup(frame1ContentPaneLayout.createParallelGroup()
                            .addGroup(frame1ContentPaneLayout.createParallelGroup()
                                .addGroup(GroupLayout.Alignment.TRAILING, frame1ContentPaneLayout.createSequentialGroup()
                                    .addGroup(frame1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(label2)
                                        .addComponent(label3))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(frame1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(textField1, GroupLayout.Alignment.TRAILING)
                                        .addComponent(textField2, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)))
                                .addGroup(frame1ContentPaneLayout.createSequentialGroup()
                                    .addGap(61, 61, 61)
                                    .addComponent(button1)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 23, GroupLayout.PREFERRED_SIZE)))
                            .addComponent(label1, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(306, Short.MAX_VALUE))
            );
            frame1ContentPaneLayout.setVerticalGroup(
                frame1ContentPaneLayout.createParallelGroup()
                    .addGroup(frame1ContentPaneLayout.createSequentialGroup()
                        .addGap(186, 186, 186)
                        .addComponent(panel1, GroupLayout.PREFERRED_SIZE, 0, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label1)
                        .addGap(28, 28, 28)
                        .addGroup(frame1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label2))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(frame1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label3))
                        .addGap(18, 18, 18)
                        .addComponent(button1)
                        .addGap(126, 126, 126)
                        .addGroup(frame1ContentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(button3)
                            .addComponent(button2))
                        .addContainerGap(130, Short.MAX_VALUE))
            );
            frame1.pack();
            frame1.setLocationRelativeTo(frame1.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JFrame frame1;
    private JButton button1;
    private JTextField textField1;
    private JTextField textField2;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JButton button2;
    private JButton button3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
