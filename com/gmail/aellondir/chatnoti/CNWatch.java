package com.gmail.aellondir.chatnoti;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;

/**
 *
 * @author James Hull
 * @serial McMod JPGH.0001 class 2 v1
 * @version 0.01
 */
public class CNWatch {

    private static boolean firstCheck = true;
    private static ArrayList<NotificationTypes> notQueue = new ArrayList<NotificationTypes>();
    private static String unBreak = "";

    //never to be instantiated
    private CNWatch() {
    }

    /**
     *Watches chat for the occerence of any String one might be interested in.
     * Strings are Tokenized first then run through the various methods of determining whether a notification is
     * necessary.
     *
     * @param check the string to be looked over.
     * @param cNM the running instance of CNMain, so as to get at the options, and from there the names stored within.
     */
    protected static void chatWatch(String check, CNMain cNM) {
        if (check == null || check.length() <= 0 || cNM.getOptions().getNamesAndWords().isEmpty()) {
            return;
        }

        if (firstCheck && check.length() >= 25 && check.charAt(0) == 0x3c) {
            unBreak = ">";
            firstCheck = false;
        } else if (firstCheck && check.length() >= 25 && !(check.charAt(0) == 0x3c)) {
            unBreak = ":";
            firstCheck = false;
        } else if (firstCheck) {
            unBreak = ":";
            firstCheck = false;
        }

        String[] checkArr = check.split(unBreak);

        if (CNOptions.enabled && CNOptions.adminM && adminCheck(checkArr, cNM)) {
            notQueue.add(NotificationTypes.ADMIN);
        } else if (CNOptions.enabled && CNOptions.watchUN && watchUserNames(checkArr[0], cNM)) {
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

    /**
     *Checks that the notification queue is NOT empty, if true then removes and returns the first notification to be
     * placed in the list, else returns NotificationTypes.NIL.
     *
     * @return the NotificationType.
     */
    protected static NotificationTypes notQueueCheck() {
        return !notQueue.isEmpty() ? notQueue.remove(0) : NotificationTypes.NIL;
    }
}
