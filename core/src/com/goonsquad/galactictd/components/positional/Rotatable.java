package com.goonsquad.galactictd.components.positional;

public class Rotatable extends com.artemis.PooledComponent {

    public boolean rotating;
    public boolean continuousRotation;
    public float rotationInRadians;
    public float progress;
    public float rotationTargetAngle;

    @Override
    protected void reset() {
        rotating = false;
        continuousRotation = false;
        rotationInRadians = 0f;
        progress = 0f;
        rotationTargetAngle = 0f;
    }
}
