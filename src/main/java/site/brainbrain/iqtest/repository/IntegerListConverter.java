package site.brainbrain.iqtest.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;
import site.brainbrain.iqtest.exception.JsonConvertException;

@Converter
public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(final List<Integer> attributes) {
        try {
            return objectMapper.writeValueAsString(attributes);
        } catch (final JsonProcessingException e) {
            throw new JsonConvertException(e.getMessage());
        }
    }

    @Override
    public List<Integer> convertToEntityAttribute(final String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<Integer>>() {});
        } catch (final JsonProcessingException e) {
            throw new JsonConvertException(e.getMessage());
        }
    }
}
