package org.stonlexx.gamelibrary.core.event.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.core.event.CoreEvent;

import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
@Getter
public class SchedulerCancelEvent extends CoreEvent {

    private final String schedulerIdentifier;
    private final ScheduledFuture<?> scheduledFuture;
}
