package agenda;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single event in the agenda.
 */
public class Event {

    /**
     * The title of this event
     */
    private String myTitle;

    /**
     * The starting time of the event
     */
    private LocalDateTime myStart;

    /**
     * The duration of the event
     */
    private Duration myDuration;

    private ChronoUnit repetitionFrequency = null;
    private Termination termination = null;
    private Set<LocalDate> exceptions = new HashSet<>();

    /**
     * Constructs an event
     *
     * @param title    the title of this event
     * @param start    the start time of this event
     * @param duration the duration of this event
     */
    public Event(String title, LocalDateTime start, Duration duration) {
        this.myTitle = title;
        this.myStart = start;
        this.myDuration = duration;
    }

    public void setRepetition(ChronoUnit frequency) {
        this.repetitionFrequency = frequency;
    }

    public void addException(LocalDate date) {
        exceptions.add(date);
    }

    public void setTermination(LocalDate terminationInclusive) {
        this.termination = new Termination(myStart.toLocalDate(), repetitionFrequency, terminationInclusive);
    }

    public void setTermination(long numberOfOccurrences) {
        this.termination = new Termination(myStart.toLocalDate(), repetitionFrequency, numberOfOccurrences);
    }

    public int getNumberOfOccurrences() {
        if (termination == null) throw new UnsupportedOperationException("No termination set");
        return termination.getNumberOfOccurrences();
    }

    public LocalDate getTerminationDate() {
        if (termination == null) throw new UnsupportedOperationException("No terminaion set");
        return termination.getTerminationDate();
    }

    /**
     * Tests if an event occurs on a given day
     *
     * @param aDay the day to test
     * @return true if the event occurs on that day, false otherwise
     */

    public boolean isInDay(LocalDate aDay) {
        if (repetitionFrequency == null) {
            // Cas événement simple
            LocalDate startDate = myStart.toLocalDate();
            LocalDate endDate = myStart.plus(myDuration).toLocalDate();
            return (!aDay.isBefore(startDate)) && (!aDay.isAfter(endDate));
        } else {
            // Cas événement répétitif
            LocalDate current = myStart.toLocalDate();
            while (!current.isAfter(aDay)) {
                if (exceptions.contains(current)) {
                    current = current.plus(1, repetitionFrequency);
                    continue;
                }
                if (current.equals(aDay)) return true;
                if (termination != null) {
                    if (current.isEqual(termination.getTerminationDate())) return true; // Inclure la date de terminaison
                    if (termination.isAfter(current)) break;
                }
                current = current.plus(1, repetitionFrequency);
            }
            return false;
        }
    }

    /**
     * @return the myTitle
     */
    public String getTitle() {
        return myTitle;
    }

    /**
     * @return the myStart
     */
    public LocalDateTime getStart() {
        return myStart;
    }

    /**
     * @return the myDuration
     */
    public Duration getDuration() {
        return myDuration;
    }

    @Override
    public String toString() {
        return "Event{title='%s', start=%s, duration=%s}".formatted(myTitle, myStart, myDuration);
    }
}
