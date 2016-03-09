package apeha.allinone.fitting;

import apeha.allinone.item.Property;
import com.google.common.collect.Lists;

import java.util.List;

public class SearchedSet extends PlayerSet implements Comparable<SearchedSet> {
    int score = 0;

    public void updateScore(List<String> params) {
        score = 0;
        List<Property> properties = Lists.newLinkedList();

        for (String param : params)
            properties.add(Property.getPropertyFrom(param));

        for (Property property : stats.keySet()) {
            if (properties.contains(property)) {
                int value = stats.get(property);
                String text = Property.formatPropertyByValue(property, value);

                score = score + Property.getScoreFrom(text);
            }
        }

        final Property damage = Property.DAMAGE;
        if (this.leftHand != null) {
            String value = this.leftHand.getPropertiesAndValues().get(damage);
            String text = Property.formatProperty(damage, value);
            score = score + Property.getScoreFrom(text);
        }
        if (this.rightHand != null) {
            String value = this.rightHand.getPropertiesAndValues().get(damage);
            String text = Property.formatProperty(damage, value);
            score = score + Property.getScoreFrom(text);
        }
    }

    public int getScore() {
        return score;
    }

    public void updateScore() {
        score = 0;
        List<String> params = Lists.newLinkedList();

        for (Property param : Property.ALL_PARAMETERS)
            params.add(param.getName());

        updateScore(params);
    }

    @Override
    public String toString() {
        return "Количество нужных статов: " + score + "\n" + super.toString();
    }

    @Override
    public int compareTo(SearchedSet o) {
        int diff = new Integer(this.getScore()).compareTo(new Integer(o.getScore()));
        if (diff == 0) {
            diff = this.getPrice().compareTo(o.getPrice());
            if (diff == -1)
                return 1;
            else if (diff == 1)
                return -1;
            else
                return this.getItemNames().compareTo(o.getItemNames());
        } else
            return diff;
    }

}
