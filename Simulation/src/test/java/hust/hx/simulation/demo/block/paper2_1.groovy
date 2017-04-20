package hust.hx.simulation.demo.block

import hust.hx.algorithm.gsa.Universe
import hust.hx.simulation.util.PrintUtil
import hust.hx.algorithm.gsa.Universe.Range as GsaRange
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
PrintUtil.print{pw->
	rs.time.eachWithIndex{t,idx->
		pw.println("$t ${rs.output[idx]}")
	}
}

def btRange=GsaRange.of(0, 1)
def tdRange=GsaRange.of(0.01, 5)
def tyRange=GsaRange.of(0, 1)
def tiRange=GsaRange.of(0, 1)
Universe u=new Universe({cordinate->
	def td=cordinate[0]
	def sys=new RegularSystem(0.8,td,0.2,0.05)
	sys.simulate()
	def output=sys.output
	def f=fitness(origin,output)
	return f
},tdRange)
u.configure(100,10)
TestUtil.timeIt{ u.rockAndRoll(); }

System.out.println(u.bestOne());
System.out.println("fitness: " + u.bestFitness());

def rsys=new RegularSystem(0.8,u.bestOne().toArray()[0],0.2,0.05)
rsys.simulate()
def time=rsys.time
def output=rsys.output
PrintUtil.print{p->
	time.eachWithIndex{v,i->
		p.println("$v ${output[i]}")
	}
}





