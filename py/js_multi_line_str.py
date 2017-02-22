import sys

if __name__=='__main__':
	args=sys.argv
	if(len(args)<2):
		print('Usage: drag file to this py file')
		print('Output: multi-line js string')
		input()
	
	with open(args[1],'r+') as f:
		f.write('\n\n')
		for line in f:
			f.write("'"+line.strip()+"'+\n")

