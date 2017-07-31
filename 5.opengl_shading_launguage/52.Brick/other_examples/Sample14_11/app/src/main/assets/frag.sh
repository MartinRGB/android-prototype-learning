precision mediump float;
varying vec2 mcLongLat;//���մӶ�����ɫ�������Ĳ���
void main()                         
{                       
   vec3 bColor=vec3(0.678,0.231,0.129);//ש�����ɫ
   vec3 mColor=vec3(0.763,0.657,0.614);//�������ɫ
   vec3 color;
   
   //���㵱ǰλ����������ż����
   int row=int(mod((mcLongLat.y+90.0)/12.0,2.0));
   //���㵱ǰ�Ƿ��ڴ��е�ש�鴹ֱ�����еĸ�������
   float ny=mod(mcLongLat.y+90.0,12.0);
   //��ż����xƫ��
   float oeoffset=0.0;
   //���㵱ǰ�Ƿ��ڴ��е�ש��ˮƽ�����еĸ�������
   float nx;
   
   if(ny>10.0)
   {//���ڴ��е�ש�鴹ֱ������
     color=mColor;
   }
   else
   {//�ڴ��е�ש�鴹ֱ������
     if(row==1)
     {//��Ϊ�������������ƫ��
        oeoffset=11.0;
     }
     //���㵱ǰ�Ƿ��ڴ��е�ש��ˮƽ�����еĸ�������
     nx=mod(mcLongLat.x+oeoffset,22.0);
     if(nx>20.0)
     {//���ڴ��е�ש��ˮƽ������
        color=mColor;
     }
     else
     {//�ڴ��е�ש��ˮƽ������
        color=bColor;
     }
   } 
   //�����������ɫ����ƬԪ
   gl_FragColor=vec4(color,0);
}     