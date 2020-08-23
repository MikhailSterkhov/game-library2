package org.stonlexx.gamelibrary.core.frame.listener;

import java.awt.event.*;

public class MouseListenerAdapter implements MouseListener, MouseWheelListener, MouseMotionListener {

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    public void mouseClicked(MouseEvent event) { }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent event) { }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent event) { }

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent event) { }

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent event) { }

    /**
     * Invoked when the mouse wheel is rotated.
     * @see MouseWheelEvent
     */
    public void mouseWheelMoved(MouseWheelEvent event) { }

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
     * Drag&amp;Drop operation.
     */
    public void mouseDragged(MouseEvent event) { }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     */
    public void mouseMoved(MouseEvent event) { }
}
