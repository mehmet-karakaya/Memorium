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

    String[] cardList = {
        "darkness",
        "double",
        "fairy",
        "fighting",
        "fire",
        "grass",
        "lightning",
        "metal",
        "psychic",
        "water"
    };

    int rows = 4;
    int columns = 5;
    int cardWidth = 90;
    int cardHeight = 128;

    ArrayList<Card> cardSet; //Create a deck of cards
    ImageIcon cardBackImageIcon;

    int boardWidth = columns * cardWidth; //5*128=640px
    int boardHeight = rows * cardHeight; //4*90=360px

    JFrame frame = new JFrame("Memorium");


    Memorium() {
        setupCards();
        shuffleCards();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void setupCards() {
        cardSet = new ArrayList<Card>();
        for (String cardName : cardList) {
            //Load Each Card Image
            Image cardImg = new ImageIcon(getClass().getResource("./img/pokemon_cards/" + cardName + ".jpg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            
            // Create Card Object and the Card Deck
            Card card = new Card(cardName, cardImageIcon);
            cardSet.add(card);
        }
        cardSet.addAll(cardSet); //Make each card a couple

        //Load Back Image for each Card
        Image cardBackImg = new ImageIcon(getClass().getResource("./img/pokemon_cards/back.jpg")).getImage();
        cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    void shuffleCards() {
        //Shuffle
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int)(Math.random() * cardSet.size());
            // Swap 2 Cards
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
        System.out.println(cardSet);
    }
}
