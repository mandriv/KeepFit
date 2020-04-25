// ICTService.aidl
package com.mandriv.ctnotifications;

import com.mandriv.ctnotifications.data.Trigger;

interface ICTService {
    List<Trigger> getCurrentTriggers();

    void addTrigger(in Trigger t);

    void removeTrigger(int triggerId);

    void clearTriggers(String appPackage);

    void updateCustomData(String appPackage, String dataName, int newValue);
}
