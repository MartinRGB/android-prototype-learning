package com.example.martinrgb.a49gles_version;

import java.util.Objects;

/**
 * Created by MartinRGB on 2017/4/18.
 */

public class Counter {
    private static int _upDown = 0;
    private static final Object _counterLock = new Object();

    public static void reset() {
        synchronized (_counterLock) {
            _upDown = 0;
        }
    }

    public static int getUpDownValue() {
        int value = 0;
        synchronized (_counterLock) {
            value = _upDown;
        }
        return value;
    }

    public static void getUpDownNextValue() {
        synchronized (_counterLock) {
            ++_upDown;
        }
    }

    public static void getUpDownPreviousValue() {
        synchronized (_counterLock) {
            --_upDown;
        }
    }
}
