package ppzeff.servise;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ppzeff.entity.Loto;
import ppzeff.entity.UserEntity;
import ppzeff.repo.PeopleRepo;
import ppzeff.repo.UserRepo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CRM {
    @Autowired
    private ZoneService zoneService;
    @Autowired
    PeopleRepo peopleRepo;
    @Autowired
    ExcelService excelService;
    @Autowired
    UserRepo userRepo;
    private final LotoService lotoService;

    public CRM(LotoService lotoService) {
        this.lotoService = lotoService;
    }

    public String getStartMessage() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("Помощь:\n")
                .append("получить стандарт /getStandard\n")
                .append("получить форму LOTO /getLOTOform\n")
                .append("получить координальные правила /getCardinalRules\n")
                .append("начать регистрацию разрешения /startRegistrationLOTO\n");
        return sb.toString();
    }

    String getPathByFileName(String fileName) {
        return CRM.class.getClassLoader().getResource(fileName).getPath();

    }

    public File getFileFromResource(String fileName) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream initialStream = classLoader.getResourceAsStream(fileName);

        if (initialStream == null) {
            throw new IllegalArgumentException("in resource file is not found!");
        } else {
            var tempPath = Files.createTempFile(FilenameUtils.getBaseName(fileName) + "_", "." + FilenameUtils.getExtension(fileName));
            Files.copy(initialStream, tempPath, StandardCopyOption.REPLACE_EXISTING);
            File file = tempPath.toFile();
            file.deleteOnExit();
            return file;
        }
    }

    public Long registrationLoto(Loto loto, TelegramBot bot) {
        try {
            loto.getPeople().setScore(loto.getPeople().getScore() + 1);
            peopleRepo.save(loto.getPeople());
            log.info("Set new score {} to {}", loto.getPeople().getScore(), loto.getPeople().getSurname());

            loto.setDt(LocalDateTime.now());
            lotoService.save(loto);
            sendSavedLoto(bot, loto);
            log.info(loto.toString());
            return loto.getId();
        } catch (RuntimeException e) {
            log.error(loto.toString());
            return null;
        }
    }

    private void sendSavedLoto(TelegramBot bot, Loto loto) {
//        List<Long> list = Arrays.asList(189632357L, 712055665L, 1433116655L, 936623203L);
        List<UserEntity> users = userRepo.findAll().stream().filter(UserEntity::isActive).toList();

        users.forEach(el -> {
            boolean isOk = bot.execute(new SendPhoto(el.getTelegramId(), loto.getImg()).caption("\nЗарегистрировано LOTO #" + loto.getId() +
                    "\nЗона работ: " + loto.getZone().getZoneName() +
                    "\nДата работ:  " + loto.getDate() +
                    "\nКомплексная:  " + loto.getComplex() +
                    "\nСпециалист: " + loto.getPeople().getSurname() + " " + loto.getPeople().getName() + " " + loto.getPeople().getPatronymic()
            )).isOk();
            log.info("Send message to {}:{} is {}", el.getTelegramId(), el.getName(), isOk);
        });

    }

    public void sendExcel(Long chatId, TelegramBot bot) {
        try {
            var file = new File(excelService.savetoExcel(lotoService.findAllLoto()));
            log.info("file send: {}", bot.execute(new SendDocument(chatId, file)).isOk());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}




































