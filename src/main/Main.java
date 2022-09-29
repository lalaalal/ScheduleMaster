package src.main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;


//메인 화면
public class Main {
    public static void main(String[] args){
        MainPanel mPanel = new MainPanel();
        mPanel.setVisible(true);

    }

    public static class MainPanel extends JFrame {
        private MainBG mainBG;
        public MainPanel(){
            this.setLocation(100, 80);
            this.setSize(500, 606);
            this.getContentPane().setBackground(Color.white);
            this.setLayout(new FlowLayout(FlowLayout.CENTER,10, 20));
            this.mainBG = new MainBG();
        }

    }

    public static class MainBG extends JPanel implements MouseListener{
        private Image mainImg;
        private JLabel label;

        public MainBG(){
            try {
                File sourceFile = new File("img/mju_main.png");
                mainImg = ImageIO.read(sourceFile);
            } catch (IOException e){
                System.out.println("No Image File");
            }
            imgInit();

            this.add(label);
            this.setBackground(Color.white);
            this.addMouseListener(this);
        }

        private void imgInit(){
            this.label = new JLabel(new ImageIcon(mainImg));
        }

        public void urlOpen(String url){
            String os = System.getProperty("os.name");
            Runtime runtime= Runtime.getRuntime();
            try {
                if (os.startsWith("Windows")){
                    String cmd = "rundll32 url.dll,FileProtocolHandler " + url;
                    Process p = runtime.exec(cmd);
                }
            }catch (Exception x){
                System.err.println("Exception Occured while invoking Browser!");
                x.printStackTrace();
            }
        }

        @Override
        public void mouseClicked(MouseEvent arg0){
            urlOpen("https://www.mju.ac.kr/sites/mjukr/intro/intro.html");
        }

        @Override
        public void mousePressed(MouseEvent arg0){
            //Empty
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //Empty
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            this.setToolTipText("홈페이지로 돌아감");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //Empty
        }
    }
}
