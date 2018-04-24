package serialPort;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import serialException.ExceptionWriter;

/**
 * ������
 * @author zhong
 *
 */
public class Client extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ���������
	 */
	public static final int WIDTH = 800;
	
	/**
	 * �������߶�
	 */
	public static final int HEIGHT = 620;
	
	/**
	 * ����������λ�ã������꣩
	 */
	public static final int LOC_X = 200;
	
	/**
	 * ����������λ�ã������꣩
	 */
	public static final int LOC_Y = 70;

	Color color = Color.WHITE; 
	Image offScreen = null;	//����˫����
	
	
	//����������
	DataView dataview = new DataView(this);	//�������ࣨ��ʾ�����������壩

	/**
	 * ������
	 * @param args	//
	 */
	public static void main(String[] args) {
		new Client().launchFrame();	
	}
	
	/**
	 * ��ʾ������
	 */
	public void launchFrame() {
		this.setBounds(LOC_X, LOC_Y, WIDTH, HEIGHT);	//�趨������������ֵ�λ��
		this.setTitle("����ͨ��           ���������Ͼ��ʵ��ѧ�������龰��֪�ļҾ����ܿ���ϵͳ����Ŀ��");	//���ó������
		//this.setIconImage(icon);
		this.setBackground(Color.white);	//���ñ���ɫ
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		int windowsWedth = Client.WIDTH;
		int windowsHeight = Client.HEIGHT;
		this.setBounds((width - windowsWedth) / 2,(height - windowsHeight) / 2, windowsWedth, windowsHeight);
		setSize(windowsWedth,windowsHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			//��ӶԴ���״̬�ļ���
			public void windowClosing(WindowEvent arg0) {
				//�����ڹر�ʱ
				System.exit(0);	//�˳�����
			}
			
		});

		this.addKeyListener(new KeyMonitor());	//��Ӽ��̼�����
		this.setResizable(false);	//���ڴ�С���ɸ���
		this.setVisible(true);	//��ʾ����
			
		new Thread(new RepaintThread()).start();	//�����ػ��߳�
	}
	
	/**
	 * ���������������Ԫ��
	 */
	public void paint(Graphics g) {
		Color c = g.getColor();
		
		g.setFont(new Font("΢���ź�", Font.BOLD, 40));
		g.setColor(Color.black);
		g.drawString("�����龳��֪�ļҾ����ܿ���ϵͳ", 45, 190);
		
		g.setFont(new Font("Segoe Script", Font.BOLD, 26));
		g.setColor(Color.BLACK);
		g.drawString("B150413 12/19/22", 280, 260);
		
		g.setFont(new Font("΢���ź�", Font.BOLD, 30));
		g.setColor(Color.BLACK);
		g.drawString("�����������Enter�����������桪������", 100, 480);
	}
	
	/**
	 * ˫���巽ʽ�ػ������Ԫ�����
	 */
	public void update(Graphics g) {
		if (offScreen == null)	offScreen = this.createImage(WIDTH, HEIGHT);
		Graphics gOffScreen = offScreen.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.white);
		gOffScreen.fillRect(0, 0, WIDTH, HEIGHT);	//�ػ���������
		this.paint(gOffScreen);	//�ػ�����Ԫ��
		gOffScreen.setColor(c);
		g.drawImage(offScreen, 0, 0, null);	//���»��õĻ�����������ԭ������
	}
	
	/*
	 * �ڲ�����ʽʵ�ֶԼ����¼��ļ���
	 */
	private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if (keyCode == KeyEvent.VK_ENTER) {	//���������û��û�����enter����ִ������Ĳ���
				setVisible(false);	//��ȥ��ӭ����
				dataview.setVisible(true);	//��ʾ������
				dataview.dataFrame();	//��ʼ��������
			}
		}
		
	}
	
	
	/*
	 * �ػ��̣߳�ÿ��250�����ػ�һ�Σ�
	 */
	private class RepaintThread implements Runnable {
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					//�ػ��̳߳����׳��쳣ʱ����һ��Dialog����ʾ�쳣��ϸ��Ϣ
					String err = ExceptionWriter.getErrorInfoFromException(e);
					JOptionPane.showMessageDialog(null, err, "����", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
			}
		}
		
	}
	
}
