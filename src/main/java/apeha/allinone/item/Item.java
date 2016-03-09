package apeha.allinone.item;

import com.google.common.collect.Maps;

import java.util.Map;

public class Item extends SimpleItem {
    private Map<Property, String> propertiesAndValues;
    private String imageSrc = null;

    public Item() {
        this.propertiesAndValues = Maps.newHashMap();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Map<Property, String> getPropertiesAndValues() {
        return this.propertiesAndValues;
    }

    public void setPropertiesAndValues(Map<Property, String> propertiesAndValues) {
        this.propertiesAndValues = propertiesAndValues;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    private String formatProperties() {
        Property[] correctPosition = new Property[]{Property.DURABILITY,
                Property.PRICE_IN_SHOP, Property.PRICE_IN_MARKET,
                Property.REQUIRED_RACE, Property.REQUIRED_LEVEL,
                Property.POWER, Property.AGILITY, Property.REACTION,
                Property.SPITE, Property.FOTUNE, Property.CONSTITUTION,
                Property.GUARD_OF_FORTUNE, Property.GUARD_OF_RESPONSE,
                Property.GUARD_OF_CRITICAL_STRIKE, Property.GUARD_OF_DODGE,
                Property.HEAD_ARMOR, Property.BODY_ARMOR, Property.LEGS_ARMOR,
                Property.LEFT_HAND_ARMOR, Property.RIGHT_HAND_ARMOR,
                Property.MASTERY_OF_FISTICUFFS, Property.MASTERY_OF_DEFENCE,
                Property.MASTERY_OF_WEAPON, Property.MARKSMANSHIP,
                Property.GUARD_OF_FORTUNE_ENEMY,
                Property.GUARD_OF_RESPONSE_ENEMY,
                Property.GUARD_OF_CRITICAL_STRIKE_ENEMY,
                Property.GUARD_OF_DODGE_ENEMY, Property.TWO_HANDED,
                Property.DAMAGE, Property.CHARGES, Property.POWER_IMPACT,
                Property.ACCURACY, Property.AREA_OF_EFFECT, Property.SPELLED};
        StringBuilder builder = new StringBuilder();

        for (Property property : correctPosition) {
            if (propertiesAndValues.containsKey(property)) {
                String value = propertiesAndValues.get(property);
                builder.append(Property.formatProperty(property, value) + "\n");
            }
        }

        return builder.toString().trim();
    }

    @Override
    public String getProperties() {
        return formatProperties();
    }
}
