package apeha.allinone.search;

import apeha.allinone.item.Item;
import apeha.allinone.item.Property;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemWithScore extends Item {
    private int goodStats = 0;
    private int badStats = 0;

    public ItemWithScore(Item item) {
        this.setName(item.getName());
        this.setPropertiesAndValues(item.getPropertiesAndValues());
        this.setImageSrc(item.getImageSrc());
        computeScore();
    }

    @Override
    public String toString() {
        return "Кол-во статов: " + this.getGoodStats() + "\n"
                + super.toString();
    }

    public int getGoodStats() {
        return goodStats;
    }

    public int getBadStats() {
        return badStats;
    }

    public void computeScore(Property... properties) {
        this.goodStats = 0;
        this.badStats = 0;
        final Property damage = Property.DAMAGE;
        Map<Property, String> propertiesAndValues = this
                .getPropertiesAndValues();

        List<Property> list = Lists.newArrayList(properties);
        List<Property> guardsEnemy = Property.GUARDS_OF_ENEMY;
        List<Property> guardAndMasteries = Property.GUARDS_AND_MASTERIES;
        List<Property> stats = Property.STATS;

        if (propertiesAndValues.containsKey(damage) && !list.contains(damage))
            list.add(damage);

        for (Property property : propertiesAndValues.keySet()) {
            String value = propertiesAndValues.get(property);
            int intVal = getScoreFromValue(property, value);
            if (guardAndMasteries.contains(property)
                    || guardsEnemy.contains(property)
                    || stats.contains(property) || property.equals(damage)) {
                if (list.contains(property))
                    this.goodStats = this.goodStats + intVal;
                else
                    this.badStats = this.badStats + intVal;
            }
        }
    }

    private int getScoreFromValue(Property property, String value) {
        Matcher matcher = Pattern.compile("-*\\d+").matcher(value);
        if (matcher.find()) {
            int val = Integer.parseInt(matcher.group());
            if (Property.STATS.contains(property))
                return val;
            else if (Property.GUARDS_OF_ENEMY.contains(property))
                return Math.abs(val) / 5;
            else if (Property.GUARDS_AND_MASTERIES.contains(property))
                return val / 5;
            else if (property.equals(Property.DAMAGE)) {
                if (value.contains("-")) {
                    String[] values = value.split("-");
                    val = Integer.parseInt(values[0])
                            + Integer.parseInt(values[1]);
                }
                return val / 4;
            } else
                return 0;
        }
        return 0;
    }

    public void computeScore() {
        computeScore(Property.values());
    }

    // @Override
    // public int compareTo(Object o) {
    // ItemWithScore iws = (ItemWithScore) o;
    // int diff = new Integer(this.goodStats - this.badStats).compareTo(new
    // Integer(iws.goodStats -iws.badStats));
    // if(diff == 0 ){
    // String thisVal =
    // this.getPropertiesAndValues().get(Property.PRICE_IN_SHOP);
    // String iwssVal =
    // iws.getPropertiesAndValues().get(Property.PRICE_IN_SHOP);
    // int priceDiff = new BigDecimal(thisVal).compareTo(new
    // BigDecimal(iwssVal));
    // if(diff == 0 )
    // return this.getName().compareTo(iws.getName());
    // else
    // return priceDiff;
    // }
    // else
    // return diff;
    // }

    @Override
    public int compareTo(Object o) {
        ItemWithScore iws = (ItemWithScore) o;
        return new Integer(this.goodStats)
                .compareTo(new Integer(iws.goodStats));
    }
}
