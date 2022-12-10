package com.schedulemaster.app.misc;

import javax.swing.*;

/**
 * SpinnerModel allow number due to given range.
 *
 * @author lalaalal
 */
public class RangedSpinnerNumberModel extends SpinnerNumberModel {
    private final int min;
    private final int max;

    public RangedSpinnerNumberModel() {
        min = Integer.MIN_VALUE;
        max = Integer.MAX_VALUE;
    }

    public RangedSpinnerNumberModel(int min) {
        this.min = min;
        max = Integer.MAX_VALUE;
    }

    public RangedSpinnerNumberModel(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void setValue(Object value) {
        if (!(value instanceof Integer integer))
            return;
        if (integer < min)
            super.setValue(min);
        else if (integer > max)
            super.setValue(max);
        else
            super.setValue(integer);
    }
}
