package ppzeff.servise;

import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.InlineQueryRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.InlineQueryResult;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.GetFileResponse;

import lombok.extern.slf4j.Slf4j;
import ppzeff.entity.Loto;
import ppzeff.entity.People;
import ppzeff.entity.Zone;
import ppzeff.entity.Status;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BotController
@Slf4j
public class MyBotService implements TelegramMvcController {
    private final LotoService lotoService;
    private final PeopleService peopleService;
    private final KeyBoardService keyBoardService;
    private final ZoneService zoneService;

    private static final Map<Long, Integer> mesIdToChange = new HashMap<>();
    private final CRM crm;

    public MyBotService(LotoService lotoService, PeopleService peopleService, KeyBoardService keyBoardService, ZoneService zoneService, CRM crm) {
        this.lotoService = lotoService;
        this.peopleService = peopleService;
        this.keyBoardService = keyBoardService;
        this.zoneService = zoneService;
        this.crm = crm;
    }


    @CallbackQueryRequest
    public BaseRequest call(CallbackQuery callback, Chat chat, Message message, TelegramBot bot) {
        log.info("chatID {} - callbackQuery message '{}' data `{}`", chat.id(), callback.message().text(), callback.data());

        Loto loto = lotoService.getLotoById(chat.id());
        switch (loto.getStatus()) {
            case GET_ZONE -> {
                Zone zone = zoneService.findZoneById(callback.data());
                loto.setZone(zone);
                loto.setStatus(Status.GET_DATE);
                lotoService.setLoto(chat.id(), loto);

                EditMessageText editMessageText =
                        new EditMessageText(
                                chat.id(),
                                callback.message().messageId(),
                                callback.message().text() + "\n" +
                                        "Выбрано: " + loto.getZone().getZoneName());
                editMessageText.replyMarkup(new InlineKeyboardMarkup());
                editMessageText.parseMode(ParseMode.Markdown);
                bot.execute(editMessageText);

                SendMessage sendMessage = new SendMessage(
                        chat.id(),
                        "Укажите дату проведения работ:")
                        .replyMarkup(keyBoardService.getDateKeyBoard());
                return sendMessage;


            }
            case GET_DATE -> {
                loto.setDate(callback.data());
                loto.setStatus(Status.GET_PEOPLE);
                lotoService.setLoto(chat.id(), loto);

                mesIdToChange.put(chat.id(), callback.message().messageId());
                EditMessageText editMessageText =
                        new EditMessageText(
                                chat.id(),
                                callback.message().messageId(),
                                callback.message().text() + "\n" +
                                        "Выбрано: " + loto.getDate());
                editMessageText.replyMarkup(new InlineKeyboardMarkup());
                editMessageText.parseMode(ParseMode.Markdown);
                bot.execute(editMessageText);

                SendMessage sendMessage = new SendMessage(chat.id(),
                        "Укажите специалиста:")
                        .replyMarkup(keyBoardService.getInlineQueryBoard());
                return sendMessage;


            }
            case GET_COMPLEX -> {
                loto.setComplex(callback.data());
                loto.setStatus(Status.GET_PHOTO);
                lotoService.setLoto(chat.id(), loto);

                EditMessageText editMessageText =
                        new EditMessageText(
                                chat.id(),
                                callback.message().messageId(),
                                callback.message().text() + "\n" +
                                        "Выбрано: " + loto.getComplex());
                editMessageText.replyMarkup(new InlineKeyboardMarkup());
                editMessageText.parseMode(ParseMode.Markdown);
                bot.execute(editMessageText);

                SendMessage sendMessage = new SendMessage(chat.id(),
                        "Приложите фото титульного листа: ");
                return sendMessage;
            }

            case END_LOTO_REG -> {
                if (callback.data().equals("ДА")) {
                    loto.setStatus(Status.DONE_LOTO_REG);
                    lotoService.setLoto(chat.id(), loto);

                    EditMessageCaption editMessageCaption = new EditMessageCaption(chat.id(), callback.message().messageId());
                    editMessageCaption.caption(callback.message().caption());
                    editMessageCaption.replyMarkup(new InlineKeyboardMarkup());
                    editMessageCaption.parseMode(ParseMode.Markdown);
                    bot.execute(editMessageCaption);

                    return new SendMessage(chat.id(),
                            "Выбрано: Верно. \nLOTO # " + crm.registrationLoto(loto, bot) + "\n"
                    );
                } else {
                    return new SendMessage(chat.id(), "Жаль :)\nначать регистрацию наряда  /startRegistrationLOTO\n");
                }
            }
            default -> {
                return new SendMessage(chat.id(), "что-то пошло не так...");
            }
        }
    }

