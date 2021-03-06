package io.github.pactstart.simple.web.framework.enums;

public enum RequestSource {

    Android(1),

    iOS(2);

    private int value;

    RequestSource(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * 不抛异常，没找到返回null
     *
     * @param name
     * @return
     */
    public static RequestSource valueOfName(String name) {
        if (name == null || name.trim().length() == 0) {
            return null;
        }
        RequestSource result = null;
        for (RequestSource item : values()) {
            if (item.name().equals(name)) {
                result = item;
                break;
            }
        }
        return result;
    }
}
