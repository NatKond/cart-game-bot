package de.natkond.controller;

import de.natkond.config.BotConfig;
import de.natkond.service.UpdateHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static de.natkond.util.BotUtils.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class CardGameBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final UpdateHandlerService updateHandlerService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update == null) {
            log.error("Received update is null.");
            return;
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            sendAnswerMessage(updateHandlerService.handleUpdate(update));
        } else {
            log.error("Received unsupported message type {}", update);
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update){
        SendMessage message = createSendMessage(update, "Unsupported Message Type");
        sendAnswerMessage(message);
    }

    void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }
}
