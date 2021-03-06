package com.v3ld1n.util;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.v3ld1n.V3LD1N;

/**
 * This class is provided as an easy way to handle repeating scheduled tasks. Do not use scheduleSyncRepeatingTask as when a new instance of this class is created, it automatically schedules one. Just create a new intstance.
 * @author KingFaris10
 */
public abstract class RepeatableRunnable extends BukkitRunnable {

    private int repeatableTaskID = 0;
    private long repeats = 0;
    private long maxRepeats = 0;

    public RepeatableRunnable() {
    }

    /**
     * Run the task.
     * 
     * @param delay - Delay in server ticks before executing first repeat.
     * @param period - Period in server ticks of the task.
     * @param amountOfTimes - The amount of times to call the onRun method.
     */
    @SuppressWarnings("deprecation")
    public void start(long delay, long period, long amountOfTimes) {
        this.repeatableTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(V3LD1N.getPlugin(), this, delay, period);
        this.maxRepeats = amountOfTimes > 0 ? amountOfTimes : 1;
    }

    @Override
    public void run() {
        if (this.repeats < this.maxRepeats - 1) {
            this.repeats++;
            this.onRun();
        } else if (this.repeats == this.maxRepeats - 1) {
            this.repeats++;
            this.onLastRun();
        } else {
            try {
                if (Bukkit.getScheduler().isCurrentlyRunning(this.repeatableTaskID) || Bukkit.getScheduler().isQueued(this.repeatableTaskID)) {
                    Bukkit.getScheduler().cancelTask(this.repeatableTaskID);
                }
            } catch (Exception ex) {
                try {
                    this.cancel();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public abstract void onRun();

    public void onLastRun() {
    }

    public long getRepeats() {
        return this.repeats;
    }

    public long getMaxRepeats() {
        return this.maxRepeats;
    }

    public void setTaskID(int taskID) {
        this.repeatableTaskID = taskID;
    }

}