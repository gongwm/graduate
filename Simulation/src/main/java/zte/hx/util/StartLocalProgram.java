package zte.hx.util;

/**
 * 打开电脑主机安装程序
 * 
 * @author web
 * 
 */
public class StartLocalProgram {
	public static void open(String path) {
		ProcessBuilder pb = null;
		try {
			pb = new ProcessBuilder(path);
			pb.redirectErrorStream(true);
			pb.start();// 启动进程
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			pb = null;
		}
	}

	public static void main(String[] args) {
		String installpath = "notepad.exe";
		StartLocalProgram.open(installpath);
	}

}
