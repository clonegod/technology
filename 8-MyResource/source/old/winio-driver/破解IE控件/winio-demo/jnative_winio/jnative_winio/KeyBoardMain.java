package test.jnative_winio;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.xvolks.jnative.Native;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.util.Callback;
import org.xvolks.jnative.util.windows.WindowsUtils;


public class KeyBoardMain extends JFrame{

	
	public KeyBoardMain(){
		
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
		new KeyBoardMain();
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
						//List<HWND> childWindos = new ArrayList<>();
						//WindowsUtils.enumerateChildWindows(User32.GetForegroundWindow(), childWindos);
						/*for(HWND hwnd : childWindos) {
							System.out.println(hwnd.getValueAsString());
							//User32.SendMessage(hWnd, Msg, wParam, lParam);
						}*/
						VirtualKeyBoard.KeyPress(VKMapping.toScanCode(this.key));
						System.out.println(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date())+"\tsend key:"+this.key);
					}
					sleep(500+new Random().nextInt(500-300)+new Random().nextInt(500-300));
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
		JTextField windowName=new JTextField("Internet Explorer", 10);
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
