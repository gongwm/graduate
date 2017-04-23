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
PrintUtil.print{pw->
	rs.time.eachWithIndex{t,idx->
		pw.println("$t ${rs.output[idx]}")
	}
}

def btRange=GsaRange.of(0.001, 1)
def tdRange=GsaRange.of(0.001, 5)
def tyRange=GsaRange.of(0.001, 1)
def tiRange=GsaRange.of(0.001, 1)

def sys=new ThreadLocal<RegularSystem>(){
	public RegularSystem initialValue(){
		return new RegularSystem(0.1,0.1,0.1,0.1)
	}
}

def u=new WeighedGSA({cordinate->
	def bt=cordinate[0]
	def td=cordinate[1]
	
	def s=sys.get()
	s.reset(bt,td,0.2,0.05)
	s.simulate()
	def output=s.output.clone()
	def f=fitness(origin,output)
	return f
},[btRange,tdRange])

u.configure(100,50)
TestUtil.timeIt{ u.rockAndRoll(); }

System.out.println(u.bestOne());
System.out.println("fitness: " + u.bestFitness());

def best=u.bestOne().toArray()
def rsys=new RegularSystem(best[0],best[1],0.2,0.05)
rsys.simulate()
def time=rsys.time
def output=rsys.output
PrintUtil.print{p->
	time.eachWithIndex{v,i->
		p.println("$v ${output[i]}")
	}
}
