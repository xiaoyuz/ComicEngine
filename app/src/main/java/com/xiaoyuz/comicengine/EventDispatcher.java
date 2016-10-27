package com.xiaoyuz.comicengine;

import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.DeadEvent;
import com.squareup.otto.ThreadEnforcer;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Main event dispatcher for distributing/subscribing to events
 * sent and received on the main thread. It will only allow
 * posting events from the main thread and this is the expected
 * behavior and will be enough for almost all cases. In the odd
 * situation where events need to be posted from a background
 * thread the ThreadEventDispatcher can be used however all UI
 * updating must be done on the main thread so all such subscribers
 * MUST be registered to this event dispatcher.
 */
public class EventDispatcher {

    /**
     * Interface to implement by any object being posted to the event bus
     * in case it wants to be notified after the object has been successfully
     * posted to all its event handlers.
     */
    public interface DisposableEvent {
        /**
         * Called when the event has been completely handled by all its
         * event handlers.
         * Implementors MUST NOT post any new events to the bus from this method.
         */
        void dispose();
    }

    public enum Group {
        Main
    }

    private static final String TAG = "EventDispatcher";

    private final Bus mEventBus;
    private final EnumMap<Group, List<Object>> mGroups = new EnumMap<Group, List<Object>>(
            Group.class);
    private final List<DisposableEvent> mDisposables = new LinkedList<DisposableEvent>();
    private int mPostingDepth;
    private static final EventDispatcher instance = new EventDispatcher();

    private EventDispatcher() {
        mEventBus = new Bus(ThreadEnforcer.MAIN, "main-bus");
    }

    /**
     * Posts an event to all registered handlers. Any exceptions thrown by
     * handlers will be rethrown by the event dispatcher. This is to ensure
     * that exceptions doesn't get hidden.
     *
     * Note: When posting an event to the bus that event will be distributed
     * to all its subscribers before the end of the run loop however if you
     * are posting an event (B) as a response to another event (A) happening
     * your new event will only be queued before your call to post() returns.
     * This means the event (B) may not yet have been delivered on the return
     * of your call to post(). The initial call (post(A)) will not return until
     * all queued events (B in this case) have also been delivered. It works
     * this way to ensure same order of deliveries of events to all subscribers.
     *
     * <p>
     * If no handlers have been subscribed for {@code event}'s class, and
     * {@code event} is not already a {@link DeadEvent}, it will be wrapped in a
     * DeadEvent and reposted.
     *
     * @param event event to post.
     */
    static public void post(Object event) {
        instance.handlePost(event);
    }

    private void handlePost(Object event) {
        ++mPostingDepth;
        if (event instanceof DisposableEvent) {
            mDisposables.add((DisposableEvent)event);
        }
        mEventBus.post(event);
        if (--mPostingDepth == 0) {
            for (DisposableEvent disposable : mDisposables) {
                disposable.dispose();
            }
            mDisposables.clear();
        }
    }

    /**
     * Registers all handler methods on {@code object} to receive events.
     * Handler methods are selected and classified using this EventBus's
     * {@link HandlerFindingStrategy}; the default strategy is the
     * {@link AnnotatedHandlerFinder}.
     *
     * @param object object whose handler methods should be registered.
     */
    static public void register(Object object) {
        instance.mEventBus.register(object);
    }

    /**
     * Unregisters all handler methods on a registered {@code object}.
     *
     * @param object object whose handler methods should be unregistered.
     */
    static public void unregister(Object object) {
        try {
            instance.mEventBus.unregister(object);
            for (List<Object> list : instance.mGroups.values()) {
                list.remove(object);
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Error when unregistering event handler", e);
        }
    }

    /**
     * Registers all handler methods on {@code object} to receive events.
     * Handler methods are selected and classified using this EventBus's
     * {@link HandlerFindingStrategy}; the default strategy is the
     * {@link AnnotatedHandlerFinder}. The {@code object} will be associated
     * with the {@code group} and will be unregistered when the Group
     * is unregistered.
     *
     * @param object object whose handler methods should be registered.
     * @param group Group that the object belongs to.
     */
    static public void register(Object object, Group group) {
        List<Object> eventHandlers;

        if (instance.mGroups.containsKey(group)) {
            eventHandlers = instance.mGroups.get(group);
        } else {
            eventHandlers = new LinkedList<Object>();
            instance.mGroups.put(group, eventHandlers);
        }

        // Register to event bus first since it may throw exception and we
        // don't want to keep a reference in that case
        register(object);

        eventHandlers.add(object);
    }

    /**
     * Unregisters all event handlers from the given {@code group}.
     *
     * @param group Group whose event handlers should be unregistered.
     */
    static public void unregister(Group group) {
        List<Object> eventHandlers = instance.mGroups.get(group);
        if (eventHandlers != null) {
            for (Object eventHandler : eventHandlers) {
                instance.mEventBus.unregister(eventHandler);
            }
            instance.mGroups.remove(group);
        }
    }
}
