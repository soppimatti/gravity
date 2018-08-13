package com.matti.gravity;

/**
 * Created by matti on 13/02/16.
 */
public interface ITickListener
{
    public void onTick(long tickCounter);
    public boolean onUpdate(long tickCounter);
    public void onRender(long tickCounter);
}
