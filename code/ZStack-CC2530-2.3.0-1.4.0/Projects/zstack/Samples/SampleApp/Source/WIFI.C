#include <ioCC2530.h>
#include "OnBoard.h"
#include <stdio.h>
#include <string.h>

#include "AF.h"
#include "OnBoard.h"
#include "OSAL_Tasks.h"
#include "SerialApp.h"
#include "ZDApp.h"
#include "ZDObject.h"
#include "ZDProfile.h"

#include "hal_drivers.h"
#include "hal_key.h"
//#if defined ( LCD_SUPPORTED )
#include "hal_lcd.h"
//#endif
#include "hal_led.h"
#include "hal_uart.h"

#include "DHT11.h"
#include "nwk_globals.h"
#include "WIFI.H"

#include "SerialApp.h"

//函数
void clearBuff(void);
void SetWifi(void);
void dispIGTState(void);
void Init_ESP8266(void);
void Wifi_Rst(void);
void Connect_Wifi(void);
void Init_MUX(void);
void Init_Server(void);
int Send_Wifi_DATA(char *str, int len);

void Init_Wifi(void);

//命令
#define AT_C      "AT\r\n"
#define MUX_C     "AT+CIPMUX=1\r\n"
#define SERVER_C  "AT+CIPSERVER=1,5000\r\n"
#define RST_C "AT+RST\r\n"

//收到数据判断
#define Rev_F "ESP" 


#define Rev_TUL "UNLINX"
#define Rev_TLI "LINK"
#define Rev_GD "GETDATA"
#define Rev_TUL1 "Unlink"

//////////////////////////
#define IGT P0_6       // P0.6口控制IGT

/****************************************************************************
* 名    称: clearBuff()
* 功    能: 清空接收缓冲区函数
* 入口参数: 无
* 出口参数: 无
****************************************************************************/

void clearBuff(void)
{
  for(jj=0;jj<128;jj++)
  {
    Recdata[jj]=0x00;
  }
  jj=0;
}

/****************************************************************************
* 名    称: SetWifi()
* 功    能: 设置LED灯相应的IO口
* 入口参数: 无
* 出口参数: 无
****************************************************************************/
void SetWifi(void)
{
  P0DIR |= 0x40;           //P0.6定义为输出
  IGT = 1;                 //高电平复位
  Delay_ms(100);
  IGT = 0;                 //低电平工作
}


/****************************************************************************
* 名    称: dispIGTState()
* 功    能: 检测到wifi模块的启动或者停止
* 入口参数: 无
* 出口参数: 无
****************************************************************************/
void dispIGTState(void)
{
  if(IGT>0) //监测WIFI模块复位管脚
  {
     HalLcdWriteString( "IGTH", HAL_LCD_LINE_2 );
  }
  else
  {
    HalLcdWriteString( "IGTL", HAL_LCD_LINE_2 ); 
  }                
}

void Init_ESP8266(void)
{
  HalUARTWrite(0x00, "Init_ESP8266\r\n", strlen("Init_ESP8266\r\n"));
}


void Wifi_Rst(void)
{

  clearBuff();
  HalUARTWrite(0x00, "AT+RST\r\n", 8);
 
  Delay_ms(100);
/*
  if(strstr((char const *)Recdata,"OK")==NULL)
  {
      HalLcdWriteString( "Wifi Reset....", HAL_LCD_LINE_2 );
       clearBuff();
       Delay_ms(5000);

  }
  else
  {
      HalLcdWriteString("Wifi Reset[OK]", HAL_LCD_LINE_2);
       clearBuff();
       Delay_ms(5000);
  }
      */
}


