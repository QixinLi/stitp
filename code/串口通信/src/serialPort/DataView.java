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


	private List<String> commList = null;	//������ö˿ں�
	private SerialPort serialPort = null;	//���洮�ڶ���
	
	private Font font = new Font("΢���ź�", Font.BOLD, 25);
	
	private JLabel teml = new JLabel("�¶ȣ�", JLabel.CENTER);
	private JLabel tem = new JLabel("��������", JLabel.CENTER);	//�¶�
	
	private JLabel huml = new JLabel("ʪ�ȣ�", JLabel.CENTER);	
	private JLabel hum = new JLabel("��������", JLabel.CENTER);	//ʪ��
	
	//private JLabel pa = new JLabel("��������", JLabel.CENTER);	//ѹǿ
	//private JLabel rain = new JLabel("��������", JLabel.CENTER);	//����
	//private JLabel win_sp = new JLabel("��������", JLabel.CENTER);	//����
	//private JLabel win_dir = new JLabel("��������", JLabel.CENTER);	//����
	
	private JLabel lightsl = new JLabel("�ƹ⣺", JLabel.CENTER);	
	private JLabel lights = new JLabel("��������",JLabel.CENTER);//�ƹ�
	private JLabel lightsimg = new JLabel();	
	
	private JLabel comml = new JLabel("���ں�:");
	private Choice commChoice = new Choice();	//����ѡ��������
	private JLabel bpsl = new JLabel("������:");
	private Choice bpsChoice = new Choice();	//������ѡ��
	
	private Button openSerialButton = new Button("�򿪴���");
	
	Image offScreen = null;	//�ػ�ʱ�Ļ���
	
	/**
	 * ��Ĺ��췽��
	 * @param client
	 */
	public DataView(Client client) {
		//dataFrame();
		commList = SerialTool.findPort();	//�����ʼ��ʱ��ɨ��һ����Ч����
	}
	
	/**
	 * ���˵�������ʾ��
	 * ���Label����ť��������������¼�������
	 */
	public void dataFrame() {
		this.setBounds(Client.LOC_X, Client.LOC_Y, Client.WIDTH, Client.HEIGHT);
		this.setTitle("����ͨ��           ���������Ͼ��ʵ��ѧ�������龰��֪�ļҾ����ܿ���ϵͳ����Ŀ��");
		jp.setBackground(Color.white);
		jp.setLayout(null);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				if (serialPort != null) {
					//�����˳�ʱ�رմ����ͷ���Դ
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
		//��Ӵ���ѡ��ѡ��
		commChoice.setBounds(160, 397, 200, 200);
		//����Ƿ��п��ô��ڣ��������ѡ����
		if (commList == null || commList.size()<1) {
			JOptionPane.showMessageDialog(null, "û����������Ч���ڣ�", "����", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			for (String s : commList) {
				commChoice.add(s);
			}
		}
		jp.add(commChoice);
		
		bpsl.setBounds(466,383, 50, 50);
		jp.add(bpsl);
		//��Ӳ�����ѡ��
		bpsChoice.setBounds(526, 396, 200, 200);
		bpsChoice.add("115200");
		bpsChoice.add("19200");
		bpsChoice.add("14400");
		bpsChoice.add("9600");
		bpsChoice.add("4800");
		bpsChoice.add("2400");
		bpsChoice.add("1200");
		jp.add(bpsChoice);
		
		//��Ӵ򿪴��ڰ�ť
		openSerialButton.setBounds(250, 490, 300, 50);
		openSerialButton.setBackground(Color.WHITE);
		openSerialButton.setFont(new Font("΢���ź�", Font.BOLD, 20));
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
		
		//��Ӵ򿪴��ڰ�ť���¼�����
		openSerialButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				//��ȡ��������
				String commName = commChoice.getSelectedItem();			
				//��ȡ������
				String bpsStr = bpsChoice.getSelectedItem();
				
				//��鴮�������Ƿ��ȡ��ȷ
				if (commName == null || commName.equals("")) {
					JOptionPane.showMessageDialog(null, "û����������Ч���ڣ�", "����", JOptionPane.INFORMATION_MESSAGE);		
					commList = SerialTool.findPort();	//�����ʼ��ʱ��ɨ��һ����Ч����
				}
				else {
					//��鲨�����Ƿ��ȡ��ȷ
					if (bpsStr == null || bpsStr.equals("")) {
						JOptionPane.showMessageDialog(null, "�����ʻ�ȡ����", "����", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						//�������������ʾ���ȡ��ȷʱ
						int bps = Integer.parseInt(bpsStr);
						try {
							
							//��ȡָ���˿����������ʵĴ��ڶ���
							serialPort = SerialTool.openPort(commName, bps);
							//�ڸô��ڶ�������Ӽ�����
							SerialTool.addListener(serialPort, new SerialListener());
							//�����ɹ�������ʾ
							JOptionPane.showMessageDialog(null, "�����ɹ����Ժ���ʾ������ݣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
							openSerialButton.setBackground(Color.lightGray);
							//openSerialButton.setFont(new Font("΢���ź�", Font.BOLD, 20));
							openSerialButton.setForeground(Color.darkGray);
						} catch (SerialPortParameterFailure | NotASerialPort | NoSuchPort | PortInUse | TooManyListeners e1) {
							//��������ʱʹ��һ��Dialog��ʾ����Ĵ�����Ϣ
							JOptionPane.showMessageDialog(null, e1, "����", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
				
			}
		});
		
		
		this.setResizable(false);
		
		
	}
	
	/**
	 * ���ڲ�����ʽ����һ�����ڼ�����
	 * @author zhong
	 *
	 */
	private class SerialListener implements SerialPortEventListener {
		
		/**
		 * �����ص��Ĵ����¼�
		 */
	    public void serialEvent(SerialPortEvent serialPortEvent) {
	    	
	        switch (serialPortEvent.getEventType()) {

	            case SerialPortEvent.BI: // 10 ͨѶ�ж�
	            	JOptionPane.showMessageDialog(null, "�봮���豸ͨѶ�ж�", "����", JOptionPane.INFORMATION_MESSAGE);
	            	break;

	            case SerialPortEvent.OE: // 7 ��λ�����������

	            case SerialPortEvent.FE: // 9 ֡����

	            case SerialPortEvent.PE: // 8 ��żУ�����

	            case SerialPortEvent.CD: // 6 �ز����

	            case SerialPortEvent.CTS: // 3 �������������

	            case SerialPortEvent.DSR: // 4 ����������׼������

	            case SerialPortEvent.RI: // 5 ����ָʾ

	            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 ��������������
	            	break;
	            
	            case SerialPortEvent.DATA_AVAILABLE: // 1 ���ڴ��ڿ�������
	            	
	            	//System.out.println("found data");
					byte[] data = null;
					
					try {
						if (serialPort == null) {
							JOptionPane.showMessageDialog(null, "���ڶ���Ϊ�գ�����ʧ�ܣ�", "����", JOptionPane.INFORMATION_MESSAGE);
						}
						else {
							data = SerialTool.readFromPort(serialPort);	//��ȡ���ݣ������ֽ�����
							System.out.println(new String(data));
							
							//�Զ����������
							if (data == null || data.length < 1) {	//��������Ƿ��ȡ��ȷ	
								JOptionPane.showMessageDialog(null, "��ȡ���ݹ�����δ��ȡ����Ч���ݣ������豸�����", "����", JOptionPane.INFORMATION_MESSAGE);
								System.exit(0);
							}
							else {
								
								String dataOriginal = new String(data);	//���ֽ���������ת��λΪ������ԭʼ���ݵ��ַ���
								//System.out.println(dataOriginal);
								//String dataValid = "";	//��Ч���ݣ���������ԭʼ�����ַ���ȥ���ͷ*���Ժ���ַ�����
								String[] elements = null;	//�������水�ո���ԭʼ�ַ�����õ����ַ�������	
								//��������
								//if (dataOriginal.charAt(0) == '*') {	//�����ݵĵ�һ���ַ���*��ʱ��ʾ���ݽ�����ɣ���ʼ����							
									//dataValid = dataOriginal.substring(1);
									elements = dataOriginal.split(" ");
									if (elements == null || elements.length < 1) {	//��������Ƿ������ȷ
										JOptionPane.showMessageDialog(null, "���ݽ������̳��������豸�����", "����", JOptionPane.INFORMATION_MESSAGE);
										System.exit(0);
									}
									else {
										try {
											//���½���Labelֵ
											/*for (int i=0; i<elements.length; i++) {
												System.out.println(elements[i]);
											}*/
											//System.out.println("win_dir: " + elements[5]);
											
											if(elements[0].equals("ON"))
											{
												lights.setText("����");
												lights.setBackground(Color.white);
												lights.setForeground(Color.black);
												setIcon("image/lightsOn.png",lightsimg);
											}
											else if(elements[0].equals("OFF"))
											{
												lights.setText("�ص�");
												lights.setBackground(Color.black);
												lights.setForeground(Color.white);
												setIcon("image/lightsOff.png",lightsimg);
											}
											else
											{
												tem.setText(elements[0] + " ��");
												hum.setText(elements[1] + " %");
											}
										} catch (ArrayIndexOutOfBoundsException e) {
											JOptionPane.showMessageDialog(null, "���ݽ������̳������½�������ʧ�ܣ������豸�����", "����", JOptionPane.INFORMATION_MESSAGE);
											//System.exit(0);
										}
									//}	
								}
							}
							
						}						
						
					} catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
						JOptionPane.showMessageDialog(null, e, "����", JOptionPane.INFORMATION_MESSAGE);
						System.exit(0);	//������ȡ����ʱ��ʾ������Ϣ���˳�ϵͳ
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
