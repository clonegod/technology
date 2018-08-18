package sample.winio;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import clonegod.crawler.winio.jnative.User32;
import clonegod.crawler.winio.jnative.VKMapping;
import clonegod.crawler.winio.jnative.VirtualKeyBoard;


public class KeyBoardIETest extends JFrame{

	
	/**
	 * 使用jnative调用winio的例子（32位jdk环境，因为jnative仅支持32位winio）
	 * 操作：
	 * 1. 运行main()
	 * 2. 打开notepat++
	 * 3. 勾选启动，将焦点定位到notepad++，通过winio发送的字符消息就会显示在notepad++中了
	 */
	private static final long serialVersionUID = 1L;

	public KeyBoardIETest(){
		
		this.setTitle("驱动级按键精灵");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());
		this.add(new MyPanel());
		this.add(new MyPanel());
		this.add(new MyPanel());
		this.setSize(500, 200);
		this.setVisible(true);
		this.setResizable(false);
	}
	public static void main(String[] args) {
		new KeyBoardIETest();
	}
	
	
	class HotKeyThread extends Thread{
		private String key;
		private String windowName;
		private int time;
		private boolean isRunning ;
		public HotKeyThread(String key, int time,String windowName){
			this.key=key;
			this.time=time;
			this.windowName=windowName;
			this.isRunning=true;
		}
		@Override
		public void run() {
			while(isRunning){
				try {
					System.out.println(this.windowName + "\t" + User32.GetWindowText(User32.GetForegroundWindow()));
					
					if(User32.GetWindowText(User32.GetForegroundWindow()).contains(this.windowName)){
						VirtualKeyBoard.KeyPress(VKMapping.toScanCode(this.key));
						System.out.println(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date())+"\tsend key:"+this.key);
					}
					sleep(1000);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					isRunning =false;
				}
			}
			
		}
		
		public void stopRun(){
			isRunning =false;
		}
	}
	
	class MyPanel extends JPanel{
		JCheckBox checkbox=new JCheckBox("启用");
		
		JFormattedTextField time=new JFormattedTextField(java.text.NumberFormat.getInstance());
		
		// 设置需要匹配的窗口名称关键字
//		JTextField windowName=new JTextField("Internet Explorer", 10);
		JTextField windowName=new JTextField("Notepad", 10);
		
		JComboBox box=new JComboBox(VKMapping.getKeyName().toArray());
		
		public MyPanel(){
			this.setLayout(new FlowLayout());
			this.add(checkbox);
			this.add(new JLabel("时间(ms)"));
			time.setColumns(10);
			time.setText("1");
			this.add(time);
			this.add(new JLabel("程序名"));
			this.add(windowName);
			this.add(new JLabel("按键"));
			this.add(box);
			checkbox.addActionListener(new ActionListener() {
				HotKeyThread th;
				@Override
				public void actionPerformed(ActionEvent e) {
					if(checkbox.isSelected()){
						th=new HotKeyThread(box.getSelectedItem().toString(),Integer.parseInt(time.getText()),windowName.getText());
//						th.setPriority(Thread.MAX_PRIORITY);
						th.start();
					}else{
						if(th!=null){
							th.stopRun();
							th=null;
						}
						
					}
				}
			});
		}
	}
}
