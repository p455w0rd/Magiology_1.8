package com.magiology.client.gui.guiutil.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import javax.script.Invocable;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.lang.program.ProgramCommon;
import com.magiology.api.lang.program.ProgramUsable;
import com.magiology.client.render.font.FontRendererMClipped;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexModel;
import com.magiology.util.utilclasses.LogUtil;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.m_extension.BlockPosM;
import com.magiology.util.utilobjects.vectors.Vec2i;


public class GuiJavaScriptEditor extends GuiTextEditor{
	
	public boolean colors=false;
	
	protected int noChangeCompileTime=0,comileWaitAmmount=20;
	
	protected List<ErrorMarker> errors=new ArrayList<>();
	
	public BlockPosM runPos;
	
	public Object[] runArgs={};
	
	public GuiJavaScriptEditor(Vec2i pos, Vec2i size){
		super(pos, size);
	}
	
	private List<DoubleObject<List<String>,List<Integer>>> coloredText;
	private String selectedWord="";
	private VertexModel selection=null;
	protected float xOffset, yOffset;
	
	@Override
	protected void rednerText(FontRendererMClipped fr){
		xOffset=-sliderX*(maxWidth-size.x);
		yOffset=-sliderY*((textBuffer.size()+1)*fr.FONT_HEIGHT-size.y);
		if(colors){
			if(coloredText==null)colorCode(fr);
			rednerColoredCode(fr);
		}
		else super.rednerText(fr);
		errors.forEach(error->error.draw(mouse.x, mouse.y));
	}
	@Override
	public boolean keyTyped(int code, char ch){
		boolean result=super.keyTyped(code, ch);
		colorCode(FontRendererMClipped.get());
		return result;
	}
	@Override
	public void mouseClicked(int x, int y, int button){
		super.mouseClicked(x, y, button);
		colorCode(FontRendererMClipped.get());
	}
	@Override
	public void mouseClickMove(int x, int y){
		super.mouseClickMove(x, y);
		colorCode(FontRendererMClipped.get());
	}
	@Override
	public void update(){
		super.update();
		if(noChangeCompileTime>0){
			noChangeCompileTime--;
			if(noChangeCompileTime==0){
				compile();
			}
		}
	}
	public void compile(){
		noChangeCompileTime=comileWaitAmmount;
		errors.clear();
		
		try{
			Invocable program=ProgramUsable.compile(getText());
			List<CharSequence> log=new ArrayList<>();
			ProgramUsable.run(program, log, "main", runArgs==null||runArgs.length==0?new Object[]{"undefined"}:runArgs, new Object[]{UtilM.getTheWorld(),runPos});
		}catch(ScriptException e){
			try{
				readExceptions(e);
			}catch(Exception e2){
				e2.printStackTrace();
			}
		}
	}
	
	private void readExceptions(Exception e){
		String s=e.getMessage();
		
		if(s.startsWith("ReferenceError: ")){
			String invalidReference=s.substring("ReferenceError: \"".length(), s.length());
			invalidReference=invalidReference.substring(0, invalidReference.indexOf('"'));
			
			int line=Integer.parseInt(s.substring(s.indexOf("line number ")+"line number ".length()))-ProgramUsable.jsJavaBridgeLines-1;
			int colum=0;
			
			StringBuilder word=new StringBuilder();
			for(char c:getLine(line).toString().toCharArray()){
				if(Character.isJavaIdentifierPart(c))word.append(c);
				else{
					int lenght=word.length();
					if(lenght>0){
						if(word.toString().equals(invalidReference))break;
						else colum+=lenght;
						word=new StringBuilder();
					}
					colum++;
				}
			}
			
			errors.add(new ErrorMarker(new Vec2i(colum, line), invalidReference.length(),s));
			return;
		}
		
		if(s.startsWith("<eval>:")){
			s=s.substring("<eval>:".length(),s.indexOf('\n')-1);
			
			int line=Integer.parseInt(s.substring(0,s.indexOf(":")))-ProgramUsable.jsJavaBridgeLines-1;
			int colum=Integer.parseInt(s.substring(s.indexOf(":")+1,s.indexOf(" Exp")));
			String found=s.substring(s.indexOf("but found ")+"but found ".length());
			
			errors.add(new ErrorMarker(new Vec2i(colum, line), found.length(),s.substring(s.indexOf("Expe"))));
			return;
		}
		if(s.startsWith("No such function")){
			errors.add(new ErrorMarker(new Vec2i(0, textBuffer.size()-1), 1,s));
			return;
		}
		PrintUtil.println(s);
	}
	
	protected class ErrorMarker{
		public final Vec2i errorPos;
		public final int errorLenght;
		public final String message;
		public int timeSelected;
		
