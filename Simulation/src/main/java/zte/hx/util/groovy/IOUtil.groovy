package zte.hx.util.groovy

import org.springframework.core.io.ClassPathResource

class IOUtil {
	static def readAsString(String path){
		def input=new ClassPathResource(path).getInputStream()
		def scanner=new Scanner(input)
		def sb=new StringBuilder()
		while(scanner.hasNextLine()){
			sb.append(scanner.nextLine())
		}
		return sb.toString()
	}
}
