package uz.pdp;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.pdp.model.BotUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends TelegramLongPollingBot {
    Map<Long, BotUser> userMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Main());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return "1691881564:AAHuoS65xxiZ46VSKwAKm7qaqcqmJKHQhCg";
    }


    BotUser botUser = null;
    String userChatId = "1321579147";

    @Override
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();

        String sendingMessage = "";
        SendMessage sendMessage = new SendMessage();
        if (message.hasText()) {
            if (message.getText().equals("/start")) {
                botUser = new BotUser();
                botUser.setStep(1);
                userMap.put(message.getChatId(), botUser);
            } else {
                botUser = userMap.get(message.getChatId());
                if (botUser == null) {
                    sendingMessage = "Серверда хатолик сабаб илтимос /start ни босинг. Юзага келган хатолик сабаб сиздан узр сўраймиз.";
                    sendMessage.setText(sendingMessage);
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (message.getText().equals("change admin chat id")) {
                botUser.setStep(9);
            }

            switch (botUser.getStep()) {
                case 1:
                    sendingMessage = "Исмингизни киритинг";
                    botUser.setStep(2);
                    break;
                case 2:
                    botUser.setName(message.getText());
                    sendingMessage = "Ёшингизни киритинг";
                    botUser.setStep(3);
                    break;
                case 3:
                    botUser.setAge(Integer.parseInt(message.getText()));
                    sendingMessage = "Сизни нима безовта қилябди? (Касаллик ҳақида ёзиб юборсангиз)";
                    botUser.setStep(4);
                    break;
                case 4:
                    sendingMessage = "Сиз билан боғланиш учун телефон рақамингизни юборинг";
                    botUser.setDisease(message.getText());
                    setButton(sendMessage, botUser.getStep());
                    botUser.setStep(5);
                    break;
                case 5:
                    botUser.setPhone(message.getText());
                    sendingMessage = "✅ Сизнинг ҳабарингизни админга жўнатилди" +
                            " тез орада сиз билан боғланишади";
                    botUser.setStep(6);
                    break;
                case 7:
                    sendingMessage = "Мурожаатингизни ёзинг";
                    if (message.getText().equals("Админга мурожаат юбориш")) {
                        botUser.setStep(8);
                    } else {
                        sendingMessage = "Админга мурожаат қилиш учун \"Админга мурожаат юбориш\" тугмасини босинг";
                    }
                    break;
                case 8:
                    if (message.getText().equals("Админга мурожаат юбориш")) {
                        sendingMessage = "Мурожаатингизни ёзинг";
                    } else {
                        sendingMessage = "✅ Сизнинг ҳабарингизни админга жўнатилди" +
                                " тез орада сиз билан боғланишади";
                        botUser.setDisease(message.getText());
                        botUser.setStep(6);
                    }
                    break;
                case 9:
                    sendingMessage = "Id ни киритинг";
                    botUser.setStep(10);
                    break;
                case 10:
                    userChatId = message.getText();
                    botUser.setStep(1);
                    sendingMessage = "✅ Муваффақиятли ўзгартирилди";
                    break;
            }

        } else if (message.hasContact()) {
            Contact contact = message.getContact();

            botUser.setPhone(contact.getPhoneNumber());
            botUser.setStep(6);
            sendingMessage = "✅ Сизнинг ҳабарингизни админга жўнатилди" +
                    " тез орада сиз билан боғланишади";
        }

        if (botUser.getStep() == 6) {
            sendMessage.setText("Исми: " + botUser.getName() + "\nЁши: " + botUser.getAge() +
                    "\n\ud83d\udcde Tel: " + botUser.getPhone() + "\n" +
                    "\nКасаллик : " + botUser.getIllness());
            sendMessage.setChatId(userChatId);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            botUser.setStep(7);
            setButton(sendMessage, botUser.getStep());
        }
        sendMessage.setText(sendingMessage);
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }

    @Override
    public String getBotUsername() {
        return "ipar_admin_bot";
    }

    public void setButton(SendMessage sendMessage, int step) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        if (step == 4) {
            KeyboardRow keyboardRow = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setText("\ud83d\udcde Рақамни юбориш");
            keyboardButton.setRequestContact(true);
            keyboardRow.add(keyboardButton);
            keyboardRows.add(keyboardRow);
        }
        if (step == 7) {
            KeyboardRow keyboardRow = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setText("Админга мурожаат юбориш");
            keyboardRow.add(keyboardButton);
            keyboardRows.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

    }
}
