package com.magiology.util.renderers;

/**
 * Created by LapisSea on 25.1.2016..
 */
public class ShinySurfaceRenderer extends VertexRenderer{

    @Override
    public Renderer.RendererBase getRenderer(){
        return Renderer.POS_UV_NORMAL_COLOR;
    }
}
