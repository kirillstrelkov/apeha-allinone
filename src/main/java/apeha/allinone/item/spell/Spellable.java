package apeha.allinone.item.spell;

import org.joda.time.DateTime;

public interface Spellable {
    void spell(int days);

    DateTime getSpellStartCalendar();

    boolean isSpellable();

    boolean isLessThen24hAlive();

    boolean isAlive();

    String getTimeLeft();
}
