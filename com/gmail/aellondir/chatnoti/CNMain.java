package com.gmail.aellondir.chatnoti;

import java.io.IOException;
import net.minecraft.client.Minecraft;

/**
 *
 * @author James Hull
 * @serial McMod JPGH.0001 Class 1 v1
 * @version 0.01
 */
public class CNMain {

    private static final char sectionSym = (char) 0xa7;
    private static CNOptions cNOpt;
    private static CNChatLogger cNLog;
    private static CNThread cNThread;
    private static boolean initSuc = false, printed = false;
    private static int numTicks = 0;
    private final static String[][] helpWelcomeArr = {
        {"Chat Notifications 0.01 -- Initialized successfully",
            "Thank you for using my mod",
            "type \"/cnhelp\" for the help pages."},
        {"§lHelp§r -- Page 1 of 3 -- Description",
            "Chat Notifications does 2 things,",
            "Watchs chat for occurences of words and names",
            "names you specify, and logs what occurs in chat",
            "You may choose whether any or all of these are enabled.",
            "Either by typing in one of the commands on the following pages,",
            "or by modifying the options/names files in you minecraft folder.",
            "\"/cnhelp 2\" for the next page."},
        {"§lHelp§r -- Page 2 of 3 -- Options Comands",
            "/cntoggle <true or false> - turns the base function of the mod on or off.",
            "/cnwun <true or false> - turns on or off watching for specified usernames other than your own.",
            "/cnadmin <true or false> - turns on or off checking chat for naughty words none are hardcoded you must define them.",
            "/cnchatlogger <true or false> - turns on or off keeping a .txt file of chat.",
            "/cnvolume <0 - 100> - sets the volum of notifications."},
        {"§lHelp§r -- Page 3 of 3 -- Names/Words Commands",
            "/cnlistnames - prints a list of names to the chat log.",
            "/cnlistwords - prints a list of words to the chat log.",
            "/cnaddname [nametoadd] - adds a name to the list of names.",
            "/cnrmvname [nametoremove] - removes a name from the list of names.",
            "/cnaddwrd [wordtoadd] - adds a name to the list of words.",
            "/cnrmvwrd [wordtoremove] - removes a word from the list of words."}
    };

    public CNMain() {

        cNOpt = new CNOptions();

        if (CNOptions.chatLog) {
            cNLog = new CNChatLogger();
        }

        if (CNOptions.enabled) {
            cNThread = new CNThread("Bob");
        }

        initSuc = true;
    }

    public void watch(String msg) {
        if (CNOptions.enabled) {
            CNMain.tRun();

            CNWatch.chatWatch(msg);
        }

        if (cNLog != null) {
            cNLog.logWrt(scrubServerJargon(msg));
        }
    }

