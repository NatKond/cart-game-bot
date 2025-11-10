package de.natkond.service;

import de.natkond.GameSession;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static de.natkond.util.BotUtils.*;

import static de.natkond.util.BotUtils.createSendMessage;

@Service
public class UpdateHandlerServiceImpl implements UpdateHandlerService {

    private final ConcurrentMap<Long, GameSession> sessions = new ConcurrentHashMap<>();

    @Override
    public SendMessage handleUpdate(Update update) {
        Long chatId = update.getMessage().getChatId();
        if (checkMessageExists(update)) {
            return handleMessage(chatId, update);
        }
        return createSendMessage(update,START_MESSAGE + READ_RULES_MESSAGE);
    }

    private SendMessage handleMessage(long chatId, Update update) {
        GameSession session = sessions.computeIfAbsent(chatId, id -> new GameSession());
        String input = update.getMessage().getText();

        return switch (input) {
            case START -> createSendMessage(update, session.handleStart());
            case PLAY -> createSendMessageWithButtons(update, session.handlePlay(), ANSWERS);
            case YES_ANSWER -> {
                if (session.isGameOver()){
                    yield createSendMessage(update, session.handleDefault());
                }
                String answer = session.handleYes();
                yield session.isGameOver() ? createSendMessage(update, answer)
                        : createSendMessageWithButtons(update, answer, ANSWERS);
            }
            case NO_ANSWER -> session.isGameOver() ? createSendMessage(update, session.handleDefault())
                    : createSendMessage(update, session.handleNo());
            case RULES_ANSWER -> session.isGameOver() ? createSendMessage(update, session.handleRules())
                    : createSendMessageWithButtons(update, session.handleRules(), ANSWERS);
            default -> session.isGameOver() ? createSendMessage(update, session.handleDefault())
                    : createSendMessageWithButtons(update, session.handleDefault(), ANSWERS);
        };
    }

    private static boolean checkMessageExists(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }
}
