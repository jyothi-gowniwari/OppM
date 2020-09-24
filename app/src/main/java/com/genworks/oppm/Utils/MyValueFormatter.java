package com.genworks.oppm.Utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class MyValueFormatter extends ValueFormatter
{

    private final DecimalFormat mFormat;
    private String suffix;
    private int mFormat1;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("#");

    }

    @Override
    public String getFormattedValue(float value) {
        return "" + ((int) value);
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (axis instanceof XAxis) {
            return mFormat.format(value);
        } else if (value > 0) {
            return mFormat.format(value);
        } else {
            return mFormat.format(value);
        }
    }
}