void Connect_Wifi(void)
{
  
  clearBuff();
//  Print_Str(AT_C);
  HalUARTWrite (0x00, AT_C, strlen(AT_C));
   Delay_ms(50);
 
 /* 
  if(strstr((char const *)Recdata,"OK")==NULL)
  {
      HalLcdWriteString( "connect.......", HAL_LCD_LINE_2 );
      Delay_ms(500);
  }
  else
  {
      HalLcdWriteString("connect...[OK]", HAL_LCD_LINE_2);
      Delay_ms(500);
  } */
  while(strstr((char const *)Recdata,"OK")==NULL)
  {
//    LED1=1;
//    DelayMS(500);
//    LED1=0;
//    LCD_P8x16Str(8,2,"connect.......");
    HalLcdWriteString( "connect.......", HAL_LCD_LINE_2 );
    clearBuff();
 //   Print_Str(AT_C);
    HalUARTWrite (0x00, AT_C, strlen(AT_C));
 //   DelayMS(500);
  } 
 // LCD_P8x16Str(8,2,"connect...[OK]");
//  HalLcdWriteString( "connect...[OK]", HAL_LCD_LINE_2 );
 // LED1=0;
  
}



void Init_MUX(void)
{ 
  clearBuff();
//  Print_Str(MUX_C);
  HalUARTWrite (0x00, MUX_C, strlen(MUX_C));
   Delay_ms(50);
  
    if(strstr((char const *)Recdata,"OK")==NULL)
  {
      HalLcdWriteString( "Init MUX.......", HAL_LCD_LINE_2 );
      Delay_ms(500);
  }
  else
  {
      HalLcdWriteString("Init MUX...[OK]", HAL_LCD_LINE_2);
      Delay_ms(500);
  }
/*  
  while(strstr((char const *)Recdata,"OK")==NULL)
  {
//    LED2=1;
    DelayMS(500);
 //   LED2=0;
 //   LCD_P8x16Str(8,2,"Init MUX......");
    HalLcdWriteString( "Init MUX......", HAL_LCD_LINE_2 );
    clearBuff();
//    Print_Str(MUX_C);
    HalUARTWrite (0x00, MUX_C, strlen(MUX_C));
    DelayMS(500);
  }
//  LCD_P8x16Str(8,2,"Init MUX..[OK]");
  HalLcdWriteString( "Init MUX..[OK]", HAL_LCD_LINE_2 );
//  LED2=0;
  */
}

void Init_Server(void)
{
  
  
  clearBuff();
//  Print_Str(SERVER_C);
  HalUARTWrite (0x00,SERVER_C, strlen(SERVER_C));
   Delay_ms(50);
 
  if(strstr((char const *)Recdata,"OK")==NULL)
  {
      HalLcdWriteString( "Init Server.......", HAL_LCD_LINE_2 );
  Delay_ms(500);
  }
  else
  {
      HalLcdWriteString("Init Server...[OK]", HAL_LCD_LINE_2);
 Delay_ms(500);
  }
  
/*  
  while(strstr((char const *)Recdata,"OK")==NULL)
  {
 //   LED3=1;
    DelayMS(500);
//    LED3=0;
//    LCD_P8x16Str(8,2,"Init Server...");
 HalLcdWriteString( "Init Server...", HAL_LCD_LINE_2 );   
    clearBuff();
//    Print_Str(SERVER_C);
    HalUARTWrite (0x00,SERVER_C, strlen(SERVER_C));
    DelayMS(500);
  }
//  LCD_P8x16Str(8,2,"TCP Server[OK]");
  HalLcdWriteString( "TCP Server[OK]", HAL_LCD_LINE_2 );
//  LED3=0;*/
}

/*
int Send_Wifi_DATA(char *str, int len)
{
  char SEND_C[20];
  memset(SEND_C,0,20);
  sprintf(SEND_C,"AT+CIPSEND=%d,%d\r\n",g_thread,len);
  
  Print_Str(SEND_C);
  DelayMS(50);
  
  while(strstr((char const *)Recdata,">")==NULL)
  {
    if(g_timeout>50)
    {
        g_timeout=0;
        return -1;
    }
    DelayMS(5);
    g_timeout++;
    
  }
  Print_Str(str);
  return 0;
}
*/

/****************************************************************
主函数							
****************************************************************/


