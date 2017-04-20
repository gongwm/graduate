package hust.hx.algorithm.gsa

class GSATestUtil {
	static def nlist(obj,int n){
		def res=[]
		n.times{ res<<obj }
		res
	}
	
	
	static void main(args){
		println nlist('sf',3)
	}
	
}




