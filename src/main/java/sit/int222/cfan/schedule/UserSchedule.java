package sit.int222.cfan.schedule;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.Jwtblacklist;
import sit.int222.cfan.entities.Pin;
import sit.int222.cfan.repositories.JwtblacklistRepository;
import sit.int222.cfan.repositories.PinRepository;

import java.sql.Timestamp;
import java.util.*;

@Service
@Log4j2
public class UserSchedule {
    @Autowired
    JwtblacklistRepository jwtblacklistRepository;
    @Autowired
    PinRepository pinRepository;

    public Timestamp getCurrentTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Bangkok"));
        Date date = calendar.getTime();
        return new Timestamp(date.getTime());
    }

    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Bangkok")
    public void deleteJwtblacklist() {
        List<Jwtblacklist> jwtblacklists = jwtblacklistRepository.findAllByExpLessThan(getCurrentTime());
        ListIterator<Jwtblacklist> iterator = jwtblacklists.listIterator();
        while (iterator.hasNext()) {
            Jwtblacklist jwtblacklist = iterator.next();
            log.info("deleteJwtblacklist :" + jwtblacklist.getJwtblacklistid() + " " + jwtblacklist.getExp());
        }
        jwtblacklistRepository.deleteAll(jwtblacklists);
    }

    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Bangkok")
    public void deletePin() {
        List<Pin> pinList = pinRepository.findAllByExpLessThan(getCurrentTime());
        ListIterator<Pin> iterator = pinList.listIterator();
        while (iterator.hasNext()) {
            Pin pin = iterator.next();
            log.info("deletePin :" + pin.getPinid() + " " + pin.getExp());
        }
        pinRepository.deleteAll(pinList);
    }
}
