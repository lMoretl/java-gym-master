package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable =
            new EnumMap<>(DayOfWeek.class);

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        timetable.computeIfAbsent(day, d -> new TreeMap<>())
                .computeIfAbsent(time, t -> new ArrayList<>())
                .add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> byTime = timetable.get(dayOfWeek);
        if (byTime == null || byTime.isEmpty()) return Collections.emptyList();

        List<TrainingSession> result = new ArrayList<>();
        for (TimeOfDay time : byTime.navigableKeySet()) {
            result.addAll(byTime.get(time));
        }
        return Collections.unmodifiableList(result);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> byTime = timetable.get(dayOfWeek);
        if (byTime == null) return Collections.emptyList();
        List<TrainingSession> list = byTime.get(timeOfDay);
        if (list == null) return Collections.emptyList();
        return Collections.unmodifiableList(list);
    }

    public LinkedHashMap<Coach, Integer> getCountByCoaches() {
        Map<Coach, Integer> counters = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> byTime : timetable.values()) {
            for (List<TrainingSession> sameTimeList : byTime.values()) {
                for (TrainingSession s : sameTimeList) {
                    counters.merge(s.getCoach(), 1, Integer::sum);
                }
            }
        }

        List<Map.Entry<Coach, Integer>> entries = new ArrayList<>(counters.entrySet());
        entries.sort((e1, e2) -> {
            int c = Integer.compare(e2.getValue(), e1.getValue()); // по убыванию
            if (c != 0) return c;
            // стабильная сортировка по ФИО, чтобы результат был детерминирован
            Coach a = e1.getKey(), b = e2.getKey();
            int s = a.getSurname().compareTo(b.getSurname());
            if (s != 0) return s;
            int n = a.getName().compareTo(b.getName());
            if (n != 0) return n;
            return a.getMiddleName().compareTo(b.getMiddleName());
        });

        LinkedHashMap<Coach, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Coach, Integer> e : entries) {
            result.put(e.getKey(), e.getValue());
        }
        return result;
    }
}
