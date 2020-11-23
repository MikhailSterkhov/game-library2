package org.stonlexx.gamelibrary.common.frame.component;

import javax.swing.*;
import java.awt.event.MouseEvent;

public interface BaseFrameComponentClickConsumer {

    void accept(JComponent swingComponent, MouseEvent mouseEvent);
}
