package serialPort;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import serialException.*;

public class DataView extends JFrame {
	private JPanel jp=new JPanel();
	private static final long serialVersionUID = 1L;


	private List<String> commList = null;	//保存可用端口号
	private SerialPort serialPort = null;	//保存串口对象
	
	private Font font = new Font("微软雅黑", Font.BOLD, 25);
	
	private JLabel teml = new JLabel("温度：", JLabel.CENTER);
	private JLabel tem = new JLabel("暂无数据", JLabel.CENTER);	//温度
	
	private JLabel huml = new JLabel("湿度：", JLabel.CENTER);	
	private JLabel hum = new JLabel("暂无数据", JLabel.CENTER);	//湿度
	
	//private JLabel pa = new JLabel("暂无数据", JLabel.CENTER);	//压强
	//private JLabel rain = new JLabel("暂无数据", JLabel.CENTER);	//雨量
	//private JLabel win_sp = new JLabel("暂无数据", JLabel.CENTER);	//风速
	//private JLabel win_dir = new JLabel("暂无数据", JLabel.CENTER);	//风向
	
	private JLabel lightsl = new JLabel("灯光：", JLabel.CENTER);	
	private JLabel lights = new JLabel("暂无数据",JLabel.CENTER);//灯光
	private JLabel lightsimg = new JLabel();	
	
	private JLabel comml = new JLabel("串口号:");
	private Choice commChoice = new Choice();	//串口选择（下拉框）
	private JLabel bpsl = new JLabel("波特率:");
	private Choice bpsChoice = new Choice();	//波特率选择
	
	private Button openSerialButton = new Button("打开串口");
	
	Image offScreen = null;	//重画时的画布
	
	/**
	 * 类的构造方法
	 * @param client
	 */
	public DataView(Client client) {
		//dataFrame();
		commList = SerialTool.findPort();	//程序初始化时就扫描一次有效串口
	}
	
