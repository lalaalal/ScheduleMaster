package com.schedulemaster;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Main");

        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(3, 1));
        setLocationRelativeTo(null);

        JPanel flowPanel = new JPanel(new FlowLayout());
        JPanel inputPanel = new JPanel();
        flowPanel.add(inputPanel);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        inputPanel.setLayout(layout);

        JLabel idLabel = new JLabel("id", SwingConstants.CENTER);
        JLabel pwLabel = new JLabel("pw", SwingConstants.CENTER);
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");
        JTextField idField = new JTextField(10);

        JTextField pwField = new JPasswordField(10);
        pwField.setFont(idField.getFont());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        inputPanel.add(idLabel, c);

        c.weightx = 3;
        c.gridx = 1;
        inputPanel.add(idField, c);

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 1;
        inputPanel.add(pwLabel, c);

        c.weightx = 3;
        c.gridx = 1;
        inputPanel.add(pwField, c);

        c.weightx = 3;
        c.gridy = 2;
        inputPanel.add(loginButton, c);

        c.gridy = 3;
        inputPanel.add(signupButton, c);

        getContentPane().add(new JPanel());
        getContentPane().add(flowPanel);
        getContentPane().add(new JPanel());

        setVisible(true);
    }
}
