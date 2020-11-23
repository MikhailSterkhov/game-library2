package org.stonlexx.gamelibrary.common.frame.component.impl;

import org.stonlexx.gamelibrary.common.frame.component.BaseFrameComponentAdapter;

import javax.swing.*;

public class StandardFrameComponent<C extends JComponent> extends BaseFrameComponentAdapter<C> {

    public StandardFrameComponent(C swingComponent) {
        super(swingComponent);
    }
}
