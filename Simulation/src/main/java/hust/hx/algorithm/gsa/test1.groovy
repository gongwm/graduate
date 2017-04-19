package hust.hx.algorithm.gsa

def u=new Universe(new Universe.NaturalLaw(){
			double judgeFitness(double[] cordinate){
				return 10**6*(cordinate[0]**2)+cordinate[1]**4+cordinate[2]**4+cordinate[3]**4+cordinate[4]**4
			}
		},new Universe.Range(-100,100),new Universe.Range(-100,100),
		new Universe.Range(-100,100),,new Universe.Range(-100,100),new Universe.Range(-100,100))

u.rockAndRoll();
println u.bestOne()
println u.bestFitness()