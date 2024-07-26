package ploton.SpringMVC_ProductOrderCustomer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ploton.SpringMVC_ProductOrderCustomer.exception.JsonEntityException;

@Service
public class EntityUtils {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }


    public static <T> String serialize(T entity) {
        try {
            return mapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new JsonEntityException("Serialization Error - " + e.toString());
        }
    }

    public static <T> T deSerialize(String jsonEntity, Class<T> clazz) {
        try {
            return mapper.readValue(jsonEntity, clazz);
        } catch (JsonProcessingException e) {
            throw new JsonEntityException("Deserialization Error - " + e.toString());
        }
    }
}
