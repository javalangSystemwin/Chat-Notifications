package com.gmail.aellondir.chatnoti;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;

/**
 *
 * @author James Hull
 * @serial McMod JPGH.0001 class 2
 * @version 0.01
 */
public class CNWatch {

    private static boolean firstRun = true;
    private static ArrayList<NotificationTypes> notQueue = new ArrayList<NotificationTypes>();
    private static String unBreak = "";

    //never to be instantiated
    private CNWatch() {
    }

    protected static void chatWatch(String check, CNMain cNM) {
        if (check == null || check.length() == 0 || cNM.getOptions().getNamesAndWords().isEmpty()) {
            return;
        }

        if (firstRun && check.length() >= 0 && check.charAt(0) == 0x3c) {
            unBreak = ">";
            firstRun = false;
        } else if (firstRun && check.length() >= 0 && !(check.charAt(0) == 0x3c)) {
            unBreak = ":";
            firstRun = false;
        } else {
            unBreak = ":";
            firstRun = false;
        }

        String[] checkArr = check.split(unBreak);

        if (CNOptions.enabled && CNOptions.adminM && adminCheck(checkArr, cNM)) {
            notQueue.add(NotificationTypes.ADMIN);
        } else if (CNOptions.enabled && CNOptions.watchUN && watchUserNames(checkArr[1], cNM)) {
            notQueue.add(NotificationTypes.W_UN);
        } else if (CNOptions.enabled && watchNames(checkArr, cNM)) {
            notQueue.add(NotificationTypes.GENERAL);
        }
    }

    private static boolean adminCheck(String[] check, CNMain cNM) {
        for (String str: check) {
            for (String str1: str.split(" ")) {
                Integer compInt = cNM.getOptions().getNamesAndWords().get(str1);

                if (compInt != null && compInt == 0) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean watchUserNames(String check, CNMain cNM) {
        for (String str : check.split(" ")) {
            if (!Minecraft.getMinecraft().thePlayer.username.equals(str)
                    && cNM.getOptions().getNamesAndWords().get(str) != null
                    && cNM.getOptions().getNamesAndWords().get(str) == 1) {
                return true;
            }
        }

        return false;
    }

    private static boolean watchNames(String check[], CNMain cNM) {
        for (String str: check) {
            for (String str1: str.split(" ")) {
                Integer compInt = cNM.getOptions().getNamesAndWords().get(str1);

                if (compInt != null && compInt == 1) {
                    return true;
                }
            }
        }

        return false;
    }

    protected static NotificationTypes notQueueCheck() {
        return !notQueue.isEmpty() ? notQueue.remove(0) : NotificationTypes.NIL;
    }
}
