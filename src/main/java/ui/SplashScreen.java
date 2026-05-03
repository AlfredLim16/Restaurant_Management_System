package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author admin
 */
public class SplashScreen extends JDialog implements ActionListener, Runnable {

    private final JPanel content;
    private final JProgressBar progressBar;
    private final JLabel title, subtitle, version, statusLabel, percentLabel;
    private int progress = 0;
    private Timer loadingTimer;

    private final String[] loadingSteps = {"Initializing components...", 
        "Loading user interface classess...", "Connecting to services...", 
        "Preparing the system...", "Launching application..."
    };

    public SplashScreen() {
        setUndecorated(true);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);

        content = new JPanel(null);
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));

        title = new JLabel("Restaurant Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Historic", Font.BOLD, 26));
        title.setBounds(0, 110, 400, 36);
        content.add(title);

        subtitle = new JLabel("System", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI Historic", Font.BOLD, 26));
        subtitle.setBounds(0, 148, 400, 36);
        content.add(subtitle);

        version = new JLabel("Version 1.0.0", SwingConstants.CENTER);
        version.setFont(new Font("Segoe UI Historic", Font.PLAIN, 12));
        version.setForeground(new Color(150, 150, 150));
        version.setBounds(0, 200, 400, 20);
        content.add(version);

        statusLabel = new JLabel("Starting...", SwingConstants.LEFT);
        statusLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        statusLabel.setBounds(24, 340, 280, 18);
        content.add(statusLabel);

        percentLabel = new JLabel("0 %", SwingConstants.RIGHT);
        percentLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 12));
        percentLabel.setForeground(new Color(100, 100, 100));
        percentLabel.setBounds(310, 340, 66, 18);
        content.add(percentLabel);

        progressBar = new JProgressBar(0, 100);
        progressBar.setBounds(24, 370, 352, 4);
        progressBar.setForeground(new Color(0, 143, 74));
        progressBar.setBackground(new Color(230, 230, 230));
        progressBar.setBorderPainted(false);
        progressBar.setValue(0);
        content.add(progressBar);

        add(content);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        progress += (int) (Math.random() * 1) + 1;
        if(progress > 100){
            progress = 100;
        }

        progressBar.setValue(progress);
        percentLabel.setText(progress + " %");

        int step = Math.min(loadingSteps.length - 1, (int) ((progress / 100.0) * loadingSteps.length));
        statusLabel.setText(loadingSteps[step]);

        if(progress >= 100){
            if(loadingTimer != null && loadingTimer.isRunning()){
                loadingTimer.stop();
            }
            dispose();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                }
            });
        }
    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                startLoading();
            }
        });
    }

    public void startLoading() {
        setVisible(true);
        loadingTimer = new Timer(70, this);
        loadingTimer.start();
    }
}