    @InlineQueryRequest
    public BaseRequest inlineQueryRequest(InlineQuery inlineQuery) throws IOException {
        log.info("chatID {} - inlineQuery `{}`", inlineQuery.from().id(), inlineQuery.query());

        Loto loto = lotoService.getLotoById(inlineQuery.from().id());
        switch (loto.getStatus()) {
            case GET_PEOPLE -> {
                List<InlineQueryResult> results = new ArrayList<>();
                List<People> list = peopleService.filterPeopleBySurname(inlineQuery.query());
                for (int i = 0; i < list.size(); i++) {
                    results.add(new InlineQueryResultArticle(String.valueOf(i),
                            list.get(i).getName() + " " + list.get(i).getPatronymic(),
                            "/loto " + list.get(i).getMyId()).thumbUrl(list.get(i).getPhotoLink()));
                }
                InlineQueryResult[] arr = results.toArray(new InlineQueryResult[]{});
                AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery(inlineQuery.id(), arr);
                return answerInlineQuery;
            }
            default -> {
            }
        }
        return null;
    }

    @BotRequest(value = "/start", type = {MessageType.MESSAGE})
    public BaseRequest start(TelegramBot bot, Message message) {
        log.info("chatID {} - command `{}` - {}", message.chat().id(), message.text(), message);
        return new SendMessage(message.chat().id(), crm.getStartMessage());
    }

    @BotRequest(value = {"/lotoform", "/getLOTOform"}, type = {MessageType.MESSAGE})
    public BaseRequest lotoform(TelegramBot bot, Message message) {
        log.info("chatID {} - command `{}` - {}", message.chat().id(), message.text(), message);
        try {
            return new SendDocument(message.chat().id(),
                    crm.getFileFromResource("SAF_F026_LOTO_FORM_v.3.xlsx"))
                    .caption("SAF F026 v.3");
        } catch (IOException e) {
            log.error("file not found " + "SAF_F026_LOTO_FORM_v.3.xlsx");
            return new SendMessage(message.chat().id(), "Ошибка отправки файла");
        }
    }

    @BotRequest(value = {"/getStandard", "/standard"}, type = {MessageType.MESSAGE})
    public BaseRequest getStandard(TelegramBot bot, Message message) {
        log.info("chatID {} - command `{}` - {}", message.chat().id(), message.text(), message);
        try {
            return new SendDocument(message.chat().id(),
                    crm.getFileFromResource("LOTO_Standard_v1.4.pdf"))
                    .caption("Стандарт версии v1.4.");
        } catch (IOException e) {
            log.error("file not found " + "LOTO_Standard_v1.4.pdf");
            return new SendMessage(message.chat().id(), "Ошибка отправки файла");
        }
    }

    @BotRequest(value = {"/load"}, type = {MessageType.MESSAGE})
    public void loadExcel(TelegramBot bot, Message message) {
        crm.sendExcel(message.chat().id(), bot);
    }


    @BotRequest(value = {"/getCardinalRules", "/cardinal"}, type = {MessageType.MESSAGE})
    public BaseRequest getCardinalRules(TelegramBot bot, Message message) {
        log.info("chatID {} - command `{}` - {}", message.chat().id(), message.text(), message);
        try {
            return new SendDocument(message.chat().id(),
                    crm.getFileFromResource("Cardinal_rules.pdf"))
                    .caption("Кардинальные правила.");
        } catch (IOException e) {
            log.error("file not found " + "Cardinal_rules.pdf");
            return new SendMessage(message.chat().id(), "Ошибка отправки файла");
        }
    }

    @BotRequest(value = {"/startRegistrationLOTO", "/registrationloto"}, type = {MessageType.MESSAGE})
    public BaseRequest startRegistrationLOTO(Message message, Chat chat) {
        log.info("chatID {} - command `{}` - {}", message.chat().id(), message.text(), message);

        Loto loto = new Loto();
        loto.setStatus(Status.GET_ZONE);
        lotoService.setLoto(chat.id(), loto);
        return new SendMessage(
                chat.id(),
                "Выберите зону проведения работ:")
                .replyMarkup(keyBoardService.getZoneBoard());
    }

