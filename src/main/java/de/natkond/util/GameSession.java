package de.natkond.util;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static de.natkond.util.BotUtils.*;
import static de.natkond.util.GameConstants.*;

public class GameSession {

    private int playerScore;
    private int dealerScore;
    private Card playerCurrentCard;
    private Card dealerCurrentCard;
    private boolean hasPlayerAce;
    private boolean hasDealerAce;
    private List<Card> dealerLastCards;
    private List<Card> playerHand;
    private List<Card> dealerHand;
    private List<Card> deck;
    @Getter
    private boolean gameOver = true;
    boolean hasPlayerStopped;
    boolean hasDealerStopped;

    private void initGame() {
        deck = Card.getStandardDeck36();
        playerScore = 0;
        dealerScore = 0;
        playerCurrentCard = null;
        dealerCurrentCard = null;
        hasPlayerAce = false;
        hasDealerAce = false;
        dealerLastCards = new ArrayList<>();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        gameOver = false;
        hasPlayerStopped = false;
        hasDealerStopped = false;
    }

/*    public String processUserInput(String input) {
        return switch (input) {
            case START -> handleStart();
            case YES_ANSWER -> gameOver ? START_MESSAGE + READ_RULES_MESSAGE : handleYes();
            case NO_ANSWER -> gameOver ? START_MESSAGE + READ_RULES_MESSAGE : handleNo();
            case RULES_ANSWER -> GAME_RULES + "\n" + (gameOver ? "" : TAKE_CARD_PLAYER_MESSAGE);
            default -> gameOver ? START_MESSAGE + READ_RULES_MESSAGE : TAKE_CARD_PLAYER_MESSAGE;
        };
    }
 */

    public String handlePlay() {
        initGame();
        return TAKE_CARD_PLAYER_MESSAGE;
    }

    public String handleStart(){
        return WELCOME_MESSAGE + READ_RULES_MESSAGE + START_MESSAGE;
    }

    public String handleRules(){
        if (gameOver) {
            return GAME_RULES + "\n" + START_MESSAGE;
        }
        return GAME_RULES + "\n" + TAKE_CARD_PLAYER_MESSAGE;
    }

    public String handleDefault(){
        if (gameOver) {
            return GAME_RULES + "\n" + START_MESSAGE;
        }
        return TAKE_CARD_PLAYER_MESSAGE;
    }

    public String handleYes() {
        playerCurrentCard = deck.getFirst();
        takeCardFromDeck(deck, playerHand);
        playerScore += playerCurrentCard.rank();
        if (playerCurrentCard.face().equals("A")){
            hasPlayerAce = true;
            if (playerScore + 10 == WIN_SCORE){
                playerScore += 10;
                return buildGameStateMessage();
            }
        }
        dealerLastCards = new ArrayList<>();
        if (playerScore > WIN_SCORE) {
            return buildGameStateMessage();
        } else if (playerScore == WIN_SCORE) {
            hasPlayerStopped = true;
            while (!hasDealerStopped) {
                dealersTurn();
            }
        } else {
            if (!hasDealerStopped) {
                dealersTurn();
            }
        }
        return buildGameStateMessage();
    }

    public String handleNo() {
        hasPlayerStopped = true;
        if (hasPlayerAce && playerScore + 10 < WIN_SCORE) {
            playerScore += 10;
        }
        dealerLastCards = new ArrayList<>();
        while (!hasDealerStopped) {
            dealersTurn();
        }
        return buildGameStateMessage();
    }

    private void dealersTurn() {
        if ((dealerScore < MAX_SAVE_SCORE_FOR_DEALER && !hasDealerAce) || (hasDealerAce && dealerScore + 10 < MAX_SAVE_SCORE_FOR_DEALER)) {
            dealerCurrentCard = deck.getFirst();
            takeCardFromDeck(deck, dealerHand);
            dealerScore += dealerCurrentCard.rank();
            dealerLastCards.add(dealerCurrentCard);
            if (dealerCurrentCard.face().equals("A")){
                hasDealerAce = true;
                if (dealerScore + 10 == WIN_SCORE){
                    dealerScore += 10;
                    hasDealerStopped = true;
                    return;
                }
            }
            if (dealerScore >= WIN_SCORE) {
                hasDealerStopped = true;
            }
        } else {
            if (hasDealerAce) {
                dealerScore += 10;
            }
            hasDealerStopped = true;
        }
    }

    private String buildGameStateMessage(){
        StringBuilder answer = new StringBuilder();
        if (isGameOverCheck()) {
            answer.append(PLAYERS_HAND)
                    .append(Card.printDeck(playerHand, 1))
                    .append(DEALERS_HAND)
                    .append(Card.printDeck(dealerHand, 1))
                    .append(PLAYERS_SCORE)
                    .append(hasPlayerAce && playerScore + 10 < WIN_SCORE ? playerScore + 10 : playerScore)
                    .append(DEALERS_SCORE)
                    .append(hasDealerAce && dealerScore + 10 < WIN_SCORE ? dealerScore + 10 : dealerScore)
                    .append(".\n")
                    .append(determineWinner())
                    .append(GAME_OVER);
        } else if (hasPlayerStopped) {
            answer.append("\n").append(END_PLAYERS_TURN_MESSAGE);
        } else if (hasDealerStopped) {
            answer.append("\n").append(END_DEALERS_TURN_MESSAGE);
        }
        if (!hasPlayerStopped && !isGameOverCheck()) {
            answer.append(PLAYERS_CARD)
                    .append(playerCurrentCard)
                    .append(".\n")
                    .append(PLAYERS_HAND)
                    .append(Card.printDeck(playerHand, 1))
                    .append(PLAYERS_SCORE)
                    .append(hasPlayerAce && playerScore + 10 < WIN_SCORE ? playerScore + 10 : playerScore)
                    .append(".\n")
                    .append(TAKE_CARD_DEALER_MESSAGE)
                    .append(dealerLastCards.size())
                    .append(dealerLastCards.size() == 1 ? " card" : " cards" )
                    .append(". ")
                    .append(NUMBER_OF_CARDS_DEALER_MESSAGE)
                    .append(dealerHand.size())
                    .append(dealerHand.size() == 1 ? " card" : " cards" )
                    .append(".\n")
                    .append(TAKE_CARD_PLAYER_MESSAGE);
        }
        return answer.toString();
    }

    private String determineWinner() {
        if (playerScore > WIN_SCORE) return DEALER_WON;
        if (dealerScore > WIN_SCORE) return PLAYER_WON;
        if (playerScore > dealerScore) return PLAYER_WON;
        if (playerScore == dealerScore) return DRAW;
        return DEALER_WON;
    }

    private boolean isGameOverCheck() {
        return gameOver = (hasPlayerStopped && hasDealerStopped) || playerScore > WIN_SCORE || dealerScore > WIN_SCORE;
    }

    private void takeCardFromDeck(List<Card> deck, List<Card> hand) {
        Card currentCard = deck.getFirst();
        deck.removeFirst();
        hand.add(currentCard);
    }
}