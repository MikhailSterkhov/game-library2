package org.stonlexx.gamelibrary.common.frame.listener;

import java.awt.event.*;

public abstract class WindowListenerAdapter implements WindowListener, WindowStateListener, WindowFocusListener {

    /**
     * Invoked when a window has been opened.
     */
    public void windowOpened(WindowEvent event) { }

    /**
     * Invoked when a window is in the process of being closed.
     * The close operation can be overridden at this point.
     */
    public void windowClosing(WindowEvent event) { }

    /**
     * Invoked when a window has been closed.
     */
    public void windowClosed(WindowEvent event) { }

    /**
     * Invoked when a window is iconified.
     */
    public void windowIconified(WindowEvent event) { }

    /**
     * Invoked when a window is de-iconified.
     */
    public void windowDeiconified(WindowEvent event) { }

    /**
     * Invoked when a window is activated.
     */
    public void windowActivated(WindowEvent event) { }

    /**
     * Invoked when a window is de-activated.
     */
    public void windowDeactivated(WindowEvent event) { }

    /**
     * Invoked when a window state is changed.
     * @since 1.4
     */
    public void windowStateChanged(WindowEvent event) { }

    /**
     * Invoked when the Window is set to be the focused Window, which means
     * that the Window, or one of its subcomponents, will receive keyboard
     * events.
     *
     * @since 1.4
     */
    public void windowGainedFocus(WindowEvent event) { }

    /**
     * Invoked when the Window is no longer the focused Window, which means
     * that keyboard events will no longer be delivered to the Window or any of
     * its subcomponents.
     *
     * @since 1.4
     */
    public void windowLostFocus(WindowEvent event) { }
}
