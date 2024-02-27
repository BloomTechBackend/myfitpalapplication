package com.decadev.config;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.decadev.converters.ExerciseListConverter;
import com.decadev.converters.FitnessGoalConverter;
import com.decadev.converters.FitnessLevelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbConfiguration {

    @Value("${aws.dynamodb.endpoint}")
    private String dynamoDbEndpoint;
    @Value("${aws.dynamodb.accessKey}")
    private String awsAccessKey;
    @Value("${aws.dynamodb.secretKey}")
    private String awsSecretKey;

    @Autowired
    private FitnessGoalConverter fitnessGoalConverter;

    @Autowired
    private FitnessLevelConverter fitnessLevelConverter;

    @Autowired
    private ExerciseListConverter  exerciseListConverter;

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDb());
    }


    // Method to create AmazonDynamoDB client
    private AmazonDynamoDB amazonDynamoDb() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dynamoDbEndpoint, "us-west-2"))
                .withCredentials(amazonDynamoDBCredentials())
                .build();
    }

    // Method to provide AWS credentials for DynamoDB client
    private AWSCredentialsProvider amazonDynamoDBCredentials() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey));
    }
}
