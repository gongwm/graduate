package zte.hx.simulation.util

class PrintUtil {
	static def path='C:\\Users\\Administrator.WIN7U-20131225W\\Desktop\\out.txt'
	//static def path='C:\\Users\\Administrator\\Desktop\\out.txt'

	static def printTo(path,Printer p){
		def pw=new PrintWriter(path)
		p.print(pw)
		pw.flush()
		pw.close()
	}

	static def print(Printer p){
		printTo(path,p)
	}
}

@FunctionalInterface
interface Printer{
	void print(PrintWriter pw)
}
