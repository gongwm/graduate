package zte.hx.simulation.demo

import zte.hx.util.TestUtil

/**
 * 很慢。
 * 
 * @author hx
 *
 */
class GroovyPidTest {
	static def main(args){
		TestUtil.timeIt{doIt()}
	}

	static def doIt(){
		def dt=0.01//delta t
		def t=[]//t
		(0..100).each{t+=it*dt}
//		println "t[90..100]: ${t[90..100]}"

		def e=[]//error
		t.each{e+=1}

		def pout=[]//kp
		def kp=1

		def iout=[]//ki
		def ki=1

		def dout=[]//kd
		def kd=1
		def t1d=1

		e.eachWithIndex{a,k->
			if(k==0){
				pout+=kp*e[0]
				iout+=0
				dout+=e[0]
			}else{
				if(k==1){
					dout+= (t1d/(t1d+dt)*dout[0]+
							1/(t1d+dt)*kd*(e[k]-e[k-1]));
				}else{
					dout+= (t1d /(t1d+dt) *dout[k-1] +
							1/(t1d+dt)*kd*(e[k]-2*e[k-1]+e[k-2]));
				}
				pout+=pout[k-1]+kp*(e[k]-e[k-1])
				iout+=iout[k-1]+ki*dt*(e[k])
			}
		}

		dout.eachWithIndex{value,idx->
			dout[idx]=value.toDouble().trunc(5)
		}
//		println "pout[0..10]: ${pout[0..10]}"
//		println "iout[90..100]: ${iout[90..100]}"
//		println "dout[90..100]: ${dout[90..100]}"
	}
}
