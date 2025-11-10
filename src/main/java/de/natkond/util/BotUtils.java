package de.natkond.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public final class BotUtils {

    public static final String START = "/start";
    public static final String PLAY = "/play";
    public static final String WELCOME_MESSAGE = "Welcome to the card game!\n";
    public static final String START_MESSAGE = "To start the game, type " + PLAY + "\n";
    public static final String PLAYERS_SCORE = "Your score: ";
    public static final String PLAYERS_HAND = "Your hand:\n";
    public static final String PLAYERS_CARD = "Your card is ";
    public static final String DEALERS_SCORE = " | Dealer's score: ";
    public static final String DEALERS_HAND = "Dealer's hand:\n";
    public static final String TAKE_CARD_DEALER_MESSAGE = "Dealer takes ";
    public static final String NUMBER_OF_CARDS_DEALER_MESSAGE = "Dealer has ";
    public static final String YES_ANSWER = "Y";
    public static final String NO_ANSWER = "N";
    public static final List<String> ANSWERS = List.of(YES_ANSWER, NO_ANSWER);
    public static final String RULES_ANSWER = "/rules";
    public static final String READ_RULES_MESSAGE = "To read rules type " + RULES_ANSWER + "\n";
    public static final String TAKE_CARD_PLAYER_MESSAGE = "Would you like to take a card?(Y/N)\n";
    public static final String END_PLAYERS_TURN_MESSAGE = "You stopped taking cards.\n";
    public static final String END_DEALERS_TURN_MESSAGE = "Dealer stops taking cards.\n";
    public static final String PLAYER_WON = "Congratulations! You won!\n";
    public static final String DEALER_WON = "Dealer won.\n";
    public static final String DRAW = "It's a draw!\n";
    public static final String GAME_OVER = "Game over. To play again, type " + PLAY + ".\n";

    public static final String GAME_RULES =
            """
                    The deck has 36 cards:
                    4 suits: ♠ ♦ ♥ ♣, each has
                    9 cards: 6, 7, 8, 9, 10, J, Q, K, A.
                    Card values: numbers = value,
                    J = 2, Q = 3, K = 4, A = 1 or 11.
                    You draw cards (Y) or stop (N).
                    The dealer then plays — whoever is closer to 21 wins!""";

    private static final int MAX_ROW_SIZE_IN_KEYBOARD = 4;

    public static SendMessage createSendMessage(Update update, String textToSend) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(textToSend);
        return sendMessage;
    }

    public static SendMessage createSendMessageWithButtons(Update update, String textToSend, List<String> buttons) {
        SendMessage sendMessage = createSendMessage(update, textToSend);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = getKeyboardRows(buttons);

        keyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }

    private static List<KeyboardRow> getKeyboardRows(List<String> buttons) {
        List<KeyboardRow> keyboard = new ArrayList<>();

        int rowLength = 0;
        int i = 0;
        KeyboardRow row = new KeyboardRow();
        while (i < buttons.size()) {
            row.add(new KeyboardButton(buttons.get(i)));
            rowLength++;
            if (rowLength == MAX_ROW_SIZE_IN_KEYBOARD || i == buttons.size() - 1) {
                keyboard.add(row);
                row = new KeyboardRow();
                rowLength = 0;
            }
            i++;
        }
        return keyboard;
    }
}
