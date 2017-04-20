package hust.hx.simulation.demo.block

import hust.hx.simulation.util.PrintUtil

def fitness(List origin,List output){
	def res=0.0
	for(int i=0;i<output.size();++i){
		res+=(output[i]-origin[i])**2
	}
	return res
}

def rs=new RegularSystem(0.8, 3.36, 0.2, 0.05)
rs.simulate()
def out0=rs.output


def param=[]
def fit=[]
(1..1000).collect{it*5/1000}.each{
	def rs1=new RegularSystem(0.8, it, 0.2, 0.05)
	rs1.simulate()
	def out1=rs1.output
	def fitness=fitness(out0,out1)
	println "$it $fitness"
	param<<it
	fit<<fitness
}

PrintUtil.print{pw->
	param.eachWithIndex {v,i->
		pw.println("$v ${fit[i]}")
	}
}