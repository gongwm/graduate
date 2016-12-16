package test

import groovy.json.JsonOutput

class A{
	def a
	def b
}

def a=new A()
a.a=1
a.b=2

def str=JsonOutput.toJson(a)
println str