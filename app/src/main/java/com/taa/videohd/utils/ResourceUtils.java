package com.taa.videohd.utils;

import android.content.Context;
import android.content.res.XmlResourceParser;
import com.taa.videohd.ui.model.KeyValue;
import com.taa.videohd.ui.model.KeyValueComparator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by trung on 12/11/2015.
 */
public class ResourceUtils
{
    public static List<KeyValue> getCountries()
    {
        String[] isoCountryCodes = Locale.getISOCountries();
        List<KeyValue> countries = new ArrayList<>();
        for (String countryCode : isoCountryCodes)
        {
            Locale locale = new Locale("", countryCode);
            String countryName = locale.getDisplayCountry();
            String regionCode = locale.getCountry();
            countries.add(new KeyValue(regionCode, countryName));
        }


//        Locale[] locales = Locale.getAvailableLocales();
//        for (Locale locale : locales)
//        {
//            String code = locale.getCountry();
//            String country = locale.getDisplayCountry();
//            if (code.trim().length() > 0 && !code.contains(code) && country.trim().length() > 0 && !countries.contains(country))
//            {
//                countries.add(new KeyValue(code, country));
//            }
//        }

        Collections.sort(countries, new KeyValueComparator());
        countries.add(0, new KeyValue("", "Any"));
        return countries;
    }

    public static List<KeyValue> getStringResource(Context context, int xml) throws XmlPullParserException, IOException
    {
        XmlResourceParser xmlResourceParser = context.getResources().getXml(xml);
        int eventType = xmlResourceParser.getEventType();
        List<KeyValue> vimeoCategories = new ArrayList<>();
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if (eventType == XmlPullParser.START_TAG)
            {
                if (xmlResourceParser.getName().equals("category"))
                {
                    KeyValue keyValue = new KeyValue(xmlResourceParser.getAttributeValue(0), xmlResourceParser.getAttributeValue(1));
                    vimeoCategories.add(keyValue);
                }
            }
            eventType = xmlResourceParser.next();
        }
        return vimeoCategories;
    }
}
