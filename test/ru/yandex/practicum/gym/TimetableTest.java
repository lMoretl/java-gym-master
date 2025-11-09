package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mon = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mon.size());
        Assertions.assertEquals(new TimeOfDay(13, 0), mon.getFirst().getTimeOfDay());

        List<TrainingSession> tue = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tue.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        List<TrainingSession> thu = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thu.size());
        Assertions.assertEquals(new TimeOfDay(13, 0), thu.get(0).getTimeOfDay());
        Assertions.assertEquals(new TimeOfDay(20, 0), thu.get(1).getTimeOfDay());
        // вторник — 0
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        Assertions.assertEquals(1,
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size());
        Assertions.assertTrue(
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)).isEmpty());
    }


    @Test
    void testSameTimeDifferentCoachesAndGroups() {
        Timetable timetable = new Timetable();

        Group g1 = new Group("Дети", Age.CHILD, 60);
        Group g2 = new Group("Взрослые", Age.ADULT, 90);
        Coach c1 = new Coach("Иванов", "Игорь", "Олегович");
        Coach c2 = new Coach("Петров", "Павел", "Алексеевич");

        timetable.addNewTrainingSession(new TrainingSession(g1, c1, DayOfWeek.SATURDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(g2, c2, DayOfWeek.SATURDAY, new TimeOfDay(10, 0)));

        List<TrainingSession> at10 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.SATURDAY, new TimeOfDay(10, 0));
        Assertions.assertEquals(2, at10.size());

        List<TrainingSession> day = timetable.getTrainingSessionsForDay(DayOfWeek.SATURDAY);
        Assertions.assertEquals(2, day.size());
        Assertions.assertEquals(new TimeOfDay(10, 0), day.get(0).getTimeOfDay());
        Assertions.assertEquals(new TimeOfDay(10, 0), day.get(1).getTimeOfDay());
    }

    @Test
    void testOrderByTimeWithinDay() {
        Timetable timetable = new Timetable();

        Group g = new Group("Группа", Age.ADULT, 60);
        Coach c = new Coach("Смирнов", "Антон", "Юрьевич");

        timetable.addNewTrainingSession(new TrainingSession(g, c, DayOfWeek.WEDNESDAY, new TimeOfDay(18, 30)));
        timetable.addNewTrainingSession(new TrainingSession(g, c, DayOfWeek.WEDNESDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(g, c, DayOfWeek.WEDNESDAY, new TimeOfDay(14, 15)));

        List<TrainingSession> w = timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY);
        Assertions.assertEquals(3, w.size());
        Assertions.assertEquals(new TimeOfDay(9, 0), w.get(0).getTimeOfDay());
        Assertions.assertEquals(new TimeOfDay(14, 15), w.get(1).getTimeOfDay());
        Assertions.assertEquals(new TimeOfDay(18, 30), w.get(2).getTimeOfDay());
    }

    @Test
    void testGetCountByCoachesSorted() {
        Timetable timetable = new Timetable();

        Group g = new Group("Группа", Age.CHILD, 60);
        Coach a = new Coach("Агеев", "Кирилл", "Владимирович");
        Coach b = new Coach("Белов", "Никита", "Сергеевич");

        timetable.addNewTrainingSession(new TrainingSession(g, a, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(g, a, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(g, a, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(new TrainingSession(g, b, DayOfWeek.THURSDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(g, b, DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));

        LinkedHashMap<Coach, Integer> counts = timetable.getCountByCoaches();
        Assertions.assertEquals(2, counts.size());

        Map.Entry<Coach, Integer> first = counts.entrySet().iterator().next();
        Assertions.assertEquals(a, first.getKey());
        Assertions.assertEquals(3, first.getValue());

        Integer lastVal = counts.values().stream().skip(1).findFirst().orElseThrow();
        Assertions.assertEquals(2, lastVal);
    }
}
