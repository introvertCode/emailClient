package com.barosanu.view;

public enum FontSize {
    SMALL,
    MEDIUM,
    BIG;

    public static String getCssPath(FontSize fontSize){
        switch(fontSize) {
            case SMALL:
                return "/style/fontSmall.css";
            case MEDIUM:
                return "/style/fontMedium.css";
            case BIG:
                return "/style/fontBig.css";
            default:
                return null;
        }
    }
}
