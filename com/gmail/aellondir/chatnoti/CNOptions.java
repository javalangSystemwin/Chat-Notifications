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
    protected static boolean enabled = true,
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
    private static HashMap<String, Integer> namesAndWords = new HashMap<String, Integer>();
    protected static int adminWA = 0, namesAccum = 0;
    private static File optsFile = new File(Minecraft.getMinecraftDir().getAbsolutePath() + System.getProperty("file.seperator") + "CNOpts.txt"),
            namesFile = new File(Minecraft.getMinecraftDir().getAbsolutePath() + System.getProperty("file.seperator") + "CNNames.txt");

    /**
     * Names and Words for Administrator mode now immediately capitalized and
     * put in a
     * HashMap for more ready retrieval.
     *
     * @code {Integer} defines how the word should be treated.
     * 0 = Admin.
     * 1 = Names.
     * @code {String} is the word to be used.
     *
     * @return the namesAndWords
     */
    protected static HashMap<String, Integer> getNamesAndWords() {
        return namesAndWords;
    }

    //private constructor as this class is not to be instantiated.
    private CNOptions() {
    }

    /**
     *
     * @return
     */
    protected static int getOptions() throws OptionsFailedException {
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

    private static boolean readOptions() throws FileNotFoundException, IOException, OptionsFailedException {
        BufferedReader optsRead = new BufferedReader(new FileReader(optsFile));

        if (optsRead == null) {
            return false;
        }

        String str = optsRead.readLine();
        boolean reWrite = false;

        if (!str.equals(Long.toString(serialVersionUID))) {
            reWrite = true;
        }

        str = optsRead.readLine();

        do {
            String[] strArr = str.split("=");

            if (strArr[0].equals("enabled")) {
                enabled = Boolean.parseBoolean(strArr[1]);
            } else if (strArr[0].equals("watchun")) {
                watchUN = Boolean.parseBoolean(strArr[1]);
            } else if (strArr[0].equals("adminmode")) {
                adminM = Boolean.parseBoolean(strArr[1]);
            } else if (strArr[0].equals("chatLog")) {
                chatLog = Boolean.parseBoolean(strArr[1]);
            } else if (strArr[0].equals("volume")) {
                volume = Float.parseFloat(strArr[1]) / 100.0F;
            } else {
            }

            str = optsRead.readLine();
        } while (str != null && str.length() != 0);

        if (reWrite) {
            boolean write = writeOptions();

            if (write) {
                return true;
            } else {
                throw new OptionsFailedException("Options Failed to read or write correctly.");
            }
        }

        return true;
    }

    private static boolean readNames() throws FileNotFoundException, IOException, OptionsFailedException {
        BufferedReader namesRead = new BufferedReader(new FileReader(namesFile));

        if (namesRead == null) {
            return false;
        }

        String str = namesRead.readLine();
        boolean reWrite = false;

        if (!str.equals(Long.toString(serialVersionUID))) {
            reWrite = true;
        }

        str = namesRead.readLine();

        do {
            boolean namesR = false, adminR = false;

            if (str.equals("nameslist:")) {
                namesR = true;
                adminR = false;
            } else if (str.equals("adminList")) {
                namesR = false;
                adminR = true;
            } else if (str.equals("")) {
            } else {
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

        if (!reWrite) {
            reWrite = !(adminWA + namesAccum > 0);
        }

        if (reWrite) {
            boolean write = writeOptions();

            if (write) {
                return true;
            } else {
                throw new OptionsFailedException("Names Failed to read or write correctly.");
            }
        }

        return true;
    }

    protected static boolean writeOptions() throws IOException {
        PrintWriter optWrt = new PrintWriter(new FileWriter(optsFile), true);

        try {
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
        } catch (Exception e) {
            optWrt.close();
            e.printStackTrace();
        }

        return true;
    }

    protected static boolean writeNames() throws IOException {
        PrintWriter namesWrt = new PrintWriter(new FileWriter(namesFile));

        try {
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
        } catch (Exception e) {
            namesWrt.close();
            e.printStackTrace();
        }

        return true;
    }
}
