def origin=[
	l1:['s1', 'j1'],
	l10:['b6', 'j1'],
	l11:['b5', 'b7'],
	l2:['j1', 'b1'],
	l3:['b1', 'b2'],
	l4:['b2', 'j2'],
	l5:['j2', 'b3'],
	l6:['b3', 'b4'],
	l7:['b4', 'j2'],
	l8:['b3', 'b5'],
	l9:['b5', 'b6']]

def resolve(remaining,existing,tails){
	def remain=remaining.clone()
	def exist=existing.clone()

	exist<<tails
	remain.remove(tails.find{true}.key)
	while(true){
		tails=remain.findAll{it.value[0]==tails.find{true}.value[1]}
		def count=tails.size()

		if(count==0){
			tails=[:]
			break
		}else if(count==1){
			def added=tails.find{true} // only one
			remain.remove(added.key)
			exist<<added
		}else/* if(count>1) */{
			break
		}
	}
	return [remaining:remain,existing:exist,tails:tails]
}

def splitFound(found){
	def chains=[]
	if(found.tails && found.remaining){
		found.tails.each{
			chains << [remaining:found.remaining.clone(), existing:found.existing.clone(), tails:[:]<<it]
		}
	}else{
		chains<<found
	}
	return chains
}

def resumeReso(chains, List resolved){
	chains.each{
		if(!it.tails){
			resolved.add(it)
		}else{
			def found=resolve(it.remaining, it.existing, it.tails)
			if(found.tails.size()>1){
				found=splitFound(found)
				resumeReso(found,resolved)
			}else if(found.tails.size()==0){
				resolved.add(found)
			}
		}
	}
	return resolved
}

def resumeResolve(chains){
	def resolved=[]
	resumeReso(chains,resolved)
	return resolved
}

def resolveAll(origin,start){
	def initFound=resolve(origin,[:],origin.find{it.key==start})
	def initChains=splitFound(initFound)
	def chains=resumeResolve(initChains)

	def mainChain=chains.max{it.existing.size()}

	return mainChain
}

def resolveRemaining(remaining){
	def result=[]

	def initFound=resolve(remaining,[:],remaining.find{true})
	def initChains=splitFound(initFound)
	println initChains
	def chains=resumeResolve(initChains)
	result<<chains
}

def mainChain=resolveAll(origin,'l1')
println mainChain
println 'done'
println resolveRemaining(mainChain.remaining)


