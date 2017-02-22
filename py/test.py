import sys

if __name__=='__main__':
  print('here: '+sys.argv[1])
  print(len(sys.argv))
  for arg in sys.argv:
    print(arg)

  input()