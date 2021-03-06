package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.V3LD1N;

public class SidebarMessage {
    private final int TITLE_CHARACTER_LIMIT = 32;
    private final int LINE_CHARACTER_LIMIT = 40;
    private final String PREFIX = ConfigSetting.SCOREBOARD_PREFIX.getString();

    private String title;
    private List<String> lines = new ArrayList<>();

    public SidebarMessage(String title) {
        this.title = StringUtil.substring(title, TITLE_CHARACTER_LIMIT);
    }

    public String getTitle() {
        return this.title;
    }

    public List<String> getLines() {
        return this.lines;
    }

    public void setTitle(String title) {
        this.title = StringUtil.substring(title, TITLE_CHARACTER_LIMIT);
    }

    public void addLine(String text) {
        String newText = StringUtil.substring(text, LINE_CHARACTER_LIMIT);
        this.lines.add(newText);
    }

    public void display(Player player, long ticks) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();

        String name = StringUtil.substring(PREFIX + title, 16);
        String displayName = StringUtil.formatText(title.replaceAll("_", " "));

        Objective messageObjective = board.registerNewObjective(name, "dummy");
        messageObjective.setDisplayName(displayName);
        messageObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int sideNumber = lines.size();

        for (String line : lines) {
            line = StringUtil.formatText(line.replaceAll("_", " "));
            Score score = messageObjective.getScore(line);
            score.setScore(sideNumber);
            sideNumber--;
        }

        player.setScoreboard(board);
        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                for (Objective objective : board.getObjectives()) {
                    if (objective.getName().startsWith(PREFIX)) {
                        objective.unregister();
                    }
                }
            }
        }, ticks);
    }
}