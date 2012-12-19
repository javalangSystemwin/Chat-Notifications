package com.gmail.aellondir.chatnoti;

import java.io.IOException;
import net.minecraft.client.Minecraft;

/**
 *
 * @author James Hull
 * @serial McMod JPGH.0001 Class 1
 * @version 0.01N
 */
public class CNMain {

    private static final char sectionSym = (char) 0xa7;
    private CNChatLogger cNLog;
    private CNThread cNThread;

    public CNMain() {
        try {
            CNOptions.getOptions();
        } catch (OptionsFailedException e) {
            e.printStackTrace();
        }

        if (CNOptions.enabled && CNOptions.chatLog) {
            cNLog = new CNChatLogger();
        }

        cNThread = new CNThread();
    }

    public void watch(String msg) {
        tRun();

        CNWatch.chatWatch(msg);

        if (cNLog != null) {
            cNLog.logWrt(scrubServerJargon(msg));
        }
    }

    public boolean handleCommand(String comStr) {
        if (comStr == null || !isCommand(comStr)) {
            return false;
        }

        boolean optionsChanged = false, namesChanged = false, chatLogOn = CNOptions.chatLog, commandEntered = false;

        String[] strArr = comStr.substring(0).split(" ");

        if (strArr.length == 2) {
            switch (strArr[0]) {
                case "CNEnabled":
                    CNOptions.enabled = Boolean.parseBoolean(strArr[1]);
                    commandEntered = true;
                    optionsChanged = true;
                    break;
                case "CNWUN":
                    CNOptions.watchUN = Boolean.parseBoolean(strArr[1]);
                    commandEntered = true;
                    optionsChanged = true;
                    break;
                case "CNAdmin":
                    CNOptions.adminM = Boolean.parseBoolean(strArr[1]);
                    commandEntered = true;
                    optionsChanged = true;
                    break;
                case "CNChatLogger":
                    CNOptions.chatLog = Boolean.parseBoolean(strArr[1]);
                    commandEntered = true;
                    optionsChanged = true;
                    chatLogOn = CNOptions.chatLog;
                    break;
                case "CNVolume":
                    CNOptions.volume = Float.parseFloat(strArr[1]) / 100.0F;
                    commandEntered = true;
                    optionsChanged = true;
                    break;
                case "CNHelp":
                    //@todo call helpPrint(strArr[1]) the parameter being the page number
                    commandEntered = true;
                    break;
                case "CNAddName":
                    namesChanged = addName(strArr[1]);
                    commandEntered = true;
                    break;
                case "CNRmvName":
                    namesChanged = rmvName(strArr[1]);
                    commandEntered = true;
                    break;
                case "CNAddWrd":
                    namesChanged = addWord(strArr[1]);
                    commandEntered = true;
                    break;
                case "CNRmvWrd":
                    namesChanged = rmvWord(strArr[1]);
                    commandEntered = true;
                    break;
                default:
                    return false;
            }
        }

        if (optionsChanged && CNOptions.enabled && !cNThread.isAlive()) {
            tRun();
        }

        if (optionsChanged) {
            try {
                return CNOptions.writeOptions();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (namesChanged) {
            try {
                return CNOptions.writeNames();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return commandEntered;
    }

    private boolean isCommand(String comStr) {
        return comStr.charAt(0) == 0x2F ? true : false;
    }

    private void tRun() {
        if (cNThread != null && !cNThread.isAlive()) {
            cNThread.start();
        }
    }

    private boolean addName(String name) {
        if (CNOptions.getNamesAndWords().containsKey(name)) {
            return false;
        }

        CNOptions.getNamesAndWords().put(name, 0);

        return true;
    }

    private boolean rmvName(String name) {
        return CNOptions.getNamesAndWords().remove(name) != null ? true : false;
    }

    private boolean addWord(String word) {
        if (CNOptions.getNamesAndWords().containsKey(word)) {
            return false;
        }

        CNOptions.getNamesAndWords().put(word, 1);

        return true;
    }

    private boolean rmvWord(String word) {
        return CNOptions.getNamesAndWords().remove(word) != null ? true : false;
    }

    private boolean helpPrint(int i) {
        String[][] helpArr;

        return false;
    }

    private String scrubServerJargon(String par1Str) {
        String scrubbed = par1Str;

        int i = par1Str.indexOf(sectionSym);

        do {
            if (i == 0) {
                scrubbed = scrubbed.substring(2);
            } else if (i > 0 && i != -1) {
                scrubbed = scrubbed.substring(0, i) + scrubbed.substring(i + 2);
            }

            i = scrubbed.indexOf(sectionSym);
        } while (i != -1);

        return scrubbed;
    }

    public void closeMinecraft() {
        CNOptions.enabled = false;

        cNLog.closeLog();
    }
}
