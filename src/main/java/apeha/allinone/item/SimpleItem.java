package apeha.allinone.item;

public class SimpleItem implements Comparable<Object> {
    protected String name;
    private String properties;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((properties == null) ? 0 : properties.hashCode());
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
        SimpleItem other = (SimpleItem) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (properties == null) {
            if (other.properties != null)
                return false;
        } else if (!properties.equals(other.properties))
            return false;
        return true;
    }

    @Override
    public int compareTo(Object o) {
        SimpleItem si = (SimpleItem) o;
        if (this.getName().equals(si.getName())
                && this.getProperties().equals(si.getProperties()))
            return 0;
        else
            return this.getName().compareTo(si.getName());
    }

    @Override
    public String toString() {
        return this.getName() + "\n" + this.getProperties();
    }
}
