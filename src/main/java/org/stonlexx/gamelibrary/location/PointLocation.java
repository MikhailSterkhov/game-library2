package org.stonlexx.gamelibrary.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PointLocation implements Cloneable {

    private double x, y, z;


    public PointLocation add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    public PointLocation add(PointLocation pointLocation) {
        return add(pointLocation.x, pointLocation.y, pointLocation.z);
    }

    public PointLocation subtract(double x, double y, double z) {
        return add(-x, -y, -z);
    }

    public PointLocation subtract(PointLocation pointLocation) {
        return add(-pointLocation.x, -pointLocation.y, -pointLocation.z);
    }

    @Override
    public PointLocation clone() {
        return new PointLocation(x, y, z);
    }

}
