package com.kainosterholt.open_gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

public class TDModelPart {
	Vector<Short> faces;
	Vector<Short> vtPointer;
	Vector<Short> vnPointer;
	private FloatBuffer normalBuffer;
    private FloatBuffer uvBuffer;
	private ShortBuffer faceBuffer;
	
	public TDModelPart(Vector<Short> faces, Vector<Short> vtPointer,
			Vector<Short> vnPointer, Vector<Float> vn, Vector<Float> vt) {
		super();
		this.faces = faces;
		this.vtPointer = vtPointer;
		this.vnPointer = vnPointer;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vnPointer.size() * 4*3);
		byteBuf.order(ByteOrder.nativeOrder());
		normalBuffer = byteBuf.asFloatBuffer();
		for(int i=0; i<vnPointer.size(); i++){
			float x=vn.get(vnPointer.get(i)*3);
			float y=vn.get(vnPointer.get(i)*3+1);
			float z=vn.get(vnPointer.get(i)*3+2);
			normalBuffer.put(x);
			normalBuffer.put(y);
			normalBuffer.put(z);
		}
		normalBuffer.position(0);

        ByteBuffer uvBuf = ByteBuffer.allocateDirect(vtPointer.size() * 4*2);
        byteBuf.order(ByteOrder.nativeOrder());
        uvBuffer = byteBuf.asFloatBuffer();
        for(int i=0; i<vtPointer.size(); i++){
            float u=vt.get(vtPointer.get(i)*2);
            float v=vt.get(vtPointer.get(i)*2+1);
            uvBuffer.put(u);
            uvBuffer.put(v);
        }
        uvBuffer.position(0);
		

		ByteBuffer fBuf = ByteBuffer.allocateDirect(faces.size() * 2);
		fBuf.order(ByteOrder.nativeOrder());
		faceBuffer = fBuf.asShortBuffer();
		faceBuffer.put(toPrimitiveArrayS(faces));
		faceBuffer.position(0);
	}
	public String toString(){
		String str=new String();
		str+="\nNumber of faces:"+faces.size();
		str+="\nNumber of vnPointers:"+vnPointer.size();
		str+="\nNumber of vtPointers:"+vtPointer.size();
		return str;
	}
	public ShortBuffer getFaceBuffer() { return faceBuffer; }
	public FloatBuffer getNormalBuffer(){
		return normalBuffer;
	}
    public FloatBuffer getUVBuffer() { return uvBuffer; }

	private static short[] toPrimitiveArrayS(Vector<Short> vector){
		short[] s;
		s=new short[vector.size()];
		for (int i=0; i<vector.size(); i++){
			s[i]=vector.get(i);
		}
		return s;
	}

	public int getFacesCount() {
		return faces.size();
	}
}