	/**
	 * 主菜单窗口显示；
	 * 添加Label、按钮、下拉条及相关事件监听；
	 */
	public void dataFrame() {
		this.setBounds(Client.LOC_X, Client.LOC_Y, Client.WIDTH, Client.HEIGHT);
		this.setTitle("串口通信           ————南京邮电大学【基于情景感知的家居智能控制系统】项目组");
		jp.setBackground(Color.white);
		jp.setLayout(null);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				if (serialPort != null) {
					//程序退出时关闭串口释放资源
					SerialTool.closePort(serialPort);
				}
				System.exit(0);
			}
			
		});
		
		teml.setBounds(40, 103, 100, 50);
		teml.setOpaque(true);
		teml.setBackground(Color.white);
		teml.setFont(font);
		teml.setForeground(Color.black);
		jp.add(teml);
		
		tem.setBounds(140, 103, 225, 50);
		tem.setOpaque(true);
		tem.setBackground(Color.black);
		tem.setFont(font);
		tem.setForeground(Color.white);
		jp.add(tem);
		
		huml.setBounds(420, 103, 100, 50);
		huml.setOpaque(true);
		huml.setBackground(Color.white);
		huml.setFont(font);
		huml.setForeground(Color.black);
		jp.add(huml);
		
		hum.setBounds(520, 103, 225, 50);
		hum.setOpaque(true);
		hum.setBackground(Color.black);
		hum.setFont(font);
		hum.setForeground(Color.white);
		jp.add(hum);
		
		/*pa.setBounds(140, 193, 225, 50);
		pa.setBackground(Color.black);
		pa.setFont(font);
		pa.setForeground(Color.white);
		add(pa);

		rain.setBounds(520, 193, 225, 50);
		rain.setBackground(Color.black);
		rain.setFont(font);
		rain.setForeground(Color.white);
		add(rain);
		
		win_sp.setBounds(140, 283, 225, 50);
		win_sp.setBackground(Color.black);
		win_sp.setFont(font);
		win_sp.setForeground(Color.white);
		add(win_sp);
		
		win_dir.setBounds(520, 283, 225, 50);
		win_dir.setBackground(Color.black);
		win_dir.setFont(font);
		win_dir.setForeground(Color.white);
		add(win_dir);*/
		
		lightsl.setBounds(40, 193, 100, 50);
		lightsl.setOpaque(true);
		lightsl.setBackground(Color.white);
		lightsl.setFont(font);
		lightsl.setForeground(Color.black);
		jp.add(lightsl);
		
		lights.setBounds(140, 193, 225, 50);
		lights.setOpaque(true);
		lights.setBackground(Color.BLACK);
		lights.setFont(font);
		lights.setForeground(Color.white);
		jp.add(lights);
		
		lightsimg.setBounds(400, 193, 50, 50);
		setIcon("image/lightsOff.png",lightsimg);
		jp.add(lightsimg);

		
		comml.setBounds(80, 383, 50, 50);
		jp.add(comml);
		//添加串口选择选项
		commChoice.setBounds(160, 397, 200, 200);
		//检查是否有可用串口，有则加入选项中
		if (commList == null || commList.size()<1) {
			JOptionPane.showMessageDialog(null, "没有搜索到有效串口！", "错误", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			for (String s : commList) {
				commChoice.add(s);
			}
		}
		jp.add(commChoice);
		
		bpsl.setBounds(466,383, 50, 50);
		jp.add(bpsl);
		//添加波特率选项
		bpsChoice.setBounds(526, 396, 200, 200);
		bpsChoice.add("115200");
		bpsChoice.add("19200");
		bpsChoice.add("14400");
		bpsChoice.add("9600");
		bpsChoice.add("4800");
		bpsChoice.add("2400");
		bpsChoice.add("1200");
		jp.add(bpsChoice);
		
		//添加打开串口按钮
		openSerialButton.setBounds(250, 490, 300, 50);
		openSerialButton.setBackground(Color.WHITE);
		openSerialButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
		openSerialButton.setForeground(Color.BLACK);
		jp.add(openSerialButton);
		
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		int windowsWedth = Client.WIDTH;
		int windowsHeight = Client.HEIGHT;
		this.setBounds((width - windowsWedth) / 2,(height - windowsHeight) / 2, windowsWedth, windowsHeight);
		setSize(windowsWedth,windowsHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(jp);
		setVisible(true);
		
		//添加打开串口按钮的事件监听
		openSerialButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				//获取串口名称
				String commName = commChoice.getSelectedItem();			
				//获取波特率
				String bpsStr = bpsChoice.getSelectedItem();
				
				//检查串口名称是否获取正确
				if (commName == null || commName.equals("")) {
					JOptionPane.showMessageDialog(null, "没有搜索到有效串口！", "错误", JOptionPane.INFORMATION_MESSAGE);		
					commList = SerialTool.findPort();	//程序初始化时就扫描一次有效串口
				}
				else {
					//检查波特率是否获取正确
					if (bpsStr == null || bpsStr.equals("")) {
						JOptionPane.showMessageDialog(null, "波特率获取错误！", "错误", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						//串口名、波特率均获取正确时
						int bps = Integer.parseInt(bpsStr);
						try {
							
							//获取指定端口名及波特率的串口对象
							serialPort = SerialTool.openPort(commName, bps);
							//在该串口对象上添加监听器
							SerialTool.addListener(serialPort, new SerialListener());
							//监听成功进行提示
							JOptionPane.showMessageDialog(null, "监听成功，稍后将显示监测数据！", "提示", JOptionPane.INFORMATION_MESSAGE);
							openSerialButton.setBackground(Color.lightGray);
							//openSerialButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
							openSerialButton.setForeground(Color.darkGray);
						} catch (SerialPortParameterFailure | NotASerialPort | NoSuchPort | PortInUse | TooManyListeners e1) {
							//发生错误时使用一个Dialog提示具体的错误信息
							JOptionPane.showMessageDialog(null, e1, "错误", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
				
			}
		});
		
		
		this.setResizable(false);
		
		
	}
	
	/**
	 * 以内部类形式创建一个串口监听类
	 * @author zhong
	 *
	 */
	private class SerialListener implements SerialPortEventListener {
		
		/**
		 * 处理监控到的串口事件
		 */
	    public void serialEvent(SerialPortEvent serialPortEvent) {
	    	
	        switch (serialPortEvent.getEventType()) {

	            case SerialPortEvent.BI: // 10 通讯中断
	            	JOptionPane.showMessageDialog(null, "与串口设备通讯中断", "错误", JOptionPane.INFORMATION_MESSAGE);
	            	break;

	            case SerialPortEvent.OE: // 7 溢位（溢出）错误

	            case SerialPortEvent.FE: // 9 帧错误

	            case SerialPortEvent.PE: // 8 奇偶校验错误

	            case SerialPortEvent.CD: // 6 载波检测

	            case SerialPortEvent.CTS: // 3 清除待发送数据

	            case SerialPortEvent.DSR: // 4 待发送数据准备好了

	            case SerialPortEvent.RI: // 5 振铃指示

	            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
	            	break;
	            
	            case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
	            	
	            	//System.out.println("found data");
					byte[] data = null;
					
					try {
						if (serialPort == null) {
							JOptionPane.showMessageDialog(null, "串口对象为空！监听失败！", "错误", JOptionPane.INFORMATION_MESSAGE);
						}
						else {
							data = SerialTool.readFromPort(serialPort);	//读取数据，存入字节数组
							System.out.println(new String(data));
							
							//自定义解析过程
							if (data == null || data.length < 1) {	//检查数据是否读取正确	
								JOptionPane.showMessageDialog(null, "读取数据过程中未获取到有效数据！请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
								System.exit(0);
							}
							else {
								
								String dataOriginal = new String(data);	//将字节数组数据转换位为保存了原始数据的字符串
								//System.out.println(dataOriginal);
								//String dataValid = "";	//有效数据（用来保存原始数据字符串去除最开头*号以后的字符串）
								String[] elements = null;	//用来保存按空格拆分原始字符串后得到的字符串数组	
								//解析数据
								//if (dataOriginal.charAt(0) == '*') {	//当数据的第一个字符是*号时表示数据接收完成，开始解析							
									//dataValid = dataOriginal.substring(1);
									elements = dataOriginal.split(" ");
									if (elements == null || elements.length < 1) {	//检查数据是否解析正确
										JOptionPane.showMessageDialog(null, "数据解析过程出错，请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
										System.exit(0);
									}
									else {
										try {
											//更新界面Label值
											/*for (int i=0; i<elements.length; i++) {
												System.out.println(elements[i]);
											}*/
											//System.out.println("win_dir: " + elements[5]);
											
											if(elements[0].equals("ON"))
											{
												lights.setText("开灯");
												lights.setBackground(Color.white);
												lights.setForeground(Color.black);
												setIcon("image/lightsOn.png",lightsimg);
											}
											else if(elements[0].equals("OFF"))
											{
												lights.setText("关灯");
												lights.setBackground(Color.black);
												lights.setForeground(Color.white);
												setIcon("image/lightsOff.png",lightsimg);
											}
											else
											{
												tem.setText(elements[0] + " ℃");
												hum.setText(elements[1] + " %");
											}
										} catch (ArrayIndexOutOfBoundsException e) {
											JOptionPane.showMessageDialog(null, "数据解析过程出错，更新界面数据失败！请检查设备或程序！", "错误", JOptionPane.INFORMATION_MESSAGE);
											//System.exit(0);
										}
									//}	
								}
							}
							
						}						
						
					} catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
						JOptionPane.showMessageDialog(null, e, "错误", JOptionPane.INFORMATION_MESSAGE);
						System.exit(0);	//发生读取错误时显示错误信息后退出系统
					}	
		            
					break;
	
	        }

	    }

	}
	public void setIcon(String file,JLabel com)
	{ 
		ImageIcon ico=new ImageIcon(file);  
		Image temp=ico.getImage().getScaledInstance(com.getWidth(),com.getHeight(),ico.getImage().SCALE_DEFAULT);  
		ico=new ImageIcon(temp);
		com.setIcon(ico);  
		com.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
}
