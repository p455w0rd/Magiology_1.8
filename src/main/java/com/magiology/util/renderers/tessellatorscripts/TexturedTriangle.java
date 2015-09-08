package com.magiology.util.renderers.tessellatorscripts;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.renderer.WorldRenderer;

import org.lwjgl.opengl.GL11;

import com.magiology.util.renderers.TessHelper;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class TexturedTriangle{
    public PositionTextureVertex[] vertexPositions;
    public int nVertices;
    private boolean invertNormal;
    private static WorldRenderer renderer=TessHelper.getWR();

    public TexturedTriangle(PositionTextureVertex[] PTV){
    	if(PTV.length!=3)return;
        this.vertexPositions = PTV;
        this.nVertices = PTV.length;
    }
    public void flipFace(){
        PositionTextureVertex[] apositiontexturevertex = new PositionTextureVertex[this.vertexPositions.length];
        for(int i=0;i<this.vertexPositions.length;++i)apositiontexturevertex[i] = this.vertexPositions[this.vertexPositions.length - i - 1];
        this.vertexPositions = apositiontexturevertex;
    }

    public void draw(){
        Vec3M Vec3=Vec3M.conv(vertexPositions[1].vector3D.subtract(vertexPositions[0].vector3D));
        Vec3M vec31=Vec3M.conv(vertexPositions[1].vector3D.subtract(vertexPositions[2].vector3D));
        Vec3M vec32=vec31.crossProduct(Vec3).normalize();
        renderer.startDrawing(GL11.GL_TRIANGLES);
        if(invertNormal)renderer.setNormal(-((float)vec32.x),-((float)vec32.y),-((float)vec32.z));
        else renderer.setNormal((float)vec32.x,(float)vec32.y,(float)vec32.z);
        for(int i=0;i<3;++i){
            PositionTextureVertex PTV=vertexPositions[i];
            renderer.addVertexWithUV(((float)PTV.vector3D.xCoord), ((float)PTV.vector3D.yCoord), ((float)PTV.vector3D.zCoord), PTV.texturePositionX, PTV.texturePositionY);
        }
        TessHelper.draw();
    }
}