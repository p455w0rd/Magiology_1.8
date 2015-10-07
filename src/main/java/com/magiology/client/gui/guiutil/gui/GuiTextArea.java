package com.magiology.client.gui.guiutil.gui;

import static com.magiology.util.utilclasses.Util.*;

import java.awt.Color;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.client.render.font.FontRendererMClipped;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.NormalizedVertixBuffer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.tessellatorscripts.Drawer;
import com.magiology.util.utilclasses.Get.Render.Font;
import com.magiology.util.utilobjects.vectors.AdvancedPhysicsFloat;
import com.magiology.util.utilobjects.vectors.Vec2i;

public class GuiTextArea extends Gui implements Updateable{
	
	private List<StringBuilder> textBuffer=new ArrayList<StringBuilder>();
	private List<Integer> cachedWidth=new ArrayList<Integer>();
	
	private List<UndoSave> undoSteps=new ArrayList<UndoSave>();
	private int undoPos,prevXPos;
	
	public Vec2i cursorPosition=Vec2i.zero,selectionStart=Vec2i.zero;
	public Vec2i pos, size,mouse=Vec2i.zero,lastMouse=Vec2i.zero,mouseClick=Vec2i.zero;
	public int width,white=Color.WHITE.hashCode(),black=new Color(16,16,32,255).hashCode(),maxWidth;
	private float sliderX,prevSliderX,sliderY,prevSliderY;
	private AdvancedPhysicsFloat sliderXCol=new AdvancedPhysicsFloat(0, 0.2F,true),sliderYCol=new AdvancedPhysicsFloat(0, 0.2F,true);
	
	public boolean active=false,visible=true,insertMode=false;
	

	public GuiTextArea(Vec2i pos,Vec2i size){
		textBuffer.add(new StringBuilder());
		cachedWidth.add(0);
		this.pos=pos;
		this.size=size;
	}

