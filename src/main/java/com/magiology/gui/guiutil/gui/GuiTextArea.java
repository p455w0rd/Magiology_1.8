package com.magiology.gui.guiutil.gui;

import java.awt.Color;
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
import org.lwjgl.opengl.GL11;

import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.tessellatorscripts.Drawer;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.Util;
import com.magiology.util.utilobjects.vectors.Vec2i;

public class GuiTextArea extends Gui{
	
	private List<StringBuilder> textBuffer=new ArrayList<StringBuilder>();
	private List<Integer> cachedWidth=new ArrayList<Integer>();
	
	protected Vec2i cursorPosition=Vec2i.zero;
	protected Vec2i selectionStart=Vec2i.zero;
	protected int xPos, yPos;
	protected int width;
	
	protected boolean active=true;
	protected boolean visible=true;
	
	protected int backgroundColor;
	protected int textColor=0xFFFFFFFF;
	protected int cursorColor=0xFFFFFFFF;
	protected int border=5;
	
	protected FontRenderer fr=Get.Render.FR();

	public GuiTextArea(int xPos, int yPos){
		textBuffer.add(new StringBuilder());
		cachedWidth.add(0);
		this.xPos=xPos;
		this.yPos=yPos;
	}

	public Vec2i getSize(){
		return new Vec2i(width+border*2, textBuffer.size()*fr.FONT_HEIGHT+border*2);
	}

	public void render(int mx, int my){
		if(!isVisible())return;
		if(backgroundColor!=0){
			Vec2i size=getSize();
			drawRect(xPos, yPos, xPos+size.x, yPos+size.y, backgroundColor);
		}
		
		if(isActive()&&hasSelection()){
			renderSelection(0xFFDFB578);
		}
		
		fr.drawString("", 0, 0, Color.WHITE.hashCode());
		for(int i=0;i<textBuffer.size();i++){
			drawStringNoReset(fr, textBuffer.get(i).toString(), xPos+border, yPos+i*fr.FONT_HEIGHT+border, false);
		}
		
		// Selection
		if(isActive()&&hasSelection()){
			GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ZERO);
			renderSelection(0xFFFFFFFF);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		
		// Cursor
		if(isActive()&&Get.Render.partialTicks/6%2==0){
			int cursorX=xPos+fr.getStringWidth(getCurrentLine().substring(0, cursorPosition.x));
			int cursorY=yPos+cursorPosition.y*fr.FONT_HEIGHT;

			drawVerticalLine(cursorX+border, cursorY-2+border, cursorY+10+border, cursorColor);
		}
	}

	private void renderSelection(int color){
		Pair<Vec2i, Vec2i> selection=selection();
		Vec2i first=selection.getLeft();
		Vec2i last=selection.getRight();

		int xPos=this.xPos+border;
		int yPos=this.yPos+border;

		if(first.y==last.y){
			int x1=fr.getStringWidth(getLine(first.y).substring(0, first.x));
			int x2=fr.getStringWidth(getLine(first.y).substring(0, last.x));
			drawRect(xPos+x1, yPos+first.y*fr.FONT_HEIGHT, xPos+x2, yPos+(first.y+1)*fr.FONT_HEIGHT,color);
		}else{
			int x1=fr.getStringWidth(getLine(first.y).substring(0, first.x));
			int x2=cachedWidth.get(first.y);
			drawRect(xPos+x1, yPos+first.y*fr.FONT_HEIGHT, xPos+x2, yPos+(first.y+1)*fr.FONT_HEIGHT, color);
			for(int i=0;i<last.y-first.y-1;i++){
				x2=cachedWidth.get(first.y+i+1);
				drawRect(xPos, yPos+(first.y+i+1)*fr.FONT_HEIGHT, xPos+x2, yPos+(first.y+i+2)*fr.FONT_HEIGHT, color);
			}

			x2=fr.getStringWidth(getLine(last.y).substring(0, last.x));
			drawRect(xPos, yPos+last.y*fr.FONT_HEIGHT, xPos+x2, yPos+(last.y+1)*fr.FONT_HEIGHT, color);
		}
	}

	private int clickCount=0;
	private long lastClickTime=Long.MAX_VALUE;
	private static final int CLICK_TIME=200;

	public void onMouseDown(int mx, int my, int button){
		if(!isVisible()||button!=0)return;
		
		Vec2i intersection=intersect(mx, my);
		if(intersection==null){
			setActive(false);
			return;
		}

		long time=System.currentTimeMillis();
		if(time-lastClickTime>CLICK_TIME){
			clickCount=0;
		}
		lastClickTime=time;

		setActive(true);

		if(clickCount==0){
			setCursorPositionInternal(intersection);
		}else if(clickCount==1){
			selectWord(intersection);
		}else if(clickCount==2){
			selectLine(intersection);
			clickCount=0;
		}

		clickCount++;
	}

