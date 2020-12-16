package io.github.danilopiazza.spring.batch.xml;

import org.apache.maven.pom._4_0.Dependency;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Value("file:pom.xml")
    Resource inputFile;

    @Bean
    public Job job(JobBuilderFactory jobs, Step step) {
        return jobs.get("job")
                .start(step)
                .build();
    }

    @Bean
    public Step step(StepBuilderFactory steps, ItemReader<Dependency> reader, ItemProcessor<Dependency, String> processor, ItemWriter<String> writer) {
        return steps.get("step")
                .<Dependency, String>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public ItemReader<Dependency> reader(Jaxb2Marshaller marshaller) {
        return new StaxEventItemReaderBuilder<Dependency>().name("itemReader")
                .resource(inputFile)
                .addFragmentRootElements("dependency")
                .unmarshaller(marshaller)
                .build();
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("org.apache.maven.pom._4_0");
        marshaller.setMappedClass(Dependency.class);
        return marshaller;
    }

    @Bean
    public ItemProcessor<Dependency, String> processor() {
        return item -> "groupId: " + item.getGroupId() + ", artifactId: " + item.getArtifactId() + ", version: " + item.getVersion();
    }

    @Bean
    @SuppressWarnings("java:S106")
    public ItemWriter<String> writer() {
        return items -> items.forEach(System.out::println);
    }
}