		public ErrorMarker(Vec2i pos, int lenght, String message){
			Vec2i errorPos=new Vec2i(0, (pos.y+1)*9-1);
			int errorLenght=1;
			try{
				String line=getLine(pos.y).toString();
				
				errorPos.x=FontRendererMClipped.get().getStringWidth(line.substring(0, pos.x))-1;
				errorLenght=FontRendererMClipped.get().getStringWidth(line.substring(pos.x,pos.x+lenght))+2;
			}catch(Exception e){
				errorLenght=Math.max(FontRendererMClipped.get().getStringWidth(getLine(pos.y).toString())+2,10);
			}
			
			this.errorPos=errorPos.add(GuiJavaScriptEditor.this.pos);
			this.errorLenght=errorLenght;
			this.message=message;
		}
		
		public void draw(int x, int y){
			
			Vector2f 
				pos1=new Vector2f(xOffset+errorPos.x,yOffset+errorPos.y),
				pos2=new Vector2f(xOffset+errorPos.x+errorLenght,yOffset+errorPos.y);
			
			if(pos1.y<pos.y-3||pos1.y>pos.y+3+size.y)return;
			
			float minX=pos.x-3,maxX=pos.x+3+size.x;
			pos1.x=UtilM.snap(pos1.x, minX, maxX);
			pos2.x=UtilM.snap(pos2.x, minX, maxX);
			if(pos1.x==pos2.x)return;
			
			OpenGLM.disableTexture2D();
			ColorF.RED.bind();
			Renderer.LINES.begin();
			Renderer.LINES.addVertex(pos1.x+UtilM.CRandF(1),pos1.y+UtilM.CRandF(1),0);
			Renderer.LINES.addVertex(pos2.x+UtilM.CRandF(1),pos2.y+UtilM.CRandF(1),0);
			Renderer.LINES.draw();
			ColorF.WHITE.bind();
			OpenGLM.enableTexture2D();
		}
		
		public boolean isSelected(int x, int y){
			return false;
		}
		
		@Override
		public String toString(){
			return new StringBuilder().append("ErrorMarker[errorPos = ").append(errorPos).append(", errorLenght = ").append(errorLenght).append(", message = \"").append(message).append('"').append("]").toString();
		}
	}
	
	private int level=0;
	protected List<String> 
		strings=new ArrayList<String>(),
		statics=new ArrayList<String>(),
		classes=new ArrayList<String>(),
		nubers=new ArrayList<String>();
	protected Map<int[],String> 
		vars=new HashMap<int[],String>(),
		unfinishedVars=new HashMap<int[],String>();
	
