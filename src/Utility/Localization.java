package Utility;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Localization {

    // localizes time from DB
    public static LocalDateTime LocalizeTimeFromDB(String originalTimeFormat) {
        Timestamp ts = Timestamp.valueOf(originalTimeFormat);
        LocalDateTime ldt = ts.toLocalDateTime();

        ZonedDateTime zdtOut = ldt.atZone(ZoneId.of("UTC"));
        ZonedDateTime zdtOutToLocalTZ = zdtOut.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));

        return zdtOutToLocalTZ.toLocalDateTime();
    }

    // stores time in DB as UTC
    public static LocalDateTime localTimeToUTC(LocalDateTime localTime) {
        ZonedDateTime zdt = localTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime utczdt = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime utcTime = utczdt.toLocalDateTime();

        return utcTime;
    }

    // LDT to String
    public static String LDTToString(LocalDateTime someTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        return someTime.format(formatter);
    }

    // 12 hour to 24 hour time format
    public static String FormatTimeForDB(LocalDateTime time) {
        Timestamp ts = Timestamp.valueOf(time);
        return ts.toString();
    }

    // String to LDT
    public static LocalDateTime StringToLDT(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        return LocalDateTime.parse(time, formatter);
    }
}

