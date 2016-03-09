package apeha.allinone.fitting;

import apeha.allinone.common.Category;
import apeha.allinone.db.DbHandler;
import apeha.allinone.item.Item;
import apeha.allinone.item.ModItem;
import apeha.allinone.item.Property;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayerSet {

    protected Map<Property, Integer> stats = Maps.newTreeMap();
    ModItem helmet = null;
    ModItem amulet = null;
    ModItem armour = null;
    ModItem leftHand = null;
    ModItem rightHand = null;
    ModItem belt = null;
    ModItem boots = null;
    ModItem gloves = null;
    ModItem vambrace = null;
    ModItem ring1 = null;
    ModItem ring2 = null;
    ModItem ring3 = null;
    ModItem ring4 = null;
    ModItem ring5 = null;
    ModItem ring6 = null;
    ModItem ring7 = null;
    ModItem ring8 = null;
    private BigDecimal price = new BigDecimal("0.00");

    public PlayerSet() {
        for (Property property : Property.ALL_PARAMETERS)
            stats.put(property, 0);
    }

    private void calc(ModItem item) {
        if (item != null) {
            Map<Property, String> propertiesAndValues = item
                    .getPropertiesAndValues();

            for (Property property : propertiesAndValues.keySet()) {
                if (stats.containsKey(property)) {
                    String formattedProp = Property.formatProperty(property,
                            propertiesAndValues.get(property));
                    int val = stats.get(property);
                    val = val + Property.getInteger(property, formattedProp);
                    stats.put(property, val);
                } else if (property.equals(Property.PRICE_IN_SHOP)) {
                    price = price.add(new BigDecimal(propertiesAndValues
                            .get(Property.PRICE_IN_SHOP)));
                }
            }
        }
    }

    private void resetStats() {
        for (Property property : stats.keySet())
            stats.put(property, 0);
        price = new BigDecimal("0.00");
    }

    private void recalculate() {
        resetStats();

        calc(helmet);
        calc(amulet);
        calc(armour);

        calc(leftHand);
        calc(rightHand);

        calc(belt);
        calc(boots);
        calc(gloves);
        calc(vambrace);

        calc(ring1);
        calc(ring2);
        calc(ring3);
        calc(ring4);
        calc(ring5);
        calc(ring6);
        calc(ring7);
        calc(ring8);
    }

    public void putOn(Category cat, ModItem item) {
        if (cat.equals(Category.AMULETS)) {
            amulet = item;
        } else if (cat.equals(Category.ARMOURS)) {
            armour = item;
        } else if (cat.equals(Category.BELTS)) {
            belt = item;
        } else if (cat.equals(Category.BOOTS)) {
            boots = item;
        } else if (cat.equals(Category.GLOVES)) {
            gloves = item;
        } else if (cat.equals(Category.HELMETS)) {
            helmet = item;
        } else if (cat.equals(Category.RINGS)) {
            if (ring1 == null)
                ring1 = item;
            else if (ring2 == null)
                ring2 = item;
            else if (ring3 == null)
                ring3 = item;
            else if (ring4 == null)
                ring4 = item;
            else if (ring5 == null)
                ring5 = item;
            else if (ring6 == null)
                ring6 = item;
            else if (ring7 == null)
                ring7 = item;
            else if (ring8 == null)
                ring8 = item;
        } else if (cat.equals(Category.VAMBRACES)) {
            vambrace = item;
        } else {
            if (leftHand == null) {
                leftHand = item;

            } else if (rightHand == null
                    && !item.getPropertiesAndValues().containsKey(
                    Property.TWO_HANDED)) {
                rightHand = item;
            }
        }
        recalculate();
    }

    public void putOn(ModItem item) {
        Map<Category, List<Item>> categoryAndItems = DbHandler
                .getCategoryAndItems();

        for (Category category : categoryAndItems.keySet())
            for (Item dbitem : categoryAndItems.get(category))
                if (dbitem.getName().equals(item.getName())) {
                    putOn(category, item);
                    break;
                }
    }

    private String formatName(ModItem item, int count) {
        String str = "";

        if (count == 1)
            str = item.getName() + "\n";
        else
            str = String.valueOf(count) + "x " + item.getName() + "\n";

        return str;
    }

    private void safelyAddToList(List<ModItem> list, ModItem item) {
        if (item != null)
            list.add(item);
    }

    private List<ModItem> getItems() {
        List<ModItem> items = Lists.newLinkedList();
        safelyAddToList(items, helmet);
        safelyAddToList(items, amulet);
        safelyAddToList(items, armour);

        safelyAddToList(items, leftHand);
        safelyAddToList(items, rightHand);

        safelyAddToList(items, belt);
        safelyAddToList(items, boots);
        safelyAddToList(items, gloves);
        safelyAddToList(items, vambrace);

        List<ModItem> rings = Lists.newLinkedList();
        safelyAddToList(rings, ring1);
        safelyAddToList(rings, ring2);
        safelyAddToList(rings, ring3);
        safelyAddToList(rings, ring4);
        safelyAddToList(rings, ring5);
        safelyAddToList(rings, ring6);
        safelyAddToList(rings, ring7);
        safelyAddToList(rings, ring8);
        Collections.sort(rings);

        items.addAll(rings);
        return items;
    }

    public String getItemNames() {
        String text = "";
        final List<ModItem> items = getItems();

        for (ModItem item : items) {
            if (!text.contains(item.getName()))
                text = text
                        + formatName(item, Collections.frequency(items, item));
        }

        return text;
    }

    public String getStatsAsText() {
        String text = "";
        Set<Property> keySet = stats.keySet();
        for (Property key : keySet)
            text = text + Property.formatPropertyByValue(key, stats.get(key))
                    + "\n";
        return text;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getSetPrice() {
        return Property
                .formatProperty(Property.PRICE_IN_SHOP, price.toString());
    }

    @Override
    public String toString() {
        return getSetPrice() + "\n\n" + getStatsAsText() + "\n"
                + getItemNames();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amulet == null) ? 0 : amulet.hashCode());
        result = prime * result + ((armour == null) ? 0 : armour.hashCode());
        result = prime * result + ((belt == null) ? 0 : belt.hashCode());
        result = prime * result + ((boots == null) ? 0 : boots.hashCode());
        result = prime * result + ((gloves == null) ? 0 : gloves.hashCode());
        result = prime * result + ((helmet == null) ? 0 : helmet.hashCode());
        result = prime * result
                + ((leftHand == null) ? 0 : leftHand.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result
                + ((rightHand == null) ? 0 : rightHand.hashCode());
        result = prime * result + ((ring1 == null) ? 0 : ring1.hashCode());
        result = prime * result + ((ring2 == null) ? 0 : ring2.hashCode());
        result = prime * result + ((ring3 == null) ? 0 : ring3.hashCode());
        result = prime * result + ((ring4 == null) ? 0 : ring4.hashCode());
        result = prime * result + ((ring5 == null) ? 0 : ring5.hashCode());
        result = prime * result + ((ring6 == null) ? 0 : ring6.hashCode());
        result = prime * result + ((ring7 == null) ? 0 : ring7.hashCode());
        result = prime * result + ((ring8 == null) ? 0 : ring8.hashCode());
        result = prime * result + ((stats == null) ? 0 : stats.hashCode());
        result = prime * result
                + ((vambrace == null) ? 0 : vambrace.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerSet other = (PlayerSet) obj;
        if (amulet == null) {
            if (other.amulet != null)
                return false;
        } else if (!amulet.equals(other.amulet))
            return false;
        if (armour == null) {
            if (other.armour != null)
                return false;
        } else if (!armour.equals(other.armour))
            return false;
        if (belt == null) {
            if (other.belt != null)
                return false;
        } else if (!belt.equals(other.belt))
            return false;
        if (boots == null) {
            if (other.boots != null)
                return false;
        } else if (!boots.equals(other.boots))
            return false;
        if (gloves == null) {
            if (other.gloves != null)
                return false;
        } else if (!gloves.equals(other.gloves))
            return false;
        if (helmet == null) {
            if (other.helmet != null)
                return false;
        } else if (!helmet.equals(other.helmet))
            return false;
        if (leftHand == null) {
            if (other.leftHand != null)
                return false;
        } else if (!leftHand.equals(other.leftHand))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (rightHand == null) {
            if (other.rightHand != null)
                return false;
        } else if (!rightHand.equals(other.rightHand))
            return false;
        if (ring1 == null) {
            if (other.ring1 != null)
                return false;
        } else if (!ring1.equals(other.ring1))
            return false;
        if (ring2 == null) {
            if (other.ring2 != null)
                return false;
        } else if (!ring2.equals(other.ring2))
            return false;
        if (ring3 == null) {
            if (other.ring3 != null)
                return false;
        } else if (!ring3.equals(other.ring3))
            return false;
        if (ring4 == null) {
            if (other.ring4 != null)
                return false;
        } else if (!ring4.equals(other.ring4))
            return false;
        if (ring5 == null) {
            if (other.ring5 != null)
                return false;
        } else if (!ring5.equals(other.ring5))
            return false;
        if (ring6 == null) {
            if (other.ring6 != null)
                return false;
        } else if (!ring6.equals(other.ring6))
            return false;
        if (ring7 == null) {
            if (other.ring7 != null)
                return false;
        } else if (!ring7.equals(other.ring7))
            return false;
        if (ring8 == null) {
            if (other.ring8 != null)
                return false;
        } else if (!ring8.equals(other.ring8))
            return false;
        if (stats == null) {
            if (other.stats != null)
                return false;
        } else if (!stats.equals(other.stats))
            return false;
        if (vambrace == null) {
            if (other.vambrace != null)
                return false;
        } else if (!vambrace.equals(other.vambrace))
            return false;
        return true;
    }

}
