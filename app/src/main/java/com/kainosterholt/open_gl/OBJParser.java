package com.kainosterholt.open_gl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import android.content.res.AssetManager;
import android.util.Log;

public class OBJParser {

    public static final boolean LOGGING_ENABLED = false;

	int numVertices=0;
	int numFaces=0;
	IAssetManager mAssetManager;

	Vector<Short> faces=new Vector<Short>();
	Vector<Short> vtPointer=new Vector<Short>();
	Vector<Short> vnPointer=new Vector<Short>();
	Vector<Float> v=new Vector<Float>();
	Vector<Float> vn=new Vector<Float>();
	Vector<Float> vt=new Vector<Float>();
	Vector<TDModelPart> parts=new Vector<TDModelPart>();

	public OBJParser(IAssetManager assetManager){
		mAssetManager = assetManager;
	}

	public TDModel parseOBJ(String fileName) {
		BufferedReader reader=null;
		String line = null;

		try
        { //try to open file
            AssetManager am = mAssetManager.getContext().getAssets();
            InputStream in_s = am.open(fileName);
			reader = new BufferedReader(new InputStreamReader(in_s));
		}
		catch(IOException e){
		}
		try {//try to read lines of the file
			while((line = reader.readLine()) != null)
			{
				if( LOGGING_ENABLED ) { Log.v("obj",line); }
				if(line.startsWith("f"))
                {
					processFLine(line);
				}
				else if(line.startsWith("vn"))
				{
                    processVNLine(line);
				}
                else if(line.startsWith("vt"))
                {
                    processVTLine(line);
                }
				else if(line.startsWith("v"))
                {
                    processVLine(line);
                }
			}
		} 		
		catch(IOException e){
			System.out.println("wtf...");
		}
		TDModel t=new TDModel(v, vn, vt, faces, vnPointer, vtPointer);
		if (LOGGING_ENABLED) { Log.v("models",t.toString()); };
		return t;
	}


	private void processVLine(String line){
		String [] tokens=line.split("[ ]+"); //split the line at the spaces
		int c=tokens.length; 
		for(int i=1; i<c; i++){ //add the vertex to the vertex array
			v.add(Float.valueOf(tokens[i]));
		}
	}
	private void processVNLine(String line){
		String [] tokens=line.split("[ ]+"); //split the line at the spaces
		int c=tokens.length; 
		for(int i=1; i<c; i++){ //add the vertex to the vertex array
			vn.add(Float.valueOf(tokens[i]));
		}
	}
	private void processVTLine(String line){
		String [] tokens=line.split("[ ]+"); //split the line at the spaces
		int c=tokens.length; 
		for(int i=1; i<c; i++){ //add the vertex to the vertex array
			vt.add(Float.valueOf(tokens[i]));
		}
	}
	private void processFLine(String line){
		String [] tokens=line.split("[ ]+");
		int c=tokens.length;

		if(tokens[1].matches("[0-9]+")){//f: v
			if(c==4) {//3 faces
                for (int i = 1; i < c; i++) {
                    Short s = Short.valueOf(tokens[i]);
                    s--;
                    faces.add(s);
                }
            }
		}
		if(tokens[1].matches("[0-9]+/[0-9]+")){//if: v/vt
			if(c==4){//3 faces
				for(int i=1; i<c; i++){
					Short s=Short.valueOf(tokens[i].split("/")[0]);
					s--;
					faces.add(s);
					s=Short.valueOf(tokens[i].split("/")[1]);
					s--;
					vtPointer.add(s);
				}
			}
		}
		if(tokens[1].matches("[0-9]+//[0-9]+")){//f: v//vn
			if(c==4){//3 faces
				for(int i=1; i<c; i++){
					Short s=Short.valueOf(tokens[i].split("//")[0]);
					s--;
					faces.add(s);
					s=Short.valueOf(tokens[i].split("//")[1]);
					s--;
					vnPointer.add(s);
				}
			}
		}
		if(tokens[1].matches("[0-9]+/[0-9]+/[0-9]+")){//f: v/vt/vn

			if(c==4){//3 faces
				for(int i=1; i<c; i++){
					Short s=Short.valueOf(tokens[i].split("/")[0]);
					s--;
					faces.add(s);
					s=Short.valueOf(tokens[i].split("/")[1]);
					s--;
					vtPointer.add(s);
					s=Short.valueOf(tokens[i].split("/")[2]);
					s--;
					vnPointer.add(s);
				}
			}
		}
	}

}

