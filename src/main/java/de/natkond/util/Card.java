package de.natkond.util;

import java.util.*;

public record Card(Suit suit, String face, int rank) {

    public enum Suit {
        CLUB, DIAMOND, HEART, SPADE;

        public char getImage() {
            return (new char[]{9827, 9830, 9829, 9824})[this.ordinal()];
        }
    }

    @Override
    public String toString() {
        int index = face.equals("10") ? 2 : 1;
        String faceString = face.substring(0, index);
        String rankString = faceString.equals("A") ? "1 or 11" : String.valueOf(rank);
        return "%s%c(%s)".formatted(faceString, suit.getImage(), rankString);
    }

    public static Card getNumericCard(Suit suit, int cardNumber) {
        if (cardNumber > 1 && cardNumber < 11) {
            return new Card(suit, String.valueOf(cardNumber), cardNumber);
        }
        throw new IllegalArgumentException("Invalid card number: " + cardNumber);
    }

    public static Card getFaceCard(Suit suit, char face, int rank) {
        int charIndex = "JQKA".indexOf(face);
        if (charIndex > -1) {
            if (charIndex == 3){
                return new Card(suit, String.valueOf(face), 1);
            }
            return new Card(suit, String.valueOf(face), rank);
        }
        throw new IllegalArgumentException("Invalid face card selection: " + face);
    }

    public static List<Card> getStandardDeck36() {
        List<Card> standardDeck = new LinkedList<>();
        String face = "JQKA";
        for (Suit suit : Suit.values()) {
            for (int i = 6; i <= 10; i++) {
                standardDeck.add(getNumericCard(suit, i));
            }
            for (int i = 0; i < face.length(); i++) {
                standardDeck.add(getFaceCard(suit, face.charAt(i), i + 2));
            }
        }
        Collections.shuffle(standardDeck);
        return standardDeck;
    }

    public static List<Card> getStandardDeck52() {
        List<Card> standardDeck = new ArrayList<>(52);
        String face = "JQKA";
        for (Suit suit : Suit.values()) {
            for (int i = 2; i <= 10; i++) {
                standardDeck.add(getNumericCard(suit, i));
            }
            for (int i = 0; i < face.length(); i++) {
                standardDeck.add(getFaceCard(suit, face.charAt(i), 10));
            }
        }
        Collections.shuffle(standardDeck);
        return standardDeck;
    }

    public static String printDeck(List<Card> deck) {
        return printDeck(deck, 4);
    }

    public static String printDeck(List<Card> deck, int rows) {
        StringBuilder output = new StringBuilder();
        int cartInRow = deck.size() / rows;
        for (int i = 0; i < rows; i++) {
            int startIndex = i * cartInRow;
            int endIndex = startIndex + cartInRow;
            deck.subList(startIndex, endIndex).forEach(c -> output.append("%7s ".formatted(c)));
            output.append("\n");
        }
        return output.toString();
    }
}
