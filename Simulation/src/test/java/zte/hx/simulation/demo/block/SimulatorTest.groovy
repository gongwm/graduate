package zte.hx.simulation.demo.block

import groovy.json.JsonSlurper
import zte.hx.simulation.util.PrintUtil
import zte.hx.util.TestUtil
import zte.hx.util.groovy.IOUtil

class SimulatorTest {
	static def main(args){
		Simulator sim=new Simulator()

		def modelStr=IOUtil.readAsString('/zte/hx/simulation/demo/block/exciter_model.json')
		def model=new JsonSlurper().parseText(modelStr)
		println model

		sim.initSystem(model)
		TestUtil.timeIt{ sim.simulate() }

		TestUtil.printRange(sim.components.b7.data,20)
		
		def time=sim.config.time
		def out=sim.components.b7.data
		println time.size()
		println out.size()
		PrintUtil.print{pw->
			time.eachWithIndex {v,k->
				pw.println("${time[k]} ${out[k]}")
			}
		}
	}
}
