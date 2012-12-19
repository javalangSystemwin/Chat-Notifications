package com.gmail.aellondir.chatnoti;

/**
 *
 * @author James Hull
 * @serial McMod JPGH.0001 Class 1
 * @version 0.01N
 */
public class CNMain {

    private static final char sectionSym = (char) 167;

    public CNMain() {

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
    }
}
