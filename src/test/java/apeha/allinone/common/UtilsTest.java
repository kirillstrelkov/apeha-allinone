package apeha.allinone.common;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void getIntegerTest() {
        String durab = "199/200";
        assertEquals(199, Utils.getInteger("^\\d+/", durab));
        assertEquals(200, Utils.getInteger("/\\d+$", durab));

        String text = "2389.98";
        assertEquals(2389, Utils.getInteger(".*", text));
        assertEquals(98, Utils.getInteger("\\.\\d+", text));

        String level = "11-12";
        assertEquals(11, Utils.getInteger("\\d+-", level));
        assertEquals(-12, Utils.getInteger("-\\d+", level));
    }

    @Test
    public void getCalendarFromTest() {
        String text = "5д 11ч 5мин";

        assertEquals(
                Utils.dateFormat.print(DateTime.now().plusDays(5).plusHours(11)
                        .plusMinutes(5)),
                Utils.dateFormat.print(Utils.getCalendarFrom(text)));
    }
}
