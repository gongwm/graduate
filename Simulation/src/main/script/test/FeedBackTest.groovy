package test

def dt=0.01

def totalTime=60//s

def n=totalTime/dt

def time=[]
def input=[]
(0..n).eachWithIndex {v,idx->
	time+=dt*idx;
	input+=1.0;
}

def K=1.0
def T=1.0

def output=[]
output[0]=0

(1..n).eachWithIndex {v,k->
	def err=input[k]-output[k]
}





































