package skarbnikApp;

import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateService {

   public static String dateConverter(Date date) {
       return new DateFormatter(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).getFormat().format(date);
   }
}