	private void colorCode(FontRendererMClipped fr){
		coloredText=new ArrayList<DoubleObject<List<String>,List<Integer>>>();
		if(statics.isEmpty()){
			statics.add("runPos");
			classes.add("World");
			classes.add("Math");
			classes.add("BlockPos");
			classes.add("EnumFacing");
		}
		for(int i=0;i<textBuffer.size();i++)coloredText.add(colorLine(fr,i));
		TessUtil.getVB().cleanUp();
		highlightWords(fr);
		selection=TessUtil.getVB().exportToNormalisedVertexBufferModel();
		selection.setInstantNormalCalculation(false);
		selection.translate(-0.5, 0, 0);
		selection.setClearing(false);
		resetColorData();
		noChangeCompileTime=comileWaitAmmount;
	}
	private void highlightWords(FontRendererMClipped fr){
		if(!isSelected()){
			StringBuilder line=getLine(getCursorPosition().y);
			if(line.length()>0){
				Matcher 
					forward=word.matcher(line),
					backward=word.matcher(new StringBuilder(line).reverse());
				
				selectedWord=line.substring(
					backward.find(line.length()-getCursorPosition().x)?line.length()-backward.start():0, 
					forward.find(getCursorPosition().x)?forward.start():getLength(getCursorPosition().y)
				);
			}
		}else selectedWord=getSelectedText();
		
		
		char[] chars=selectedWord.toCharArray();
		boolean valid=vars.containsValue(selectedWord)||statics.contains(selectedWord);
		
		if(valid)for(char c:chars){
			if(!Character.isJavaIdentifierPart(c)){
				valid=false;
				break;
			}
		}
		if(valid){
			int selectedWordLenght=selectedWord.length();
			if(!selectedWord.isEmpty()){
				for(int i=0;i<coloredText.size();i++){
					String line=getLine(i).toString();
					if(line.contains(selectedWord)){
						int count=0;
						do{
							count++;
							int 
								start=line.indexOf(selectedWord),
								end=start+selectedWordLenght;
							if((start==0?true:!Character.isJavaIdentifierPart(line.charAt(start-1)))&&(end==line.length()?true:!Character.isJavaIdentifierPart(line.charAt(end)))){
								int
									offset=fr.getStringWidth(line.substring(0, start)),
									lenght=fr.getStringWidth(line.substring(start, end)),
									minX=pos.x+offset-1,
									minY=pos.y+i*fr.FONT_HEIGHT-1,
									maxX=minX+lenght+2,
									maxY=minY+11;
								
								TessUtil.getVB().addVertex(minX, maxY, 0);
								TessUtil.getVB().addVertex(maxX, maxY, 0);
								TessUtil.getVB().addVertex(maxX, minY, 0);
								TessUtil.getVB().addVertex(minX, minY, 0);
							}
							line=line.substring(end);
						}while(line.contains(selectedWord)&&count<100);
						if(count==100)LogUtil.error("Word highlight error!");
					}
				}
			}
		}
	}
	protected void rednerColoredCode(FontRendererMClipped fr){
		if(selection!=null){
			GL11U.texture(false);
			selection.pushMatrix();
			selection.translate(xOffset, yOffset, 0);
			OpenGLM.color(0, 0, 0, 1);
			selection.draw();
			
			OpenGLM.color(0.2F, 0.2F, 0.2F, 1);
			selection.setDrawAsWire(true);
			selection.draw();
			selection.setDrawAsWire(false);
			
			OpenGLM.color(1, 1, 1, 1);
			selection.popMatrix();
			GL11U.texture(true);
		}
		for(int i=0;i<coloredText.size();i++){
			int offset=0;
			List<String> line=coloredText.get(i).obj1;
			List<Integer> colors=coloredText.get(i).obj2;
			for(int j=0;j<line.size();j++){
				fr.min=new Vector2f(pos.x-2,pos.y-2);
				fr.max=new Vector2f(pos.x+size.x+2,pos.y+size.y+2);
				String lin=line.get(j);
				fr.drawString(line.get(j), pos.x+offset, pos.y+i*fr.FONT_HEIGHT, colors.get(j));
				offset+=fr.getStringWidth(lin);
			}
		}
	}
	protected void resetColorData(){
		strings=new ArrayList<String>();
		statics=new ArrayList<String>();
		classes=new ArrayList<String>();
		nubers=new ArrayList<String>();
		vars=new HashMap<int[],String>();
		unfinishedVars=new HashMap<int[],String>();
		statics.add("runPos");
		classes.add("World");
		classes.add("Math");
		classes.add("BlockPos");
		classes.add("EnumFacing");
		level=0;
	}
	private DoubleObject<List<String>,List<Integer>> colorLine(FontRendererMClipped fr,int i){
		int cyan=0x00D0D0,red=0xED7072,yellow=0xFFFF00,blue=0x4994FF,orange=0xEFC090,purple=0xDC78A4;
		List<String> line=new ArrayList<String>();
		List<Integer> colors=new ArrayList<Integer>();
		line.add(textBuffer.get(i).toString());
		colors.add(0xBED6FF);
		String 
			raw=ProgramCommon.removeSpacesFrom(textBuffer.get(i).toString(),'{','}','(',')','=',';','*','/','+','-','%','!','>','<','@','#'),
			words[]=raw.split("((?<=\\W)|(?=\\W))");
		boolean varFound=false,functionFound=false,functionStarted=false;
		for(int k=0;k<words.length;k++){
			String word=words[k];
			word=word.replaceAll(" ", "");
			if(!word.isEmpty()){
				if(word.equals("{")){
					level++;
				}else if(word.equals("}")){
					level--;
					List<int[]> varToRemove=new ArrayList<int[]>();
					for(Entry<int[],String> var:unfinishedVars.entrySet()){
						if(var.getKey()[0]>level){
							vars.put(new int[]{var.getKey()[0],var.getKey()[1],i}, var.getValue());
							varToRemove.add(var.getKey());
						}
					}
					for(int[] key:varToRemove)unfinishedVars.remove(key);
				}else{
					if(varFound){
						varFound=false;
						if(level>0)unfinishedVars.put(new int[]{level,i}, word);
						else statics.add(word);
					}
					if(functionStarted&&!word.contains("(")){
						functionStarted=false;
						
						int level=this.level+1;
						boolean prevValid=false;
						while(k<words.length&&!words[k].contains(")")&&!words[k].contains("{")&&!words[k].contains("}")&&!words[k].contains("var")&&!words[k].contains("=")){
							if(!words[k].contains(" ")){
								boolean valid=true;
								for(char c:words[k].toCharArray()){
									if(!Character.isJavaIdentifierPart(c)){
										valid=false;
										break;
									}
								}
								if(prevValid==valid)break;
								prevValid=valid;
								if(valid)unfinishedVars.put(new int[]{level,i}, words[k]);
							}
							k++;
						}
					}
					if(functionFound){
						functionFound=false;
						functionStarted=true;
					}
					if(word.equals("var"))varFound=true;
					if(word.equals("function"))functionFound=true;
					else{
						try{
							Float.parseFloat(word);
							nubers.add(word);
						}catch(Exception e){}
					}
				}
			}
			String rawLine=textBuffer.get(i).toString();
			if(rawLine.contains("\"")&&StringUtils.countMatches(rawLine, "\"")%2==0){
				boolean in=false;
				char[] cs=rawLine.toCharArray();
				for(int j=0;j<cs.length;j++){
					if(cs[j]=='"'){
						in=!in;
						if(in){
							int start=j,end=-1;
							j++;
							while(cs[j]!='"'&&j<cs.length)j++;
							end=j;
							in=!in;
							if(end+1-start>0)strings.add(rawLine.substring(start, end+1));
						}
					}
				}
			}
			colorKeyWord(line, colors, "var", cyan);
			colorKeyWord(line, colors, "return", cyan);
			colorKeyWord(line, colors, "function", cyan);
			colorKeyWord(line, colors, "true", cyan);
			colorKeyWord(line, colors, "false", cyan);
			colorKeyWord(line, colors, "for", cyan);
			colorKeyWord(line, colors, "if", cyan);
			colorKeyWord(line, colors, "while", cyan);
			colorKeyWord(line, colors, "new", cyan);
		}
		//color words
		ArrayList<Entry<int[], String>> vars1=new ArrayList();
		vars1.addAll(vars.entrySet());
		vars1.addAll(unfinishedVars.entrySet());
		for(Entry<int[],String> word:vars1){
			String keyWord=word.getValue();
			int keyWordLenght=keyWord.length();
			for(int j=0;j<line.size();j++){
				String part=line.get(j);
				boolean onlyVar=line.size()==1&&line.get(0).equals(keyWord);
				if(part.contains(keyWord)&&(onlyVar||!part.equals(keyWord))){
					if(level>=word.getKey()[0]&&i>=word.getKey()[1]&&(word.getKey().length>2?i<=word.getKey()[2]:true)){
						int keyWordStart=part.indexOf(keyWord);
						String 
							rawLine=UtilM.join(line.toArray()),
							beforeKeyWord=part.substring(0, keyWordStart),
							KeyWord=part.substring(keyWordStart, keyWordStart+keyWordLenght),
							aferKeyWord=part.substring(keyWordStart+keyWordLenght, part.length());
						boolean shouldColor=true;
						if(rawLine.contains("\"")){
							int 
								before=StringUtils.countMatches(beforeKeyWord, "\""),
								after=StringUtils.countMatches(aferKeyWord, "\"");
							if(before%2!=0&&after%2!=0){
								shouldColor=false;
							}
						}
						if(shouldColor){
							line.set(j, aferKeyWord);
							line.add(j, KeyWord);
							colors.add(j,blue);
							line.add(j, beforeKeyWord);
							colors.add(j,0xBED6FF);
							j--;
						}
					}
				}
			}
		}
		for(String word:nubers)colorKeyWord(line, colors, word, yellow);
		for(String word:classes)colorKeyWord(line, colors, word, red);
		for(String word:statics)colorKeyWord(line, colors, word, orange);
		for(String word:strings)colorKeyWord(line, colors, word, purple);
		
		return new DoubleObject<List<String>,List<Integer>>(line, colors);
	}
	
