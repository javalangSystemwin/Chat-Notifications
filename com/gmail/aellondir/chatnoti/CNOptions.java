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
public final class CNOptions {
    private final static long serialVersionUID = 798909871L;

    /**
     *
     */
    protected static boolean enabled = true,
            watchUN = false,
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

    /**
     *
     */
    protected static int adminWA = 0,
            /**
             *
             */
            namesAccum = 0;
    // used for the storage of options, names, and word files for later use in rewriting the files.
    private static File optsFile, namesFile;

    /**
     * Constructor attempts to write the two necessary files into the Minecraft directory, there is no encryption used
     * as
     * it is intended that one should be able to set ones options outside of the game, as well as within.
     */
    public CNOptions() {
        optsFile = new File(Minecraft.getMinecraftDir().getAbsolutePath() + System.getProperty("file.seperator") + "CNOpts.txt");

        try {
            if (!optsFile.exists()) {
                optsFile.createNewFile();
                this.writeOptions();
            } else {
                this.readOptions();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }

        namesFile = new File(Minecraft.getMinecraftDir().getAbsolutePath() + System.getProperty("file.seperator") + "CNNames.txt");

        try {
            if (!namesFile.exists()) {
                namesFile.createNewFile();
                this.writeNames();
            } else {
                this.readNames();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * Names and Words for Administrator mode now put in a
     * HashMap for more ready retrieval.
     *
     * @code {String} is the word to be used.
     *
     * @code {Integer} defines how the word should be treated.
     * 0 = Admin.
     * 1 = Names.
     *
     * @return the namesAndWords Hashmap.
     */
    protected static HashMap<String, Integer> getNamesAndWords() {
        return namesAndWords;
    }

    //Trys to read the options decides whether or not a rewrite is necessary.
    // RETURN TYPE: the success of the operation, only returns true if no exceptions have been thrown, and its gotten to
    // its final if statement or past.
    private boolean readOptions() throws FileNotFoundException, IOException {
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
            }
        }

        return true;
    }

    //Trys to read the names decides whether or not a rewrite is necessary.
    // RETURN TYPE: the success of the operation, only returns true if no exceptions have been thrown, and its gotten to
    // its final if statement or past.
    private boolean readNames() throws FileNotFoundException, IOException {
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
                return false;
            }
        }

        return true;
    }

    /**
     * Called when the options need writing/rewriting.
     *
     * @return returns the success of the operation true = Completed Successfully, false = failed, either due to
     *         exception or otherwise.
     *
     * @throws IOException
     */
    protected boolean writeOptions() throws IOException {
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
            optWrt.println("volume=" + Integer.toString((int) (volume * 100.0F)));
            optWrt.println();
            optWrt.println();
        } catch (Exception e) {
            optWrt.close();
            e.printStackTrace();
        }

        optWrt.close();
        return true;
    }

    /**
     * Called when the names/words need writing/rewriting.
     *
     * @return returns the success of the operation true = Completed Successfully, false = failed, either due to
     *         exception or otherwise.
     *
     * @throws IOException
     */
    protected boolean writeNames() throws IOException {
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

        namesWrt.close();
        return true;
    }
}
