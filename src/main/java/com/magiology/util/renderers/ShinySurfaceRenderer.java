package com.magiology.util.renderers;

import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.Vec3M;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by LapisSea on 25.1.2016..
 */
public class ShinySurfaceRenderer extends VertexRenderer{

    private ColorF baseColor=new ColorF();

    private Vec3M lightVector=new Vec3M(0,-1,0);

    @Override
    public Renderer.RendererBase getRenderer(){
        return Renderer.POS_UV_NORMAL_COLOR;
    }

    @Override
    protected void triangleToTesselatorQuads(ShadedTriangle triangle){


        Vec3M finalNormal=GL11U.transformVector(triangle.normal.addVector(0,0,0), new Vector3f(),rotation.x,rotation.y,rotation.z,1).normalize();


        Vec3M reflectVec=getLightVector().reflect(finalNormal);

        for(int b=0;b<2;b++){

            Vec3M normalPos=Vec3M.conv(triangle.pos3[b].vector3D);
            Vec3M toCameraVec=normalPos.subtract(TessUtil.calculateRenderPosV(UtilM.getThePlayer())).normalize();



            float specular=(float)toCameraVec.dotProduct(reflectVec);

            Vec3M finalVec=GL11U.transformVector(new Vec3M(triangle.pos3[b].vector3D.xCoord, triangle.pos3[b].vector3D.yCoord, triangle.pos3[b].vector3D.zCoord), transformation);
            Renderer.POS_UV_COLOR.addVertex(finalVec, triangle.pos3[b].texturePositionX, triangle.pos3[b].texturePositionY,baseColor.r+specular,baseColor.g+specular,baseColor.b+specular,baseColor.a);
        }
    }

    public ColorF getBaseColor(){
        return baseColor;
    }

    public void setBaseColor(ColorF baseColor){
        this.baseColor=baseColor==null?new ColorF(1,1,1,1):baseColor;
    }

    public Vec3M getLightVector(){
        return lightVector;
    }

    public void setLightVector(Vec3M lightVector){
        this.lightVector=lightVector==null?new Vec3M(0,-1,0):lightVector;
    }
}
