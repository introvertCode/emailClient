package com.barosanu.view;

public enum ColorTheme {
    LIGHT,
    DEFAULT,
    DARK;

    public static String getCssPath(ColorTheme colorTheme){
        switch(colorTheme) {
            case LIGHT:
                return "/style/themeLight.css";
            case DEFAULT:
                return "/style/themeDefault.css";
            case DARK:
                return "/style/themeDark.css";
            default:
                return null;
        }
    }
}
