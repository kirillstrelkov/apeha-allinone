package apeha.allinone.item;

import apeha.allinone.item.stone.Modification;

import java.util.List;
import java.util.Map;

public class ModItem extends Item {
    private Modification mod1 = null;
    private Modification mod2 = null;

    public ModItem(Item item) {
        super();
        this.setName(item.getName());
        this.setImageSrc(item.getImageSrc());
        Map<Property, String> propertiesAndValues = item
                .getPropertiesAndValues();
        this.setPropertiesAndValues(propertiesAndValues);
        this.setProperties(item.getProperties());
    }

    public void setMods(List<Modification> mods) {
        if (mods.size() > 0) {
            this.mod1 = mods.get(0);
            if (mods.size() > 1)
                this.mod2 = mods.get(1);
        }

    }

    public Modification getMod1() {
        return mod1;
    }

    public Modification getMod2() {
        return mod2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ModItem other = (ModItem) obj;
        if (mod1 == null) {
            if (other.mod1 != null)
                return false;
        } else if (!mod1.equals(other.mod1))
            return false;
        if (mod2 == null) {
            if (other.mod2 != null)
                return false;
        } else if (!mod2.equals(other.mod2))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((mod1 == null) ? 0 : mod1.hashCode());
        result = prime * result + ((mod2 == null) ? 0 : mod2.hashCode());
        return result;
    }

}
