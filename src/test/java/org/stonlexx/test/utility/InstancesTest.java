package org.stonlexx.test.utility;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.stonlexx.gamelibrary.utility.Instances;

public class InstancesTest {

    public static void main(String[] args) {
        InstanceableObject instanceableObject = InstanceableObject.getInstance();
        System.out.println(instanceableObject.getTestMessage());
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class InstanceableObject {

        public static InstanceableObject getInstance() {
            InstanceableObject instanceableObject = Instances.getInstance(InstanceableObject.class);

            if (instanceableObject == null) {
                Instances.addInstance(instanceableObject = new InstanceableObject());
            }

            return instanceableObject;
        }

        @Getter
        private final String testMessage
                = ("Success created!");
    }

}
