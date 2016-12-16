package zte.hx.simulation.test.block

import zte.hx.simulation.util.PrintUtil
import zte.hx.util.TestUtil

class Simulator {
	Config config
	Map<String,Block> components=[:]
	Map<String,Line> lines=[:]

	def out=[]

	static def main(args){
		Simulator sim=new Simulator()
		sim.simulate(ExciterModel.exciter)
		TestUtil.printRange(sim.out,20)
		def time=sim.config.time
		def out=sim.out

		println time.size()
		println out.size()

		PrintUtil.print{pw->
			time.eachWithIndex {v,k->
				pw.println("${time[k]} ${out[k]}")
			}
		}
	}

	def simulate(Map model){
		initSystem(model)
		
		out.add(components.b5.getOutput())
		config.iterate{i,k->
			lines.each{
				it.value.push(i,k)
			}
			out.add(components.b5.getOutput())
		}
	}
	
	private def checkValidation(){
		
	}
	
	private def adjustLine(){
		
	}

	private initSystem(Map model){
		model.components.each{
			components[it.key]=BlockFactory.create(it.value)
		}
		model.lines.each{
			lines[it.key]=new Line(
					components[it.value[0]],
					components[it.value[1]])
		}
		model.components.each{
			if(it.value.type=='joint'){
				def joint=components[it.key] as Joint
				it.value.lines.each{
					joint.addLine(lines[it.key],it.value as char)
				}
			}
		}

		config=new Config()
		config.config(model.config.T,model.config.t,model.config.tt)

		components.each{
			if(it instanceof Inertia){
				it.setConfig(config)
			}
		}
	}
}

class BlockFactory{
	static def create(Map info){
		def b
		switch(info.type){
			case 'step':b=new StepSource();break;
			case 'joint':b=new Joint();break;
			case 'inertia':b=new Inertia().config(info.k,info.t);break;
			case 'amplifier':b=new Amplifier(info.k);break;
			case 'limiter':b=new Limiter(info.upper,info.lower);break;
			default: throw new IllegalArgumentException('no such model');break;
		}
		return b;
	}
}
