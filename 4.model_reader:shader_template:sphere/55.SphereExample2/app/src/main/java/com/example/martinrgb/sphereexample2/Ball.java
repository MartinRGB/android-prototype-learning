package com.example.martinrgb.sphereexample2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by MartinRGB on 2017/5/12.
 */

public class Ball {
    private IntBuffer vertexBuffer;  //顶点坐标数据缓冲
    private IntBuffer nomalBuffer;  //顶点法向量数据缓冲
    private ByteBuffer indexBuffer; //顶点构建索引数据缓冲
    public float angleX,angleY;  //沿x轴旋转角度
    int vCount=0;
    int iCount=0;
    public Ball(int scale){
        //顶点坐标初始化数据
        final int UNIT_SIZE=10000;
        ArrayList<Integer> alVertex=new ArrayList<Integer>();
        final int angleSpan=18;          //将小球进行单位切分的角度
        for (int vAngle = -90; vAngle <= 90; vAngle=vAngle+angleSpan) {  //垂直方向angleSpan度一份
            for (int hAngle = 0; hAngle <360; hAngle=hAngle+angleSpan ) { //水平方向angleSpan度一份
                //纵向横向各到一个角度后计算对应的此点在球面上的坐标
                double xozLength=scale*UNIT_SIZE*Math.cos(Math.toRadians(vAngle));
                int x=(int) (xozLength*Math.cos(Math.toRadians(hAngle)));
                int y=(int) (xozLength*Math.sin(Math.toRadians(hAngle))) ;
                int z=(int) (scale*UNIT_SIZE*Math.sin(Math.toRadians(vAngle)));
                alVertex.add(x);
                alVertex.add(y);
                alVertex.add(z);
            }
        }
        vCount=alVertex.size()/3;  //顶点数量为坐标值数量的三分之一，因为一个顶点有三个坐标
        //将alVertix中的坐标值转存到一个int数组中
        int vertices []=new int[alVertex.size()];
        for (int i = 0; i < alVertex.size(); i++) {
            vertices[i]=alVertex.get(i);
        }
        //创建顶点坐标数据缓冲
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder()); //设置字节顺序
        vertexBuffer=vbb.asIntBuffer();  //转换成int型缓冲
        vertexBuffer.put(vertices);   //向缓冲区放入顶点坐标数据
        vertexBuffer.position(0);  //设置缓冲区起始位置

        //创建法线坐标数据缓冲
        ByteBuffer nbb=ByteBuffer.allocateDirect(vertices.length*4); //一个整型是4个字节
        nbb.order(ByteOrder.nativeOrder());  //设置字节顺序   由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        nomalBuffer=nbb.asIntBuffer(); //转换成int型缓冲
        nomalBuffer.put(vertices);      //想缓冲区放入顶点坐标数据
        nomalBuffer.position(0);         //设置缓冲区起始位置

        ArrayList<Integer> alIndex=new ArrayList<Integer>();
        int row=(180/angleSpan)+1; //球面切分的行数
        int col=360/angleSpan;  //球面切分的列数
        for (int i = 0; i < row; i++) {  //对每一行循环
            if(i>0 && i<row-1){
                //中间行
                for (int j = -1; j < col; j++) {
                    //中间行的两个相邻点与下一行的对应点构成三角形
                    int k=i*col+j;
                    alIndex.add(k+col);
                    alIndex.add(k+1);
                    alIndex.add(k);
                }
                for (int j = 0; j < col+1; j++) {
                    //中间行的两个相邻点与上一行的对应点构成三角形
                    int k=i*col+j;
                    alIndex.add(k-col);
                    alIndex.add(k-1);
                    alIndex.add(k);
                }
            }
        }
        iCount=alIndex.size();
        byte indices []=new byte[iCount];
        for (int i = 0; i < iCount; i++) {
            indices[i]=alIndex.get(i).byteValue();
        }
        //三角形构造数据索引缓冲
        indexBuffer=ByteBuffer.allocateDirect(iCount);  //由于indices是byte型的，索引不用乘以4
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    public void drawSelf(GL10 gl){
        gl.glRotatef(angleX, 1+angleY, 0, 0);  //沿x轴旋转
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);  //启用顶点坐标数组
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);  //启用顶点向量数组

        //为画笔指定顶点坐标数据
        gl.glVertexPointer(
                3 , //顶点坐标数量，三个坐标一个顶点
                GL10.GL_FIXED ,  //顶点坐标数据类型
                0, //连续顶点之间的数据间隔
                vertexBuffer //顶点坐标数据
        );
        //为画笔指定顶点向量数据
        gl.glNormalPointer(GL10.GL_FIXED, 0, nomalBuffer);
        //绘制图形
        gl.glDrawElements(
                GL10.GL_TRIANGLES,  //以三角形的方式填充
                iCount, GL10.GL_UNSIGNED_BYTE, indexBuffer);

    }

}