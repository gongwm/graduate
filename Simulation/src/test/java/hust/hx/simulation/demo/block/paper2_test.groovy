package hust.hx.simulation.demo.block

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
TestUtil.timeIt{rs.simulate()}
def out0=rs.output

def rs1=new RegularSystem(0.6009873553, 4.4080055403, 0.2699913743, 0.3074793669)
rs1.simulate()
def out1=rs1.output

println 'fitness: '+fitness(out0,out1)

def time=rs1.time
def output=rs1.output
PrintUtil.print{p->
	time.eachWithIndex{v,i->
		p.println("$v ${output[i]}")
	}
}
