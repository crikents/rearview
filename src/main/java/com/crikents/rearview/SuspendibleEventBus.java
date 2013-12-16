package com.crikents.rearview;

import net.minecraftforge.event.Event;
import net.minecraftforge.event.EventBus;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Scott on 12/14/13.
 */
public class SuspendibleEventBus extends EventBus {
    EventBus bus;
    CopyOnWriteArrayList<Class> suspended = new CopyOnWriteArrayList<Class>();

    SuspendibleEventBus(EventBus bus) {
        this.bus = bus;
    }

    @Override
    public void register(Object target) {
        bus.register(target);
    }

    @Override
    public void unregister(Object object) {
        bus.unregister(object);
    }

    @Override
    public boolean post(Event event) {
        for (Class sus : suspended)
            if (event == null || sus.isInstance(event)) return false;
        return bus.post(event);
    }

    public void suspend(Class c) {
        suspended.add(c);
    }

    public void resume(Class c) {
        suspended.remove(c);
    }
}
