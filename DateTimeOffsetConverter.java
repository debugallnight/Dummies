import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter(autoApply = true)
public class DateTimeOffsetConverter implements AttributeConverter<OffsetDateTime, String> {
    
    private static DateTimeFormatter MAIN_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnnnn xxx");
    private static DateTimeFormatter SECONDARY_FROM_DB_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnn xxx");
    
    @Override
    public String convertToDatabaseColumn(OffsetDateTime attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.format(MAIN_FORMATTER);
    }
    
    @Override
    public OffsetDateTime convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        try {
            return OffsetDateTime.parse(dbData, MAIN_FORMATTER);
        } catch (final DateTimeParseException e) {
            // Try backup formatter (has 7 digits for nanoseconds)
            return OffsetDateTime.parse(dbData, SECONDARY_FROM_DB_FORMATTER);
        }
    }
}
