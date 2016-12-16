package zte.hx.simulation.test.block

class Simulator {
	def components=[:]
	def lines=[:]

	def simulate(Map model){
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
					joint.addLine(it[0],it[1])
				}
			}
		}
		
		Config config=new Config()
		config.T=model.config.T
		config.T=model.config.t
		config.tt=model.config.tt
		
		components.each{
			if(it instanceof Inertia){
				it.config=config
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
			default: throw new IllegalArgumentException('no such model');break;
		}
		return b;
	}
}