	private static final Pattern patternWord=Pattern.compile("\\b");

	private void selectWord(Vec2i intersection){
		StringBuilder line=getLine(intersection.y);

		Matcher forward=patternWord.matcher(line);
		Matcher backward=patternWord.matcher(new StringBuilder(line).reverse());

		int next=forward.find(intersection.x) ? forward.start():getLineLength(intersection.y);
		int prev=backward.find(line.length()-intersection.x) ? line.length()-backward.start():0;

		selectionStart=new Vec2i(prev, intersection.y);
		cursorPosition=new Vec2i(next, intersection.y);
	}

	private void selectLine(Vec2i intersection){
		selectionStart=new Vec2i(0, intersection.y);
		if(intersection.y+1>=textBuffer.size()){
			cursorPosition=new Vec2i(getLineLength(intersection.y), intersection.y);
		}else{
			cursorPosition=new Vec2i(0, intersection.y+1);
		}
	}

	public void onMouseDragged(int mx, int my){
		if(!isVisible()||!isActive())
			return;

		Vec2i intersection=intersect(mx, my);
		if(intersection!=null){
			cursorPosition=intersection;
		}
	}

	public void onKeyTyped(int key, char ch){
		if(!isVisible()||!isActive())
			return;

		switch (key){
			case Keyboard.KEY_DELETE:
				delete();
				return;
			case Keyboard.KEY_UP:
				up();
				return;
			case Keyboard.KEY_DOWN:
				down();
				return;
			case Keyboard.KEY_LEFT:
				left();
				return;
			case Keyboard.KEY_RIGHT:
				right();
				return;
		}

		switch(ch){
			case 1: // ^A
				selectAll();
				return;
			case 3: // ^C
				GuiScreen.setClipboardString(getSelectedText());
				return;
			case 22: // ^V
				replace(GuiScreen.getClipboardString());
				return;
			case 24: // ^X
				GuiScreen.setClipboardString(getSelectedText());
				deleteSelected();
				return;
			case 8: // backspace
				backspace();
				return;
			case 27: // ESC
				setActive(false);
				return;
			case 13: // CR
				carriageReturn();
				return;
			case '\t':
				tab();
				return;
			default:
				if(!Character.isISOControl(ch)){
					replace(String.valueOf(ch));
				}
				return;
		}
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
			removeFromCache(cursorPosition.y);
			Vec2i newCursorPosition=new Vec2i(getLineLength(cursorPosition.y-1), cursorPosition.y-1);
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
		if(cursorPosition.x>=getLineLength(cursorPosition.y)){
			StringBuilder line=getLine(cursorPosition.y+1);
			removeFromCache(cursorPosition.y+1);
			getLine(cursorPosition.y).append(line);
			refreshLine(cursorPosition.y);
		}else{
			getCurrentLine().deleteCharAt(cursorPosition.x);
			refreshLine(cursorPosition.y);
			refreshWidth();
		}
	}
	
	private void carriageReturn(){
		if(GuiScreen.isShiftKeyDown()){
			deleteSelected();

			StringBuilder sb=new StringBuilder();
			String currentLine=getCurrentLine().toString();
			if(currentLine.length()>0){
				// Add spaces in front forindent
				int i=0;
				int size=currentLine.length();
				while (currentLine.charAt(i)==' '&&i<size){
					sb.insert(0, ' ');
					i++;
				}
			}

			addToCache(cursorPosition.y+1, sb);
			Vec2i newCursorPosition=new Vec2i(getLineLength(cursorPosition.y+1), cursorPosition.y+1);
			sb.append(currentLine.substring(cursorPosition.x, currentLine.length()));
			getCurrentLine().delete(cursorPosition.x, currentLine.length());

			refreshLine(cursorPosition.y);
			refreshLine(cursorPosition.y+1);
			refreshWidth();

			setCursorPositionInternal(newCursorPosition);
		}else{
			// un-select
			setActive(false);
		}
	}
	
	private static final Pattern patternBlankSpace=Pattern.compile("^\\s*$");
	private static final Pattern patternFirstNonWhitespace=Pattern.compile("[^\\s]|\\s$");

