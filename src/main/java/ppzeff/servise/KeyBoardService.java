package ppzeff.servise;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class KeyBoardService {
    @Autowired
    private final ZoneService zoneService;

    public KeyBoardService(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    public InlineKeyboardMarkup getDateKeyBoard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (int i = 0; i <= 3; i++) {
            var date = LocalDate.now().plusDays(i).format(dateFormat);
            markup.addRow(
                    new InlineKeyboardButton(date)
                            .callbackData(date));
        }
        return markup;

    }

    public InlineKeyboardMarkup getComplexKeyBoard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.addRow(
                new InlineKeyboardButton("ДА").callbackData("YES"),
                new InlineKeyboardButton("НЕТ").callbackData("NO")
        );
        return markup;
    }

    public InlineKeyboardMarkup getYesNoKeyBoard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.addRow(
                new InlineKeyboardButton("Верно").callbackData("ДА"),
                new InlineKeyboardButton("НЕ верно").callbackData("НЕТ")
        );
        return markup;
    }

    public InlineKeyboardMarkup getInlineQueryBoard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.addRow(
                new InlineKeyboardButton("выбрать специалиста...").switchInlineQueryCurrentChat("")
        );
        return markup;
    }

    public InlineKeyboardMarkup getZoneBoard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        zoneService.getAllZone().forEach(el -> markup.addRow(new InlineKeyboardButton(el.getZoneName()).callbackData(el.getMyId())));
        return markup;
    }
}
