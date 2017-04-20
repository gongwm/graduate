package hust.hx.simulation.demo.block

import java.util.List

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

def rs1=new RegularSystem(1, 5, 0.2, 0.05)
rs1.simulate()
def out1=rs1.output

println out1.size()
println out0.size()
println fitness(out0,out1)