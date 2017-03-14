package hust.hx.simulation.demo.block

import groovy.json.JsonOutput

public class ExciterModel{
	static def exciter=[
		config:[
			type:'fixed',
			T:0.01,
			t:0.0,
			tt:10
		],
		components:[
			s1:[type:'step'],
			j1:[type:'joint',lines:[l1:'+', l10:'-']],
			b1:[type:'inertia',k:40,t:0.1],
			b2:[type:'limiter',upper:30,lower:-30],
			j2:[type:'joint',lines:[l4:'+',l7:'-']],
			b3:[type:'inertia',k:-20,t:-10],
			b4:[type:'amplifier',k:0.01],
			b5:[type:'inertia',k:1,t:1],
			b6:[type:'inertia',k:0.05,t:0.05],
			b7:[type:'scope']
		],
		lines:[
			l1:['s1', 'j1'],
			l2:['j1', 'b1'],
			l3:['b1', 'b2'],
			l4:['b2', 'j2'],
			l5:['j2', 'b3'],
			l6:['b3', 'b4'],
			l7:['b4', 'j2'],
			l8:['b3', 'b5'],
			l9:['b5', 'b6'],
			l10:['b6', 'j1'],
			l11:['b5', 'b7']]
	]
	
	static def main(args){
		println JsonOutput.toJson(exciter)
	}
}