package com.magiology.objhelper.helpers.renderers.tessellatorscripts;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class TexturedTriangle{
    public PositionTextureVertex[] vertexPositions;
    public int nVertices;
    private boolean invertNormal;
    private static final String __OBFID = "CL_00000850";
    private static Tessellator tess=Tessellator.instance;

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
        Vec3 vec3=vertexPositions[1].vector3D.subtract(vertexPositions[0].vector3D);
        Vec3 vec31=vertexPositions[1].vector3D.subtract(vertexPositions[2].vector3D);
        Vec3 vec32=vec31.crossProduct(vec3).normalize();
        tess.startDrawing(GL11.GL_TRIANGLES);
        if(invertNormal)tess.setNormal(-((float)vec32.xCoord),-((float)vec32.yCoord),-((float)vec32.zCoord));
        else tess.setNormal((float)vec32.xCoord,(float)vec32.yCoord,(float)vec32.zCoord);
        for(int i=0;i<3;++i){
            PositionTextureVertex PTV=vertexPositions[i];
            tess.addVertexWithUV(((float)PTV.vector3D.xCoord), ((float)PTV.vector3D.yCoord), ((float)PTV.vector3D.zCoord), PTV.texturePositionX, PTV.texturePositionY);
        }
        tess.draw();
    }
}
