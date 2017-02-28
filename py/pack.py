""" 
concat mutil js files into one single js file.
remove extra white spaces.
"""
import sys
from pathlib import Path

def compress(files,skip=['//','/*','*/','*'],
                   outfile='out.js',
				   exclude_files=['lodash','jquery-3.1.1.min','snap.svg','.html']):
	def should_include(line):
		return not any([line.strip().startswith(s) for s in skip])
	
	def exclude_inline_comment(line):
		line=line.strip()
		idx=line.find('//')
		if idx==-1:
			idx=line.find('/*')
		return line if idx==-1 else line[:idx]
	
	with open(outfile,'w',encoding='utf-8') as out:
		filtered_files=[file for file in files 
                            if not any([ef in file[file.rfind('\\'):] for ef in exclude_files])]
		print(filtered_files)
		for file in filtered_files:
			p=Path(file)
			if p.is_file() and p.suffix=='.js':
				with open(file,'r',encoding='utf-8') as f:
					out.write(''.join([exclude_inline_comment(line) for line in f if should_include(line)]))
			elif p.is_dir():
				compress([str(file) for file in p.iterdir()],skip,outfile,exclude_files)
				return None
			else:
				pass
		
if __name__=='__main__':
	args=sys.argv
	
	if(len(args)<2):
		print('Usage: drag file or dir to this py file')
		print('Output: compressed js file')
		sys.exit(1)

	files=args[1:]
	compress(files)

