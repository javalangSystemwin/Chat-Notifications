package com.gmail.aellondir.chatnoti;

import java.io.*;
import java.util.Calendar;
import net.minecraft.client.Minecraft;

/**
 *
 * @author James Hull
 * @serial McMod JPGH.0001 Class 4
 * @version 0.01
 */
public class CNChatLogger {
    private static PrintWriter logWrt;
    private static File logDir;
    private static File logFile = null;

    public CNChatLogger() {

        logDir = new File(Minecraft.getMinecraftDir() + System.getProperty("file.separator") + "Chat Logs");

        if (!logDir.exists()) {
            logDir.mkdirs();
        }

            String dayMonth = Calendar.DAY_OF_MONTH + "/" + Calendar.MONTH + "/" + Calendar.YEAR;

        do {
            int fileNum = 1;

            StringBuilder sB = new StringBuilder(256);

            try {
                sB.append(logDir.getCanonicalPath()).append(System.getProperty("file.separator")).append("ChatLog(")
                        .append(fileNum).append(")").append(dayMonth).append(".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }

            logFile = new File(sB.toString());

            if (logFile.exists()) {
                logFile = null;
            }

        } while (logFile == null);

        try {
            logWrt = new PrintWriter(logFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void logWrt(String msg) {
        if (msg == null) {
            return;
        } else if (logWrt == null) {
            try {
                logWrt = new PrintWriter(logFile);
            } catch (IOException e) {

            }
        }

        logWrt.println(msg);

        logWrt.flush();
    }

    protected static void closeLog() {
        logWrt.close();
    }
}
