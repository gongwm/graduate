package hust.hx.simulation.demo.block

import groovy.json.JsonSlurper
import hust.hx.simulation.util.PrintUtil
import hust.hx.util.TestUtil
import hust.hx.util.groovy.IOUtil

class SimulatorTest {
	static def main(args){
		def modelStr=IOUtil.readAsString('/hust/hx/simulation/demo/block/exciter_model.json')
		def model=new JsonSlurper().parseText(modelStr)
		println model

		Simulator sim=new Simulator()
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
