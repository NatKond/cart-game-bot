package de.natkond.cardGame;

import java.util.*;

import static de.natkond.util.BotUtils.*;

public class CardGameOld {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {

        GameSessionOld session = new GameSessionOld();
        String answer = readPlayerAnswer(session.handleStart());
        while (true) {
            String text = switch (answer) {
                //case START -> session.handleStart();
                case PLAY -> session.handlePlay();
                case YES_ANSWER -> session.isGameOver() ? session.handleDefault() : session.handleYes();
                case NO_ANSWER -> session.isGameOver() ? session.handleDefault() : session.handleNo();
                case RULES_ANSWER -> session.handleRules();
                default -> session.handleDefault();
            };
            answer = readPlayerAnswer(text);
        }


        /*while (!gameOver) {
            if (!hasPlayerStopped) {
                if (!playerHand.isEmpty()) {
                    System.out.print("Your hand : " + Card.printDeck(playerHand, 1) + PLAYERS_SCORE + playerScore + ". ");
                }
                System.out.println(String.join("", WELCOME_MESSAGE, READ_RULES_MESSAGE, START_MESSAGE));

                String answer = readPlayerAnswer();
                if (answer.equalsIgnoreCase(YES_ANSWER)) {
                    playerScore = takeCardFromDeck(standardDeck36, playerHand, playerScore);

                    if (playerScore == WIN_SCORE) {
                        System.out.println(END_PLAYERS_TURN_MESSAGE);
                        hasPlayerStopped = true;
                    }

                } else {
                    System.out.println(END_PLAYERS_TURN_MESSAGE);
                    hasPlayerStopped = true;
                }
            }
            if (!hasDealerStopped) {
                if (dealerScore < MAX_SAVE_SCORE_FOR_DEALER) {
                    System.out.println(TAKE_CARD_DEALER_MESSAGE);
                    dealerScore = takeCardFromDeck(standardDeck36, dealerHand, dealerScore);
                    System.out.print("Dealers hand : " + Card.printDeck(dealerHand, 1) + DEALERS_SCORE + dealerScore + ". ");
                    if (dealerScore == WIN_SCORE) {
                        hasDealerStopped = true;
                    }
                } else {
                    System.out.println(END_DEALERS_TURN_MESSAGE);
                    hasDealerStopped = true;
                }
            }
            gameOver = (hasPlayerStopped && hasDealerStopped) || playerScore > WIN_SCORE || dealerScore > WIN_SCORE;
        }
        String scoreMessage = PLAYERS_SCORE + playerScore + DEALERS_SCORE + dealerScore;
        System.out.println(scoreMessage);
        if (dealerScore > WIN_SCORE || (dealerScore < playerScore) && playerScore <= WIN_SCORE) {
            System.out.println(PLAYER_WON);
        } else if (dealerScore == playerScore) {
            System.out.println(DRAW);
        } else {
            System.out.println(DEALER_WON);
        }
         */
    }

    private static String readPlayerAnswer(String text) {
        String answer;
        do {
            System.out.print(text);
            answer = SCANNER.nextLine();
        } while (!answer.equalsIgnoreCase(YES_ANSWER) && !answer.equalsIgnoreCase(NO_ANSWER)
                && !answer.equalsIgnoreCase(RULES_ANSWER) && !answer.equalsIgnoreCase(PLAY));
        return answer;
    }
}
