//Marked by bedtwL - 07/23/2024
/*
package com.njdge.fastbuilder.profile;

import com.njdge.fastbuilder.utils.TaskTicker;

public class TimerTicker extends TaskTicker {
    private PlayerProfile profile;


    public TimerTicker(int delay, int period, boolean async, PlayerProfile profile) {
        super(delay, period, async);
        this.profile = profile;
    }

    @Override
    public void onRun() {
        if (profile != null) {
            profile.setTime(profile.getTime() + 50); // increase time by 50 milliseconds
        } else {
            // Handle the case where profile is null
            System.out.println("Profile is null");
        }
    }

    @Override
    public TickType getTickType() {
        return TickType.COUNT_UP;
    }

    @Override
    public int getStartTick() {
        return 0;
    }

    public void stop() {
        this.cancel();
    }
}
*/