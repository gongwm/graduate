package hust.hx.algorithm.gsa

import static java.lang.Math.*

import hust.hx.algorithm.gsa.ClassicGSA.Range as GsaRange
import hust.hx.util.TestUtil

def r=new GsaRange(-100,100)

def range={l,h->
	new GsaRange(l,h)
}

def nlist(obj,int n){
	def res=[]
	n.times{ res<<obj }
	res
}

def tests=[
	test1:[
		{x->	10**6*(x[0]**2)+x[1..-1].inject(0){acc,v->acc+v**4}},
		nlist(r,4)
	],

	test3:[
		{x->	(x.collect{abs(it)} as Iterable).max()},
		nlist(r,10)
	],

	test10:[
		{x->
			x=(x as List)
			1/40000*x.inject(0){acc,v->acc+v**2}-
			(x as Iterable).indexed().inject(1){acc,k,v->acc*cos(v/sqrt(k+1))}+1
		},
		nlist(range(-600,600),30)
	],

	test14:[
		{x->4*x[0]**2-2.1*x[0]**4+1/3*x[0]**6+x[0]*x[1]-4*x[1]**2+4*x[1]**4},
		nlist(range(-5,5),2)
	],

	test15:[
		{x->
			def x1=x[0],x2=x[1]
			(1+(x1+x2+1)**2*(19-14*x1+3*x1**2-14*x2+6*x1*x2+3*x2**2))*
					(30+(2*x1-3*x2)**2*(18-32*x1+12*x1**2+48*x2-36*x1*x2+27*x2**2))
		},
		nlist(range(-5,5),2)]
]

def runTest={test->
	def data=tests[test]
	def u=new WeighedGSA(data[0],data[1])
	u.configure(1000,50)
	TestUtil.timeIt{u.rockAndRoll()}
}

runTest('test15')

