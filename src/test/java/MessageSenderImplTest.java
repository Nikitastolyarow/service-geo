import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.sender.MessageSenderImpl;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.HashMap;

public class MessageSenderImplTest {

    @Test
    void testLanguageForRussian() {
        String ip = "172.0.32.11";
        HashMap rusMap = new HashMap();
        rusMap.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        GeoServiceImpl geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(ip))
                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));

        LocalizationServiceImpl localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");

        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);

        String preferences = messageSender.send(rusMap);
        String expected = "Добро пожаловать";

        Assertions.assertEquals(expected, preferences);

    }

    @Test
    void testLanguageForNonRussianIP() {
        String ip = "96.44.183.149";
        HashMap engMap = new HashMap();
        engMap.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        GeoServiceImpl geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(ip))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));

        LocalizationServiceImpl localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");

        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);

        String preferences = messageSender.send(engMap);
        String expected = "Welcome";

        Assertions.assertEquals(expected, preferences);
    }

    @Test
    void checkLocationIpRus() {
        String ip = "172.0.32.11";
        Location location = new Location("Moscow", Country.RUSSIA, "Lenina", 15);
        GeoService geoService = new GeoServiceImpl();
        Location probable = geoService.byIp(ip);
        Assertions.assertEquals(location.getCountry(), probable.getCountry());
    }

    @Test
    void checkLocationIpEng() {
        String ip = "96.44.183.149";
        Location location = new Location("New York", Country.USA, " 10th Avenue", 32);
        GeoService geoService = new GeoServiceImpl();
        Location probable = geoService.byIp(ip);
        Assertions.assertEquals(location.getCountry(), probable.getCountry());
    }

    @Test
    void checkReturnTextRus() {
        String text = "Добро пожаловать";
        String probable = new LocalizationServiceImpl().locale(Country.RUSSIA);
        Assertions.assertEquals(text, probable);
    }

    @Test
    void checkReturnTextEng() {
        String text = "Welcome";
        String probable = new LocalizationServiceImpl().locale(Country.USA);
        Assertions.assertEquals(text, probable);
    }

}
