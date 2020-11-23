package org.stonlexx.gamelibrary.common.event.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.common.event.Event;

import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
@Getter
public class SchedulerCancelEvent extends Event {

    private final String schedulerIdentifier;
    private final ScheduledFuture<?> scheduledFuture;
}
