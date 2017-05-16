package hust.hx.simulation.demo.block

import hust.hx.algorithm.gsa.ClassicGSA
import hust.hx.algorithm.gsa.WeighedGSA
import hust.hx.algorithm.gsa.ClassicGSA.Range as GsaRange
import hust.hx.simulation.util.PrintUtil
import hust.hx.util.TestUtil


def fitness(List origin,List output){
	def res=0.0
	for(int i=0;i<output.size();++i){
		res+=(output[i]-origin[i])**2
	}
	return res
}

def rs=new RegularSystem(0.8, 3.36, 0.2, 0.05)
rs.simulate()
def origin=rs.output

def u=new ClassicGSA({cordinate->
	def td=cordinate[0]
	def sys=new RegularSystem(0.8,td,0.2,0.05)
	sys.simulate()
	def output=sys.output
	def f=fitness(origin,output)
	return f
},[GsaRange.of(0.01, 5)])
u.configure(100,10)
TestUtil.timeIt{ u.rockAndRoll(); }


System.out.println(u.bestOne());
System.out.println("fitness: " + u.bestFitness());
def rsys=new RegularSystem(0.8,u.bestOne()[0],0.2,0.05)
rsys.simulate()
def time=rsys.time
def output=rsys.output
PrintUtil.print{p->
	time.eachWithIndex{v,i->
		p.println("$v ${output[i]}")
	}
}
