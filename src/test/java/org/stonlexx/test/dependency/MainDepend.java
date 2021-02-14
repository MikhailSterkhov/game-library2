package org.stonlexx.test.dependency;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.stonlexx.gamelibrary.common.dependency.Depend;

@Depend
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainDepend {

    protected final int dependLevel = (100);
    protected final String dependName = ("MainDependency");
}
