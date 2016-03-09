package apeha.allinone.search;

public enum SearchType {
    STRONG("Строгий"), OPTIMAL("Оптимальный"), WEAK("Мягкий"),;

    private String type;

    SearchType(String type) {
        this.type = type;
    }

    public static String[] getTypes() {
        SearchType[] values = SearchType.values();
        String[] types = new String[values.length];
        for (int i = 0; i < values.length; i++)
            types[i] = values[i].getType();
        return types;
    }

    public static SearchType getType(String type) {
        for (SearchType stype : SearchType.values())
            if (stype.getType().equals(type))
                return stype;
        return null;
    }

    public String getType() {
        return type;
    }
}
