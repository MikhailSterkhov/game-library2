package org.stonlexx.gamelibrary.core.frame.component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public interface BaseFrameButtonClickConsumer {

    void accept(JComponent swingComponent, ActionEvent actionEvent);
}
