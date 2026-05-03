package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
 * @author admin
 */
public class AboutPanel extends JPanel implements ActionListener, ComponentListener {

    private final int cardsTop = 150;
    private final int cardSpacing = 20;
    private final int minCardWidth = 140;
    private final int nameLabelHeight = 26;

    private static final int MARGIN_LEFT = 35;
    private static final int MARGIN_RIGHT = 35;
    private static final int MARGIN_BOTTOM = 35;

    private Timer layoutDebounce;
    private JSeparator separator;
    private JLabel header, subHeader, mainLabel, teamName;

    private String[] members = {
        "Tom Justine De Jesus",
        "Allysa Rose Tolarba",
        "Nolan Claveria",
        "Aina Bulawin",
        "Alfred Lim"
    };

    private String[] images = {
        "/images/tom-justine-de-jesus.jpg",
        "/images/allysa-rose-tolarba.jpg",
        "/images/nolan-claveria.jpg",
        "/images/aina-bulawin.jpg",
        "/images/alfred-lim.jpg"
    };

    private TeamCard[] teamCards;
    private JLabel[] nameLabels;

    private TeamCard teamCard;

    public AboutPanel(){
        setLayout(null);
        setBackground(Color.WHITE);

        header = UIComponents.header("About");
        header.setBounds(MARGIN_LEFT, 25, 500, 32);
        add(header);

        subHeader = UIComponents.subHeader("Introduction of Restaurant Management System");
        subHeader.setBounds(MARGIN_LEFT, 55, 500, 32);
        add(subHeader);

        separator = UIComponents.separator();
        separator.setBounds(MARGIN_LEFT, 92, 200, 1);
        add(separator);

        mainLabel = new JLabel("Get to Know Our Team", SwingConstants.LEFT);
        mainLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 16));
        mainLabel.setForeground(new Color(85, 85, 85));
        mainLabel.setBounds(MARGIN_LEFT, 110, 500, 32);
        add(mainLabel);

        teamCards = new TeamCard[members.length];
        nameLabels = new JLabel[members.length];
        for(int i = 0;i < members.length;i++){
            teamCard = new TeamCard(images[i]);
            teamCards[i] = teamCard;
            add(teamCard);

            teamName = new JLabel(members[i], SwingConstants.CENTER);
            teamName.setFont(new Font("Segoe UI Historic", Font.PLAIN, 16));
            teamName.setForeground(Color.BLACK);
            add(teamName);
            nameLabels[i] = teamName;
        }
        layoutDebounce = new Timer(120, this);
        layoutDebounce.setRepeats(false);

        addComponentListener(this);
        updateLayout(false);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == layoutDebounce){
            updateLayout(true);
        }
    }

    @Override
    public void componentResized(ComponentEvent e){
        updateLayout(false);
        layoutDebounce.restart();
    }

    @Override
    public void componentMoved(ComponentEvent e){

    }

    @Override
    public void componentShown(ComponentEvent e){
        updateLayout(true);
    }

    @Override
    public void componentHidden(ComponentEvent e){

    }

    private void updateLayout(boolean rescaleImages){
        int width = getWidth();
        int height = getHeight();
        if(width == 0 || height == 0){
            return;
        }

        int separatorWidth = Math.max(50, width - MARGIN_LEFT - MARGIN_RIGHT);
        separator.setBounds(MARGIN_LEFT, 92, separatorWidth, 1);

        int availableWidth = Math.max(0, width - MARGIN_LEFT - MARGIN_RIGHT);
        int columns = Math.min(5, Math.max(1, (availableWidth + cardSpacing) / (minCardWidth + cardSpacing)));
        int cardWidth = (availableWidth - (columns - 1) * cardSpacing) / columns;
        if(cardWidth < 80){
            cardWidth = 80;
        }
        int cardHeight = cardWidth;

        for(int i = 0;i < teamCards.length;i++){
            int col = i % columns;
            int row = i / columns;
            int x = MARGIN_LEFT + col * (cardWidth + cardSpacing);
            int y = cardsTop + row * (cardHeight + cardSpacing + nameLabelHeight + 6);

            teamCards[i].setCardBounds(x, y, cardWidth, cardHeight, rescaleImages);
            nameLabels[i].setBounds(x, y + cardHeight + 6, cardWidth, nameLabelHeight);
        }
    }

    private static class TeamCard extends JPanel {

        private JLabel imageLabel;
        private String imagePath;
        private Image sourceImage;
        private int cachedWidth = -1;
        private int cachedHeight = -1;
        private URL path;
        private Image imageScaled;

        TeamCard(String imagePath){
            this.imagePath = imagePath;
            setLayout(null);
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(imageLabel);

            try{
                path = AboutPanel.class.getResource(imagePath);
                if(path != null){
                    sourceImage = new ImageIcon(path).getImage();
                }
            }catch(Exception ex){
                sourceImage = null;
            }
        }

        void setCardBounds(int x, int y, int width, int height, boolean rescaleImages){
            setBounds(x, y, width, height);
            imageLabel.setBounds(0, 0, width, height);

            if(!rescaleImages){
                return;
            }
            if(width <= 0 || height <= 0){
                return;
            }
            if(width == cachedWidth && height == cachedHeight){
                return;
            }

            cachedWidth = width;
            cachedHeight = width;

            if(sourceImage != null){
                imageScaled = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(imageScaled));
            }else{
                imageLabel.setIcon(null);
            }
        }
    }

}
