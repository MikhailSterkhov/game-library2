package org.stonlexx.gamelibrary.common.frame.component;

import javax.swing.*;
import java.awt.event.ActionEvent;

public interface BaseFrameButtonClickConsumer {

    void accept(JComponent swingComponent, ActionEvent actionEvent);
}
