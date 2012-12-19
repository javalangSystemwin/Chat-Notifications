package com.gmail.aellondir.chatnoti;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.WorldClient;

/**
 *
 * @author James Hull
 * @serial McMod JPGH.0001 class 3
 * @version 0.01
 */
public class CNThread extends Thread {

    @Override
    public void run() {
        WorldClient wC = null;
        EntityClientPlayerMP eCP = null;

        do {
            if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
                wC = Minecraft.getMinecraft().theWorld;
                eCP = Minecraft.getMinecraft().thePlayer;
            }
        } while (wC == null || eCP == null);

        do {
            String str = "note.";
            float var = (float)Math.pow(2.0D, (double)(4.0D - 12) / 12.0D);
            switch (CNWatch.notQueueCheck()) {
                case NIL:
                    break;
                case GENERAL:
                    str += "harp";
                    break;
                case ADMIN:
                    str += "bass";
                    break;
                case W_UN:
                    str += "hat";
                    break;
                default:
                    break;
            }

            if (!str.equals("note.")) {
                wC.playSound((double)eCP.chunkCoordX + 0.5D, (double)eCP.chunkCoordY + 0.5D,
                        (double)eCP.chunkCoordZ + 0.5D, str, 3.0F, var);
            }

            try {
                this.sleep(0x2710L);
            } catch (java.lang.InterruptedException e) {
                e.printStackTrace();
            }
        } while (CNOptions.enabled);
    }
}
