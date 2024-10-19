import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Memorium {
    class Card {
        String cardName;
        ImageIcon cardImageIcon;

        Card(String cardName, ImageIcon cardImageIcon) {
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;
        }

        public String toString() {
            return cardName;
        }
    }

    String[] cardLists = {
        "Pokémon Cards",
        "Windows"
    };

    int rows = 4;
    int columns = 5;
    int deckSize = (rows * columns) / 2;//(4*5)/2=10
    int cardWidth = 140;
    int cardHeight = 200;

    ArrayList<Card> cardSet; //Create a deck of cards
    ImageIcon cardBackImageIcon;

    int boardWidth = columns * cardWidth; //5*128=640px
    int boardHeight = rows * cardHeight; //4*90=360px

    JFrame frame = new JFrame("Memorium");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();
    JComboBox cardSetsBox = new JComboBox(cardLists);

    int errorCount = 0;
    ArrayList<JButton> board;
    Timer hideCardTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;
    String deckName = "Pokémon Cards";

    Memorium() {
        setupCards();
        shuffleCards();

        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Errors: " + Integer.toString(errorCount));

        textPanel.setPreferredSize(new Dimension(boardWidth, 30));
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        //Card Game Board
        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!gameReady) {
                        return;
                    }
                    JButton tile = (JButton) e.getSource();
                    if (tile.getIcon() == cardBackImageIcon) {
                        if (card1Selected == null) {
                            card1Selected = tile;
                            int index = board.indexOf(card1Selected);
                            card1Selected.setIcon(cardSet.get(index).cardImageIcon);
                        } else if (card2Selected == null) {
                            card2Selected = tile;
                            int index = board.indexOf(card2Selected);
                            card2Selected.setIcon(cardSet.get(index).cardImageIcon);

                            //Check if two cards are the same
                            if (card1Selected.getIcon() != card2Selected.getIcon()) {
                                errorCount += 1;
                                textLabel.setText("Errors: " + Integer.toString(errorCount));
                                hideCardTimer.start();
                            } else {
                                card1Selected = null;
                                card2Selected = null;
                            }
                        }
                    }
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }
        frame.add(boardPanel);

        //Restart Game Button
        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(boardWidth/2, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameReady) {
                    return;
                }
                gameReady = false;
                restartButton.setEnabled(false);
                card1Selected = null;
                card2Selected = null;
                deckName = String.valueOf(cardSetsBox.getSelectedItem());
                shuffleCards();

                //Reassign Buttons with new Cards
                for (int i = 0; i < board.size(); i++) {
                    board.get(i).setIcon(cardSet.get(i).cardImageIcon);
                }

                errorCount = 0;
                textLabel.setText("Errors: " + Integer.toString(errorCount));
                hideCardTimer.start();
            }
        });
        restartGamePanel.setLayout(new GridLayout(1, 2));
        restartGamePanel.add(restartButton);

        //Card Sets List Box
        cardSetsBox.setFont(new Font("Arial", Font.PLAIN, 16));
        cardSetsBox.setPreferredSize(new Dimension(boardWidth/2, 30));
        cardSetsBox.setFocusable(false);
        cardSetsBox.setEnabled(false);
        restartGamePanel.add(cardSetsBox);
        frame.add(restartGamePanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        //Start Game
        hideCardTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCards();
            }
        });
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();

    }

    void setupCards() {
        cardSet = new ArrayList<Card>();
        for (int i = 0; i < deckSize; i++) {
            //Load Each Card Image
            Image cardImg = new ImageIcon(getClass().getResource("./img/" + deckName + "/" + i + ".jpg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            
            // Create Card Object and the Card Deck
            Card card = new Card(Integer.toString(i), cardImageIcon);
            cardSet.add(card);
        }
        cardSet.addAll(cardSet); //Make each card a couple

        //Load Back Image for each Card
        Image cardBackImg = new ImageIcon(getClass().getResource("./img/" + deckName + "/back.jpg")).getImage();
        cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    void shuffleCards() {
        setupCards();
        //Shuffle
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int)(Math.random() * cardSet.size());
            // Swap 2 Cards
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
    }

    void hideCards() {
        if (gameReady && card1Selected != null && card2Selected != null) {//Flip only the selected two cards face down
            card1Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected.setIcon(cardBackImageIcon);
            card2Selected = null;
        } else { //Flip all cards face down
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restartButton.setEnabled(true);
            cardSetsBox.setEnabled(true);
        }
    }
}