	public void render(int x, int y){
		if(!visible)return;
		if(!Mouse.isButtonDown(0)){
			lastMouse=mouse;
			mouse=new Vec2i(x-pos.x, y-pos.y);
		}
		FontRendererMClipped fr=FontRendererMClipped.get();
		int guiScale=getGuiScaleRaw();
		GL11U.texture(false);
		GL11.glLineWidth(guiScale);
		
		NormalizedVertixBuffer buff=TessUtil.getNVB();
		buff.addVertex(pos.x-3, pos.y+size.y+3, 0);
		buff.addVertex(pos.x+size.x+3, pos.y+size.y+3, 0);
		buff.addVertex(pos.x+size.x+3, pos.y-3, 0);
		buff.addVertex(pos.x-3, pos.y-3, 0);
		
		GL11U.color(black);
		buff.setClearing(false);
		buff.draw();
		buff.setClearing(true);
		
		GL11U.color(new Color(160, 160, 160, 255).hashCode());
		buff.setDrawAsWire(true);
		buff.draw();
		buff.setDrawAsWire(false);
		
		GL11.glLineWidth(1);
		GL11U.texture(true);
		
		GL11.glPushMatrix();
		float xOffset=-sliderX*(maxWidth-size.x);
		GL11.glTranslated(xOffset, 0, 0);
		if(active&&hasSelection()){
			renderSelection(0xFFDFB578);
		}
		
		GL11.glTranslated(-xOffset, 0, 0);
		buff.pushMatrix();
		buff.translate(xOffset, 0, 0);
		for(int i=0;i<textBuffer.size();i++){
			fr.min=new Vector2f(pos.x-2,pos.y-2);
			fr.max=new Vector2f(pos.x+size.x+2,pos.y+size.y+2);
			fr.drawString(textBuffer.get(i).toString(), pos.x, pos.y+i*fr.FONT_HEIGHT, white);
		}
		buff.popMatrix();
		GL11.glTranslated(xOffset, 0, 0);
		
		// Selection
		if(active&&hasSelection()){
			GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
			renderSelection(0xFFFFFFFF);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		
		if(active&&getWorldTime(getTheWorld())/6%2==0){
			if(cursorPosition.x>getCurrentLine().length())cursorPosition.x=getCurrentLine().length();
			int cursorX=pos.x+fr.getStringWidth(getCurrentLine().substring(0, cursorPosition.x));
			int cursorY=pos.y+cursorPosition.y*fr.FONT_HEIGHT;
			boolean vertical=cursorPosition.x-getCurrentLine().length()<0;
			Rectangle box=new Rectangle(pos.x-2,pos.y-2,size.x+4, size.y+4);
			if(box.contains(cursorX+xOffset, cursorY)&&(vertical?true:box.contains(cursorX+xOffset+7, cursorY))){
				if(vertical)drawVerticalLine(cursorX, cursorY-2, cursorY+10, white);
				else drawRect(cursorX, cursorY+8, cursorX+5, cursorY+7, white);
			}
		}
		GL11.glPopMatrix();
		
		if(maxWidth>size.x){
			GL11U.texture(false);
			GL11U.SetUpOpaqueRendering(2);
			GL11U.color(new Color(160, 160, 160, 55).hashCode());
			int height=8,width=Math.max((int)((size.x-height)*((float)size.x/(float)maxWidth)),10),toSlide=maxWidth-size.x;
			drawModalRectWithCustomSizedTexture((pos.x)-1, pos.y+size.y-height-1, 0, 0, size.x-height+2, height+2, 0, 0);
			GL11U.color(new Color(160, 160, 160, (int)(75*sliderXCol.getPoint())).hashCode());
			drawModalRectWithCustomSizedTexture((int)(pos.x+(size.x-width-height)*sliderX), pos.y+size.y-height, 0, 0, width, height, 0, 0);
			GL11U.texture(true);
			GL11U.EndOpaqueRendering();
		}else sliderX=0;
	}

	private void renderSelection(int color){
		FontRenderer fr=Font.FR();
		Pair<Vec2i, Vec2i> selection=selection();
		Vec2i first=selection.getLeft();
		Vec2i last=selection.getRight();
		
		float xOffset=-sliderX*(maxWidth-size.x);
		if(first.y==last.y){
			int x1=fr.getStringWidth(getLine(first.y).substring(0, first.x));
			int x2=fr.getStringWidth(getLine(first.y).substring(0, last.x));
			drawRect((int)Math.max(pos.x+x1, pos.x-xOffset-2), pos.y+first.y*fr.FONT_HEIGHT, (int)Math.min(pos.x+x2, pos.x+size.x-xOffset+2), pos.y+(first.y+1)*fr.FONT_HEIGHT,color);
		}else{
			int x1=fr.getStringWidth(getLine(first.y).substring(0, first.x));
			int x2=cachedWidth.get(first.y);
			drawRect(pos.x+x1, pos.y+first.y*fr.FONT_HEIGHT, pos.x+x2, pos.y+(first.y+1)*fr.FONT_HEIGHT, color);
			for(int i=0;i<last.y-first.y-1;i++){
				x2=cachedWidth.get(first.y+i+1);
				drawRect((int)Math.max(pos.x, pos.x-xOffset-2), pos.y+(first.y+i+1)*fr.FONT_HEIGHT, (int)Math.min(pos.x+x2, pos.x+size.x-xOffset+2), pos.y+(first.y+i+2)*fr.FONT_HEIGHT, color);
			}

			x2=fr.getStringWidth(getLine(last.y).substring(0, last.x));
			drawRect((int)Math.max(pos.x, pos.x-xOffset-2), pos.y+last.y*fr.FONT_HEIGHT, (int)Math.min(pos.x+x2, pos.x+size.x-xOffset+2), pos.y+(last.y+1)*fr.FONT_HEIGHT, color);
		}
	}

	private int clickCount=0;
	private long lastClickTime=Long.MAX_VALUE;
	private static final int CLICK_TIME=200;

	public void mouseClicked(int x, int y, int button){
		if(!visible||button!=0)return;
		lastMouse=mouse;
		mouse=new Vec2i(x-pos.x, y-pos.y);
		mouseClick=new Vec2i(x-pos.x, y-pos.y);
		if(handleSlider(x, y)){
			Vec2i intersection=findCharAtPos(x, y);
			if(intersection==null){
				active=false;
				return;
			}
			prevXPos=intersection.x;
			long time=System.currentTimeMillis();
			if(time-lastClickTime>CLICK_TIME){
				clickCount=0;
			}
			lastClickTime=time;

			active=true;

			if(clickCount==0){
				setCursorPositionInternal(intersection);
			}else if(clickCount==1){
				selectWord(intersection);
			}else if(clickCount==2){
				selectLine(intersection);
				clickCount=0;
			}
		}

		clickCount++;
	}
	
	public void mouseClickMove(int x, int y){
		if(!visible||!active)return;
		Vec2i move=lastMouse.add(-mouse.x,-mouse.y);
		lastMouse=mouse;
		mouse=new Vec2i(x-pos.x, y-pos.y);
		if(handleSlider(x, y)){
			Vec2i intersection=findCharAtPos(x, y);
			if(intersection!=null){
				cursorPosition=intersection;
			}
		}
	}
	private boolean handleSlider(int x, int y){
		if(size.y-mouseClick.y<=8){
//			TODO: if(mouseClick.x)
			int width=Math.max((int)((size.x-8)*((float)size.x/(float)maxWidth)),10);
			prevSliderX=sliderX;
			sliderX=keepValueInBounds(((float)(mouse.x-width/2)/(float)(size.x-width)), 0, 1);
			return false;
		}
		return true;
	}
	private static final Pattern patternWord=Pattern.compile("\\b");

	private void selectWord(Vec2i intersection){
		StringBuilder line=getLine(intersection.y);

		Matcher forward=patternWord.matcher(line);
		Matcher backward=patternWord.matcher(new StringBuilder(line).reverse());
		
		int next=forward.find(intersection.x) ? forward.start():getLength(intersection.y);
		int prev=backward.find(line.length()-intersection.x) ? line.length()-backward.start():0;
		
		selectionStart=new Vec2i(prev, intersection.y);
		cursorPosition=new Vec2i(next, intersection.y);
	}

	private void selectLine(Vec2i intersection){
		selectionStart=new Vec2i(0, intersection.y);
		if(intersection.y+1>=textBuffer.size()){
			cursorPosition=new Vec2i(getLength(intersection.y), intersection.y);
		}else{
			cursorPosition=new Vec2i(0, intersection.y+1);
		}
	}
	
	public boolean keyTyped(int code, char ch){
		String prevText=getText();
		try{
			if(!visible||!active)return false;
			boolean con=false;
			switch(code){
				case Keyboard.KEY_INSERT:insertMode=!insertMode;break;
				case Keyboard.KEY_DELETE:delete();break;
				case Keyboard.KEY_UP:up();break;
				case Keyboard.KEY_DOWN:down();break;
				case Keyboard.KEY_LEFT:left();break;
				case Keyboard.KEY_RIGHT:right();break;
				case Keyboard.KEY_END:{
					if(!GuiScreen.isCtrlKeyDown())cursorPosition.x=getCurrentLine().length();
					else{
						cursorPosition.y=Math.max(0, textBuffer.size()-1);
						if(cursorPosition.x>getCurrentLine().length())cursorPosition.x=getCurrentLine().length();
					}
				}break;
				case Keyboard.KEY_Z:{
					if(GuiScreen.isCtrlKeyDown()&&!undoSteps.isEmpty()){
						selectAll();
						replace(undoSteps.get(undoPos).content);
						cursorPosition=selectionStart=undoSteps.get(undoPos).cursor;
						undoPos++;
						if(undoPos>100)undoPos=100;
					}
				}break;
				case Keyboard.KEY_Y:{
					if(GuiScreen.isCtrlKeyDown()&&!undoSteps.isEmpty()&&undoPos>0){
						selectAll();
						replace(undoSteps.get(undoPos).content);
						cursorPosition=selectionStart=undoSteps.get(undoPos).cursor;
						undoPos--;
						if(undoPos>100)undoPos=100;
					}
				}break;
				default:con=true;
			}
			if(con)switch(ch){
				case 1:selectAll();break;//^A
				case 3:GuiScreen.setClipboardString(getSelectedText());break;//^C
				case 22:replace(GuiScreen.getClipboardString());break;//^V
				case 24:{
					GuiScreen.setClipboardString(getSelectedText());
					deleteSelected();
				}break;//^X
				case 8:backspace();break;//backspace
				case 27:active=false;break;//ESC
				case 13:replace("\n");break;//CR
				case '\t':tab();break;
				default:
					if(!Character.isISOControl(ch))replace(String.valueOf(ch));
					break;
			}
			if(!GuiScreen.isCtrlKeyDown()||code!=Keyboard.KEY_Z||code!=Keyboard.KEY_Y){
				for(int i=0;i<undoPos;i++){
					undoSteps.remove(0);
				}
				undoPos=0;
			}
			if(!(code==Keyboard.KEY_Z&&GuiScreen.isCtrlKeyDown())){
				if(!getText().equals(prevText)){
					undoSteps.add(0,new UndoSave(prevText, cursorPosition.add(0, 0)));
					if(undoSteps.size()>100)undoSteps.remove(101);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		maxWidth=0;
		for(int i:cachedWidth){
			maxWidth=Math.max(maxWidth, i);
		}
		return true;
	}

	public void selectAll(){
		selectionStart=Vec2i.zero;
		cursorPosition=getLastPosition();
	}

	private void backspace(){
		if(hasSelection()){
			deleteSelected();
			return;
		}
		if(cursorPosition.equals(Vec2i.zero))
			return;
		if(cursorPosition.x==0){
			StringBuilder line=getCurrentLine();
			removeLine(cursorPosition.y);
			Vec2i newCursorPosition=new Vec2i(getLength(cursorPosition.y-1), cursorPosition.y-1);
			getLine(cursorPosition.y-1).append(line);
			refreshLine(cursorPosition.y-1);
			setCursorPositionInternal(newCursorPosition);
			refreshWidth();
		}else{
			getCurrentLine().deleteCharAt(cursorPosition.x-1);
			refreshLine(cursorPosition.y);
			setCursorPositionInternal(new Vec2i(cursorPosition.x-1, cursorPosition.y));
			refreshWidth();
		}
	}
	
	private void delete(){
		if(hasSelection()){
			deleteSelected();
			return;
		}
		if(cursorPosition.equals(getLastPosition()))
			return;
		if(cursorPosition.x>=getLength(cursorPosition.y)){
			StringBuilder line=getLine(cursorPosition.y+1);
			removeLine(cursorPosition.y+1);
			getLine(cursorPosition.y).append(line);
			refreshLine(cursorPosition.y);
		}else{
			if(GuiScreen.isCtrlKeyDown()){
				selectWord(cursorPosition);
				delete();
			}else{
				getCurrentLine().deleteCharAt(cursorPosition.x);
			}
			refreshLine(cursorPosition.y);
			refreshWidth();
		}
	}
	
	private static final Pattern patternBlankSpace=Pattern.compile("^\\s*$");
	private static final Pattern patternFirstNonWhitespace=Pattern.compile("[^\\s]|\\s$");

	private void tab(){
		// Got one line
		if(cursorPosition.y==selectionStart.y){
			if(cursorPosition.x % 2==0)
				replace("    ");
			else
				replace(" ");

			if(patternBlankSpace.matcher(getCurrentLine().toString().substring(0, cursorPosition.x)).matches()){
				// We're starting with some whitespace, move forward
				Matcher matcher=patternFirstNonWhitespace.matcher(getCurrentLine());
				if(matcher.find()){
					setCursorPositionInternal(new Vec2i(matcher.start(), cursorPosition.y));
				}
			}
		}else{
			Pair<Vec2i, Vec2i> selection=selection();
			// Tab multiple lines
			for(int i=selection.getLeft().y;i <= selection().getRight().y;i++){
				getLine(i).insert(0, "    ");
				refreshLine(i);
			}

			// Adjust selection
			tab_adjustSelection(selection);
		}
		
		refreshWidth();
	}

	private void tab_adjustSelection(Pair<Vec2i, Vec2i> selection){
		boolean flip=cursorPosition==selection.getLeft();
		selectionStart=new Vec2i(0, selection.getLeft().y);
		cursorPosition=new Vec2i(getLength(selection.getRight().y), selection.getRight().y);

		if(flip){
			Vec2i temp=selectionStart;
			selectionStart=cursorPosition;
			cursorPosition=temp;
		}
	}

	private void up(){
		FontRenderer fr=Font.FR();
		if(cursorPosition.y==0)return;
		
		int x=fr.getStringWidth(getLine(cursorPosition.y-1).substring(0, prevXPos>getLine(cursorPosition.y-1).length()?getLine(cursorPosition.y-1).length():prevXPos));
		try{
			x=findCharAtPos(pos.x+x, pos.y+(cursorPosition.y-1)*fr.FONT_HEIGHT).x;
			setCursorPositionInternal(new Vec2i(x, cursorPosition.y-1));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void down(){
		FontRenderer fr=Font.FR();
		if(cursorPosition.y>=textBuffer.size()-1)return;
		
		int x=fr.getStringWidth(getLine(cursorPosition.y+1).substring(0, prevXPos>getLine(cursorPosition.y+1).length()?getLine(cursorPosition.y+1).length():prevXPos));
		try{
			x=findCharAtPos(pos.x+x, pos.y+(cursorPosition.y+1)*fr.FONT_HEIGHT).x;
			setCursorPositionInternal(new Vec2i(x, cursorPosition.y+1));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void left(){
		if(cursorPosition.equals(Vec2i.zero))return;
		prevXPos=cursorPosition.x;
		if(cursorPosition.x==0){
			setCursorPositionInternal(new Vec2i(getLength(cursorPosition.y-1), cursorPosition.y-1));
		}else setCursorPositionInternal(new Vec2i(cursorPosition.x-1, cursorPosition.y));
	}

	private void right(){
		if(cursorPosition.equals(getLastPosition()))return;
		prevXPos=cursorPosition.x;
		if(cursorPosition.x>=getLength(cursorPosition.y)){
			setCursorPositionInternal(new Vec2i(0, cursorPosition.y+1));
		}else{
			setCursorPositionInternal(new Vec2i(cursorPosition.x+1, cursorPosition.y));
		}
	}

	private Vec2i findCharAtPos(int mx, int my){
		FontRenderer fr=Font.FR();
		try{
			mx+=sliderX*(maxWidth-size.x);
			int y=my-pos.y;
			if(y<0||y>=size.y)return null;
			y/=fr.FONT_HEIGHT;
			int x=mx-pos.x;
			if(x<0||x>size.x*2)return null;
			y=Math.min(y,textBuffer.size()-1);
			x=Math.min(x,width);
			String s=fr.trimStringToWidth(textBuffer.get(y).toString(), x);
			char nextChar='';
			if(textBuffer.get(y).length()>s.length())nextChar=textBuffer.get(y).toString().charAt(s.length());
			x=s.length();
			if(nextChar!=''&&fr.getCharWidth(nextChar)<=((mx-pos.x)-fr.getStringWidth(s))*2)x++;
			return new Vec2i(x, y);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	protected StringBuilder getLine(int line){
		return textBuffer.get(line);
	}
	protected StringBuilder getCurrentLine(){
		return getLine(cursorPosition.y);
	}
	protected void refreshLine(int line){
		cachedWidth.set(line, Font.FR().getStringWidth(textBuffer.get(line).toString()));
	}
	protected void refreshWidth(){
		width=0;
		maxWidth=0;
		for(int w:cachedWidth){
			if(w>width)width=w;
			maxWidth=Math.max(maxWidth, w);
		}
	}
	protected void setLine(StringBuilder sb, int pos){
		textBuffer.set(pos, sb);
		cachedWidth.set(pos, 0);
		refreshLine(pos);
		refreshWidth();
	}
	protected void addLine(StringBuilder sb, int pos){
		textBuffer.add(pos, sb);
		cachedWidth.add(pos, 0);
		refreshLine(pos);
		refreshWidth();
	}
	protected void addLine(StringBuilder sb){
		textBuffer.add(sb);
		cachedWidth.add(0);
		refreshLine(textBuffer.size()-1);
		refreshWidth();
	}
	protected void removeLine(int line){
		textBuffer.remove(line);
		cachedWidth.remove(line);
		refreshWidth();
	}
	public int getLength(int line){
		return textBuffer.get(line).length();
	}
	public Vec2i getLastPosition(){
		return new Vec2i(textBuffer.get(textBuffer.size()-1).length(), textBuffer.size()-1);
	}
	protected Pair<Vec2i, Vec2i> selection(){
		Vec2i first, last;
		if(selectionStart.y<cursorPosition.y||(selectionStart.y==cursorPosition.y&&selectionStart.x<cursorPosition.x)){
			first=selectionStart;
			last=cursorPosition;
		}else{
			first=cursorPosition;
			last=selectionStart;
		}
		return new ImmutablePair<Vec2i, Vec2i>(first, last);
	}
	public String getSelectedText(){
		if(!hasSelection())return "";
		
		Pair<Vec2i, Vec2i> selection=selection();
		Vec2i first=selection.getLeft();
		Vec2i last=selection.getRight();

		List<StringBuilder> selectedLines=textBuffer.subList(first.y, last.y+1);
		if(selectedLines.size()==1){
			return selectedLines.get(0).substring(first.x, last.x);
		}else{
			StringBuilder sb=new StringBuilder();
			sb.append(selectedLines.get(0).substring(first.x)).append("\r\n");
			for(int i=1;i<selectedLines.size()-1;i++){
				sb.append(selectedLines.get(i)).append("\r\n");
			}
			sb.append(selectedLines.get(selectedLines.size()-1).substring(0, last.x));
			return sb.toString();
		}
	}

	public boolean hasSelection(){
		return !selectionStart.equals(cursorPosition);
	}

	public void clearSelection(){
		selectionStart=selection().getLeft();
	}
	
	public void deleteSelected(){
		if(!hasSelection())return;
		
		Pair<Vec2i, Vec2i> selection=selection();
		Vec2i first=selection.getLeft();
		Vec2i last=selection.getRight();
		
		StringBuilder firstLine=textBuffer.get(first.y);
		if(first.y==last.y){
			firstLine.delete(first.x, last.x);
		}else{
			firstLine.delete(first.x, getLength(first.y));
		}
		
		refreshLine(first.y);
		setCursorPositionInternal(first);
		
		if(first.y==last.y){
			refreshWidth();
			return;
		}
		
		int numLines=last.y-first.y;
		StringBuilder lastLine=getLine(first.y+numLines);
		if(last.x<lastLine.length()-1){
			firstLine.append(lastLine.substring(last.x, lastLine.length()));
			refreshLine(first.y);
		}

		for(int i=0;i<numLines;i++){
			removeLine(first.y+1);
		}
	}
	
	public void append(String text){
		insert(text, getLastPosition());
	}
	
	public void insert(String text){
		insert(text, cursorPosition, true);
	}
	
	public void insert(String text, Vec2i pos){
		insert(text, cursorPosition, false);
	}
	
	protected void insert(String text, Vec2i pos, boolean advanceCursor){
		clearSelection();
		if(text==null)throw new NullPointerException();
		checkInside(pos);
		
		String[] toInsert=stringNewlineSplit(text.replaceAll("\t", "    "));
		if(text.equals("\n"))toInsert=new String[]{"\n"};
		StringBuilder insertLine=getLine(pos.y);
		String endOfLine=insertLine.substring(pos.x, insertLine.length());
		insertLine.delete(pos.x, insertLine.length()); 
		insertLine.append(toInsert[0]);
 		
		if(toInsert.length>1){
			if(toInsert.length>2)for(int i=1;i<toInsert.length-1;i++)
				addLine(new StringBuilder(toInsert[i]));
			addLine(new StringBuilder(toInsert[toInsert.length-1]).append(endOfLine),toInsert.length-1);
			
			if(advanceCursor){
				int newCursorY=cursorPosition.y+toInsert.length-1;
				setCursorPositionInternal(new Vec2i(Math.min(toInsert[toInsert.length-1].length(), getLength(newCursorY)), newCursorY));
			}
		}else{
			if(advanceCursor)setCursorPositionInternal(new Vec2i(Math.min(cursorPosition.x+toInsert[0].length(), getLength(cursorPosition.y)), cursorPosition.y));
			insertLine.append(endOfLine);
		}
//		int maxSpaces=Integer.MAX_VALUE;
		//next line magic
		for(int i=0;i<textBuffer.size();i++){
			{
				final String s=textBuffer.get(i).toString();
				if(s.equals("\n")){
					setLine(new StringBuilder(""),i);
					addLine(new StringBuilder(""),i+1);
				}
				else if(s.contains("\n")){
					
					int spacesAtTheBeginning=0;
					String spaces="";
					for(int j=0;j<s.length();j++){
						if(s.charAt(j)==' '){
							spacesAtTheBeginning=j;
							spaces+=" ";
						}
						else continue;
					}
					
					String[] lines=s.split("\n");
					setLine(new StringBuilder(lines[0]),i);
					for(int j=1;j<lines.length;j++)addLine(new StringBuilder(lines[j]),i+1);
					if(s.endsWith("{\n")){
						addLine(new StringBuilder("}"),i+1);
						addLine(new StringBuilder("    "),i+1);
					}
					else if(s.endsWith("\n"))addLine(new StringBuilder(spaces),i+1);
					cursorPosition.y++;
					if(getCurrentLine().toString().trim().length()==0)cursorPosition.x=getCurrentLine().length();
					else cursorPosition.x=0;
				}
			}
		}
		refreshLine(pos.y);
		refreshWidth();
		for(int i:cachedWidth){
			maxWidth=Math.max(maxWidth, i);
		}
	}
	
	public void replace(String text){
		deleteSelected();
		insert(text);
	}
	
	public void setCursorPos(Vec2i position){
		if(position.x<0||position.y<0)
			throw new IndexOutOfBoundsException();
		if(position.y>textBuffer.size()){
			position=getLastPosition();
		}else if(position.x>getLength(position.y)){
			position=new Vec2i(getLength(position.y), position.y);
		}
		setCursorPositionInternal(position);
	}
	
	protected void setCursorPositionInternal(Vec2i position){
		this.cursorPosition=position;
		this.selectionStart=position;
	}

	public GuiTextArea setText(String text){
		setCursorPositionInternal(Vec2i.zero);
		textBuffer.clear();
		cachedWidth.clear();
		addLine(new StringBuilder());
		append(text);
		setCursorPositionInternal(getLastPosition());
		return this;
	}
	private static String newline=System.getProperty("line.separator");
	public String getText(){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<textBuffer.size();i++){
			sb.append(textBuffer.get(i));
			if(i!=textBuffer.size()-1)sb.append(newline);
		}
		return sb.toString();
	}

	protected void checkInside(Vec2i pos){
		if(pos.x<0||pos.y<0||pos.y>=textBuffer.size())throw new IndexOutOfBoundsException();
		if(pos.x>getLength(pos.y))throw new IndexOutOfBoundsException();
	}
	public static void drawRect(int x1, int y1, int x2, int y2, int color){
		if(x1>x2){
			int t=x1;
			x1=x2;
			x2=t;
		}
		if(y1>y2){
			int t=y1;
			y1=y2;
			y2=t;
		}
		GL11U.color(codeToColorF(color));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		Drawer.startDrawingQuads();
		Drawer.addVertex(x1, y2, 0);
		Drawer.addVertex(x2, y2, 0);
		Drawer.addVertex(x2, y1, 0);
		Drawer.addVertex(x1, y1, 0);
		Drawer.draw();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}
	private static final Field f_posX=ReflectionHelper.findField(FontRenderer.class, "posX", "field_78295_j");
	private static final Field f_posY=ReflectionHelper.findField(FontRenderer.class, "posY", "field_78296_k");
	private static final Method m_renderStringAtPos=ReflectionHelper.findMethod(FontRenderer.class, null, new String[] { "renderStringAtPos", "func_78255_a" }, String.class, boolean.class);

	public static void drawString(String s, int x, int y, boolean shadow){
		try{
			f_posX.set(Font.FR(), x);
			f_posY.set(Font.FR(), y);
			m_renderStringAtPos.invoke(Font.FR(), s, shadow);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static class UndoSave{
		public String content;
		public Vec2i cursor;
		public UndoSave(String content, Vec2i cursor){
			this.content=content;
			this.cursor=cursor;
		}
		
	}
	@Override
	public void update(){
		if((Mouse.isButtonDown(0)?size.y-mouseClick.y<=8:false)||size.y-mouse.y<=8){
			if(Mouse.isButtonDown(0))sliderXCol.wantedPoint=1;
			else sliderXCol.wantedPoint=0.5F;
		}
		else sliderXCol.wantedPoint=0.2F;
		sliderXCol.update();
	}
}