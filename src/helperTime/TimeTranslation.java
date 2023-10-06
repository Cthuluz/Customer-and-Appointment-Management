package helperTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;

/**
 * The time translation class handles time translation.
 */
public class TimeTranslation {

    /**
     * The returnBusinessHours function returns an observable list of the office hours converted to local system time.
     * The observable list has the hours separated by 15 minutes.
     *
     * @return observable list of local times
     */
    public static ObservableList<LocalTime> returnBusinessHours() {
        ObservableList<LocalTime> businessHours = FXCollections.observableArrayList();

        int i = 0;
        LocalDateTime startDateTime = LocalDateTime.of(1995,5,5,8,0);
        ZonedDateTime startZoned = startDateTime.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime translatedZoned;
        LocalDateTime endDateTime;
        LocalTime endTime;

        while(true) {
            translatedZoned = (startZoned.plusMinutes(i)).withZoneSameInstant(ZoneId.systemDefault());
            endDateTime = translatedZoned.toLocalDateTime();
            endTime = endDateTime.toLocalTime();
            businessHours.add(endTime);
            if ((startDateTime.plusMinutes(i).getHour() >= 22) && (startDateTime.plusMinutes(i).getMinute() == 0)) {
                break;
            }
            i += 15;
        }

        return businessHours;
    }
}
