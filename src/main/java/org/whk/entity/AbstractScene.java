package org.whk.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractScene implements SceneInterface {


    public AbstractScene() {

    }

    public abstract void sceneTick();

    public void tick() {

    }

}
