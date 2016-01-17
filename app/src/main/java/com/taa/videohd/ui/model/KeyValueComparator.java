package com.taa.videohd.ui.model;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by trung on 12/11/2015.
 */
public class KeyValueComparator implements Comparator<KeyValue>
{
    private Comparator comparator;

    public KeyValueComparator()
    {
        comparator = Collator.getInstance();
    }

    public int compare(KeyValue o1, KeyValue o2)
    {
        return comparator.compare(o1.getValue(), o2.getValue());
    }
}