    @BotRequest(type = {MessageType.MESSAGE})
    public BaseRequest photo(Chat chat, Message message, TelegramBot bot) {
        log.info("chatID {} - command `{}` - {}", message.chat().id(), message.text(), message);

        BaseRequest sendMessage = null;

        Loto loto = lotoService.getLotoById(chat.id());
        switch (loto.getStatus()) {
            case GET_PHOTO -> {
                PhotoSize[] photo = message.photo();

                if (photo != null) {
                    log.info("chatID {} - photo length `{}` - {}", message.chat().id(), photo.length, message);

                    GetFile getFile = new GetFile(photo[photo.length - 1].fileId());
                    GetFileResponse getFileResponse = bot.execute(getFile);
                    com.pengrad.telegrambot.model.File file1 = getFileResponse.file();
                    String fullPath = bot.getFullFilePath(file1);
                    try {
                        InputStream inputStream = new URL(fullPath).openStream();
                        loto.setImg(inputStream.readAllBytes());

//                        Files.copy(inputStream, Paths.get(photo[photo.length - 1].fileId() + ".jpg"), StandardCopyOption.REPLACE_EXISTING);
                        sendMessage = new SendPhoto(chat.id(), loto.getImg());
//                        sendMessage = new SendPhoto(chat.id(), new File(Paths.get(photo[photo.length - 1].fileId() + ".jpg").toUri()));
//                        InputStream inputStream1 = Files.newInputStream(Paths.get(photo[photo.length - 1].fileId() + ".jpg"));

                        ((SendPhoto) sendMessage).caption("\nВерно?" +
                                "\nЗона работ: " + loto.getZone().getZoneName() +
                                "\nДата работ:  " + loto.getDate() +
                                "\nКомплексная:  " + loto.getComplex() +
                                "\nСпециалист: " + loto.getPeople().getName() + " " + loto.getPeople().getPatronymic()
                        ).replyMarkup(keyBoardService.getYesNoKeyBoard());
                        loto.setStatus(Status.END_LOTO_REG);
                        lotoService.setLoto(chat.id(), loto);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    sendMessage = new SendMessage(chat.id(), "Ожидается фото...");
                }
            }
            default -> {
                log.info("Message " + message.text() + "from " + chat.id());
                log.info("LOTO: " + lotoService.getLotoById(chat.id()));
                return new SendMessage(chat.id(), "Программа ожидает другой ввод.\n Нажмите /start для получения информации.");
            }
        }
        return sendMessage;
    }


    @MessageRequest("/loto {peopleId:[\\S]+}")
    public BaseRequest lotoTag(@BotPathVariable("peopleId") String peopleId, Chat chat, Message message, TelegramBot bot) {
        log.info("chatID {} - command `{}` - {}", message.chat().id(), message.text(), message);

        Loto loto = lotoService.getLotoById(chat.id());
        switch (loto.getStatus()) {
            case GET_PEOPLE -> {
                loto.setPeople(peopleService.getPeopleById(peopleId));
                loto.setStatus(Status.GET_COMPLEX);
                lotoService.setLoto(chat.id(), loto);

                DeleteMessage deleteMessage = new DeleteMessage(chat.id(), message.messageId());
                bot.execute(deleteMessage);

                EditMessageText editMessageText =
                        new EditMessageText(
                                chat.id(),
                                mesIdToChange.get(chat.id()) + 1,
//                                message.messageId() - 1,
                                "Укажите специалиста:" + "\n" +
                                        "Выбрано: " + loto.getPeople().getName() + " " + loto.getPeople().getPatronymic());
                editMessageText.replyMarkup(new InlineKeyboardMarkup());
                editMessageText.parseMode(ParseMode.Markdown);
                bot.execute(editMessageText);

                SendMessage sendMessage = new SendMessage(chat.id(),
                        "Комплексная блокировка? ").replyMarkup(keyBoardService.getComplexKeyBoard());

                return sendMessage;
            }
            default -> {
                new SendMessage(chat.id(), "ERROR");
            }
        }
        return null;
    }

    @Override
    public String getToken() {
        return ""; // @dlfyt_bot
//        return ""; // @DANCHEEHS_bot

    }
}
