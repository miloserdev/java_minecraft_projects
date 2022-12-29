package Generator.populator;

import java.util.ArrayDeque;
import java.util.Queue;

import org.bukkit.block.Block;

import A_Main.Main;

public class BlockToucher {
    private static final int TOUCHES_PER_TICK = 50;

    private Queue<Block> needsTouching = new ArrayDeque<>();
    private boolean running;

    public void touch(Block block) {
        needsTouching.add(block);

        if (!running) {
            running = true;
            Main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask( Main.getPlugin(), new TouchTask());
        }
    }

    private class TouchTask implements Runnable {
        @Override
        public void run() {
            if (needsTouching.isEmpty()) {
                running = false;
            } else {
                for (int i = 0; i < TOUCHES_PER_TICK; i++) {
                    if (!needsTouching.isEmpty()) {
                        Block block = needsTouching.remove();
                        if (block.getType().hasGravity()) {
                            block.getState().update(true, true);
                        }
                    }
                }

                Main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask( Main.getPlugin(), this);
            }
        }
    }
}