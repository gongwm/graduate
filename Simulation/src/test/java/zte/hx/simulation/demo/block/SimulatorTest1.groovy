package zte.hx.simulation.demo.block

import org.springframework.core.io.ClassPathResource

import zte.hx.simulation.util.PrintUtil
import zte.hx.util.TestUtil

class SimulatorTest1 {
	static def main(args){
		Simulator sim=new Simulator()
		sim.initSystem(ExciterModel.exciter)
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
