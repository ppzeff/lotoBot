package ppzeff.config;

import com.github.kshashov.telegram.config.TelegramBotGlobalProperties;
import com.github.kshashov.telegram.config.TelegramBotGlobalPropertiesConfiguration;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MyBotConfiguration implements TelegramBotGlobalPropertiesConfiguration {

    @Override
    public void configure(TelegramBotGlobalProperties.Builder builder) {

        String token = "";
        builder.configureBot(token, botBuilder -> {
            botBuilder.configure(builder1 -> builder1.updateListenerSleep(200L));
        }).processBot(token, telegramBot -> {


            List<BotCommand> commands = new ArrayList<>();
            commands.add(new BotCommand("/start", "начать работу"));
            commands.add(new BotCommand("/registrationloto", "начать регистрацию разрешения LOTO"));
            commands.add(new BotCommand("/lotoform", "получить форму LOTO разрешения"));
            commands.add(new BotCommand("/cardinal", "направить кардинальные правила"));
            commands.add(new BotCommand("/standard", "получить стандарт LOTO"));

            if (telegramBot.execute(new SetMyCommands(commands.toArray(new BotCommand[0]))).isOk()) {
                log.info("config bot command");
                log.info(commands.toString());
            } else {
                log.error("false config bot command");
            }

            // TODO
        });
    }
}

