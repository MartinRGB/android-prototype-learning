#!/usr/bin/env bash
uniform mat4 uMVPMatrix; //�ܱ任����
attribute vec3 aPosition;  //����λ��
attribute vec2 aLongLat;   //���㾭γ��
varying vec2 mcLongLat;
void main()     
{                   
   //�����ܱ任�������˴λ��ƴ˶���λ��         		
   gl_Position = uMVPMatrix * vec4(aPosition,1); 
   //������ľ�γ�ȴ���ƬԪ��ɫ��
   mcLongLat=aLongLat;
}                      