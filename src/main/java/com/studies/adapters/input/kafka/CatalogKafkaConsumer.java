package com.studies.adapters.input.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.domain.dto.BookDTO;
import com.studies.domain.ports.input.AddBookUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CatalogKafkaConsumer {

    private final AddBookUseCase addBookUseCase;

    @Autowired
    public CatalogKafkaConsumer(AddBookUseCase addBookUseCase) {
        this.addBookUseCase = addBookUseCase;
    }

  //  @KafkaListener(topics = "book-topic", groupId = "catalog-group")
    public void consume(String message) throws JsonProcessingException {
        // Assuming the message is a JSON string representing a Book
        // You would typically use a library like Jackson to convert it to a Book object
        // For simplicity, let's assume the message is directly convertible
        ObjectMapper mapper = new ObjectMapper();
        BookDTO bookDTO = mapper.readValue(message, BookDTO.class);
        addBookUseCase.addBook(bookDTO);
        System.out.println("Book added via Kafka");

    }

}
