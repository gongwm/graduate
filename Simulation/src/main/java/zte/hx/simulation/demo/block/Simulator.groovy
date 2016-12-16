package zte.hx.simulation.demo.block

import zte.hx.simulation.util.PrintUtil
import zte.hx.util.TestUtil

class Simulator {
	Config config
	Map<String,Block> components=[:]
	Map<String,Line> lines=[:]

	private initSystem(Map model){
		config=new Config()
		config.config(model.config.T,model.config.t,model.config.tt)
		
		model.components.each{
			components[it.key]=BlockFactory.create(it.value)
		}
		
		model.lines.each{
			def start=components[it.value[0]]
			def end=components[it.value[1]]
			def line=new Line(start,end)
			lines[it.key]=line
			if(end instanceof Scope){
				line.push(0,config.T)
			}
		}
		model.components.each{
			if(it.value.type=='joint'){
				def joint=components[it.key] as Joint
				it.value.lines.each{
					joint.addLine(lines[it.key],it.value as char)
				}
			}
		}

		components.each{
			if(it instanceof Inertia){
				it.setConfig(config)
			}
		}
	}

	def simulate(){
		config.iterate{i,k->
			lines.each{
				it.value.push(i,k)
			}
		}
	}

	private def checkValidation(){
	}

	private def adjustLine(){
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
			case 'scope':b=new Scope();break;
			default: throw new IllegalArgumentException('no such model');break;
		}
		return b;
	}
}
