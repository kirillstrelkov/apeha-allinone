package apeha.allinone.item.spell;

import apeha.allinone.common.Utils;
import apeha.allinone.item.Item;
import apeha.allinone.item.Property;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.util.Map;

public class SpellableItem extends Item implements Spellable {
    private static int spellableDays = 5;

    private DateTime date = null;
    private String comment = null;

    public SpellableItem(Item item) {
        super();
        this.setName(item.getName());
        this.setImageSrc(item.getImageSrc());

        Map<Property, String> propertiesAndValues = item
                .getPropertiesAndValues();

        if (propertiesAndValues.containsKey(Property.SPELLED)) {
            String value = propertiesAndValues.get(Property.SPELLED);
            this.setDate(Utils.getCalendarFrom(value));
            propertiesAndValues.remove(Property.SPELLED);
        }

        this.setPropertiesAndValues(propertiesAndValues);
        this.setProperties(item.getProperties());
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getProperties() {
        return super.getProperties() + "\n"
                + Property.formatProperty(Property.SPELLED, this.getTimeLeft());
    }

    @Override
    public void spell(int days) {
        setDate(DateTime.now().plusDays(days));
    }

    @Override
    public DateTime getSpellStartCalendar() {
        return getDate().minusDays(spellableDays);
    }

    @Override
    public boolean isSpellable() {
        int minutes = Minutes.minutesBetween(DateTime.now(), getDate())
                .getMinutes();

        return 0 < minutes && minutes < spellableDays * 24 * 60;
    }

    @Override
    public boolean isLessThen24hAlive() {
        int minutes = Minutes.minutesBetween(DateTime.now(), getDate())
                .getMinutes();

        return 0 < minutes && minutes < 24 * 60;
    }

    @Override
    public boolean isAlive() {
        int minutes = Minutes.minutesBetween(DateTime.now(), getDate())
                .getMinutes();

        return 0 < minutes;
    }

    @Override
    public String getTimeLeft() {
        DateTime thisDate = getDate();
        DateTime now = DateTime.now();

        int days = Days.daysBetween(now, thisDate).getDays();
        int hours = Hours.hoursBetween(now, thisDate).getHours() % 24;
        int mins = Minutes.minutesBetween(now, thisDate).getMinutes() % 60;

        return String.format("%dд %dч %dмин", days, hours, mins);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        SpellableItem other = (SpellableItem) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (comment == null) {
            if (other.comment != null)
                return false;
        } else if (!comment.equals(other.comment))
            return false;
        return true;
    }

}
