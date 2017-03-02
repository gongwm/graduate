package zte.hx.simulation.util

class PrintUtil {
	static def path='D:\\out.txt'

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
