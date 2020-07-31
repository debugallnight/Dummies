
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DateUtil {

     public final static String ALLOWED_DEFINED_DATE_FORMATS = "MM/dd/yyyy, MM/dd/yyyy HH:mm:ss,  MM/dd/yyyy hh:mm:ss aa,  MMM/dd/yyyy HH:mm:ss,  MMM/dd/yyyy hh:mm:ss aa, MM-dd-yyyy HH:mm:ss, MM-dd-yyyy hh:mm:ss aa, MMM-dd-yyyy HH:mm:ss, MMM-dd-yyyy hh:mm:ss aa, EEE MMM dd HH:mm:ss Z yyyy";


     private static final List<String> defined_date_formats = Arrays
            .asList(ALLOWED_DEFINED_DATE_FORMATS.split("\\s*,\\s*"));
  
  public static OffsetDateTime convertStringToOffsetDateTime(String dateValue) {
        
        if (dateValue == null) {
            return null;
        }
        
        final List<String> defined_date_formats = Arrays
                .asList(DateConstants.ALLOWED_DEFINED_DATE_FORMATS.split("\\s*,\\s*"));
        boolean formatterFound = false;
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.setLenient(false);
        for (final String defined_date_format : defined_date_formats) {
            try {
                simpleDateFormat.applyPattern(defined_date_format);
                Date formattedDate = simpleDateFormat.parse(dateValue);
                if (defined_date_format.indexOf('Z') == 0) {
                    if (dateValue.length() != defined_date_format.length()) {
                        continue;
                    }
                }
                formatterFound = true;
                break;
            } catch (final ParseException e) {
                formatterFound = false;
            }
        }
        
        if (formatterFound) {
            Date incomingDate = null;
            Instant dateInstant = null;
            try {
                incomingDate = simpleDateFormat.parse(dateValue);
                dateInstant = incomingDate.toInstant();
                return OffsetDateTime.ofInstant(dateInstant, ZoneId.systemDefault());
            } catch (final ParseException e) {
               
            }
        }
        
        return null;
    }
