package ppzeff.servise;

import com.pengrad.telegrambot.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogAction {
    public void log(Message message) {
        log.info(message.from().id() + " " + message);
    }
}
