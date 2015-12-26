import os, sys

print 'foo'
print os.getcwd()
print os.path.sep

print os.path.dirname(os.path.abspath(sys.argv[0]))
#print  os.path.dirname(__file___)