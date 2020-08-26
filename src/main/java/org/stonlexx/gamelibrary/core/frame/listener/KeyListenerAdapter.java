package org.stonlexx.gamelibrary.core.frame.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class KeyListenerAdapter implements KeyListener {

    /**
     * Invoked when a key has been typed.
     * This event occurs when a key press is followed by a key release.
     */
    public void keyTyped(KeyEvent event) { }

    /**
     * Invoked when a key has been pressed.
     */
    public void keyPressed(KeyEvent event) { }

    /**
     * Invoked when a key has been released.
     */
    public void keyReleased(KeyEvent event) { }
}
