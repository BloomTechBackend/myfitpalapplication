package com.decadev.services;

import com.decadev.entities.*;
import com.decadev.enums.*;
import com.decadev.repositories.WorkoutSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WorkoutSessionService {

    private ExerciseService exerciseService;
    private WorkoutSessionRepository workoutSessionRepository;


    @Autowired
    public WorkoutSessionService(ExerciseService exerciseService, WorkoutSessionRepository workoutSessionRepository) {
        this.exerciseService = exerciseService;
        this.workoutSessionRepository = workoutSessionRepository;
    }
//TODO: need to revisit adding relevant number of exercises ,priority count + accessory count

    public List<WorkoutSession> generateWorkoutSessionsForUser(User user) {
        int availability = Math.min(user.getAvailability(), 7); // Limit to 7 sessions max
        List<WorkoutSession> sessions = new ArrayList<>();

        List<Day> daysForGoal = new ArrayList<>();
        if (user.getFitnessGoal() == FitnessGoal.WEIGHT_LOSS) {
            daysForGoal = Arrays.asList(Day.UPPER_BODY, Day.LOWER_BODY); // Focus on upper and lower body for weight loss
        } else if (user.getFitnessGoal() == FitnessGoal.BUILD_MUSCLE) {
            daysForGoal = Arrays.asList(Day.BACK_AND_SHOULDERS, Day.CHEST, Day.ARMS_CORE, Day.LEGS);
        } else if (user.getFitnessGoal() == FitnessGoal.STRENGTH) {
            daysForGoal = Arrays.asList(Day.PUSH, Day.PULL,  Day.LEGS);
        }

        for (int i = 0; i < availability; i++) {
            Day day = daysForGoal.get(i % daysForGoal.size()); // Use custom day list based on fitness goal
            List<Exercise> exercisesForDay = exerciseService.getExercisesForDay(user.getFitnessLevel(), user.getFitnessGoal(), day);

            WorkoutSession session = new WorkoutSession();
            session.setUserId(user.getUserId());
            session.setDay(day);
            session.setExercises(exercisesForDay);
            session.setSessionDuration("1"); // Assume 1 hour for simplicity, adjust if needed

            workoutSessionRepository.save(session);
            sessions.add(session);
        }

        return sessions;
    }


    /**
     * Retrieves a workout session by its unique identifier.
     *
     * @param sessionId the unique ID of the workout session.
     * @return an Optional containing the found workout session or an empty Optional if not found.
     */
    public Optional<WorkoutSession> findWorkoutSessionById(String sessionId) {
        return workoutSessionRepository.findById(sessionId);
    }



    /**
     * Deletes a workout session from the repository by its unique identifier.
     *
     * @param sessionId the unique ID of the workout session to be deleted.
     */
    public void deleteWorkoutSessionById(String sessionId) {
        workoutSessionRepository.deleteById(sessionId);
    }
}