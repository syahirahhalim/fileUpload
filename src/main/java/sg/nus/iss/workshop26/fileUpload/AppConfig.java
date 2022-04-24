package sg.nus.iss.workshop26.fileUpload;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;
import java.util.logging.Level;

@Configuration
public class AppConfig {

    private Logger logger = Logger.getLogger(AppConfig.class.getName());

    @Value("${spaces.endpoint}")
    private String endpoint;
    
    @Value("${spaces.region}")
    private String region;


    @Bean
    AmazonS3 createS3Client(){

        // export S3_ACCESS_KEY=
        // export S3_SECRET_KEY=
        final String accessKey = System.getenv("S3_ACCESS_KEY");
        final String secretKey = System.getenv("S3_SECRET_KEY");
        logger.log(Level.INFO, "accessKey > " + accessKey);
        logger.log(Level.INFO, "secretKey > " + secretKey);
        
        // S3 credentials
        final BasicAWSCredentials basiccred = new BasicAWSCredentials(accessKey, secretKey);
        final AWSStaticCredentialsProvider credProv = new AWSStaticCredentialsProvider(basiccred);

        final EndpointConfiguration endpointConfig = new EndpointConfiguration(endpoint, region);

        return AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(endpointConfig)
            .withCredentials(credProv)
            .build();
    }
    
}
