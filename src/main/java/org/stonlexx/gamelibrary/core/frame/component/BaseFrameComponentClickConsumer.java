package org.stonlexx.gamelibrary.core.frame.component;

import javax.swing.*;
import java.awt.event.MouseEvent;

public interface BaseFrameComponentClickConsumer {

    void accept(JComponent swingComponent, MouseEvent mouseEvent);
}
