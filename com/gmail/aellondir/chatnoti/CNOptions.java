package com.gmail.aellondir.chatnoti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import net.minecraft.client.Minecraft;

/**
 *
 * @author James Hull
 * @serial McMod JPGH.0001 Class 0
 * @version 0.01
 */
public class CNOptions {

    /**
     *
     */
    protected final static long serialVersionUID = 798909871L;
    /**
     *
     */
    protected static boolean
            enabled = true,
            watchUN = false,
            /**
             *
             */
            adminM = false,
            chatLog = false;
    /**
     *
     */
    protected static float volume = 1.00F;
    /**
     * Names and Words for Administrator mode now immediately capitalized and
     * put in a
     * HashMap for more ready retrieval.
     *
     * @code {Integer} defines how the word should be treated.
     *                  0 = Admin.
     *                  1 = Names.
     * @code {String} is the word to be used.
     */
    protected static HashMap<String, Integer> namesAndWords = new HashMap<>();
    protected static int adminWA = 0, namesAccum = 0;
    private static File optsFile = new File(Minecraft.getMinecraftDir().getAbsolutePath() + System.getProperty("file.seperator") + "CNOpts.txt"),
            namesFile = new File(Minecraft.getMinecraftDir().getAbsolutePath() + System.getProperty("file.seperator") + "CNNames.txt");

    //private constructor as this class is not to be instantiated.
    private CNOptions() {
    }

    /**
     *
     * @return
     */
    protected static int getOptions() {
        boolean opts = false, names = false;

        try {
            opts = readOptions();
        } catch (FileNotFoundException e) {
            //trys to write the options file. if it fails prints the stack trace, because fuck you thats why.
            try {
                opts = writeOptions();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            names = readNames();
        } catch (FileNotFoundException e) {
            try {
                names = writeNames();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (opts && names) {
            return 1;
        } else if (opts) {
            return 42;
        } else if (names) {
            return -42;
        } else {
            return -1;
        }
    }

    private static boolean readOptions() throws FileNotFoundException, IOException {
        BufferedReader optsRead = new BufferedReader(new FileReader(optsFile));

        if (optsRead == null) {
            return false;
        }

        String str = optsRead.readLine();
        boolean reWrite = false;

        if (!str.equals(serialVersionUID)) {
            reWrite = true;
        }

        str = optsRead.readLine();

        do {
            String[] strArr = str.split("=");

            switch (strArr[0]) {
                case "enabled":
                    enabled = Boolean.parseBoolean(strArr[1]);
                    break;
                case "watchun":
                    watchUN = Boolean.parseBoolean(strArr[1]);
                case "adminmode":
                    adminM = Boolean.parseBoolean(strArr[1]);
                    break;
                case "chatLog":
                    chatLog = Boolean.parseBoolean(strArr[1]);
                case "volume":
                    volume = Float.parseFloat(strArr[1])/100.0F;
                    break;
                default:
                    Minecraft.getMinecraft().thePlayer.addChatMessage("Dafuq?  Why would you modify the options file, making it unreadable?");
                    return false;
            }

            str = optsRead.readLine();
        } while (str != null && str.length() != 0);

        if (reWrite) {
             return writeOptions();
        }

        return true;
    }

    private static boolean readNames() throws FileNotFoundException, IOException {
        BufferedReader namesRead = new BufferedReader(new FileReader(namesFile));

        if (namesRead == null) {
            return false;
        }

        String str = namesRead.readLine();
        boolean reWrite = false;

        if (!str.equals(serialVersionUID)) {
            reWrite = true;
        }

        str = namesRead.readLine();

        do {
            boolean namesR = false, adminR = false;

            switch (str) {
                case "nameslist:":
                    namesR = true;
                    adminR = false;
                    break;
                case "adminList":
                    namesR = false;
                    adminR = true;
                    break;
                default:
                    Minecraft.getMinecraft().thePlayer.addChatMessage("Dafuq? Why did you make the names file unreadable?");
                    reWrite = true;
                    break;
            }

            if (namesR && !(str.equals(""))) {
                namesAndWords.put(str.toUpperCase(), 1);
                namesAccum++;
            }

            if (adminR && !(str.equals(""))) {
                namesAndWords.put(str, 0);
                adminWA++;
            }

            str = namesRead.readLine();
        } while (str != null);

        if (reWrite) {
            return writeNames();
        }

        return true;
    }

    private static boolean writeOptions() throws IOException {
        PrintWriter optWrt = new PrintWriter(new FileWriter(optsFile), true);

        if (optWrt == null) {
            return false;
        }

        optWrt.println(serialVersionUID);
        optWrt.println();
        optWrt.println("enabled=" + Boolean.toString(enabled));
        optWrt.println("watchun=" + Boolean.toString(watchUN));
        optWrt.println("adminmode=" + Boolean.toString(adminM));
        optWrt.println("chatLog=" + Boolean.toString(chatLog));
        optWrt.println("volume=" + Float.toString(volume * 100.0F));
        optWrt.println();
        optWrt.println();

        return true;
    }

    private static boolean writeNames() throws IOException {
        PrintWriter namesWrt = new PrintWriter(new FileWriter(namesFile));

        if (namesWrt == null) {
            return false;
        }

        namesWrt.println(serialVersionUID);
        namesWrt.println();
        namesWrt.println("nameslist:");
        if (namesAndWords.isEmpty()) {
            namesAndWords.put(Minecraft.getMinecraft().thePlayer.username, 1);

            namesWrt.println(Minecraft.getMinecraft().thePlayer.username);
            namesWrt.println();
            namesWrt.println("adminwordlist:");
        } else {
            for (String str : namesAndWords.keySet()) {
                if (namesAndWords.get(str) == 1) {
                    namesWrt.println(str);
                }
            }

            namesWrt.println();
            namesWrt.println("adminwordlist:");

            for (String str : namesAndWords.keySet()) {
                if (namesAndWords.get(str) == 0) {
                    namesWrt.println(str);
                }
            }
        }

        namesWrt.println();
        namesWrt.println();

        return true;
    }
}
