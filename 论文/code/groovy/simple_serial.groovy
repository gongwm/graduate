def dt=0.01
def totalTime=60

def n=totalTime/dt

def time=[]
def input=[]
(1..n).eachWithIndex{a,idx->
time+=idx*dt
input+=1
}

def ty1=0.1

def ty=0.5

def out1=[]
out1[0]=0

def out2=[]
out2[0]=0

(1..n).eachWithIndex{a,k->
out1[k]=ty1/(ty1+dt)*out1[k-1]+
          dt/(ty1+dt)*input[k];
out1[k]=out1[k].toDouble().trunc(4)
out2[k]=(out2[k-1]+1/ty*dt*out1[k]).toDouble().trunc(4)
}

println out1[1..10]
println out2[90..100]

def pw=new PrintWriter(new File('out.txt'))
time.eachWithIndex{a,k->
pw.println("${time[k]} ${out2[k]}")
}
pw.flush()
pw.close()

