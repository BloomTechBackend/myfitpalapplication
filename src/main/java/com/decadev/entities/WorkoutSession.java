package com.decadev.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.decadev.converters.DayConverter;
import com.decadev.converters.ExerciseListConverter;
import com.decadev.enums.Day;
import lombok.Data;

import java.util.List;

@Data
@DynamoDBTable(tableName = "WorkoutSessions")
public class WorkoutSession {
    @DynamoDBHashKey(attributeName = "sessionId")
    @DynamoDBAutoGeneratedKey
    private String sessionId;

    @DynamoDBAttribute(attributeName = "userId") // Reference to parent plan
    private String userId;

    @DynamoDBTypeConverted(converter = DayConverter.class)
    @DynamoDBAttribute(attributeName = "day")
    private Day day;

    @DynamoDBTypeConverted(converter = ExerciseListConverter.class)
    @DynamoDBAttribute(attributeName = "exercises")
    private List<Exercise> exercises;

    @DynamoDBAttribute(attributeName = "sessionDuration")
    private String sessionDuration;

    // Constructor with DynamoDB annotations
    public WorkoutSession() {
    }

    public WorkoutSession (String sessionId, Day day, List<Exercise> exercises){}
}
// TODO: Validate the data integrity and consistency between WorkoutPlan and WorkoutSession,
//  especially when updates or deletions occur.