	private void tab(){
		
		if(GuiScreen.isShiftKeyDown()){
			// Got one line
			if(cursorPosition.y==selectionStart.y){
				clearSelection();
				if(patternBlankSpace.matcher(getCurrentLine().toString().substring(0, cursorPosition.x)).matches()){

					// We're starting with some whitespace, move forward
					Matcher matcher=patternFirstNonWhitespace.matcher(getCurrentLine());
					if(matcher.find()){
						setCursorPositionInternal(new Vec2i(matcher.start(), cursorPosition.y));
					}

					// Remove some spaces
					if(cursorPosition.x==1){
						// Got one to remove...
						getCurrentLine().deleteCharAt(cursorPosition.x-1);
						setCursorPositionInternal(new Vec2i(cursorPosition.x-1, cursorPosition.y));
					}else if(cursorPosition.x>0){
						// Got two to remove!
						getCurrentLine().delete(cursorPosition.x-2, cursorPosition.x);
						setCursorPositionInternal(new Vec2i(cursorPosition.x-2, cursorPosition.y));
					}
					refreshLine(cursorPosition.y);
				}else{
					// Move the cursor back
					setCursorPositionInternal(new Vec2i(Math.max(cursorPosition.x-2+cursorPosition.x % 2, 0), cursorPosition.y));
				}
			}else{
				Pair<Vec2i, Vec2i> selection=selection();
				// Tab multiple lines
				for(int i=selection.getLeft().y;i <= selection().getRight().y;i++){
					String line=getLine(i).toString();
					if(line.startsWith("  ")){
						getLine(i).delete(0, 2);
						refreshLine(i);
					}else if(line.startsWith(" ")){
						getLine(i).deleteCharAt(0);
						refreshLine(i);
					}
				}

				// Adjust selection
				tab_adjustSelection(selection);
			}
		}else{
			// Got one line
			if(cursorPosition.y==selectionStart.y){
				if(cursorPosition.x % 2==0)
					replace("  ");
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
					getLine(i).insert(0, "  ");
					refreshLine(i);
				}

				// Adjust selection
				tab_adjustSelection(selection);
			}
		}
		refreshWidth();
	}

	private void tab_adjustSelection(Pair<Vec2i, Vec2i> selection){
		boolean flip=cursorPosition==selection.getLeft();
		selectionStart=new Vec2i(0, selection.getLeft().y);
		cursorPosition=new Vec2i(getLineLength(selection.getRight().y), selection.getRight().y);

		if(flip){
			Vec2i temp=selectionStart;
			selectionStart=cursorPosition;
			cursorPosition=temp;
		}
	}

	private void up(){
		if(cursorPosition.y==0)
			return;

		int x=fr.getStringWidth(getCurrentLine().substring(0, cursorPosition.x));
		x=fr.trimStringToWidth(getLine(cursorPosition.y-1).toString(), x).length();
		setCursorPositionInternal(new Vec2i(x, cursorPosition.y-1));
	}

	private void down(){
		if(cursorPosition.y>=textBuffer.size()-1)
			return;

		int x=fr.getStringWidth(getCurrentLine().substring(0, cursorPosition.x));
		x=fr.trimStringToWidth(getLine(cursorPosition.y+1).toString(), x).length();
		setCursorPositionInternal(new Vec2i(x, cursorPosition.y+1));
	}

	private void left(){
		if(cursorPosition.equals(Vec2i.zero))
			return;
		if(cursorPosition.x==0){
			setCursorPositionInternal(new Vec2i(getLineLength(cursorPosition.y-1), cursorPosition.y-1));
		}else setCursorPositionInternal(new Vec2i(cursorPosition.x-1, cursorPosition.y));
	}

	private void right(){
		if(cursorPosition.equals(getLastPosition()))
			return;
		if(cursorPosition.x>=getLineLength(cursorPosition.y)){
			setCursorPositionInternal(new Vec2i(0, cursorPosition.y+1));
		}else{
			setCursorPositionInternal(new Vec2i(cursorPosition.x+1, cursorPosition.y));
		}
	}

	private Vec2i intersect(int mx, int my){
		mx-=border;
		my-=border;
		
		int y=(my-xPos)/fr.FONT_HEIGHT;
		if(y<0||y>=textBuffer.size())
			return null;
		int x=mx-xPos;
		if(x<-border)
			return null;
		if(x>width+border*2)
			return null;
		String s=fr.trimStringToWidth(textBuffer.get(y).toString(), mx-xPos);
		x=s.length();
		return new Vec2i(x, y);
	}

	protected StringBuilder getLine(int line){
		return textBuffer.get(line);
	}

	protected StringBuilder getCurrentLine(){
		return getLine(cursorPosition.y);
	}

	protected void refreshLine(int line){
		cachedWidth.set(line, fr.getStringWidth(textBuffer.get(line).toString()));
	}

	protected void refreshWidth(){
		width=0;
		for(int w:cachedWidth){
			if(w>width)width=w;
		}
	}

	protected void addToCache(int line, StringBuilder sb){
		textBuffer.add(line, sb);
		cachedWidth.add(line, 0);
		refreshLine(line);
		refreshWidth();
	}

	protected void addToCache(StringBuilder sb){
		textBuffer.add(sb);
		cachedWidth.add(0);
		refreshLine(textBuffer.size()-1);
		refreshWidth();
	}

	protected void removeFromCache(int line){
		textBuffer.remove(line);
		cachedWidth.remove(line);
		refreshWidth();
	}

	public int getLineLength(int line){
		return textBuffer.get(line).length();
	}

	public Vec2i getLastPosition(){
		return new Vec2i(textBuffer.get(textBuffer.size()-1).length(), textBuffer.size()-1);
	}

	public Vec2i getSelectionStart(){
		return selectionStart;
	}

	public Vec2i getCursorPosition(){
		return cursorPosition;
	}

	public boolean isActive(){
		return active;
	}

	public boolean isVisible(){
		return visible;
	}

	public GuiTextArea setActive(boolean active){
		this.active=active;
		return this;
	}

	public GuiTextArea setVisible(boolean visible){
		this.visible=visible;
		return this;
	}

	public GuiTextArea setPosition(int x, int y){
		this.xPos=x;
		this.yPos=y;
		return this;
	}

	public GuiTextArea setBackgroundColor(int color){
		this.backgroundColor=color;
		return this;
	}

	public GuiTextArea setTextColor(int color){
		this.textColor=color;
		return this;
	}

	public GuiTextArea setCursorColor(int color){
		this.cursorColor=color;
		return this;
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
			firstLine.delete(first.x, getLineLength(first.y));
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
			removeFromCache(first.y+1);
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
		
		String[] toInsert=Util.stringNewlineSplit(text.replaceAll("\t", "  "));
		StringBuilder insertLine=getLine(pos.y);
		String endOfLine=insertLine.substring(pos.x, insertLine.length());
		insertLine.delete(pos.x, insertLine.length());
		insertLine.append(toInsert[0]);
		
		if(toInsert.length>1){
			if(toInsert.length>2){
				for(int i=1;i<toInsert.length-1;i++){
					addToCache(new StringBuilder(toInsert[i]));
				}
			}
			addToCache(new StringBuilder(toInsert[toInsert.length-1]).append(endOfLine));
			
			if(advanceCursor){
				// TODO min shouldn't be needed but I've seen this crashing randomly
				int newCursorY=cursorPosition.y+toInsert.length-1;
				setCursorPositionInternal(new Vec2i(Math.min(toInsert[toInsert.length-1].length(), getLineLength(newCursorY)), newCursorY));
			}
		}else{
			if(advanceCursor)setCursorPositionInternal(new Vec2i(Math.min(cursorPosition.x+toInsert[0].length(), getLineLength(cursorPosition.y)), cursorPosition.y));
			insertLine.append(endOfLine);
		}
		refreshLine(pos.y);
		refreshWidth();
	}

	public void replace(String text){
		deleteSelected();
		insert(text);
	}

	public void setCursorPosition(Vec2i position){
		if(position.x<0||position.y<0)
			throw new IndexOutOfBoundsException();
		if(position.y>textBuffer.size()){
			position=getLastPosition();
		}else if(position.x>getLineLength(position.y)){
			position=new Vec2i(getLineLength(position.y), position.y);
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
		addToCache(new StringBuilder());
		append(text);
		setCursorPositionInternal(getLastPosition());
		return this;
	}

	public String getText(){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<textBuffer.size();i++){
			sb.append(textBuffer.get(i));
			if(i!=textBuffer.size()-1){
				sb.append("\r\n");
			}
		}
		return sb.toString();
	}

	protected void checkInside(Vec2i pos){
		if(pos.x<0||pos.y<0||pos.y>=textBuffer.size())throw new IndexOutOfBoundsException();
		if(pos.x>getLineLength(pos.y))throw new IndexOutOfBoundsException();
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

		GL11U.color(color);
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

	public static void drawStringNoReset(FontRenderer fontRenderer, String s, int x, int y, boolean shadow) {
		try{
			f_posX.set(fontRenderer, x);
			f_posY.set(fontRenderer, y);
			m_renderStringAtPos.invoke(fontRenderer, s, shadow);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}