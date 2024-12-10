package agenda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste des événements répétitifs sans fin, mais avec des exceptions
 */
public class NoTerminationEventTest {
    // November 1st, 2020
    LocalDate nov_1_2020 = LocalDate.of(2020, 11, 1);

    // November 1st, 2020, 22:30
    LocalDateTime nov_1__2020_22_30 = LocalDateTime.of(2020, 11, 1, 22, 30);

    // 120 minutes
    Duration min_120 = Duration.ofMinutes(120);

    // Un événement répétitif quotidien, sans fin
    Event neverEnding;

    @BeforeEach
    void setUp() {
        // November 1st, 2020, 22:30, 120 minutes
        neverEnding = new Event("Never Ending", nov_1__2020_22_30, min_120);
        neverEnding.setRepetition(ChronoUnit.DAYS);
    }

    @Test
    public void eventIsInItsStartDay() {
        assertTrue(neverEnding.isInDay(nov_1_2020),
            "Un événement a lieu dans son jour de début");
    }

    @Test
    public void eventIsNotInDayBefore() {
        assertFalse(neverEnding.isInDay(nov_1_2020.minusDays(1)),
            "Un événement n'a pas lieu avant son jour de début");
    }

    @Test
    public void eventOccurs10DayAfter() {
        assertTrue(neverEnding.isInDay(nov_1_2020.plusDays(10)),
            "Cet événement doit se produire tous les jours");
    }
    
    @Test
    public void eventIsNotInExceptionDays() {
        neverEnding.addException(nov_1_2020.plusDays(2)); // ne se produit pas à J+2
        neverEnding.addException(nov_1_2020.plusDays(4)); // ne se produit pas à J+4
        assertTrue(neverEnding.isInDay(nov_1_2020.plusDays(1)),
            "Cet événement se produit tous les jours sauf exceptions");
        assertFalse(neverEnding.isInDay(nov_1_2020.plusDays(2)),
            "Cet événement ne se produit pas à J+2");
        assertTrue(neverEnding.isInDay(nov_1_2020.plusDays(3)),
            "Cet événement se produit tous les jours sauf exceptions");
        assertFalse(neverEnding.isInDay(nov_1_2020.plusDays(4)),
            "Cet événement ne se produit pas à J+4");
    }

    @Test
    public void testEventWithoutRepetition() {
        Event simpleEvent = new Event("Simple Event", nov_1__2020_22_30, min_120);
        simpleEvent.addException(nov_1_2020);

        assertTrue(simpleEvent.isInDay(nov_1_2020), "L'événement simple doit se produire le jour de son début même avec une exception");
    }

    @Test
    public void testExceptionOnStartDay() {
        neverEnding.addException(nov_1_2020); // Exception le jour de début

        assertFalse(neverEnding.isInDay(nov_1_2020), "L'événement ne doit pas se produire le jour de son début s'il y a une exception");
    }

    @Test
    public void testEventOccursFarInFuture() {
        assertTrue(neverEnding.isInDay(nov_1_2020.plusDays(1000)), "L'événement doit se produire 1000 jours après son début");
    }

    @Test
    public void repetitiveEventWithExceptionIsNotInDay() {
        Event repetitiveEvent = new Event("Repetitive Event", nov_1__2020_22_30, min_120);
        repetitiveEvent.setRepetition(ChronoUnit.DAYS);
        repetitiveEvent.addException(nov_1_2020.plusDays(2));

        assertFalse(repetitiveEvent.isInDay(nov_1_2020.plusDays(2)), "L'événement ne doit pas se produire le jour de l'exception");
        assertTrue(repetitiveEvent.isInDay(nov_1_2020.plusDays(1)), "L'événement doit se produire un jour sans exception");
    }

    @Test
    public void addExceptionToRepetitiveEvent() {
        Event repetitiveEvent = new Event("Repetitive Event", nov_1__2020_22_30, min_120);
        repetitiveEvent.setRepetition(ChronoUnit.DAYS);
        repetitiveEvent.addException(nov_1_2020.plusDays(1));

        assertFalse(repetitiveEvent.isInDay(nov_1_2020.plusDays(1)), "L'événement ne doit pas se produire le jour de l'exception ajoutée");
    }

    @Test
    public void testGetNumberOfOccurrencesWithoutTermination() {
        Event event = new Event("Event without termination", nov_1__2020_22_30, min_120);
        event.setRepetition(ChronoUnit.DAYS);
        assertThrows(UnsupportedOperationException.class, event::getNumberOfOccurrences,
                "La méthode doit lever une exception si aucune terminaison n'est définie.");
    }


}
