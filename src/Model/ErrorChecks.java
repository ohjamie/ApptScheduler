package Model;

import javafx.collections.ObservableList;
import java.time.*;
import java.time.format.DateTimeFormatter;
import static Utility.DBConnection.getAllAppointments;

/** LAMBDA EXPRESSION #2: SORTING A COLLECTION
 * By using the lambda expression, it simplifies the following, thanks to abstraction:
 * -- Comparator<Appointment> compareByStart = Comparator.comparing(Appointment::getStart);
 * -- Collections.sort(apptTimes, compareByStart);
 * into one line of a lambda expression as displayed below. */

public class ErrorChecks {

    public static boolean apptInFifteenMinutes() {
        LocalDateTime currentTime = LocalDateTime.now();

        for (Appointment appt : getAllAppointments()) {
            LocalDateTime apptStart = appt.getStart();
            if (currentTime.isAfter(apptStart.minusMinutes(15)) && currentTime.isBefore(apptStart)) {
                return true;
            }
        }
        return false;
    }

    public static boolean selectedApptIsOverlapping(Appointment selectedAppt, LocalDateTime newStartTime, LocalDateTime newEndTime) {

        // LAMBDA EXPRESSION #2: SORTING WITH COMPARISON
        ObservableList<Appointment> apptTimes = getAllAppointments();
        apptTimes.sort((o1, o2) -> o1.getStart().compareTo(o2.getStart()));

        Appointment apptBefore;
        Appointment apptAfter;

        if(apptTimes.indexOf(selectedAppt) == 0) {
            apptAfter = apptTimes.get(apptTimes.indexOf(selectedAppt) + 1);
            System.out.println("appointment selected should be 0. appt selected: " + apptTimes.indexOf(selectedAppt));
            if (newEndTime.isAfter(apptAfter.getStart())) {
                System.out.println("time overlaps");
                return true;
            }
        } else if(apptTimes.indexOf(selectedAppt) == (apptTimes.size() - 1)) {
            apptBefore = apptTimes.get(apptTimes.indexOf(selectedAppt) - 1);
            System.out.println("appointment selected should be last. appt selected: " + apptTimes.indexOf(selectedAppt));
            if (newStartTime.isBefore(apptBefore.getEnd())) {
                System.out.println("time overlaps");
                return true;
            }
        } else if((apptTimes.indexOf(selectedAppt) > 0) && (apptTimes.indexOf(selectedAppt) < (apptTimes.size() - 1))) {
            apptBefore = apptTimes.get(apptTimes.indexOf(selectedAppt) - 1);
            apptAfter = apptTimes.get(apptTimes.indexOf(selectedAppt) + 1);
            System.out.println("appointment selected is neither first or last. appt selected: " + apptTimes.indexOf(selectedAppt));
            if (newStartTime.isBefore(apptBefore.getEnd()) || newEndTime.isAfter(apptAfter.getStart())) {
                System.out.println("time overlaps");
                return true;
            }
        }
        else {
            System.out.println("time does not overlap");
            return false;
        }
        return false;
    }

    public static boolean newApptIsOverlapping(Appointment newAppt, LocalDateTime startTime, LocalDateTime endTime) {

        // LAMBDA EXPRESSION #2: SORTING WITH COMPARISON
        ObservableList<Appointment> apptTimes = getAllAppointments();
        apptTimes.sort((o1, o2) -> o1.getStart().compareTo(o2.getStart()));

        for(Appointment appt : apptTimes) {
            System.out.println(appt.getStart());
        }

        Appointment apptBefore;
        Appointment apptAfter;

        if(apptTimes.indexOf(newAppt) == 0) {
            apptAfter = apptTimes.get(apptTimes.indexOf(newAppt) + 1);
            if (endTime.isAfter(apptAfter.getStart())) {
                System.out.println("Error: times overlap. First appointment.");
                return true;
            }
        } else if(apptTimes.indexOf(newAppt) == (apptTimes.size() - 1)) {
            apptBefore = apptTimes.get(apptTimes.indexOf(newAppt) - 1);
            if (startTime.isBefore(apptBefore.getEnd())) {
                System.out.println("Error: times overlap. Middle of all appointments.");
                return true;
            }
        } else if((apptTimes.indexOf(newAppt) > 0) && (apptTimes.indexOf(newAppt) < (apptTimes.size() - 1))) {
            apptBefore = apptTimes.get(apptTimes.indexOf(newAppt) - 1);
            apptAfter = apptTimes.get(apptTimes.indexOf(newAppt) + 1);
            if (startTime.isBefore(apptBefore.getEnd()) || endTime.isAfter(apptAfter.getStart())) {
                System.out.println("Error: times overlap. Last appointment.");
                return true;
            }
        }
        else {
            System.out.println("Success: times do not overlap");
            return false;
        }
        return false;
    }

    public static boolean isWithinBusinessHours(LocalDateTime apptStart, LocalDateTime apptEnd) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime apptStartTime = LocalTime.parse(apptStart.toString().substring(11,16), timeFormatter);
        LocalTime apptEndTime = LocalTime.parse(apptEnd.toString().substring(11,16), timeFormatter);
        LocalTime open = LocalTime.of(9, 0);
        LocalTime closed = LocalTime.of(17,0);

        if (apptEnd.isBefore(apptStart)) {
            System.out.println("End is before start");
            return false;
        } else if(apptStartTime.isBefore(open) || apptStartTime.isAfter(closed)) {
            return false;
        } else if(apptEndTime.isAfter(closed)) {
            return false;
        } else if (apptStart.getDayOfWeek()==DayOfWeek.SATURDAY || apptStart.getDayOfWeek()==DayOfWeek.SUNDAY) {
            System.out.println("Closed on weekends");
            return false;
        } else if (apptEnd.getDayOfWeek()==DayOfWeek.SATURDAY || apptEnd.getDayOfWeek()==DayOfWeek.SUNDAY) {
            System.out.println("Closed on weekends");
            return false;
        } else {
            System.out.println("Appt is within business hours");
            return true;
        }
    }

    // For filtering all/week/month lists on dashboard
    public static boolean isWithinRange(LocalDateTime startDateRange, LocalDateTime apptDate, LocalDateTime endDateRange) {
        return apptDate.isAfter(startDateRange) && apptDate.isBefore(endDateRange) || apptDate.isEqual(startDateRange);
    }
}
