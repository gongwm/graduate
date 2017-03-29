import groovy.transform.TupleConstructor

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

@TupleConstructor
class Stage{
	Map remaining
	Map existing
	Map tails

	def split(){
		def result=[]
		if(tails.size()<=1){
			result<<this
		}else{
			tails.each{
				result<<new Stage(remaining.clone(),existing.clone(),[:]<<it)
			}
		}
		result
	}

	def resolveToBranch(){
		assert tails.size()==1

		existing<<tails
		remaining.remove(tails.find{true}.key)
		while(true){
			tails=remaining.findAll{it.value[0]==tails.find{true}.value[1]}
			def count=tails.size()

			if(count==0){
				tails=[:]
				break
			}else if(count==1){
				def added=tails.find{true} // only one
				remaining.remove(added.key)
				existing<<added
			}else/* if(count>1) */{
				break
			}
		}
	}
	
	
}

def resumeIt(stages,List resolved){
	
}



def resumeResolve(chains){
	def resolved=[]
	resume(chains,resolved)
	return resolved
}

def resume(chains, List resolved){
	chains.each{
		if(!it.tails){
			resolved.add(it)
		}else{
			def found=resolve(it.remaining, it.existing, it.tails)
			if(found.tails.size()>1){
				found=splitFound(found)
				resume(found,resolved)
			}else if(found.tails.size()==0){
				resolved.add(found)
			}
		}
	}
	return resolved
}

def resolveAll(origin,start){
	return resolveChain(origin){it.key==start}
}

def resolveRemaining(remaining){
	def result=[]

	def remain=remaining.clone()

	while(remain){
		def mainChain=resolveChain(remain){true}
		result<<mainChain.existing
		remain=mainChain.remaining
	}
	result
}

def resolveChain(origin,Closure start){
	def initFound=resolve(origin,[:],origin.find(start))

	def initChains=splitFound(initFound)
	def chains=resumeResolve(initChains)
	def mainChain=chains.max{it.existing.size()}

	return mainChain
}

def resolveIt(origin,start){
	def chains=[]

	def remain=origin.clone()
	def clue=start

	while(remain){
		def mainChain=resolveChain(remain,clue)
		chains<<mainChain.existing
		remain=mainChain.remaining
		clue={true}
	}
	def res=[:]
	chains.each{
		it.each{res<<it}
	}
	return res
}



