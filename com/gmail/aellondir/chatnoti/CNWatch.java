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
    private static ArrayList<NotificationTypes> notQueue = new ArrayList<>();
    private static String unBreak = "";

    //never to be instantiated
    private CNWatch() {
    }

    protected static void chatWatch(String check) {

        if (check == null || check.length() == 0 || CNOptions.getNamesAndWords().isEmpty()) {
            return;
        }

        if (firstRun && check.length() >= 0 && check.charAt(0) == 0x3c) {
            unBreak = ">";
        } else if (firstRun && check.length() >= 0 && !(check.charAt(0) == 0x3c)) {
            unBreak = ":";
        } else {
            unBreak = ":";
        }

        String[] checkArr = check.split(unBreak);

        if (CNOptions.adminM && CNOptions.getNamesAndWords().containsValue(0)) {
            if (checkArr.length != 2) {
                for (String str : checkArr) {
                    if (str.contains(" ")) {
                        String[] strS = str.split(" ");

                        for (String strC : strS) {
                            if (CNOptions.getNamesAndWords().get(strC) != null && CNOptions.getNamesAndWords().get(strC) == 0) {
                                notQueue.add(NotificationTypes.ADMIN);
                                return;
                            }
                        }
                    } else {
                        if (CNOptions.getNamesAndWords().get(str) != null && CNOptions.getNamesAndWords().get(str) == 0) {
                            notQueue.add(NotificationTypes.ADMIN);
                            return;
                        }
                    }
                }
            }
        }

        if (CNOptions.watchUN && CNOptions.namesAccum > 1) {
            for (String str : checkArr[0].split(" ")) {
                if (!Minecraft.getMinecraft().thePlayer.username.equals(str)
                        && CNOptions.getNamesAndWords().get(str) != null
                        && CNOptions.getNamesAndWords().get(str) == 1) {
                    notQueue.add(NotificationTypes.W_UN);
                    return;
                }
            }
        }

        for (String str : checkArr) {
            String[] strArr;

            if (str.contains(" ")) {
                strArr = str.split(" ");
            } else {
                strArr = null;
            }

            if (strArr != null && strArr.length > 1) {
                for (String strC : strArr) {
                    if (CNOptions.getNamesAndWords().get(strC) != null
                            && CNOptions.getNamesAndWords().get(strC) == 1) {
                        notQueue.add(NotificationTypes.GENERAL);
                        return;
                    }
                }
            } else {
                if (CNOptions.getNamesAndWords().get(str) != null
                        && CNOptions.getNamesAndWords().get(str) == 1) {
                    notQueue.add(NotificationTypes.GENERAL);
                    return;
                }
            }
        }
    }

    protected static NotificationTypes notQueueCheck() {
        return !notQueue.isEmpty() ? notQueue.remove(0) : NotificationTypes.NIL;
    }
}
