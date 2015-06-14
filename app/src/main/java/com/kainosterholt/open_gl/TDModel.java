package com.kainosterholt.open_gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

public class TDModel {
	private Vector<Float> v;
	private Vector<Float> vn;
	private Vector<Float> vt;

    private Vector<Short> indices;
    private Vector<Short> vtPointer;
    private Vector<Short> vnPointer;

	FloatBuffer vertexBuffer;
    ShortBuffer indexBuffer;

	public TDModel(Vector<Float> v, Vector<Float> vn, Vector<Float> vt,
                   Vector<Short> indices, Vector<Short> vnPointer, Vector<Short> vtPointer) {
		super();
		this.v = v;
		this.vn = vn;
		this.vt = vt;

        this.indices = indices;
        this.vnPointer = vnPointer;
        this.vtPointer = vtPointer;

        this.buildVertexBuffer();
        this.buildIndexBuffer();
	}

	public String toString(){
		String str=new String();
		str+="\nNumber of verts: "+v.size();
		str+="\nNumber of vns: "+vn.size();
		str+="\nNumber of vts: "+vt.size();
		str+="\n/////////////////////////\n";

		return str;
	}

	private void buildVertexBuffer(){
		ByteBuffer vBuf = ByteBuffer.allocateDirect(getVBOSize());

		vBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = vBuf.asFloatBuffer();

        for(int i=0; i < v.size() / 3; i++)
        {
            float x = v.get(i * 3);
            float y = v.get(i * 3 + 1);
            float z = v.get(i * 3 + 2);

            vertexBuffer.put(x);
            vertexBuffer.put(y);
            vertexBuffer.put(z);

            int j = 0;
            while( i != indices.get(j) )
            {
                ++j;
            }

            if( vnPointer.isEmpty() == false ) {
                float nx = vn.get(vnPointer.get(j) * 3);
                float ny = vn.get(vnPointer.get(j) * 3 + 1);
                float nz = vn.get(vnPointer.get(j) * 3 + 2);

                vertexBuffer.put(nx);
                vertexBuffer.put(ny);
                vertexBuffer.put(nz);
            }

            if( vtPointer.isEmpty() == false ) {
                float u = vt.get(vtPointer.get(j) * 2);
                float v = vt.get(vtPointer.get(j) * 2 + 1);

                vertexBuffer.put(u);
                vertexBuffer.put(v);
            }
        }

        vertexBuffer.position(0);
	}

    private void buildIndexBuffer()
    {
        ByteBuffer buff = ByteBuffer.allocateDirect(getIBOSize());

        buff.order(ByteOrder.nativeOrder());
        indexBuffer = buff.asShortBuffer();

        for(int i=0; i < indices.size(); i++)
        {
            short idx = indices.get(i);
            indexBuffer.put(idx);
        }

        indexBuffer.position(0);
    }

    public int getVBOSize()
    {
        return  getNumVerts() * (3 * 4 + 3 * 4 + 2 * 4);
    }

    public int getIBOSize()
    {
        return indices.size() * 2;
    }

    public int getNumIndices()
    {
        return indices.size();
    }

    public int getNumVerts()
    {
        return v.size() / 3;
    }

    public int getStride()
    {
        int stride = 12;
        if( vnPointer.isEmpty() == false )
            stride += 12;
        if( vtPointer.isEmpty() == false )
            stride += 8;

        return stride;
    }

}


