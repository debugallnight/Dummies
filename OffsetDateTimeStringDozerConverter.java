import java.time.OffsetDateTime;

import org.dozer.DozerConverter;

public class OffsetDateTimeStringDozerConverter extends DozerConverter<OffsetDateTime, String> {
    
    public OffsetDateTimeStringDozerConverter() {
        super(OffsetDateTime.class, String.class);
    }
    
    @Override
    public String convertTo(OffsetDateTime source, String destination) {
        
        if (source == null) {
            return null;
        }
        return source.toString();
    }
    
    @Override
    public OffsetDateTime convertFrom(String dateValue, OffsetDateTime destination) {
        if (dateValue == null) {
            return null;
        }
        
        return DateUtil.convertStringToOffsetDateTime(dateValue);
    }
}
