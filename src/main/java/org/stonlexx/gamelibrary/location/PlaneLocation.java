package org.stonlexx.gamelibrary.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PlaneLocation implements Cloneable {
    
    private double x, y;

    public PlaneLocation add(double x, double y) {
        this.x += x;
        this.y += y;

        return this;
    }

    public PlaneLocation add(PlaneLocation planeLocation) {
        return add( planeLocation.x, planeLocation.y );
    }

    public PlaneLocation subtract(double x, double y) {
        return add( -x, -y );
    }

    public PlaneLocation subtract(PlaneLocation planeLocation) {
        return add( -planeLocation.x, -planeLocation.y );
    }

    @Override
    public PlaneLocation clone() {
        return new PlaneLocation(x, y);
    }
}
