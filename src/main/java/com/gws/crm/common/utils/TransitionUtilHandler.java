package com.gws.crm.common.utils;


import com.gws.crm.common.constant.Language;
import com.gws.crm.common.properites.AppInfo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;


@Component
@Data
public class TransitionUtilHandler {

    @Autowired
    AppInfo appInfo;

    public Language validateLanguage(String lang) throws Exception {
        if (lang != null && lang != "") {
            return getLanguage(lang);
        } else if (appInfo.getDefaultLanguage() != null) {
            return getLanguage(appInfo.getDefaultLanguage());
        } else {
            throw new Exception("Invalid Supplied Language");
        }

    }

    public Language getLanguage(String lang) {
        if (lang.equalsIgnoreCase(Language.AR.getValue())) {
            return Language.AR;
        } else {
            return Language.EN;
        }
    }

    public Date getDateWith(Date date, int milliSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, milliSecond);
        return calendar.getTime();
    }


}
