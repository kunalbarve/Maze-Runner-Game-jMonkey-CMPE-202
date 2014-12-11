package mygame;

import com.jme3.app.state.AbstractAppState;

public abstract class StateManager extends AbstractAppState{
    abstract void setCoinCount(int count);
    abstract int getCurrent();
}