    public static boolean handleCommand(String comStr) {
        if (comStr == null && !isMyCommand(comStr) && initSuc) {
            return false;
        }

        boolean optionsChanged = false, namesChanged = false, chatLogOn = CNOptions.chatLog, commandEntered = false;

        String[] strArr = comStr.substring(1).split(" ");

        if (strArr.length == 2) {
            if (strArr[0].equals("CNTOGGLE")) {
                CNOptions.enabled = Boolean.parseBoolean(strArr[1]);
                commandEntered = true;
                optionsChanged = true;
            } else if (strArr[0].equals("CNWUN")) {
                CNOptions.watchUN = Boolean.parseBoolean(strArr[1]);
                commandEntered = true;
                optionsChanged = true;
            } else if (strArr[0].equals("CNADMIN")) {
                CNOptions.adminM = Boolean.parseBoolean(strArr[1]);
                commandEntered = true;
                optionsChanged = true;
            } else if (strArr[0].equals("CNCHATLOGGER")) {
                CNOptions.chatLog = Boolean.parseBoolean(strArr[1]);
                commandEntered = true;
                optionsChanged = true;
                chatLogOn = CNOptions.chatLog;
            } else if (strArr[0].equals("CNVOLUME")) {
                CNOptions.volume = Float.parseFloat(strArr[1]) / 100.0F;
                commandEntered = true;
                optionsChanged = true;
            } else if (strArr[0].equals("CNHELP")) {
                try {
                    helpPrint(Integer.parseInt(strArr[1]));
                } catch (NumberFormatException e) {
                    helpPrint(-1);
                }
                commandEntered = true;
            } else if (strArr[0].equals("CNADDNAME")) {
                namesChanged = addName(strArr[1]);
                commandEntered = true;
            } else if (strArr[0].equals("CNRMVNAME")) {
                namesChanged = rmvName(strArr[1]);
                commandEntered = true;
            } else if (strArr[0].equals("CNADDWRD")) {
                namesChanged = addWord(strArr[1]);
                commandEntered = true;
            } else if (strArr[0].equals("CNRMVWRD")) {
                namesChanged = rmvWord(strArr[1]);
                commandEntered = true;
            } else {
                return false;
            }
        } else if (strArr.length == 1) {
            if (strArr[0].equals("CNLISTNAMES")) {
                listNames();
                commandEntered = true;
            } else if (strArr[0].equals("CNLISTWORDS")) {
                listWords();
                commandEntered = true;
            } else if (strArr[0].equals("CNHELP")) {
                helpPrint(-1);
                commandEntered = true;
            } else {
                return false;
            }
        }

        if (optionsChanged && CNOptions.enabled) {
            tRun();
        }

        if (optionsChanged && CNOptions.chatLog != chatLogOn) {
            CNMain.initOrSDCLog(CNOptions.chatLog);
        }

        if (optionsChanged) {
            try {
                cNOpt.writeOptions();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (namesChanged) {
            try {
                cNOpt.writeNames();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return commandEntered;
    }

    private static boolean isMyCommand(String comStr) {
        if (comStr.length() >= 12) {
            return comStr.substring(0, 3).equals("/cn");
        } else {
            return false;
        }
    }

    private static void tRun() {
        if (cNThread != null && !cNThread.isAlive()) {
            cNThread.start();
        } else if (cNThread == null) {
            cNThread = new CNThread("Bob2");

            cNThread.start();
        }
    }

    private static void initOrSDCLog(boolean sDOrInit) {
        if (sDOrInit && cNLog == null) {
            cNLog = new CNChatLogger();
        } else if (!sDOrInit && cNLog != null) {
            CNChatLogger.closeLog();

            cNLog = null;
        }
    }

    private static boolean addName(String name) {
        if (CNOptions.getNamesAndWords().containsKey(name)) {
            return false;
        }

        CNOptions.getNamesAndWords().put(name, 0);

        return true;
    }

    private static boolean rmvName(String name) {
        return CNOptions.getNamesAndWords().remove(name) != null ? true : false;
    }

    private static boolean addWord(String word) {
        if (CNOptions.getNamesAndWords().containsKey(word)) {
            return false;
        }

        CNOptions.getNamesAndWords().put(word, 1);

        return true;
    }

    private static boolean rmvWord(String word) {
        return CNOptions.getNamesAndWords().remove(word) != null ? true : false;
    }

    private static boolean listNames() {
        if (CNOptions.getNamesAndWords().containsValue(1)) {
            for (String str : CNOptions.getNamesAndWords().keySet()) {
                if (CNOptions.getNamesAndWords().get(str) == 1) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(str);
                }

                return true;
            }
        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage("There are no names in this list.");

            return true;
        }

        return false;
    }

    private static boolean listWords() {
        if (CNOptions.getNamesAndWords().containsValue(0)) {
            for (String str : CNOptions.getNamesAndWords().keySet()) {
                if (CNOptions.getNamesAndWords().get(str) == 0) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(str);
                }

                return true;
            }
        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage("There are no names in this list.");

            return true;
        }

        return false;
    }

    public static void seDoBheathaAbhaile() {
        if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().theWorld.isRemote && numTicks == 20 && !printed) {
            for (String str : helpWelcomeArr[0]) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(str);
            }
        } else if (!printed) {
            numTicks++;
        }
    }

    private static boolean helpPrint(int i) {
        if (i <= 3 && i > 0) {
            for (String str : helpWelcomeArr[i]) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(str);
            }
            return true;
        } else {
            for (String str : helpWelcomeArr[1]) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(str);
            }
        }

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