	private void colorKeyWord(List<String> line, List<Integer> colors,String keyWord,int color){
		int keyWordLenght=keyWord.length();
		for(int j=0;j<line.size();j++){
			String part=line.get(j);
			boolean onlyVar=line.size()==1&&line.get(0).equals(keyWord);
			
			String rawLine=UtilM.join(line.toArray());
			if(part.contains(keyWord)&&(onlyVar||!part.equals(keyWord))){
				int keyWordStart=part.indexOf(keyWord);
				String 
					beforeKeyWord=part.substring(0, keyWordStart),
					KeyWord=part.substring(keyWordStart, keyWordStart+keyWordLenght),
					aferKeyWord=part.substring(keyWordStart+keyWordLenght, part.length());
				boolean shouldColor=true;
				if(rawLine.contains("\"")){
					int 
						before=StringUtils.countMatches(beforeKeyWord, "\""),
						after=StringUtils.countMatches(aferKeyWord, "\"");
					if(before%2!=0&&after%2!=0){
						shouldColor=false;
					}
				}
				if(shouldColor){
					line.set(j, aferKeyWord);
					line.add(j, KeyWord);
					colors.add(j,color);
					line.add(j, beforeKeyWord);
					colors.add(j,0xBED6FF);
					j--;
				}
			}
		}
	}
}
